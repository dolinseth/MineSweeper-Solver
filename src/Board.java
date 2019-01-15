import java.util.Random;
import java.util.ArrayList;

public class Board {
	private int width, height;
	private Tile[][] board;
	
	public Board(int width, int height, int numBombs) {
		this.width = width;
		this.height = height;
		Tile.board = this;	
		boolean firstMoveSuccessful = false;
		Random rnd = new Random();
		while(!firstMoveSuccessful) {
			genBoard(numBombs);
			int x = rnd.nextInt(width);
			int y = rnd.nextInt(height);
			if(getTile(x, y).getValue() == 0) {
				selectTile(x, y);
				firstMoveSuccessful = true;
			}
		}
	}
	
	public void genBoard(int numBombs) {
		int[] bombIndices = new int[numBombs];
		Random rnd = new Random();
		for(int i = 0; i < numBombs; i++) {
			int possibleNum = rnd.nextInt(width * height);
			while(ArrayContains(bombIndices, possibleNum)) {
				possibleNum = rnd.nextInt(width * height);
			}
			
			bombIndices[i] = possibleNum;
		}
		
		board = new Tile[width][height];
		for(int i = 0; i < bombIndices.length; i++) {
			int x = bombIndices[i] % width;
			int y = bombIndices[i] / width;
			board[x][y] = new Tile(x, y, true);
		}
		for(int x = 0; x < width; x++) {
			for(int y = 0; y < height; y++) {
				if(board[x][y] == null) {
					board[x][y] = new Tile(x, y);
				}
			}
		}
		for(int x = 0; x < width; x++) {
			for(int y = 0; y < height; y++) {
				if(!board[x][y].isBomb()) {
					board[x][y].calcValue();
				}
			}
		}
		
//		System.out.println("Board gen finished");
	}
	
	public void calcValues(){
		for(int x = 0; x < width; x++) {
			for(int y = 0; y < height; y++) {
				if(!board[x][y].isBomb()) {
					board[x][y].calcValue();
				}
			}
		}
	}
	
	public Board(Board board) {
		this.width = board.width;
		this.height = board.height;
		this.board = new Tile[width][height];
		for(int x = 0; x < width; x++) {
			for(int y = 0; y < height; y++) {
				this.board[x][y] = board.getTile(x, y);
			}
		}
	}
	
	private boolean ArrayContains(int[] arr, int num) {
		for(int i = 0; i < arr.length; i++) {
			if(arr[i] == num) {
				return true;
			}
		}
		
		return false;
	}
	
	public ArrayList<Tile> getTiles(ArrayList<Coordinate> coords){
		ArrayList<Tile> tiles = new ArrayList<Tile>();
//		System.out.println("Getting tiles from " + coords.size() + " coordinates");
		for(int i = 0; i < coords.size(); i++) {
			int x = coords.get(i).getX();
			int y = coords.get(i).getY();
			try{
				Tile t = board[x][y];
				if(!t.isVisible() || t.getValue() != 0) {
					tiles.add(t);
				}
			}catch(ArrayIndexOutOfBoundsException e){
				System.out.println("getTiles tried to pull a tile out of bounds @x=" + x + ",y=" + y);
			}
		}
		return tiles;
	}
	
	public ArrayList<Tile> getSurroundingTiles(int x, int y){
//		System.out.println("Got surrounding tiles from @" + x + ", " + y);
//		System.out.println(board[x][y].getSurroundingCoords().size());
		return getTiles(board[x][y].getSurroundingCoords());
	}
	
	public Tile getTile(Coordinate coord) {
		return board[coord.getX()][coord.getY()];
	}
	
	public Tile getTile(int x, int y) {
		return board[x][y];
	}
	
	public Tile getTile(int x, int y, RelativeCoordinate relCoord) {
		return board[x + relCoord.getDx()][y + relCoord.getDy()];
	}
	
	public ArrayList<Tile> getAdjNonZero(int x, int y) {
		ArrayList<Tile> tiles = getSurroundingTiles(x, y);
		for(int i = 0; i < tiles.size(); i++) {
			if(!tiles.get(i).isVisible() || tiles.get(i).getValue() == 0) {
				tiles.remove(i);
				i--;
			}
		}
		
		return tiles;
	}
	
	public ArrayList<Tile> getAdjUnknown(int x, int y){
		ArrayList<Tile> tiles = getSurroundingTiles(x, y);
		for(int i = 0; i < tiles.size(); i++) {
			if(tiles.get(i).isVisible() || tiles.get(i).isMarked()) {
				tiles.remove(i);
				i--;
			}
		}
		
		return tiles;
	}
	
	public ArrayList<Tile> getAdjMarked(int x, int y) {
		ArrayList<Tile> tiles = getSurroundingTiles(x, y);
		for(int i = 0; i < tiles.size(); i++) {
			if(!tiles.get(i).isMarked()) {
				tiles.remove(i);
				i--;
			}
		}
		
		return tiles;
	}
	
