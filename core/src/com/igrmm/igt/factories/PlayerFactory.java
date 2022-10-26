package com.igrmm.igt.factories;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.gdx.math.Rectangle;
import com.igrmm.igt.AsepriteAnimation;
import com.igrmm.igt.Assets;
import com.igrmm.igt.Save;
import com.igrmm.igt.components.AnimationComponent;
import com.igrmm.igt.components.SpawnPointComponent;
import com.igrmm.igt.components.boundingboxes.BoundingBoxComponent;
import com.igrmm.igt.components.MovementComponent;
import com.igrmm.igt.components.boundingboxes.SpawnPointBoundingBoxComponent;

import java.util.Objects;

public class PlayerFactory {
	public static Entity createPlayer(Engine engine, Assets assets) {
		Entity playerEntity = engine.createEntity();
		engine.addEntity(playerEntity);
		Save save = assets.getSave();

		//Components dependencies
		AsepriteAnimation asepriteAnimation = assets.getAsepriteAnimation("player");

		//Default components
		playerEntity.add(new BoundingBoxComponent());
		playerEntity.add(new AnimationComponent(asepriteAnimation));
		playerEntity.add(new MovementComponent());

		//Serializable components
		playerEntity.add(save.statisticsC);
		playerEntity.add(save.spawnPointC);
		playerEntity.add(save.mapC);

		//Tweak numbers
		MovementComponent playerMovC = playerEntity.getComponent(MovementComponent.class);
		AnimationComponent playerAnimationC = playerEntity.getComponent(AnimationComponent.class);

		playerAnimationC.currentAnimation = "idle_right";
		playerAnimationC.offset = 16f;
		playerMovC.maxSpeed = 240f;
		playerMovC.acceleration = 1080f;
		playerMovC.friction = 1080f;

		//make player spawn at saved spawn point
		SpawnPointComponent playerSpawnPointC = playerEntity.getComponent(SpawnPointComponent.class);
		BoundingBoxComponent playerBboxC = playerEntity.getComponent(BoundingBoxComponent.class);
		Rectangle playerBbox = playerBboxC.bbox;
		for (Entity spawnPointE : engine.getEntitiesFor(Family.one(SpawnPointBoundingBoxComponent.class).get())) {
			SpawnPointComponent spawnPointC = spawnPointE.getComponent(SpawnPointComponent.class);
			if (Objects.equals(playerSpawnPointC.name, spawnPointC.name)) {
				SpawnPointBoundingBoxComponent spawnPointBboxC
						= spawnPointE.getComponent(SpawnPointBoundingBoxComponent.class);
				Rectangle spawnPointBbox = spawnPointBboxC.bbox;

				playerBbox.x = spawnPointBbox.x;
				playerBbox.y = spawnPointBbox.y;
			}
		}

		return playerEntity;
	}
}
