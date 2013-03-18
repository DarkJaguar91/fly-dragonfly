package com.bmnb.fly_dragonfly.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.bmnb.fly_dragonfly.Fly_DragonFly;
import com.bmnb.fly_dragonfly.input.MenuInput;

public class MenuScreen implements Screen{
	
	//instance variables
	private Texture dragon_menu;
	private Texture menu_back;
	private Texture play_btnTex;
	private Texture options_btnTex;
	private Texture exit_btnTex;
	private Texture play_btnTex_clicked;
	private Texture options_btnTex_clicked;
	private Texture exit_btnTex_clicked;
	
	private boolean play_clicked = false;
	private boolean options_clicked = false;
	private boolean exit_clicked = false;
	
	private boolean start_new_game = false;
	private boolean exit_game = false;
	private boolean show_options = false;
	
	private int play_counter = 0;
	
	protected SpriteBatch batch;
	protected BitmapFont font;
	protected Fly_DragonFly game;
	
	protected boolean drawHighScores;
	protected boolean drawOptions;
	protected CharSequence message = "";
	protected float screenHeight, screenWidth;
	
	public MenuScreen(Fly_DragonFly g){
		game = g;
	}

	public void playBtnClicked(){
		play_clicked = true;
	}
	public void playBtnReleased(){
		play_clicked = false;
		start_new_game = true;
		play_counter = 0;
	}
	
	public void exitBtnClicked(){
		exit_clicked = true;
	}
	public void exitBtnReleased(){
		exit_clicked = false;
		exit_game = true;
		play_counter = 0;
	}
	
	public void optionsBtnClicked(){
		options_clicked = true;
	}
	public void optionsBtnReleased(){
		options_clicked = false;
		show_options = true;
		play_counter = 0;
	}
	
	@Override
	public void render(float delta) {
		//clear screen
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT); 
		
		//draw everything
		batch.begin();
		
		font.setColor(Color.WHITE);
		
		if(drawHighScores){
			drawHighScores(batch);
		}
		else if(drawOptions){
			drawOptionScreen(batch);
		}
		else{
			//draw main menu
			float menuScreenStartX = 10;
			float menuScreenStartY = 50;		
			float menuScreenWidth = 280;
			float menuScreenHeight = 340;	
						
			//draw logo
			batch.draw(dragon_menu, ((menuScreenStartX+menuScreenWidth)/2)-70, 
					menuScreenStartY + menuScreenHeight, 150, 100, 0, 0, 
					dragon_menu.getWidth(), dragon_menu.getHeight(), false, false);
			
			batch.draw(menu_back, menuScreenStartX, menuScreenStartY, menuScreenWidth, menuScreenHeight, 0, 0, 
					menu_back.getWidth(), menu_back.getHeight(), false, false);

			//draw menu buttons				
			if(!play_clicked){
				batch.draw(play_btnTex, ((menuScreenStartX+menuScreenWidth)/2)-70, 
						menuScreenStartY + 180, 140, 70, 0, 0, 
						play_btnTex.getWidth(), play_btnTex.getHeight(), false, false);				
			}
			else{
				batch.draw(play_btnTex_clicked, ((menuScreenStartX+menuScreenWidth)/2)-70, 
						menuScreenStartY + 180, 140, 70, 0, 0, 
						play_btnTex_clicked.getWidth(), play_btnTex_clicked.getHeight(), false, false);
			}
					
			if(!options_clicked)
				batch.draw(options_btnTex, ((menuScreenStartX+menuScreenWidth)/2)-50, 
						menuScreenStartY + 110, 100, 50, 0, 0, 
						options_btnTex.getWidth(), options_btnTex.getHeight(), false, false);
			else
				batch.draw(options_btnTex_clicked, ((menuScreenStartX+menuScreenWidth)/2)-50, 
						menuScreenStartY + 110, 100, 50, 0, 0, 
						options_btnTex_clicked.getWidth(), options_btnTex_clicked.getHeight(), false, false);
				
			if(exit_clicked)
				batch.draw(exit_btnTex_clicked, ((menuScreenStartX+menuScreenWidth)/2)-50, 
					menuScreenStartY + 40, 100, 50, 0, 0, 
					exit_btnTex_clicked.getWidth(), exit_btnTex_clicked.getHeight(), false, false);
			else
				batch.draw(exit_btnTex, ((menuScreenStartX+menuScreenWidth)/2)-50, 
						menuScreenStartY + 40, 100, 50, 0, 0, 
						exit_btnTex.getWidth(), exit_btnTex.getHeight(), false, false);
			
			//display click animation
			if(start_new_game){
				play_counter++;
				if(play_counter > 5)
					game.startGame();
			}
			if(exit_game){
				play_counter++;
				if(play_counter > 5)
					System.exit(1);
			}
			if(show_options){
				play_counter++;
				if(play_counter > 5){
					//showOptions();
				}
			}
			
//			font.setScale(0.6f);
			font.setColor(Color.BLACK);
			message = "Main Menu";			
			font.draw(batch, message, 30,menuScreenStartY+menuScreenHeight-30);			
		}
		
