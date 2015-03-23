import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;

public class Level implements Drawable {
	private Wire wires[];
	private Slot slots[];
	private Signal signals[];
	private int numAndGates, numOrGates, numXorGates, numNotGates;
	private LogicGate carrying;
	private LevelMode levelMode;
	private int signalIndex; /* Index into signal array to display in signal window. */
	private int timeIndex; /* Index into time array of signals in signal window. */

	private final AndGate toolbarAndGate;
	private final NotGate toolbarNotGate;
	private final OrGate toolbarOrGate;

	private int simulationIndex; /* Temporal location in simulation mode. */
	private int tick; /* Frame counter mod 100. */
	private boolean correct, tempCorrect;
	private ImageSet imageSet;

	public Level(ImageSet imageSet, Wire wires[], Slot slots[], Signal signals[], int numAndGates, int numOrGates, int numXorGates, int numNotGates) {
		this.imageSet = imageSet;
		this.wires = wires;
		this.slots = slots;
		this.signals = signals;
		this.numAndGates = numAndGates;
		this.numOrGates = numOrGates;
		this.numXorGates = numXorGates;
		this.numNotGates = numNotGates;

		levelMode = LevelMode.Construction;
		toolbarAndGate = new AndGate(imageSet.getAnd(), new Point(450, 550), null);
		toolbarNotGate = new NotGate(imageSet.getNot(), new Point(750, 550), null);
		toolbarOrGate = new OrGate(imageSet.getOr(), new Point(550, 550), null);
	}

	@Override
	public void draw(Graphics2D g, LevelData ignore0, MouseInfo mouseInfo, LogicGate ignore1) {
		g.drawImage(imageSet.getBackground(), 0, 0, null);
		g.drawImage(imageSet.getToolbar(), 0, 500, null);
		g.drawImage(imageSet.getBorder(), 0, 490, null);

		if (levelMode == LevelMode.Construction) {
			g.drawImage(imageSet.getSimulate(), 300, 500, null);

			LevelData levelData = new LevelData(LevelMode.Construction, 0);

			for (Wire wire : wires)
				wire.draw(g, levelData, mouseInfo, ignore1);
			for (Slot slot : slots)
				slot.draw(g, levelData, mouseInfo, carrying);
			for (Signal signal : signals)
				signal.draw(g, levelData, mouseInfo, ignore1);

			/* Draw toolbar. */
			g.setColor(Color.BLACK);
			g.drawLine(0, 500, 800, 500);
			g.drawLine(300, 500, 300, 600);
			g.drawLine(400, 500, 400, 600);
			g.drawLine(500, 500, 500, 600);
			g.drawLine(600, 500, 600, 600);
			g.drawLine(700, 500, 700, 600);
			g.drawString("Sim", 340, 552);

			toolbarAndGate.draw(g, levelData, mouseInfo, ignore1);
			g.drawString("" + numAndGates, toolbarAndGate.getPosition().x, toolbarAndGate.getPosition().y);

			toolbarNotGate.draw(g, levelData, mouseInfo, ignore1);
			g.drawString("" + numNotGates, toolbarNotGate.getPosition().x, toolbarNotGate.getPosition().y);

			toolbarOrGate.draw(g, levelData, mouseInfo, ignore1);
			g.drawString("" + numOrGates, toolbarOrGate.getPosition().x, toolbarOrGate.getPosition().y);

			/* Logic for current gate being carried. */
			if (mouseInfo != null) {
				if (mouseInfo.clicked()) {
					if (carrying != null) {
						boolean gatePutInSlot = false;
						for (Slot slot : slots)
							if (slot.inBounds(mouseInfo.getX(), mouseInfo.getY()))
								if (slot.canAccept(carrying))
									gatePutInSlot = true;

						if (!gatePutInSlot) {
							if (carrying instanceof AndGate)
								numAndGates++;
							else if (carrying instanceof NotGate)
								numNotGates++;
							else if (carrying instanceof OrGate)
								numOrGates++;
						}

						carrying = null;
					} else {
						/* toolbar clicks */
						if (mouseInfo.getX() > 400 && mouseInfo.getX() < 500
							&& mouseInfo.getY() > 500 && numAndGates > 0) {
							carrying = new AndGate(imageSet.getAnd(), new Point(mouseInfo.getX(), mouseInfo.getY()), null);
							numAndGates--;
						} else if (mouseInfo.getX() > 500 && mouseInfo.getX() < 600 && mouseInfo.getY() > 500
							&& numOrGates > 0) {

							carrying = new OrGate(imageSet.getOr(), new Point(mouseInfo.getX(), mouseInfo.getY()), null);
							numOrGates--;
						} else if (mouseInfo.getX() > 700 && mouseInfo.getY() > 500 && numNotGates > 0) {
							System.out.println("Picked up NotGate");
							carrying = new NotGate(imageSet.getNot(), new Point(mouseInfo.getX(), mouseInfo.getY()), null);
							numNotGates--;
						} else if (mouseInfo.getX() > 300 && mouseInfo.getX() < 400 && mouseInfo.getY() > 500) {
							boolean filled = true;
							for (Slot slot : slots)
								if (!slot.filled())
									filled = false;
							if (filled) {
								levelMode = LevelMode.Simulation;
								tick = simulationIndex = 0;
								correct = false;
								tempCorrect = true;
								System.out.println("Changed to simulation mode.");
							} else
								System.out.println("Could not change to simulation mode.");
						}
						/* clicks on slots - to remove gates perhaps */
						for (Slot slot : slots) {
							if (slot.inBounds(mouseInfo.getX(), mouseInfo.getY())) {
								LogicGate gate = slot.getGate();
								if (gate != null) {
									if (gate instanceof AndGate)
										numAndGates++;
									else if (gate instanceof NotGate)
										numNotGates++;
									else if (gate instanceof OrGate)
										numOrGates++;
									slot.removeGate();
								}
							}
						}
					}
				} else if (carrying != null)
					carrying.setPosition(mouseInfo.getX(), mouseInfo.getY());
			}

			if (carrying != null)
				carrying.draw(g, levelData, mouseInfo, ignore1);
		} else if (levelMode == LevelMode.Simulation) {
			LevelData levelData = new LevelData(LevelMode.Simulation, simulationIndex);
			for (Slot slot : slots)
				slot.draw(g, levelData, mouseInfo, ignore1);
			for (Wire wire : wires)
				wire.draw(g, levelData, mouseInfo, ignore1);
			for (Signal signal : signals)
				signal.draw(g, levelData, mouseInfo, ignore1);

			tick++;
			if (tick >= 100) {
				for (Signal signal : signals) {
					if (signal.isOutput()) {
						if (!(signal.evaluate(simulationIndex) == signal.getSignalLevelAt(simulationIndex)))
							tempCorrect = false;
					}
				}

				tick = 0;
				System.out.println("Simulation Tick.");
				simulationIndex++;
			}
			if (simulationIndex >= signals[0].getSignalLength()) {
				levelMode = LevelMode.Construction;
				System.out.println("Solution is " + tempCorrect);
				correct = tempCorrect;
			}

		}

		drawSignalWindow(g);
	}

