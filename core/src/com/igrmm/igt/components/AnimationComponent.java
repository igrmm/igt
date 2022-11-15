package com.igrmm.igt.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import java.util.HashMap;
import java.util.Map;

public class AnimationComponent implements Component {
	public final Map<String, Animation<TextureRegion>> animations = new HashMap<>();
	public String currentAnimation;
	public float stateTime = 0f;
	public float offsetX = 0f;
	public float offsetY = 0f;
}
