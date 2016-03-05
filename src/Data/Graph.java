package Data;

import java.util.ArrayList;
import java.util.List;

/**
 * @author zhr
 * @version 1.0.0
 * @date 2016��3��4��
 * @description
 */
public class Graph {
	public List<Point> points;
	public List<Edge> edges;	
	//图中节点的数量
	public int num;
	
	public Graph()
	{
		points = new ArrayList<Point>();
		edges = new ArrayList<Edge>();
	}
	
	//将点和边全部赋值给points和edges后，进行图的初始化
	public void init()
	{
		num = points.size();
	}
}
