package com.bmnb.fly_dragonfly.objects;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;

public class Frog extends StaticEnemy{

	public Frog(Vector2 position, float width, float height, float speed,
			float scWidth, float scHeight) {
		super(position, width, height, speed, scWidth, scHeight);
		
		setTexture(new Texture("data/square.png"));
	}

	@Override
	public void update(float delta) {
		
		if (isDead())
			removeable = true;
		super.update(delta);
	}

	
	
}
