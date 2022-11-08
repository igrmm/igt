package com.igrmm.igt.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.igrmm.igt.Igt;

public class PauseScreen extends ScreenAdapter {
	private final Igt game;
	private final Stage userInterfaceStage;
	private final Stage stage;
	private boolean pausePressed = false;

	public PauseScreen(Igt game, Stage userInterfaceStage) {
		this.game = game;
		this.userInterfaceStage = userInterfaceStage;
		stage = new Stage(new ScreenViewport());
		Table table = new Table();
		table.setFillParent(true);
		table.center();
		Label.LabelStyle labelStyle = new Label.LabelStyle();
		labelStyle.font = game.assets.getFont("dogicapixel");
		Label label = new Label("PAUSE", labelStyle);
		table.add(label);
		stage.addActor(table);
		Gdx.input.setInputProcessor(stage);
		stage.addListener(new InputListener() {
			@Override
			public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
				setPauseInput(true);
				return true;
			}

			@Override
			public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
				setPauseInput(false);
			}

			@Override
			public boolean keyDown(InputEvent event, int keycode) {
				if (keycode == Input.Keys.ESCAPE) setPauseInput(true);
				return true;
			}

			@Override
			public boolean keyUp(InputEvent event, int keycode) {
				if (keycode == Input.Keys.ESCAPE) setPauseInput(false);
				return true;
			}
		});
	}

	@Override
	public void render(float delta) {
		stage.act();
		stage.draw();
	}

	@Override
	public void resize(int width, int height) {
		stage.getViewport().update(width, height, true);
	}

	@Override
	public void dispose() {
		stage.dispose();
	}

	public void setPauseInput(boolean pressed) {
		if (pressed) {
			pausePressed = true;
		} else if (pausePressed) {
			pausePressed = false;
			Gdx.input.setInputProcessor(userInterfaceStage);
			game.restoreGameScreen();
		}
	}
}
