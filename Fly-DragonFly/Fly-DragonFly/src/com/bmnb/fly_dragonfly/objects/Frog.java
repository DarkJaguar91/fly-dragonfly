package com.bmnb.fly_dragonfly.objects;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.bmnb.fly_dragonfly.screens.GameScreen;

public class Frog extends StaticEnemy{

	private static final float DIFF_Y_TRIGGER = 900;
	private boolean hasTriggered = false;
	
	public Frog(Vector2 position, float width, float height, float speed,
			float scWidth, float scHeight, Player player) {
		super(position, width, height, speed, scWidth, scHeight, player);
		
		sortVal = 2;
		
		setTexture(new Texture("data/square.png"));
	}

	@Override
	public void update(float delta) {
		
		if (isDead())
			removeable = true;
		
		if (this.getY() - player.getY() < DIFF_Y_TRIGGER && !hasTriggered){
			hasTriggered = true;
			GameScreen.addObject(new Tongue(this.getPosition(),0,0,GameScreen.scrollSpeed,this.getWidth(),100,player));
		}
		
		super.update(delta);
	}

	
	
}
