/* Immutable class holding all the images for a level. */

import java.awt.Image;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

public class ImageSet {
	private final Image background, toolbar, border;

	public ImageSet(String backgroundUrl, String toolbarUrl, String borderUrl) throws IOException {
		background = loadFromUrl(backgroundUrl);
		toolbar = loadFromUrl(toolbarUrl);
		border = loadFromUrl(borderUrl);
	}

	private Image loadFromUrl(String url) throws IOException {
		return ImageIO.read(new File(url));
	}

	public Image getBackground() {
		return background;
	}

	public Image getBorder() {
		return border;
	}

	public Image getToolbar() {
		return toolbar;
	}
}

