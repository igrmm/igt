package com.igrmm.igt.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.math.Rectangle;

/**
 * Generic BoundingBox component containing a rectangle. Unique bounding boxes can be created by extending this class,
 * making it easy to identify entities.
 */
public class BoundingBoxComponent implements Component {
	public final Rectangle bbox;

	public BoundingBoxComponent() {
		bbox = new Rectangle();
	}

	public BoundingBoxComponent(float x, float y, float width, float height) {
		bbox = new Rectangle(x, y, width, height);
	}
}
