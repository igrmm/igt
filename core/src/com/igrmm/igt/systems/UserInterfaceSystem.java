package com.igrmm.igt.systems;

import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Disposable;
import com.igrmm.igt.components.MovementComponent;

public class UserInterfaceSystem extends EntitySystem implements Disposable {
	private final Stage stage;
	private boolean rightPressed = false;
	private boolean leftPressed = false;

	public UserInterfaceSystem(final MovementComponent playerMovementC) {
		stage = new Stage();
		Gdx.input.setInputProcessor(stage);
		stage.addListener(new InputListener() {
			public boolean keyDown(InputEvent event, int keycode) {
				if (keycode == Input.Keys.A) {
					playerMovementC.movementSignalIntention =
							playerMovementC.movementSignalIntention > 0 ? 0 : MovementComponent.LEFT_SIGNAL;
					leftPressed = true;
				}

				if (keycode == Input.Keys.D) {
					playerMovementC.movementSignalIntention =
							playerMovementC.movementSignalIntention < 0 ? 0 : MovementComponent.RIGHT_SIGNAL;
					rightPressed = true;
				}

				return true;
			}

			public boolean keyUp(InputEvent event, int keycode) {
				if (keycode == Input.Keys.A) {
					playerMovementC.movementSignalIntention = rightPressed ? MovementComponent.RIGHT_SIGNAL : 0;
					leftPressed = false;
				}

				if (keycode == Input.Keys.D) {
					playerMovementC.movementSignalIntention = leftPressed ? MovementComponent.LEFT_SIGNAL : 0;
					rightPressed = false;
				}

				return true;
			}
		});
	}

	@Override
	public void update(float deltaTime) {
		stage.act();
	}

	@Override
	public void dispose() {
		stage.dispose();
	}
}
