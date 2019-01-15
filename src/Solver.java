import java.util.ArrayList;
import java.util.Scanner;

public class Solver {
	Board board;
	private final int movesPerSecond = 15;
	private boolean moveMade;
	public Solver(int width, int height, int numBombs) {
		Tile.width = width;
		Tile.height = height;
		board = new Board(width, height, numBombs);
//		Tile.board = board;
//		board.calcValues();
		System.out.println("Solver initialized");
	}
	
	public void solveBoard(boolean verbose, boolean slowly) {
		Scanner scan = new Scanner(System.in);
		board.printBoard();
		waitForUser();
		moveMade = true;
		while(!isSolved()) {
			while(moveMade) {
				moveMade = false;
				for(int x = 0; x < board.getWidth(); x++) {
					for(int y = 0; y < board.getHeight(); y++) {
						Tile t = board.getTile(x, y);
						if(t.isVisible() && !t.isMarked() && t.getValue() != 0) {
							if(verbose) {
								waitForUser();
							}
							solveTile(x, y, verbose, slowly);
						}
					}
				}
			}
			if(!isSolved()){
				if(!verbose) {
					board.printBoard();				
				}
				System.out.println("No further moves found.");
				waitForUser();
				break;
//				System.out.print("Please enter the coordinates of a tile that you would like to select: ");
//				board.selectTile(new Coordinate(scan.nextLine()));
//				moveMade = true;
//				System.out.println();
			}
		}
		
		scan.close(); // close your resource leaks
	}
	
