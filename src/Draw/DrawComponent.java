package Draw;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Ellipse2D;
import java.util.List;

import javax.swing.JComponent;

import Data.Edge;
import Data.Graph;
import Data.Point;

public class DrawComponent extends JComponent{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public List<Graph> graphs;
	
	//偏移量
	public static int offset = 0;
	public int radius;
	
	@Override
	protected void paintComponent(Graphics g) {
		if(graphs != null)
		{
			for(int j = 0;j < graphs.size();j++)
			{
				Graph graph = graphs.get(j);
				for(int i = 0;i < graph.edges.size();i++)
				{
					Edge edge = graph.edges.get(i);
					g.setColor(Color.BLACK);
					g.drawLine((int)(edge.u.pos.x + radius + offset), (int)(edge.u.pos.y + radius + offset),
							(int)(edge.v.pos.x + radius + offset),
							(int)(edge.v.pos.y + radius + offset));
				}
				for(int i = 0;i < graph.points.size();i++)
				{
					Point point = graph.points.get(i);
					g.setColor(Color.red);
					g.fillOval((int)(point.pos.x + radius - 5 + offset),(int)(point.pos.y + radius - 5 + offset), 10, 10);
					
				}
			}
		}

		
	}
	
	public void setGraph(List<Graph> graph,int radius)
	{
		this.graphs = graph;
		this.radius = radius;
		repaint();
	}
	

	

}
