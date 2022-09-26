package com.igrmm.igt.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;
import com.igrmm.igt.components.BoundingBoxComponent;
import com.igrmm.igt.components.TextureComponent;

public class RenderingSystem extends IteratingSystem implements Disposable {
	private final SpriteBatch batch;
	private final OrthographicCamera camera;
	private final Array<Entity> renderQueue;
	private final ComponentMapper<TextureComponent> textureM;
	private final ComponentMapper<BoundingBoxComponent> bBoxM;
	Texture bg;

	public RenderingSystem(Texture bg) {
		super(Family.all(BoundingBoxComponent.class, TextureComponent.class).get());
		batch = new SpriteBatch();
		camera = new OrthographicCamera();
		renderQueue = new Array<>();
		textureM = ComponentMapper.getFor(TextureComponent.class);
		bBoxM = ComponentMapper.getFor(BoundingBoxComponent.class);
		camera.position.x = 16f;
		camera.position.y = 16f;
		this.bg = bg;
	}

	@Override
	public void update(float deltaTime) {
		super.update(deltaTime);
		camera.update();
		batch.setProjectionMatrix(camera.combined);
		batch.begin();
		batch.draw(bg, -bg.getWidth() / 2f + 16f, -bg.getHeight() / 2f + 16f);
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
	}
}
