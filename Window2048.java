
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.SwingConstants;

public class Window2048 extends JFrame {
	static int WIDTH = 1200;
	static int HEIGHT = 800;
	static Dimension windowSize = new Dimension(WIDTH, HEIGHT);

	static Color MENU_RED = new Color(0xf94449);
	static Color TREE_GREEN = new Color(0x1A5319);
	static Color TREE_DARK_GREEN = new Color(0x113610);
	static Color TWO = new Color(0x96d35f);
	static Color FOUR = new Color(0x6ce4cf);
	static Color EIGHT = new Color(0x74a7ff);
	static Color SIXTEEN = new Color(0x295ff4);
	static Color THIRTYTWO = new Color(0xd357fe);
	static Color SIXTYFOUR = new Color(0x782cf6);
	static Color ONEHUNDREDTWENTYEIGHT = new Color(0xee719e);
	static Color TWOHUNDREDFIFTYSIX = new Color(0xff6250);
	static Color FIVEHUNDREDTWELVE = new Color(0xb51a00);
	static Color ONETHOUSANDTWENTYFOUR = new Color(0x999999);
	static Color TWENTYFORTYEIGHT = new Color(0xfecb3e);

	static Controller2048 theController;

	static final int BOARD_SIZE = 500;

	static JLabel[][] labels;

	private static JPanel menu = new JPanel();
	private static JPanel gameOverView = new JPanel(new GridLayout(11, 1)); // 10 score + 'back' button
	private static JLabel[] leaderboardSlots = new JLabel[10];
	private static JPanel boardView = new JPanel();
	private static JPanel leaderboardView = new JPanel();

