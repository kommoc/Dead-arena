package net.kommocgame.src.entity.AI.task;

import com.badlogic.gdx.ai.steer.Steerable;
import com.badlogic.gdx.ai.steer.SteeringAcceleration;
import com.badlogic.gdx.ai.steer.SteeringBehavior;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.RayCastCallback;

import net.kommocgame.src.Function;
import net.kommocgame.src.VecUtils;
import net.kommocgame.src.entity.EntityBase;
import net.kommocgame.src.entity.EntityLiving;
import net.kommocgame.src.entity.AI.asynch.RaycastRequest;
import net.kommocgame.src.entity.AI.asynch.SteerableImp;
import net.kommocgame.src.world.World;

public class CollisionAvoid extends SteeringBehavior<Vector2> {
	
	Vector2 vec_out = new Vector2();
	
	Vector2 vec_r_0 = new Vector2();
	Vector2 vec_r_1 = new Vector2();
	Vector2 vec_l_0 = new Vector2();
	Vector2 vec_l_1 = new Vector2();
	
	Vector2 sup_vec_rl		= new Vector2();
	Vector2 prew_vec_out	= new Vector2();
	
	Function func_interp = new Function(1000, Interpolation.linear);
	
	float distance;
	
	public final static float GAP_LENGTH = 0.1f;
	private World world;
	
	private final RayCastCollisionAvoidance line_r = new RayCastCollisionAvoidance();
	private final RayCastCollisionAvoidance line_l = new RayCastCollisionAvoidance();
	
	private SteerableImp entity;
	
	private final RaycastRequest rr_left;
	private final RaycastRequest rr_right;
	
	/********************************************************************/
	public Vector2 vector_normal_to_velocity		= new Vector2();
	public Vector2 vector_normal_to_velocity_helper	= new Vector2();
	
	public CollisionAvoid(Steerable<Vector2> owner) {
		super(owner);
		entity = (SteerableImp) owner;
		distance = owner.getBoundingRadius() * 4f;
		
		world = entity.worldObj;
		rr_left = new RaycastRequest(line_l);
		rr_right = new RaycastRequest(line_r);
		entity.getRaycast_list().add(rr_left);
		entity.getRaycast_list().add(rr_right);
		
	}

	@Override
	protected SteeringAcceleration<Vector2> calculateRealSteering(SteeringAcceleration<Vector2> steering) {
		owner.angleToVector(sup_vec_rl, entity.getOrientation()).setLength(distance);
		float gap = owner.getBoundingRadius() + GAP_LENGTH;
		
		vec_r_0.set(MathUtils.cosDeg(entity.getRotation() + 90f), MathUtils.sinDeg(entity.getRotation() + 90f))
				.add(owner.getPosition());
		vec_l_0.set(MathUtils.cosDeg(entity.getRotation() - 90f), MathUtils.sinDeg(entity.getRotation() - 90f))
				.add(owner.getPosition());
		vec_r_1.set(vec_r_0).add(sup_vec_rl);
		vec_l_1.set(vec_l_0).add(sup_vec_rl);
		
		raycastLine(rr_left, vec_l_0, vec_l_1);
		raycastLine(rr_right, vec_r_0, vec_r_1);
		
		vec_out.set(line_l.getPoint().dst(owner.getPosition()) > line_r.getPoint().dst(owner.getPosition()) ? 
				getFinalVelocity(line_r) : getFinalVelocity(line_l)).scl(10f);
		
		if(vec_out.len() > 0) {
			if(vec_out.len() > prew_vec_out.len()) {
				//System.out.println("	CollisionAvoid.calcRealSteer() ### set NEW VELOCITY!");
				prew_vec_out.set(vec_out);
				func_interp.reset();
				func_interp.start();
			}
			
			//steering.linear.set(prew_vec_out);
		}
		
		if(func_interp.hasEnded() && !prew_vec_out.isZero()) {
			prew_vec_out.setZero();
			func_interp.reset();
			func_interp.start();
		}
		
		if(owner.getLinearVelocity().isZero(owner.getZeroLinearSpeedThreshold()) && entity.getSteering().linear.isZero() && !vec_out.isZero()) {
			steering.angular = prew_vec_out.angleRad();
			//prew_vec_out.setZero();
		}
		
		//prew_vec_out.interpolate(vec_out, func_interp.getValue(), Interpolation.exp5Out);
		steering.linear.interpolate(prew_vec_out, func_interp.getValue(), Interpolation.exp5Out);
		//steering.linear.set(prew_vec_out);
		
		//DebugWorld.debugLine(owner.getPosition(), owner.getPosition().cpy().add(steering.linear));
		//DebugWorld.debugLine(owner.getPosition(), owner.getPosition().cpy().add(entity.control.linear), Color.RED);
		//System.out.println("	CollisionAvoid.calcRealSteer() ### getLinearVelocity: " + owner.getLinearVelocity());
		//System.out.println("	CollisionAvoid.calcRealSteer() ### steering.linear: " + steering.linear);
		//System.out.println("	CollisionAvoid.calcRealSteer() ### prew_vec_out: " + prew_vec_out);
		
		func_interp.init();
		//System.out.println("	CollisionAvoid.calcRealSteer() ### prew_vec_out: " + prew_vec_out);
		//DebugWorld.debugLine(owner.getPosition(), owner.getPosition().cpy().add(owner.getLinearVelocity().cpy().setLength(5f)));
		
		return steering;
	}
	
	/*
	private void raycastLine(RayCastCollisionAvoidance callback, Vector2 begin, Vector2 end) {
		vec_out.setZero();
		callback.vec_line.setZero();
		callback.vec_normal.setZero();
		
		if(begin.dst(end) > 0f) {
			world.physics.rayCast(callback, begin, end);
		} else System.out.println("	ColisionAvoid.raycastLine() ### missed raycast!");
	}*/
	
	private void raycastLine(RaycastRequest request, Vector2 begin, Vector2 end) {
		request.createNewRequest(begin, end);
	}
	
	private Vector2 getFinalVelocity(RayCastCollisionAvoidance line) {
		VecUtils.getNormalVec(owner.getPosition().cpy().add(line.getNormal()), entity.getSteering().linear.cpy()
				.add(owner.angleToVector(vector_normal_to_velocity_helper, owner.getOrientation()).nor()), owner.getPosition(),
				vector_normal_to_velocity);
		if(vector_normal_to_velocity.isZero())
			return vector_normal_to_velocity;
		
		if(vector_normal_to_velocity.len() < (owner.getBoundingRadius() + GAP_LENGTH)) {
			vector_normal_to_velocity.setLength((owner.getBoundingRadius() + GAP_LENGTH) * 3f);
		}
		
		return vector_normal_to_velocity;
	}
	
	public class RayCastCollisionAvoidance {
		
		Vector2 vec_line = new Vector2();
		Vector2 vec_normal = new Vector2();
		Vector2 vec_point = new Vector2();
		
		public RayCastCollisionAvoidance() { }
		
		public Vector2 getNormal() {
			return vec_normal;
		}
		
		public Vector2 getPoint() {
			return vec_point;
		}
		
		public void reportRayFixture(Vector2 point, Vector2 normal) {
			vec_out.setZero();
			vec_line.setZero();
			vec_normal.setZero();
			
			vec_line.set(normal.set(-normal.y, normal.x));
			vec_point.set(point);
			VecUtils.getNormalVec(owner.getPosition(), vec_line, vec_point, vec_normal);
		}
	}
	
}
