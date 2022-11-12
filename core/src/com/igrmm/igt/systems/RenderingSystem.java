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
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Disposable;
import com.igrmm.igt.components.AnimationComponent;
import com.igrmm.igt.components.BoundingBoxComponent;
import com.igrmm.igt.components.StageComponent;

public class RenderingSystem extends IteratingSystem implements Disposable {
	private final SpriteBatch batch;
	private final OrthographicCamera camera;
	private final OrthogonalTiledMapRenderer mapRenderer;
	private final ComponentMapper<AnimationComponent> animationM;
	private final ComponentMapper<BoundingBoxComponent> bboxM;
	private final Vector3 cameraTarget;
	private final Vector2 playerCenter;
	private final Rectangle playerBbox;
	private final Stage stage;

	public RenderingSystem(TiledMap tiledMap, Entity playerE) {
		super(Family.all(BoundingBoxComponent.class, AnimationComponent.class).get());
		batch = new SpriteBatch();
		camera = new OrthographicCamera();
		mapRenderer = new OrthogonalTiledMapRenderer(tiledMap);
		animationM = ComponentMapper.getFor(AnimationComponent.class);
		bboxM = ComponentMapper.getFor(BoundingBoxComponent.class);
		camera.position.x = 16f;
		camera.position.y = 16f;
		cameraTarget = new Vector3();
		playerCenter = new Vector2();
		playerBbox = bboxM.get(playerE).bbox;
		stage = playerE.getComponent(StageComponent.class).stage;
	}

	@Override
	public void update(float deltaTime) {
		playerBbox.getCenter(playerCenter);
		cameraTarget.set(playerCenter, 0f);
		camera.position.slerp(cameraTarget, 0.2f);
		camera.update();
		mapRenderer.setView(camera);
		mapRenderer.render();
		batch.setProjectionMatrix(camera.combined);
		batch.begin();
		for (Entity entity : getEntities()) {
			AnimationComponent animationC = animationM.get(entity);
			String currentAnimation = animationC.currentAnimation;
			float offset = animationC.offset;
			animationC.stateTime += deltaTime;
			TextureRegion tex = animationC.animations.get(currentAnimation).getKeyFrame(animationC.stateTime, true);
			BoundingBoxComponent bboxC = bboxM.get(entity);
			batch.draw(tex, bboxC.bbox.x - offset, bboxC.bbox.y - offset);
		}
		batch.end();
		stage.draw();
	}

	@Override
	protected void processEntity(Entity entity, float deltaTime) {
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
