package com.igrmm.igt.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.utils.Disposable;
import com.igrmm.igt.Igt;
import com.igrmm.igt.components.AnimationComponent;
import com.igrmm.igt.components.DebugComponent;
import com.igrmm.igt.components.MovementComponent;
import com.igrmm.igt.components.StageComponent;
import com.igrmm.igt.factories.PlayerFactory.PlayerETComponent;
import com.igrmm.igt.screens.PauseScreen;

import java.util.Objects;

public class PlayerSystem extends EntitySystem implements Disposable {
	private final Igt game;
	private final PlayerETComponent playerETC;
	private final MovementComponent playerMovC;
	private final AnimationComponent playerAnimationC;
	private final Stage stage;
	private final Label debugLabel;

	private boolean rightPressed = false;
	private boolean leftPressed = false;
	private boolean pausePressed = false;

	public PlayerSystem(Entity playerE, Igt game) {
		this.game = game;
		playerETC = playerE.getComponent(PlayerETComponent.class);
		playerMovC = playerE.getComponent(MovementComponent.class);
		playerAnimationC = playerE.getComponent(AnimationComponent.class);
		stage = playerE.getComponent(StageComponent.class).stage;
		debugLabel = playerE.getComponent(DebugComponent.class).debugLabel;
	}

	public void setAnimation(String animation) {
		if (!Objects.equals(playerAnimationC.currentAnimation, animation)) {
			playerAnimationC.currentAnimation = animation;
			playerAnimationC.stateTime = 0f;
		}
	}

	@Override
	public void update(float deltaTime) {

		debugLabel.setText("FPS: " + Gdx.graphics.getFramesPerSecond() +
				"\n\nppcx: " + Gdx.graphics.getPpiX() +
				"\n\nppcy: " + Gdx.graphics.getPpiY() +
				"\n\ndensity: " + Gdx.graphics.getDensity() +
				"\n\n" + Gdx.graphics.getDisplayMode().toString() +
				"\n\nwxh: " + Gdx.graphics.getWidth() + "x" + Gdx.graphics.getHeight() +
				"\n\ndelta: " + deltaTime +
				"\n\nentities: " + getEngine().getEntities().size());

		stage.act();

		//animations
		if (playerMovC.movementSignalIntention != 0) playerMovC.facing = playerMovC.movementSignalIntention;

		if (playerMovC.facing == MovementComponent.RIGHT_SIGNAL) {
			if (!playerMovC.jumping) {
				if (Math.abs(playerMovC.speed.x) > 0.7f * playerMovC.maxSpeed * deltaTime) {
					setAnimation("walk_right");
				} else {
					setAnimation("idle_right");
				}
			} else {
				setAnimation("jump_right");
			}
		}

		if (playerMovC.facing == MovementComponent.LEFT_SIGNAL) {
			if (!playerMovC.jumping) {
				if (Math.abs(playerMovC.speed.x) > 0.7f * playerMovC.maxSpeed * deltaTime) {
					setAnimation("walk_left");
				} else {
					setAnimation("idle_left");
				}
			} else {
				setAnimation("jump_left");
			}
		}

		//track time played
		playerETC.timePlayed += deltaTime;
	}

	public boolean setLeftInput(boolean pressed) {
		leftPressed = pressed;
		if (leftPressed) {
			playerMovC.movementSignalIntention =
					playerMovC.movementSignalIntention > 0 ? 0 : MovementComponent.LEFT_SIGNAL;
		} else {
			playerMovC.movementSignalIntention = rightPressed ? MovementComponent.RIGHT_SIGNAL : 0;
		}
		return leftPressed;
	}

	public boolean setRightInput(boolean pressed) {
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

	public void resizeScreen(int width, int height) {
		stage.getViewport().update(width, height, true);
	}

	@Override
	public void dispose() {
		stage.dispose();
	}
}
