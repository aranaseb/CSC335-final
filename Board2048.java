
/**
 * NOTES:
 * 			- merging has not been implemented yet!!!!! mostly just moves UDLR
 * 				 - so I also don't know whether or not the merging logic that 
 * 				   already exists in this file works or not (bc can't merge yet)
 * 			- There's a lot of repeated/duplicate code; I'll combine them
 * 			  when I'm confident with the game logic/later on after it actually
 * 			  fully works.
 * 				 - the public funcs (up(), down(), left(), right()) should be
 * 				   exactly the same tho unless we want to change it
 */
import java.util.ArrayList;
import java.util.Random;

public class Board2048 {
	private int[][] grid;
	private int score;
	private ArrayList<int[]> empty; // list of [i,j] coordinates of zeros/empty

	public Board2048(int theSize) {
		grid = new int[theSize][theSize];
		score = 0;
		empty = new ArrayList<int[]>();
		initializeTile(0,0);
		initializeTile(0,1);

		// add the rest of the zeros in top row
		for (int i=2; i<grid.length; i++){
			int[] toAdd = {0, i};
			empty.add(toAdd);
		}
		// add zeros everywhere else
		for (int i=1; i<grid.length; i++){
			for (int j=0; j<grid.length; j++){
				int[] toAdd = {i, j};
				empty.add(toAdd);
			}
		}

		// testing:
		// System.out.println(toString());
		// down();
		// System.out.println(toString());
		// right();
		// System.out.println(toString());
		// left();
		// System.out.println(toString());
		// up();
		// System.out.println(toString());
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

	private void moveTile(int oldI, int oldJ, int newI, int newJ){
		if ((oldI==newI) && (oldJ==newJ)){
			return;
		}
		grid[newI][newJ] = grid[oldI][oldJ];
		grid[oldI][oldJ] = 0;
		int[] arg1 = {oldI, oldJ};
		int findIdx = empty.indexOf(arg1);
		if (findIdx != -1){
			empty.remove(findIdx);
		}
		int[] arg2 = {newI, newJ};
		empty.add(arg2);
	}
	
	private void merge(int i, int j, Direction dir){
		/**
		 * Merges two identical tiles. 
		 * 
		 * @param i
		 * @param j
		 * @param dir
		 */

		// "Should add the sum of the two tiles to the total score"
		score += grid[i][j] * 2;
	}

	private void moveTilesUp(){
		for (int i=0; i<grid.length; i++){
			for (int j=0; j<grid.length; j++){
				// for all spaces that have existing tiles/values:
				if (grid[i][j] != 0){
					int k = i;
					// find top-most empty spot:
					while ((k!=0) && (grid[k-1][j] == 0)){
						k--;
					}
					moveTile(i, j, k, j);
				}
			}
		}
	}

	private void moveTilesDown(){
		for (int i=grid.length-1; i>=0; i--){
			for (int j=0; j<grid.length; j++){
				// for all spaces that have existing tiles/values:
				if (grid[i][j] != 0){
					int k = i;
					// find top-most empty spot:
					while ((k!=grid.length-1) && (grid[k+1][j] == 0)){
						k++;
					}
					moveTile(i, j, k, j);
				}
			}
		}
	}

	private void moveTilesRight(){
		for (int i=0; i<grid.length; i++){
			for (int j=grid.length-1; j>=0; j--){  // start TOP RIGHT
				// for all spaces that have existing tiles/values:
				if (grid[i][j] != 0){
					int k = j;
					// find top-most empty spot:
					while ((k!=grid.length - 1) && (grid[i][k+1] == 0)){
						k++;
					}
					moveTile(i, j, i, k);
				}
			}
		}
	}

	private void moveTilesLeft(){
		for (int i=0; i<grid.length; i++){
			for (int j=0; j<grid.length; j++){  // start TOP LEFT
				// for all spaces that have existing tiles/values:
				if (grid[i][j] != 0){
					int k = j;
					// find left-most empty spot:
					while ((k!=0) && (grid[i][k-1] == 0)){
						k--;
					}
					moveTile(i, j, i, k);
				}
			}
		}
	}

	public void up(){
		/**
		 * Executes the user command for UP
		 */
		moveTilesUp();  // puts all existing tiles in upper-most spots

		for (int i=0; i<grid.length; i++){
			for (int j=0; j<grid.length; j++){
				if (grid[i][j] != 0){
					if (grid[i][j] == grid[i+1][j]){
						merge(i, j, Direction.UP);
					}
				}
			}
		}
		// fill any holes created by merging by moving tiles up again
		moveTilesUp();
		// newRandomTile();
	}

	public void down(){
		/**
		 * Executes the user command for DOWN
		 */
		moveTilesDown();  // puts all existing tiles in lower-most spots

		for (int i=grid.length-1; i>=0; i--){
			for (int j=0; j<grid.length; j++){
				if (grid[i][j] != 0){
					// check if a merge is required
					if (grid[i][j] == grid[i-1][j]){
						merge(i, j, Direction.DOWN);
					}
				}
			}
		}
		// fill any holes created by merging by moving tiles down again
		moveTilesDown();
		// newRandomTile();
	}

	public void left(){
		/**
		 * Executes the user command for LEFT
		 */
		// Do the iteration
		moveTilesLeft();  // puts all existing tiles in left-most spots

		for (int i=0; i<grid.length; i++){
			for (int j=0; j<grid.length; j++){
				if (grid[i][j] != 0){
					if (grid[i][j] == grid[i][j+1]){
						merge(i, j, Direction.LEFT);
					}
				}
			}
		}
		// fill any holes created by merging by moving tiles left again
		moveTilesLeft();		
		// newRandomTile();
	}

	public void right(){
		/**
		 * Executes the user command for RIGHT
		 */
		// Do the iteration
		moveTilesRight();  // puts all existing tiles in right-most spots

		for (int i=0; i<grid.length; i++){
			for (int j=grid.length-1; j>=0; j--){
				if (grid[i][j] != 0){
					if (grid[i][j] == grid[i][j-1]){
						merge(i, j, Direction.RIGHT);
					}
				}
			}
		}
		// fill any holes created by merging by moving tiles right again
		moveTilesRight();
		// newRandomTile();
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
