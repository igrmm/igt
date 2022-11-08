package com.igrmm.igt.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.math.Vector2;

public class MovementComponent implements Component {
	public static final int RIGHT_SIGNAL = 1;
	public static final int LEFT_SIGNAL = -1;

	public float maxSpeed = 0f;
	public float acceleration = 0f;
	public float friction = 0f;
	public final Vector2 speed = new Vector2();
	public boolean jumping = false;
	public boolean grounded = false;
	public float jumpTimer = 0f;
	public float jumpTime = 0f;
	public float jumpForce = 0f;
	public float gravity = 0f;

	public int movementSignalIntention = 0;
	public boolean jumpIntention = false;
}
