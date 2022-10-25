package com.igrmm.igt.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class ErrorScreen extends ScreenAdapter {
	private final SpriteBatch batch;
	private final BitmapFont font;
	private final String error;

	public ErrorScreen(String error) {
		font = new BitmapFont();
		batch = new SpriteBatch();
		this.error = error;
	}

	@Override
	public void resize(int width, int height) {
		batch.getProjectionMatrix().setToOrtho2D(0.0f, 0.0f, width, height);
	}

	@Override
	public void render(float delta) {
		batch.begin();
		font.draw(batch, error, 0.0f, Gdx.graphics.getHeight());
		batch.end();
	}

	@Override
	public void dispose() {
		batch.dispose();
		font.dispose();
	}
}
