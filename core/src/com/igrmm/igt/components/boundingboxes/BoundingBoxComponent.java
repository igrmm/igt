package com.igrmm.igt.components.boundingboxes;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.math.Rectangle;

/**
 * Generic BoundingBox component containing a rectangle. Unique bounding boxes can be created by extending this class,
 * making it easy to identify entities.
 */
public class BoundingBoxComponent implements Component {
	public final Rectangle bBox = new Rectangle();
}
