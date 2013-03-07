/**
 * 
 */
package com.bmnb.fly_dragonfly.screens;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.bmnb.fly_dragonfly.flocking.BoidsModel;
import com.bmnb.fly_dragonfly.graphics.GameParticleEmitter.Particle;
import com.bmnb.fly_dragonfly.graphics.GameParticleEmitter.ParticleType;
import com.bmnb.fly_dragonfly.graphics.ScrollingBackground;
import com.bmnb.fly_dragonfly.input.GameInput;
import com.bmnb.fly_dragonfly.map.MapLoader;
import com.bmnb.fly_dragonfly.map.Spawner;
import com.bmnb.fly_dragonfly.objects.Bird;
import com.bmnb.fly_dragonfly.objects.Enemy;
import com.bmnb.fly_dragonfly.objects.Frog;
import com.bmnb.fly_dragonfly.objects.GameObject;
import com.bmnb.fly_dragonfly.objects.Player;
import com.bmnb.fly_dragonfly.objects.Spider;
import com.bmnb.fly_dragonfly.objects.VenusFlytrap;

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
	protected BoidsModel boidsmodel;
	protected MapLoader map;
	protected Spawner spawner;
	/**
	 * Static vars for static methods
	 */
	protected static ArrayList<GameObject> objects, fireParticles,
			poisonParticles, enemies, rocks;

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
		fireParticles = new ArrayList<GameObject>();
		poisonParticles = new ArrayList<GameObject>();
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
		/*addObject(new Frog(new Vector2(width / 2, height / 2), 50, 50, 0,
				width, height, player));
		addObject(new Frog(new Vector2(width / 2 - 50, height / 2), 50, 50, 0,
				width, height, player));
		addObject(new Frog(new Vector2(width / 2 + 50, height / 2), 50, 50, 0,
				width, height, player));
		addObject(new Frog(new Vector2(width / 2 - 100, height / 2), 50, 50, 0,
				width, height, player));
		addObject(new Frog(new Vector2(width / 2 + 100, height / 2), 50, 50, 0,
				width, height, player));
		addObject(new VenusFlytrap(new Vector2(width / 4, height / 4 * 3), 50,
				50, scrollSpeed, width, height, player));
		addObject(new Bird(new Vector2(width, height), 50, 50,
				scrollSpeed, width, height, player));
		addObject(new Spider(new Vector2(width, height), 50, 50,
				scrollSpeed, width, height, player));*/
		// Add the flocking models:
		boidsmodel = new BoidsModel();

		// huge debug
		((GameInput) Gdx.input.getInputProcessor()).setGameScreen(this);
		// Load map
		try {
			 this.map = new MapLoader("data/TestMap.xml");
			 this.spawner = new Spawner(map, boidsmodel, this, scrollSpeed, player);
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(1);
		}
	}

	public static ArrayList<GameObject> getEnemies() {
		return enemies;
	}

	@Override
	public void render(float delta) {
		// clear the screen
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		// draw everything
		batch.begin();

		// draw background
		//scroller.draw(batch, delta);

		// draw objects
		/*for (int i = 0; i < objects.size(); ++i)
			objects.get(i).draw(batch, delta);
		boidsmodel.update(delta,this);
		spawner.update(delta);
		//draw tutorial screens
		Texture tex = new Texture("tutorial_background.png");
		batch.draw(tex, 10, 10, width-10, height-10, 0, 0, tex.getWidth(), tex.getHeight(), false, false);		
		
		
		batch.end();

		// do collision
		doCollisionDetection();

		// remove all dead opjects

		removeDeadObjects();
	}
	
	public void showTutorialScreen(int y){
		
	}

	public Player getPlayer() {
		return player;
	}

	/**
	 * This removes all objects that need to be.
	 */
	protected void removeDeadObjects() {
		for (int i = 0; i < objects.size(); ++i) {
			if (objects.get(i).isRemovable()) {
				// enemies
				if (objects.get(i) instanceof Enemy)
					enemies.remove(objects.get(i));
				// objects
				objects.remove(i);
				--i;
			}
		}
		for (int i = 0; i < fireParticles.size(); ++i) {
			if (fireParticles.get(i).isDead()) {
				fireParticles.remove(i);
				--i;
			}
		}
		for (int i = 0; i < poisonParticles.size(); ++i) {
			if (poisonParticles.get(i).isDead()) {
				poisonParticles.remove(i);
				--i;
			}
		}
	}

	protected void doCollisionDetection() {
		// first enemies
		Rectangle particlesBox = getParticleRect();

		for (GameObject o : enemies) {
			if (!o.isDead())
				if (particlesBox != null)
					if (o.getBoundingRectangle().overlaps(particlesBox)) {
						for (GameObject p : fireParticles) {
							if (!p.isDead())
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
			if (((Particle) o).type == ParticleType.fire) {
				if (!fireParticles.contains(o))
					fireParticles.add(o);
			} else if (!poisonParticles.contains(o))
				poisonParticles.add(o);

		} else {
			if (o instanceof Enemy)
				enemies.add(o);
			
			if (objects.size() == 0)
				objects.add(o);
			else {
				boolean added = false;
				for (int i = 0; i < objects.size(); ++i){
					if (o.compareTo(objects.get(i)) < 0){
						objects.add(i, o);
						added = true;
						break;
					}
				}
				if (!added)
					objects.add(o);
			}
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
		for (GameObject o : fireParticles) {
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
		fireParticles = new ArrayList<GameObject>();
		enemies = new ArrayList<GameObject>();
		rocks = new ArrayList<GameObject>();
	}
}
