import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.RenderingHints;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.naming.ldap.Control;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.SwingConstants;

public class GUI {
	private static Board2048 theBoard;
	private static Controller2048 theController;

	private static JFrame window = new JFrame("2048");
	private static final int WINDOW_SIZE = 980;

	private static final int SPACING = 40;
	private static JPanel menu = new JPanel(new GridLayout(11,1));  // maybe change; I just didn't mind how GridLayout (11,1) looks with this
	private static JPanel gameOverView = new JPanel(new GridLayout(11,1));  // 10 score + 'back' button
	private static JLabel[] leaderboardSlots = new JLabel[10];

	private static JPanel boardView = new JPanel() {
		@Override
		protected void paintComponent(Graphics g) {
			super.paintComponent(g);
			Dimension arcs = new Dimension(50, 50); // Border corners arcs {width,height}, change this to
													// whatever you want
			int width = getWidth();
			int height = getHeight();
			Graphics2D graphics = (Graphics2D) g;
			graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

			// Draws the rounded panel with borders.
			graphics.setColor(getBackground());
			graphics.fillRoundRect(0, 0, width - 1, height - 1, arcs.width, arcs.height);// paint background
		}
	};

	private static JPanel leaderboardView = new JPanel(new GridLayout(11,1)) {
		@Override
		protected void paintComponent(Graphics g) {
			super.paintComponent(g);
			Dimension arcs = new Dimension(150, 50);
			int width = getWidth();
			int height = getHeight();
			Graphics2D graphics = (Graphics2D) g;
			graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
			graphics.setColor(getBackground());
			graphics.fillRoundRect(0, 0, width - 1, height - 1, arcs.width, arcs.height);// paint background
		}
	};

	private static JLabel[][] labels;
	
	private static final int BOARD_SIZE = WINDOW_SIZE - SPACING*2;

