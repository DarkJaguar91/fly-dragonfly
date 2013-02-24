package com.bmnb.fly_dragonfly.objects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;

public abstract class Enemy extends GameObject{

	protected float health = 100;
	
	public Enemy(Vector2 position, float width, float height, float speed,
			float scWidth, float scHeight) {
		super(position, width, height, speed, scWidth, scHeight);
	}
	
	public void doDamage(float amount){
		health -= amount;
		
		Gdx.app.log("frog life", health + "");
		
//		if (health <= 0)
//			kill();
	}
}
