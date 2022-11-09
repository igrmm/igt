package com.igrmm.igt.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.math.Rectangle;

public class BoundingBoxComponent implements Component {
	public final Rectangle bbox;

	public BoundingBoxComponent() {
		bbox = new Rectangle();
	}

	public BoundingBoxComponent(float x, float y, float width, float height) {
		bbox = new Rectangle(x, y, width, height);
	}
}
