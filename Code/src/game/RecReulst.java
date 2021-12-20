package game;

public class RecReulst
{
	public double value;
	public Direction dir;

	RecReulst(double value, Direction dir)
	{
		this.value = value;
		this.dir = dir;
	}
	
	RecReulst(){}
	
	@Override
	public String toString() {
		return "value = " + value +" dir = " + dir;
	}
	
}