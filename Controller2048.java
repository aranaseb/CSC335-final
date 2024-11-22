/**
 * This class provides an interface between the GUI and the model
 * @author Winston Reese
 */

public class Controller2048 {

    private final Board2048 board;
    private final Leaderboard leaderboard;

    public Controller2048(Board2048 board) {
        this.board = board;
        // TODO I hardcoded this leaderboard name for now, this could be changed if necessary
        this.leaderboard = new Leaderboard("leaderboard.txt");
    }

    public int getTileAt(int x, int y) {
        /**
         * @param x: the desired x-coordinate
         * @param y: the desired y-coordinate
         *
         * @pre x and y are within the size of the board
         *
         * @returns the value stored at the requested coordinate
         */

        return board.get(x, y);
    }

    public int getSize() {
        /**
         * @return the size of the board
         */
        return board.size();
    }

    public void move(Direction direction) {
        /**
         * Applies a move to the board in the requested direction
         */
        if (direction == Direction.UP)
            board.up();
        else if (direction == Direction.LEFT)
            board.left();
        else if (direction == Direction.DOWN)
            board.down();
        else if (direction == Direction.RIGHT)
            board.right();
    }

    public void saveScore(int score) {
        /**
         * Adds a score to the leaderboard and updates the corresponding text file
         * @param score - score to be saved
         */
        leaderboard.addScore(score);
        leaderboard.save();
    }

    public boolean isOver() {
        /**
         * @returns whether there is a possible move based on the state of
         * the board
         * TODO implement this
         */
        return false;
    }
}
