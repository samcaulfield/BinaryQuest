import java.awt.Graphics2D;

public class SplashScreen implements Drawable
{
	private int windowWidth, windowHeight, animationIndex;
	private static final int finishedAnimationIndex = 20;

	public SplashScreen(int windowWidth, int windowHeight)
	{
		this.windowWidth = windowWidth;
		this.windowHeight = windowHeight;
		animationIndex = 0;
	}

	@Override
	public void draw(Graphics2D g, LevelData ignore, MouseInfo mouseInfo, LogicGate ignoreToo)
	{
		g.drawString("Binary Quest " + animationIndex, 400, 300);
		animationIndex++;
	}

	@Override
	public boolean finishedAnimation()
	{
		return animationIndex >= finishedAnimationIndex;
	}
}

