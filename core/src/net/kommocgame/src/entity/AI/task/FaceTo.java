package net.kommocgame.src.entity.AI.task;

import com.badlogic.gdx.ai.steer.Limiter;
import com.badlogic.gdx.ai.steer.Steerable;
import com.badlogic.gdx.ai.steer.SteeringAcceleration;
import com.badlogic.gdx.ai.steer.behaviors.ReachOrientation;
import com.badlogic.gdx.ai.utils.ArithmeticUtils;
import com.badlogic.gdx.ai.utils.Location;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;

import net.kommocgame.src.entity.EntityLiving;

public class FaceTo extends ReachOrientation<Vector2> {
	
	private float coneThreshold = 0;
	
	private boolean exec = false;
	
	public FaceTo(Steerable<Vector2> owner, float threshold) {
		super(owner);
		coneThreshold = threshold / 2f;
	}
	
	public FaceTo(Steerable<Vector2> owner, Location<Vector2> target, float threshold) {
		super(owner, target);
		coneThreshold = threshold / 2f;
	}
	
	@Override
	protected SteeringAcceleration<Vector2> calculateRealSteering (SteeringAcceleration<Vector2> steering) {
		if(target != null)
			return face(steering, target.getPosition());
		System.out.println("	FaceTo.calculateRealSteering() ### target is not defined!");
		
		return steering.setZero();
	}

	protected SteeringAcceleration<Vector2> face (SteeringAcceleration<Vector2> steering, Vector2 targetPosition) {
		// Get the direction to target
		Vector2 toTarget = steering.linear.set(targetPosition).sub(owner.getPosition());
		// Check for a zero direction, and return no steering if so
		if (toTarget.isZero(getActualLimiter().getZeroLinearSpeedThreshold())) return steering.setZero();
	
		//if(true) return steering.setZero();
		// Calculate the orientation to face the target
		float orientation = owner.vectorToAngle(toTarget);
		
		//System.out.println("Or1: " + orientation);
		//System.out.println("Or2(owner): " + ArithmeticUtils.wrapAngleAroundZero(owner.getOrientation()));
		
		float rotation = ArithmeticUtils.wrapAngleAroundZero(orientation - owner.getOrientation());
		steering.linear.setZero();
		
		//System.out.println("\n	rotation:" + rotation);
		//System.out.println("	PRE_steer_lin:" + steering.linear);
		//System.out.println("	PRE_steer_ang:" + steering.angular);
		
		if(Math.abs(rotation) < coneThreshold * MathUtils.degRad) {
			exec = true;
			//System.out.println("STG_3");
			return steering.setZero();
		}
		
		if(Math.abs(rotation) < owner.getMaxAngularAcceleration() * MathUtils.degRad/* * MathUtils.degRad*/) {
			steering.angular = rotation;
			//System.out.println("maxAccel: " + rotation);
			//System.out.println("STG_2");
			return steering;
		}
		
		steering.angular = rotation * owner.getMaxAngularAcceleration() * MathUtils.degRad;
		
		//System.out.println("STG_1: " + steering.angular);
		
		return steering;
	}
	
	public boolean isExecuted() {
		return exec;
	}
	
	//
	// Setters overridden in order to fix the correct return type for chaining
	//

	@Override
	public FaceTo setOwner (Steerable<Vector2> owner) {
		this.owner = owner;
		return this;
	}

	@Override
	public FaceTo setEnabled (boolean enabled) {
		this.enabled = enabled;
		return this;
	}

	/** Sets the limiter of this steering behavior. The given limiter must at least take care of the maximum angular speed and
	 * acceleration.
	 * @return this behavior for chaining. */
	@Override
	public FaceTo setLimiter (Limiter limiter) {
		this.limiter = limiter;
		return this;
	}

	@Override
	public FaceTo setTarget (Location<Vector2> target) {
		this.target = target;
		return this;
	}

	@Override
	public FaceTo setAlignTolerance (float alignTolerance) {
		this.alignTolerance = alignTolerance;
		return this;
	}

	@Override
	public FaceTo setDecelerationRadius (float decelerationRadius) {
		this.decelerationRadius = decelerationRadius;
		return this;
	}

	@Override
	public FaceTo setTimeToTarget (float timeToTarget) {
		this.timeToTarget = timeToTarget;
		return this;
	}
}
