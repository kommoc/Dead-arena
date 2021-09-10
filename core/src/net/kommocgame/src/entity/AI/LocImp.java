package net.kommocgame.src.entity.AI;

import com.badlogic.gdx.ai.utils.Location;
import com.badlogic.gdx.math.Vector2;

public class LocImp extends Vector2 implements Location<Vector2> {
	
	public LocImp(Vector2 vec) {
		this.set(vec);
	}
	
	public LocImp(float x, float y) {
		this.set(x, y);
	}
	
	@Override
	public Vector2 getPosition() {
		return this;
	}

	@Override
	public float getOrientation() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void setOrientation(float orientation) {
		
	}

	@Override
	public float vectorToAngle(Vector2 vector) {
		return 0;
	}

	@Override
	public Vector2 angleToVector(Vector2 outVector, float angle) {
		return null;
	}

	@Override
	public Location<Vector2> newLocation() {
		return this;
	}

}
