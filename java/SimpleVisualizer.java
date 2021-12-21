import java.awt.Color;
import java.awt.Container;
import java.awt.Graphics2D;
import java.awt.KeyEventDispatcher;
import java.awt.KeyboardFocusManager;
import java.awt.RenderingHints;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.WindowConstants;

public class SimpleVisualizer extends JFrame {

	private static final long serialVersionUID = 1L;
	final JLabel jLabel = new JLabel();

	int posX, posY;

	void setGraphics(Graphics2D graphics, int width, int height) {
		graphics.setPaint(Color.WHITE);
		graphics.fillRect(0, 0, width, height);

		graphics.setPaint(Color.GRAY);
		graphics.fillRect(posX, posY, width / 2, height / 2);
	}

	int oldWidth = -1, oldHeight = -1;

	void update() {
		Container pane = getContentPane();
		int width = pane.getWidth();
		int height = pane.getHeight();

		BufferedImage canvas = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		Graphics2D graphics = canvas.createGraphics();
		graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

		setGraphics(graphics, width, height);
		jLabel.setIcon(new ImageIcon(canvas));
		repaint();

	}

	boolean dispatchKeyPressed(KeyEvent e) {
		if (e.getID() != KeyEvent.KEY_PRESSED) {
			return false;
		}

		switch (e.getKeyCode()) {
		case KeyEvent.VK_LEFT: // LEFT
			posX -= 1;
			return true;
		case KeyEvent.VK_UP: // UP
			posY -= 1;
			return true;
		case KeyEvent.VK_RIGHT: // RIGHT
			posX += 1;
			return true;
		case KeyEvent.VK_DOWN: // DOWN
			posY += 1;
			return true;
		default:
			return false;
		}
	}

	public SimpleVisualizer() {
		add(jLabel);
		Container pane = getContentPane();
		KeyboardFocusManager manager = KeyboardFocusManager.getCurrentKeyboardFocusManager();
		manager.addKeyEventDispatcher(new KeyEventDispatcher() {

			@Override
			public boolean dispatchKeyEvent(KeyEvent e) {
				boolean pressed = dispatchKeyPressed(e);
				if (pressed) {
					update();
				}
				return pressed;
			}
		});

		addComponentListener(new ComponentAdapter() {
			@Override
			public void componentResized(ComponentEvent e) {
				int width = pane.getWidth();
				int height = pane.getHeight();

				if ((width == oldWidth && height == oldHeight) || width <= 10 || height <= 10) {
					return;
				}
				oldWidth = width;
				oldHeight = height;

				update();
			}
		});
	}

	public static void main(String[] args) {
		SimpleVisualizer frame = new SimpleVisualizer();
		frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		frame.setVisible(true);
		frame.setBounds(0, 0, 320, 240);

	}
}
