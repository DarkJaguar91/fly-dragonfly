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
import com.bmnb.fly_dragonfly.graphics.GameParticleEmitter.Particle;
import com.bmnb.fly_dragonfly.graphics.ScrollingBackground;
import com.bmnb.fly_dragonfly.input.GameInput;
import com.bmnb.fly_dragonfly.objects.Enemy;
import com.bmnb.fly_dragonfly.objects.Frog;
import com.bmnb.fly_dragonfly.objects.GameObject;
import com.bmnb.fly_dragonfly.objects.Player;

/**
 * Game screen controls the drawing update, everything for the game
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
	
	/**
	 * Static vars for static methods
	 */
	protected static ArrayList<GameObject> objects, particles;
	
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
		
		// init player
		addObject(player = new Player(new Vector2(width / 2f, 25), 50, 50, 300, width, height));
	
		// init the scroller
		scroller = new ScrollingBackground("data/space.jpg", width, height, scrollSpeed);
		
		// set the input processor
		Gdx.input.setInputProcessor(new GameInput(width, height, player));
		
		// debug
		addObject(new Frog(new Vector2(width /2, height /2), 50, 50, 0, width, height));
	}

	@Override
	public void render(float delta) {
		// clear the screen
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		// draw everything
		batch.begin();
		
		//draw background
		scroller.draw(batch, delta);
		
		//draw objects
		for (int i = 0; i < objects.size(); ++i)
			objects.get(i).draw(batch, delta);
		
		batch.end();
		
		for (int i = 0; i < particles.size(); ++i){
			for (int a = 0; a < objects.size(); ++a){
				if (objects.get(a) instanceof Enemy){
					if (particles.get(i).getBoundingRectangle().overlaps(objects.get(a).getBoundingRectangle())){
						particles.get(i).kill();
						((Enemy)objects.get(a)).doDamage(player.getDamage());
					}
				}
			}
		}
		
		// remove all dead opjects
		removeDeadObjects();
	}
	
	protected void removeDeadObjects (){
		for (int i = 0; i < objects.size(); ++i){
			if (objects.get(i).isRemovable()){
				objects.remove(i);
				--i;
			}
		}
		for (int i = 0; i < particles.size(); ++i){
			if (particles.get(i).isDead()){
				particles.remove(i);
				--i;
			}
		}	
	}

	/**
	 * Add an object to the list
	 * @param o Object to add
	 */
	public static void addObject(GameObject o){
		if (o instanceof Particle){
			particles.add(o);
		}
		else {
			objects.add(o);
		}
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
	}
}
