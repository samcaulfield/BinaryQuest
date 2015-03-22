import java.awt.Point;

public class MouseInfo
{
	private Point position;
	private boolean clicked;

	public MouseInfo(Point position, boolean clicked)
	{
		this.position = position;
		this.clicked = clicked;
	}

	public int getX()
	{
		return position.x;
	}

	public int getY()
	{
		return position.y;
	}

	public Point getPosition()
	{
		return position;
	}

	public void setClicked(boolean clicked)
	{
		this.clicked = clicked;
	}

	public boolean clicked()
	{
		return clicked;
	}
}

