import java.awt.Frame;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Scanner;

import javax.swing.JFrame;

import Data.Edge;
import Data.Graph;
import Data.Point;
import Data.Vector;
import Draw.DrawComponent;
import Draw.GraphFrame;

/**
 * @author zhr
 * @version 1.0.0
 * @date 2016��3��4��
 * @description
 */
public class Main {
	
	
	
	public static void main(String[] args)
	{

		Main main = new Main();
//		Graph graph = main.fileToGraph("graph");
		List<Graph> graphs = main.fileToGraphs("graph3");
		
//		Graph graph = main.numToGraph(graphs.size());
//		System.out.println(graphs.size());
		KK kk = new KK(graphs.get(0),400,400,300,300);
//		double length = main.graphEdgeLength(graph) * 0.6;
		
		GraphFrame frame = new GraphFrame();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);	
//		frame.setGraph(graph,200);
//		frame.setGraph(kk.graph,0);
		
//		for(int i = 0;i < graphs.size();i++)
//		{
//			KK kk1 = new KK(graphs.get(i),(int)length,(int)length,graph.points.get(i).pos.x,
//					graph.points.get(i).pos.y);
//			frame.setGraph(kk1.graph, 0);
//		}
//		
		
		
//		FR fr = new FR(graphs.get(0), 400, 400);
//		fr.initGraph();
//		fr.infLayout();
//		fr.initLayout(45);


//		System.out.println("points:" + graph.points.size());
//		FR fr = new FR(graph, 400, 400);
////		fr.initLayout(300);
//		for(int i = 0;i < graph.points.size();i++)
//		{
//			Point point = graph.points.get(i);
//			System.out.println("x:" + point.pos.x + " y:" + point.pos.y);
//		}
//		

//		fr.initGraph();
////		fr.initLayout(300);
////		fr.infLayout();
		
		frame.setGraph(kk.graph,0);
		
//		Scanner scanner = new Scanner(System.in);
//		
//		for(int i = 1;i < 45;i++)
//		{
//			frame.setGraph(graphs.get(0),200);
//			fr.calculate();
//			System.out.println(i);
//			scanner.next();
////			try 
////			{
////				Thread.sleep(500);
////			} catch (InterruptedException e) {
////				// TODO Auto-generated catch block
////				e.printStackTrace();
////			}
//		}
		
			
	}
	
	
	public Graph fileToGraph(String path)
	{
		Graph graph = new Graph();
		Map<Integer, Point> map = new HashMap<Integer, Point>();
		File file = new File(path);
		BufferedReader bReader = null;
		try 
		{
			bReader = new BufferedReader(new FileReader(file));
			String txt = "";
			while((txt = bReader.readLine()) != null)
			{
				if(txt != null&&!txt.equals(""))
				{
					String[] vertex = txt.split(" ");
					if(vertex != null)
					{
						int pointl = Integer.valueOf(vertex[0]);
						int pointr = Integer.valueOf(vertex[1]);
						Point pointL = map.get(pointl);
						Point pointR = map.get(pointr);
						if(pointL == null)
						{
							pointL = new Point(pointl);
							graph.points.add(pointL);
							map.put(pointl, pointL);
						}
						if(pointR == null)
						{
							pointR = new Point(pointr);
							graph.points.add(pointR);
							map.put(pointr, pointR);
						}
						Edge edge = new Edge();
						edge.u = pointL;
						edge.v = pointR;
						graph.edges.add(edge);
					}
				}
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		finally
		{
			if(bReader != null)
			{
				try {
					bReader.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		graph.init();
		return graph;
	}
	
	public List<Graph> fileToGraphs(String path)
	{
		ArrayList<Graph> graphs = new ArrayList<Graph>();
		Graph graph = new Graph();
		Map<Integer, Point> map = new HashMap<Integer, Point>();
		File file = new File(path);
		BufferedReader bReader = null;
		try 
		{
			bReader = new BufferedReader(new FileReader(file));
			String txt = "";
			while((txt = bReader.readLine()) != null)
			{
				if(txt != null&&!txt.equals(""))
				{
					String[] vertex = txt.split(" ");
					if(vertex != null)
					{
						int pointl = Integer.valueOf(vertex[0]);
						int pointr = Integer.valueOf(vertex[1]);
						Point pointL = map.get(pointl);
						Point pointR = map.get(pointr);
						if(pointL == null)
						{
							pointL = new Point(pointl);
							graph.points.add(pointL);
							map.put(pointl, pointL);
						}
						if(pointR == null)
						{
							pointR = new Point(pointr);
							graph.points.add(pointR);
							map.put(pointr, pointR);
						}
						Edge edge = new Edge();
						edge.u = pointL;
						edge.v = pointR;
						graph.edges.add(edge);
					}
				}
				else
				{
					graph.init();
					graphs.add(graph);
					graph = new Graph();
					map.clear();
				}
			}
			graph.init();
			graphs.add(graph);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		finally
		{
			if(bReader != null)
			{
				try {
					bReader.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		return graphs;
	}
	
	//根据节点数目形成星型Graph
	public Graph numToGraph(int num)
	{
		Graph graph = new Graph();
		Point point1 = new Point(1);
		graph.points.add(point1);
		for(int i = 2;i <= num;i++)
		{
			Point point = new Point(i);
			Edge edge = new Edge();
			edge.u = point1;
			edge.v = point;
			graph.points.add(point);
			graph.edges.add(edge);
		}
		graph.init();
		return graph;
	}
	
	public int graphEdgeLength(Graph graph)
	{
		double whole_length = 0;
		for(int i = 0;i < graph.edges.size();i++)
		{
			Point u = graph.edges.get(i).u;
			Point v = graph.edges.get(i).v;
			Vector vector = Vector.minus(u.pos, v.pos);
			whole_length += vector.length();
		}
		return (int) (whole_length / 4);
	}
}
