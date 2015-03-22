import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;

public class AndGate extends LogicGate {

	public AndGate(Image image, Point position, Evaluable inputs[]) {
		super(image, position, inputs);
		minInputs = 2;
		maxInputs = Integer.MAX_VALUE;
	}

	@Override
	public void draw(Graphics2D g, LevelData levelData, MouseInfo mouseInfo, LogicGate ignore) {
		g.drawImage(image, position.x - 39, position.y - 39, null);
/*		g.setColor(Color.BLACK);
		g.drawLine(position.x - 39, position.y - 39, position.x - 39, position.y + 39);
		g.drawLine(position.x - 39, position.y - 39, position.x, position.y - 39);
		g.drawLine(position.x - 39, position.y + 39, position.x, position.y + 39);
		g.drawArc(position.x - 39, position.y - 39, 79, 78, 90, -180);
*/
	}

	@Override
	public SignalLevel evaluate(int arg) {
		return SignalLevel.Undefined;
	}

	@Override
	public boolean finishedAnimation() {
		return true;
	}
}

