import java.awt.Graphics2D;

public class EndScreen implements Drawable {

	public EndScreen() {
	}

	@Override
	public void draw(Graphics2D g, LevelData ignore0, MouseInfo mouseInfo, LogicGate ignore1) {
		g.drawString("You win!", 400, 300);
	}

	@Override
	public boolean finishedAnimation() {
		return true;
	}
}

