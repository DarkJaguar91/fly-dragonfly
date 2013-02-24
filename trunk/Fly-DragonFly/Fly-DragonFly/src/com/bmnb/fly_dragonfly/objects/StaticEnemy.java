package com.bmnb.fly_dragonfly.objects;

import com.badlogic.gdx.math.Vector2;

public abstract class StaticEnemy extends Enemy{

	public StaticEnemy(Vector2 position, float width, float height,
			float speed, float scWidth, float scHeight) {
		super(position, width, height, speed, scWidth, scHeight);
	}

	@Override
	protected void move(float delta) {
		translateY(-speed * delta);
	}	

}
