package com.bmnb.fly_dragonfly.Objects;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

/**
 * 
 * @author Brandon James Talbot
 * 
 */

public abstract class StaticObject {
	protected Vector2 position;
	protected float width, height, angle;
	Texture texture;

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
	public StaticObject(Vector2 position, float width, float height,
			float angle, String texture) {
		this.position = position;
		this.width = width;
		this.height = height;
		this.angle = angle;

		loadTexture(texture);
	}

	/**
	 * Update method
	 * 
	 * @param delta
	 *            The delta time for the game
	 */
	public abstract void update(float delta);

	/**
	 * Rotates the object
	 * 
	 * @param amount
	 *            The amount to rotate by.
	 */
	protected void rotate(float amount) {
		angle += amount;
	}

	/**
	 * The method that loads the texture
	 * 
	 * @param textPath
	 *            The texture path
	 */
	protected void loadTexture(String textPath) {
		texture = new Texture(textPath);
	}

	/**
	 * Generated a rectangle for bounding box and returns it
	 * 
	 * @return A rectangle to represent the 2D bounding box
	 */
	public Rectangle getBoundingBox() {
		return new Rectangle(position.x - width / 2f, position.y - height / 2f,
				width, height);
	}

	/**
	 * Draws the object
	 * 
	 * @param batch
	 *            The sprite batch to use (remember to begin the sprite batch)
	 */
	public void Draw(SpriteBatch batch) {
		batch.draw(texture, position.x - width / 2f, position.y - height / 2f,
				width / 2f, height / 2f, width, height, 1, 1, angle, 0, 0,
				texture.getWidth(), texture.getHeight(), false, false);
	}

	/**
	 * @return the position
	 */
	public Vector2 getPosition() {
		return position;
	}

	/**
	 * @return the width
	 */
	public float getWidth() {
		return width;
	}

	/**
	 * @return the height
	 */
	public float getHeight() {
		return height;
	}

	/**
	 * @return the angle
	 */
	public float getAngle() {
		return angle;
	}

}
