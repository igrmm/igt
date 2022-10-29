package com.igrmm.igt.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.igrmm.igt.factories.PlayerFactory.PlayerETComponent;

public class TimeTrackingSystem extends EntitySystem {
	private final PlayerETComponent playerETC;

	public TimeTrackingSystem(Entity playerE) {
		ComponentMapper<PlayerETComponent> playerETM = ComponentMapper.getFor(PlayerETComponent.class);
		playerETC = playerETM.get(playerE);
	}

	@Override
	public void update(float deltaTime) {
		playerETC.timePlayed += deltaTime;
	}
}
