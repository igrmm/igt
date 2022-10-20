package com.igrmm.igt.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;
import com.igrmm.igt.components.AnimationComponent;
import com.igrmm.igt.components.boundingboxes.BoundingBoxComponent;

public class RenderingSystem extends IteratingSystem implements Disposable {
	private final SpriteBatch batch;
	private final OrthographicCamera camera;
	private final OrthogonalTiledMapRenderer mapRenderer;
	private final Array<Entity> renderQueue;
	private final ComponentMapper<AnimationComponent> animationM;
	private final ComponentMapper<BoundingBoxComponent> bBoxM;

	public RenderingSystem(TiledMap tiledMap) {
		super(Family.all(BoundingBoxComponent.class, AnimationComponent.class).get());
		batch = new SpriteBatch();
		camera = new OrthographicCamera();
		mapRenderer = new OrthogonalTiledMapRenderer(tiledMap);
		renderQueue = new Array<>();
		animationM = ComponentMapper.getFor(AnimationComponent.class);
		bBoxM = ComponentMapper.getFor(BoundingBoxComponent.class);
		camera.position.x = 16f;
		camera.position.y = 16f;
	}

	@Override
	public void update(float deltaTime) {
		super.update(deltaTime);
		camera.update();
		mapRenderer.setView(camera);
		mapRenderer.render();
		batch.setProjectionMatrix(camera.combined);
		batch.begin();
		for (Entity entity : renderQueue) {
			AnimationComponent animationC = animationM.get(entity);
			String currentAnimation = animationC.currentAnimation;
			float offset = animationC.offset;
			animationC.stateTime += deltaTime;
			TextureRegion tex = animationC.animations.get(currentAnimation).getKeyFrame(animationC.stateTime, true);
			BoundingBoxComponent bBoxC = bBoxM.get(entity);
			batch.draw(tex, bBoxC.bBox.x - offset, bBoxC.bBox.y - offset);
		}
		batch.end();
		renderQueue.clear();
	}

	@Override
	protected void processEntity(Entity entity, float deltaTime) {
		renderQueue.add(entity);
	}

	public void resizeScreen(int width, int height) {
		camera.viewportHeight = height;
		camera.viewportWidth = width;
	}

	@Override
	public void dispose() {
		batch.dispose();
		mapRenderer.dispose();
	}
}
