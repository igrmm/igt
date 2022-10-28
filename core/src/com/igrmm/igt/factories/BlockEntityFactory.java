package com.igrmm.igt.factories;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.utils.XmlReader;
import com.igrmm.igt.components.EntityTypeComponent;

public class BlockEntityFactory implements EntityFactory {
	@Override
	public Entity createEntity(MapObject mapObject) {
		Entity entity = EntityFactory.super.createEntity(mapObject);
		entity.add(new BlockETComponent());
		return entity;
	}

	@Override
	public Component createComponent(String propertyType, XmlReader.Element componentPropertiesXml) {
		throw new Error("Component not found = " + propertyType);
	}

	public static class BlockETComponent extends EntityTypeComponent {
	}
}
