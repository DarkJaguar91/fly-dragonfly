package com.bmnb.fly_dragonfly.objects;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.bmnb.fly_dragonfly.graphics.GameParticleEmitter;

/**
 * Player class, holds all methods needed for the player specifically
 * 
 * @author Brandon James Talbot
 * 
 */
public class Player extends GameObject {

	/**
	 * Static set up vars
	 */
	protected static final float maxVertPath = 0.35f, upSpeedPercent = 0.8f,
			downSpeedPercent = 1.2f;

	/**
	 * Global vars
	 */
	protected Vector2 targetPosition;
	protected GameParticleEmitter dragonBreath;

	/**
	 * Constructor
	 * 
	 * @param position
	 * @param width
	 * @param height
	 * @param speed
	 * @param scWidth
	 * @param scHeight
	 */
	public Player(Vector2 position, float width, float height, float speed,
			float scWidth, float scHeight) {
		super(position, width, height, speed, scWidth, scHeight);
		targetPosition = position;
	}

	@Override
	protected void loadTexture() {
		setTexture(new Texture("data/libgdx.png"));
		try {
			dragonBreath = new GameParticleEmitter(new BufferedReader(
					new InputStreamReader(Gdx.files.internal(
							"data/dragonflyBreath").read()), 512), new Texture(
					"data/ember.png"));
			
			dragonBreath.setContinuous(false);
		} catch (Exception e) {
			Gdx.app.log("error", e.getMessage());
		}
	}

	/**
	 * Sets the new position the player should move to
	 * 
	 * @param fingerPos
	 *            The position of the finger
	 */
	public void moveToFinger(Vector2 fingerPos) {
		targetPosition.x = fingerPos.x < this.getWidth() / 2f ? this.getWidth() / 2f
				: fingerPos.x > screenWidth - this.getWidth() / 2f ? screenWidth
						- this.getWidth() / 2f
						: fingerPos.x;

		targetPosition.y = fingerPos.y < this.getHeight() / 2f ? this
				.getHeight() / 2f
				: fingerPos.y > screenHeight * maxVertPath ? screenHeight
						* maxVertPath : fingerPos.y;
	}

	/**
	 * Stops the player moving (should be called when finger is released)
	 */
	public void stopMovingToFinger() {
		targetPosition = this.getPosition().cpy();
	}
	
	/**
	 * Starts the shooting of the dragonBreath
	 */
	public void startShooting(){
		dragonBreath.setContinuous(true);
	}

	/**
	 * Stops the shooting of the dragonbreath
	 */
	public void stopShooting(){
		dragonBreath.setContinuous(false);
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see com.bmnb.fly_dragonfly.objects.GameObject#move(float)
	 */
	@Override
	protected void move(float delta) {
		// check distancing
		if (this.getPosition().cpy().sub(targetPosition).len() <= speed * delta) {
			targetPosition = this.getPosition().cpy();
		}

		// calc new direction
		Vector2 direction = targetPosition.cpy().sub(this.getPosition());
		direction.nor();

		// move the player
		this.translateX(speed * delta * direction.x);
		this.translateY(speed
				* (direction.y > 0 ? upSpeedPercent : downSpeedPercent) * delta
				* direction.y);

		// move the particle engine position for the fire breath
		dragonBreath.setPosition(this.getX(), this.getY() + this.getHeight() / 2f);
	}

	/* (non-Javadoc)
	 * @see com.bmnb.fly_dragonfly.objects.GameObject#draw(com.badlogic.gdx.graphics.g2d.SpriteBatch, float)
	 */
	@Override
	public void draw(SpriteBatch spriteBatch, float delta) {
		
		dragonBreath.draw(spriteBatch, delta);
		
		super.draw(spriteBatch, delta);
	}

	
}
