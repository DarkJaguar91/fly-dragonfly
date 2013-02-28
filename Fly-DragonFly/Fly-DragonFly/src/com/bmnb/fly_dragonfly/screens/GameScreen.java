/**
 * 
 */
package com.bmnb.fly_dragonfly.screens;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.bmnb.fly_dragonfly.flocking.BoidsModel;
import com.bmnb.fly_dragonfly.graphics.GameParticleEmitter.Particle;
import com.bmnb.fly_dragonfly.graphics.ScrollingBackground;
import com.bmnb.fly_dragonfly.input.GameInput;
import com.bmnb.fly_dragonfly.objects.Enemy;
import com.bmnb.fly_dragonfly.objects.Frog;
import com.bmnb.fly_dragonfly.objects.GameObject;
import com.bmnb.fly_dragonfly.objects.Player;
import com.bmnb.fly_dragonfly.tools.CollisionDetector;

/**
 * Game screen controls the drawing update, everything for the game
 * 
 * @author Brandon James Talbot
 * 
 */
public class GameScreen implements Screen {

	/**
	 * Final static vars for global use
	 */
	public static final float width = 800, height = 1280, scrollSpeed = 200;

	/**
	 * global variables for class
	 */
	protected SpriteBatch batch;
	protected OrthographicCamera camera;
	protected Player player;
	protected ScrollingBackground scroller;
	protected BoidsModel mosquitoes;

	/**
	 * Static vars for static methods
	 */
	protected static ArrayList<GameObject> objects, particles, colisionObjects;
	protected static CollisionDetector collisionDetector;

	@Override
	public void show() {
		// setting up of major devices
		batch = new SpriteBatch();
		camera = new OrthographicCamera(width, height);
		camera.translate(width / 2f, height / 2f);
		camera.update();
		batch.setProjectionMatrix(camera.combined); // needs only be done once,
													// since camera does not
													// move
		// init the arrays
		objects = new ArrayList<GameObject>();
		particles = new ArrayList<GameObject>();
		colisionObjects = new ArrayList<GameObject>();

		// collision (col) detector init
		collisionDetector = new CollisionDetector(width, height);

		// init player
		addObject(player = new Player(new Vector2(width / 2f, 25), 50, 50, 300,
				width, height));

		// init the scroller
		scroller = new ScrollingBackground("data/space.jpg", width, height,
				scrollSpeed);

		// set the input processor
		Gdx.input.setInputProcessor(new GameInput(width, height, player));

		// debug
		addObject(new Frog(new Vector2(width / 2, height / 2), 50, 50, 0,
				width, height, player));
		addObject(new Frog(new Vector2(width / 2 - 50, height / 2), 50, 50, 0,
				width, height, player));
		addObject(new Frog(new Vector2(width / 2 + 50, height / 2), 50, 50, 0,
				width, height, player));
		addObject(new Frog(new Vector2(width / 2 - 100, height / 2), 50, 50, 0,
				width, height, player));
		addObject(new Frog(new Vector2(width / 2 + 100, height / 2), 50, 50, 0,
				width, height, player));

		// Add the flocking models:
		mosquitoes = new BoidsModel();
		// mosquitoes.spawnBoids(5, 5, width, height, 50); //DEBUG
	}

	@Override
	public void render(float delta) {
		// clear the screen
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		// draw everything
		batch.begin();

		// draw background
		scroller.draw(batch, delta);

		// draw objects
		for (int i = 0; i < objects.size(); ++i)
			objects.get(i).draw(batch, delta);
		mosquitoes.update(delta);

		batch.end();

		// do collision
		// collisionDetector.checkForCollision(objects);

		// debug
		for (int i = 0; i < colisionObjects.size(); ++i) {
			for (int a = 0; a < particles.size(); ++a) {
				if (!particles.get(a).isDead())
					if (colisionObjects.get(i).getBoundingRectangle()
							.overlaps(particles.get(a).getBoundingRectangle())) {
						((Enemy) colisionObjects.get(i)).doDamage(Player
								.getDamage());
						particles.get(a).kill();
					}
			}
		}

		// remove all dead opjects
		removeDeadObjects();
	}

	/**
	 * This removes all objects that need to be.
	 */
	protected void removeDeadObjects() {
		for (int i = 0; i < objects.size(); ++i) {
			if (objects.get(i).isRemovable()) {
				if (objects.get(i) instanceof Enemy) {
					colisionObjects.remove(objects.get(i));
				}
				// collisionDetector.deregisterFromGrid(objects.get(i));
				objects.remove(i);
				--i;
			}
		}
		for (int i = 0; i < particles.size(); ++i) {
			if (particles.get(i).isDead()) {
				// collisionDetector.deregisterFromGrid(particles.get(i));
				particles.remove(i);
				--i;
			}
		}
	}

	/**
	 * Add an object to the list
	 * 
	 * @param o
	 *            Object to add
	 */
	public static void addObject(GameObject o) {
		// collisionDetector.registerOnGrid(o);

		if (o instanceof Particle) {
			if (!particles.contains(o))
				particles.add(o);
		} else {
			if (o instanceof Enemy) {// || o instanceof Player){
				colisionObjects.add(o);
			}
			objects.add(o);
		}
	}

	/**
	 * moves the object within the grid
	 * 
	 * @param The
	 *            obejct to move
	 */
	public static void moveObject(GameObject o) {
		collisionDetector.registerOnGrid(o);
	}

	@Override
	public void resize(int width, int height) {
	}

	@Override
	public void hide() {
	}

	@Override
	public void pause() {
	}

	@Override
	public void resume() {
	}

	@Override
	public void dispose() {
		batch.dispose();
		objects = new ArrayList<GameObject>();
		particles = new ArrayList<GameObject>();
		colisionObjects = new ArrayList<GameObject>();
		collisionDetector = null;
	}
}
