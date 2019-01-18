public class PlayMultiple {
	public static void main(String[] args) {
		int numPuzzles = 3;
		int numSucceed = 0;
		for(int i = 0; i < numPuzzles; i++){
				Solver solver = new Solver(15, 15, 50);//width, height, numbombs
				solver.solveBoard(false, false);//verbose, slowly
				System.out.println("Puzzle " + i + " finished\nBoard as solved");
				solver.board.printBoard();
				solver.board.setAllVisibility(true);
				System.out.println("Real Board");
				solver.board.printBoard();
				if(solver.isSolved()) {
					System.out.println("All bombs found!");
					numSucceed++;
				}
				else {
					System.out.println("Incorrect solution found, something went wrong.");
					solver.printUnmarkedBombLocations();
				}
		}
		
		System.out.println("Finished solving " + numPuzzles + " boards\n" + numSucceed + " succeeded\n" + (numPuzzles - numSucceed) + " failed\n" + ((float)numSucceed/numPuzzles * 100) + "% correctly solved");
	}
}
