package com.igrmm.igt;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.JsonValue;

import java.util.HashMap;
import java.util.Map;

public class Assets {
	public static final String MAPS_DIR = "tiled/maps/";
	public static final String TEXTURES_DIR = "textures/";
	public static final String ANIMATIONS_DIR = "animations/";

	private final AssetManager assetManager;
	private final Map<String, AsepriteAnimation> asepriteAnimations;

	public Assets() {
		assetManager = new AssetManager();
		assetManager.setLoader(TiledMap.class, new IgtTmxMapLoader(new InternalFileHandleResolver()));
		assetManager.setLoader(JsonValue.class, new JsonLoader(new InternalFileHandleResolver()));
		asepriteAnimations = new HashMap<>();
	}

	public void loadAll() {
		//load tiled maps
		FileHandle mapsDirHandle = Gdx.files.internal(MAPS_DIR);
		for (FileHandle mapFileHandle : mapsDirHandle.list()) {
			assetManager.load(mapFileHandle.path(), TiledMap.class);
		}

		//load textures
		FileHandle texturesDirHandle = Gdx.files.internal(TEXTURES_DIR);
		for (FileHandle textureFileHandle : texturesDirHandle.list()) {
			assetManager.load(textureFileHandle.path(), Texture.class);
		}

		//load animations json
		Array<String> animationJsonPaths = new Array<>();
		FileHandle animationsDirHandle = Gdx.files.internal(ANIMATIONS_DIR);
		for (FileHandle animationFileHandle : animationsDirHandle.list()) {
			assetManager.load(animationFileHandle.path(), JsonValue.class);
			animationJsonPaths.add(animationFileHandle.path());
		}

		assetManager.finishLoading();

		//make animations from json
		for (String animationJsonPath : animationJsonPaths) {
			JsonValue animationJson = assetManager.get(animationJsonPath, JsonValue.class);
			String texturePath = TEXTURES_DIR + animationJson.get("meta").getString("image");
			Texture animationTexture = assetManager.get(texturePath, Texture.class);
			asepriteAnimations.put(animationJsonPath, new AsepriteAnimation(animationJson, animationTexture));
		}
	}

	/**
	 * @param tiledMapName file name only, without extension.
	 */
	public TiledMap getTiledMap(String tiledMapName) {
		return assetManager.get(MAPS_DIR + tiledMapName + ".tmx", TiledMap.class);
	}

	/**
	 * @param asepriteAnimationName file name only, without extension.
	 */
	public AsepriteAnimation getAsepriteAnimation(String asepriteAnimationName) {
		return asepriteAnimations.get(ANIMATIONS_DIR + asepriteAnimationName + ".json");
	}

	public void dispose() {
		assetManager.dispose();
	}
}
