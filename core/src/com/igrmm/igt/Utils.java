package com.igrmm.igt;

import com.badlogic.gdx.Gdx;

public class Utils {
	/**
	 * @param widthPercentage desired value in screen width percentage.
	 * @return the given screen width percentage converted to pixels.
	 */
	public static float wpToPx(int widthPercentage) {
		return widthPercentage * Gdx.graphics.getWidth() / 100f;
	}

	/**
	 * @param centimeters desired value in centimeters.
	 * @return the given value in centimeters converted to pixels.
	 */
	public static float cmToPx(float centimeters) {
		return centimeters / (2f / (Gdx.graphics.getPpcX() + Gdx.graphics.getPpcY()));
	}
}
