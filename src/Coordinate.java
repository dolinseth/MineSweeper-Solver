public class Coordinate {
	private int x, y;
	
	public Coordinate(int x, int y) {
		this.x = x;
		this.y = y;
	}
	
	public Coordinate(String s) {
		s = s.replace(" ", "");
		int index = s.indexOf(',');
		x = Integer.parseInt(s.substring(0, index));
		y = Integer.parseInt(s.substring(index + 1));
	}
	
	public int getX() {
		return x;
	}
	
	public int getY() {
		return y;
	}
	
	public String toString() {
		return x + ", " + y;
	}
}