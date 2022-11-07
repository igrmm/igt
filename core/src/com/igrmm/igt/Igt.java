package com.igrmm.igt;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.utils.ScreenUtils;
import com.igrmm.igt.screens.GameScreen;
import com.igrmm.igt.screens.LoadingScreen;

public class Igt extends Game {
	public final Assets assets = new Assets();
	private GameScreen gameScreen;

	@Override
	public void create() {
		setScreen(new LoadingScreen(this));
	}

	@Override
	public void dispose() {
		assets.flushSave();
		if (this.screen != null) this.screen.dispose();
		assets.dispose();
	}

	@Override
	public void render() {
		ScreenUtils.clear(0, 0, 0, 1);
		super.render();
	}

	public void setGameScreen(GameScreen gameScreen) {
		this.gameScreen = gameScreen;
		restoreGameScreen();
	}

	public void restoreGameScreen() {
		setScreen(gameScreen);
	}
}
