package com.bmnb.fly_dragonfly.objects;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

public class Tongue extends StaticEnemy{

	
	public static final float growRate = 0.1f;//percentage
	protected float length;
	protected Vector2 target;
	protected boolean grow = true;
	
	
	public Tongue(Vector2 position, float width, float height, float speed,
			float scWidth, float scHeight, Player player) {
		super(position, width, height, speed, scWidth, scHeight, player);
		
		sortVal = 3;
		
		target = player.getPosition().cpy().add(player.direction.cpy().mul(player.speed * (1 / growRate))); // calculate pos further away
		
		length = target.cpy().sub(this.getPosition()).len();

		float angle = (target.cpy().sub(this.getPosition().cpy())).angle();
		
		//this.setRotation(angle);
		
		this.setTexture(new Texture("data/square.png"));
	}

	/* (non-Javadoc)
	 * @see com.bmnb.fly_dragonfly.objects.GameObject#update(float)
	 */
	@Override
	public void update(float delta) {
//		if (this.getHeight() >= length){
//			grow = false;
//		}
//		
//		this.setSize(this.getWidth(), this.getHeight() + (grow ? length * growRate : - growRate * length));
		//Gdx.app.log("FrogTongue", "Growing - " + this.getHeight());
		
		
		super.update(delta);
	}

	/* (non-Javadoc)
	 * @see com.bmnb.fly_dragonfly.objects.StaticEnemy#move(float)
	 */
	@Override
	protected void move(float delta) {
		this.target = target.cpy().sub(new Vector2(0, delta * speed));
		super.move(delta);
	}
	
}
