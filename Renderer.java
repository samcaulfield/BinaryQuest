import java.awt.Color;
import java.awt.Font;
import java.awt.Image;

public interface Renderer {

	public void drawCircle(double x, double y, double radius);

	public void drawImage(Image image, int x, int y);

	public void drawLine(double x0, double y0, double x1, double y1);

	public void drawRect(double x, double y, double width, double height);

	public void drawString(String string, int x, int y);

	public void setColor(Color color);

	public void setFont(Font font);
}

