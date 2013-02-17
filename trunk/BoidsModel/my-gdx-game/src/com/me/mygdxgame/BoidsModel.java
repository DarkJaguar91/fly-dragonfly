package com.me.mygdxgame;
import java.util.Vector;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

/**
 * Flocking Model (Boids) with fleeing behavior
 * @author benjamin
 * 
 * Acknowledgments: 
 * I've builded this more advanced flocking model from pointers provided in the following blog:
 * Harry Brundage. Neat Algorithms - Flocking. Available at [http://harry.me/2011/02/17/neat-algorithms---flocking/]
 */
public class BoidsModel {
	private final float COHERE_RADIUS = 200;
	private final float ALIGN_RADIUS = 190;
	private final float SEPARATE_RADIUS = 25;
	private final float SEPARATION_SCALE = 0.5f;
	private final float MAX_SPEED = 35f;
	private final float MIN_SPEED = 25f;
	private final int NUM_ELEMENTS = 100;
	private final float DESTRUCTION_BUFFER = 150;
	private final float CREATION_BUFFER = 100;
	private final float WAIT_TIME_BEFORE_NEW_FLOCK = 20;
	private float countDownBeforeNewFlock = 0;
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
				double dist = distSq(b1.getPosition(),b2.getPosition());
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
				double dist = distSq(b1.getPosition(),b2.getPosition());
				if (dist < ALIGN_RADIUS * ALIGN_RADIUS){
					meanVel.add(b2.getVelocity());
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
				double dist = distSq(b1.getPosition(),b2.getPosition());
				if (dist < SEPARATE_RADIUS * SEPARATE_RADIUS){
					meanVel.add(b1.getPosition().cpy().sub(b2.getPosition().cpy().nor().div((float)Math.sqrt(dist)*SEPARATION_SCALE)));
					countInRadius++;
				}
			}
			if (countInRadius > 0){
				b1.getVelocity().add(meanVel.div(countInRadius).nor());
			}
		}
	}
	/**
	 * Method to make boids flee if they are in a specified radius from specified position
	 * @param pos
	 * @param radius
	 */
	private void fleeFromObject(Vector2 pos, float radius){
		double radSq = radius*radius;
		for (Boid b: elements){
			double dist = distSq(b.getPosition(),pos);
			if (dist < radSq)
				b.getVelocity().sub(pos.cpy().sub(b.getPosition()));
		}
	}
	/**
	 * Remove the boids that are off-screen
	 */
	private void removeBoidsOutsideBounds(){
		for(int i = 0; i < elements.size(); ++i){
			Boid b = elements.get(i);
			if (b.getPosition().x < -DESTRUCTION_BUFFER ||
				b.getPosition().y < -DESTRUCTION_BUFFER ||
				b.getPosition().x > Gdx.graphics.getWidth()+DESTRUCTION_BUFFER ||
				b.getPosition().x > Gdx.graphics.getHeight()+DESTRUCTION_BUFFER)
					elements.remove(i--);
		}
	}
	/**
	 * Method to add boids to the system on a timed interval 
	 * @param delta
	 */
	private void topupBoids(float delta){
		countDownBeforeNewFlock -= delta;
		if (countDownBeforeNewFlock <= 0)
			countDownBeforeNewFlock = WAIT_TIME_BEFORE_NEW_FLOCK;
		else
			return;
		for (int i = 0; i < NUM_ELEMENTS - elements.size(); ++i){
			float newspeed = (float)Math.random()*MAX_SPEED;
			newspeed = newspeed < MIN_SPEED ? MIN_SPEED : newspeed;
			Vector2 bPos = new Vector2((float)Math.random()*Gdx.graphics.getWidth(),Gdx.graphics.getHeight()+CREATION_BUFFER);
			elements.add(new Boid(bPos,
					(new Vector2(0,-1).mul(newspeed))));
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
	}
	/**
	 * Update method. Invoke this method to update boid positions.
	 * @param delta
	 */
	public void update(float delta){
		topupBoids(delta);
		//boid ops
		cohere(delta);
		align(delta);
		separate(delta);
		for (int i = 0; i < 5; ++i)
			if (Gdx.input.isTouched(i))
				fleeFromObject(new Vector2(Gdx.input.getX(i),Gdx.graphics.getHeight()-Gdx.input.getY(i)),100);
		//after all properties are added clamp the boid's speed:
		clampBoidSpeed();
		//finally update positions:
		for(Boid b:elements){
			b.setOldPosition(b.getPosition());
			b.getPosition().add(b.getVelocity().cpy().mul(delta));
		}
		//Now bound Boids to the viewport
		removeBoidsOutsideBounds();
	}
	/**
	 * Returns the squared distance between POINTS in homogeneous coords
	 * TODO: refactor: put in external class
	 * @param p1 First point
	 * @param p2 Second point
	 * @return Square of euclidian distance between the points
	 */
	public static double distSq(Vector2 p1, Vector2 p2){
		return (p1.x - p2.x)*(p1.x - p2.x) + (p1.y - p2.y)*(p1.y - p2.y);
	}
}
