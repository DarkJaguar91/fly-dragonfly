package com.bmnb.fly_dragonfly;

import com.badlogic.gdx.Game;
import com.bmnb.fly_dragonfly.screens.GameScreen;

/**
 * Main class to start the game This calls upon the first screen (splash screen)
 * 
 * @author Brandon James Talbot
 * 
 */

public class Fly_DragonFly extends Game {

	@Override
	public void create() {
		setScreen(new GameScreen());
	}
}
