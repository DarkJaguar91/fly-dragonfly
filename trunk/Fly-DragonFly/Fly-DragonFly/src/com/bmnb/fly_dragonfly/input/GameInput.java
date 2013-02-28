/**
 * 
 */
package com.bmnb.fly_dragonfly.input;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.math.Vector2;
import com.bmnb.fly_dragonfly.objects.Player;
import com.bmnb.fly_dragonfly.screens.GameScreen;

/**
 * @author Brandon
 * 
 */
public class GameInput implements InputProcessor {

	protected float width, height;
	protected Player player;
	protected int movePointer = -1, shootPointer = -1;

	// debug
	protected GameScreen screen;
	
	public GameInput(float width, float height, Player player) {
		this.width = width;
		this.height = height;
		this.player = player;
	}

	public void setGameScreen(GameScreen in){
		screen = in;
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
		screenX = (int) (screenX * ((float) width / (float) Gdx.graphics
				.getWidth()));
		screenY = (int) (height - screenY
				* ((float) height / (float) Gdx.graphics.getHeight()));
//	 	debug
		if (screenX > width * 0.4f && screenX < width * 0.6f && screenY > height * 0.8f){
			screen.spawnBoids();
			return true;
		}
//	 	debug
		if (screenX < width * 0.2f && screenY > height * 0.8f){
			player.convertWeaponFireflies();
			return true;
		}
//	 	debug
		if (screenX > width * 0.8f && screenY > height * 0.8f){
			player.convertWeaponMossies();
			return true;
		}
		
		if (shootPointer == -1) {
			if (screenX < width * 0.1f && screenY < width * 0.1f) {
				player.startShooting();
				shootPointer = pointer;
				return true;
			}
		}

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
		screenX = (int) (screenX * ((float) width / (float) Gdx.graphics
				.getWidth()));
		screenY = (int) (height - screenY
				* ((float) height / (float) Gdx.graphics.getHeight()));

		if (shootPointer == pointer) {
			player.stopShooting();
			shootPointer = -1;
			return true;
		}

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
		screenX = (int) (screenX * ((float) width / (float) Gdx.graphics
				.getWidth()));
		screenY = (int) (height - screenY
				* ((float) height / (float) Gdx.graphics.getHeight()));

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
		screenX = (int) (screenX * ((float) width / (float) Gdx.graphics
				.getWidth()));
		screenY = (int) (height - screenY
				* ((float) height / (float) Gdx.graphics.getHeight()));
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
