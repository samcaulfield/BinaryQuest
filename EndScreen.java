import java.awt.Graphics2D;

public class EndScreen implements Drawable {

	public EndScreen() {
	}

	@Override
	public void draw(Renderer renderer, LevelData ignore0, InputInfo inputInfo, LogicGate ignore1) {
		renderer.drawString("You win!", 400, 300);
	}

	@Override
	public boolean finishedAnimation() {
		return true;
	}
}

