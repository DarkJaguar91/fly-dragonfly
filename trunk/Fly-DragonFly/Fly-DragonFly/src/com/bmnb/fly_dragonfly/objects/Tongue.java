package com.bmnb.fly_dragonfly.objects;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public class Tongue extends StaticEnemy {

	public static final float growRate = 0.7f;// percentage
	protected float length;
	protected Vector2 target;
	protected boolean grow = true;

	public Tongue(Vector2 position, float width, float height, float speed,
			float scWidth, float scHeight, Player player) {
		super(position, width, height, speed, scWidth, scHeight, player,100);

		sortVal = 3;

		target = player.getPosition().cpy();

		target.add((new Vector2(0, 1)).mul(this.speed * 1.5f));

		length = target.cpy().sub(this.getPosition().cpy()).len() * 1.5f;

		// this.setSize(100, 0);

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
		if (this.getHeight() <= 0)
			kill();

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
				this.getY(), this.getWidth() / 2, -this.getHeight(),
				this.getWidth(), this.getHeight(), 1, 1, this.getRotation(), 0,
				0, this.getTexture().getWidth(), this.getTexture().getHeight(),
				false, false);
		// super.draw(spriteBatch);
	}

	@Override
	public void kill() {
		this.removeable = true;
		super.kill();
	}

	public boolean checkCollision(Rectangle o) {
		double a_rectangleMinX = o.x;
		double a_rectangleMinY = o.y;
		double a_rectangleMaxX = o.x + o.width;
		double a_rectangleMaxY = o.y + o.height;
		double a_p1x = this.getPosition().x;
		double a_p1y = this.getPosition().y;
		double a_p2x = target.x;
		double a_p2y = target.y;

		double minX = a_p1x;
		double maxX = a_p2x;

		if (a_p1x > a_p2x) {
			minX = a_p2x;
			maxX = a_p1x;
		}

		// Find the intersection of the segment's and rectangle's x-projections

		if (maxX > a_rectangleMaxX) {
			maxX = a_rectangleMaxX;
		}

		if (minX < a_rectangleMinX) {
			minX = a_rectangleMinX;
		}

		if (minX > maxX) // If their projections do not intersect return false
		{
			return false;
		}

		// Find corresponding min and max Y for min and max X we found before

		double minY = a_p1y;
		double maxY = a_p2y;

		double dx = a_p2x - a_p1x;

		if (Math.abs(dx) > 0.0000001) {
			double a = (a_p2y - a_p1y) / dx;
			double b = a_p1y - a * a_p1x;
			minY = a * minX + b;
			maxY = a * maxX + b;
		}

		if (minY > maxY) {
			double tmp = maxY;
			maxY = minY;
			minY = tmp;
		}

		// Find the intersection of the segment's and rectangle's y-projections

		if (maxY > a_rectangleMaxY) {
			maxY = a_rectangleMaxY;
		}

		if (minY < a_rectangleMinY) {
			minY = a_rectangleMinY;
		}

		if (minY > maxY) // If Y-projections do not intersect return false
		{
			return false;
		}

		return true;
	}
}
