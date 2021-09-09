package net.kommocgame.src.entity.AI.asynch;

import com.badlogic.gdx.ai.steer.Steerable;
import com.badlogic.gdx.ai.steer.SteeringAcceleration;
import com.badlogic.gdx.ai.steer.SteeringBehavior;
import com.badlogic.gdx.ai.utils.Location;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.reflect.ClassReflection;
import com.badlogic.gdx.utils.reflect.ReflectionException;

import net.kommocgame.src.VecUtils;
import net.kommocgame.src.entity.EntityLiving;
import net.kommocgame.src.entity.AI.FieldOfView;
import net.kommocgame.src.entity.AI.Node;
import net.kommocgame.src.entity.AI.asynch.actions.Action;
import net.kommocgame.src.world.World;

public class SteerableImp implements Steerable<Vector2> {
	
	private SteeringBehavior<Vector2>		behavior;
	private SteeringAcceleration<Vector2>	steering;
	
	private final FieldOfView<Vector2> fieldOfView;
	
	private TaskManager	taskManager;
	
	private final Vector2		position = new Vector2();
	private float		orientation;
	//private LocImp		location;
	private float 		zeroLinearSpeedThreshold;
	
	private float 		maxLinearSpeed;
	private float 		maxLinearAcceleration;
	private float 		maxAngularSpeed;
	private float 		maxAngularAcceleration;
	
	private float		angularVelocity;
	private final Vector2		linearVelocity = new Vector2();
	
	private float 		boundingRadius;
	private boolean		isTagged;
	
	private boolean 	isDead;
	
	public World		worldObj;
	PathFinderImp		pathFinder;
	private float 		rotation;
	private boolean		isDelete = false;
	
	private Array<RaycastRequest> raycast_checkPoint_list = new Array<RaycastRequest>();
	private EntityInfoRequest info_request;
	
	private EntityLiving entity_track;
	
	private Array<Action> action_list = new Array<Action>();
	
	public SteerableImp(EntityLiving entity) {
		entity_track = entity;
		setZeroLinearSpeedThreshold(entity.getZeroLinearSpeedThreshold());
		setMaxAngularAcceleration(entity.getMaxAngularAcceleration());
		setMaxAngularSpeed(entity.getMaxAngularSpeed());
		setMaxLinearAcceleration(entity.getMaxLinearAcceleration());
		setMaxLinearSpeed(entity.getMaxLinearSpeed());
		setBoundingRadius(entity.getBoundingRadius());
		
		linearVelocity.set(entity.getLinearVelocity());
		setRotation(entity.getRotation());
		
		setOrientation(entity.getOrientation());
		steering = new SteeringAcceleration<Vector2>(new Vector2(), 0);
		
		taskManager = new TaskManager(this);
		
		this.worldObj = entity.worldObj;
		System.out.println("SIZE OF NODES: " + worldObj.getLevel().getGridNodes().getGraphPath().nodes.size);
		pathFinder = new PathFinderImp(this, worldObj.getLevel().getGridNodes());
		fieldOfView = new FieldOfView<Vector2>(this, worldObj.getSteerList(), 5, 200);
		
		try {
			for(int i = 0; i < entity.requerements.size; i++) {
				RequerementImp requerement = (RequerementImp) ClassReflection.getConstructor(entity.requerements.get(i),
						SteerableImp.class).newInstance(this);
				
				getAI().addRequerement(requerement);
			}
		} catch (ReflectionException e) {
			e.printStackTrace();
		}
		
		info_request = new EntityInfoRequest(this);
	}

	@Override
	public Vector2 getPosition() {
		return position;
	}

	@Override
	public float getOrientation() {
		return orientation;
	}
	
	public Array<RaycastRequest> getRaycast_list() {
		return raycast_checkPoint_list;
	}
	
	public EntityInfoRequest getInfoRequest() {
		return info_request;
	}

	@Override
	public float vectorToAngle (Vector2 vector) {
		return VecUtils.vectorToAngle(vector);
	}

	@Override
	public Vector2 angleToVector (Vector2 outVector, float angle) {
		return VecUtils.angleToVector(outVector, angle);
	}

	@Override
	public Location<Vector2> newLocation() {
		return this;
	}

	@Override
	public float getZeroLinearSpeedThreshold() {
		return zeroLinearSpeedThreshold;
	}

	@Override
	public float getMaxLinearSpeed() {
		return maxLinearSpeed;
	}

	@Override
	public float getMaxLinearAcceleration() {
		return maxLinearAcceleration;
	}

	@Override
	public float getMaxAngularAcceleration() {
		return maxAngularAcceleration;
	}

	@Override
	public Vector2 getLinearVelocity() {
		return linearVelocity;
	}

	@Override
	public float getAngularVelocity() {
		return angularVelocity;
	}
	
	@Override
	public float getMaxAngularSpeed() {
		return maxAngularSpeed;
	}
	
	@Override
	public float getBoundingRadius() {
		return boundingRadius;
	}
	
	public FieldOfView<Vector2> getFieldOfView() {
		return fieldOfView;
	}
	
	public PathFinderImp getPathFinder() {
		return pathFinder;
	}
	
	public Node getEntityNode() {
		return worldObj.getLevel().getGridNodes() != null ? Node.getNodeByPos(worldObj.getLevel().getGridNodes(), getPosition().x,
				getPosition().y) : null;
	}
	
	public SteeringAcceleration<Vector2> getSteering() {
		return steering;
	}
	
	public SteeringBehavior<Vector2> getBehavior() {
		return behavior;
	}
	
	public Array<Action> getActionList() {
		return action_list;
	}
	
	public TaskManager getAI() {
		return taskManager;
	}

	public float getRotation() {
		return rotation;
	}
	
	public void setBoundingRadius(float radius) {
		boundingRadius = radius;
	}
	
	@Override
	public void setMaxAngularAcceleration(float maxAngularAcceleration) {
		this.maxAngularAcceleration = maxAngularAcceleration;
	}
	
	@Override
	public void setOrientation(float orientation) {
		this.orientation = orientation;
	}
	
	@Override
	public void setMaxLinearAcceleration(float maxLinearAcceleration) {
		this.maxLinearAcceleration = maxLinearAcceleration;
	}
	
	public void setRotation(float degree) {
		this.rotation = degree;
	}
	
	public void setPosition(final Vector2 pos) {
		this.getPosition().set(pos);
	}
	
	public void setLinearVelocity(final Vector2 vel) {
		this.getLinearVelocity().set(vel);
	}

	@Override
	public void setMaxAngularSpeed(float maxAngularSpeed) {
		this.maxAngularSpeed = maxAngularSpeed;
	}
	
	@Override
	public void setZeroLinearSpeedThreshold(float value) {
		this.zeroLinearSpeedThreshold = value;
	}

	@Override
	public void setTagged(boolean tagged) {
		this.isTagged = tagged;
	}
	
	public void setDelete(boolean isDelete) {
		this.isDelete = isDelete;
	}
	
	@Override
	public void setMaxLinearSpeed(float maxLinearSpeed) {
		this.maxLinearSpeed = maxLinearSpeed;
	}
	
	public void setBehavior(SteeringBehavior<Vector2> behavior) {
		this.behavior = behavior;
	}
	
	public boolean isDead() {
		return isDead;
	}
	
	public boolean isDelete() {
		return isDelete;
	}
	
	@Override
	public boolean isTagged() {
		return isTagged;
	}
	
	
}
