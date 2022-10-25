package com.igrmm.igt.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.igrmm.igt.components.StatisticsComponent;

public class TimeTrackingSystem extends EntitySystem {
	private final StatisticsComponent playerStatisticsC;

	public TimeTrackingSystem(Entity playerEntity) {
		playerStatisticsC = playerEntity.getComponent(StatisticsComponent.class);
	}

	@Override
	public void update(float deltaTime) {
		playerStatisticsC.timePlayed += deltaTime;
	}
}
