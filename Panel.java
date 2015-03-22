import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import javax.swing.JPanel;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseListener;
import java.awt.Point;
import javax.swing.Timer;

public class Panel extends JPanel implements MouseListener, ActionListener, MouseMotionListener
{
	private Drawable currentDrawable;
	private Level currentLevel;
	private MouseInfo mouseInfo;
	private int levelNumber;

	public Panel(Drawable currentDrawable)
	{
		this.currentDrawable = currentDrawable;
		currentLevel = LevelDefinitions.nextLevel();
		Timer timer = new Timer(20, this);
		timer.start();
		addMouseListener(this);
		addMouseMotionListener(this);
		levelNumber = 0;
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
		currentDrawable.draw(g2d, null, mouseInfo, null);
		if (currentDrawable instanceof Level) {
			g2d.setFont(new Font("TimesRoman", Font.PLAIN, 36));
			g2d.drawString("Level: " + levelNumber, 650, 450);
		}
		if (mouseInfo != null)
			mouseInfo.setClicked(false); /* Keep position data but unset click flag */
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
				levelNumber++;
			}
		}
		repaint();
	}

	@Override
	public void mouseMoved(MouseEvent e)
	{
		mouseInfo = new MouseInfo(new Point(e.getX(), e.getY()), false);
	}

	@Override
	public void mouseClicked(MouseEvent e)
	{
		mouseInfo = new MouseInfo(new Point(e.getX(), e.getY()), true);
	}

	public void mouseDragged(MouseEvent e){}
	public void mouseEntered(MouseEvent e){}
	public void mouseExited(MouseEvent e){}
	public void mousePressed(MouseEvent e){}
	public void mouseReleased(MouseEvent e){}
}

