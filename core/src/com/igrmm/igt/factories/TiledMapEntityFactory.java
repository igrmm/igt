package com.igrmm.igt.factories;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.utils.XmlReader;
import com.igrmm.igt.components.*;
import com.igrmm.igt.components.boundingboxes.*;

import java.util.Iterator;
import java.util.Objects;

public class TiledMapEntityFactory {
	public static Entity createEntity(MapObject mapObject) {
		Entity entity = new Entity();
		MapProperties properties = mapObject.getProperties();

		//get bounding box component
		BoundingBoxComponent bboxC = getBboxC(
				properties.get("entityType", String.class),
				properties.get("x", Float.class),
				properties.get("y", Float.class),
				properties.get("width", Float.class),
				properties.get("height", Float.class)
		);
		entity.add(bboxC);

		//get other components
		XmlReader xmlReader = new XmlReader();
		Iterator<String> iterator = properties.getKeys();
		while (iterator.hasNext()) {
			String key = iterator.next();
			Object value = properties.get(key);
			if (Objects.equals(key, "componentXml")) {
				XmlReader.Element componentXml = xmlReader.parse(value.toString());
				Component component = getComponent(componentXml);
				entity.add(component);
			}
		}

		return entity;
	}

	private static BoundingBoxComponent getBboxC(String entityType, Float x, Float y, Float width, Float height) {
		switch (entityType) {
			case "block":
				return new BlockBoundingBoxComponent(x, y, width, height);
			case "spawn-point":
				return new SpawnPointBoundingBoxComponent(x, y, width, height);
			default:
				return new BoundingBoxComponent(x, y, width, height);
		}
	}

	private static Component getComponent(XmlReader.Element componentXml) {
		XmlReader.Element properties = componentXml.getChildByName("properties");
		String propertyType = componentXml.get("propertytype");
		switch (propertyType) {
			case "spawnPointComponent":
				SpawnPointComponent spawnPointC = new SpawnPointComponent();
				XmlReader.Element spawnPointNameXml = properties.getChild(0);
				if (Objects.equals(spawnPointNameXml.getAttribute("name"), "name"))
					spawnPointC.name = spawnPointNameXml.getAttribute("value", "start");
				return spawnPointC;
			default:
				throw new Error("Component not found");
		}
	}
}
