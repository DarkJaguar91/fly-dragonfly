/**
 * 
 */
package com.bmnb.fly_dragonfly.Objects;

import com.badlogic.gdx.math.Vector2;

/**
 * @author bjtal
 * 
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

		// movement checking (if close to finger)
		if ((Math.abs(position.x - lastFingerPos.x) < 2)
				&& (Math.abs(position.y - lastFingerPos.y) < 2)) {
			direction = new Vector2(0, 0);
		}

		super.update(delta);
	}

	public void moveToFinger(Vector2 fingerPos) {
		lastFingerPos = fingerPos.cpy();
		
		Vector2 fingerpos = fingerPos.cpy();
		Vector2 pos = position.cpy();
		
		fingerpos.sub(pos);
		fingerpos.nor();
		
		direction = fingerpos.cpy();
	}
}
