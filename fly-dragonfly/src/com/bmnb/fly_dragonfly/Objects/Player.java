/**
 * 
 */
package com.bmnb.fly_dragonfly.Objects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;

/**
 * The player class, this houses all methods needed for the player object
 * 
 * @author Brandon James Talbot
 */
public class Player extends DynamicObject {

	private Vector2 lastFingerPos;

	/**
	 * Constructor
	 * 
	 * @param position
	 *            The position of the object
	 * @param width
	 *            the width of the object
	 * @param height
	 *            the height of the object
	 * @param angle
	 *            the angle of the object
	 * @param speed
	 *            the speed of the object
	 * @param texture
	 *            the texture path
	 */
	public Player(Vector2 position, float width, float height, float angle,
			float speed, String texture) {
		super(position, width, height, angle, speed, texture);
		lastFingerPos = position;
	}

	@Override
	public void update(float delta) {

		// movement checking (if close to finger) the ship stops
		if ((Math.abs(position.x - lastFingerPos.x) < 2)
				&& (Math.abs(position.y - lastFingerPos.y) < 2)) {
			direction = new Vector2(0, 0);
		}

		super.update(delta);
	}

	@Override
	protected void move(float delta) {
		this.position.x += this.direction.x * delta * speed;
		this.position.y += this.direction.y > 0 ? this.position.y >= Gdx.graphics
				.getHeight() * 0.2f ? 0 : this.direction.y * delta * speed
				: this.direction.y * delta * speed;
	}

	/**
	 * This is a method specific to the Player, it allows for moving towards the
	 * finger position sent
	 * 
	 * @param fingerPos
	 *            The position of the finger
	 */
	public void moveToFinger(Vector2 fingerPos) {
		lastFingerPos = fingerPos.cpy();

		Vector2 fingerpos = fingerPos.cpy();
		Vector2 pos = position.cpy();

		fingerpos.sub(pos);
		fingerpos.nor();

		direction = fingerpos.cpy();
	}
}
