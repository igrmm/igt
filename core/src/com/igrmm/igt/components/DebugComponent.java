package com.igrmm.igt.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.scenes.scene2d.ui.Label;

public class DebugComponent implements Component {
	public final Label debugLabel;

	public DebugComponent(Label debugLabel) {
		this.debugLabel = debugLabel;
	}
}
