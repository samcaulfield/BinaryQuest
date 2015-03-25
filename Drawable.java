import java.awt.Graphics2D;

public interface Drawable
{
	public void draw(Renderer renderer, LevelData levelData, InputInfo inputInfo, LogicGate carrying);

	public boolean finishedAnimation();
}

