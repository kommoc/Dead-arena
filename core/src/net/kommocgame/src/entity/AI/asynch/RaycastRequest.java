package net.kommocgame.src.entity.AI.asynch;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.RayCastCallback;

import net.kommocgame.src.entity.AI.task.CollisionAvoid.RayCastCollisionAvoidance;
import net.kommocgame.src.world.World;
import net.kommocgame.src.world.physics.Box2dWorld;

public class RaycastRequest {
	
	private boolean isSended	= false;
	private boolean isReceived	= false;
	private boolean result		= false;
	private boolean lastResult	= false;
	
	private Vector2 fromPoint;
	private Vector2 toPoint;
	
	private RayCastCollisionAvoidance callback;
	
	public RaycastRequest() {
		this(null);
	}
	
	public RaycastRequest(RayCastCollisionAvoidance callback) {
		this.fromPoint = new Vector2();
		this.toPoint = new Vector2();
		this.callback = callback;
	}
	
	public void createNewRequest(Vector2 fromPoint, Vector2 toPoint) {
		if(!isSended) {
			this.toPoint.set(toPoint);
			this.fromPoint.set(fromPoint);
			isReceived = false;
			isSended = true;
		}
	}
	/*
	public SteerableImp getSteerable() {
		return null;
	}*/
	public void applyRaycast(World world) {
		if(callback != null) {
			world.getRayManager().collisionAvoidRaycast(callback, fromPoint, toPoint);
			isReceived = true;
			isSended = false;
		} else {
			result = world.getRayManager().checkPoint(fromPoint, toPoint);
			lastResult = result;
			isReceived = true;
		}
	}
	
	@Deprecated
	public boolean getResult() {
		isSended = isReceived ? false : true;
		return isReceived == true ? result : false;
	}
	
	public boolean getLastResult() {
		isSended = isReceived ? false : true;
		return lastResult;
	}
	
	public boolean isReceived() {
		return isReceived;
	}
}
