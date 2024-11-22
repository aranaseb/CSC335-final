/**
 * This class provides an interface between the GUI and the model
 * @author Winston Reese
 */

public class Controller2048 {

    private final Board2048 board;

    public Controller2048(Board2048 board) {
        this.board = board;
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

    public boolean isOver() {
        /**
         * @returns whether there is a possible move based on the state of
         * the board
         * TODO implement this
         */
        return false;
    }
}
