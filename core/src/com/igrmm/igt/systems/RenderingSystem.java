package com.igrmm.igt.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;
import com.igrmm.igt.components.BoundingBoxComponent;
import com.igrmm.igt.components.TextureComponent;

public class RenderingSystem extends IteratingSystem implements Disposable {
	private final SpriteBatch batch;
	private final OrthographicCamera camera;
	private final OrthogonalTiledMapRenderer mapRenderer;
	private final Array<Entity> renderQueue;
	private final ComponentMapper<TextureComponent> textureM;
	private final ComponentMapper<BoundingBoxComponent> bBoxM;

	public RenderingSystem(TiledMap tiledMap) {
		super(Family.all(BoundingBoxComponent.class, TextureComponent.class).get());
		batch = new SpriteBatch();
		camera = new OrthographicCamera();
		mapRenderer = new OrthogonalTiledMapRenderer(tiledMap);
		renderQueue = new Array<>();
		textureM = ComponentMapper.getFor(TextureComponent.class);
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
			TextureComponent textureC = textureM.get(entity);
			BoundingBoxComponent bBoxC = bBoxM.get(entity);
			batch.draw(textureC.texture, bBoxC.bBox.x, 0);
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
