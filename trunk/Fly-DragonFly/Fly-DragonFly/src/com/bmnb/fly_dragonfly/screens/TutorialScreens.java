//Nathan Floor
//FLRNAT001

package com.bmnb.fly_dragonfly.screens;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class TutorialScreens {
	protected Texture tutorial_tex;
	protected Texture okBtnTex;
	protected Texture okBtnTex_clicked;
	protected boolean okBtnClicked = false;

	protected Texture tutFrogTex;
	protected Texture tutSpiderTex;
	protected Texture tutFlytrapTex;
	protected Texture tutbirdTex;
	protected Texture tutFlameTex;
	protected Texture tutFlyMoziTex;
	protected Texture tutTextTex;
	protected Texture tutWinTex;
	protected BitmapFont font;

	protected static int tutID;
	public boolean draw_tutorial;

	public TutorialScreens(BitmapFont f){
		//init tutorial graphics
		tutorial_tex = new Texture("data/tutorials/tutorial_bg.png");
		tutorial_tex.setFilter(TextureFilter.Linear,TextureFilter.Linear);
		okBtnTex = new Texture("data/tutorials/okay_button.png");
		okBtnTex_clicked = new Texture("data/tutorials/okay_button_clicked.png");
		okBtnTex.setFilter(TextureFilter.Linear,TextureFilter.Linear);		
		tutFrogTex = new Texture("data/tutorials/frog1.png");
		tutSpiderTex = new Texture("data/tutorials/spider_withnet.png");
		tutFlytrapTex = new Texture("data/textures/flytrap_left.png");
		tutbirdTex = new Texture("data/tutorials/bird1.png");
		tutFlyMoziTex = new Texture("data/tutorials/firefly.png");
		tutFlameTex = new Texture("data/tutorials/flamesButton.png");
		tutWinTex = new Texture("data/tutorials/dragonfly.png");

		font = f;
		tutID = 0;
	}

	int play_counter = 0;
	public void okBtnClicked(){
		okBtnClicked = true;
	}
	public void okBtnReleased(){
		okBtnClicked = false;
		draw_tutorial = false;
		play_counter = 0;
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
	//draw tutorial screen
	public void drawTutorialScreen(SpriteBatch spritebatch){
		float tutScreenStartX = 30;
		float tutScreenStartY = GameScreen.height/3;		
		float tutScreenWidth = GameScreen.width-70;
		float tutScreenHeight = 500;	
		CharSequence msg = "";

		spritebatch.draw(tutorial_tex, tutScreenStartX, tutScreenStartY, tutScreenWidth, tutScreenHeight, 0, 0, 
				tutorial_tex.getWidth(), tutorial_tex.getHeight(), false, false);

		if(!okBtnClicked)
			spritebatch.draw(okBtnTex, ((tutScreenStartX+tutScreenWidth)/2)-50, 
					tutScreenStartY + 70, 100, 50, 0, 0, 
					okBtnTex.getWidth(), okBtnTex.getHeight(), false, false);
		else
			spritebatch.draw(okBtnTex_clicked, ((tutScreenStartX+tutScreenWidth)/2)-50, 
					tutScreenStartY + 70, 100, 50, 0, 0, 
					okBtnTex_clicked.getWidth(), okBtnTex_clicked.getHeight(), false, false);

		if(!draw_tutorial){
			play_counter++;
			if(play_counter > 10)
				showTutorialScreen(0);
		}

		font.setScale(0.7f);	
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
			msg = "Watch out for the gas! FlyTraps are stationary. They secrete " +
					"gas which draws you towards them. Get too close and you will die.";			
			font.drawWrapped(spritebatch, msg, tutScreenStartX+20+tutFlytrapTex.getWidth()+7,
					(tutScreenStartY+tutScreenHeight) - 30,
					tutScreenWidth-tutFlytrapTex.getWidth()-50);
			break;
		case 3:
			spritebatch.draw(tutSpiderTex, tutScreenStartX+20, 
					(tutScreenStartY+tutScreenHeight) - tutSpiderTex.getHeight() - 20, 
					tutSpiderTex.getWidth(), 
					tutSpiderTex.getHeight(), 0, 0, 
					tutSpiderTex.getWidth(), tutSpiderTex.getHeight(), false, false);	
			msg = "Beware the Spider's webs!" +
					"Try burn them before reaching it. " +
					"Save some fire-breath to escape!";			
			font.drawWrapped(spritebatch, msg, tutScreenStartX+20+tutSpiderTex.getWidth()+7,
					(tutScreenStartY+tutScreenHeight) - 30,
					tutScreenWidth-tutSpiderTex.getWidth()-50);
			break;
		case 4:
			spritebatch.draw(tutbirdTex, tutScreenStartX+20, 
					(tutScreenStartY+tutScreenHeight) - tutbirdTex.getHeight() - 20, 
					tutbirdTex.getWidth(), 
					tutbirdTex.getHeight(), 0, 0, 
					tutbirdTex.getWidth(), tutbirdTex.getHeight(), false, false);
			msg = "Birds are fast and accurate. " +
					"They are waiting for you, " +
					"they will swoop down and catch you! ";			
			font.drawWrapped(spritebatch, msg, tutScreenStartX+20+tutbirdTex.getWidth()+7,
					(tutScreenStartY+tutScreenHeight) - 30,
					tutScreenWidth-tutbirdTex.getWidth()-50);
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
			font.drawWrapped(spritebatch, msg, tutScreenStartX+20+tutFlyMoziTex.getWidth()+7,
					(tutScreenStartY+tutScreenHeight) - 30,
					tutScreenWidth-tutFlyMoziTex.getWidth()-50);
			break;
		case 6:
			spritebatch.draw(tutFlameTex, tutScreenStartX+25, 
					(tutScreenStartY+tutScreenHeight) - tutFlameTex.getHeight() - 25, 
					tutFlameTex.getWidth(), 
					tutFlameTex.getHeight(), 0, 0, 
					tutFlameTex.getWidth(), tutFlameTex.getHeight(), false, false);	
			msg = "You can use your flame to kill enemies and traps which are trying to kill you." +
					"Tap/Press in the bottom right hand corner of the screen to breath your flame." +
					"But you have give yourself time to catch your breath, so plan ahead!";			
			font.drawWrapped(spritebatch, msg, tutScreenStartX+20+tutFlameTex.getWidth()+7,
					(tutScreenStartY+tutScreenHeight) - 30,
					tutScreenWidth-tutFlameTex.getWidth()-50);
			break;
		case 0:
			//win condition
			spritebatch.draw(tutWinTex, tutScreenStartX+20, 
					(tutScreenStartY+tutScreenHeight) - tutWinTex.getHeight() - 20, 
					tutWinTex.getWidth(), 
					tutWinTex.getHeight(), 0, 0, 
					tutWinTex.getWidth(), tutWinTex.getHeight(), false, false);	
			msg = "Well done! You have survived the Jungle.";
			font.setScale(1f);
			font.drawWrapped(spritebatch, msg, tutScreenStartX+20+tutWinTex.getWidth()+7,
					(tutScreenStartY+tutScreenHeight) - 30,
					tutScreenWidth-tutWinTex.getWidth()-50);
			break;
		}		
		font.setColor(Color.WHITE);
		font.setScale(1f);
	}

	
	public void dispose(){
		tutbirdTex.dispose();
		tutFrogTex.dispose();
		tutSpiderTex.dispose();
		tutFlyMoziTex.dispose();
		tutFlytrapTex.dispose();
		tutFlameTex.dispose();
		tutbirdTex.dispose();
		okBtnTex.dispose();
		okBtnTex_clicked.dispose();
		tutTextTex.dispose();
	}
}
