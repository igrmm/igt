package com.igrmm.igt;

import com.igrmm.igt.components.MovementComponent;
import com.igrmm.igt.components.SpawnPointComponent;
import com.igrmm.igt.components.StatisticsComponent;
import com.igrmm.igt.components.MapComponent;

public class Save {
	private transient boolean loaded = false;

	public final StatisticsComponent statisticsC = new StatisticsComponent();
	public final SpawnPointComponent spawnPointC = new SpawnPointComponent();
	public final MapComponent mapC = new MapComponent();
	public final MovementComponent movementC = new MovementComponent();

	public boolean isLoaded() {
		return this.loaded;
	}

	public void setLoaded(boolean loaded) {
		this.loaded = loaded;
	}
}
