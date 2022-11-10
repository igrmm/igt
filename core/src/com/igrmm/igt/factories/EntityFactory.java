package com.igrmm.igt.factories;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.utils.XmlReader;
import com.igrmm.igt.components.BoundingBoxComponent;

import java.util.Iterator;
import java.util.Objects;

public interface EntityFactory {
	public default Entity createEntity(MapObject mapObject) {
		Entity entity = new Entity();
		MapProperties mapObjectProperties = mapObject.getProperties();

		//get bounding box component
		BoundingBoxComponent bboxC = new BoundingBoxComponent();
		bboxC.bbox.set(
				mapObjectProperties.get("x", Float.class),
				mapObjectProperties.get("y", Float.class),
				mapObjectProperties.get("width", Float.class),
				mapObjectProperties.get("height", Float.class)
		);
		entity.add(bboxC);

		//get other components
		XmlReader xmlReader = new XmlReader();
		Iterator<String> iterator = mapObjectProperties.getKeys();
		while (iterator.hasNext()) {
			String key = iterator.next();
			Object value = mapObjectProperties.get(key);
			if (Objects.equals(key, "componentXml")) {
				XmlReader.Element componentXml = xmlReader.parse(value.toString());
				XmlReader.Element componentPropertiesXml = componentXml.getChildByName("properties");
				String propertyType = componentXml.get("propertytype");
				Component component = createComponent(propertyType, componentPropertiesXml);
				entity.add(component);
			}
		}

		return entity;
	}

	/**
	 * Override this method if the entity has other components than bounding box.
	 */
	public default Component createComponent(String propertyType, XmlReader.Element componentPropertiesXml) {
		throw new RuntimeException("Component not implemented: " + propertyType);
	}
}
