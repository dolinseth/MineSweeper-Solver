public class Play {
	public static void main(String[] args) {
		Solver solver = new Solver(15, 25, 63);//width, height, numbombs
		solver.solveBoard(false, true);//verbose, slowly
		System.out.println("Solver finished");
		solver.board.printBoard();
		solver.board.setAllVisibility(true);
		solver.board.printBoard();
		if(solver.isSolved()) {
			System.out.println("All bombs found!");
		}
		else {
			System.out.println("Incorrect solution found, something went wrong.");
			solver.printUnmarkedBombLocations();
		}
	}
}
