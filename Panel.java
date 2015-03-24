import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import javax.swing.JPanel;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseListener;
import java.awt.Point;
import javax.swing.Timer;

public class Panel extends JPanel implements MouseListener, ActionListener, MouseMotionListener, KeyListener
{
	private Drawable currentDrawable;
	private Level currentLevel;
	private InputInfo inputInfo;
	private int levelNumber;

	public Panel(Drawable currentDrawable)
	{
		setFocusable(true); /* needed to detect key pressed */
		this.currentDrawable = currentDrawable;
		currentLevel = LevelDefinitions.nextLevel();
		Timer timer = new Timer(15, this);
		timer.start();
		addMouseListener(this);
		addMouseMotionListener(this);
		addKeyListener(this);
		levelNumber = 0;
		inputInfo = new InputInfo(-1, -1, false, '\0');
	}

	public Drawable getCurrentDrawable()
	{
		return currentDrawable;
	}

	public void setCurrentDrawable(Drawable drawable)
	{
		currentDrawable = drawable;
	}

	@Override
	public void paint(Graphics g)
	{
		super.paint(g);
		Graphics2D g2d = (Graphics2D) g;
		currentDrawable.draw(g2d, null, inputInfo, null);
		if (currentDrawable instanceof Level) {
			g2d.setFont(new Font("TimesRoman", Font.PLAIN, 36));
			g2d.setColor(Color.BLACK);
			g2d.drawString("Level: " + levelNumber, 650, 450);
		}
		inputInfo.setClicked(false); /* Keep position data but unset click flag */
	}

	@Override
	public void actionPerformed(ActionEvent e)
	{
		if (currentDrawable instanceof SplashScreen) {
			if (currentDrawable.finishedAnimation())
				currentDrawable = currentLevel;
		} else if (currentDrawable instanceof Level) {
			if (currentDrawable.finishedAnimation()) {
				currentDrawable = currentLevel = LevelDefinitions.nextLevel();
				if (currentLevel == null) { /* Finshed the game. */
					currentDrawable = new EndScreen();
				} else
					levelNumber++;
			}
		}
		repaint();
	}

	public void keyPressed(KeyEvent e) {}
	public void keyTyped(KeyEvent e) {}

	@Override
	public void keyReleased(KeyEvent e) {
		inputInfo.setKey(e.getKeyChar());
		System.out.println("Released key " + e.getKeyChar());
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		inputInfo.setX(e.getX());
		inputInfo.setY(e.getY());
		inputInfo.setClicked(false);
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		inputInfo.setX(e.getX());
		inputInfo.setY(e.getY());
		inputInfo.setClicked(true);
	}

	public void mouseDragged(MouseEvent e){}
	public void mouseEntered(MouseEvent e){}
	public void mouseExited(MouseEvent e){}
	public void mousePressed(MouseEvent e){}
	public void mouseReleased(MouseEvent e){}
}

