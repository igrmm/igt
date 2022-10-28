package com.igrmm.igt.factories;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.maps.MapObject;

public class TiledMapEntityFactory implements EntityFactory {
	@Override
	public Entity createEntity(MapObject mapObject) {
		String entityType = mapObject.getProperties().get("entityType", String.class);
		switch (entityType) {
			case "block":
				BlockEntityFactory blockEntityFactory = new BlockEntityFactory();
				return blockEntityFactory.createEntity(mapObject);
			case "spawn-point":
				SpawnPointEntityFactory spawnPointEntityFactory = new SpawnPointEntityFactory();
				return spawnPointEntityFactory.createEntity(mapObject);
			default:
				throw new RuntimeException("Entity not implemented: " + entityType);
		}
	}
}
