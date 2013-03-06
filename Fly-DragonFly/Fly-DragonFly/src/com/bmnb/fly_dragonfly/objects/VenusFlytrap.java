package com.bmnb.fly_dragonfly.objects;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.bmnb.fly_dragonfly.graphics.GameParticleEmitter;

public class VenusFlytrap extends StaticEnemy {
	private static final float VACUUM_RADIUS = 200;
	private static final float MAX_PULL_SPEED = 200;
	private static final float ALPHA = 0.000002f;
	private static final float COUNT_DOWN_MAX = 20;
	private float countDown = COUNT_DOWN_MAX; 
	private GameParticleEmitter poisonGas;
	public VenusFlytrap(Vector2 position, float width, float height,
			float speed, float scWidth, float scHeight, Player player) {
		super(position, width, height, speed, scWidth, scHeight, player);
		
		sortVal = 2;
		
		setTexture(new Texture("data/square.png")); 
		try {
			poisonGas = new GameParticleEmitter(new BufferedReader(
					new InputStreamReader(Gdx.files.internal(
							"data/flytrapSpit").read()), 512), new Texture(
					"data/particle.png"),GameParticleEmitter.ParticleType.spit);
			
			poisonGas.setContinuous(false);
		} catch (Exception e) {
			Gdx.app.log("error", e.getMessage());
		}
	}
	@Override
	public void update(float delta){
		super.update(delta);
		poisonGas.setPosition(this.getX(), this.getY());
		Vector2 v = (this.getPosition().sub(player.getPosition()));
		float radSq = VACUUM_RADIUS*VACUUM_RADIUS;
		if (v.len2() < radSq){ 
			player.setPosition(player.getPosition().add(v.nor().mul(MAX_PULL_SPEED*(float)Math.pow(1-v.len()/VACUUM_RADIUS,ALPHA)*delta)));			
		}
		if (countDown-- <= 0){
			poisonGas.setContinuous(true);
			countDown = COUNT_DOWN_MAX;
		} else poisonGas.setContinuous(false);
			
	}
	@Override
	public void draw(SpriteBatch spriteBatch, float delta){
		super.draw(spriteBatch,delta);
		poisonGas.draw(spriteBatch,delta);
	}
}
