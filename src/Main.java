import java.awt.Frame;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;


import Data.Edge;
import Data.Graph;
import Data.Point;

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
		
		Frame frame = new Frame("graph");
		frame.setSize(400, 400);
		frame.setLocation(100, 100);
		frame.setVisible(true);
	
		
		Graph graph = main.fileToGraph("graph");

			
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
						}
						if(pointR == null)
						{
							pointR = new Point(pointr);
							graph.points.add(pointR);
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
