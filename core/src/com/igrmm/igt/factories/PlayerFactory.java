package com.igrmm.igt.factories;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
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

		//default components
		BoundingBoxComponent playerBboxC = new BoundingBoxComponent();
		MovementComponent playerMovC = new MovementComponent();
		DebugComponent playerDebugC = new DebugComponent();
		StageComponent playerStageC = new StageComponent();
		AnimationComponent playerAnimationC = new AnimationComponent();
		BroadPhaseCollisionComponent playerBphaseColC = new BroadPhaseCollisionComponent();

		//serializable components
		PlayerETComponent playerETC = save.playerETC;
		SpawnPointComponent playerSpawnPointC = save.spawnPointC;
		MapComponent playerMapC = save.mapC;

		//configure components
		playerBboxC.bbox.setSize(32f);
		playerMovC.maxSpeed = 240f;
		playerMovC.acceleration = 1080f;
		playerMovC.friction = 1080f;
		playerMovC.gravity = -1800.0f;
		playerMovC.jumpForce = 450.0f;
		playerMovC.jumpTime = 0.2f;
		Label.LabelStyle labelStyle = new Label.LabelStyle();
		labelStyle.font = assets.getFont("dogicapixel");
		playerDebugC.debugLabel.setStyle(labelStyle);
		playerStageC.stage.setViewport(new ScreenViewport());
		playerAnimationC.offset = 16f;
		AsepriteAnimation asepriteAnimation = assets.getAsepriteAnimation("player");
		for (String animationName : asepriteAnimation.names) {
			float duration = asepriteAnimation.durations.get(animationName);
			TextureRegion[] textureRegions = asepriteAnimation.textureRegions.get(animationName);
			playerAnimationC.animations.put(animationName, new Animation<>(duration, textureRegions));
		}
		playerAnimationC.currentAnimation = "idle_right";

		//add components
		playerE.add(playerBboxC);
		playerE.add(playerMovC);
		playerE.add(playerDebugC);
		playerE.add(playerStageC);
		playerE.add(playerAnimationC);
		playerE.add(playerBphaseColC);
		playerE.add(playerETC);
		playerE.add(playerSpawnPointC);
		playerE.add(playerMapC);

		//make player spawn at saved spawn point
		ComponentMapper<SpawnPointComponent> spawnPointM = ComponentMapper.getFor(SpawnPointComponent.class);
		ComponentMapper<BoundingBoxComponent> bboxM = ComponentMapper.getFor(BoundingBoxComponent.class);
		for (Entity spawnPointE : engine.getEntitiesFor(Family.one(SpawnPointEntityFactory.SpawnPointETComponent.class).get())) {
			SpawnPointComponent spawnPointC = spawnPointM.get(spawnPointE);
			if (Objects.equals(playerSpawnPointC.name, spawnPointC.name)) {
				BoundingBoxComponent spawnPointBboxC = bboxM.get(spawnPointE);
				Rectangle spawnPointBbox = spawnPointBboxC.bbox;

				playerBboxC.bbox.x = spawnPointBbox.x;
				playerBboxC.bbox.y = spawnPointBbox.y;
			}
		}

		return playerE;
	}

	public static class PlayerETComponent extends EntityTypeComponent {
		public static final int LEFT_KEY_INDEX = 0;
		public static final int RIGHT_KEY_INDEX = 1;
		public static final int JUMP_KEY_INDEX = 2;
		public final int[] keyBindings = {Input.Keys.A, Input.Keys.D, Input.Keys.SPACE};
		public float timePlayed = 0f;
	}
}
