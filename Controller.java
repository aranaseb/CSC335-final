import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Controller {

    Board board;
    private int score;
    Random r = new Random(0);

    private Leaderboard leaderboard;

    public Controller (Board theBoard) {
        /**
         * Creates a Board2048 model for the program. It initializes the grid, score,
         * and ArrayList of empty spaces that are used to generate new tiles.
         * Creates a grid of the size that is requested by the user.
         *
         * @param theSize - The size of the grid to create for the user
         */

        board = theBoard;
        score = 0;

        leaderboard = new Leaderboard("leaderboard.txt");

        setup();
    }

    public List<Integer> getLeaderboard(){
        return leaderboard.getScores();  // Still NOT an escaping reference (it's unmodifiable)!!
    }

    public void saveScore(int score) {
        /**
         * Adds a score to the leaderboard and updates the corresponding text file
         * @param score - score to be saved
         */
        leaderboard.addScore(score);
        leaderboard.save();
    }

    public int getScore() {
        /**
         * Reutrns the current score that the user has.
         *
         * @return user's current score (int)
         */
        return score;
    }

    public GameStatus getStatus(){
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
        if (board.has2048()){
            return GameStatus.WIN;
        }
        if (!board.isFull()){  // if 'empty' has anything then a move is possible
            return GameStatus.IN_PROGRESS;
        }
        if (doesMerge()){  // if a merge is possible then game is not over
            return GameStatus.IN_PROGRESS;
        }
        // No moves are possible. The game is over.
        return GameStatus.LOSS;
    }

    public int getTileAt(int i, int j) {
        /**
         * @param i
         * @param j
         *
         * @return the entry at the (i, j) position
         */
        return board.get(i, j);
    }

    public int getSize() {
        /**
         * @return the size of the board
         */
        return board.size();
    }

    public void up(){
        /**
         * Executes the user command for UP
         */
        moveTilesUp();  // puts all existing tiles in upper-most spots

        for (int i=0; i<board.size()-1; i++){
            for (int j=0; j<board.size(); j++){
                if (board.get(i, j) != 0){
                    if (board.get(i, j) ==  board.get(i+1, j)) {
                        merge(i, j, Direction.UP);
                    }
                }
            }
        }
        // fill any holes created by merging by moving tiles up again
        moveTilesUp();
        if (!board.isFull()){
            newRandomTile();
        }
    }

    public void down(){
        /**
         * Executes the user command for DOWN
         */
        moveTilesDown();  // puts all existing tiles in lower-most spots

        for (int i=board.size()-1; i>0; i--){
            for (int j=0; j<board.size(); j++){
                if (board.get(i, j) != 0){
                    // check if a merge is required
                    if (board.get(i, j) == board.get(i-1, j)){
                        merge(i, j, Direction.DOWN);
                    }
                }
            }
        }
        // fill any holes created by merging by moving tiles down again
        moveTilesDown();
        if (!board.isFull()){
            newRandomTile();
        }
    }

    public void left(){
        /**
         * Executes the user command for LEFT
         */
        moveTilesLeft();  // puts all existing tiles in left-most spots

        for (int i=0; i<board.size(); i++){
            for (int j=0; j<board.size()-1; j++){
                if (board.get(i, j) != 0){
                    if (board.get(i, j) == board.get(i, j+1)){
                        merge(i, j, Direction.LEFT);
                    }
                }
            }
        }
        // fill any holes created by merging by moving tiles left again
        moveTilesLeft();
        if (!board.isFull()){
            newRandomTile();
        }
    }

    public void right(){
        /**
         * Executes the user command for RIGHT
         */
        moveTilesRight();  // puts all existing tiles in right-most spots

        for (int i=0; i<board.size(); i++){
            for (int j=board.size()-1; j>0; j--){
                if (board.get(i, j) != 0){
                    if (board.get(i, j) == board.get(i, j-1)){
                        merge(i, j, Direction.RIGHT);
                    }
                }
            }
        }
        // fill any holes created by merging by moving tiles right again
        moveTilesRight();
        if (!board.isFull()){
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

        newRandomTile();
        newRandomTile();
    }

    private void initializeTile(int i, int j){
        /**
         * Initializes a single tile at the spot board.get(i, j on
         * the game board. Creates a new number on the board in
         * that given spot with a 7/10 probability of getting a
         * 2 (and 3/10 of getting a 4).
         * Note: does NOT remove the space from 'empty' here
         *
         * @param i: the i in board.get(i, j of the empty tile to initialize
         * @param j: the j in board.get(i, j of the empty tile to initialize
         */
        assert (board.get(i, j) == 0);
        // 7/10 probability of getting a 2
        int options[] = {2,2,2,2,2,2,2,4,4,4};
        board.set(options[r.nextInt(0,9)], i, j);
    }

    private void merge(int i, int j, Direction dir){
        /**
         * Merges two identical tiles while moving in the given
         * direction, dir.
         *
         * @param i: in board.get(i, j
         * @param j: in board.get(i, j
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
        assert (board.get(mi, mj) == board.get(i, j));  // Should NEVER be different values

        int newValue = 2*board.get(i, j);
        // 1. combine the tiles into a new tile in spot [i,j]
        board.set(newValue, i, j);
        // 2. remove the merged tile from the grid
        board.set(0, mi, mj);
        // 3. add the merged space to the zeros
        int[] addArg = {mi, mj};
        // 4. update score ("add the sum of the two tiles to the total score")
        score += newValue;
    }

    private void moveTilesUp(){
        /**
         * Moves all of the tiles in the grid to the upper-most positions.
         */
        for (int i=0; i<board.size(); i++){
            for (int j=0; j<board.size(); j++){
                // for all spaces that have existing tiles/values:
                if (board.get(i, j) != 0){
                    int k = i;
                    // find top-most empty spot:
                    while ((k!=0) && (board.get(k-1, j) == 0)){
                        k--;
                    }
                    board.moveTile(i, j, k, j);
                }
            }
        }
    }

    private void moveTilesDown(){
        /**
         * Moves all of the tiles in the grid to the bottom-most positions.
         */
        for (int i=board.size()-1; i>=0; i--){
            for (int j=0; j<board.size(); j++){
                // for all spaces that have existing tiles/values:
                if (board.get(i, j) != 0){
                    int k = i;
                    // find top-most empty spot:
                    while ((k!=board.size()-1) && (board.get(k+1, j) == 0)){
                        k++;
                    }
                    board.moveTile(i, j, k, j);
                }
            }
        }
    }

    private void moveTilesRight(){
        /**
         * Moves all of the tiles in the grid to the right-most positions.
         */
        for (int i=0; i<board.size(); i++){
            for (int j=board.size()-1; j>=0; j--){  // start TOP RIGHT
                // for all spaces that have existing tiles/values:
                if (board.get(i, j) != 0){
                    int k = j;
                    // find top-most empty spot:
                    while ((k!=board.size() - 1) && (board.get(i, k+1) == 0)){
                        k++;
                    }
                    board.moveTile(i, j, i, k);
                }
            }
        }
    }

    private void moveTilesLeft(){
        /**
         * Moves all of the tiles in the grid to the left-most positions.
         */
        for (int i=0; i<board.size(); i++){
            for (int j=0; j<board.size(); j++){  // start TOP LEFT
                // for all spaces that have existing tiles/values:
                if (board.get(i, j) != 0){
                    int k = j;
                    // find left-most empty spot:
                    while ((k!=0) && (board.get(i, k-1) == 0)){
                        k--;
                    }
                    board.moveTile(i, j, i, k);
                }
            }
        }
    }

    private void newRandomTile(){
        /**
         * Adds a new random tile on an empty space. Uses the 'empty'
         * ArrayList to find an empty space on the board.
         */
        assert (!board.isFull());;
        int[] coords = board.getEmptyIndex();
        initializeTile(coords[0], coords[1]);
    }

    private Boolean doesMerge(){
        /**
         * This returns whether or not a merge would occur if the user
         * provided a move in some/any direction.
         *
         * @return: Whether a move/merge is possible in the game board
         */

        for (int i=0; i<board.size()-1; i++){
            for (int j=0; j<board.size()-1; j++){
                if (board.get(i, j) != 0){
                    if (board.get(i, j) == board.get(i+1, j)){  // check vertical equality
                        return true;
                    }
                    if (board.get(i, j) == board.get(i,j+1)){   // check horizontal equality
                        return true;
                    }
                }
            }
        }

        for (int i=0; i<board.size()-1; i++){
            // covers bottom row (that isn't checked by prev loop)
            if (board.get(board.size()-1, i) == board.get(board.size()-1, i+1)){
                return true;
            }

            // right row (that isn't checked by prev loop)
            if (board.get(i, board.size()-1) == board.get(i+1, board.size()-1)){
                return true;
            }
        }

        return false;
    }

//    @Override
//    public String toString(){
//        /**
//         * For testing, not (at least currently) meant to be used
//         * in the implementation of the game.
//         *
//         * @return: String representation of the game board ('grid')
//         */
//        String output = "[ ";
//        for (int i=0; i<board.size(); i++){
//            if (i!=0){
//                output += "  ";
//            }
//            output += "[";
//            for (int j : grid[i]){
//                if (j==0){
//                    output += "     ";
//                } else{
//                    output += String.format("%4d", j);
//                    output += " ";
//                }
//            }
//            output = output.substring(0, output.length()-1);
//            output += "]\n";
//        }
//        output += " ]";
//        return output;
//    }
}