	public void setAllVisibility(boolean newVisibility) {
		for(int x = 0; x < width; x++) {
			for(int y = 0; y < height; y++) {
				board[x][y].setVisibility(newVisibility);
			}
		}
	}
	
	public void printBoard() {
		System.out.println("> = marked\n* = bomb\n- = 0\n");
		String s = "\n";
		s += (".        ");
		for(int x = 0; x < width; x++) {
			s += ((x % 10) + " ");
		}
		s += "\n";
		s += (".       ");
		for(int i = 0; i < width * 2; i++) {
			s += ("-");
		}
		s += "\n";
		for(int y = 0; y < height; y++) {
			s += (y + "\t|");
			for(int x = 0; x < width; x++) {
				Tile t = board[x][y];
//				System.out.println("tile @" + x + ", " + y + ". isVisible=" + t.isVisible() + ". isMarked=" + t.isMarked() + ". isBomb=" + t.isBomb());
				if(t.isVisible() || t.isMarked()) {
					if(t.isMarked()) {
						s += (">|");
					}
					else if(t.isBomb()) {
						s += ("*|");
					}
					else if(t.getValue() == 0) {
						s += ("-|");
					}
					else {
						s += (t.getValue() + "|");						
					}
				}
				else {
					s += (" |");
				}
			}
		
			s += "\n";
			s += (".       ");
			for(int i = 0; i < width * 2; i++) {
				s += ("-");
			}
			s += "\n";
		}
		System.out.println(s);
	}
	
	public void printBoard(int x1, int y1) {
		String s = "\n";
		s += (".        ");
		for(int x = 0; x < width; x++) {
			s += ((x % 10) + " ");
		}
		s += "\n";
		s += (".       ");
		for(int i = 0; i < width * 2; i++) {
			s += ("-");
		}
		s += "\n";
		for(int y = 0; y < height; y++) {
			s += (y + "\t|");
			for(int x = 0; x < width; x++) {
				Tile t = board[x][y];
//				System.out.println("tile @" + x + ", " + y + ". isVisible=" + t.isVisible() + ". isMarked=" + t.isMarked() + ". isBomb=" + t.isBomb());
				if(t.isVisible() || t.isMarked()) {
					if(t.getLoc().getX() == x1 && t.getLoc().getY() == y1) {
						s += ("#|");
					}
					else if(t.isMarked()) {
						s += (">|");
					}
					else if(t.isBomb()) {
						s += ("*|");
					}
					else if(t.getValue() == 0) {
						s += ("-|");
					}
					else {
						s += (t.getValue() + "|");
//						System.out.println("Printed value @" + x + ", " + y);
					}
				}
				else {
					s += (" |");
				}
			}
		
			s += "\n";
			s += (".       ");
			for(int i = 0; i < width * 2; i++) {
				s += ("-");
			}
			s += "\n";
		}
		System.out.println(s);
	}
	
	public void selectTile(int x, int y) {
//		System.out.println("Selected tile @" + x + ", " + y);
		Tile t = getTile(x, y);
		if(t.getValue() == 0) {
			discoverContiguousEmpties(t);
		}
		else if(!t.isBomb()) {
			t.setVisibility(true);
		}
		else if(t.isBomb()){
			throw new RuntimeException("Hit a bomb");
		}
	}
	
	public boolean tileIsSolved(int x, int y) {
		if(getAdjMarked(x, y).size() == getTile(x, y).getValue()) {
			return true;
		}
		else {
			return false;
		}
	}
	
	public void checkIfSolved(int x, int y) {
		if(getAdjMarked(x, y).size() == getTile(x, y).getValue()) {
			getTile(x, y).isSolved = true;
			ArrayList<Tile> toSelect = getAdjUnknown(x, y);
			for(int i = 0; i < toSelect.size(); i++) {
				selectTile(toSelect.get(i).getLoc());
			}
		}
	}
	
	public void checkIfSolved(ArrayList<Tile> tiles) {
		for(int i = 0; i < tiles.size(); i++) {
			checkIfSolved(tiles.get(i).getLoc().getX(), tiles.get(i).getLoc().getY());
		}
	}
	
	public void selectTile(Coordinate coord) {
		Tile t = getTile(coord.getX(), coord.getY());
		if(t.getValue() == 0) {
			discoverContiguousEmpties(t);
		}
		else if(!t.isBomb()) {
			t.setVisibility(true);
		}
		else if(t.isBomb()){
			throw new RuntimeException("Hit a bomb @ " + coord.getX() + ", " + coord.getY());
		}
	}
	
	public void discoverContiguousEmpties(Tile t) {
		t.setVisibility(true);
		ArrayList<Tile> tiles = getTiles(t.getSurroundingCoords());
		for(int i = 0; i < tiles.size(); i++) {
			if(!tiles.get(i).isVisible()) {
				if(tiles.get(i).getValue() == 0) {
					discoverContiguousEmpties(tiles.get(i));
				}
				else {
					tiles.get(i).setVisibility(true);
				}
			}
		}
	}
	
	public int getWidth() {
		return width;
	}
	
	public int getHeight() {
		return height;
	}
}