package com.igrmm.igt.factories;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.utils.XmlReader;
import com.igrmm.igt.components.EntityTypeComponent;
import com.igrmm.igt.components.SpawnPointComponent;

import java.util.Objects;

public class SpawnPointEntityFactory implements EntityFactory {
	@Override
	public Entity createEntity(MapObject mapObject) {
		Entity entity = EntityFactory.super.createEntity(mapObject);
		entity.add(new SpawnPointETComponent());
		return entity;
	}

	@Override
	public Component createComponent(String propertyType, XmlReader.Element componentPropertiesXml) {
		if ("spawnPointComponent".equals(propertyType)) {
			SpawnPointComponent spawnPointC = new SpawnPointComponent();
			XmlReader.Element spawnPointNameXml = componentPropertiesXml.getChild(0);
			if (Objects.equals(spawnPointNameXml.getAttribute("name"), "name"))
				spawnPointC.name = spawnPointNameXml.getAttribute("value", "start");
			return spawnPointC;
		}
		return EntityFactory.super.createComponent(propertyType, componentPropertiesXml);
	}

	public static class SpawnPointETComponent extends EntityTypeComponent {
	}
}
