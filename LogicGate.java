import java.awt.Image;
import java.awt.Point;

public abstract class LogicGate implements Drawable, Evaluable {
	protected Point position;
	protected Evaluable inputs[];
	protected Image image;
	protected int minInputs, maxInputs;

	public LogicGate(Image image, Point position, Evaluable inputs[]) {
		this.position = position;
		this.image = image;
		this.inputs = inputs;
	}

	public int getMinInputs() {
		return minInputs;
	}

	public int getMaxInputs() {
		return maxInputs;
	}

	public void setInputs(Evaluable inputs[]) {
		this.inputs = inputs;
	}

	public Point getPosition() {
		return position;
	}

	public void setPosition(int x, int y) {
		this.position = new Point(x, y);
	}
}

