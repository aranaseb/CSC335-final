/*
 * This class provides an interface between the GUI and the model
 * @author Winston Reese
 */

public class Controller2048 {

    private Board2048 board;

    public Controller2048() {

    }

    public int getTileAt(int x, int y) {
        /*
         * @param x: the desired x-coordinate
         * @param y: the desired y-coordinate
         *
         * @pre x and y are within the size of the board
         *
         * @returns the value stored at the requested coordinate
         */
        return 0;
    }

    public void move(Direction direction) {
        /*
         * Applies a move to the board in the requested direction
         */
    }

    public boolean isOver() {
        /*
         * @returns whether there is a possible move based on the state of
         * the board
         */
        return false;
    }
}
