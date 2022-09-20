package com.igrmm.igt.screens;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Texture;
import com.igrmm.igt.components.TextureComponent;
import com.igrmm.igt.systems.RenderingSystem;

public class GameScreen extends ScreenAdapter {
	private final Texture img = new Texture("img.png");
	private final Engine engine = new PooledEngine();

	public GameScreen() {
		Entity playerEntity = engine.createEntity();
		engine.addEntity(playerEntity);
		playerEntity.add(new TextureComponent(img));
		engine.addSystem(new RenderingSystem());
	}

	@Override
	public void render(float delta) {
		engine.update(delta);
	}

	@Override
	public void resize(int width, int height) {
		RenderingSystem renderingSystem = engine.getSystem(RenderingSystem.class);
		renderingSystem.resizeScreen(width,height);
	}

	@Override
	public void dispose() {
		img.dispose();
		RenderingSystem renderingSystem = engine.getSystem(RenderingSystem.class);
		renderingSystem.dispose();
	}
}
