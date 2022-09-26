package com.igrmm.igt.factories;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.igrmm.igt.components.BoundingBoxComponent;
import com.igrmm.igt.components.MovementComponent;

public class PlayerFactory {
	public static Entity createPlayer(Engine engine) {
		Entity playerEntity = engine.createEntity();
		engine.addEntity(playerEntity);

		//Create components
		playerEntity.add(new BoundingBoxComponent());
		MovementComponent playerMovementC = new MovementComponent();
		playerEntity.add(playerMovementC);

		//Tweak numbers
		playerMovementC.maxSpeed = 240f;
		playerMovementC.acceleration = 1080f;
		playerMovementC.friction = 1080f;

		return playerEntity;
	}
}
