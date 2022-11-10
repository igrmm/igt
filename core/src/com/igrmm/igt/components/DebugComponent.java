package com.igrmm.igt.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.ui.Label;

public class DebugComponent implements Component {
	public final Label debugLabel = new Label("", new Label.LabelStyle(new BitmapFont(), null));
}
