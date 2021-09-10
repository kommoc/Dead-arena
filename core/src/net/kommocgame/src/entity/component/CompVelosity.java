package net.kommocgame.src.entity.component;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.math.Vector2;

public class CompVelosity implements Component {
	
	public Vector2 vector_motion = new Vector2(0, 0);
	
	public float motionX = 0.0f;
	public float motionY = 0.0f;
	
	public float maxMotionR = 5f;
	
	public void setMaxMotion(float maxMotion) {
		maxMotionR = maxMotion;
	}
}
