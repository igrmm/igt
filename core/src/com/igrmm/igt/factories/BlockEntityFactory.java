package com.igrmm.igt.factories;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.maps.MapObject;
import com.igrmm.igt.components.EntityTypeComponent;

public class BlockEntityFactory implements EntityFactory {
	@Override
	public Entity createEntity(MapObject mapObject) {
		Entity entity = EntityFactory.super.createEntity(mapObject);
		entity.add(new BlockETComponent());
		return entity;
	}

	public static class BlockETComponent extends EntityTypeComponent {
	}
}
