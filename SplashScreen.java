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
	public void draw(Renderer renderer, LevelData ignore, InputInfo inputInfo, LogicGate ignoreToo) {
		renderer.drawString("Binary Quest " + animationIndex, 400, 300);
		animationIndex++;
	}

	@Override
	public boolean finishedAnimation()
	{
		return animationIndex >= finishedAnimationIndex;
	}
}

