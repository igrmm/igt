package com.igrmm.igt.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.igrmm.igt.Igt;
import com.igrmm.igt.components.MovementComponent;
import com.igrmm.igt.factories.PlayerFactory.PlayerETComponent;
import com.igrmm.igt.screens.PauseScreen;

public class PlayerSystem extends EntitySystem implements Disposable {
	private final Igt game;
	private final PlayerETComponent playerETC;
	private final MovementComponent playerMovC;
	private final Stage stage;
	private boolean rightPressed = false;
	private boolean leftPressed = false;
	private boolean pausePressed = false;

	public PlayerSystem(Entity playerE, Igt game) {
		this.game = game;
		playerETC = playerE.getComponent(PlayerETComponent.class);
		playerMovC = playerE.getComponent(MovementComponent.class);
		stage = new Stage(new ScreenViewport());
		Gdx.input.setInputProcessor(stage);
		stage.addListener(new InputListener() {
			public boolean keyDown(InputEvent event, int keycode) {
				if (keycode == playerETC.leftKey) setLeftInput(true);
				if (keycode == playerETC.rightKey) setRightInput(true);
				if (keycode == playerETC.jumpKey) setJumpInput(true);
				if (keycode == Input.Keys.ESCAPE) setPauseInput(true);
				return true;
			}

			public boolean keyUp(InputEvent event, int keycode) {
				if (keycode == playerETC.leftKey) setLeftInput(false);
				if (keycode == playerETC.rightKey) setRightInput(false);
				if (keycode == playerETC.jumpKey) setJumpInput(false);
				if (keycode == Input.Keys.ESCAPE) setPauseInput(false);
				return true;
			}
		});

		//add on screen controller
		Skin skin = game.assets.getSkin();
		Table table = new Table();
		table.setFillParent(true);
		table.bottom();
		addOnScreenController(table, skin);
		stage.addActor(table);
	}

	@Override
	public void update(float deltaTime) {
		stage.act();
		stage.draw();

		//track time played
		playerETC.timePlayed += deltaTime;
	}

	public void resizeScreen(int width, int height) {
		stage.getViewport().update(width, height, true);
	}

	@Override
	public void dispose() {
		stage.dispose();
	}

	private boolean setLeftInput(boolean pressed) {
		leftPressed = pressed;
		if (leftPressed) {
			playerMovC.movementSignalIntention =
					playerMovC.movementSignalIntention > 0 ? 0 : MovementComponent.LEFT_SIGNAL;
		} else {
			playerMovC.movementSignalIntention = rightPressed ? MovementComponent.RIGHT_SIGNAL : 0;
		}
		return leftPressed;
	}

	private boolean setRightInput(boolean pressed) {
		rightPressed = pressed;
		if (rightPressed) {
			playerMovC.movementSignalIntention =
					playerMovC.movementSignalIntention < 0 ? 0 : MovementComponent.RIGHT_SIGNAL;
		} else {
			playerMovC.movementSignalIntention = leftPressed ? MovementComponent.LEFT_SIGNAL : 0;
		}
		return rightPressed;
	}

	public void setPauseInput(boolean pressed) {
		if (pressed) {
			pausePressed = true;
		} else if (pausePressed) {
			setRightInput(setLeftInput(setJumpInput(pausePressed = false)));
			game.setScreen(new PauseScreen(game, stage));
		}
	}

	public boolean setJumpInput(boolean pressed) {
		return playerMovC.jumpIntention = pressed;
	}

	private void addOnScreenController(Table table, Skin skin) {
		//set button size in centimeters
		float buttonSizeCm = 1.5f;
		float pixelSizeCm = 2.0f / (Gdx.graphics.getPpcX() + Gdx.graphics.getPpcY());
		float buttonSize = buttonSizeCm / pixelSizeCm;

		//set padding
		float buttonPad = 3.0f * Gdx.graphics.getWidth() / 100.0f;

		GameButton leftButton = new GameButton(skin, "left-button");
		table.add(leftButton).width(buttonSize).height(buttonSize).pad(buttonPad);
		leftButton.addListener(new InputListener() {
			public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
				setLeftInput(leftButton.over = true);
			}

			public void exit(InputEvent event, float x, float y, int pointer, Actor toActor) {
				setLeftInput(leftButton.over = false);
			}
		});

		GameButton rightButton = new GameButton(skin, "right-button");
		table.add(rightButton).width(buttonSize).height(buttonSize);
		rightButton.addListener(new InputListener() {
			public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
				setRightInput(rightButton.over = true);
			}

			public void exit(InputEvent event, float x, float y, int pointer, Actor toActor) {
				setRightInput(rightButton.over = false);
			}
		});

		// empty cell
		table.add(new Actor()).expandX();

		GameButton actionButton = new GameButton(skin, "action-button");
		table.add(actionButton).width(buttonSize).height(buttonSize);
		actionButton.addListener(new InputListener() {
			public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
				actionButton.over = true;
			}

			public void exit(InputEvent event, float x, float y, int pointer, Actor toActor) {
				actionButton.over = false;
			}
		});

		GameButton jumpButton = new GameButton(skin, "jump-button");
		table.add(jumpButton).width(buttonSize).height(buttonSize).pad(buttonPad);
		jumpButton.addListener(new InputListener() {
			public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
				setJumpInput(jumpButton.over = true);
			}

			public void exit(InputEvent event, float x, float y, int pointer, Actor toActor) {
				setJumpInput(jumpButton.over = false);
			}
		});
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
