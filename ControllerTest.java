import static org.junit.jupiter.api.Assertions.*;

import javax.swing.plaf.basic.BasicSplitPaneUI.KeyboardDownRightHandler;

import org.junit.jupiter.api.Test;

class ControllerTest {
	
	Controller controller;
	Board board = new Board(4);
	
	@Test
	void testConstructor() {
		controller = new Controller(board);
		
		assertEquals(0, controller.getScore());
		assertEquals(4, controller.getSize());
		assertEquals(GameStatus.IN_PROGRESS, controller.getStatus());
		
		int count = 0;
		
		for (int i = 0; i < 4; i++) {
			for (int j = 0; j < 4; j++) {
				if (controller.getTileAt(i, j) != 0)
					count++;
			}
		}
		
		assertEquals(2, count);
	}

	@Test
	void testGameStatus() {
		controller = new Controller(board);
		
		assertEquals(GameStatus.IN_PROGRESS, controller.getStatus());
		
		for (int i = 0; i < 4; i++) {
			for (int j = 0; j < 4; j++) {
				board.set(4, i, j);
			}
		}
		
		assertEquals(GameStatus.IN_PROGRESS, controller.getStatus());
		
		board.set(2048, 0, 0);
		
		assertEquals(GameStatus.WIN, controller.getStatus());
		
		fillBoard(board);
		
		assertEquals(GameStatus.LOSS, controller.getStatus());
		
		// test full but possible to merge
		
		board.set(1, 0, 1);
		assertEquals(GameStatus.IN_PROGRESS, controller.getStatus());
		fillBoard(board);
		
		board.set(1, 1, 0);
		assertEquals(GameStatus.IN_PROGRESS, controller.getStatus());
		fillBoard(board);
		
		
		board.set(16, 3, 2);
		assertEquals(GameStatus.IN_PROGRESS, controller.getStatus());
		fillBoard(board);
		
		board.set(16, 2, 3);
		assertEquals(GameStatus.IN_PROGRESS, controller.getStatus());
	}
	
	@Test
	void testUp() {
		int count = 0;
		
		controller = new Controller(board);
		
		emptyBoard(board);
		
		controller.up();
		
		for (int i = 0; i < 4; i++) {
			for (int j = 0; j < 4; j++) {
				if (controller.getTileAt(i, j) != 0)
					count++;
			}
		}
		
		assertEquals(1, count);

		emptyBoard(board);
		
		board.set(3, 0, 0);
		board.set(3, 1, 0);
		
		board.set(3, 2, 0);
		board.set(3, 3, 0);
		
		board.set(7, 3, 3);
		board.set(7, 2, 3);
		
		board.set(5, 2, 2);
		board.set(9, 1, 2);
		
		controller.up();
		
		assertEquals(6, controller.getTileAt(0, 0));
		assertEquals(6, controller.getTileAt(1, 0));
		assertEquals(14, controller.getTileAt(0, 3));
		assertEquals(9, controller.getTileAt(0, 2));
		assertEquals(5, controller.getTileAt(1, 2));
		
		controller.up();
		
		assertEquals(12, controller.getTileAt(0, 0));
		assertEquals(14, controller.getTileAt(0, 3));
		assertEquals(9, controller.getTileAt(0, 2));
		assertEquals(5, controller.getTileAt(1, 2));
		
		fillBoard(board);
		
		controller.up();
		
		// test that nothing changed
		count = 1;
		
		for (int i = 0; i < 4; i++) {
			for (int j = 0; j < 4; j++) {
				assertEquals(count, controller.getTileAt(i, j));
				count++;
			}
		}
	}
	
	@Test
	void testDown() {
		int count = 0;
		
		controller = new Controller(board);
		
		emptyBoard(board);
		
		controller.down();
		
		for (int i = 0; i < 4; i++) {
			for (int j = 0; j < 4; j++) {
				if (controller.getTileAt(i, j) != 0)
					count++;
			}
		}
		
		assertEquals(1, count);
		
		emptyBoard(board);
		
		board.set(3, 0, 0);
		board.set(3, 1, 0);
		
		board.set(3, 2, 0);
		board.set(3, 3, 0);
		
		board.set(7, 3, 3);
		board.set(7, 2, 3);
		
		board.set(5, 2, 2);
		board.set(9, 1, 2);
		
		controller.down();
		
		assertEquals(6, controller.getTileAt(3, 0));
		assertEquals(6, controller.getTileAt(2, 0));
		assertEquals(14, controller.getTileAt(3, 3));
		assertEquals(5, controller.getTileAt(3, 2));
		assertEquals(9, controller.getTileAt(2, 2));
		
		controller.down();
		
		assertEquals(12, controller.getTileAt(3, 0));
		assertEquals(14, controller.getTileAt(3, 3));
		assertEquals(5, controller.getTileAt(3, 2));
		assertEquals(9, controller.getTileAt(2, 2));
		
		fillBoard(board);
		
		controller.down();
		
		// test that nothing changed
		count = 1;
		
		for (int i = 0; i < 4; i++) {
			for (int j = 0; j < 4; j++) {
				assertEquals(count, controller.getTileAt(i, j));
				count++;
			}
		}
	}
	