	private void drawSignalWindow(Graphics2D g) {
		/* Draw signal name column irrespective of level mode. */
		g.setColor(Color.BLACK);
		g.drawLine(50, 500, 50, 600);

		int windowWidth = 0;
		if (levelMode == LevelMode.Construction)
			windowWidth = 300;
		else if (levelMode == LevelMode.Simulation)
			windowWidth = 800;

		/* Horizontal dividing lines. */
		g.drawLine(0, 533, windowWidth, 533);
		g.drawLine(0, 566, windowWidth, 566);

		if (levelMode == LevelMode.Construction) {
			/* Vertical signal-over-time lines. */
			g.setColor(Color.DARK_GRAY);
			for (int i = 2; i <= 5; i++)
				g.drawLine(50 * i, 500, 50 * i, 600);
			/* Signals. */
			for (int i = 0; i < 3; i++) { /* For each row. */
				if (signalIndex + i < signals.length) {
					g.setColor(Color.RED);
					SignalLevel previous = SignalLevel.Off;
					for (int j = 0; j < 5; j++) { /* For each time slot. */
						if (timeIndex + j < signals[i].getSignalLength()) {
							SignalLevel signalLevel = signals[i].getSignalLevelAt(j);
							if (signalLevel == SignalLevel.On) {
								g.drawLine(50 + j * 50, 505 + i * 33, 100 + j * 50, 505 + i * 33);
							} else if (signalLevel == SignalLevel.Off) {
								g.drawLine(50 + j * 50, 528 + i * 33, 100 + j * 50, 528 + i * 33);
							}
							if (previous != signalLevel)
								g.drawLine(50 + j * 50, 505 + i * 33, 50 + j * 50, 528 + i * 33);
							previous = signalLevel;
						}
					}
				}
			}
		}

		g.setColor(Color.BLACK);
		g.drawString(signals[signalIndex].getName(), 0, 515);
		g.drawString(signals[signalIndex + 1].getName(), 0, 548);
		if (signals.length > 2)
			g.drawString(signals[signalIndex + 2].getName(), 0, 566);
	}

	@Override
	public boolean finishedAnimation() {
		return correct;
	}
}

