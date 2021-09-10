package net.kommocgame.src.entity.AI.asynch;

import com.badlogic.gdx.ai.steer.SteeringAcceleration;
import com.badlogic.gdx.math.Vector2;

import net.kommocgame.src.entity.AI.asynch.actions.Action;

public interface IEntityRequest {
	
	void applySteering(SteeringAcceleration<Vector2> steering);
	
	void executeAction(Action action);
	
	boolean isTracking();
	
	void setTracking(boolean par1);
}
