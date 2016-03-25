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
	//节点产生的能量值
	public double E[];
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
	//温度T
	public double T;
	//目前系统总能量
	public double wholeE;
	
	//遗传算法 解数量
	public int gene_count = 10;
	public Point gene_point[][];
	public Point temp[][];
	public double gene_e[];
	//交叉概率
	public double cross = 0.75;
	//变异概率
	public double variance = 0.05;
	//遗传最大迭代次数
	public int gene_whole_count = 100;
	
	public KK(Graph graph,int width,int height)
	{
		this.graph = graph;
		value = 1;
		count = 20;
		num = graph.points.size();
		gene_point = new Point[graph.points.size()][gene_count];
		temp = new Point[graph.points.size()][gene_count];
		gene_e = new double[gene_count];
		whole_count = count * graph.points.size() * 100;
		set = new HashSet<Integer>();
		this.width = width;
		this.height = height;
		
		d = new int[num][num];
		g = new int[num][num];
		l = new double[num][num];
		k = new double[num][num];
		point = new Point[num];
		delta = new double[num];
		E = new double[num];
		T = width / 10;
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
		double L = ((double)width) / 10;

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
		Random random = new Random(1);
		//初始化所有节点
		for(int i = 0;i < num;i++)
		{
			Point point = new Point(i);
			point.pos.x = random.nextInt(width) + 10;
			point.pos.y = random.nextInt(height) + 10;
			this.point[i] = point;
			set.add(i);
		}
		
		computeE();
//		wholeE = computeE();
		
//		changeVertexPosition();
		
//		computeDelta();
		long start = System.currentTimeMillis();
		layout();
		long end = System.currentTimeMillis();
		
		System.out.println(end - start);
		
		for(int i = 0;i < graph.points.size();i++)
		{
			Point point = graph.points.get(i);
			point.pos.x = this.point[point.num - 1].pos.x;
			point.pos.y = this.point[point.num - 1].pos.y;
		}
//		System.out.println(whole_count);
	}
	
	public void geneAlgorithm()
	{
		//初始化10个可行解
		Random random = new Random();
		for(int i = 0;i < gene_count;i++)
		{
			for(int j = 0;j < num;j++)
			{
				Point point = new Point(j);
				point.pos.x = random.nextInt(width) + 10;
				point.pos.y = random.nextInt(height) + 10;
				gene_point[j][i] = point;
			}
		}
		
	}
	
	
	//评估函数，计算能量，越小越好
	public void fitness(Point point[][],int num)
	{
		double E = 0;
		for(int i = 0;i < this.num;i++)
		{
			for(int j = 0;j < this.num;j++)
			{
				if(i != j)
				{
					double Xij = point[i][num].pos.x - point[j][num].pos.x;
					double Yij = point[i][num].pos.y - point[j][num].pos.y;
					double distance = Math.sqrt(Xij * Xij + Yij * Yij);
					E += k[i][j] * 0.5 * Math.pow((distance - l[i][j]), 2);
				}
			}
		}
		this.gene_e[num] = 1/E;
	}
	
	
	
	//选出两个个体使用交叉规则，在进行变异得出结果,总共产生gene_count个新生儿
	public void gene_cal()
	{
		for(int k = 0;k < gene_count;k += 2)
		{
			int index[] = gene_selection();
			Point point[][] = new Point[this.num][2];
			for(int i = 0;i < this.num;i++)
			{
				Point pt1 = new Point(gene_point[i][index[0]]);
				Point pt2 = new Point(gene_point[i][index[1]]);
				point[i][0] = pt1;
				point[i][1] = pt2;
			}
			Random random = new Random();
			//进行交叉 
			if(random.nextDouble() < cross)
			{
				//单点交叉
				int val = random.nextInt(this.num);
				double temp_x = this.gene_point[val][index[0]].pos.x;
				double temp_y = this.gene_point[val][index[0]].pos.y;
				this.gene_point[val][index[0]].pos.x = this.gene_point[val][index[1]].pos.x;
				this.gene_point[val][index[0]].pos.y = this.gene_point[val][index[1]].pos.y;
				this.gene_point[val][index[1]].pos.x = temp_x;
				this.gene_point[val][index[1]].pos.x = temp_y;
			}
			//进行变异
			if(random.nextDouble() < variance)
			{
				//取3个点坐标变异
				for(int i = 0;i < this.num / 10.0;i++)
				{
					int val = random.nextInt(this.num);
					int offset_x = random.nextInt(20) + 1;
					if(random.nextDouble() < 0.5)
						offset_x = -offset_x;
					int offset_y = random.nextInt(20) + 1;
					if(random.nextDouble() < 0.5)
						offset_y = -offset_y;
					this.gene_point[val][index[0]].pos.x += offset_x;
					this.gene_point[val][index[0]].pos.y += offset_y;
					this.gene_point[val][index[1]].pos.x += offset_x;
					this.gene_point[val][index[1]].pos.y += offset_y;
				}		
			}
			
			
		}

		
	}
	
	//轮转盘选出解
	public int[] gene_selection()
	{
		double whole = 0;
		for(int i = 0;i < gene_count;i++)
		{
			whole = this.gene_e[i];
		}
		for(int i = 0;i < gene_count;i++)
		{
			this.gene_e[i] = this.gene_e[i] / whole;
		}
		Random random = new Random();
		int[] index = new int[2];
		int first = -1;
		int second = -1;
		double val = random.nextDouble();
		for(int i = 0;i < gene_count;i++)
		{
			val -= this.gene_e[i];
			if(val < 0)
			{
				first = i;
				break;
			}
		}
		if(first == -1)
			first = gene_count - 1;
		
		do {
			val = random.nextDouble();
			for(int i = 0;i < gene_count;i++)
			{
				val -= this.gene_e[i];
				if(val < 0)
				{
					second = i;
					break;
				}
			}
			if(second == -1)
				second = gene_count - 1;
		} while (second != first);

		index[0] = first;
		index[1] = second;
		return index;
	}
	
	public void changeVertexPosition()
	{
		Random random = new Random();
		double[] x_set = new double[num];
		double[] y_set = new double[num];
		int count = 10;
		do 
		{
			for(int i = 0;i < num;i++)
			{
				double offset_x = random.nextInt((int)T);
				double offset_y = random.nextInt((int)T);
				if(T < 1)
				{
					offset_x = random.nextDouble();
					offset_y = random.nextDouble();
				}

				int symbol = random.nextInt(2);
				if(symbol == 1)
					offset_x = -offset_x;
				symbol = random.nextInt(2);
				if(symbol == 1)
					offset_y = -offset_y;
				x_set[i] = offset_x;
				y_set[i] = offset_y;
				if(point[i].pos.x + offset_x > width + 10)
				{
					x_set[i] = width + 10 - point[i].pos.x;
				}
				if(point[i].pos.x + offset_x < 10)
				{
					x_set[i] = point[i].pos.x - 10;
				}
				if(point[i].pos.y + offset_y > height + 10)
				{
					y_set[i] = height + 10 - point[i].pos.y;
				}
				if(point[i].pos.y + offset_y < 10)
				{
					y_set[i] = point[i].pos.y - 10;
				}
				point[i].pos.x += x_set[i];
				point[i].pos.y += y_set[i];
			}
//			double temp = computeE();
//			if(temp < wholeE)
//			{
//				temp = wholeE;
//				T = 0.9 * T;
//				count = 10;
//			}
//			else
//			{
//				for(int i = 0;i < num;i++)
//				{
//					point[i].pos.x -= x_set[i];
//					point[i].pos.y -= y_set[i];
//				}
//				count--;
//			}
		} while (T < 1&&count < 0);		
	}
	
	
	
	public void layout()
	{
		while(true)
		{
//			double maxE = -1;
//			vertex = -1;
//			if(set.size() == 0)
//			{
//				for(int i = 0;i < num;i++)
//				{
//					set.add(i);
//				}
//			}
//			Iterator<Integer> iterator = set.iterator();
//			while(iterator.hasNext())
//			{
//				int num = iterator.next().intValue();
//				if(E[num] > maxE)
//				{
//					maxE = E[num];
//					vertex = num;
//				}
//			}
//			choose_delta = maxE;
//			if(choose_delta > value)
//				randomOffset(vertex);
//			whole_count--;
////			int num = 0;
////			while(choose_delta > value)
////			{
////				computeE(vertex);
////				whole_count--;
////				if(!computeOffset(vertex))
////				{
////					break;
////				}
////				num++;
////				if(num > count)
////				{
////					break;
////				}
////			}	
//			set.remove(vertex);
//			if(whole_count < 0)
//				return;
			if(gene_whole_count < 0)
				break;
			
		}
	}
	
	//计算所有的E
	public void computeE()
	{
		double E = 0;
		for(int i = 0;i < num;i++)
		{
			E = 0;
			for(int j = 0;j < num;j++)
			{
				if(i != j)
				{
					double Xij = point[i].pos.x - point[j].pos.x;
					double Yij = point[i].pos.y - point[j].pos.y;
					double distance = Math.sqrt(Xij * Xij + Yij * Yij);
					E += k[i][j] * 0.5 * Math.pow((distance - l[i][j]), 2);
				}
			}
			this.E[i] = E;
		}
	}
	
	//计算单个E
	public void computeE(int m)
	{
		double E = 0;
		for(int i = 0;i < num;i++)
		{
			if(i != m)
			{
				double Xij = point[m].pos.x - point[i].pos.x;
				double Yij = point[m].pos.y - point[i].pos.y;
				double distance = Math.sqrt(Xij * Xij + Yij * Yij);
				E += k[m][i] * 0.5 * Math.pow((distance - l[m][i]), 2);
			}
		}
		this.E[m] = E;
		choose_delta = E;
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
		if(Math.abs(offset_x) < 0.1&&Math.abs(offset_y) < 0.1)
			return false;
//		System.out.println("x:" + offset_x + " y:" + offset_y);
		point[m].pos.x += offset_x;
		point[m].pos.y += offset_y;
		point[m].pos.x = Math.max(Math.min(point[m].pos.x, width + 10), 10);
		point[m].pos.y = Math.max(Math.min(point[m].pos.y, height + 10), 10);
		
		computeE(m);
//		computeDelta(m);
		return true;
	}
	
	
