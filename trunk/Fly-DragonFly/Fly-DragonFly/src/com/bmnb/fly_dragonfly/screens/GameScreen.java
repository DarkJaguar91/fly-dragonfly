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
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.bmnb.fly_dragonfly.flocking.BoidsModel;
import com.bmnb.fly_dragonfly.graphics.GameParticleEmitter.Particle;
import com.bmnb.fly_dragonfly.graphics.ScrollingBackground;
import com.bmnb.fly_dragonfly.input.GameInput;
import com.bmnb.fly_dragonfly.objects.Enemy;
import com.bmnb.fly_dragonfly.objects.Frog;
import com.bmnb.fly_dragonfly.objects.GameObject;
import com.bmnb.fly_dragonfly.objects.Player;

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
	protected static ArrayList<GameObject> objects, particles, enemies, rocks;

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
		enemies = new ArrayList<GameObject>();
		rocks = new ArrayList<GameObject>();

		// init player
		addObject(player = new Player(new Vector2(width / 2f, 50), 100, 100,
				300, width, height));

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
		mosquitoes.spawnBoids(32, 32, width, height, 10); //DEBUG
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
		doCollisionDetection();

		// remove all dead opjects

		removeDeadObjects();
	}

	/**
	 * This removes all objects that need to be.
	 */
	protected void removeDeadObjects() {
		for (int i = 0; i < objects.size(); ++i) {
			if (objects.get(i).isRemovable()) {
				if (objects.get(i) instanceof Enemy)
					enemies.remove(objects.get(i));
				objects.remove(i);
				--i;
			}
		}
		for (int i = 0; i < particles.size(); ++i) {
			if (particles.get(i).isDead()) {
				particles.remove(i);
				--i;
			}
		}
	}

	protected void doCollisionDetection() {
		// first enemies
		Rectangle particlesBox = getParticleRect();

		for (GameObject o : enemies) {
			if (particlesBox != null)
				if (o.getBoundingRectangle().overlaps(particlesBox)) {
					for (GameObject p : particles) {
						if (p.getBoundingRectangle().overlaps(
								o.getBoundingRectangle())) {
							((Particle) p).kill();
							((Enemy) o).doDamage(player.getDamage());
						}
					}
				}

			// do for player with checks
		}

		// add player check with boids

	}

	/**
	 * Add an object to the list
	 * 
	 * @param o
	 *            Object to add
	 */
	public static void addObject(GameObject o) {
		if (o instanceof Particle) {
			if (!particles.contains(o))
				particles.add(o);
		} else {
			if (o instanceof Enemy)
				enemies.add(o);
			objects.add(o);
		}
	}

	/**
	 * Calculates a Bounding rectangle for all the particles in order to keep
	 * efficiency
	 * 
	 * @return The bounding rectangle for all the particles
	 */
	protected Rectangle getParticleRect() {
		int xmin = Integer.MAX_VALUE, ymin = Integer.MAX_VALUE;
		int xmax = Integer.MIN_VALUE, ymax = Integer.MIN_VALUE;
		for (GameObject o : particles) {
			xmin = (int) Math.min(xmin, o.getX() - o.getWidth());
			ymin = (int) Math.min(ymin, o.getY() - o.getHeight());
			xmax = (int) Math.min(xmax, o.getX() + o.getWidth());
			ymax = (int) Math.min(ymax, o.getY() + o.getHeight());
		}

		if (xmin == Integer.MAX_VALUE || ymin == Integer.MAX_VALUE)
			return null;
		else
			return new Rectangle(xmin, ymin, Math.abs(xmax - xmin),
					Math.abs(ymax - ymin));
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
		enemies = new ArrayList<GameObject>();
		rocks = new ArrayList<GameObject>();
	}
}
