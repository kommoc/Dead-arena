package net.kommocgame.src.entity.AI.asynch;

import com.badlogic.gdx.math.Vector2;

public class EntityInfoRequest {
	
	private final Vector2 entity_position = new Vector2();
	private final Vector2 entity_velocity = new Vector2();
	private float orientation = 0;
	private float rotation = 0;
	
	private final SteerableImp owner;
	
	public EntityInfoRequest(SteerableImp steerable) {
		owner = steerable;
	}
	
	public void applyRequest(Vector2 position, Vector2 velocity, float rotation, float orientation) {
		entity_position.set(position);
		entity_velocity.set(velocity);
		this.orientation = orientation;
		this.rotation = rotation;
	}
	
	public void updateInfo() {
		owner.setPosition(entity_position);
		owner.setLinearVelocity(entity_velocity);
		owner.setOrientation(orientation);
		owner.setRotation(rotation);
	}
}
