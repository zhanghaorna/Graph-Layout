package Draw;

import javax.swing.JFrame;

import Data.Graph;

public class GraphFrame extends JFrame{
	
	public Graph graph;
	public DrawComponent drawComponent;
	
	public GraphFrame()
	{
		setTitle("Graph");
		setSize(DEFAULT_WIDTH, DEFAULT_HEIGHT);
		drawComponent = new DrawComponent();
		add(drawComponent);
	}
	
	public void setGraph(Graph graph)
	{
		this.graph = graph;
		drawComponent.setGraph(graph);
		repaint();
	}
	
	public static final int DEFAULT_WIDTH = 500;
	public static final int DEFAULT_HEIGHT = 500;
}
