/* Immutable class encapsulating certain information pertinent to the level's
 * current state. */

public class LevelData {
	private final LevelMode levelMode;
	private final int levelIndex;

	public LevelData(LevelMode levelMode, int levelIndex) {
		this.levelMode = levelMode;
		this.levelIndex = levelIndex;
	}

	public LevelMode getLevelMode() {
		return levelMode;
	}

	public int getLevelIndex() {
		return levelIndex;
	}
}

