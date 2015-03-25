import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;

public class SwingRenderer implements Renderer {
	private Graphics2D g;

	public void setGraphics2D(Graphics2D g) {
		this.g = g;
	}

	@Override
	public void drawCircle(double x, double y, double radius) {
		g.draw(new Ellipse2D.Double(x, y, 2 * radius, 2 * radius));
	}

	@Override
	public void drawImage(Image image, int x, int y) {
		g.drawImage(image, x, y, null);
	}

	@Override
	public void drawLine(double x0, double y0, double x1, double y1) {
		g.draw(new Line2D.Double(x0, y0, x1, y1));
	}

	@Override
	public void drawRect(double x, double y, double width, double height) {
		g.draw(new Rectangle2D.Double(x, y, width, height));
	}

	@Override
	public void drawString(String string, int x, int y) {
		g.drawString(string, x, y);
	}

	@Override
	public void setColor(Color color) {
		g.setColor(color);
	}

	@Override
	public void setFont(Font font) {
		g.setFont(font);
	}
}

