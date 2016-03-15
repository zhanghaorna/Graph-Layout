import java.util.Random;

import Data.Edge;
import Data.Graph;
import Data.Point;

/**
 * @author zhr
 * @version 1.0.0
 * @date 2016年3月14日
 * @description
 */
public class KK {
	//显示区域宽度
	public int width;
	//显示区域高度
	public int height;
	//最短路径矩阵
	public int d[][];
	//节点数量
	public int num;
	//图矩阵
	public int g[][];
	//节点间的理想距离
	public double l[][];
	//节点间的弹簧力
	public double k[][];
	//节点的坐标矩阵
	public Point point[];
	//节点的delta值
	public double delta[];
	//初设的阈值
	public double value;
	
	public KK(Graph graph,int width,int height)
	{
		value = 10;
		this.width = width;
		this.height = height;
		num = graph.points.size();
		d = new int[num][num];
		g = new int[num][num];
		l = new double[num][num];
		k = new double[num][num];
		point = new Point[num];
		delta = new double[num];
		//floyd算法求最短路径矩阵
		for(int i = 0;i < num;i++)
			for(int j = 0;j < num;j++)
			{
				if(i == j)
					d[i][j] = 0;
				else
					d[i][j] = Integer.MAX_VALUE;
			}
		//将graph转为二维矩阵存储
		for(int i = 0;i < graph.edges.size();i++)
		{
			Edge edge = graph.edges.get(i);
			int u = edge.u.num - 1;
			int v = edge.v.num - 1;
			g[u][v] = 1;
			g[v][u] = 1;
			d[u][v] = 1;
			d[v][u] = 1;
		}
		//Floyd-Warshall运算
		for(int k = 0;k < num;k++)
			for(int i = 0;i < num;i++)
				for(int j = 0;j < num;j++)
				{
					if(d[i][j] > d[i][k] + d[k][j])
						d[i][j] = d[i][k] + d[k][j];
				}
		//计算最佳距离矩阵
		int maxD = 0;
		for(int i = 0;i < num;i++)
			for(int j = 0;j < num;j++)
			{
				if(d[i][j] != Integer.MAX_VALUE&&d[i][j] > maxD)
					maxD = d[i][j];
			}
		double L = ((double)width) / maxD;

		//计算节点间的弹簧力
		double K = maxD * maxD;
		for(int i = 0;i < num;i++)
			for(int j = 0;j < num;j++)
			{
				if(i != j)
				{
					l[i][j] = L * d[i][j];
					k[i][j] = K / (d[i][j] * d[i][j]);
				}
			}
		Random random = new Random();
		//初始化所有节点
		for(int i = 0;i < num;i++)
		{
			Point point = new Point(i);
			point.pos.x = random.nextInt(width);
			point.pos.y = random.nextInt(height);
			this.point[i] = point;
		}
		
		computeDelta();
		layout();
	}
	
	public void layout()
	{
		double maxDelta = delta[0];
		for(int i = 0;i < num;i++)
		{
			if(delta[i] > maxDelta)
				maxDelta = delta[i];
		}
	}
	
	public void computeDelta()
	{
		for(int i = 0;i < num;i++)
		{
			double EX = 0;
			double EY = 0;
			for(int j = 0;j < num;j++)
			{
				if(i != j)
				{
					double v1 = point[i].pos.x - point[j].pos.x;
					double v2 = point[i].pos.y - point[j].pos.y;
					double v3 = l[i][j] * v1;
					double v4 = l[i][j] * v2;
					double v5 = Math.sqrt(v1*v1 + v2*v2);
					EX += k[i][j]*(v1 - v3 / v5);
					EY += k[i][j]*(v2 - v4 / v5);
				}
			}
			delta[i] = Math.sqrt(EX * EX + EY * EY);
		}

	}
	


}
