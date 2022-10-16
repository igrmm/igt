package com.igrmm.igt;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.utils.ScreenUtils;
import com.igrmm.igt.screens.GameScreen;

public class Igt extends Game {
	public final Assets assets = new Assets();

	@Override
	public void create() {
		assets.loadAll();
		setScreen(new GameScreen(this));
	}

	@Override
	public void dispose() {
		if (this.screen != null) this.screen.dispose();
		assets.dispose();
	}

	@Override
	public void render() {
		ScreenUtils.clear(0, 0, 0, 1);
		super.render();
	}
}
