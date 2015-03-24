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
	private double tick; /* Frame counter mod 100. */
	private boolean correct, tempCorrect;
	private ImageSet imageSet;

	/* fixed UI locations */
	private final int toolbarUpperY = 500, toolbarLowerY = 600;

	class SavedSignal {
		SignalLevel signal[];
		String name;

		public SavedSignal(SignalLevel signal[], String name) {
			this.signal = signal;
			this.name = name;
		}
	}

	SavedSignal savedOutputSignals[];

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
	public void draw(Graphics2D g, LevelData ignore0, InputInfo inputInfo, LogicGate ignore1) {
		g.drawImage(imageSet.getBackground(), 0, 0, null);
		g.drawImage(imageSet.getToolbar(), 0, 500, null);
		g.drawImage(imageSet.getBorder(), 0, 490, null);

		LevelData levelData = null;

		int signalsInWindow = Math.min(3, signals.length);
		char key = inputInfo.getKey();
		switch(key) {
		case 'w':
			if (signalIndex > 0)
				signalIndex--;
			break;
		case 's':
			if (signalIndex + signalsInWindow < signals.length)
				signalIndex++;
			else
				System.out.println("Couldn't do that");
			break;
		}
		inputInfo.setKey('\0');

		if (levelMode == LevelMode.Construction) {
			levelData = new LevelData(LevelMode.Construction, 0);

			for (Wire wire : wires)
				wire.draw(g, levelData, inputInfo, ignore1);
			for (Slot slot : slots)
				slot.draw(g, levelData, inputInfo, carrying);
			for (Signal signal : signals)
				signal.draw(g, levelData, inputInfo, ignore1);

			g.drawImage(imageSet.getSimulate(), 300, 500, null);

			/* Draw toolbar. */
			g.setColor(Color.BLACK);
			g.drawLine(0, 500, 800, 500);
			g.drawLine(300, 500, 300, 600);
			g.drawLine(400, 500, 400, 600);
			g.drawLine(500, 500, 500, 600);
			g.drawLine(600, 500, 600, 600);
			g.drawLine(700, 500, 700, 600);
			g.drawString("Sim", 340, 552);

			toolbarAndGate.draw(g, levelData, inputInfo, ignore1);
			g.drawString("" + numAndGates, toolbarAndGate.getPosition().x, toolbarAndGate.getPosition().y);

			toolbarNotGate.draw(g, levelData, inputInfo, ignore1);
			g.drawString("" + numNotGates, toolbarNotGate.getPosition().x, toolbarNotGate.getPosition().y);

			toolbarOrGate.draw(g, levelData, inputInfo, ignore1);
			g.drawString("" + numOrGates, toolbarOrGate.getPosition().x, toolbarOrGate.getPosition().y);

			/* Logic for current gate being carried. */
			if (inputInfo.isClicked()) {
				if (carrying != null) {
					boolean gatePutInSlot = false;
					for (Slot slot : slots)
						if (slot.inBounds(inputInfo.getX(), inputInfo.getY()))
							if (slot.canAccept(carrying)) {
								LogicGate gate = slot.getGate();
								if (gate != null) {
									if (gate instanceof AndGate)
										numAndGates++;
									else if (gate instanceof NotGate)
										numNotGates++;
									else if (gate instanceof OrGate)
										numOrGates++;
								}
								slot.setGate(carrying);
								slot.draw(g, levelData, inputInfo, ignore1);
								gatePutInSlot = true;
							}

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
					if (inputInfo.getX() > 400 && inputInfo.getX() < 500
						&& inputInfo.getY() > 500 && numAndGates > 0) {
						carrying = new AndGate(imageSet.getAnd(), new Point(inputInfo.getX(), inputInfo.getY()), null);
						numAndGates--;
					} else if (inputInfo.getX() > 500 && inputInfo.getX() < 600 && inputInfo.getY() > 500
						&& numOrGates > 0) {

						carrying = new OrGate(imageSet.getOr(), new Point(inputInfo.getX(), inputInfo.getY()), null);
						numOrGates--;
					} else if (inputInfo.getX() > 700 && inputInfo.getY() > 500 && numNotGates > 0) {
						System.out.println("Picked up NotGate");
						carrying = new NotGate(imageSet.getNot(), new Point(inputInfo.getX(), inputInfo.getY()), null);
						numNotGates--;
					} else if (inputInfo.getX() > 300 && inputInfo.getX() < 400 && inputInfo.getY() > 500) {
						boolean filled = true;
						for (Slot slot : slots)
							if (!slot.filled())
								filled = false;
						if (filled)
							switchToSimulationMode();
						else
							System.out.println("Could not change to simulation mode.");
					}
					/* clicks on slots - to remove gates perhaps */
					for (Slot slot : slots) {
						if (slot.inBounds(inputInfo.getX(), inputInfo.getY())) {
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
				carrying.setPosition(inputInfo.getX(), inputInfo.getY());

			if (carrying != null)
				carrying.draw(g, levelData, inputInfo, ignore1);
		} else if (levelMode == LevelMode.Simulation) {
			levelData = new LevelData(LevelMode.Simulation, simulationIndex);
			for (Slot slot : slots)
				slot.draw(g, levelData, inputInfo, ignore1);
			for (Wire wire : wires)
				wire.draw(g, levelData, inputInfo, ignore1);
			for (Signal signal : signals) {
				signal.draw(g, levelData, inputInfo, ignore1);
				String name = signal.getName();
				for (SavedSignal saved : savedOutputSignals) {
					if (saved.name == name) {
						saved.signal[simulationIndex] = signal.evaluate(simulationIndex);
					}
				}
			}

			tick++;
			if (tick >= 50) {
				for (Signal signal : signals) {
					if (signal.isOutput()) {
						SignalLevel level = signal.evaluate(simulationIndex);
						if (!(signal.evaluate(simulationIndex) == signal.getSignalLevelAt(simulationIndex)))
							tempCorrect = false;
					}
				}

				tick = 0;
				System.out.println("Simulation Tick.");
				simulationIndex++;
			}
			if (simulationIndex >= signals[0].getSignalLength()) {
				if (!tempCorrect) /* Stay in sim mode if correct so no redraw before level change. */
					levelMode = LevelMode.Construction;
				System.out.println("Solution is " + tempCorrect);
				correct = tempCorrect;
			}

		}

		drawSignalWindow(g);
	}

	private void switchToSimulationMode() {
		levelMode = LevelMode.Simulation;
		tick = simulationIndex = 0;
		correct = false;
		tempCorrect = true;
		System.out.println("Changed to simulation mode.");
		int numOutputs = 0;
		for (Signal signal : signals)
			if (signal.isOutput())
				numOutputs++;
		savedOutputSignals = new SavedSignal[numOutputs];

		int savedJ = 0;
		for (int i = 0; i < numOutputs; i++) {
			for (int j = savedJ; j < signals.length; j++) {
				if (signals[j].isOutput()) {
					savedJ = j + 1;
					savedOutputSignals[i] = new SavedSignal(new SignalLevel[signals[0].getSignalLength()], signals[j].getName());
				}
			}
		}
	}

	private void drawSignalWindow(Graphics2D g) {
		/* draw column for signal name */
		g.setColor(Color.BLACK);
		g.drawLine(50, toolbarUpperY, 50, toolbarLowerY);

		/* figure out size of signal box given level mode */
		int windowWidth = 0, numBoxes = 0;
		if (levelMode == LevelMode.Construction) {
			windowWidth = 300;
			numBoxes = signals[0].getSignalLength();
		} else if (levelMode == LevelMode.Simulation) {
			windowWidth = 800;
			numBoxes = signals[0].getSignalLength();
		}
		int numSignals = Math.min(signals.length, 3);
		int boxSize = (windowWidth - 50) / numBoxes;
		int boxHeight = (toolbarLowerY - toolbarUpperY) / numSignals;

		/* draw vertical (signal over time) lines */
		for (int i = 1; i <= numBoxes; i++)
			g.drawLine(50 + i * (windowWidth - 50) / numBoxes, toolbarUpperY, 50 + i * (windowWidth - 50) / numBoxes, toolbarLowerY);

		for (int i = 0; i < numSignals; i++) {
			int lineY = toolbarUpperY + (toolbarLowerY - toolbarUpperY) / numSignals * i + i;
			g.drawLine(0, lineY, windowWidth, lineY);
		}

		for (int i = signalIndex; i < signalIndex + numSignals; i++)
			g.drawString(signals[i].getName(), 25, toolbarUpperY + (i - signalIndex + 1) * ((toolbarLowerY - toolbarUpperY) / numSignals));

		int step = 9;
		g.setColor(Color.RED);
		for (int i = 0; i < numSignals; i++) {
			SignalLevel previous = SignalLevel.Off;
			for (int j = 0; j < numBoxes; j++) {
				int x = 50 + j * boxSize;
				SignalLevel level = signals[signalIndex + i].getSignalLevelAt(j);
				if (previous != level)
					g.drawLine(x, toolbarUpperY + boxHeight * i + step, x, toolbarUpperY + boxHeight * (i + 1) - step);
				previous = level;
				if (level == SignalLevel.On)
					g.drawLine(x, toolbarUpperY + boxHeight * i + step, x + boxSize, toolbarUpperY + boxHeight * i + step);
				else if (level == SignalLevel.Off)
					g.drawLine(x, toolbarUpperY + boxHeight * (i + 1) - step, x + boxSize, toolbarUpperY + boxHeight * (i + 1) - step);
			}
		}

		if (levelMode == LevelMode.Simulation) {
			double scanX = 50 + simulationIndex * boxSize + (tick * boxSize / 50.0);
			g.setColor(Color.YELLOW);
			g.drawLine((int) scanX, toolbarUpperY, (int) scanX, toolbarLowerY);


			for (int i = signalIndex; i < signalIndex + numSignals; i++) {
				for (int k = 0; k < savedOutputSignals.length; k++) {
					SavedSignal saved = savedOutputSignals[k];
				if (signals[i].getName() == saved.name) {

	SignalLevel previous = SignalLevel.Off;
	for (int j = 0; j < numBoxes; j++) {
		int x = 50 + j * boxSize;
		SignalLevel level = saved.signal[j];

		int newi = i - signalIndex;

		if (level == SignalLevel.On) {
			if (j == simulationIndex)
				g.drawLine(x, toolbarUpperY + boxHeight * newi + step, (int) scanX, toolbarUpperY + boxHeight * newi + step);
			else
				g.drawLine(x, toolbarUpperY + boxHeight * newi + step, x + boxSize, toolbarUpperY + boxHeight * newi + step);
			if (previous != level)
				g.drawLine(x, toolbarUpperY + boxHeight * newi + step, x, toolbarUpperY + boxHeight * (newi + 1) - step);
		} else if (level == SignalLevel.Off) {
			if (j == simulationIndex)
				g.drawLine(x, toolbarUpperY + boxHeight * (newi + 1) - step, (int) scanX, toolbarUpperY + boxHeight * (newi + 1) - step);
			else
				g.drawLine(x, toolbarUpperY + boxHeight * (newi + 1) - step, x + boxSize, toolbarUpperY + boxHeight * (newi + 1) - step);
			if (previous != level)
				g.drawLine(x, toolbarUpperY + boxHeight * newi + step, x, toolbarUpperY + boxHeight * (newi + 1) - step);

		}
		previous = level;
	}



					}
				}
			}
		}
	}

	@Override
	public boolean finishedAnimation() {
		return correct;
	}
}

