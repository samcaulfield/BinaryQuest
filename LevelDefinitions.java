import java.awt.Image;
import java.awt.Point;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

public class LevelDefinitions
{
	private static int level = 0;

	public static Level nextLevel()
	{
		Level ret = null;

		ImageSet standardImages = null;
		try {
			standardImages = new ImageSet("./background.png", "./toolbar.png",
				"./border.png", "./simulate.png", "./and.png", "./or.png", "./not.png");
		} catch (IOException e) {
			e.printStackTrace();
		}

		switch (level) {
		case 0:
			Signal a = new Signal(SignalType.Input, new SignalLevel[] {
				SignalLevel.Off, SignalLevel.On
			}, "A", new Point(75, 250), 25, null);

			Wire w0 = new Wire(SignalLevel.Undefined, new Point[] {
				new Point(100, 250), new Point(350, 250)
			}, new Evaluable[] {
				a
			});

			Slot slot = new Slot(100, new Point(400, 250), null, new Evaluable[] {
				w0
			});

			Wire w1 = new Wire(SignalLevel.Undefined, new Point[] {
				new Point(450, 250), new Point(700, 250)
			}, new Evaluable[] {
				slot
			});

			Signal b = new Signal(SignalType.Output, new SignalLevel[] {
				SignalLevel.On, SignalLevel.Off
			} , "B", new Point(725, 250), 25, new Evaluable[] {
				w1
			});

			ret = new Level(standardImages, new Wire[] {
				w0, w1
			}, new Slot[] {
				slot
			}, new Signal[] {
				a, b
			}, 1, 1, 1, 1);
			break;
		case 1:
			Signal signals[] = new Signal[] {
				new Signal(SignalType.Input, new SignalLevel[] {
					SignalLevel.Off, SignalLevel.Off, SignalLevel.Off, SignalLevel.Off,
					SignalLevel.On, SignalLevel.On, SignalLevel.On, SignalLevel.On
				}, "A", new Point(75, 120), 25, null),
			
				new Signal(SignalType.Input, new SignalLevel[] {
					SignalLevel.Off, SignalLevel.Off, SignalLevel.On, SignalLevel.On,
					SignalLevel.Off, SignalLevel.Off, SignalLevel.On, SignalLevel.On
				}, "B", new Point(75, 180), 25, null),
		
				new Signal(SignalType.Input, new SignalLevel[] {
					SignalLevel.Off, SignalLevel.On, SignalLevel.Off, SignalLevel.On,
					SignalLevel.Off, SignalLevel.On, SignalLevel.Off, SignalLevel.On
				}, "C", new Point(75, 350), 25, null),
				
				new Signal(SignalType.Output, new SignalLevel[] {
					SignalLevel.On, SignalLevel.Off, SignalLevel.On, SignalLevel.Off,
					SignalLevel.On, SignalLevel.Off, SignalLevel.On, SignalLevel.On
				}, "D", new Point(725, 250), 25, null) /* this null to be set later */
			};

			Wire wire0 = new Wire(SignalLevel.Undefined, new Point[] {
				new Point(100, 120), new Point(250, 120)
			}, new Evaluable[] {
				signals[0]
			});

			Wire wire1 = new Wire(SignalLevel.Undefined, new Point[] {
				new Point(100, 180), new Point(250, 180)
			}, new Evaluable[] {
				signals[1]
			});

			Wire wire2 = new Wire(SignalLevel.Undefined, new Point[] {
				new Point(100, 350), new Point(250, 350)
			}, new Evaluable[] {
				signals[2]
			});

			Slot slot0 = new Slot(100, new Point(300, 150), null, new Evaluable[] {
				wire0, wire1
			});

			Slot slot1 = new Slot(100, new Point(300, 350), null, new Evaluable[] {
				wire2
			});

			Wire wire3 = new Wire(SignalLevel.Undefined, new Point[] {
				new Point(350, 150), new Point(425, 150), new Point(425, 220), new Point(500, 220)
			}, new Evaluable[] {
				slot0
			});

			Wire wire4 = new Wire(SignalLevel.Undefined, new Point[] {
				new Point(350, 350), new Point(425, 350), new Point(425, 280), new Point(500, 280)
			}, new Evaluable[] {
				slot1
			});

			Slot slot2 = new Slot(100, new Point(550, 250), null, new Evaluable[] {
				wire3, wire4
			});

			Wire wire5 = new Wire(SignalLevel.Undefined, new Point[] {
				new Point(600, 250), new Point(700, 250)
			}, new Evaluable[] {
				slot2
			});

			signals[3].setInputs(new Evaluable[] {
				wire5
			});

			ret = new Level(standardImages, new Wire[] {
				wire0, wire1, wire2, wire3, wire4, wire5
			}, new Slot[] {
				slot0, slot1, slot2
			}, signals, 1, 1, 1, 1);
			break;
		}

		level++;
		return ret;
	}
}

