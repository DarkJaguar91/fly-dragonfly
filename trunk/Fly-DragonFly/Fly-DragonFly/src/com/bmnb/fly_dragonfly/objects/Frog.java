package com.bmnb.fly_dragonfly.objects;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.bmnb.fly_dragonfly.screens.GameScreen;

public class Frog extends StaticEnemy{

	private static final float DIFF_Y_TRIGGER = 400;
	private boolean hasTriggered = false;
	
	public Frog(Vector2 position, float width, float height, float speed,
			float scWidth, float scHeight, Player player) {
		super(position, width, height, speed, scWidth, scHeight, player,100);
		
		sortVal = 2;
		
		setTexture(new Texture("data/square.png"));
	}
	
	@Override
	public void update(float delta) {
		
		if (isDead())
			removeable = true;
		
		if (this.getY() - player.getY() < DIFF_Y_TRIGGER && !hasTriggered){
			hasTriggered = true;
			GameScreen.addObject(new Tongue(new Vector2(this.getX(), this.getY()), this.getWidth(), this.getHeight(), this.speed,
					screenWidth, screenHeight, player));
		}
		
		super.update(delta);
	}
}
