/**
 * 
 */
package com.bmnb.fly_dragonfly.screens;

import java.util.ArrayList;

import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenEquations;
import aurelienribon.tweenengine.TweenManager;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.bmnb.fly_dragonfly.flocking.Boid;
import com.bmnb.fly_dragonfly.flocking.BoidsModel;
import com.bmnb.fly_dragonfly.flocking.FireFly;
import com.bmnb.fly_dragonfly.graphics.GameParticleEmitter.Particle;
import com.bmnb.fly_dragonfly.graphics.GameParticleEmitter.ParticleType;
import com.bmnb.fly_dragonfly.graphics.Meter;
import com.bmnb.fly_dragonfly.graphics.ScrollingBackground;
import com.bmnb.fly_dragonfly.graphics.flashAnim;
import com.bmnb.fly_dragonfly.input.GameInput;
import com.bmnb.fly_dragonfly.map.MapLoader;
import com.bmnb.fly_dragonfly.map.Spawner;
import com.bmnb.fly_dragonfly.objects.Enemy;
import com.bmnb.fly_dragonfly.objects.GameObject;
import com.bmnb.fly_dragonfly.objects.Player;
import com.bmnb.fly_dragonfly.objects.Tongue;
import com.bmnb.fly_dragonfly.objects.Web;
import com.bmnb.fly_dragonfly.sound.MediaPlayer;

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
	protected Texture tutTextTex;
	protected boolean draw_tutorial;
	protected int tutID;
	protected CharSequence playerScore;
	protected float survivalTime;
	
	protected static Sprite flashSprite;
	protected static TweenManager flashman;
	
	private FrameBuffer m_fbo = null;
	private TextureRegion m_fboRegion = null;
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

		// init flash sprite
		flashSprite = new Sprite(new Texture("data/textures/flash.png"));
		flashSprite.setPosition(0, 0);
		flashSprite.setSize(width, height);
		flashSprite.setColor(1,1,1,0);
		flashman = new TweenManager();
		
		// init player
		addObject(player = new Player(new Vector2(width / 2f, 50), 150, 150,
				500, width, height));

		// init the scroller
		scroller = new ScrollingBackground(new String[]{"data/backgrounds/bg_final_flat_1.png", "data/backgrounds/bg_final_flat_2.png", "data/backgrounds/bg_final_flat_3.png"}, width, height,
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
		tutTextTex = new Texture("data/tutorials/tutOkBtn2.png");
		
		livesTex = new Texture("data/tutorials/lives.png");
		draw_tutorial = false;
		tutID = 0;
		survivalTime = 0;//TODO
		
		// set the input processor
		Gdx.input.setInputProcessor(new GameInput(width, height, player));
		((GameInput)Gdx.input.getInputProcessor()).setGameScreen(this);//TODO

		// Add the flocking models:
		boidsmodel = new BoidsModel();
		
		// Load map
		try {
			 this.map = new MapLoader("data/maps/map.xml");
			 this.spawner = new Spawner(map, boidsmodel, this, scrollSpeed, player);
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(1);
		}
		
		//Init mana meter
		manaMeter = new Meter(new Vector2(width/6,height-height/30), width/3, height/15, width, height, false, 
				new Texture("data/textures/mana_meter.png"), new Texture("data/textures/mana_meter_grey.png"), 1, player);
		
		//Load font
		font = new BitmapFont(Gdx.files.internal("data/font/commicsans.fnt"),
		         Gdx.files.internal("data/font/commicsans.png"), false);
		
		MediaPlayer.loadMusic("data/sound/background.mp3");
		MediaPlayer.playMusic("data/sound/background.mp3",true);
		MediaPlayer.setMusicVolume("data/sound/background.mp3", 0.02f);
	}

	/**
	 * Returns the enemies array
	 * @return The array of enemies
	 */
	public static ArrayList<GameObject> getEnemies() {
		return enemies;
	}

	/**
	 * Flashes the screen.
	 */
	public static void flash(){
		Tween.registerAccessor(Sprite.class, new flashAnim());

		Tween.to(flashSprite, flashAnim.ALPHA, 0.7f).target(1)
				.repeatYoyo(3, 0f).ease(TweenEquations.easeInCirc)
				.start(flashman);
	}
	
	@Override
	public void render(float delta) {
		// clear the screen
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);                  
		if(m_fbo == null)
		{
			// m_fboScaler increase or decrease the antialiasing quality

			m_fbo = new FrameBuffer(Format.RGBA4444, (int)(width), (int)(height), false);
			m_fboRegion = new TextureRegion(m_fbo.getColorBufferTexture());
			m_fboRegion.flip(false, true);
		}

	    m_fbo.begin();
	    Gdx.gl.glClearColor(0, 0, 0, 0);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
	    // draw everything
		batch.begin();
		// draw objects
		for (int i = 0; i < objects.size(); ++i)
				objects.get(i).draw(batch, delta);
		
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
			
			//draw score 
			playerScore = ""+player.getScore();
			font.draw(batch, playerScore, 10, height-livesTex.getHeight()-50);		
			player.increaseScoreBy(delta);
			
			//update objects
			for (int i = 0; i < objects.size(); ++i)
				objects.get(i).update(delta);
			
			boidsmodel.update(delta,this);
			spawner.update(delta);
			flashman.update(delta);
		}	

		manaMeter.setProgress(player.getMana()/player.getMaxMana());
		manaMeter.draw(batch,delta);	
		
		batch.end();
		m_fbo.end();
		
		batch.begin();
		// draw background
		scroller.draw(batch, delta);
		batch.draw(m_fboRegion,0,0,width,height);
		flashSprite.draw(batch);
		batch.end();
		// do collision
		doCollisionDetection();

		// remove all dead objects
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
	public boolean isShowingTutorialScreen(){
		return draw_tutorial;
	}
	//draw tutorial screen TODO
	private void drawTutorialScreen(SpriteBatch spritebatch){
		float tutScreenStartX = 30;
		float tutScreenStartY = height/3;		
		float tutScreenWidth = width-70;
		float tutScreenHeight = 500;	
		CharSequence msg = "";

		spritebatch.draw(tutorial_tex, tutScreenStartX, tutScreenStartY, tutScreenWidth, tutScreenHeight, 0, 0, 
				tutorial_tex.getWidth(), tutorial_tex.getHeight(), false, false);

		spritebatch.draw(okBtnTex, ((tutScreenStartX+tutScreenWidth)/2)-50, 
				tutScreenStartY + 70, 100, 50, 0, 0, 
				okBtnTex.getWidth(), okBtnTex.getHeight(), false, false);
		
		spritebatch.draw(tutTextTex, tutScreenStartX+20+tutFrogTex.getWidth()+5, 
				(tutScreenStartY+130), tutScreenWidth-tutFrogTex.getWidth()-50, 
				tutScreenWidth-okBtnTex.getWidth()-130, 0, 0, 
				tutTextTex.getWidth(), tutTextTex.getHeight(), false, false);
		font.setScale(0.6f);	
		font.setColor(Color.BLACK);
		switch(tutID){
		case 1:			
			spritebatch.draw(tutFrogTex, tutScreenStartX+20, 
		
					(tutScreenStartY+tutScreenHeight) - tutFrogTex.getHeight() - 20, 
					tutFrogTex.getWidth(), tutFrogTex.getHeight(), 0, 0, 
					tutFrogTex.getWidth(), tutFrogTex.getHeight(), false, false);			
									
			msg = "Beware of his tongue! All frogs are stationary but they " +
					"don't need to move to catch their prey...cause they have long, " +
					"fast tongues. Try keep a good distance away from them.";			
			font.drawWrapped(spritebatch, msg, tutScreenStartX+20+tutFrogTex.getWidth()+7,
					(tutScreenStartY+tutScreenHeight) - 30,
					tutScreenWidth-tutFrogTex.getWidth()-50);
					
			break;
		case 2:
			spritebatch.draw(tutFlytrapTex, tutScreenStartX+20, 
					(tutScreenStartY+tutScreenHeight) - tutFlytrapTex.getHeight() - 20, 
					tutFlytrapTex.getWidth(), 
					tutFlytrapTex.getHeight(), 0, 0, 
					tutFlytrapTex.getWidth(), tutFlytrapTex.getHeight(), false, false);	
			msg = "Watch out for the gas! FlyTraps are stationary but they secrete " +
					"gas which draws you towards them. And if you get too close, " +
					"then you will die.";			
			font.drawWrapped(spritebatch, msg, tutScreenStartX+20+tutFrogTex.getWidth()+7,
					(tutScreenStartY+tutScreenHeight) - 30,
					tutScreenWidth-tutFrogTex.getWidth()-50);
			break;
		case 3:
			spritebatch.draw(tutSpiderTex, tutScreenStartX+20, 
					(tutScreenStartY+tutScreenHeight) - tutSpiderTex.getHeight() - 20, 
					tutSpiderTex.getWidth(), 
					tutSpiderTex.getHeight(), 0, 0, 
					tutSpiderTex.getWidth(), tutSpiderTex.getHeight(), false, false);	
			msg = "Beware their webs, if you get caught they can come and eat " +
					"you unless you manage to break free in time! " +
					"So try and save some fire-breath to escape!";			
			font.drawWrapped(spritebatch, msg, tutScreenStartX+20+tutFrogTex.getWidth()+7,
					(tutScreenStartY+tutScreenHeight) - 30,
					tutScreenWidth-tutFrogTex.getWidth()-50);
			break;
		case 4:
			spritebatch.draw(tutbirdTex, tutScreenStartX+20, 
					(tutScreenStartY+tutScreenHeight) - tutbirdTex.getHeight() - 20, 
					tutbirdTex.getWidth(), 
					tutbirdTex.getHeight(), 0, 0, 
					tutbirdTex.getWidth(), tutbirdTex.getHeight(), false, false);
			msg = "Birds are fast and accurate. If you are not ready for them they will catch you. " +
					"They are waiting for you to pass by, " +
					"at which point they will swoop down and try eat you! " +
					"If you don't fly away, they will kill you.";			
			font.drawWrapped(spritebatch, msg, tutScreenStartX+20+tutFrogTex.getWidth()+7,
					(tutScreenStartY+tutScreenHeight) - 30,
					tutScreenWidth-tutFrogTex.getWidth()-50);
			break;
		case 5:
			spritebatch.draw(tutFlyMoziTex, tutScreenStartX+20, 
					(tutScreenStartY+tutScreenHeight) - tutFlyMoziTex.getHeight() - 20, 
					tutFlyMoziTex.getWidth(), 
					tutFlyMoziTex.getHeight(), 0, 0, 
					tutFlyMoziTex.getWidth(), tutFlyMoziTex.getHeight(), false, false);	
			msg = "You can eat Mosquitoes and flies to change your fire-breath and get points."+
					"Mosquitoes will increase your flame's breadth and how much damage it causes."+ 
					"Flies will increase your flame's range but decrease the damage done";		
			font.drawWrapped(spritebatch, msg, tutScreenStartX+20+tutFrogTex.getWidth()+7,
					(tutScreenStartY+tutScreenHeight) - 30,
					tutScreenWidth-tutFrogTex.getWidth()-50);
			break;
		case 6:
			spritebatch.draw(tutFlameTex, tutScreenStartX+20, 
					(tutScreenStartY+tutScreenHeight) - tutFlameTex.getHeight() - 20, 
					tutFlameTex.getWidth(), 
					tutFlameTex.getHeight(), 0, 0, 
					tutFlameTex.getWidth(), tutFlameTex.getHeight(), false, false);	
			msg = "You can use your flame to kill enemies and traps which are trying to kill you." +
					"Tap/Press in the bottom right hand corner of the screen to breath your flame." +
					"But you have give yourself time to catch your breath, so plan ahead!";			
			font.drawWrapped(spritebatch, msg, tutScreenStartX+20+tutFrogTex.getWidth()+7,
					(tutScreenStartY+tutScreenHeight) - 30,
					tutScreenWidth-tutFrogTex.getWidth()-50);
			break;
		case 0:
			//win condition
			spritebatch.draw(livesTex, tutScreenStartX+20, 
					(tutScreenStartY+tutScreenHeight) - livesTex.getHeight() - 20, 
					livesTex.getWidth(), 
					livesTex.getHeight(), 0, 0, 
					livesTex.getWidth(), livesTex.getHeight(), false, false);	
			msg = "Well done! You have survived the Jungle.";		
			font.drawWrapped(spritebatch, msg, tutScreenStartX+20+tutFrogTex.getWidth()+7,
					(tutScreenStartY+tutScreenHeight) - 30,
					tutScreenWidth-tutFrogTex.getWidth()-50);
			break;
		}		
		font.setColor(Color.WHITE);
		font.setScale(1f);
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
									if (o instanceof Web){
										((Particle) p).kill();
										((Enemy) o).doDamage(player.getDamage());
									}
									else {
										if (o.circularCollsion(p)){
//											Gdx.app.log("Coll - ", "true");
											((Particle) p).kill();
											((Enemy) o).doDamage(player.getDamage());
										}
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
							((Tongue)o).stopGrowing();
						}
					}
					if (o instanceof Web){
						if (player.playerHitAnimation())
							o.kill();
					}
					else{
						if (player.circularCollsion(o))
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
					player.increaseScoreBy(10);
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

	//TODO
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
		tutTextTex.dispose();
	}
}