		batch.end();
		
	}
	
	public void drawOptionScreen(SpriteBatch batch){
		message = "Options";			
		font.draw(batch, message, 10,300);
	}
	public void showOptions(){
		drawHighScores = false;
		if(drawOptions){
			drawOptions = false;
		}
		else{
			drawOptions = true;
		}
		System.out.println("show options");
	}
	
	public void drawHighScores(SpriteBatch batch){
		message = "HighScores";			
		font.draw(batch, message, 10,300);
	}
	public void showHighScores(){
		drawOptions = false;
		if(drawHighScores){
			drawHighScores = false;
		}
		else{
			drawHighScores = true;
		}
		System.out.println("show highscores");
	}

	public void showMainMenu(){
		drawHighScores = false;
		drawOptions = false;
		System.out.println("show main menu");
	}
	
	@Override
	public void resize(int width, int height) {}

	@Override
	public void show() {
		// setting up of major devices
		batch = new SpriteBatch();
		drawHighScores = false;
		drawOptions = false;
		screenHeight = GameScreen.height;
		screenWidth = GameScreen.width;
		
		//import textures
		dragon_menu = new Texture("data/menu/dragonfly.png");
		menu_back = new Texture("data/menu/tutorial_bg.png");
		play_btnTex = new Texture("data/menu/play_button.png");
		options_btnTex = new Texture("data/menu/options_button.png");
		exit_btnTex = new Texture("data/menu/exit_button.png");
		play_btnTex_clicked = new Texture("data/menu/play_button_clicked.png");
		options_btnTex_clicked = new Texture("data/menu/options_button_clicked.png");
		exit_btnTex_clicked = new Texture("data/menu/exit_button_clicked.png");		
		
		// set the input processor
		Gdx.input.setInputProcessor(new MenuInput(game));
		Gdx.input.setCatchBackKey(true);
		Gdx.input.setCatchMenuKey(true);
		((MenuInput)Gdx.input.getInputProcessor()).setGameScreen(this);//TODO
		
		//Load font
		font = new BitmapFont(Gdx.files.internal("data/font/commicsans.fnt"),
				Gdx.files.internal("data/font/commicsans.png"), false);	
		
	}

	@Override
	public void hide() {}

	@Override
	public void pause() {}

	@Override
	public void resume() {}

	@Override
	public void dispose() {
		batch.dispose();
		dragon_menu.dispose();
		menu_back.dispose();
		play_btnTex.dispose();
		options_btnTex.dispose();
		exit_btnTex.dispose();
		play_btnTex_clicked.dispose();
		options_btnTex_clicked.dispose();
		exit_btnTex_clicked.dispose();
	}

}