//	public double computeE()
//	{
//		double E = 0;
//		for(int i = 0;i < num - 1;i++)
//			for(int j = i + 1;j < num;j++)
//			{
//				double Xij = point[i].pos.x - point[j].pos.x;
//				double Yij = point[i].pos.y - point[j].pos.y;
//				double distance = Math.sqrt(Xij * Xij + Yij * Yij);
//				E += k[i][j] * 0.5 * Math.pow((distance - l[i][j]), 2);
//			}
//		return E;
//	}
	
	//对节点m进行移动
	public void randomOffset(int m)
	{
		T = width / 10;
		Random random = new Random();
		int count = 4;
		//0代表向上，1代表向右，2代表向下，3代表向左
		int or = 0;
		while(count > 0)
		{
			int offset = 0;
			double x = 0,y = 0;
			double height = 0,width = 0;
			int result = 2;
			switch (or) {
			case 0:
				y = point[m].pos.y;
				if(y > 11)
					result = computeDelta(m, 0, -1);
//				height = Math.min(y - 10,T);
//				if(height - 1 < 1)
//					offset = 1;
//				else
//					offset = random.nextInt((int)(height - 1)) + 1;
//				if(y - offset > 10)				
//					result = computeDelta(m, 0, -offset);
	
				break;
			case 1:
				x = point[m].pos.x;
				if(x < this.width + 9)
					result = computeDelta(m, 1, 0);
//				width = Math.min(x - 10, T);
//				if(width - 1 < 1)
//					offset = 1;
//				else
//					offset = random.nextInt((int)(width - 1)) + 1;
//				if(x + offset < this.width + 10)
//					result = computeDelta(m, offset, 0);
				break;
			case 2:
				y = point[m].pos.y;
				if(y < this.height + 9)
					result = computeDelta(m, 0, 1);
//				height = Math.min(this.height + 10 - y,T);
//				if(height - 1 < 1)
//					offset = 1;
//				else
//					offset = random.nextInt((int)(height - 1)) + 1;
//				if(y + offset < this.height + 10)
//					result = computeDelta(m, 0, offset);
				break;
			case 3:
				x = point[m].pos.x;
				if(x > 11)
					result = computeDelta(m, -1, 0);
//				width = Math.min(this.width + 10 - x, T);
//				if(width - 1 < 1)
//					offset = 1;
//				else
//					offset = random.nextInt((int)(width - 1)) + 1;
//				if(x - offset > 10)
//					result = computeDelta(m, -offset, 0);
				break;
			default:
				break;
			}
			if(result == 0)
			{
				return;
			}
			else if(result == 1)
			{
				T = T * 0.9;
				count = 10;
			}
			else 
			{
				or++;
				if(or == 4)
					or = 0;
				count--;
			}
		}
	}
	
	//0表示已比设置的阈值小，1表示原来的delta大，2就表示比原来的delta大
	public int computeDelta(int m ,int offset_x,int offset_y)
	{
		double EX = 0;
		double EY = 0;
		double E = 0;
		for(int j = 0;j < num;j++)
		{
			if(m != j)
			{
				double v1 = point[m].pos.x - point[j].pos.x;
				if(offset_x != 0)
				{
					v1 = point[m].pos.x + offset_x - point[j].pos.x;
				}
				double v2 = point[m].pos.y - point[j].pos.y;
				if(offset_y != 0)
				{
					v2 = point[m].pos.y + offset_y - point[j].pos.y;
				}
				double d = Math.sqrt(v1*v1 + v2*v2);
				
				E += 0.5 * k[m][j] * Math.pow(d - l[m][j], 2);				
//				double v3 = l[m][j] * v1;
//				double v4 = l[m][j] * v2;
//				double v5 = Math.sqrt(v1*v1 + v2*v2);
//				EX += k[m][j]*(v1 - v3 / v5);
//				EY += k[m][j]*(v2 - v4 / v5);
			}
		}
//		double delt = Math.sqrt(EX * EX + EY * EY);
//		if(Double.isNaN(delt))
//			delt = 0;
		if(E < this.E[m])
		{
			point[m].pos.x += offset_x;
			point[m].pos.y += offset_y;

			this.E[m] = E;

			if(E < value)
				return 0;
			else
			{
				return 1;
			}
		}
		else {
			return 2;
		}
		
	}


}
