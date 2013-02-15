/**
 * 
 */
package com.bmnb.fly_dragonfly.Objects;

import com.badlogic.gdx.math.Vector2;

/**
 * @author Brandon James Talbot
 * 
 */
public abstract class DynamicObject extends StaticObject {

	public Vector2 direction;
	protected float speed;

	/**
	 * Constructor
	 * 
	 * @param position
	 *            The position of the object
	 * @param width
	 *            The width of the object
	 * @param height
	 *            The height of the object
	 * @param angle
	 *            The angle for the object to be rotated by
	 * @param texture
	 *            The texture path for the texture
	 */
	public DynamicObject(Vector2 position, float width, float height,
			float angle, float speed, String texture) {
		super(position, width, height, angle, texture);
		this.speed = speed;
		direction = new Vector2(0, 0);
	}

	/**
	 * Moves the object if needed.
	 * 
	 * @param delta
	 *            The delta game time
	 */
	protected void move(float delta) {
		this.position.x += this.direction.x * delta * speed;
		this.position.y += this.direction.y * delta * speed;
	}

	@Override
	public void update(float delta) {
		move(delta);
	}

	/**
	 * @param speed
	 *            the speed to set
	 */
	public void setSpeed(float speed) {
		this.speed = speed;
	}

}