	public Window2048() {
		setTitle("2048");
		setSize(windowSize);
		setResizable(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLocationRelativeTo(null);
		setLayout(null);

	}

	public static Window2048 window;

	public static void main(String[] args) {
		window = new Window2048();

		int size = 4;

		theController = new Controller2048(new Board2048(size));

		initializeLeaderboard();
		initializeMenu();

		JLabel winOrLoss = new JLabel();
		JLabel scoreLabel = new JLabel();
		initializeGameOverView(winOrLoss, scoreLabel);

		window.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				
				GameStatus initialStatus = theController.getStatus();
				
				if (e.getKeyCode() == 87 || e.getKeyCode() == 38)
					theController.move(Direction.UP);
				else if (e.getKeyCode() == 65 || e.getKeyCode() == 37)
					theController.move(Direction.LEFT);
				else if (e.getKeyCode() == 83 || e.getKeyCode() == 40)
					theController.move(Direction.DOWN);
				else if (e.getKeyCode() == 68 || e.getKeyCode() == 39)
					theController.move(Direction.RIGHT);

				updateView();
				
				if (gameOver() && initialStatus == GameStatus.IN_PROGRESS) { // prevents this from executing multiple
					// times
					theController.saveScore(theController.getScore());
					drawGameOverView(winOrLoss, scoreLabel);
}
			}
		});
		window.setVisible(true);
		runMenu();
	}

	public void paint(Graphics g) {
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
		g2d.fillRoundRect(WIDTH / 2 - BOARD_SIZE / 2, HEIGHT / 2 - BOARD_SIZE / 2 + 20, BOARD_SIZE + 20,
				BOARD_SIZE + 15, 150, 150);
	}

	private static void updateView() {
		/**
		 * Updates the visible cells to contain the data stored in the model
		 */
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

	private static void initializeMenu() {
		menu.setSize(new Dimension(600, 300));
		menu.setLocation(WIDTH / 2 - 600 / 2, HEIGHT / 2 - 300 / 2);
		menu.setBackground(MENU_RED);
		
		JButton gameButton = new JButton("Play game");
		JButton leaderboardButton = new JButton("Leaderboard");
		
		JPanel buttonPanel = new JPanel();
		buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.X_AXIS));
		buttonPanel.setAlignmentX(SwingConstants.CENTER);
		buttonPanel.add(gameButton);
		buttonPanel.add(Box.createHorizontalGlue());
		buttonPanel.add(leaderboardButton);
		buttonPanel.setOpaque(false);
		
		menu.add(buttonPanel);

		gameButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				menu.setVisible(false);
				int size = requestSize();
				drawBoard(size);
			}
		});
		leaderboardButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				menu.setVisible(false);
				drawLeaderboard();
			}
		});
		window.add(menu);
	}

	private static void runMenu() {
		menu.setVisible(true);
		window.revalidate();
	}

	private static void drawBoard(int size) {
		boardView.setSize(new Dimension(BOARD_SIZE, BOARD_SIZE));
		boardView.setLocation(WIDTH / 2 - BOARD_SIZE / 2, HEIGHT / 2 - BOARD_SIZE / 2);
		boardView.setBorder(BorderFactory.createLineBorder(TREE_DARK_GREEN, 15));
		boardView.setBackground(TREE_DARK_GREEN);
		window.add(boardView);
		
		theController = new Controller2048(new Board2048(size));

		boardView.setLayout(new GridLayout(size, size));

		labels = new JLabel[size][size];

		for (int i = 0; i < theController.getSize(); i++) {
			for (int j = 0; j < theController.getSize(); j++) {
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
		window.revalidate();
	}

	private static void initializeGameOverView(JLabel title, JLabel sc) {

		gameOverView.add(title);
		gameOverView.add(sc);

		JButton homeButton = new JButton("Close");
		homeButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				System.out.println("switch back to selection menu (after game completed)");
				window.remove(gameOverView);
				runMenu();
			}
		});
		gameOverView.add(homeButton);
	}

	private static void drawGameOverView(JLabel title, JLabel sc) {
		/**
		 * display win/loss message and then let the user either play again or go to the
		 * leaderboard
		 */
		if (theController.getStatus() == GameStatus.LOSS) {
			title.setText("You have lost the game. Better luck next time!");
			sc.setText("Your score was " + Integer.toString(theController.getScore()));
		} else if (theController.getStatus() == GameStatus.WIN) {
			title.setText("You won!!! :D");
			sc.setText("Your score was " + Integer.toString(theController.getScore()));
		}
		window.add(gameOverView);
		window.revalidate();
	}

	public static int requestSize() {
		JSlider sizePicker = new JSlider(4, 8);
		sizePicker.setPaintTicks(true);
		sizePicker.setMajorTickSpacing(1);
		sizePicker.setPaintLabels(true);
		sizePicker.setValue(4);

		JPanel dialog = new JPanel();
		JLabel sizeLabel = new JLabel("Pick a Size:");
		dialog.add(sizeLabel);
		dialog.add(sizePicker);
		JOptionPane.showMessageDialog(null, dialog, "Pick Size", JOptionPane.INFORMATION_MESSAGE);
		return sizePicker.getValue();
	}

	private static void initializeLeaderboard() {
		/**
		 * I hightly HIGHLY doubt this will update correctly when you finish a game and
		 * come back
		 * 
		 * Have an internal int[5] arr for the scores. - when a game ends, refresh the
		 * leaderboard list - draw the page looping thru
		 */

		for (int i = 0; i < 10; i++) {
			// add it to leaderboardView
			JLabel jl = new JLabel("0", SwingConstants.CENTER);
			leaderboardView.add(jl);
			leaderboardSlots[i] = jl;
		}

		JButton backButton = new JButton("Back");
		backButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				System.out.println("switch back to selection menu");
				window.remove(backButton);
				runMenu();
			}
		});
		leaderboardView.add(backButton);
	}

	private static void drawLeaderboard() {
		updateLeaderboard();
		window.add(leaderboardView);
		window.revalidate();
	}

	private static void updateLeaderboard() {
		/**
		 * I hightly HIGHLY doubt this will update correctly when you finish a game and
		 * come back
		 * 
		 * Have an internal int[5] arr for the scores. - when a game ends, refresh the
		 * leaderboard list - draw the page looping thru
		 */
		if (theController == null) { // no games started yet
			return;
		}

		List<Integer> lb = theController.getLeaderboard();

		for (int i = 0; i < lb.size(); i++) {
			if (i >= 10) { // only print the top 10 scores
				break;
			}
			JLabel thisJL = leaderboardSlots[i];
			thisJL.setText(Integer.toString(lb.get(i)));
		}
		window.add(leaderboardView);
	}

	private static Boolean gameOver() {
		return theController.getStatus() != GameStatus.IN_PROGRESS;
	}
}
