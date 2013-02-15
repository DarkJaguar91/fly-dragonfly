package com.bmnb.fly_dragonfly.Screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.bmnb.fly_dragonfly.InputProcessors.GameInputProcessor;
import com.bmnb.fly_dragonfly.Objects.Player;

/**
 * The Game screen This holds all objects and drawing methods required for the
 * game
 * 
 * @author Brandon James Talbot
 * 
 */

public class GameScreen implements Screen {

	protected Player player;
	protected SpriteBatch batch;

	public Player getPlayer() {
		return player;
	}

	@Override
	public void render(float delta) {
		player.update(delta);

		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		batch.begin();
		player.Draw(batch);
		batch.end();
	}

	@Override
	public void resize(int width, int height) {
	}

	@Override
	public void show() {
		batch = new SpriteBatch();

		player = new Player(new Vector2(), 50, 50, 0, 60, "data/square.png");

		Gdx.input.setInputProcessor(new GameInputProcessor(this));
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
	}

}
