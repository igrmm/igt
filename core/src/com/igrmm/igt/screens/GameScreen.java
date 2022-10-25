package com.igrmm.igt.screens;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.igrmm.igt.Assets;
import com.igrmm.igt.Igt;
import com.igrmm.igt.Save;
import com.igrmm.igt.components.MovementComponent;
import com.igrmm.igt.components.MapComponent;
import com.igrmm.igt.factories.PlayerFactory;
import com.igrmm.igt.systems.PhysicsSystem;
import com.igrmm.igt.systems.RenderingSystem;
import com.igrmm.igt.systems.TimeTrackingSystem;
import com.igrmm.igt.systems.UserInterfaceSystem;

public class GameScreen extends ScreenAdapter {
	private final Engine engine;

	private GameScreen(Engine engine) {
		this.engine = engine;
	}

	public static GameScreen createGameScreen(Igt game) {
		Engine engine = new PooledEngine();
		Assets assets = game.assets;
		Save save = assets.getSave();
		MapComponent mapComponent = save.mapComponent;
		TiledMap tiledMap = assets.getTiledMap(mapComponent.name);
		Entity playerEntity = PlayerFactory.createPlayer(engine, assets);
		MovementComponent playerMovementC = playerEntity.getComponent(MovementComponent.class);
		engine.addSystem(new UserInterfaceSystem(playerMovementC));
		engine.addSystem(new PhysicsSystem());
		engine.addSystem(new RenderingSystem(tiledMap));
		engine.addSystem(new TimeTrackingSystem());
		return new GameScreen(engine);
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
		RenderingSystem renderingSystem = engine.getSystem(RenderingSystem.class);
		renderingSystem.dispose();
		UserInterfaceSystem userInterfaceSystem = engine.getSystem(UserInterfaceSystem.class);
		userInterfaceSystem.dispose();
	}
}
