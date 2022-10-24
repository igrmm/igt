package com.igrmm.igt.factories;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.igrmm.igt.AsepriteAnimation;
import com.igrmm.igt.Assets;
import com.igrmm.igt.components.AnimationComponent;
import com.igrmm.igt.components.StatisticsComponent;
import com.igrmm.igt.components.boundingboxes.BoundingBoxComponent;
import com.igrmm.igt.components.MovementComponent;

public class PlayerFactory {
	public static Entity createPlayer(Engine engine, Assets assets) {
		Entity playerEntity = engine.createEntity();
		engine.addEntity(playerEntity);

		//Components dependencies
		AsepriteAnimation asepriteAnimation = assets.getAsepriteAnimation("player");

		//Create components
		playerEntity.add(new StatisticsComponent());
		playerEntity.add(new BoundingBoxComponent());
		playerEntity.add(new MovementComponent());
		playerEntity.add(new AnimationComponent(asepriteAnimation));

		//Tweak numbers
		MovementComponent playerMovementC = playerEntity.getComponent(MovementComponent.class);
		AnimationComponent playerAnimationC = playerEntity.getComponent(AnimationComponent.class);

		playerAnimationC.currentAnimation = "idle_right";
		playerAnimationC.offset = 16f;
		playerMovementC.maxSpeed = 240f;
		playerMovementC.acceleration = 1080f;
		playerMovementC.friction = 1080f;

		return playerEntity;
	}
}
