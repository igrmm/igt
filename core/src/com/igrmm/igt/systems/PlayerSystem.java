package com.igrmm.igt.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.igrmm.igt.Assets;
import com.igrmm.igt.components.MovementComponent;
import com.igrmm.igt.factories.PlayerFactory.PlayerETComponent;

public class PlayerSystem extends EntitySystem implements Disposable {
	private final PlayerETComponent playerETC;
	private final MovementComponent playerMovC;
	private final Stage stage;
	private boolean rightPressed = false;
	private boolean leftPressed = false;

	public PlayerSystem(Entity playerE, Assets assets) {
		ComponentMapper<PlayerETComponent> playerETM = ComponentMapper.getFor(PlayerETComponent.class);
		playerETC = playerETM.get(playerE);
		playerMovC = playerE.getComponent(MovementComponent.class);
		stage = new Stage(new ScreenViewport());
		Gdx.input.setInputProcessor(stage);
		stage.addListener(new InputListener() {
			public boolean keyDown(InputEvent event, int keycode) {
				if (keycode == playerETC.leftKey) setLeftInput(true);
				if (keycode == playerETC.rightKey) setRightInput(true);
				return true;
			}

			public boolean keyUp(InputEvent event, int keycode) {
				if (keycode == playerETC.leftKey) setLeftInput(false);
				if (keycode == playerETC.rightKey) setRightInput(false);
				return true;
			}
		});

		//add on screen controller
		Skin skin = assets.getSkin();
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
				leftButton.over = setLeftInput(true);
			}

			public void exit(InputEvent event, float x, float y, int pointer, Actor toActor) {
				leftButton.over = setLeftInput(false);
			}
		});

		GameButton rightButton = new GameButton(skin, "right-button");
		table.add(rightButton).width(buttonSize).height(buttonSize);
		rightButton.addListener(new InputListener() {
			public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
				rightButton.over = setRightInput(true);
			}

			public void exit(InputEvent event, float x, float y, int pointer, Actor toActor) {
				rightButton.over = setRightInput(false);
			}
		});

		// empty cell
		table.add(new Actor()).expandX();
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
