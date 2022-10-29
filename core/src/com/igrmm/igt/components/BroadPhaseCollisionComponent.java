package com.igrmm.igt.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.utils.Array;
import com.igrmm.igt.CollisionMaybe;

public class BroadPhaseCollisionComponent implements Component {
	public Array<CollisionMaybe> collisions = new Array<>();
}
