package com.bmnb.fly_dragonfly.objects;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.bmnb.fly_dragonfly.screens.GameScreen;
import com.bmnb.fly_dragonfly.sound.MediaPlayer;

public class Frog extends StaticEnemy {

	private static final float DIFF_Y_TRIGGER = 600;
	private boolean hasTriggered = false;
	private long frogSndHnd;
	public Frog(Vector2 position, float width, float height, float speed,
			float scWidth, float scHeight, Player player) {
		super(position, width, height, speed, scWidth, scHeight, player, 100);

		sortVal = 2;

		setTexture(new Texture("data/textures/frog1.png"));
		MediaPlayer.loadSound("data/sound/toad.mp3");
		frogSndHnd = MediaPlayer.playSound("data/sound/toad.mp3", true);
		MediaPlayer.setSoundVolume("data/sound/toad.mp3",1.0f,frogSndHnd);
	}
	@Override
	public void kill(){
		super.kill();
		MediaPlayer.stopSound("data/sound/toad.mp3",frogSndHnd);
	}
	@Override
	public void update(float delta) {

		if (isDead()){
			removeable = true;
		}
		if (this.getY() - player.getY() < DIFF_Y_TRIGGER && !hasTriggered) {
			hasTriggered = true;
			GameScreen.addObject(new Tongue(this.getPosition().cpy(), this.getWidth()/4f, 1,
					this.speed, screenWidth, screenHeight, player));
		}
		if (!hasTriggered) {
			float angle = (player.getPosition().cpy().sub(this.getPosition()
					.cpy())).angle();
			this.setRotation(angle - 90);
		}

		super.update(delta);
	}
}
