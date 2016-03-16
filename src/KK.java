import java.util.HashSet;
import java.util.Iterator;
import java.util.Random;
import java.util.Set;

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
	//选中的最大delta值节点
	public int vertex;
	//被选中节点的delta值
	public double choose_delta;
	//最大迭代次数
	public int count;
	//最大迭代总次数
	public int whole_count;
	//标记点集合
	public Set<Integer> set;
	
	public Graph graph;
	
	public KK(Graph graph,int width,int height)
	{
		this.graph = graph;
		value = 1;
		count = 20;
		whole_count = count * graph.points.size() * 5000;
		set = new HashSet<Integer>();
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
					if(d[i][k] != Integer.MAX_VALUE&&d[k][j] != Integer.MAX_VALUE)
					{
						if(d[i][j] > d[i][k] + d[k][j])
							d[i][j] = d[i][k] + d[k][j];
					}
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
		Random random = new Random(5);
		//初始化所有节点
		for(int i = 0;i < num;i++)
		{
			Point point = new Point(i);
			point.pos.x = random.nextInt(width) + 10;
			point.pos.y = random.nextInt(height) + 10;
			this.point[i] = point;
			set.add(i);
		}
		
		
		computeDelta();
		layout();
		for(int i = 0;i < graph.points.size();i++)
		{
			Point point = graph.points.get(i);
			point.pos.x = this.point[point.num - 1].pos.x;
			point.pos.y = this.point[point.num - 1].pos.y;
		}
		System.out.println(whole_count);
	}
	
	
	
	public void layout()
	{
		while(true)
		{
			double maxDelta = -1;
			vertex = -1;
			if(set.size() == 0)
			{
				for(int i = 0;i < num;i++)
				{
					set.add(i);
				}
			}
			Iterator<Integer> iterator = set.iterator();
			while(iterator.hasNext())
			{
				int num = iterator.next().intValue();
				if(delta[num] > maxDelta)
				{
					maxDelta = delta[num];
					vertex = num;
				}
			}

			choose_delta = maxDelta;
//			if(set.size() == num)
//			{
//				System.out.println(choose_delta);
//				if(choose_delta < value)
//					break;
//			}
			System.out.println("vertex" + vertex);
			int num = 0;
			while(choose_delta > value)
			{
				if(!computeOffset(vertex))
				{
					break;
				}
				num++;
				whole_count--;
				if(num > count)
				{
					break;
				}
			}
			set.remove(vertex);
			if(whole_count < 0)
				return;
			computeDelta();
		}
	}
	
	//计算所有delta
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
			if(Double.isNaN(delta[i]))
				delta[i] = 0;
		}
	}
	
	//计算单个delta
	public void computeDelta(int m)
	{
		double EX = 0;
		double EY = 0;
		for(int j = 0;j < num;j++)
		{
			if(m != j)
			{
				double v1 = point[m].pos.x - point[j].pos.x;
				double v2 = point[m].pos.y - point[j].pos.y;
				double v3 = l[m][j] * v1;
				double v4 = l[m][j] * v2;
				double v5 = Math.sqrt(v1*v1 + v2*v2);
				EX += k[m][j]*(v1 - v3 / v5);
				EY += k[m][j]*(v2 - v4 / v5);
			}
		}
		delta[m] = Math.sqrt(EX * EX + EY * EY);
		if(Double.isNaN(delta[m]))
			delta[m] = 0;
		choose_delta = delta[m];
	}
	
	//计算节点偏移量,并进行偏移
	public boolean computeOffset(int m)
	{
		double EX2 = 0,EXY = 0,EYX = 0,EY2 = 0,EX = 0,EY = 0;
		
		for(int i = 0;i < num&&i!= m;i++)
		{
			double Xmi = point[m].pos.x - point[i].pos.x;
			double Ymi = point[m].pos.y - point[i].pos.y;
			double XY = Xmi*Xmi + Ymi*Ymi;
			double sqrtXY = Math.sqrt(XY);
			EX += k[m][i] * (Xmi - l[m][i]*Xmi / sqrtXY);
			EY += k[m][i] * (Ymi - l[m][i]*Ymi / sqrtXY);
			EX2 += k[m][i] * (1 - l[m][i]*Ymi*Ymi / (sqrtXY * XY));
			EXY += k[m][i] * (l[m][i]*Xmi*Ymi / (sqrtXY * XY));
			EYX += k[m][i] * (l[m][i]*Xmi*Ymi / (sqrtXY * XY));
			EY2 += k[m][i] * (1 - l[m][i]*Xmi*Xmi / (sqrtXY * XY));
 		}
		double offset_x = (EXY*EY - EX*EY2) / (EX2*EY2 - EXY*EYX);
		double offset_y = (EX*EYX - EX2*EY) / (EX2*EY2 - EXY*EYX);
		if(Double.isNaN(offset_x)&&Double.isNaN(offset_y))
			return false;
//		System.out.println("x:" + offset_x + " y:" + offset_y);
		point[m].pos.x += offset_x;
		point[m].pos.y += offset_y;
		point[m].pos.x = Math.max(Math.min(point[m].pos.x, width + 10), 10);
		point[m].pos.y = Math.max(Math.min(point[m].pos.y, height + 10), 10);
		computeDelta(m);
		return true;
	}
	


}
