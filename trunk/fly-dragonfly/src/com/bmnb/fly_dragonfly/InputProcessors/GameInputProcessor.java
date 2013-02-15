/**
 * 
 */
package com.bmnb.fly_dragonfly.InputProcessors;

import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.math.Vector2;
import com.bmnb.fly_dragonfly.Screen.GameScreen;

/**
 * @author bjtal
 *
 */
public class GameInputProcessor implements InputProcessor{

	GameScreen screen;
	
	public GameInputProcessor(GameScreen screen){
		this.screen = screen;
	}
	
	@Override
	public boolean keyDown(int keycode) {
		
		switch (keycode){
		case Keys.W :
			screen.player.direction.y = 1;
			break;
		case Keys.S :
			screen.player.direction.y = -1;
			break;
		case Keys.A :
			screen.player.direction.x = -1;
			break;
		case Keys.D :
			screen.player.direction.x = 1;
			break;
		}
		
		return false;
	}

	@Override
	public boolean keyUp(int keycode) {
		
		switch (keycode){
		case Keys.W :
			screen.player.direction.y = 0;
			break;
		case Keys.S :
			screen.player.direction.y = 0;
			break;
		case Keys.A :
			screen.player.direction.x = 0;
			break;
		case Keys.D :
			screen.player.direction.x = 0;
			break;
		}
		
		return false;
	}

	@Override
	public boolean keyTyped(char character) {
		return false;
	}

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		
		screen.player.moveToFinger(new Vector2(screenX, Gdx.graphics.getHeight() - screenY));
		
		return true;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		this.screen.player.direction.x = 0;
		this.screen.player.direction.y = 0;
		return true;
	}

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		
		screen.player.moveToFinger(new Vector2(screenX, Gdx.graphics.getHeight() - screenY));
		
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
