package com.igrmm.igt.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.igrmm.igt.Assets;
import com.igrmm.igt.components.MovementComponent;
import com.igrmm.igt.factories.PlayerFactory.PlayerETComponent;

public class PlayerSystem extends EntitySystem implements Disposable {
	private final PlayerETComponent playerETC;
	private final Stage stage;
	private boolean rightPressed = false;
	private boolean leftPressed = false;

	public PlayerSystem(Entity playerE, Assets assets) {
		ComponentMapper<PlayerETComponent> playerETM = ComponentMapper.getFor(PlayerETComponent.class);
		playerETC = playerETM.get(playerE);
		final MovementComponent playerMovC = playerE.getComponent(MovementComponent.class);
		stage = new Stage(new ScreenViewport());
		Gdx.input.setInputProcessor(stage);
		stage.addListener(new InputListener() {
			public boolean keyDown(InputEvent event, int keycode) {
				if (keycode == Input.Keys.A) {
					playerMovC.movementSignalIntention =
							playerMovC.movementSignalIntention > 0 ? 0 : MovementComponent.LEFT_SIGNAL;
					leftPressed = true;
				}

				if (keycode == Input.Keys.D) {
					playerMovC.movementSignalIntention =
							playerMovC.movementSignalIntention < 0 ? 0 : MovementComponent.RIGHT_SIGNAL;
					rightPressed = true;
				}

				return true;
			}

			public boolean keyUp(InputEvent event, int keycode) {
				if (keycode == Input.Keys.A) {
					playerMovC.movementSignalIntention = rightPressed ? MovementComponent.RIGHT_SIGNAL : 0;
					leftPressed = false;
				}

				if (keycode == Input.Keys.D) {
					playerMovC.movementSignalIntention = leftPressed ? MovementComponent.LEFT_SIGNAL : 0;
					rightPressed = false;
				}

				return true;
			}
		});
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
