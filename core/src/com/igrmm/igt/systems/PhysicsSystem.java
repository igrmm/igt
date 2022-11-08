package com.igrmm.igt.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.igrmm.igt.components.BoundingBoxComponent;
import com.igrmm.igt.components.MovementComponent;

public class PhysicsSystem extends IteratingSystem {
	private final ComponentMapper<MovementComponent> movementM;

	public PhysicsSystem() {
		super(Family.all(MovementComponent.class, BoundingBoxComponent.class).get());
		movementM = ComponentMapper.getFor(MovementComponent.class);
	}

	@Override
	protected void processEntity(Entity entity, float deltaTime) {
		MovementComponent movementC = movementM.get(entity);

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

		//jumping
		if (movementC.jumpIntention) {
			if (movementC.grounded && !movementC.jumping) {
				movementC.jumpTimer = movementC.jumpTime;
			}
			movementC.jumping = true;
		} else if (movementC.grounded) movementC.jumping = false;

		if (movementC.jumpTimer > 0.0f) {
			movementC.speed.y = movementC.jumpForce * deltaTime;
			movementC.jumpTimer -= deltaTime;
			if (!movementC.jumpIntention) movementC.jumpTimer = 0.0f;
		}

		//gravity
		float gravity = movementC.gravity;
		if (movementC.jumping && movementC.speed.y <= 0.0f) gravity *= 2.0f;
		movementC.speed.y += gravity * deltaTime * deltaTime;
	}
}
