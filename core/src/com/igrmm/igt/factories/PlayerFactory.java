package com.igrmm.igt.factories;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.Rectangle;
import com.igrmm.igt.AsepriteAnimation;
import com.igrmm.igt.Assets;
import com.igrmm.igt.Save;
import com.igrmm.igt.components.*;

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

		//components dependencies
		AsepriteAnimation asepriteAnimation = assets.getAsepriteAnimation("player");

		//default components
		playerE.add(new BoundingBoxComponent());
		playerE.add(new AnimationComponent(asepriteAnimation));
		playerE.add(new MovementComponent());
		playerE.add(new BroadPhaseCollisionComponent());

		//serializable components
		playerE.add(save.playerETC);
		playerE.add(save.spawnPointC);
		playerE.add(save.mapC);

		//tweak numbers
		MovementComponent playerMovC = movementM.get(playerE);
		AnimationComponent playerAnimationC = animationM.get(playerE);
		BoundingBoxComponent playerBboxC = bboxM.get(playerE);
		Rectangle playerBbox = playerBboxC.bbox;

		playerBbox.width = playerBbox.height = 32f;
		playerAnimationC.currentAnimation = "idle_right";
		playerAnimationC.offset = 16f;
		playerMovC.maxSpeed = 240f;
		playerMovC.acceleration = 1080f;
		playerMovC.friction = 1080f;
		playerMovC.gravity = -1800.0f;
		playerMovC.jumpForce = 450.0f;
		playerMovC.jumpTime = 0.2f;

		//make player spawn at saved spawn point
		SpawnPointComponent playerSpawnPointC = spawnPointM.get(playerE);
		for (Entity spawnPointE : engine.getEntitiesFor(Family.one(SpawnPointEntityFactory.SpawnPointETComponent.class).get())) {
			SpawnPointComponent spawnPointC = spawnPointM.get(spawnPointE);
			if (Objects.equals(playerSpawnPointC.name, spawnPointC.name)) {
				BoundingBoxComponent spawnPointBboxC = bboxM.get(spawnPointE);
				Rectangle spawnPointBbox = spawnPointBboxC.bbox;

				playerBbox.x = spawnPointBbox.x;
				playerBbox.y = spawnPointBbox.y;
			}
		}

		return playerE;
	}

	public static class PlayerETComponent extends EntityTypeComponent {
		public static final int LEFT_KEY_INDEX = 0;
		public static final int RIGHT_KEY_INDEX = 1;
		public static final int JUMP_KEY_INDEX = 2;
		public final int[] keys = {Input.Keys.A, Input.Keys.D, Input.Keys.SPACE};
		public float timePlayed = 0f;
	}
}
