package com.igrmm.igt.screens;

import com.badlogic.gdx.ScreenAdapter;
import com.igrmm.igt.Igt;

import java.io.PrintWriter;
import java.io.StringWriter;

public class LoadingScreen extends ScreenAdapter {
	private final Igt game;

	public LoadingScreen(Igt game) {
		this.game = game;
	}

	@Override
	public void show() {
		try {
			game.assets.loadAll();
			game.setScreen(GameScreen.createGameScreen(game));
		} catch (Exception exception) {
			exception.printStackTrace();
			StringWriter error = new StringWriter();
			exception.printStackTrace(new PrintWriter(error));
			game.setScreen(new ErrorScreen(error.toString()));
		}
	}
}
