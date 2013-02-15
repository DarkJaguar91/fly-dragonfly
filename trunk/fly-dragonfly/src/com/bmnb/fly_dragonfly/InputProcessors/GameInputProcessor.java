/**
 * 
 */
package com.bmnb.fly_dragonfly.InputProcessors;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.math.Vector2;
import com.bmnb.fly_dragonfly.Screen.GameScreen;

/**
 * This is the GameInput Processor, allows for the click, touch etc. settings
 * when the game is running.
 * 
 * @author Brandon James Talbot
 * 
 */
public class GameInputProcessor implements InputProcessor {
	GameScreen screen;

	public GameInputProcessor(GameScreen screen) {
		this.screen = screen;
	}

	@Override
	public boolean keyDown(int keycode) {
		return false;
	}

	@Override
	public boolean keyUp(int keycode) {
		return false;
	}

	@Override
	public boolean keyTyped(char character) {
		return false;
	}

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		screen.getPlayer().moveToFinger(
				new Vector2(screenX, Gdx.graphics.getHeight() - screenY));
		return true;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		this.screen.getPlayer().stop();
		return true;
	}

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		screen.getPlayer().moveToFinger(
				new Vector2(screenX, Gdx.graphics.getHeight() - screenY));
		return true;
	}

	@Override
	public boolean mouseMoved(int screenX, int screenY) {
		return false;
	}

	@Override
	public boolean scrolled(int amount) {
		return false;
	}

}
