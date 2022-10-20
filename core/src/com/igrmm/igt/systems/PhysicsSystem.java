package com.igrmm.igt.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.igrmm.igt.components.boundingboxes.BoundingBoxComponent;
import com.igrmm.igt.components.MovementComponent;

public class PhysicsSystem extends IteratingSystem {
	private final ComponentMapper<MovementComponent> movementM;
	private final ComponentMapper<BoundingBoxComponent> bBoxM;

	public PhysicsSystem() {
		super(Family.all(MovementComponent.class, BoundingBoxComponent.class).get());
		movementM = ComponentMapper.getFor(MovementComponent.class);
		bBoxM = ComponentMapper.getFor(BoundingBoxComponent.class);
	}

	@Override
	protected void processEntity(Entity entity, float deltaTime) {
		MovementComponent movementC = movementM.get(entity);
		BoundingBoxComponent bBoxC = bBoxM.get(entity);

		//acceleration
		movementC.speed.x += movementC.movementSignalIntention * movementC.acceleration * deltaTime * deltaTime;

		//max speed
		if (movementC.movementSignalIntention != 0)
			movementC.speed.x = Math.abs(movementC.speed.x) < movementC.maxSpeed * deltaTime
					? movementC.speed.x
					: movementC.maxSpeed * deltaTime * movementC.movementSignalIntention;

		//friction
		if (movementC.movementSignalIntention == 0 && movementC.speed.x != 0) {
			if (movementC.speed.x < 0) {
				movementC.speed.x += movementC.friction * deltaTime * deltaTime;
				movementC.speed.x = movementC.speed.x < 0 ? movementC.speed.x : 0;
			} else if (movementC.speed.x > 0) {
				movementC.speed.x -= movementC.friction * deltaTime * deltaTime;
				movementC.speed.x = movementC.speed.x > 0 ? movementC.speed.x : 0;
			}
		}

		bBoxC.bBox.x += movementC.speed.x;
	}
}
