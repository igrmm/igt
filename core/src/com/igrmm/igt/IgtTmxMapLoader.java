package com.igrmm.igt;

import com.badlogic.gdx.assets.loaders.FileHandleResolver;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.utils.XmlReader;

import java.util.Objects;

public class IgtTmxMapLoader extends TmxMapLoader {
	public IgtTmxMapLoader(FileHandleResolver resolver) {
		super(resolver);
	}

	@Override
	protected void loadObject(TiledMap map, MapObjects objects, XmlReader.Element element, float heightInPixels) {
		//get the "class" of the object and put it into properties as entityType
		String entityType = element.get("class");
		XmlReader.Element properties;
		if (element.hasChild("properties")) {
			properties = element.getChildByName("properties");
		} else {
			properties = new XmlReader.Element("properties", element);
			element.addChild(properties);
		}
		XmlReader.Element property = new XmlReader.Element("property", properties);
		property.setAttribute("value", entityType);
		property.setAttribute("name", "entityType");
		properties.addChild(property);
		super.loadObject(map, objects, element, heightInPixels);
	}

	@Override
	protected void loadProperties(MapProperties properties, XmlReader.Element element) {
		//add components to properties as xml objects
		if (element == null) return;
		if (element.getName().equals("properties")) {
			for (XmlReader.Element property : element.getChildrenByName("property")) {
				String name = property.getAttribute("name", null);
				String value = property.getAttribute("value", null);
				String type = property.getAttribute("type", null);
				if (value == null) {
					value = property.getText();
				}
				if (Objects.equals(type, "class")) {
					properties.put("componentXml", property);
				} else {
					Object castValue = castProperty(name, value, type);
					properties.put(name, castValue);
				}
			}
		}
	}
}
