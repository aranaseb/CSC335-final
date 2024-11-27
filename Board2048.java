
/**
 * NOTES:
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
	private ArrayList<int[]> empty;  // list of [i,j] coordinates of zeros/empty

	public Board2048(int theSize) {
		grid = new int[theSize][theSize];
		score = 0;
		empty = new ArrayList<int[]>();
		
		setup();
		// runTest();
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

	public void up(){
		/**
		 * Executes the user command for UP
		 */
		moveTilesUp();  // puts all existing tiles in upper-most spots

		for (int i=0; i<grid.length-1; i++){
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
		newRandomTile();
	}

	public void down(){
		/**
		 * Executes the user command for DOWN
		 */
		moveTilesDown();  // puts all existing tiles in lower-most spots

		for (int i=grid.length-1; i>0; i--){
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
		newRandomTile();
	}

	public void left(){
		/**
		 * Executes the user command for LEFT
		 */
		moveTilesLeft();  // puts all existing tiles in left-most spots

		for (int i=0; i<grid.length; i++){
			for (int j=0; j<grid.length-1; j++){
				if (grid[i][j] != 0){
					if (grid[i][j] == grid[i][j+1]){
						merge(i, j, Direction.LEFT);
					}
				}
			}
		}
		// fill any holes created by merging by moving tiles left again
		moveTilesLeft();		
		newRandomTile();
	}

	public void right(){
		/**
		 * Executes the user command for RIGHT
		 */
		moveTilesRight();  // puts all existing tiles in right-most spots

		for (int i=0; i<grid.length; i++){
			for (int j=grid.length-1; j>0; j--){
				if (grid[i][j] != 0){
					if (grid[i][j] == grid[i][j-1]){
						merge(i, j, Direction.RIGHT);
					}
				}
			}
		}
		// fill any holes created by merging by moving tiles right again
		moveTilesRight();
		newRandomTile();
	}

	private void runTest(){
		System.out.println(toString() + "\n\nDOWN");
		down();
		System.out.println(toString() + "\n\nRIGHT");
		right();
		System.out.println(toString() + "\n\nLEFT");
		left();
		System.out.println(toString() + "\n\nUP");
		up();
		System.out.println(toString() + "\n\nDOWN");
		down();
		System.out.println(toString() + "\n\nRIGHT");
		right();
		System.out.println(toString() + "\n\nUP");
		up();
		System.out.println(toString() + "\n\nLEFT");
		left();
		System.out.println(toString());
	}

	private void setup(){
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
		int findIdx = indexInEmpty(newI, newJ);
		if (findIdx != -1){
			empty.remove(findIdx);
		}
		int[] arg2 = {oldI, oldJ};
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

		int mi;
		int mj;
		
		mi = 0;
		mj = 0;
		assert (dir != null);  // this shouldn't happen
		if (dir == Direction.UP){
			mi = i+1;
			mj = j;
		} else if (dir == Direction.DOWN) {
			mi = i-1;
			mj = j;
		} else if (dir == Direction.LEFT) {
			mi = i;
			mj = j+1;
		} else {  // dir == RIGHT
			mi = i;
			mj = j-1;
		}
		// 0. assert [i,j]==[mi,mj]
		assert (grid[mi][mj]==grid[i][j]);  // Should NEVER be different values

		int newValue = 2*grid[i][j];
		// 1. combine the tiles into a new tile in spot [i,j]
		grid[i][j] = newValue;
		// 2. remove the merged tile from the grid
		grid[mi][mj] = 0;
		// 3. add the merged space to the zeros
		int[] addArg = {mi, mj};
		empty.add(addArg);
		// 4. update score ("add the sum of the two tiles to the total score")
		score += newValue;
		System.out.println("* Score updated: " + Integer.toString(score));
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

	private void newRandomTile(){
		/**
		 * Adds a new random tile on an empty space
		 * 
		 * Returns an int if it fails (make void if unused)
		 */
		assert (!empty.isEmpty());
		int rint = (new Random()).nextInt(empty.size());
		// maybe something is wrong with the .size()?? i.e. maybe it's len of space rather than list????!??!?!
		int[] coords = empty.get(rint);
		initializeTile(coords[0], coords[1]);
		empty.remove(rint);  // make sure this is removing INDEX rint, not object/value of rint
	}

	private int indexInEmpty(int i, int j) {
		/**
		 * Returns the index of the array [i,j] in the 'empty' ArrayList.
		 * Works similarly to ArrayList.indexOf(): returns -1 if it does
		 * not exist in the list. 
		 */
		for (int idx=0; idx<empty.size(); idx++){
			int[] coords = empty.get(idx);
			if ((coords[0]==i) && (coords[1]==j)){
				return idx;
			}
		}
		return -1;
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
