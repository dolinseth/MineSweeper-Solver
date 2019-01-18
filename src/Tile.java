import java.util.ArrayList;

public class Tile {
	public static int width, height;
	public static Board board;
	private boolean isBomb, isMarked, isVisible;
	public boolean isSolved;
	private int x, y, value;
	
	public Tile(int x, int y){
		this.x = x;
		this.y = y;
		isBomb = false;
		isVisible = false;
	}
	
	public Tile(int x, int y, boolean isBomb){
		this.x = x;
		this.y = y;
		this.isBomb = isBomb;
		isVisible = false;
	}
	
	public Coordinate getLoc(){
		return new Coordinate(x, y);
	}
	
	public void mark(){
		isMarked = true;
	}
	
	public void calcValue(){
		if(!isBomb){
			ArrayList<Tile> tiles = board.getSurroundingTiles(x, y);
			int calculatedValue = 0;
			for(int i = 0; i < tiles.size(); i++){
				if(tiles.get(i).isBomb()){
					calculatedValue++;
				}
			}
			
			value = calculatedValue;
		}
		else{
			value = -1;
		}
	}
	
	public ArrayList<Coordinate> getSurroundingCoords(){
		ArrayList<Coordinate> coords = new ArrayList<Coordinate>();
		coords.add(new Coordinate(x-1, y-1));
		coords.add(new Coordinate(x-1, y));
		coords.add(new Coordinate(x-1, y+1));
		coords.add(new Coordinate(x, y-1));
		coords.add(new Coordinate(x, y+1));
		coords.add(new Coordinate(x+1, y-1));
		coords.add(new Coordinate(x+1, y));
		coords.add(new Coordinate(x+1, y+1));
		
		for(int i = 0; i < coords.size(); i++){
			int x = coords.get(i).getX();
			int y = coords.get(i).getY();
			if (x < 0 || x >= width || y < 0 || y >= height){
				coords.remove(i);
				i--;
			}
		}
		return coords;
	}
	
	public void setVisibility(boolean newVisibility){
		isVisible = newVisibility;
	}
	
	public boolean isVisible(){
		return isVisible;
	}
	
	public boolean isMarked(){
		return isMarked;
	}
	
	public int getValue(){
		return value;
	}
	
	public boolean isBomb(){
		return isBomb;
	}
}