	public static void main(String[] args) {
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		window.setSize(new Dimension(WINDOW_SIZE - 2 * SPACING, WINDOW_SIZE));
		window.setLocationRelativeTo(null);
		window.getContentPane().setBackground(Color.LIGHT_GRAY);
		window.setLayout(new BorderLayout(SPACING, SPACING));

		JLabel header = new JLabel("2048", SwingConstants.CENTER);
		header.setPreferredSize(new Dimension(WINDOW_SIZE, SPACING));
		header.setFont(new Font("Courier", Font.BOLD, 30));
		header.setOpaque(true);
		header.setBackground(Color.LIGHT_GRAY);
		header.setForeground(Color.BLACK);
		window.add(header, BorderLayout.PAGE_START);

		theController = new Controller2048(new Board2048(4));  // to access the leaderboard in initializeLeaderboard()
															   // before a game has started
		initializeLeaderboard();
		initializeMenu();
		runMenu();
		window.setVisible(true);

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

				System.out.println(e.getKeyCode());

				updateView();
				if (gameOver() && initialStatus==GameStatus.IN_PROGRESS){  // prevents this from executing multiple times
					theController.saveScore(theController.getScore());
					drawGameOverView(winOrLoss, scoreLabel);
				}
			}
		});
		window.revalidate();
	}

	private static void initializeMenu(){
		JButton leaderboardButton = new JButton("View my stats");
		JButton gameButton = new JButton("Play game");

		leaderboardButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				System.out.println("switch to leaderboard...");
				clear();
				drawLeaderboard();
				window.revalidate();
				window.repaint();
			}
		});
		gameButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				System.out.println("switch to game...");
				clear();
				int size = requestSize();
				drawBoard(size);
				window.revalidate();
				window.repaint();
			}
		});

		menu.add(leaderboardButton);
		menu.add(gameButton);
	}

	private static void runMenu(){
		window.add(menu);
		window.revalidate();
		window.repaint();
	}

	private static void drawBoard(int size){

		boardView.removeAll();
		window.revalidate();
		window.repaint();

		boardView.setSize(new Dimension(BOARD_SIZE, BOARD_SIZE));
		boardView.setBackground(Color.WHITE);
		window.add(boardView);

		theController = new Controller2048(new Board2048(size));

		boardView.setLayout(new GridLayout(size, size, SPACING / 2, SPACING / 2));

		labels = new JLabel[size][size]; // Couldn't get components from the boardView, so I added this

		for (int i = 0; i < theController.getSize(); i++) {
			for (int j = 0; j < theController.getSize(); j++) {
				JLabel cell = new JLabel(" ", SwingConstants.CENTER) {
					@Override
					protected void paintComponent(Graphics g) {
						Dimension arcs = new Dimension(50, 50); // Border corners arcs {width,height}, change this to
						// whatever you want
						int width = getWidth();
						int height = getHeight();
						Graphics2D graphics = (Graphics2D) g;
						graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

						// Draws the rounded panel with borders.
						graphics.setColor(getBackground());
						graphics.fillRoundRect(0, 0, width - 1, height - 1, arcs.width, arcs.height);// paint background

						super.paintComponent(g); // Moved this to prevent painting over text
					}
				};

				cell.setBackground(Color.LIGHT_GRAY);
				cell.setOpaque(false);
				cell.setFont(new Font("Courier", Font.BOLD, BOARD_SIZE / size / 4));
				cell.setForeground(Color.BLACK);
				labels[i][j] = cell;
				boardView.add(cell);
			}
		}
		window.revalidate();
		window.repaint();

		updateView();
	}

	private static void initializeGameOverView(JLabel title, JLabel sc){

		gameOverView.add(title);
		gameOverView.add(sc);

		JButton homeButton = new JButton("Close");
		homeButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				System.out.println("switch back to selection menu (after game completed)");
				clear();
				runMenu();
			}
		});
		gameOverView.add(homeButton);
	}


	private static void drawGameOverView(JLabel title, JLabel sc){
		/**
		 * display win/loss message and then let the user either play again or go to the leaderboard
		 */
		if (theController.getStatus()==GameStatus.LOSS){
			title.setText("You have lost the game. Better luck next time!");
			sc.setText("Your score was " + Integer.toString(theController.getScore()));
		}
		else if (theController.getStatus()==GameStatus.WIN){
			title.setText("You won!!! :D");
			sc.setText("Your score was " + Integer.toString(theController.getScore()));
		}
		clear();
		window.add(gameOverView);
		window.revalidate();
		window.repaint();
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

	private static void initializeLeaderboard(){
		/**
		 * I hightly HIGHLY doubt this will update correctly when you finish a game and come back
		 * 
		 * Have an internal int[5] arr for the scores.
		 * 	- when a game ends, refresh the leaderboard list
		 *  - draw the page looping thru
		 */
		
		for (int i=0; i<10; i++){
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
				clear();
				runMenu();
			}
		});
		leaderboardView.add(backButton);
	}

	private static void drawLeaderboard(){
		boardView.setSize(new Dimension(BOARD_SIZE, BOARD_SIZE));
		boardView.setBackground(Color.WHITE);
		updateLeaderboard();
		window.add(leaderboardView);
	}

	private static void updateView() {
		/**
		 * Updates the visible cells to contain the data stored in the model
		 */
		for (int i = 0; i < theController.getSize(); i++) {
			for (int j = 0; j < theController.getSize(); j++) {
				JLabel cell = labels[i][j];
				String newText = Integer.toString(theController.getTileAt(i, j));

				if (newText.equals("0")) {
					cell.setText(" ");
					cell.setBackground(Color.LIGHT_GRAY);
				} else {
					cell.setText(newText);
					cell.setBackground(Color.GRAY);
				}
			}
		}
	}
	
	private static void updateLeaderboard(){
		/**
		 * I hightly HIGHLY doubt this will update correctly when you finish a game and come back
		 * 
		 * Have an internal int[5] arr for the scores.
		 * 	- when a game ends, refresh the leaderboard list
		 *  - draw the page looping thru
		 */
		if (theController == null){  // no games started yet
			return;
		}

		List<Integer> lb = theController.getLeaderboard();

		for (int i=0; i<lb.size(); i++){
			if (i >= 10){  // only print the top 10 scores
				break;
			}
			JLabel thisJL = leaderboardSlots[i];
			thisJL.setText(Integer.toString(lb.get(i)));
		}
		window.add(leaderboardView);
	}

	private static void clear(){
		/**
		 * Does the same thing as window.getContentPane().removeAll() but tries
		 * to maintain the things that should by consistent (like header)
		 */
		window.getContentPane().removeAll();
		JLabel header = new JLabel("2048", SwingConstants.CENTER);
		header.setPreferredSize(new Dimension(WINDOW_SIZE, SPACING));
		header.setFont(new Font("Courier", Font.BOLD, 30));
		header.setOpaque(true);
		header.setBackground(Color.LIGHT_GRAY);
		header.setForeground(Color.BLACK);
		window.add(header, BorderLayout.PAGE_START);
	}

	private static Boolean gameOver(){
		if (theController.getStatus()==GameStatus.IN_PROGRESS){
			return false;
		}
		return true;
	}
}