	public void solveTile(int x, int y, boolean verbose, boolean slowly) {
		Tile t = board.getTile(x, y);
		if(t.isVisible() && !t.isSolved) {
			if(verbose || slowly) {
				board.printBoard(x, y);			
			}
			if(verbose) {
				System.out.println("X: " + x);
				System.out.println("Y: " + y);
				System.out.println("T: " + t.getValue());
			}
			if(slowly) {
				try {
					Thread.sleep((int)(1000/movesPerSecond));
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			
			if(t.getValue() > 0){
				int accountedFor = board.getAdjMarked(x, y).size();
				if(t.getValue() - accountedFor == board.getAdjUnknown(x, y).size()) {
					t.isSolved = true;
					moveMade = true;
					
					ArrayList<Tile> toMark = board.getAdjUnknown(x, y);
					for(int i = 0; i < toMark.size(); i++) {
						toMark.get(i).mark();
						ArrayList<Coordinate> subCoords = toMark.get(i).getSurroundingCoords();
						for(int j = 0; j < subCoords.size(); j++) {
							solveTile(subCoords.get(j).getX(), subCoords.get(j).getY(), verbose, slowly);
						}
					}
				}
				else if(t.getValue() == accountedFor) {
					t.isSolved = true;
					moveMade = true;
					
					ArrayList<Tile> toSelect = board.getAdjUnknown(x, y);
					for(int i = 0; i < toSelect.size(); i++) {
						board.selectTile(toSelect.get(i).getLoc());
					}
				}
				else {
					ArrayList<Tile> adjacents = board.getAdjNonZero(x, y);
					for(int i = 0; i < adjacents.size(); i++) {
						Tile adjacent = adjacents.get(i);
						if(listContainsOtherList(board.getSurroundingTiles(adjacent.getLoc().getX(), adjacent.getLoc().getY()), board.getAdjUnknown(x, y))){
							int adjacentAccountedFor = board.getAdjMarked(adjacent.getLoc().getX(), adjacent.getLoc().getY()).size();
							int x2 = adjacents.get(i).getLoc().getX();
							int y2 = adjacents.get(i).getLoc().getY();
							if(adjacent.getValue() - adjacentAccountedFor == t.getValue() - accountedFor) {
								ArrayList<Tile> toSelect = subtractLists(board.getAdjUnknown(x2, y2), board.getAdjUnknown(x, y));
								for(int j = 0; j < toSelect.size(); j++) {
									board.selectTile(toSelect.get(j).getLoc());
								}
							}
							else if(adjacent.getValue() - adjacentAccountedFor - (t.getValue() - accountedFor) == subtractLists(board.getAdjUnknown(x2, y2), board.getAdjUnknown(x, y)).size()){
								ArrayList<Tile> toMark = subtractLists(board.getAdjUnknown(x2, y2), board.getAdjUnknown(x, y));
								for(int j = 0; j < toMark.size(); j++) {
									toMark.get(j).mark();
								}
							}
						}
					}
				}
			}
		}
	}
	
//	private boolean allScenariosSolveBoth(ArrayList<Scenario> scenarios, int x1, int y1, int x2, int y2) {
//		boolean result = true;
//		for(int i = 0; i < scenarios.size(); i++) {
//			if(!scenarioSolvesBoth(scenarios.get(i), x1, y1, x2, y2)) {
//				result = false;
//			}
//		}
//		
//		return result;
//	}
//	
//	private boolean scenarioSolvesBoth(Scenario scenario, int x1, int y1, int x2, int y2) {
//		Board testBoard = new Board(board);
//		ArrayList<RelativeCoordinate> relCoords = scenario.getRelCoords();
//		for(int i = 0; i < relCoords.size(); i++) {
//			testBoard.getTile(x1, y1, relCoords.get(i)).mark();
//		}
//		if(testBoard.tileIsSolved(x2, y2) && testBoard.tileIsSolved(x1, y1)) {
//			for(int i = 0; i < relCoords.size(); i++) {
//				
//			}
//			return true;
//		}
//		else {
//			return false;
//		}
//	}
	
	private boolean listContainsOtherList(ArrayList<Tile> tiles1, ArrayList<Tile> tiles2) {
		boolean result = true;
		for(int a = 0; a < tiles2.size(); a++) {
			if(!listContainsElement(tiles1, tiles2.get(a))) {
				result = false;
			}
		}
		
		return result;
	}
	
	private boolean listContainsElement(ArrayList<Tile> tiles, Tile t) {
		boolean result = false;
		for(int i = 0; i < tiles.size(); i++) {
			if(tiles.get(i).equals(t)){
				result = true;
			}
		}
		
		return result;
	}
	
	private ArrayList<Tile> subtractLists(ArrayList<Tile> tiles1, ArrayList<Tile> tiles2){
		ArrayList<Tile> result = tiles1;
		for(int b = 0; b < tiles2.size(); b++) {
			for(int a = 0; a < result.size(); a++) {
				if(result.get(a).equals(tiles2.get(b))){
					result.remove(a);
					a--;
					break;
				}
			}
		}
		
		return result;
	}
	
//	private ArrayList<Scenario> genSingleBombScenarios(int x, int y) {
//		ArrayList<Scenario> scenarios = new ArrayList<Scenario>();
//		ArrayList<Tile> unknown = board.getAdjUnknown(x, y);
//		for(int i = 0; i < unknown.size(); i++) {
//			scenarios.add(new Scenario(x, y, new RelativeCoordinate(x, y, unknown.get(i).getLoc().getX(), unknown.get(i).getLoc().getY())));
//		}
//		
//		return scenarios;
//	}
		
	public boolean isSolved() {
		boolean result = true;
		for(int x = 0; x < board.getWidth(); x++) {
			for(int y = 0; y < board.getHeight(); y++) {
				if(board.getTile(x, y).isMarked() != board.getTile(x, y).isBomb()) {
					result = false;
				}
			}
		}
		
		return result;
	}
	
	private void waitForUser() {
		Scanner scan = new Scanner(System.in);
		System.out.println("Press enter to continue...");
		try{
			scan.nextLine();
		}
		catch(java.util.NoSuchElementException e) {
			//nothing done here on purpose
		}
		scan.close();
	}
	
	public void printUnmarkedBombLocations() {
		for(int x = 0; x < board.getWidth(); x++) {
			for(int y = 0; y < board.getHeight(); y++) {
				Tile t = board.getTile(x, y);
				if(t.isBomb() && !t.isMarked()) {
					System.out.println("Unmarked bomb @ " + t.getLoc().toString());
				}
			}
		}
	}
}