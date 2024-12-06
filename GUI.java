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

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.SwingConstants;

/*
 * This class contains the main method which runs the
 * menu GUI to open a game or see scores.
 * 
 * @author Julia Ryan and Sebastian Arana
 */
public class GUI {
	private static Controller theController;

	private static JFrame window = new JFrame("2048 Menu");
	private static final int WINDOW_SIZE = 900;

	private static final int SPACING = 40;
	private static JPanel menu = new JPanel(new GridLayout(15, 30));

	private static JPanel gameOverView = new JPanel(new GridLayout(11, 1));
	private static JLabel[] leaderboardSlots = new JLabel[10];
	private static Leaderboard initialLeaderboard = new Leaderboard("leaderboard.txt");

	private static JPanel leaderboardView = new JPanel(new GridLayout(11, 1));
	private static JLabel winOrLoss;
	private static JLabel scoreLabel;

	private static final int BOARD_SIZE = WINDOW_SIZE - SPACING * 2;

	private static Color MENU_RED = new Color(0xf54248);

	private static JPanel boardView = new JPanel();

	/*
	 * sets up main window and all components. Ends by running the menu to start and
	 * makes the window visible.
	 */
	public static void main(String[] args) {
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		window.setSize(new Dimension(WINDOW_SIZE, WINDOW_SIZE));
		window.setLocationRelativeTo(null);
		window.setLayout(new BorderLayout(80, 0));

		JLabel header = new JLabel("2048", SwingConstants.CENTER);
		header.setPreferredSize(new Dimension(WINDOW_SIZE, SPACING * 2));
		header.setFont(new Font("Courier", Font.BOLD, 30));
		header.setOpaque(true);
		header.setBackground(MENU_RED);
		header.setForeground(Color.WHITE);
		window.add(header, BorderLayout.PAGE_START);

		winOrLoss = new JLabel();
		scoreLabel = new JLabel();
		initializeGameOverView();
		initializeLeaderboard();
		initializeMenu();

		runMenu();

		window.setVisible(true);
	}

	/*
	 * @pre menu is initialized.
	 * 
	 * @post Menu panel is populated with its features
	 */
	private static void initializeMenu() {
		JButton gameButton = new JButton("New Game");
		JButton leaderboardButton = new JButton("Leaderboard");

		gameButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// System.out.println("switch to game...");
				clear();
				int size = requestSize();

				theController = new Controller(new Board(size));

				// STARTS A GAME WINDOW
				Window2048 newGame = new Window2048(theController);
				newGame.run();
				window.setVisible(false);
			}
		});

		leaderboardButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// System.out.println("switch to leaderboard...");
				clear();
				drawLeaderboard();
				window.revalidate();
				window.repaint();
			}
		});

		menu.add(gameButton);
		menu.add(leaderboardButton);
	}

	/*
	 * @pre menu is initialized,
	 * 
	 * @post Menu is set as the content of the window.
	 */
	private static void runMenu() {
		window.add(menu, BorderLayout.CENTER);
		window.revalidate();
		window.repaint();
	}

	/*
	 * @pre winOrLoss, scoreLabel, and gameOverView are initialized.
	 * 
	 * @post GameOverView panel is populated with its labels and button.
	 */
	private static void initializeGameOverView() {

		gameOverView.add(winOrLoss);
		gameOverView.add(scoreLabel);

		JButton homeButton = new JButton("Close");
		homeButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// System.out.println("switch back to selection menu (after game completed)");
				clear();
				runMenu();
			}
		});
		gameOverView.add(homeButton);
	}

	/*
	 * @pre winOrLoss, scoreLabel, and gameOverView are initialized.
	 * 
	 * @post GameOverView panel has its components written according to a win or
	 * loss, then it is added to the window.
	 */
	public static void drawGameOverView(GameStatus result, int score) {
		if (result == GameStatus.LOSS) {
			winOrLoss.setText("You have lost the game. Better luck next time!");
			scoreLabel.setText("Your score was " + score);
		} else if (result == GameStatus.WIN) {
			winOrLoss.setText("You won!!! :D");
			scoreLabel.setText("Your score was " + score);
		}
		clear();
		window.add(gameOverView);
		window.revalidate();
		window.repaint();
	}

	/*
	 * @returns grid size int picked by popu window.
	 */
	public static int requestSize() {
		JSlider sizePicker = new JSlider(4, 10);
		sizePicker.setPaintTicks(true);
		sizePicker.setMajorTickSpacing(1);
		sizePicker.setPaintLabels(true);
		sizePicker.setValue(4);

		JPanel dialog = new JPanel();
		JLabel sizeLabel = new JLabel("Pick a Grid Size:");
		dialog.add(sizeLabel);
		dialog.add(sizePicker);
		JOptionPane.showMessageDialog(null, dialog, "Grid Size", JOptionPane.INFORMATION_MESSAGE);
		return sizePicker.getValue();
	}

	/*
	 * @pre leaderboardSlots and leaderboardView are not null
	 * 
	 * @post leaderboard panel is populated with its components.
	 */
	private static void initializeLeaderboard() {

		for (int i = 0; i < 10; i++) {
			JLabel jl = new JLabel("0", SwingConstants.CENTER);
			leaderboardView.add(jl);
			leaderboardSlots[i] = jl;
		}

		JButton backButton = new JButton("Back");
		backButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// System.out.println("switch back to selection menu");
				clear();
				runMenu();
			}
		});
		leaderboardView.add(backButton);
	}

	/*
	 * @pre boardView is not null.
	 * 
	 * @post leaderboard JPanel is set as the window content.
	 */
	private static void drawLeaderboard() {
		boardView.setSize(new Dimension(BOARD_SIZE, BOARD_SIZE));
		boardView.setBackground(Color.WHITE);
		updateLeaderboard();
		window.add(leaderboardView);
	}

	/*
	 * @pre window is not null.
	 * 
	 * @post sets this GUI menu to visible
	 */
	public static void show() {
		window.setVisible(true);
	}

	/*
	 * @pre leaderboard JPanel and leaderboardSlots[] are not null.
	 * 
	 * @post leaderboard is set to the controller's new data
	 */
	private static void updateLeaderboard() {
		List<Integer> lb;
		if (theController == null) {
			lb = initialLeaderboard.getScores();
		} else {
			lb = theController.getLeaderboard();
		}

		for (int i = 0; i < lb.size(); i++) {
			if (i >= 10) {
				break;
			}
			JLabel thisJL = leaderboardSlots[i];
			thisJL.setText(Integer.toString(lb.get(i)));
		}
		window.add(leaderboardView);
	}

	/*
	 * @pre window is not null
	 * 
	 * @post all components are removed except the header.
	 */
	private static void clear() {
		window.getContentPane().removeAll();
		JLabel header = new JLabel("2048", SwingConstants.CENTER);
		header.setPreferredSize(new Dimension(WINDOW_SIZE, SPACING));
		header.setFont(new Font("Courier", Font.BOLD, 30));
		header.setOpaque(true);
		header.setBackground(Color.LIGHT_GRAY);
		header.setForeground(Color.BLACK);
		window.add(header, BorderLayout.PAGE_START);
	}
}