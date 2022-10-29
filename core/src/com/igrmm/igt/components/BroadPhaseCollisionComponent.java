package com.igrmm.igt.components;

import com.badlogic.ashley.core.Component;
import com.igrmm.igt.CollisionMaybe;

import java.util.ArrayList;
import java.util.List;

public class BroadPhaseCollisionComponent implements Component {
	public List<CollisionMaybe> collisions = new ArrayList<>();
}
