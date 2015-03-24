import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;

public class Wire implements Evaluable, Drawable {
	private SignalLevel signalLevel;
	private Evaluable inputs[];
	private Point wayPoints[];

	public Wire(SignalLevel signalLevel, Point wayPoints[], Evaluable inputs[]) {
		this.signalLevel = signalLevel;
		this.wayPoints = wayPoints;
		this.inputs = inputs;
	}

	@Override
	public void draw(Graphics2D g, LevelData levelData, InputInfo inputInfo, LogicGate ignore) {
		switch (levelData.getLevelMode()) {
		case Construction:
			g.setColor(Color.YELLOW);
			break;
		case Simulation:
			signalLevel = evaluate(levelData.getLevelIndex());
			switch (signalLevel) {
			case On:
				g.setColor(Color.GREEN);
				break;
			case Off:
				g.setColor(Color.RED);
				break;
			case Undefined:
				g.setColor(Color.YELLOW);
				break;
			}
		}

		for (int i = 0; i + 1 < wayPoints.length; i++)
			g.drawLine(wayPoints[i].x, wayPoints[i].y, wayPoints[i + 1].x,
				wayPoints[i + 1].y);
	}

	@Override
	public boolean finishedAnimation()
	{
		return true;
	}

	@Override
	public SignalLevel evaluate(int levelIndex)
	{
		signalLevel = SignalLevel.Undefined;
		for (Evaluable input : inputs) {
			SignalLevel inputSignal = input.evaluate(levelIndex);
			switch (inputSignal) {
			case On:
				signalLevel = SignalLevel.On;
				break;
			case Off:
				if (signalLevel == SignalLevel.Undefined)
					signalLevel = SignalLevel.Off;
				break;
			}
		}
		return signalLevel;
	}

	public void setInputs(Evaluable inputs[]) {
		this.inputs = inputs;
	}
}

