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
	protected void move(float delta) {
		// calculate the new direction to move if the player wants it to move
		Vector2 fingerpos = lastFingerPos.cpy();
		Vector2 pos = position.cpy();

		fingerpos.sub(pos);
		fingerpos.nor();

		direction = fingerpos.cpy();
		// calculated the correct direction

		/*
		 * movement checking (if close to finger) the ship stops - note, since
		 * we stop the ship from moving all the way up the screen we reach an
		 * issue where the player could move off the screen. We thus have to add
		 * a extra check for if the finger is too far up with the y value
		 * section
		 */
		if (position.dst(lastFingerPos) <= speed * delta * direction.len()) {
			direction = new Vector2();
			position = lastFingerPos;
		}
		// end of close check

		// x movement
		this.position.x += this.direction.x * delta * speed;
		// since the dragonfly is moving already (scrolling background)
		// it makes sense that the fly cant move forward in the screen as
		// quickly as backward
		// this fixes this.
		float ySpeed = direction.y > 0 ? speed / 2f : speed * 1.5f;
		// y movement
		this.position.y += this.direction.y > 0 ? this.position.y >= Gdx.graphics
				.getHeight() * 0.2f ? 0 : this.direction.y * delta * ySpeed
				: this.direction.y * delta * ySpeed;
	}

	@Override
	public void stop() {
		lastFingerPos = position.cpy();
		super.stop();
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

		lastFingerPos.y = lastFingerPos.y > Gdx.graphics.getHeight() * 0.2f
				- height / 2f ? Gdx.graphics.getHeight() * 0.2f
				: lastFingerPos.y < height / 2f ? height / 2f : lastFingerPos.y;
		lastFingerPos.x = lastFingerPos.x > Gdx.graphics.getWidth() - width
				/ 2f ? Gdx.graphics.getWidth() - width / 2f
				: lastFingerPos.x < width / 2f ? width / 2f : lastFingerPos.x;
	}
}
