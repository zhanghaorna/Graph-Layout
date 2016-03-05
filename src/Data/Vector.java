package Data;

public class Vector {
	public double x;
	public double y;
	
	public double length()
	{
		return Math.sqrt(Math.pow(x, 2) + Math.pow(y, 2));
	}
	
	public void addVector(Vector vector)
	{
		x += vector.x;
		y += vector.y;
	}
	
	public void minusVector(Vector vector)
	{
		x -= vector.x;
		y -= vector.y;
	}
	
	public static Vector add(Vector v1,Vector v2)
	{
		Vector vector = new Vector();
		vector.x = v1.x + v2.x;
		vector.y = v1.y + v2.y;
		return vector;
	}
	
	public static Vector minus(Vector v1,Vector v2)
	{
		Vector vector = new Vector();
		vector.x = v1.x - v2.x;
		vector.y = v1.y - v2.y;
		return vector;
	}
}
