import java.awt.Frame;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.Buffer;
import java.util.HashMap;
import java.util.Map;

import javax.xml.transform.Templates;

import Data.Graph;
import Data.Point;

/**
 * @author zhr
 * @version 1.0.0
 * @date 2016Äê3ÔÂ4ÈÕ
 * @description
 */
public class Main {
	public static void main(String[] args)
	{
		Frame frame = new Frame("graph");
		frame.setSize(400, 400);
		frame.setLocation(100, 100);
		frame.setVisible(true);


		
	}
	
	
	public Graph FileToGraph(String path)
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
		return graph;
	}
}
