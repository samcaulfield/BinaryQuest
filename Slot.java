import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;

public class Slot implements Drawable, Evaluable, Bounded {
	private Point position;
	private LogicGate gate;
	private Evaluable inputs[];
	private int size;
	private int animationIndex; /* Bounded between 0 and size */

	public Slot(int size, Point position, LogicGate gate, Evaluable inputs[]) {
		this.size = size;
		this.position = position;
		this.gate = gate;
		this.inputs = inputs;
	}

	public boolean filled() {
		return gate != null;
	}

	public LogicGate getGate() {
		return gate;
	}

	public void removeGate() {
		gate = null;
	}

	public boolean matchingGate(LogicGate logicGate) {
		return inputs.length >= logicGate.getMinInputs() && inputs.length <= logicGate.getMaxInputs();
	}

	public boolean canAccept(LogicGate logicGate) {
		if (logicGate == null)
			return false;
		return animationIndex == size && inputs.length >= logicGate.getMinInputs() && inputs.length <= logicGate.getMaxInputs();
	}

	@Override
	public void draw(Graphics2D g, LevelData levelData, MouseInfo mouseInfo, LogicGate logicGate) {
		g.setColor(Color.BLACK);
		g.drawRect(position.x - size / 2, position.y - size / 2, size, size);

		/* Door animation */
		if (mouseInfo != null) {
			if (inBounds(mouseInfo.getX(), mouseInfo.getY()) && mouseInfo.clicked())
				if (animationIndex == size && canAccept(logicGate)) {
					gate = logicGate; /* might need to copy */
					gate.setInputs(inputs);
				}

			if (inBounds(mouseInfo.getX(), mouseInfo.getY()) && logicGate != null && matchingGate(logicGate)) {
				animationIndex++;
				if (animationIndex > size)
					animationIndex = size;
			} else {
				if (gate == null) { /* Don't close if gate in slot */
					animationIndex--;
					if (animationIndex < 0)
						animationIndex = 0;
				}
			}
		}

		g.setColor(Color.RED);
		g.drawLine(position.x - size / 2 + animationIndex, position.y - size / 2, position.x - size / 2 + animationIndex, position.y + size / 2);
		if (gate != null)
			gate.draw(g, levelData, mouseInfo, null);
	}

	@Override
	public boolean inBounds(int x, int y)
	{
		return (x > position.x - size / 2 && x <= position.x + size / 2
			&& y > position.y - size / 2 && y <= position.y + size / 2);
	}

	@Override
	public SignalLevel evaluate(int arg)
	{
		return gate.evaluate(arg);
	}

	@Override
	public boolean finishedAnimation()
	{
		return true;
	}
}

