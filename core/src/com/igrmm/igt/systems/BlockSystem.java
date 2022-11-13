package com.igrmm.igt.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Pools;
import com.igrmm.igt.CollisionMaybe;
import com.igrmm.igt.components.BoundingBoxComponent;
import com.igrmm.igt.components.BroadPhaseCollisionComponent;
import com.igrmm.igt.components.MovementComponent;
import com.igrmm.igt.factories.BlockEntityFactory;

public class BlockSystem extends IteratingSystem {
	private final ComponentMapper<MovementComponent> movementM;
	private final ComponentMapper<BoundingBoxComponent> bboxM;
	private final ComponentMapper<BroadPhaseCollisionComponent> bPhaseColM;
	private final Rectangle collisionArea;
	private final Rectangle activeZone;
	private final Array<Entity> filteredBlockEntities;
	private final Rectangle playerBbox;
	private ImmutableArray<Entity> blockEntities;

	public BlockSystem(Entity playerE) {
		super(Family.all(MovementComponent.class, BroadPhaseCollisionComponent.class).get());
		movementM = ComponentMapper.getFor(MovementComponent.class);
		bboxM = ComponentMapper.getFor(BoundingBoxComponent.class);
		bPhaseColM = ComponentMapper.getFor(BroadPhaseCollisionComponent.class);
		collisionArea = new Rectangle();
		activeZone = new Rectangle();
		filteredBlockEntities = new Array<>();
		playerBbox = bboxM.get(playerE).bbox;
	}

	@Override
	public void addedToEngine(Engine engine) {
		super.addedToEngine(engine);
		blockEntities = engine.getEntitiesFor(Family.all(BlockEntityFactory.BlockETComponent.class).get());
	}

	private void computeCollisionArea(Rectangle dynamicRect, Vector2 speed) {
		if (speed.x > 0.0f) {
			collisionArea.x = dynamicRect.x;
			collisionArea.width = dynamicRect.width + speed.x;
		} else {
			collisionArea.x = dynamicRect.x + speed.x;
			collisionArea.width = dynamicRect.width - speed.x;
		}

		if (speed.y > 0.0f) {
			collisionArea.y = dynamicRect.y;
			collisionArea.height = dynamicRect.height + speed.y;
		} else {
			collisionArea.y = dynamicRect.y + speed.y;
			collisionArea.height = dynamicRect.height - speed.y;
		}
	}

	private void filterBoundingBoxes() {
		filteredBlockEntities.clear();

		float screenWidth = Gdx.graphics.getWidth();
		float screenHeight = Gdx.graphics.getHeight();
		activeZone.set(
				playerBbox.x + playerBbox.width / 2f - screenWidth / 2f,
				playerBbox.y + playerBbox.height / 2f - screenHeight / 2f,
				screenWidth,
				screenHeight
		);

		for (Entity blockE : blockEntities) {
			Rectangle blockBbox = bboxM.get(blockE).bbox;
			if (activeZone.overlaps(blockBbox))
				filteredBlockEntities.add(blockE);
		}
	}

	@Override
	public void update(float deltaTime) {
		filterBoundingBoxes();
		super.update(deltaTime);
	}

	@Override
	protected void processEntity(Entity entity, float deltaTime) {
		MovementComponent movC = movementM.get(entity);
		if (movC.speed.x != 0 || movC.speed.y != 0) {
			BroadPhaseCollisionComponent bPhaseColC = bPhaseColM.get(entity);
			BoundingBoxComponent dynamicBboxC = bboxM.get(entity);

			for (Entity blockE : filteredBlockEntities) {
				BoundingBoxComponent blockBboxC = bboxM.get(blockE);
				Rectangle blockBbox = blockBboxC.bbox;

				computeCollisionArea(dynamicBboxC.bbox, movC.speed);

				if (collisionArea.overlaps(blockBbox)) {
					BlockCollision collision = Pools.obtain(BlockCollision.class);
					collision.init(movC, entity, blockE);
					bPhaseColC.collisions.add(collision);
				}
			}
		}
	}

	private static class BlockCollision extends CollisionMaybe {
		MovementComponent movC;

		public void init(MovementComponent movC, Entity dynamicEntity, Entity staticEntity) {
			super.init(true, dynamicEntity, staticEntity);
			this.movC = movC;
		}

		@Override
		public void reset() {
			super.reset();
			movC = null;
		}

		@Override
		public boolean handle() {
			if (super.handle()) {
				if (getNormalY() > 0) {
					movC.grounded = true;
				}
				return true;
			} else return false;
		}
	}
}
