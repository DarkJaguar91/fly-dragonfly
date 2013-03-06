package com.bmnb.fly_dragonfly.objects;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.bmnb.fly_dragonfly.objects.Enemy;

public class Bird extends Enemy {
	private static final float SPEED = 450;
	private Vector2 direction;
	public Bird(Vector2 position, float width, float height, float speed,
			float scWidth, float scHeight, Player player) {
		super(position, width, height, speed, scWidth, scHeight, player);
		direction = player.getPosition()/*.add(new Vector2(0,1).mul(speed))*/.sub(this.getPosition()).nor();
		this.rotate(direction.angle());
		setTexture(new Texture("data/square.png"));
	}

	@Override
	protected void move(float delta) {
		this.setPosition(this.getPosition().add(direction.cpy().mul(SPEED*delta)));

	}

}
