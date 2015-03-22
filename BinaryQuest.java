import java.awt.Dimension;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.Timer;

public class BinaryQuest
{
	public static void main(String args[])
	{
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				final int windowWidth = 800, windowHeight = 600;
				JFrame frame = new JFrame("Binary Quest");
				Panel panel = new Panel(new SplashScreen(windowWidth, windowHeight));
				panel.setPreferredSize(new Dimension(windowWidth, windowHeight));
				frame.add(panel);
				frame.pack();
				frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				frame.setVisible(true);
			}
		});
	}
}

