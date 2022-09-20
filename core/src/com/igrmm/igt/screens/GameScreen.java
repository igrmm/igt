package com.igrmm.igt.screens;

import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class GameScreen extends ScreenAdapter {
	SpriteBatch batch = new SpriteBatch();
	Texture img = new Texture("img.png");
	private final OrthographicCamera camera = new OrthographicCamera();

	@Override
	public void render(float delta) {
		camera.position.x = img.getWidth() / 2f;
		camera.position.y = img.getHeight() / 2f;

		camera.update();
		batch.setProjectionMatrix(camera.combined);
		batch.begin();
		batch.draw(img, 0, 0);
		batch.end();
	}

	@Override
	public void resize(int width, int height) {
		camera.viewportHeight = height;
		camera.viewportWidth = width;
	}

	@Override
	public void dispose() {
		batch.dispose();
		img.dispose();
	}
}