	@Test
	void testRight() {
		int count = 0;
		
		controller = new Controller(board);
		
		emptyBoard(board);
		
		controller.right();
		
		for (int i = 0; i < 4; i++) {
			for (int j = 0; j < 4; j++) {
				if (controller.getTileAt(i, j) != 0)
					count++;
			}
		}
		
		assertEquals(1, count);
		
		count = 0;
		emptyBoard(board);
		
		board.set(3, 0, 0);
		board.set(3, 0, 1);
		
		board.set(3, 0, 2);
		board.set(3, 0, 3);
		
		board.set(7, 3, 3);
		board.set(7, 3, 2);
		
		board.set(5, 2, 2);
		board.set(9, 2, 1);
		
		controller.right();
		
		assertEquals(6, controller.getTileAt(0, 3));
		assertEquals(6, controller.getTileAt(0, 2));
		assertEquals(14, controller.getTileAt(3, 3));
		assertEquals(9, controller.getTileAt(2, 2));
		assertEquals(5, controller.getTileAt(2, 3));
		
		controller.right();
		
		assertEquals(12, controller.getTileAt(0, 3));
		assertEquals(14, controller.getTileAt(3, 3));
		assertEquals(9, controller.getTileAt(2, 2));
		assertEquals(5, controller.getTileAt(2, 3));
		
		fillBoard(board);
		
		controller.right();
		
		// test that nothing changed
		count = 1;
		
		for (int i = 0; i < 4; i++) {
			for (int j = 0; j < 4; j++) {
				assertEquals(count, controller.getTileAt(i, j));
				count++;
			}
		}
	}
	
	@Test
	void testLeft() {
		int count = 0;
		
		controller = new Controller(board);
		
		emptyBoard(board);
		
		controller.left();
		
		for (int i = 0; i < 4; i++) {
			for (int j = 0; j < 4; j++) {
				if (controller.getTileAt(i, j) != 0)
					count++;
			}
		}
		
		assertEquals(1, count);
		
		count = 0;
		emptyBoard(board);
		
		board.set(3, 0, 0);
		board.set(3, 0, 1);
		
		board.set(3, 0, 2);
		board.set(3, 0, 3);
		
		board.set(7, 3, 3);
		board.set(7, 3, 2);
		
		board.set(5, 2, 2);
		board.set(9, 2, 1);
		
		controller.left();
		
		assertEquals(6, controller.getTileAt(0, 0));
		assertEquals(6, controller.getTileAt(0, 1));
		assertEquals(14, controller.getTileAt(3, 0));
		assertEquals(9, controller.getTileAt(2, 0));
		assertEquals(5, controller.getTileAt(2, 1));
		
		controller.left();
		
		assertEquals(12, controller.getTileAt(0, 0));
		assertEquals(14, controller.getTileAt(3, 0));
		assertEquals(9, controller.getTileAt(2, 0));
		assertEquals(5, controller.getTileAt(2, 1));
		
		fillBoard(board);
		
		controller.left();
		
		// test that nothing changed
		count = 1;
		
		for (int i = 0; i < 4; i++) {
			for (int j = 0; j < 4; j++) {
				assertEquals(count, controller.getTileAt(i, j));
				count++;
			}
		}
	}
	
	private void emptyBoard(Board board) {
		for (int i = 0; i < board.size(); i++) {
			for (int j = 0; j < board.size(); j++) {
				board.set(0, i, j);
			}
		}
	}
	
	private void fillBoard(Board board) {
		int count = 1;
		
		for (int i = 0; i < board.size(); i++) {
			for (int j = 0; j < board.size(); j++) {
				board.set(count, i, j);
				count++;
			}
		}
	}
}
