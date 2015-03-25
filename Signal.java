import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;

public class Signal implements Drawable, Evaluable {

	private SignalType signalType;
	private SignalLevel signal[];
	private String name;
	private Point position;
	private int radius;
	private Evaluable inputs[];

	public Signal(SignalType signalType, SignalLevel signal[], String name, Point position, int radius, Evaluable inputs[]) {
		this.signalType = signalType;
		this.signal = signal;
		this.name = name;
		this.position = position;
		this.radius = radius;
		this.inputs = inputs;
	}

	public void setInputs(Evaluable inputs[]) {
		this.inputs = inputs;
	}

	public String getName() {
		return name;
	}

	public int getSignalLength() {
		return signal.length;
	}

	public SignalLevel getSignalLevelAt(int time) {
		return signal[time];
	}

	public boolean isOutput() {
		return signalType == SignalType.Output;
	}

	@Override
	public void draw(Renderer renderer, LevelData levelData, InputInfo inputInfo, LogicGate ignore) {
		switch (levelData.getLevelMode()) {
		case Construction:
			renderer.setColor(Color.YELLOW);
			break;
		case Simulation:
			SignalLevel signalLevel = SignalLevel.Undefined;
			if (signalType == SignalType.Input)
				signalLevel = signal[levelData.getLevelIndex()];
			else if (signalType == SignalType.Output)
				signalLevel = evaluate(levelData.getLevelIndex());

			switch (signalLevel) {
				case On:
					renderer.setColor(Color.GREEN);
					break;
				case Off:
					renderer.setColor(Color.RED);
					break;
				case Undefined:
					renderer.setColor(Color.YELLOW);
					break;
			}
			break;
		}

		renderer.drawCircle(position.x - radius, position.y - radius, radius);
		renderer.drawString(name, position.x, position.y);
	}

	@Override
	public SignalLevel evaluate(int arg) {
		switch (signalType) {
		case Input:
			return signal[arg];
		case Output:
			SignalLevel ret = SignalLevel.Undefined;
			for (Evaluable input : inputs) {
				if (input.evaluate(arg) == SignalLevel.On)
					return SignalLevel.On;
				else if (input.evaluate(arg) == SignalLevel.Off)
					ret = SignalLevel.Off;	
			}
			return ret;
		}
		return SignalLevel.Undefined;
	}

	@Override
	public boolean finishedAnimation() {
		System.out.println("Called unimplemented finishedAnimation() of Signal.");
		return true;
	}
}

