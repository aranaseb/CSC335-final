import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.LayoutManager;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class Window2048 extends JFrame {
	static int WIDTH = 1200;
	static int HEIGHT = 800;
	static Dimension windowSize = new Dimension(WIDTH, HEIGHT);
	
	static Color MENU_RED = new Color(0xf94449);
	static Color TREE_GREEN = new Color(0x1A5319);

	Image img;
	Graphics2D g2d;
	BoardGUI board = new BoardGUI();

	public Window2048() {
		setTitle("2048");
		setSize(windowSize);
		setResizable(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLocationRelativeTo(null);
		
		Container pane = getContentPane();
		pane.setBackground(TREE_GREEN);
		pane.setLayout(null);

		JPanel menu = new RoundedPanel(30, MENU_RED);
		int menuWidth = 600;
		int menuHeight = 300;
		menu.setBounds(WIDTH / 2 - menuWidth / 2, HEIGHT / 2 - menuHeight / 2, menuWidth, menuHeight);
		menu.setOpaque(false);

		JLabel menuHeader = new JLabel("Menu");
		menu.add(menuHeader, BorderLayout.PAGE_START);
		
		pane.add(menu);
	}

	public static void main(String[] args) {
		Window2048 window = new Window2048();
		window.addKeyListener(new KeyListener() {

			@Override
			public void keyTyped(KeyEvent e) {

			}

			@Override
			public void keyPressed(KeyEvent e) {
			}

			@Override
			public void keyReleased(KeyEvent e) {
			}

		});
		
		window.setVisible(true);
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
	}
	
	 class RoundedPanel extends JPanel
	    {
	        private Color backgroundColor;
	        private int cornerRadius = 15;

	        public RoundedPanel(int radius, Color bgColor) {
	            super();
	            cornerRadius = radius;
	            backgroundColor = bgColor;
	        }

	        @Override
	        protected void paintComponent(Graphics g) {
	            super.paintComponent(g);
	            Dimension arcs = new Dimension(cornerRadius, cornerRadius);
	            int width = getWidth();
	            int height = getHeight();
	            Graphics2D graphics = (Graphics2D) g;
	            graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

	            if (backgroundColor != null) {
	                graphics.setColor(backgroundColor);
	            } else {
	                graphics.setColor(getBackground());
	            }
	            graphics.fillRoundRect(0, 0, width-1, height-1, arcs.width, arcs.height); //paint background
	            graphics.setColor(getForeground());
	            graphics.drawRoundRect(0, 0, width-1, height-1, arcs.width, arcs.height); //paint border
	        }
	    }
}
