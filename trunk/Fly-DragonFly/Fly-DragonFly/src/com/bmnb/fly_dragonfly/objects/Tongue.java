package com.bmnb.fly_dragonfly.objects;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

public class Tongue extends StaticEnemy {

	public static final float growRate = 0.7f;// percentage
	protected float length;
	protected Vector2 target;
	protected boolean grow = true;

	public Tongue(Vector2 position, float width, float height, float speed,
			float scWidth, float scHeight, Player player) {
		super(position, width, height, speed, scWidth, scHeight, player);

		sortVal = 3;

		target = player.getPosition().cpy();
		
		target.add((new Vector2(0, 1)).mul(this.speed * 1.5f));

		length = target.cpy().sub(this.getPosition().cpy()).len() * 1.5f;

		//this.setSize(100, 0);

		float angle = (target.cpy().sub(this.getPosition().cpy())).angle();
		
		this.setRotation(angle - 90);

		this.setTexture(new Texture("data/tongue.png"));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.bmnb.fly_dragonfly.objects.GameObject#update(float)
	 */
	@Override
	public void update(float delta) {
		if (this.getHeight() >= length) {
			grow = false;
		}

		this.setSize(this.getWidth(), this.getHeight()
				+ (grow ? length * delta * growRate : -growRate * length
						* delta));
		// Gdx.app.log("FrogTongue", "Growing - " + this.getHeight());

		super.update(delta);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.bmnb.fly_dragonfly.objects.StaticEnemy#move(float)
	 */
	@Override
	protected void move(float delta) {
		this.target = target.cpy().sub(new Vector2(0, delta * speed));
		super.move(delta);
	}

	@Override
	public void draw(SpriteBatch spriteBatch) {
		spriteBatch.draw(this.getTexture(), this.getX() - this.getWidth() / 2,
				this.getY(), this.getWidth()/2, -this.getHeight(), this.getWidth(), this
						.getHeight(), 1, 1, this.getRotation(), 0, 0, this
						.getTexture().getWidth(),
				this.getTexture().getHeight(), false, false);
		// super.draw(spriteBatch);
	}

	@Override
	public void kill() {
		this.removeable = true;
		super.kill();
	}

}
