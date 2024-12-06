
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.RenderingHints;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

/*
 * GUI class for the actual game. Returns to GUI menu class
 * when finished.
 * 
 * @author Sebastian Arana
 */
public class Window2048 extends JFrame {
	private int width = 1200;
	private int height = 800;
	private final Dimension windowSize = new Dimension(width, height);
	
	private final int BOARD_SIZE = 500;

	private static Color MENU_RED = new Color(0xf54248);
	private static Color TREE_GREEN = new Color(0x1A5319);
	private static Color TREE_DARK_GREEN = new Color(0x113610);
	private static Color TWO = new Color(0x96d35f);
	private static Color FOUR = new Color(0x6ce4cf);
	private static Color EIGHT = new Color(0x74a7ff);
	private static Color SIXTEEN = new Color(0x295ff4);
	private static Color THIRTYTWO = new Color(0xd357fe);
	private static Color SIXTYFOUR = new Color(0x782cf6);
	private static Color ONEHUNDREDTWENTYEIGHT = new Color(0xee719e);
	private static Color TWOHUNDREDFIFTYSIX = new Color(0xff6250);
	private static Color FIVEHUNDREDTWELVE = new Color(0xb51a00);
	private static Color ONETHOUSANDTWENTYFOUR = new Color(0x999999);
	private static Color TWENTYFORTYEIGHT = new Color(0xfecb3e);

	// inner class; paints custom background
	private JLayeredPane background = new JLayeredPane() {
		protected void paintComponent(Graphics g) {
			final int WIDTH = getWidth();
			final int HEIGHT = getHeight();
			Graphics2D g2d = (Graphics2D) g;
			g2d.setColor(Color.BLACK);
			g2d.fillRect(0, 0, WIDTH, HEIGHT);

			g2d.setColor(Color.WHITE);
			for (int i = 0; i < 500; i++) {
				int x = (int) (Math.random() * WIDTH);
				int y = (int) (Math.random() * HEIGHT);
				g2d.fillOval(x, y, 2, 2);

			}

			for (int i = 0; i < 200; i++) {
				int x = (int) (Math.random() * WIDTH);
				int y = (int) (Math.random() * HEIGHT);
				g2d.fillOval(x, y, 2, 3);
			}

			for (int i = 0; i < 100; i++) {
				int x = (int) (Math.random() * WIDTH);
				int y = (int) (Math.random() * HEIGHT);
				g2d.fillOval(x, y, 3, 4);
			}

			// ground
			g2d.setColor(Color.WHITE);
			g2d.fillOval(-500, HEIGHT - 250, WIDTH + 1000, 500);

			g2d.setColor(TREE_GREEN);

			g2d.fillPolygon(new int[] { WIDTH / 12, WIDTH / 2, (WIDTH / 12) * 11 }, new int[] { 800, 100, 800 }, 3);

			g2d.fillPolygon(new int[] { WIDTH / 10, WIDTH / 2, (WIDTH / 10) * 9 }, new int[] { 700, 100, 700 }, 3);

			g2d.fillPolygon(new int[] { WIDTH / 8, WIDTH / 2, (WIDTH / 8) * 7 }, new int[] { 600, 0, 600 }, 3);

			g2d.fillPolygon(new int[] { WIDTH / 7, WIDTH / 2, (WIDTH / 7) * 6 }, new int[] { 500, 0, 500 }, 3);

			g2d.fillPolygon(new int[] { WIDTH / 6, WIDTH / 2, (WIDTH / 6) * 5 }, new int[] { 400, -100, 400 }, 3);

			g2d.fillPolygon(new int[] { WIDTH / 5, WIDTH / 2, (WIDTH / 5) * 4 }, new int[] { 300, -200, 300 }, 3);

			g2d.fillPolygon(new int[] { WIDTH / 21 * 5, WIDTH / 2, (WIDTH / 21) * 16 }, new int[] { 200, -300, 200 }, 3);

			g2d.setColor(Color.WHITE);
			for (int i = 0; i < 100; i++) {
				int x = (int) (Math.random() * WIDTH);
				int y = (int) (Math.random() * HEIGHT);
				g2d.fillOval(x, y, 4, 5);
			}

			for (int i = 0; i < 100; i++) {
				int x = (int) (Math.random() * WIDTH);
				int y = (int) (Math.random() * HEIGHT);
				g2d.fillOval(x, y, 5, 6);
			}

			g2d.setColor(TREE_DARK_GREEN);
			g2d.fillRoundRect(WIDTH / 2 - BOARD_SIZE / 2 - 25, HEIGHT / 2 - BOARD_SIZE / 2 - 25, BOARD_SIZE + 50,
					BOARD_SIZE + 50, 150, 150);
			
			super.paintComponent(g);
		}
	};
	private Controller2048 theController;

