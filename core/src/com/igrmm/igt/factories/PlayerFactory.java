package com.igrmm.igt.factories;

import com.badlogic.ashley.core.ComponentMapper;
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
		Entity playerE = engine.createEntity();
		engine.addEntity(playerE);
		Save save = assets.getSave();

		//component mappers
		ComponentMapper<MovementComponent> movementM = ComponentMapper.getFor(MovementComponent.class);
		ComponentMapper<AnimationComponent> animationM = ComponentMapper.getFor(AnimationComponent.class);
		ComponentMapper<SpawnPointComponent> spawnPointM = ComponentMapper.getFor(SpawnPointComponent.class);
		ComponentMapper<BoundingBoxComponent> bboxM = ComponentMapper.getFor(BoundingBoxComponent.class);
		ComponentMapper<SpawnPointBoundingBoxComponent> spawnPointbboxM = ComponentMapper.getFor(SpawnPointBoundingBoxComponent.class);

		//components dependencies
		AsepriteAnimation asepriteAnimation = assets.getAsepriteAnimation("player");

		//default components
		playerE.add(new BoundingBoxComponent());
		playerE.add(new AnimationComponent(asepriteAnimation));
		playerE.add(new MovementComponent());

		//serializable components
		playerE.add(save.statisticsC);
		playerE.add(save.spawnPointC);
		playerE.add(save.mapC);

		//tweak numbers
		MovementComponent playerMovC = movementM.get(playerE);
		AnimationComponent playerAnimationC = animationM.get(playerE);

		playerAnimationC.currentAnimation = "idle_right";
		playerAnimationC.offset = 16f;
		playerMovC.maxSpeed = 240f;
		playerMovC.acceleration = 1080f;
		playerMovC.friction = 1080f;

		//make player spawn at saved spawn point
		SpawnPointComponent playerSpawnPointC = spawnPointM.get(playerE);
		BoundingBoxComponent playerBboxC = bboxM.get(playerE);
		Rectangle playerBbox = playerBboxC.bbox;
		for (Entity spawnPointE : engine.getEntitiesFor(Family.one(SpawnPointBoundingBoxComponent.class).get())) {
			SpawnPointComponent spawnPointC = spawnPointM.get(spawnPointE);
			if (Objects.equals(playerSpawnPointC.name, spawnPointC.name)) {
				SpawnPointBoundingBoxComponent spawnPointBboxC = spawnPointbboxM.get(spawnPointE);
				Rectangle spawnPointBbox = spawnPointBboxC.bbox;

				playerBbox.x = spawnPointBbox.x;
				playerBbox.y = spawnPointBbox.y;
			}
		}

		return playerE;
	}
}
