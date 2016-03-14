import Data.Graph;

/**
 * @author zhr
 * @version 1.0.0
 * @date 2016年3月14日
 * @description
 */
public class KK {
	//显示区域宽度
	public int width;
	//显示区域高度
	public int height;
	//最短路径矩阵
	public int d[][];
	//节点数量
	public int num;
	
	public void graphInit(Graph graph)
	{
		num = graph.points.size();
		d = new int[num][num];
		
	}

}
