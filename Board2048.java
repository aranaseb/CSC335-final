import java.util.ArrayList;
import java.util.Random;

public class Board2048 {
	private int[][] grid;
	private int score;
	private ArrayList<int[]> empty;  // list of [i,j] coordinates of zeros/empty

	public Board2048(int theSize) {
		/**
		 * Creates a Board2048 model for the program. It initializes the grid, score,
		 * and ArrayList of empty spaces that are used to generate new tiles. 
		 * Creates a grid of the size that is requested by the user. 
		 * 
		 * @param theSize - The size of the grid to create for the user
		 */
		grid = new int[theSize][theSize];
		score = 0;
		empty = new ArrayList<int[]>();
		
		setup();
	}

	public int getScore() {
		/**
		 * Reutrns the current score that the user has.
		 * 
		 * @return user's current score (int)
		 */
		return score;
	}

	public GameStatus getGameStatus(){
		/**
		 * Returns whether the game is over. 
		 * Game is over when (checked in the following order):
		 * 		1. user reaches 2048 (WIN)
		 * 		2. 'empty' is empty
		 * 			  &&
		 * 		3. no direction's move would cause a merge at any point (LOSS)
		 * 
		 * @return: GameStatus of the user's game currently in progress
		 */
		if (has2048()){
			return GameStatus.WIN;
		}

		if (!empty.isEmpty()){  // if 'empty' has anything then a move is possible
			return GameStatus.IN_PROGRESS;  // game is not over
		}
		if (doesMerge()){
			return GameStatus.IN_PROGRESS;  // game is not over
		}
		// No moves are possible. The game is over. 
		return GameStatus.LOSS;
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
		if (empty.size() != 0){
			newRandomTile();
		}
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
		if (empty.size() != 0){
			newRandomTile();
		}
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
		if (empty.size() != 0){	
			newRandomTile();
		}
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
		if (empty.size() != 0){
			newRandomTile();
		}
	}


	// Private Methods:

	private void setup(){
		/**
		 * Sets up the grid for the game by initializing the first two tiles
		 * and adding the rest of the coordinates in the grid to the
		 * ArrayList of empty grid spaces. 
		 */
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
		/**
		 * Initializes a single tile at the spot grid[i][j] on
		 * the game board. Creates a new number on the board in
		 * that given spot with a 7/10 probability of getting a
		 * 2 (and 3/10 of getting a 4).
		 * Note: does NOT remove the space from 'empty' here
		 * 
		 * @param i: the i in grid[i][j] of the empty tile to initialize
		 * @param j: the j in grid[i][j] of the empty tile to initialize
		 */
		assert (grid[i][j]==0);
		Random r = new Random();
		// 7/10 probability of getting a 2
		int options[] = {2,2,2,2,2,2,2,4,4,4};
		grid[i][j] = options[r.nextInt(0,9)];
	}

	private void moveTile(int oldI, int oldJ, int newI, int newJ){
		/**
		 * Moves the tile from the position grid[oldI][oldJ] to the
		 * position grid[newI][newJ] on the game board. Also updates
		 * 'empty' accordingly. 
		 * 
		 * @param oldI: old/prev value of i in grid[i][j]
		 * @param oldJ: old/prev value of j in grid[i][j]
		 * @param newI: new value of i in grid[i][j]
		 * @param newJ: new value of j in grid[i][j]
		 */
		if ((oldI==newI) && (oldJ==newJ)){
			return;
		}
		grid[newI][newJ] = grid[oldI][oldJ];
		grid[oldI][oldJ] = 0;

		int findIdx = indexInEmpty(newI, newJ);
		assert (findIdx != -1);
		empty.remove(findIdx);

		int[] arg2 = {oldI, oldJ};
		empty.add(arg2);
	}
	
	private void merge(int i, int j, Direction dir){
		/**
		 * Merges two identical tiles while moving in the given
		 * direction, dir. 
		 * 
		 * @param i: in grid[i][j]
		 * @param j: in grid[i][j]
		 * @param dir: the move's/merge's Direction
		 */

		int mi;
		int mj;
		
		mi = 0;
		mj = 0;
		assert (dir != null);
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
	}

	private void moveTilesUp(){
		/**
		 * Moves all of the tiles in the grid to the upper-most positions.
		 */
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
		/**
		 * Moves all of the tiles in the grid to the bottom-most positions.
		 */
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
		/**
		 * Moves all of the tiles in the grid to the right-most positions.
		 */
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
		/**
		 * Moves all of the tiles in the grid to the left-most positions.
		 */
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
		 * Adds a new random tile on an empty space. Uses the 'empty'
		 * ArrayList to find an empty space on the board.
		 */
		assert (!empty.isEmpty());
		int rint = (new Random()).nextInt(empty.size());
		int[] coords = empty.get(rint);
		initializeTile(coords[0], coords[1]);
		empty.remove(rint);
	}

	private Boolean doesMerge(){
		/**
		 * This returns whether or not a merge would occur if the user
		 * provided a move in some/any direction. 
		 * 
		 * @return: Whether a move/merge is possible in the game board
		 */

		for (int i=0; i<grid.length-1; i++){
			for (int j=0; j<grid.length-1; j++){
				if (grid[i][j] != 0){
					if (grid[i][j] == grid[i+1][j]){  // check vertical equality
						return true;
					}
					if (grid[i][j]==grid[i][j+1]){   // check horizontal equality
						return true;
					}
				}
			}
		}
		
		for (int i=0; i<grid.length-1; i++){
			// covers bottom row (that isn't checked by prev loop)
			if (grid[grid.length-1][i] == grid[grid.length-1][i+1]){
				return true;
			}

			// right row (that isn't checked by prev loop)
			if (grid[i][grid.length-1] == grid[i+1][grid.length-1]){
				return true;
			}
		}
		
		return false;
	}

	private int indexInEmpty(int i, int j) {
		/**
		 * Returns the index of the array [i,j] in the 'empty' ArrayList.
		 * Works similarly to ArrayList.indexOf(): returns -1 if it does
		 * not exist in the list. 
		 * 
		 * @param i: i in grid[i][j]
		 * @param j: j in grid[i][j]
		 * @return: index of the coords in 'empty' (as int)
		 */
		for (int idx=0; idx<empty.size(); idx++){
			int[] coords = empty.get(idx);
			if ((coords[0]==i) && (coords[1]==j)){
				return idx;
			}
		}
		return -1;
	}
	
	private Boolean has2048(){
		/**
		 * Returns whether or not 2048 exists in the board.
		 * 
		 * @return: whether 2048 exists in the game board (Bool)
		 */
		for (int i=0; i<grid.length; i++){
			for (int j=0; j<grid.length; j++){
				if (grid[i][j] == 2048){
					return true;
				}
			}
		}
		return false;
	}

	@Override
	public String toString(){
		/**
		 * For testing, not (at least currently) meant to be used 
		 * in the implementation of the game. 
		 * 
		 * @return: String representation of the game board ('grid')
		 */
		String output = "[ ";
		for (int i=0; i<grid.length; i++){
			if (i!=0){
				output += "  ";
			}
			output += "[";
			for (int j : grid[i]){
				if (j==0){
					output += "     ";
				} else{
					output += String.format("%4d", j);
					output += " ";
				}
			}
			output = output.substring(0, output.length()-1);
			output += "]\n";
		}
		output += " ]";
		return output;
	}
}
