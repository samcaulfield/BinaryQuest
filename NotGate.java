import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;

public class NotGate extends LogicGate {

	public NotGate(Image image, Point position, Evaluable inputs[]) {
		super(image, position, inputs);
		minInputs = 1;
		maxInputs = 1;
	}

	@Override
	public void draw(Graphics2D g, LevelData levelData, MouseInfo mouseInfo, LogicGate ignore) {
		g.drawImage(image, position.x - 39, position.y - 39, null);
	}

	@Override
	public SignalLevel evaluate(int arg) {
		SignalLevel signalLevel = inputs[0].evaluate(arg);
		if (signalLevel == SignalLevel.On)
			return SignalLevel.Off;
		else if (signalLevel == SignalLevel.Off)
			return SignalLevel.On;
		else
			return SignalLevel.Undefined;
	}

	@Override
	public boolean finishedAnimation() {
		return true;
	}
}

