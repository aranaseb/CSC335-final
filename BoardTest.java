import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class BoardTest {
	
	private Board board = new Board(4);

	@Test
	void testSettersAndGetters() {
		for (int i = 0; i < 4; i++) {
			for (int j = 0; j < 4; j++) {
				assertEquals(0, board.get(i, j));
			}
		}
		
		assertEquals(4, board.size());
		assertFalse(board.has2048());
		assertFalse(board.isFull());
		
		for (int i = 0; i < 4; i++) {
			for (int j = 0; j < 4; j++) {
				assertFalse(board.isFull());
				board.set(2048, i, j);
			}
		}
		
		for (int i = 0; i < 4; i++) {
			for (int j = 0; j < 4; j++) {
				assertEquals(2048, board.get(i, j));
			}
		}
	
		assertTrue(board.has2048());
		assertTrue(board.isFull());
		
		board.set(0, 1, 1);
		board.set(0, 1, 1);
		
		assertEquals(0, board.get(1, 1));
		
		board.set(4, 0, 0);
		
		assertEquals(4, board.get(0, 0));
		
		for (int i = 0; i < 10; i++) {
			assertArrayEquals(new int[] {1, 1}, board.getEmptyIndex());
		}
	}
	
	@Test
	void testMove() {
		for (int i = 0; i < 4; i++) {
			board.set(i + 1, 3, i);
			board.moveTile(3, i, i, i);
			
			if (i < 3)
				assertEquals(0, board.get(3, i));
			assertEquals(i + 1, board.get(i, i));
		}
		
		
	}
}