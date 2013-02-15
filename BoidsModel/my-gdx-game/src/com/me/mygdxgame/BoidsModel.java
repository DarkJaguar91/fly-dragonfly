package com.me.mygdxgame;
import java.util.Vector;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;

/**
 * Basic Boids' Model (with aggressive behaviour)
 * @author benjamin
 */
public class BoidsModel {
	private final float COHERE_RADIUS = 50;
	private final float ALIGN_RADIUS = 45;
	private final float SEPARATE_RADIUS = 35;
	private final float AGGRESSIVE_RADIUS = 240;
	private final float MAX_SPEED = 15f;
	private final int NUM_ELEMENTS = 100;
	private final boolean INIT_IN_UNIFORM_DIST = true;
	private final boolean AGGRESSIVE_BEHAVIOUR = true;
	private final int NUMBER_OF_FINGERS_SUPPORTED = 8;
	public Vector<Boid> elements;
	/**
	 * Method to compute cohesion for each element (based on the cohesion radius)
	 * The method works based on the mean of the position within radius.
	 * @param delta
	 */
	private void cohere(float delta){
		//TODO optimize: use a grid to exploit locality
		for(int i = 0; i < elements.size() - 1; ++i){
			Boid b1 = elements.get(i);
			Vector2 meanPos = new Vector2();
			int countInRadius = 0;
			for (int j = i + 1; j < elements.size(); ++j){
				Boid b2 = elements.get(j);
				float dist = distSq(b1.getPosition(),b2.getPosition());
				if (dist < COHERE_RADIUS * COHERE_RADIUS){
					meanPos.add(b2.getPosition());
					countInRadius++;
				}
			}
			if (countInRadius > 0){
				b1.getVelocity().add(meanPos.div(countInRadius).sub(b1.getPosition()).nor());
			}
		}
	}
	/**
	 * Method to compute alignment for each element (based on the velocity, and thus direction, of neighbouring boids).
	 * These calculations are based on the alignment radius.
	 * @param delta
	 */
	private void align(float delta){
		//TODO optimize: use a grid to exploit locality
		for(int i = 0; i < elements.size() - 1; ++i){
			Boid b1 = elements.get(i);
			Vector2 meanVel = new Vector2();
			int countInRadius = 0;
			for (int j = i + 1; j < elements.size(); ++j){
				Boid b2 = elements.get(j);
				float dist = distSq(b1.getPosition(),b2.getPosition());
				if (dist < ALIGN_RADIUS * ALIGN_RADIUS){
					meanVel.add(b2.getPosition());
					countInRadius++;
				}
			}
			if (countInRadius > 0){
				b1.getVelocity().add(meanVel.div(countInRadius).nor());
			}
		}
	}
	/**
	 * Method to compute separation of boids that are very close together (think of repulsion).
	 * The calculations are based on the separation radius (simply a matter of adding a mean distance to the velocity)
	 * 
	 * @param delta
	 */
	private void separate(float delta){
		//TODO optimize: use a grid to exploit locality
		for(int i = 0; i < elements.size() - 1; ++i){
			Boid b1 = elements.get(i);
			Vector2 meanVel = new Vector2();
			int countInRadius = 0;
			for (int j = i + 1; j < elements.size(); ++j){
				Boid b2 = elements.get(j);
				float dist = distSq(b1.getPosition(),b2.getPosition());
				if (dist < SEPARATE_RADIUS * SEPARATE_RADIUS){
					meanVel.add(b1.getPosition().cpy().sub(b2.getPosition().cpy().nor().div((float)Math.sqrt(dist))));
					countInRadius++;
				}
			}
			if (countInRadius > 0){
				b1.getVelocity().add(meanVel.div(countInRadius).nor());
			}
		}
	}
	/**
	 * Method adding aggressive behaviour to the boids. They will attack the coordinates of the touched fingers.
	 * The boids simply move in the direction of the finger position(s). The behaviour also depends on the aggressive radius.
	 * @param delta
	 */
	private void aggression(float delta){
		for (Boid b: elements){
			for (int i = 0; i < NUMBER_OF_FINGERS_SUPPORTED; ++i)
				if (Gdx.input.isTouched(i)){
					float cx = Gdx.input.getX(i);
					float cy = Gdx.graphics.getHeight() - Gdx.input.getY(i);
					Vector2 finger = new Vector2(cx,cy);
					float dist = distSq(b.getPosition(),finger);
					if (dist < AGGRESSIVE_RADIUS*AGGRESSIVE_RADIUS)
						b.getVelocity().add(finger.sub(b.getPosition()));
				}
		}
	}
	/**
	 * Method to make sure the boids stay on screen (cyclic bounds)
	 */
	private void boundBoids(){
		for(Boid b: elements){
			if (b.getPosition().x < 0)
				b.getPosition().x = Gdx.graphics.getWidth();
			else if (b.getPosition().x > Gdx.graphics.getWidth())
				b.getPosition().x = 0;
			if (b.getPosition().y < 0)
				b.getPosition().y = Gdx.graphics.getHeight();
			else if (b.getPosition().y > Gdx.graphics.getHeight())
				b.getPosition().y = 0;
		}
	}
	/**
	 * Method to clamp boid speed (in both x and y components)
	 */
	private void clampBoidSpeed(){
		for(Boid b: elements){
			b.getVelocity().x = Math.abs(b.getVelocity().x) > MAX_SPEED ? Math.signum(b.getVelocity().x)*MAX_SPEED : b.getVelocity().x;
			b.getVelocity().y = Math.abs(b.getVelocity().y) > MAX_SPEED ? Math.signum(b.getVelocity().y)*MAX_SPEED : b.getVelocity().y;
		}
	}
	/**
	 * Initializer. Can init boids uniformly or starting in a circle in the middle of the screen
	 */
	public BoidsModel(){
		elements = new Vector<Boid>(NUM_ELEMENTS);
		for (int i = 0; i < NUM_ELEMENTS; ++i){
			
			if (!INIT_IN_UNIFORM_DIST){
				float cdir = (float)Math.cos(Math.random()*2*Math.PI);
				float sdir = (float)Math.sin(Math.random()*2*Math.PI);
				elements.add(new Boid(new Vector2(cdir,sdir).nor().mul(50).add(Gdx.graphics.getWidth()/2,Gdx.graphics.getHeight()/2),
					new Vector2((float)Math.random()*MAX_SPEED,(float)Math.random()*MAX_SPEED)));
			}
			else{
				elements.add(new Boid(new Vector2((float)Math.random(),(float)Math.random()).mul(Gdx.graphics.getWidth(),Gdx.graphics.getHeight()),
						new Vector2((float)Math.random()*MAX_SPEED,(float)Math.random()*MAX_SPEED)));
			}
		}
	}
	/**
	 * Update method. Invoke this method to update boid positions.
	 * @param delta
	 */
	public void update(float delta){
		if (AGGRESSIVE_BEHAVIOUR)
			aggression(delta);
		cohere(delta);
		align(delta);
		separate(delta);
		//after all properties are added clamp the boid's speed:
		clampBoidSpeed();
		//finally update positions:
		for(Boid b:elements){
			b.setOldPosition(b.getPosition());
			b.getPosition().add(b.getVelocity().cpy().mul(delta));
		}
		//Now bound Boids to the viewport
		boundBoids();
	}
	/**
	 * Returns the squared distance between POINTS in homogenious coords
	 * TODO: refactor: put in external class
	 * @param p1 First point
	 * @param p2 Second point
	 * @return Square of euclidian distance between the points
	 */
	public static float distSq(Vector2 p1, Vector2 p2){
		return (p1.x - p2.x)*(p1.x - p2.x) + (p1.y - p2.y)*(p1.y - p2.y);
	}
}
