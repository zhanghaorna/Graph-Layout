import java.security.spec.ECField;

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
	
	public Graph graph;
	
	public FR(Graph graph,int width,int height)
	{
		this.graph = graph;
		this.width = width;
		this.height = height;
		k = Math.sqrt(width * height / graph.points.size());
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
			

		}
	}
}
