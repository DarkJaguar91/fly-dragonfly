package com.bmnb.fly_dragonfly.objects;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.bmnb.fly_dragonfly.objects.Enemy;

public class Bird extends Enemy {
	private static final float SPEED = 650;
	private static final float RANDOM_COUNTDOWN = 5;
	private Vector2 direction;
	private float countdown;
	private boolean shouldCalculate = true; 
	public Bird(Vector2 position, float width, float height, float speed,
			float scWidth, float scHeight, Player player) {
		super(position, width, height, speed, scWidth, scHeight, player,200);
		countdown = (float)Math.random()*RANDOM_COUNTDOWN;
		sortVal = 1;
		setTexture(new Texture("data/textures/bird1.png"));
	}
	
	@Override
	public void kill() {
		removeable = true;
		super.kill();
	}



	@Override
	protected void move(float delta) {
		if (countdown <= 0 ){
			if (shouldCalculate){
				direction = player.getPosition().sub(this.getPosition()).nor();
				this.rotate(direction.angle() - 90);
				shouldCalculate = false;
			}
			this.setPosition(this.getPosition().add(direction.cpy().mul(SPEED*delta)));
		} else {
			translateY(-speed * delta);
			countdown -= delta;
		}
		
		if (this.getPosition().y + this.getHeight() <= 0){
			this.kill();
			this.removeable = true;
		}
	}
	@Override
	public void draw(SpriteBatch spriteBatch, float delta){
		if (countdown >= 0 ){
			update(delta);
		}
		else super.draw(spriteBatch,delta);
	}
}
