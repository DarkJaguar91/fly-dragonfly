package com.bmnb.fly_dragonfly.objects;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.bmnb.fly_dragonfly.graphics.GameParticleEmitter;
import com.bmnb.fly_dragonfly.graphics.GameParticleEmitter.ParticleType;
import com.bmnb.fly_dragonfly.tools.MathTools;

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
			downSpeedPercent = 1.2f, mosDamage = 0.5f, flyDamage = 1.5f;
	protected static final float[] mosColor = { 0.047058824f, 0.105882354f,
			0.91764706f },
			flyColor = { 0.91764706f, 0.105882354f, 0.047058824f },
			mosAngle = { 0.0f, 0.0f, 0.0f }, flyAngle = { 0.6f, 0.8f, 1.0f },
			mosVel3 = { 1.0f, 1.0f, 1.0f }, flyVel3 = { 0.6f, 0.8f, 1.0f },
			mosVel2 = { 600f, 700f }, flyVel2 = { 400f, 500f };

	/**
	 * Global vars
	 */
	protected Vector2 targetPosition;
	protected GameParticleEmitter dragonBreath;
	protected float damage = flyDamage;

	/**
	 * Constructor
	 * 
	 * @param position
	 *            The position of the object
	 * @param width
	 *            The width of the object
	 * @param height
	 *            the height of the object
	 * @param speed
	 *            the speed for the object
	 * @param scWidth
	 *            the scren width of the game
	 * @param scHeight
	 *            the screen height of the game
	 */
	public Player(Vector2 position, float width, float height, float speed,
			float scWidth, float scHeight) {
		super(position, width, height, speed, scWidth, scHeight);

		sortVal = 0;
		
		targetPosition = position;

		// load textures (loading here for now, must create texture loader
		// later)
		this.setTexture(new Texture("data/square.png"));
		// animator = new SpriteAnimator(new TextureAtlas(
		// Gdx.files.internal("data/dragon.pack")), "dragon", 15, this);
		//animator = new SpriteAnimator(new Texture("data/dragon2.png"), 10, 10,
		//		"dragon", 15, this);		
		try {
			dragonBreath = new GameParticleEmitter(new BufferedReader(
					new InputStreamReader(Gdx.files.internal(
							"data/dragonflyBreath").read()), 512), new Texture(
					"data/particle.png"), ParticleType.fire);

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
	public void startShooting() {
		dragonBreath.setContinuous(true);
	}

	/**
	 * Stops the shooting of the dragonbreath
	 */
	public void stopShooting() {
		dragonBreath.setContinuous(false);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.bmnb.fly_dragonfly.objects.GameObject#move(float)
	 */
	@Override
	protected void move(float delta) {
		// Gdx.app.log("grid pos", getLocation(0).x + " - " + getLocation(0).y);

		// check distancing
		if (this.getPosition().cpy().sub(targetPosition).len() <= speed * delta) {
			targetPosition = this.getPosition().cpy();
		}

		// calc new direction
		direction = targetPosition.cpy().sub(this.getPosition());
		direction.nor();

		// move the player
		this.translateX(speed * delta * direction.x);
		this.translateY(speed
				* (direction.y > 0 ? upSpeedPercent : downSpeedPercent) * delta
				* direction.y);

		// move the particle engine position for the fire breath
		dragonBreath.setPosition(this.getX(), this.getY() + this.getHeight()
				/ 2f);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.bmnb.fly_dragonfly.objects.GameObject#draw(com.badlogic.gdx.graphics
	 * .g2d.SpriteBatch, float)
	 */
	@Override
	public void draw(SpriteBatch spriteBatch, float delta) {

		dragonBreath.draw(spriteBatch, delta);

		super.draw(spriteBatch, delta);
	}

	/**
	 * Converts the gun into the firefly version by 1 step
	 */
	public void convertWeaponFireflies() {
		dragonBreath.getTint().setColors(
				MathTools.interp(dragonBreath.getTint().getColors(), flyColor,
						mosColor, 1));
		dragonBreath.getAngle().setScaling(
				MathTools.interp(dragonBreath.getAngle().getScaling(),
						flyAngle, mosAngle, 1));
		dragonBreath.getVelocity().setScaling(
				MathTools.interp(dragonBreath.getVelocity().getScaling(),
						flyVel3, mosVel3, 1));
		float vel[] = new float[2];
		vel[0] = dragonBreath.getVelocity().getHighMin();
		vel[1] = dragonBreath.getVelocity().getHighMax();
		vel = MathTools.interp(vel, flyVel2, mosVel2, 1);
		dragonBreath.getVelocity().setHigh(vel[0], vel[1]);

		damage = damage + Math.abs(flyDamage - mosDamage) * 0.01f;
		damage = damage > flyDamage ? flyDamage : damage;
	}

	/**
	 * Converts the gun into the mossy version by 1 step
	 */
	public void convertWeaponMossies() {
		dragonBreath.getTint().setColors(
				MathTools.interp(dragonBreath.getTint().getColors(), mosColor,
						flyColor, 1));
		dragonBreath.getAngle().setScaling(
				MathTools.interp(dragonBreath.getAngle().getScaling(),
						mosAngle, flyAngle, 1));
		dragonBreath.getVelocity().setScaling(
				MathTools.interp(dragonBreath.getVelocity().getScaling(),
						mosVel3, flyVel3, 1));
		float vel[] = new float[2];
		vel[0] = dragonBreath.getVelocity().getHighMin();
		vel[1] = dragonBreath.getVelocity().getHighMax();
		vel = MathTools.interp(vel, mosVel2, flyVel2, 1);
		dragonBreath.getVelocity().setHigh(vel[0], vel[1]);

		damage = damage - Math.abs(flyDamage - mosDamage) * 0.01f;
		damage = damage < mosDamage ? mosDamage : damage;
	}

	/**
	 * Returns the damage of the players fireBreath
	 * 
	 * @return the firebreath damage
	 */
	public float getDamage() {
		return damage;
	}
}
