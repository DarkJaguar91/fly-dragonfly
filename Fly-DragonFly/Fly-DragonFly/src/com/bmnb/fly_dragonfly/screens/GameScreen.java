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
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.bmnb.fly_dragonfly.flocking.Boid;
import com.bmnb.fly_dragonfly.flocking.BoidsModel;
import com.bmnb.fly_dragonfly.flocking.FireFly;
import com.bmnb.fly_dragonfly.graphics.GameParticleEmitter.Particle;
import com.bmnb.fly_dragonfly.graphics.GameParticleEmitter.ParticleType;
import com.bmnb.fly_dragonfly.graphics.Meter;
import com.bmnb.fly_dragonfly.graphics.ScrollingBackground;
import com.bmnb.fly_dragonfly.input.GameInput;
import com.bmnb.fly_dragonfly.map.MapLoader;
import com.bmnb.fly_dragonfly.map.Spawner;
import com.bmnb.fly_dragonfly.objects.Enemy;
import com.bmnb.fly_dragonfly.objects.GameObject;
import com.bmnb.fly_dragonfly.objects.Player;
import com.bmnb.fly_dragonfly.objects.Tongue;

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
	protected Meter manaMeter;
	protected BitmapFont font;
	
	protected Texture tutorial_tex;
	protected Texture okBtnTex;
	protected Texture tutFrogTex;
	protected Texture tutSpiderTex;
	protected Texture tutFlytrapTex;
	protected Texture tutbirdTex;
	protected Texture tutFlameTex;
	protected Texture tutFlyMoziTex;
	protected Texture livesTex;
	protected boolean draw_tutorial;
	protected int tutID;
	
	/**
	 * Static vars for static methods
	 */
	protected static ArrayList<GameObject> objects, fireParticles,
			poisonParticles, enemies, rocks, boids;

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
		boids = new ArrayList<GameObject>();

		// init player
		addObject(player = new Player(new Vector2(width / 2f, 50), 100, 100,
				300, width, height));

		// init the scroller
		scroller = new ScrollingBackground("data/space.jpg", width, height,
				scrollSpeed);
		
		//init tutorial graphics
		tutorial_tex = new Texture("data/tutorials/map.png");
		tutorial_tex.setFilter(TextureFilter.Linear,TextureFilter.Linear);
		okBtnTex = new Texture("data/tutorials/tutOkBtn2.png");
		okBtnTex.setFilter(TextureFilter.Linear,TextureFilter.Linear);
		
		tutFrogTex = new Texture("data/tutorials/tutFrog.png");
		tutSpiderTex = new Texture("data/tutorials/tutSpider.png");
		tutFlytrapTex = new Texture("data/tutorials/tutFlyTrap.png");
		tutbirdTex = new Texture("data/tutorials/tutBird.png");
		tutFlameTex = new Texture("data/tutorials/tutFlame.png");
		tutFlyMoziTex = new Texture("data/tutorials/tutFlyMozi.png");
		
		livesTex = new Texture("data/tutorials/lives.png");
		draw_tutorial = false;
		tutID = 0;
		
		// set the input processor
		Gdx.input.setInputProcessor(new GameInput(width, height, player));
		((GameInput)Gdx.input.getInputProcessor()).setGameScreen(this);//TODO

		// Add the flocking models:
		boidsmodel = new BoidsModel();
		
		// Load map
		try {
			 this.map = new MapLoader("data/TestMap.xml");
			 this.spawner = new Spawner(map, boidsmodel, this, scrollSpeed, player);
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(1);
		}
		
		//Init mana meter
		manaMeter = new Meter(new Vector2(width/6,height-height/30), width/3, height/15, width, height, false, 
				new Texture("data/mana_meter.png"), new Texture("data/mana_meter_grey.png"), 1);
		
		//Load font
		font = new BitmapFont(Gdx.files.internal("data/font/commicsans.fnt"),
		         Gdx.files.internal("data/font/commicsans.png"), false);
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
		scroller.draw(batch, delta);

		// draw objects
		for (int i = 0; i < objects.size(); ++i)
			objects.get(i).draw(batch, delta);
		
		//draw score TODO
		
		//draw lives
		for(int i=1;i<=player.getNumLives();i++){
			batch.draw(livesTex,width-(livesTex.getWidth()-10)*i,height-livesTex.getHeight()-20,50,50,0,0,
					livesTex.getWidth(),livesTex.getHeight(),false,false);
		}
		
		
		//draw tutorial screen
		if(draw_tutorial){
			drawTutorialScreen(batch);
		}
		else{
			scroller.update(delta);
			
			//update objects
			for (int i = 0; i < objects.size(); ++i)
				objects.get(i).update(delta);
			
			boidsmodel.update(delta,this);
			spawner.update(delta);
			
			CharSequence s = "Hello World";
			font.draw(batch, s, 0, height - 450);
		}	
		
		manaMeter.setProgress(player.getMana()/player.getMaxMana());
		manaMeter.draw(batch,delta);	
		
		batch.end();

		// do collision
		doCollisionDetection();

		// remove all dead opjects

		removeDeadObjects();
	}
	
	//shows pop-up tutorial screen 
	public void showTutorialScreen(int id){
		if(draw_tutorial){
			draw_tutorial = false;
			tutID = 0;
		}
		else{
			draw_tutorial = true;
			tutID = id;
		}
	}

	//draw tutorial screen
	private void drawTutorialScreen(SpriteBatch spritebatch){
		float tutScreenStartX = 30;
		float tutScreenStartY = height/3;		
		float tutScreenWidth = width-70;
		float tutScreenHeight = 500;		

		spritebatch.draw(tutorial_tex, tutScreenStartX, tutScreenStartY, tutScreenWidth, tutScreenHeight, 0, 0, 
				tutorial_tex.getWidth(), tutorial_tex.getHeight(), false, false);

		spritebatch.draw(okBtnTex, ((tutScreenStartX+tutScreenWidth)/2)-50, 
				tutScreenStartY + 70, 100, 50, 0, 0, 
				okBtnTex.getWidth(), okBtnTex.getHeight(), false, false);

		switch(tutID){
		case 0:
			spritebatch.draw(tutFrogTex, tutScreenStartX+20, 
					(tutScreenStartY+tutScreenHeight) - tutFrogTex.getHeight() - 20, 
					tutFrogTex.getWidth(), 
					tutFrogTex.getHeight(), 0, 0, 
					tutFrogTex.getWidth(), tutFrogTex.getHeight(), false, false);
			//				spritebatch.
			break;
		case 2:
			spritebatch.draw(tutFlytrapTex, tutScreenStartX+20, 
					(tutScreenStartY+tutScreenHeight) - tutFlytrapTex.getHeight() - 20, 
					tutFlytrapTex.getWidth(), 
					tutFlytrapTex.getHeight(), 0, 0, 
					tutFlytrapTex.getWidth(), tutFlytrapTex.getHeight(), false, false);
			break;
		case 3:
			spritebatch.draw(tutSpiderTex, tutScreenStartX+20, 
					(tutScreenStartY+tutScreenHeight) - tutSpiderTex.getHeight() - 20, 
					tutSpiderTex.getWidth(), 
					tutSpiderTex.getHeight(), 0, 0, 
					tutSpiderTex.getWidth(), tutSpiderTex.getHeight(), false, false);
			break;
		case 4:
			spritebatch.draw(tutbirdTex, tutScreenStartX+20, 
					(tutScreenStartY+tutScreenHeight) - tutbirdTex.getHeight() - 20, 
					tutbirdTex.getWidth(), 
					tutbirdTex.getHeight(), 0, 0, 
					tutbirdTex.getWidth(), tutbirdTex.getHeight(), false, false);
			break;
		case 5:
			spritebatch.draw(tutFlyMoziTex, tutScreenStartX+20, 
					(tutScreenStartY+tutScreenHeight) - tutFlyMoziTex.getHeight() - 20, 
					tutFlyMoziTex.getWidth(), 
					tutFlyMoziTex.getHeight(), 0, 0, 
					tutFlyMoziTex.getWidth(), tutFlyMoziTex.getHeight(), false, false);
			break;
		default:
			//do nothing
		}		
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
				
				//boids
				if (objects.get(i) instanceof Boid)
					boids.remove(objects.get(i));
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
									if (o instanceof Tongue){
										if (((Tongue)o).checkCollision(p.getBoundingRectangle())){
											((Particle) p).kill();
											((Enemy) o).doDamage(player.getDamage());
										}
									}
									else {
										((Particle) p).kill();
										((Enemy) o).doDamage(player.getDamage());
									}
								}
						}
					}
			// do for player with checks
			if (! o.isDead()){
				
				if (o.getBoundingRectangle().overlaps(player.getBoundingRectangle())){
					if (o instanceof Tongue){
						if (((Tongue)o).checkCollision(player.getBoundingRectangle())){
							player.playerHitAnimation();
						}
					}
					else{
						player.playerHitAnimation();
					}
				}
			}
		}

		// add player check with boids
		for (GameObject o : boids){
			if (! o.isDead()){
				if (o.getBoundingRectangle().overlaps(player.getBoundingRectangle())){
					o.kill();
					if (o instanceof FireFly)
						player.convertWeaponFireflies();
					else
						player.convertWeaponMossies();
				}
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
		if (o instanceof Particle) {
			if (((Particle) o).type == ParticleType.fire) {
				if (!fireParticles.contains(o))
					fireParticles.add(o);
			} else if (!poisonParticles.contains(o))
				poisonParticles.add(o);

		} else {
			if (o instanceof Enemy)
				enemies.add(o);
				
			if (o instanceof Boid)
				boids.add(o);
			
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
		boids = new ArrayList<GameObject>();
		
		tutbirdTex.dispose();
		tutFrogTex.dispose();
		tutSpiderTex.dispose();
		tutFlyMoziTex.dispose();
		tutFlytrapTex.dispose();
		tutFlameTex.dispose();
		livesTex.dispose();
		tutbirdTex.dispose();
		okBtnTex.dispose();
	}
}
