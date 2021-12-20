package game;

public class Square {
	private int x;
	private int y;
	private EColor color;
	private double fCost=10000;
	


	public double getfCost() {
		return fCost;
	}

	public void setfCost(double fCost) {
		this.fCost = fCost;
	}

	public Square(int x,int y,EColor color) {
		this.x=x;
		this.y=y;
		this.color= color;
	}
	
	public void setSquare(int x,int y,EColor color) {
		this.x=x;
		this.y=y;
		this.color=color;
	}
	
	public void setColor(EColor color) {
		this.color=color;
	}
	
	public int[] getSquare() {
		return new int[] {x,y};
	}
	
	public int getX() {
		return x;
	}
	
	public int getY() {
		return y;
	}
	
	public EColor getColor() {
		return color;
	}
	
	@Override
	public boolean equals(Object obj) {
		if(obj instanceof Square)
		{
			Square s=(Square)obj;
			return (x==s.getX()&&y==s.getY());
		}
		return false;
	}
	
	@Override
	public String toString() {
		return "("+x+", "+y+")";
	}

}
