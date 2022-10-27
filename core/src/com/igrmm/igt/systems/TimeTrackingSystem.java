package com.igrmm.igt.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.igrmm.igt.components.StatisticsComponent;

public class TimeTrackingSystem extends EntitySystem {
	private final StatisticsComponent playerStatisticsC;

	public TimeTrackingSystem(Entity playerE) {
		ComponentMapper<StatisticsComponent> statisticsM = ComponentMapper.getFor(StatisticsComponent.class);
		playerStatisticsC = statisticsM.get(playerE);
	}

	@Override
	public void update(float deltaTime) {
		playerStatisticsC.timePlayed += deltaTime;
	}
}
