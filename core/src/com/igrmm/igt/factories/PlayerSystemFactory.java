package com.igrmm.igt.factories;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.igrmm.igt.Igt;
import com.igrmm.igt.components.DebugComponent;
import com.igrmm.igt.components.StageComponent;
import com.igrmm.igt.systems.PlayerSystem;

public class PlayerSystemFactory {
	public static PlayerSystem createPlayerSystem(Entity playerE, Igt game) {
		PlayerSystem playerSystem = new PlayerSystem(playerE, game);

		Stage stage = playerE.getComponent(StageComponent.class).stage;
		Gdx.input.setInputProcessor(stage);

		/*
		  MAKE GUI
		  |-------------|
		  | upper table |
		  |-------------|
		  | empty cell  |
		  |-------------|
		  | lower table |
		  |-------------|
		*/

		Table root = new Table();
		stage.addActor(root);
		root.setFillParent(true);
		Skin skin = game.assets.getSkin();

		//upper table
		Table upperTable = new Table();
		root.add(upperTable).fill();
		Label debugLabel = playerE.getComponent(DebugComponent.class).debugLabel;
		upperTable.add(debugLabel).pad(wpToPx(3));
		upperTable.add(new Actor()).expandX();
		Button pauseButton = new Button(skin, "pause-button");
		pauseButton.addListener(new InputListener() {
			@Override
			public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
				playerSystem.setPauseInput(true);
				return true;
			}

			@Override
			public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
				playerSystem.setPauseInput(false);
			}
		});
		upperTable.add(pauseButton).size(cmToPx(1.5f)).pad(wpToPx(3)).top();
		root.row();

		//empty cell
		root.add(new Actor()).expand(); //set all cells to max size, pushes things to edges with fill() method
		root.row();

		//lower table (VIRTUAL CONTROLLER)
		float buttonSize = cmToPx(1.5f);
		float buttonPad = wpToPx(3);
		Table lowerTable = new Table();
		root.add(lowerTable).fill();

		GameButton leftButton = new GameButton(skin, "left-button");
		lowerTable.add(leftButton).width(buttonSize).height(buttonSize).pad(buttonPad);
		leftButton.addListener(new InputListener() {
			public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
				playerSystem.setLeftInput(leftButton.over = true);
			}

			public void exit(InputEvent event, float x, float y, int pointer, Actor toActor) {
				playerSystem.setLeftInput(leftButton.over = false);
			}
		});

		GameButton rightButton = new GameButton(skin, "right-button");
		lowerTable.add(rightButton).width(buttonSize).height(buttonSize);
		rightButton.addListener(new InputListener() {
			public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
				playerSystem.setRightInput(rightButton.over = true);
			}

			public void exit(InputEvent event, float x, float y, int pointer, Actor toActor) {
				playerSystem.setRightInput(rightButton.over = false);
			}
		});

		lowerTable.add(new Actor()).expandX();

		GameButton actionButton = new GameButton(skin, "action-button");
		lowerTable.add(actionButton).width(buttonSize).height(buttonSize);
		actionButton.addListener(new InputListener() {
			public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
				actionButton.over = true;
			}

			public void exit(InputEvent event, float x, float y, int pointer, Actor toActor) {
				actionButton.over = false;
			}
		});

		GameButton jumpButton = new GameButton(skin, "jump-button");
		lowerTable.add(jumpButton).width(buttonSize).height(buttonSize).pad(buttonPad);
		jumpButton.addListener(new InputListener() {
			public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
				playerSystem.setJumpInput(jumpButton.over = true);
			}

			public void exit(InputEvent event, float x, float y, int pointer, Actor toActor) {
				playerSystem.setJumpInput(jumpButton.over = false);
			}
		});

		//install keyboard
		int[] keys = playerE.getComponent(PlayerFactory.PlayerETComponent.class).keyBindings;
		int leftKey = keys[PlayerFactory.PlayerETComponent.LEFT_KEY_INDEX];
		int rightKey = keys[PlayerFactory.PlayerETComponent.RIGHT_KEY_INDEX];
		int jumpKey = keys[PlayerFactory.PlayerETComponent.JUMP_KEY_INDEX];
		stage.addListener(new InputListener() {
			public boolean keyDown(InputEvent event, int keycode) {
				if (keycode == leftKey) playerSystem.setLeftInput(true);
				if (keycode == rightKey) playerSystem.setRightInput(true);
				if (keycode == jumpKey) playerSystem.setJumpInput(true);
				if (keycode == Input.Keys.ESCAPE) playerSystem.setPauseInput(true);
				return true;
			}

			public boolean keyUp(InputEvent event, int keycode) {
				if (keycode == leftKey) playerSystem.setLeftInput(false);
				if (keycode == rightKey) playerSystem.setRightInput(false);
				if (keycode == jumpKey) playerSystem.setJumpInput(false);
				if (keycode == Input.Keys.ESCAPE) playerSystem.setPauseInput(false);
				return true;
			}
		});

		return playerSystem;
	}

	/**
	 * @param widthPercentage desired value in screen width percentage.
	 * @return the given screen width percentage converted to pixels.
	 */
	public static float wpToPx(int widthPercentage) {
		return widthPercentage * Gdx.graphics.getWidth() / 100f;
	}

	/**
	 * @param centimeters desired value in centimeters.
	 * @return the given value in centimeters converted to pixels.
	 */
	public static float cmToPx(float centimeters) {
		return centimeters / (2f / (Gdx.graphics.getPpcX() + Gdx.graphics.getPpcY()));
	}

	private static class GameButton extends Button {
		boolean over;

		GameButton(Skin skin, String styleName) {
			super(skin, styleName);
		}

		@Override
		public boolean isOver() {
			return over;
		}
	}
}
