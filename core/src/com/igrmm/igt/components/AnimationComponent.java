package com.igrmm.igt.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.igrmm.igt.AsepriteAnimation;

import java.util.HashMap;
import java.util.Map;

public class AnimationComponent implements Component {
	public final Map<String, Animation<TextureRegion>> animations;
	public String currentAnimation;
	public float stateTime = 0f;

	public AnimationComponent(AsepriteAnimation asepriteAnimation) {
		animations = new HashMap<>();
		for (String animationName : asepriteAnimation.names) {
			float duration = asepriteAnimation.durations.get(animationName);
			TextureRegion[] textureRegions = asepriteAnimation.textureRegions.get(animationName);
			animations.put(animationName, new Animation<>(duration, textureRegions));
		}
		currentAnimation = asepriteAnimation.names.get(0);
	}
}
