package com.igrmm.igt.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.igrmm.igt.components.StatisticsComponent;

public class TimeTrackingSystem extends IteratingSystem {
	private final ComponentMapper<StatisticsComponent> statisticsM;

	public TimeTrackingSystem() {
		super(Family.one(StatisticsComponent.class).get());
		statisticsM = ComponentMapper.getFor(StatisticsComponent.class);
	}

	@Override
	protected void processEntity(Entity entity, float deltaTime) {
		StatisticsComponent statisticsC = statisticsM.get(entity);
		statisticsC.timePlayed += deltaTime;
	}
}
