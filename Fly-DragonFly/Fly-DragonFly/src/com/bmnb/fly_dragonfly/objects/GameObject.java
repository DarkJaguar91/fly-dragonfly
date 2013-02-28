/**
 * 
 */
package com.bmnb.fly_dragonfly.objects;

import java.util.ArrayList;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.bmnb.fly_dragonfly.screens.GameScreen;

/**
 * @author Brandon
 * 
 */
public abstract class GameObject extends Sprite {

	/**
	 * global vars
	 */
	protected boolean dead = false, removeable = false;
	protected float screenWidth, screenHeight, speed;
	protected Vector2 direction;
	protected Vector2 oldPosition;
	protected ArrayList<Vector2> gridLocations;

	public GameObject(Vector2 position, float width, float height, float speed,
			float scWidth, float scHeight) {
		super();
		setSize(width, height);
		setPosition(position);

		this.speed = speed;
		this.screenWidth = scWidth;
		this.screenHeight = scHeight;
		this.direction = new Vector2();
		
		// nathans stuff, if dont work -> delete
		oldPosition = new Vector2(position);
		gridLocations = new ArrayList<Vector2>();
	}

	/**
	 * @param sprite
	 */
	public GameObject(Sprite sprite) {
		super(sprite);

		oldPosition = new Vector2(this.getPosition());
		gridLocations = new ArrayList<Vector2>();
	}

	@Override
	public void draw(SpriteBatch spriteBatch, float delta) {
		super.draw(spriteBatch);
		update(delta);
	}

	/**
	 * Updates the object
	 * 
	 * @param delta
	 *            The delta time for the game
	 */
	public void update(float delta) {
		move(delta);
		GameScreen.moveObject(this);
	}

	/**
	 * Moves the object
	 * 
	 * @param delta
	 *            the delta time for the gamme
	 */
	protected abstract void move(float delta);

	@Override
	public void setPosition(float x, float y) {
		this.setX(x - this.getWidth() / 2f);
		this.setY(y - this.getHeight() / 2f);
	}

	/**
	 * setter for position
	 * 
	 * @param pos
	 *            position for the object
	 */
	public void setPosition(Vector2 pos) {
		this.setX(pos.x - this.getWidth() / 2f);
		this.setY(pos.y - this.getHeight() / 2f);
	}

	/**
	 * Gets the postion as a vector 2
	 * 
	 * @return A vector2 for the objects position
	 */
	public Vector2 getPosition() {
		return new Vector2(this.getX(), this.getY());
	}

	@Override
	public float getX() {
		return super.getX() + this.getWidth() / 2f;
	}

	@Override
	public float getY() {
		return super.getY() + this.getHeight() / 2f;
	}

	/**
	 * Getter for the dead boolean
	 * 
	 * @return Whether the object is dead or not
	 */
	public boolean isDead() {
		return dead;
	}

	/**
	 * Kills the object
	 */
	public void kill() {
		dead = true;
	}

	/**
	 * Checks if the object is now removeable (for death animations)
	 * 
	 * @return If the object is removeable
	 */
	public boolean isRemovable() {
		return removeable;
	}

	// update the object's list of grid locations
	public void setNewLocation(Vector2 newPosition) {
		gridLocations.add(newPosition);
	}

	// return the size of the list of grid locations cotaining the object
	public int getCapacity() {
		return gridLocations.size();
	}

	// return the location vector for specified index
	public Vector2 getLocation(int index) {
		return gridLocations.get(index);
	}

	// clear the list of grid locations containing the object
	public void removeAllLocations() {
		gridLocations = new ArrayList<Vector2>();
	}

	public Vector2 getPreviousPos() {
		return oldPosition;
	}
}