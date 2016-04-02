package Draw;

import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;

import Data.Graph;

public class GraphFrame extends JFrame{
	
	public List<Graph> graph;
	public DrawComponent drawComponent;
	
	public GraphFrame()
	{
		setTitle("Graph");
		setSize(DEFAULT_WIDTH, DEFAULT_HEIGHT);
		graph = new ArrayList<Graph>();
		drawComponent = new DrawComponent();
		add(drawComponent);
	}
	
	public void setGraph(Graph graph,int radius)
	{
		this.graph.add(graph);
		drawComponent.setGraph(this.graph,radius);
		repaint();
	}
	
	public static final int DEFAULT_WIDTH = 800;
	public static final int DEFAULT_HEIGHT = 800;
}
