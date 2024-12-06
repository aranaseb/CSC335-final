import java.util.ArrayList;
import java.util.Random;

public class Board {
    private int[][] grid;
    private ArrayList<int[]> empty; // list of [i,j] coordinates of zeros/empty
    private Random r;

    public Board(int theSize) {
        /**
         * Creates a Board2048 model for the program. It initializes the grid, score,
         * and ArrayList of empty spaces that are used to generate new tiles.
         * Creates a grid of the size that is requested by the user.
         *
         * @param theSize - The size of the grid to create for the user
         */

        grid = new int[theSize][theSize];
        empty = new ArrayList<int[]>();
        r = new Random();
        System.out.println("Board constructor");

        for (int i = 0; i < theSize; i++) {
            for (int j = 0; j < theSize; j++) {
                empty.add(new int[] {i, j});
            }
        }
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

    public void set(int newVal, int i, int j) {
        grid[i][j] = newVal;

        if (newVal != 0 && indexInEmpty(i, j) != -1) {
            empty.remove(indexInEmpty(i, j));
        }
        if (newVal == 0 && indexInEmpty(i, j) == -1) {
            empty.add(new int[] {i, j});
        }
    }

    public int size() {
        /**
         * @return the size of the board
         */
        return grid.length;
    }

    public boolean isFull() {
        return empty.size() == 0;
    }

    public void moveTile(int oldI, int oldJ, int newI, int newJ){
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

        set(get(oldI, oldJ), newI, newJ);
        set(0, oldI, oldJ);
    }

    public Boolean has2048(){
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

    public int[] getEmptyIndex() {
        return empty.get(r.nextInt(empty.size()));
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
}
