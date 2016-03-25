
import java.util.Random;

import Data.Edge;
import Data.Graph;
import Data.Point;
import Data.Vector;

public class FR {
	//显示区域宽度
	public int width;
	//显示区域高度
	public int height;
	//最佳距离
	public double k;
	//温度
	public double t;
	//迭代次数
	public int iterations;
	//节点移动量
	public double offset = Double.MAX_VALUE;
	
	public double offsetValue;
	//上次节点移动量
	public double pre_offset;
	
	public Graph graph;
	
	public FR(Graph graph,int width,int height)
	{
		this.graph = graph;
		this.width = width;
		this.height = height;
		k = Math.sqrt(width * height / graph.points.size());
		System.out.println("best distance:" + k);
		t = width / 10;
//		offsetValue = graph.points.size() * 0.01 * k;
		offsetValue = graph.points.size() * 0.1;
		initGraph();
	}
	
	//初始化图
	public void initGraph()
	{
		if(graph != null)
		{
			Random random = new Random();
			int x,y,symbol = 0;
			
			for(int i = 0;i < graph.points.size();i++)
			{
				symbol = random.nextInt(2);
				x = random.nextInt(width / 2);
				y = random.nextInt(width / 2);
				if(symbol == 1)
				{
					x = -x;
				}
				symbol = random.nextInt(2);
				if(symbol == 1)
				{
					y = -y;
				}
				Point point = graph.points.get(i);
				point.pos.x = x;
				point.pos.y = y;
			}
		}
	}
	
	public double repulsiveForce(double distance)
	{
		return k*k/distance;
	}
	
	public double attractiveForce(double distance)
	{
		return distance*distance/k;
	}
	
	public void initLayout(int count)
	{
		for(int i = 0;i < count;i++)
		{
			calculate();
		}
	}
	
	public void infLayout()
	{
		for(;;)
		{
			if(offset < offsetValue)
				break;
//			if(t < 0.5)
//				break;
//			if(Math.abs(offset - pre_offset) < 0.0001)
//				break;
//			pre_offset = offset;
			calculate();
		}
	}
	
	//每次迭代的工作
	public void calculate()
	{
		//计算排斥力
		for(int i = 0;i < graph.points.size();i++)
		{
			Point v = graph.points.get(i);
			v.clearDisp();
			for(int j = 0;j < graph.points.size();j++)
			{
				Point u = graph.points.get(j);
				if(u != v)
				{
					Vector vector = Vector.minus(v.pos, u.pos);
					double length = vector.length();
					vector.x = vector.x / length * repulsiveForce(length);
					vector.y = vector.y / length * repulsiveForce(length);
					v.disp.addVector(vector);
				}
			}
		}
		//计算吸引力
		for(int i = 0;i < graph.edges.size();i++)
		{
			Edge edge = graph.edges.get(i);
			Vector vector = Vector.minus(edge.v.pos, edge.u.pos);
			double length = vector.length();
			vector.x = vector.x / length * attractiveForce(length);
			vector.y = vector.y / length * attractiveForce(length);
			edge.v.disp.minusVector(vector);
			edge.u.disp.addVector(vector);
		}
		offset = 0;
		//对节点进行移动
		for(int i = 0;i < graph.points.size();i++)
		{
			Point v = graph.points.get(i);
			Vector vector = new Vector();
			vector.x = v.pos.x;
			vector.y = v.pos.y;
			double length = v.disp.length();
			v.disp.x = v.disp.x / length * Math.min(length, t);
			v.disp.y = v.disp.y / length * Math.min(length, t);

			v.pos.x = v.pos.x + v.disp.x;
			v.pos.y = v.pos.y + v.disp.y;
			
			v.pos.x = Math.min(width / 2, Math.max(- width / 2, v.pos.x));
			v.pos.y = Math.min(height / 2, Math.max(- height / 2, v.pos.y));
			vector.x = v.pos.x - vector.x;
			vector.y = v.pos.y - vector.y;
			offset += vector.length();
		}
		
		System.out.println("节点移动量" + offset  + " 当前温度:" + t);
//		if(offset < pre_offset)
//		if(t > 1)
		t = 0.95 * t;	
//		else
//			t = Math.pow(Math.E, 1 - t);
		pre_offset = offset;

			
	}
}
