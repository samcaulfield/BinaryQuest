public class InputInfo {
	private int x, y;
	private boolean clicked;
	private char key;

	public InputInfo(int x, int y, boolean clicked, char key) {
		this.x = x;
		this.y = y;
		this.clicked = clicked;
		this.key = key;
	}

	public boolean isClicked() {
		return clicked;
	}

	public void setClicked(boolean clicked) {
		this.clicked = clicked;
	}

	public char getKey() {
		return key;
	}

	public void setKey(char key) {
		this.key = key;
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	public void setX(int x) {
		this.x = x;
	}

	public void setY(int y) {
		this.y = y;
	}
}

