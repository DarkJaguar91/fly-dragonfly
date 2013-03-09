package com.bmnb.fly_dragonfly.objects;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.bmnb.fly_dragonfly.screens.GameScreen;

public class Web extends StaticEnemy {
	private static final float WIDTH_EXPANSION_SPEED = 180;
	private static final float HEIGHT_EXPANSION_SPEED = 80;
	private static final float MAX_HEIGHT = 130; 
	private int direction = 1;
	public Web(Vector2 position, float width, float height, float speed,
			float scWidth, float scHeight, Player player) {
		super(position, width, height, speed, scWidth, scHeight, player, 10);
		
		sortVal = 3;
		
		direction = (int)(Math.signum(player.getX() - this.getX())*direction);
		setTexture(new Texture("data/square.png"));
	}
	
	@Override
	public void kill(){
		removeable = true;
		super.kill();
	}
	
	@Override
	public void update(float delta){
		super.update(delta);
		this.setSize(this.getWidth() < GameScreen.width ? this.getWidth() + direction*WIDTH_EXPANSION_SPEED*delta : this.getWidth(), 
				this.getHeight() < MAX_HEIGHT ? this.getHeight() + HEIGHT_EXPANSION_SPEED*delta : this.getHeight());
	}

}