	private JLabel[][] labels;

	private JPanel boardView;;
	private JLabel scoreLabel;

	private int size;

	public Window2048(Controller2048 controller) {
		setTitle("2048");
		setSize(windowSize);
		setResizable(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLocationRelativeTo(null);
		
		add(background);

		this.theController = controller;
		this.size = theController.getSize();
	}
	
	/*
	 * @post window is visible with board and listeners
	 */
	public void run() {
		drawBoard();
		setVisible(true);
		
		addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {

				if (e.getKeyCode() == 87 || e.getKeyCode() == 38) {
					theController.move(Direction.UP);
				} else if (e.getKeyCode() == 65 || e.getKeyCode() == 37) {
					theController.move(Direction.LEFT);
				} else if (e.getKeyCode() == 83 || e.getKeyCode() == 40) {
					theController.move(Direction.DOWN);
				} else if (e.getKeyCode() == 68 || e.getKeyCode() == 39) {
					theController.move(Direction.RIGHT);
				}

				updateView();

				if (gameOver()) {
					theController.saveScore(theController.getScore());
					try {
						Thread.sleep(3000);
					} catch (InterruptedException e1) {
						e1.printStackTrace();
					}
					setVisible(false);
					GUI.show();
					GUI.drawGameOverView(theController.getStatus(), theController.getScore());
				}
			}
		});
	}

	/*
	 * @post board is drawn initially with startup tiles
	 */
	private void drawBoard() {
		boardView = new JPanel();
		boardView.setSize(new Dimension(BOARD_SIZE, BOARD_SIZE));
		boardView.setLocation(width / 2 - BOARD_SIZE / 2 - 7, height / 2 - BOARD_SIZE / 2 - 17);
		boardView.setBackground(TREE_DARK_GREEN);
		boardView.setLayout(new GridLayout(size, size));
		background.add(boardView, 1);
		
		scoreLabel = new JLabel("Score: 0", SwingConstants.CENTER);
		scoreLabel.setSize(new Dimension(250, 75));
		scoreLabel.setLocation(0, 0);
		scoreLabel.setBackground(MENU_RED);
		scoreLabel.setForeground(Color.WHITE);
		scoreLabel.setFont(new Font("Courier", Font.BOLD, 30));
		scoreLabel.setOpaque(true);
		background.add(scoreLabel, 1);

		labels = new JLabel[size][size];

		for (int i = 0; i < size; i++) {
			for (int j = 0; j < size; j++) {
				JLabel cell = new JLabel(" ", SwingConstants.CENTER) {
					@Override
					protected void paintComponent(Graphics g) {
						int width = getWidth();
						int height = getHeight();
						Graphics2D graphics = (Graphics2D) g;
						graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

						graphics.setColor(getBackground());
						graphics.fillRoundRect(0, 0, width - 1, height - 1, width, height);

						super.paintComponent(g);
					}
				};

				cell.setOpaque(false);
				cell.setFont(new Font("Courier", Font.BOLD, BOARD_SIZE / size / 4));
				cell.setForeground(Color.WHITE);
				labels[i][j] = cell;
				boardView.add(cell);
			}
		}
		
		updateView();
	}
	
	/*
	 * @post board is redrawn with new colors and numbered tiles.
	 */
	private void updateView() {
		scoreLabel.setText("Score: " + theController.getScore());

		for (int i = 0; i < theController.getSize(); i++) {
			for (int j = 0; j < theController.getSize(); j++) {
				JLabel cell = labels[i][j];
				String newValue = Integer.toString(theController.getTileAt(i, j));

				cell.setText(newValue);
				switch (Integer.parseInt(newValue)) {
				case 2:
					cell.setBackground(TWO);
					break;
				case 4:
					cell.setBackground(FOUR);
					break;
				case 8:
					cell.setBackground(EIGHT);
					break;
				case 16:
					cell.setBackground(SIXTEEN);
					break;
				case 32:
					cell.setBackground(THIRTYTWO);
					break;
				case 64:
					cell.setBackground(SIXTYFOUR);
					break;
				case 128:
					cell.setBackground(ONEHUNDREDTWENTYEIGHT);
					break;
				case 256:
					cell.setBackground(TWOHUNDREDFIFTYSIX);
					break;
				case 512:
					cell.setBackground(FIVEHUNDREDTWELVE);
					break;
				case 1024:
					cell.setBackground(ONETHOUSANDTWENTYFOUR);
					break;
				case 2048:
					cell.setBackground(TWENTYFORTYEIGHT);
					break;
				default:
					cell.setText(" ");
					cell.setBackground(TREE_GREEN);
					break;
				}
			}
		}
	}

	private Boolean gameOver() {
		return theController.getStatus() != GameStatus.IN_PROGRESS;
	}
}
