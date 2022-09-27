package com.igrmm.igt.screens;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.igrmm.igt.components.MovementComponent;
import com.igrmm.igt.components.TextureComponent;
import com.igrmm.igt.factories.PlayerFactory;
import com.igrmm.igt.systems.PhysicsSystem;
import com.igrmm.igt.systems.RenderingSystem;
import com.igrmm.igt.systems.UserInterfaceSystem;

public class GameScreen extends ScreenAdapter {
	private final Texture img = new Texture("img.png");
	private final TiledMap tiledMap = new TmxMapLoader().load("tiled/maps/start.tmx");
	private final Engine engine = new PooledEngine();

	public GameScreen() {
		Entity playerEntity = PlayerFactory.createPlayer(engine);
		playerEntity.add(new TextureComponent(img));
		MovementComponent playerMovementC = playerEntity.getComponent(MovementComponent.class);
		engine.addSystem(new UserInterfaceSystem(playerMovementC));
		engine.addSystem(new PhysicsSystem());
		engine.addSystem(new RenderingSystem(tiledMap));
	}

	@Override
	public void render(float delta) {
		engine.update(delta);
	}

	@Override
	public void resize(int width, int height) {
		RenderingSystem renderingSystem = engine.getSystem(RenderingSystem.class);
		renderingSystem.resizeScreen(width, height);
	}

	@Override
	public void dispose() {
		img.dispose();
		RenderingSystem renderingSystem = engine.getSystem(RenderingSystem.class);
		renderingSystem.dispose();
		UserInterfaceSystem userInterfaceSystem = engine.getSystem(UserInterfaceSystem.class);
		userInterfaceSystem.dispose();
	}
}
