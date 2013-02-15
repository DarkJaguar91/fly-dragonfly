package com.me.mygdxgame;

import com.badlogic.gdx.math.Vector2;

public class Boid {
	private Vector2 oldPosition;
	private Vector2 position;
	private Vector2 velocity;
	
	public Boid(Vector2 position, Vector2 velocity){
		this.position = position;
		this.velocity = velocity;
	}
	
	public Vector2 getOldPosition() {
		return oldPosition;
	}
	public void setOldPosition(Vector2 oldPosition) {
		this.oldPosition = oldPosition;
	}
	public Vector2 getPosition() {
		return position;
	}
	public void setPosition(Vector2 position) {
		this.position = position;
	}
	public Vector2 getVelocity() {
		return velocity;
	}
	public void setVelocity(Vector2 velocity) {
		this.velocity = velocity;
	}
	
}
