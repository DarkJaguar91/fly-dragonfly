package com.bmnb.fly_dragonfly;

import com.badlogic.gdx.Game;
import com.bmnb.fly_dragonfly.Screen.GameScreen;

public class Fly_DragonFly extends Game {

	@Override
	public void create() {
		setScreen(new GameScreen());
	}
}
