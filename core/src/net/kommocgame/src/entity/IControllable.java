package net.kommocgame.src.entity;

import com.badlogic.gdx.ai.steer.Steerable;
import com.badlogic.gdx.math.Vector2;

public interface IControllable extends Steerable<Vector2> {
	
	/** Method called when object try to moving. */
	void toMove(float forceX, float forceY) throws NullPointerException;
	
	/** Method called when object try to face at. */
	void toLook(float forceX, float forceY) throws NullPointerException;
}
