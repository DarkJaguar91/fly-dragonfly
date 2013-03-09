package com.bmnb.fly_dragonfly.flocking;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.bmnb.fly_dragonfly.objects.GameObject;

public class Boid extends GameObject{
	private static final float MAX_SPEED = 85f;
	private static final float MIN_SPEED = 50f;
	
	private Vector2 oldPosition;
	public Boid(Vector2 position, Vector2 direction, float width,
			float height, float scWidth, float scHeight){
		super(position,width,height,(float)Math.random()*MAX_SPEED,scWidth,scHeight);
		this.direction = direction; 
		setTexture(new Texture("data/boid.png"));
		
		sortVal = 1;
	}
	
	
	
	@Override
	public void kill() {
		this.removeable = true;
		super.kill();
	}



	public void setSpeed(float i){
		assert(i >= 0);
		this.speed = i;
	}
	public Vector2 getOldPosition() {
		return oldPosition.cpy();
	}
	public void setOldPosition(Vector2 oldPosition) {
		this.oldPosition = oldPosition;
	}
	public Vector2 getVelocity() {
		return direction.cpy().mul(speed);
	}
	public void setVelocity(Vector2 velocity) {
		this.speed = velocity.len();
		this.direction = velocity.cpy().nor();
	}
	@Override
	protected void move(float delta) {
		setOldPosition(getPosition());
		speed = Math.max(MIN_SPEED, Math.min(speed, MAX_SPEED)); //clamp speed 
		setPosition(getPosition().add(getVelocity().mul(delta)));
	}
	@Override
	public void draw(SpriteBatch batch, float delta){
		this.rotate(direction.angle());
		super.draw(batch,delta);
		this.rotate(-direction.angle());
	}
}
