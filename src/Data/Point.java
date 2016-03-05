package Data;
/**
 * @author zhr
 * @version 1.0.0
 * @date 2016��3��4��
 * @description
 */
public class Point {
	public Vector pos;
	public Vector disp;
	//点的标识
	public int num;
	
	public void clearDisp()
	{
		disp.x = 0;
		disp.y = 0;
	}
	
	public Point(int num)
	{
		pos = new Vector();
		disp = new Vector();
		this.num = num;
	}
	
	public Point()
	{
		pos = new Vector();
		disp = new Vector();
	}
}
