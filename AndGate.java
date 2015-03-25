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
	public void draw(Renderer renderer, LevelData levelData, InputInfo inputInfo, LogicGate ignore) {
		renderer.drawImage(image, position.x - 39, position.y - 39);
	}

	@Override
	public SignalLevel evaluate(int arg) {
		SignalLevel signalLevel = SignalLevel.On;
		for (Evaluable input : inputs) {
			if (input.evaluate(arg) == SignalLevel.Off) {
				if (signalLevel == SignalLevel.On)
					signalLevel = signalLevel.Off;
			} else if (input.evaluate(arg) == SignalLevel.Undefined)
				signalLevel = signalLevel.Undefined;
		}
		return signalLevel;
	}

	@Override
	public boolean finishedAnimation() {
		return true;
	}
}

