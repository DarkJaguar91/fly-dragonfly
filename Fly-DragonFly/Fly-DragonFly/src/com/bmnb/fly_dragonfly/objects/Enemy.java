package com.bmnb.fly_dragonfly.objects;

import com.badlogic.gdx.math.Vector2;

public abstract class Enemy extends GameObject{

	protected float health = 100;
	protected Player player;
	
	public Enemy(Vector2 position, float width, float height, float speed,
			float scWidth, float scHeight, Player player) {
		super(position, width, height, speed, scWidth, scHeight);
		this.player = player;
	}
	
	public void doDamage(float amount){
		health -= amount;
		
		if (health <= 0)
			kill();
	}
}
