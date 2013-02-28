package com.bmnb.fly_dragonfly.flocking;
import java.util.Vector;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.bmnb.fly_dragonfly.screens.GameScreen;

/**
 * Flocking Model (Boids) with fleeing behavior
 * @author benjamin
 * 
 * Acknowledgments: 
 * I've builded this more advanced flocking model from pointers provided in the following blog:
 * Harry Brundage. Neat Algorithms - Flocking. Available at [http://harry.me/2011/02/17/neat-algorithms---flocking/]
 */
public class BoidsModel {
	private final float COHERE_RADIUS = 600;
	private final float ALIGN_RADIUS = 590;
	private final float SEPARATE_RADIUS = 5;
	private final float SEPARATION_SCALE = 200f;
	private final int NUM_ELEMENTS = 100;
	private final float DESTRUCTION_BUFFER = 150;
	private final float CREATION_BUFFER = 20;
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
				b1.setVelocity(b1.getVelocity().add(meanPos.div(countInRadius).sub(b1.getPosition())));
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
				b1.setVelocity(b1.getVelocity().add(meanVel.div(countInRadius).nor()));
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
					meanVel.add(b1.getPosition().cpy().sub(b2.getPosition()).nor().div((float)Math.sqrt(dist+0.000000001f)*SEPARATION_SCALE));
					countInRadius++;
				}
			}
			if (countInRadius > 0){
				b1.setVelocity(b1.getVelocity().add(meanVel.div(countInRadius).nor()));
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
				b.setVelocity(b.getVelocity().sub(pos.cpy().sub(b.getPosition())));
		}
	}
	private void flee(){
		fleeFromObject(new Vector2(Gdx.input.getX(0),Gdx.graphics.getHeight()-Gdx.input.getY(0)),70);
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
				b.getPosition().y > Gdx.graphics.getHeight()+DESTRUCTION_BUFFER){
					elements.remove(i--);
					b.kill();
			}
		}
	}
	/**
	 * Method to add boids to the system on a timed interval 
	 * @param delta
	 */
	public void spawnBoids(float widthPerBoid,float heightPerBoid,float scWidth,float scHeight, int numBoids){
		for (int i = 0; i < numBoids; ++i){
			Vector2 bPos = new Vector2((float)Math.random()*scWidth,scHeight+CREATION_BUFFER);
			Boid b = new Boid(bPos,new Vector2(0,-1),widthPerBoid,heightPerBoid,scWidth,scHeight);
			elements.add(b);
			GameScreen.addObject(b);
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
		//boid ops
		cohere(delta);
		align(delta);
		separate(delta);
		flee();
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