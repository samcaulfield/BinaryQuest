import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;

public class OrGate extends LogicGate {

	public OrGate(Image image, Point position, Evaluable inputs[]) {
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
		SignalLevel signalLevel = SignalLevel.Undefined;
		for (Evaluable input : inputs) {
			SignalLevel inputSignal = input.evaluate(arg);
			if (inputSignal == SignalLevel.On)
				return SignalLevel.On;
			else if (inputSignal == SignalLevel.Off)
				signalLevel = SignalLevel.Off;
		}
		return signalLevel;
	}

	@Override
	public boolean finishedAnimation() {
		return true;
	}
}

