package com.bmnb.fly_dragonfly.input;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.bmnb.fly_dragonfly.Fly_DragonFly;
import com.bmnb.fly_dragonfly.screens.GameScreen;
import com.bmnb.fly_dragonfly.screens.MenuScreen;

public class MenuInput implements InputProcessor {

	protected MenuScreen screen;
	protected Fly_DragonFly our_game;
	protected float width, height;
	protected float menuScreenStartX, menuScreenStartY, menuScreenWidth, menuScreenHeight;
	
	public MenuInput(Fly_DragonFly g){
		our_game = g;
		width = GameScreen.width;
		height = GameScreen.height;
		menuScreenStartX = 10;
		menuScreenStartY = 50;		
		menuScreenWidth = 280;
		menuScreenHeight = 340;	
	}
	
	public void setGameScreen(MenuScreen in){
		screen = in;
	}
	
	@Override
	public boolean keyDown(int keycode) {
		 if(keycode == Input.Keys.X){
			screen.showHighScores();
		}
		else if(keycode == Input.Keys.V){
			screen.showMainMenu();
		}
		else if(keycode == Input.Keys.BACK){//press back
			//exit game
			System.exit(1);
			return true;
		}
		return false;
	}

	@Override
	public boolean keyUp(int keycode) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean keyTyped(char character) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		//player starts new game		
		if (screenX > ((menuScreenStartX+menuScreenWidth)/2)-70 && screenY > (menuScreenStartY + 180)-35 &&
				screenX < (((menuScreenStartX+menuScreenWidth)/2)-70)+140 && 
				screenY < (menuScreenStartY + 180)+70-35){
			screen.playBtnClicked();					
			return true;
		}
		//player opens options screen		
		if (screenX > ((menuScreenStartX+menuScreenWidth)/2)-50 && screenY > (menuScreenStartY + 110)+125 &&
				screenX < (((menuScreenStartX+menuScreenWidth)/2)-50)+100 && 
				screenY < (menuScreenStartY + 110)+50+125){
			screen.optionsBtnClicked();						
			return true;
		}
		//player exits game
		if (screenX > ((menuScreenStartX+menuScreenWidth)/2)-50 && screenY > (menuScreenStartY + 40)+265 &&
				screenX < ((menuScreenStartX+menuScreenWidth)/2)+100 && screenY < (menuScreenStartY + 40)+50+265){
			screen.exitBtnClicked();						
			return true;
		}
		
		return false;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {		
		//player starts new game		
		if (screenX > ((menuScreenStartX+menuScreenWidth)/2)-70 && screenY > (menuScreenStartY + 180)-35 &&
				screenX < (((menuScreenStartX+menuScreenWidth)/2)-70)+140 && 
				screenY < (menuScreenStartY + 180)+70-35){
			screen.playBtnReleased();			
			return true;
		}
		//player opens options screen		
		if (screenX > ((menuScreenStartX+menuScreenWidth)/2)-50 && screenY > (menuScreenStartY + 110)+125 &&
				screenX < (((menuScreenStartX+menuScreenWidth)/2)-50)+100 && 
				screenY < (menuScreenStartY + 110)+50+125){
			screen.optionsBtnReleased();						
			return true;
		}
		//player exits game
		if (screenX > ((menuScreenStartX+menuScreenWidth)/2)-50 && screenY > (menuScreenStartY + 40)+265 &&
				screenX < ((menuScreenStartX+menuScreenWidth)/2)+100 && screenY < (menuScreenStartY + 40)+50+265){
			screen.exitBtnReleased();						
			return true;
		}
		
		return false;
	}

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean mouseMoved(int screenX, int screenY) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean scrolled(int amount) {
		// TODO Auto-generated method stub
		return false;
	}

}
