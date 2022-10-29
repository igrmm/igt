package com.igrmm.igt;

import com.igrmm.igt.components.SpawnPointComponent;
import com.igrmm.igt.factories.PlayerFactory.PlayerETComponent;
import com.igrmm.igt.components.MapComponent;

public class Save {
	public final PlayerETComponent playerETC = new PlayerETComponent();
	public final SpawnPointComponent spawnPointC = new SpawnPointComponent();
	public final MapComponent mapC = new MapComponent();
}
