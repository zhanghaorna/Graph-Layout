import java.awt.Frame;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import javax.swing.JFrame;

import Data.Edge;
import Data.Graph;
import Data.Point;
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
		Graph graph = main.fileToGraph("graph50");		
		KK kk = new KK(graph,400,400);
		
		FR fr = new FR(graph, 400, 400);
		fr.initGraph();
		fr.infLayout();
		
		GraphFrame frame = new GraphFrame();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);	
		frame.setGraph(graph,200);
//		frame.setGraph(kk.graph,0);

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
//		frame.setGraph(graph);
		
//		for(int i = 0;i < 200;i++)
//		{
//			fr.calculate();
//			frame.setGraph(graph);
//			
//			try 
//			{
//				Thread.sleep(500);
//			} catch (InterruptedException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
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
}
