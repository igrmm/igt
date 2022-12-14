package com.igrmm.igt;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGeneratorLoader;
import com.badlogic.gdx.graphics.g2d.freetype.FreetypeFontLoader;
import com.badlogic.gdx.graphics.g2d.freetype.FreetypeFontLoader.FreeTypeFontLoaderParameter;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonValue;

import java.util.HashMap;
import java.util.Map;

public class Assets {
	public static final String MAPS_DIR = "tiled/maps/";
	public static final String TEXTURES_DIR = "textures/";
	public static final String ANIMATIONS_DIR = "animations/";
	public static final String FONTS_DIR = "fonts/";
	public static final String SAVE_PATH = "save.json";

	private final AssetManager assetManager;
	private final Map<String, AsepriteAnimation> asepriteAnimations;

	private Save save = new Save();

	public Assets() {
		assetManager = new AssetManager();
		assetManager.setLoader(TiledMap.class, new IgtTmxMapLoader(new InternalFileHandleResolver()));
		assetManager.setLoader(JsonValue.class, new JsonLoader(new InternalFileHandleResolver()));
		assetManager.setLoader(FreeTypeFontGenerator.class, new FreeTypeFontGeneratorLoader(new InternalFileHandleResolver()));
		assetManager.setLoader(BitmapFont.class, new FreetypeFontLoader(new InternalFileHandleResolver()));
		asepriteAnimations = new HashMap<>();
	}

	public void loadAll() {
		//load save file
		FileHandle saveFileHandle = Gdx.files.local(SAVE_PATH);
		if (saveFileHandle.exists()) assetManager.load(SAVE_PATH, JsonValue.class);

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

		//load fonts
		FileHandle fontsDirHandle = Gdx.files.internal(FONTS_DIR);
		for (FileHandle fontFileHandle : fontsDirHandle.list()) {
			FreeTypeFontLoaderParameter parms = new FreeTypeFontLoaderParameter();
			parms.fontFileName = fontFileHandle.path();
			parms.fontParameters.size = 14;
			assetManager.load(fontFileHandle.path(), BitmapFont.class, parms);
		}

		//load ui skin
		assetManager.load("ui/uiskin.json", Skin.class);

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

	/**
	 * @param fontName file name only, without extension.
	 */
	public BitmapFont getFont(String fontName) {
		return assetManager.get(FONTS_DIR + fontName + ".ttf", BitmapFont.class);
	}

	public Skin getSkin() {
		return assetManager.get("ui/uiskin.json", Skin.class);
	}

	public Save getSave() {
		if (assetManager.contains(SAVE_PATH)) {
			JsonValue saveJson = assetManager.get(SAVE_PATH, JsonValue.class);
			save = new Json().fromJson(Save.class, saveJson.toString());
		}
		return save;
	}

	public void flushSave() {
		String saveString = new Json().prettyPrint(save);
		FileHandle saveFileHandle = Gdx.files.local(SAVE_PATH);
		saveFileHandle.writeString(saveString, false);
		if (saveFileHandle.exists()) {
			if (assetManager.contains(SAVE_PATH)) assetManager.unload(SAVE_PATH);
			assetManager.load(SAVE_PATH, JsonValue.class);
			assetManager.finishLoadingAsset(SAVE_PATH);
		}
	}

	public void dispose() {
		assetManager.dispose();
	}
}
