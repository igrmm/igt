package com.igrmm.igt.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.math.Rectangle;

public class BoundingBoxComponent implements Component {
	public final Rectangle bbox = new Rectangle();
}
