import java.awt.Graphics2D;

public interface Drawable
{
	public void draw(Graphics2D g, LevelData levelData, MouseInfo mouseInfo, LogicGate carrying);

	public boolean finishedAnimation();
}

