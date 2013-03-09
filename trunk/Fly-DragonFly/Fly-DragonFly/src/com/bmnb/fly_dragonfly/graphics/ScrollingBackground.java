package com.bmnb.fly_dragonfly.graphics;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;


/**
 * This class holds the methods required to draw the background which scrolls dynamically
 * 
 * @author Brandon James Talbot
 *
 */

public class ScrollingBackground {
	
	/**
	 * Globals
	 */
	protected Texture tex;
	protected float [] yPositions;
	protected float width, height, speed;
	
	/**
	 * Constructor
	 * @param texture path to the texture
	 * @param scwidth The screen width
	 * @param scheight The screen height
	 * @param speed The speed for the scrolling
	 */
	public ScrollingBackground (String texture, float scwidth, float scheight, float speed){
		tex = new Texture(texture);		
		
		this.speed = speed;
		
		width = scwidth;
		height = width / tex.getWidth() * tex.getHeight();
		
		int num = (int)(scheight * 1.5f / height) + 1;
		
		yPositions = new float[num];
		
		for (int i = 0; i < num; ++i)
			yPositions[i] = i * height;
	}
	
	public void update(float delta){
		for(int i = 0;i<yPositions.length;++i){
			// scroll the positions
			yPositions[i] -= delta * speed;

			// move the tex above when it leaves the screen
			if (yPositions[i] + height < 0){
				yPositions[i] += height * yPositions.length;
			}
		}
	}
	
	/**
	 * Draws the background
	 * @param batch Sprite batch to draw with
	 * @param delta The delta time for the game
	 */
	public void draw(SpriteBatch batch, float delta){
		for (int i = 0; i < yPositions.length; ++i){
			// draw
			batch.draw(tex, 0, yPositions[i], width, height);			
		}
	}
}
