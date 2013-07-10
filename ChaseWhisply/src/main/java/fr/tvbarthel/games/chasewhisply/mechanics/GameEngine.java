package fr.tvbarthel.games.chasewhisply.mechanics;

import fr.tvbarthel.games.chasewhisply.model.DisplayableItem;
import fr.tvbarthel.games.chasewhisply.model.DisplayableItemFactory;
import fr.tvbarthel.games.chasewhisply.model.TargetableItem;

abstract public class GameEngine implements ReloadingRoutine.IReloadingRoutine {
	public static final int STATE_STOP = 0x00000001;
	public static final int STATE_RUNNING = 0x00000002;
	public static final int STATE_PAUSED = 0x00000003;

	protected GameInformation mGameInformation;
	protected ReloadingRoutine mReloadingRoutine;
	protected int mCurrentState;


	public GameEngine(GameInformation gameInformation) {
		mGameInformation = gameInformation;
		mReloadingRoutine = new ReloadingRoutine(mGameInformation.getWeapon().getReloadingTime(), this);
		mCurrentState = STATE_STOP;
	}

	/**
	 * start the game, should be called only at the beginning once.
	 */
	public void startGame() {
		mReloadingRoutine.startRoutine();
		mCurrentState = STATE_RUNNING;
		//TODO
	}

	/**
	 * pause the game.
	 */
	public void pauseGame() {
		mReloadingRoutine.stopRoutine();
		mCurrentState = STATE_PAUSED;
		//TODO
	}

	/**
	 * resume the game
	 */
	public void resumeGame() {
		mReloadingRoutine.startRoutine();
		mCurrentState = STATE_RUNNING;
		//TODO
	}

	/**
	 * stop the game, should be called only once at the end.
	 */
	public void stopGame() {
		mReloadingRoutine.stopRoutine();
		mCurrentState = STATE_STOP;
		//TODO
	}

	@Override
	public void reload() {
		mGameInformation.getWeapon().reload();
	}

	/**
	 * call when use touch screen
	 * create bullet hole or hit current target
	 */
	public void fire() {
		final int dmg = mGameInformation.getWeapon().fire();
		final TargetableItem currentTarget = mGameInformation.getCurrentTarget();
		if (dmg != 0) {
			mGameInformation.bulletFired();
			if (currentTarget == null) {
				mGameInformation.resetCombo();
				DisplayableItem hole = DisplayableItemFactory.createBulletHole();
				final float[] currentPosition = mGameInformation.getCurrentPosition();
				hole.setX((int) currentPosition[0]);
				hole.setY((int) currentPosition[1]);
				mGameInformation.addDisplayableItem(hole);
			} else {
				currentTarget.hit(dmg);
				if (!currentTarget.isAlive()) {
					//traget killed
					mGameInformation.targetKilled();
					mGameInformation.stackCombo();
				}
			}
		}
	}

	public void changePosition(float posX, float posY) {
		mGameInformation.setCurrentPosition(posX, posY);
	}

	/**
	 * Getters & Setters
	 */
	public void setSceneWidth(int sceneWidth) {
		mGameInformation.setSceneWidth(sceneWidth);
	}

	public void setSceneHeight(int sceneHeight) {
		mGameInformation.setSceneHeight(sceneHeight);
	}

	public GameInformation getGameInformation() {
		return mGameInformation;
	}

	public int getCurrentState() {
		return mCurrentState;
	}

}
