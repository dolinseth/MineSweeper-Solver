import java.util.ArrayList;

public class Scenario {
	private int x, y;
	private ArrayList<RelativeCoordinate> relCoords;
	public Scenario(int x, int y, ArrayList<RelativeCoordinate> relCoords) {
		this.x = x;
		this.y = y;
		this.relCoords = relCoords;
	}
	
	public Scenario(int x, int y, RelativeCoordinate relCoord) {
		this.x = x;
		this.y = y;
		relCoords = new ArrayList<RelativeCoordinate>();
		relCoords.add(relCoord);
	}
	
	public int getX() {
		return x;
	}
	
	public int getY() {
		return y;
	}
	
	public ArrayList<RelativeCoordinate> getRelCoords(){
		return relCoords;
	}
}