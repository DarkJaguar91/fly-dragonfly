/**
 * 
 */
package com.bmnb.fly_dragonfly.input;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.math.Vector2;
import com.bmnb.fly_dragonfly.objects.Player;

/**
 * @author Brandon
 * 
 */
public class GameInput implements InputProcessor {

	protected float width, height;
	protected Player player;
	protected int movePointer = -1, shootPointer = -1;

	public GameInput(float width, float height, Player player) {
		this.width = width;
		this.height = height;
		this.player = player;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.badlogic.gdx.InputProcessor#keyDown(int)
	 */
	@Override
	public boolean keyDown(int keycode) {
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.badlogic.gdx.InputProcessor#keyUp(int)
	 */
	@Override
	public boolean keyUp(int keycode) {
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.badlogic.gdx.InputProcessor#keyTyped(char)
	 */
	@Override
	public boolean keyTyped(char character) {
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.badlogic.gdx.InputProcessor#touchDown(int, int, int, int)
	 */
	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		screenX = (int) (screenX * ((float) width / (float)Gdx.graphics.getWidth()));
		screenY = (int) (height - screenY
				* ((float) height / (float)Gdx.graphics.getHeight()));
		
		if (movePointer == -1) {
			player.moveToFinger(new Vector2(screenX, screenY));
			movePointer = pointer;
			return true;
		}

		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.badlogic.gdx.InputProcessor#touchUp(int, int, int, int)
	 */
	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		screenX = (int) (screenX * ((float) width / (float)Gdx.graphics.getWidth()));
		screenY = (int) (height - screenY
				* ((float) height / (float)Gdx.graphics.getHeight()));

		if (pointer == movePointer) {
			player.stopMovingToFinger();
			movePointer = -1;
			return true;
		}

		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.badlogic.gdx.InputProcessor#touchDragged(int, int, int)
	 */
	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		screenX = (int) (screenX * ((float) width / (float)Gdx.graphics.getWidth()));
		screenY = (int) (height - screenY
				* ((float) height / (float)Gdx.graphics.getHeight()));

		if (pointer == movePointer) {
			player.moveToFinger(new Vector2(screenX, screenY));
			return true;
		}

		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.badlogic.gdx.InputProcessor#mouseMoved(int, int)
	 */
	@Override
	public boolean mouseMoved(int screenX, int screenY) {
		screenX = (int) (screenX * ((float) width / (float)Gdx.graphics.getWidth()));
		screenY = (int) (height - screenY
				* ((float) height / (float)Gdx.graphics.getHeight()));
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.badlogic.gdx.InputProcessor#scrolled(int)
	 */
	@Override
	public boolean scrolled(int amount) {
		return false;
	}

}
