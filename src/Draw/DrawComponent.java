package Draw;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Ellipse2D;

import javax.swing.JComponent;

import Data.Edge;
import Data.Graph;
import Data.Point;

public class DrawComponent extends JComponent{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public Graph graph;
	
	//偏移量
	public static int offset = 10;

	
	@Override
	protected void paintComponent(Graphics g) {
		if(graph != null)
		{
			for(int i = 0;i < graph.points.size();i++)
			{
				Point point = graph.points.get(i);
				g.fillOval((int)(point.pos.x + 200 - 3 + offset),(int)(point.pos.y + 200 - 3 + offset), 6, 6);
			}
			for(int i = 0;i < graph.edges.size();i++)
			{
				Edge edge = graph.edges.get(i);
				g.drawLine((int)(edge.u.pos.x + 200 + offset), (int)(edge.u.pos.y + 200 + offset),
						(int)(edge.v.pos.x + 200 + offset),
						(int)(edge.v.pos.y + 200 + offset));
			}

		}

		
	}
	
	public void setGraph(Graph graph)
	{
		this.graph = graph;
		repaint();
	}
	

	

}
