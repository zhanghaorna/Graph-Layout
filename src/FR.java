import java.awt.Window;
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
	
	public Graph graph;
	
	public FR(Graph graph,int width,int height)
	{
		this.graph = graph;
		this.width = width;
		this.height = height;
		k = Math.sqrt(width * height / graph.points.size());
		t = width / 10;
	}
	
	//初始化图
	public void initGraphLayout()
	{
		if(graph != null)
		{
			Random random = new Random();
			int x,y = 0;
			int symbol = random.nextInt(2);
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
			
		}
	}
	
	public double repulsiveForce(double distance)
	{
		return -1*k*k/distance;
	}
	
	public double attractiveForce(double distance)
	{
		return distance*distance/k;
	}
	

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
		//对节点进行移动
		for(int i = 0;i < graph.points.size();i++)
		{
			Point v = graph.points.get(i);
			double length = v.disp.length();
			v.disp.x = v.disp.x / length * Math.min(length, t);
			v.disp.y = v.disp.y / length * Math.min(length, t);
			v.pos.x = Math.min(width / 2, Math.max(- width / 2, v.pos.x));
			v.pos.y = Math.min(height / 2, Math.max(- height / 2, v.pos.y));
		}
		t = 0.8 * t;
	}
}
