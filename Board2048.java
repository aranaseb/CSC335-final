
public class Board2048 {
	private int[][] grid;

	public Board2048(int theSize) {
		grid = new int[theSize][theSize];
		grid[2][0] = 2;
		grid[1][2] = 2;
	}

	public int[][] getGrid() {
		return grid;
	}
}
