package com.igrmm.igt.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Pools;
import com.igrmm.igt.CollisionMaybe;
import com.igrmm.igt.components.BoundingBoxComponent;
import com.igrmm.igt.components.BroadPhaseCollisionComponent;
import com.igrmm.igt.components.MovementComponent;

import java.util.Collections;
import java.util.List;

public class CollisionSystem extends IteratingSystem {
	private final ComponentMapper<MovementComponent> movementM;
	private final ComponentMapper<BoundingBoxComponent> bboxM;
	private final ComponentMapper<BroadPhaseCollisionComponent> bPhaseColM;

	public CollisionSystem() {
		super(Family.all(BroadPhaseCollisionComponent.class).get());
		movementM = ComponentMapper.getFor(MovementComponent.class);
		bboxM = ComponentMapper.getFor(BoundingBoxComponent.class);
		bPhaseColM = ComponentMapper.getFor(BroadPhaseCollisionComponent.class);
	}

	@Override
	protected void processEntity(Entity entity, float deltaTime) {
		//resolve collisions
		BroadPhaseCollisionComponent bPhaseColC = bPhaseColM.get(entity);
		List<CollisionMaybe> collisions = bPhaseColC.collisions;
		Collections.sort(collisions);
		for (CollisionMaybe collision : collisions) {
			collision.resolve();
			Pools.free(collision);
		}

		//make mobile entities move after resolving collisions
		MovementComponent movC = movementM.get(entity);
		BoundingBoxComponent bboxC = bboxM.get(entity);

		Rectangle bbox = bboxC.bbox;
		Vector2 speed = movC.speed;

		bbox.x += speed.x;
		bbox.y += speed.y;
	}
}
