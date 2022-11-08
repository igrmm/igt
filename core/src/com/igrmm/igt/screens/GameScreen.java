package com.igrmm.igt.screens;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.maps.MapGroupLayer;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.igrmm.igt.Assets;
import com.igrmm.igt.Igt;
import com.igrmm.igt.Save;
import com.igrmm.igt.components.MapComponent;
import com.igrmm.igt.factories.PlayerFactory;
import com.igrmm.igt.factories.TiledMapEntityFactory;
import com.igrmm.igt.systems.*;

public class GameScreen extends ScreenAdapter {
	private final Engine engine;

	private GameScreen(Engine engine) {
		this.engine = engine;
	}

	public static GameScreen createGameScreen(Igt game) {
		Engine engine = new PooledEngine();
		Assets assets = game.assets;
		Save save = assets.getSave();

		//load tiledmap using save
		MapComponent mapC = save.mapC;
		TiledMap tiledMap = assets.getTiledMap(mapC.name);

		//get entities from tiled map
		MapGroupLayer objectsLayer = (MapGroupLayer) tiledMap.getLayers().get("objects");
		TiledMapEntityFactory tiledMapEntityFactory = new TiledMapEntityFactory();
		for (MapLayer mapLayer : objectsLayer.getLayers()) {
			for (MapObject mapObject : mapLayer.getObjects()) {
				engine.addEntity(tiledMapEntityFactory.createEntity(mapObject));
			}
		}

		Entity playerE = PlayerFactory.createPlayer(engine, assets);

		//create systems
		engine.addSystem(new PlayerSystem(playerE, game));
		engine.addSystem(new PhysicsSystem());
		engine.addSystem(new RenderingSystem(tiledMap));
		engine.addSystem(new BlockSystem(playerE));
		engine.addSystem(new CollisionSystem());

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
		PlayerSystem playerSystem = engine.getSystem(PlayerSystem.class);
		playerSystem.resizeScreen(width, height);
	}

	@Override
	public void dispose() {
		RenderingSystem renderingSystem = engine.getSystem(RenderingSystem.class);
		renderingSystem.dispose();
		PlayerSystem playerSystem = engine.getSystem(PlayerSystem.class);
		playerSystem.dispose();
	}
}
