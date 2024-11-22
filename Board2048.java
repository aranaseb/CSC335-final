import java.util.Random;

public class Board2048 {
	private int[][] grid;
	private int score;

	public Board2048(int theSize) {
		grid = new int[theSize][theSize];
		score = 0;
		initializeTile(0,0);
		initializeTile(0,1);
	}

	public int[][] getGrid() {
		return grid;  // escaping reference
	}

	public int get(int i, int j) {
		/**
		 * @param i
		 * @param j
		 *
		 * @return the entry at the (i, j) position
		 */
		return grid[i][j];
	}

	public int size() {
		/**
		 * @return the size of the board
		 */
		return grid.length;
	}

	private void initializeTile(int i, int j){
		// initializes tile at [i,j]
		Random r = new Random();
		// 7/10 probability of getting a 2
		int options[] = {2,2,2,2,2,2,2,4,4,4};
		grid[i][j] = options[r.nextInt(0,9)];
	}

	// change to public (or delete completely) if necessary later
	private void setTile(int i, int j, int val){
		grid[i][j] = val;
	}
	
	private void merge(int i, int j, int dir){
		/**
		 * Merges two identical tiles. 
		 * 
		 * 0=UP, 1=RIGHT, 2=DOWN, 3=LEFT
		 * 
		 * @param i
		 * @param j
		 * @param dir
		 */
	}

	public void up(){
		/**
		 * Executes the user command for UP
		 */
		return;
	}

	public void down(){
		/**
		 * Executes the user command for DOWN
		 */
		return;
	}

	public void left(){
		/**
		 * Executes the user command for LEFT
		 */
		return;
	}

	public void right(){
		/**
		 * Executes the user command for RIGHT
		 */
		return;
	}
	
	@Override
	public String toString(){
		/**
		 * For testing, not (at least currently) meant to be used 
		 * in the implementation of the game. 
		 */
		String output = "[ ";
		for (int i=0; i<grid.length; i++){
			if (i!=0){
				output += "  ";
			}
			output += "[";
			for (int j : grid[i]){
				output += Integer.toString(j) + ",";
			}
			output += "]\n";
		}
		output += " ]";
		return output;
	}
}
