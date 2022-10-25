package com.igrmm.igt;

import com.igrmm.igt.components.SpawnPointComponent;
import com.igrmm.igt.components.StatisticsComponent;
import com.igrmm.igt.components.MapComponent;

public class Save {
	private transient boolean loaded = false;

	public final StatisticsComponent statisticsComponent = new StatisticsComponent();
	public final SpawnPointComponent spawnPointComponent = new SpawnPointComponent();
	public final MapComponent mapComponent = new MapComponent();

	public boolean isLoaded() {
		return this.loaded;
	}

	public void setLoaded(boolean loaded) {
		this.loaded = loaded;
	}
}
