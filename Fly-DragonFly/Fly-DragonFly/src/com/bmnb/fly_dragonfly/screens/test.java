package com.bmnb.fly_dragonfly.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class test implements Screen {

	SpriteBatch batch;
	Texture t;
	float angle = 0;
	float height = 100;
	
	@Override
	public void render(float delta) {
		// clear the screen
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		batch.begin();
		batch.draw(t, Gdx.graphics.getWidth() / 2 - 50,
				Gdx.graphics.getHeight() / 2, 50, 0, 100, height, 1, 1, angle, 0, 0,
				t.getWidth(), t.getHeight(), false, false);
		batch.end();
		
		if (Gdx.input.isKeyPressed(Keys.W))
			height++;
		if (Gdx.input.isKeyPressed(Keys.S))
			height--;
		if (Gdx.input.isKeyPressed(Keys.A))
			angle++;
		if (Gdx.input.isKeyPressed(Keys.D))
			angle--;
		
	}

	@Override
	public void resize(int width, int height) {
	}

	@Override
	public void show() {
		batch = new SpriteBatch();
		t = new Texture("data/tongue.png");
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
