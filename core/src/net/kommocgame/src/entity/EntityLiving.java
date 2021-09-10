package net.kommocgame.src.entity;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ai.fsm.DefaultStateMachine;
import com.badlogic.gdx.ai.fsm.State;
import com.badlogic.gdx.ai.steer.Proximity.ProximityCallback;
import com.badlogic.gdx.ai.steer.Steerable;
import com.badlogic.gdx.ai.steer.SteeringAcceleration;
import com.badlogic.gdx.ai.steer.SteeringBehavior;
import com.badlogic.gdx.ai.utils.ArithmeticUtils;
import com.badlogic.gdx.ai.utils.Location;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

import net.kommocgame.src.Game;
import net.kommocgame.src.Loader;
import net.kommocgame.src.VecUtils;
import net.kommocgame.src.control.KeyBinding;
import net.kommocgame.src.entity.AI.EffectFactor;
import net.kommocgame.src.entity.AI.FieldOfView;
import net.kommocgame.src.entity.AI.asynch.IEntityRequest;
import net.kommocgame.src.entity.AI.asynch.RequerementImp;
import net.kommocgame.src.entity.AI.asynch.actions.Action;
import net.kommocgame.src.entity.AI.event.AIEvent;
import net.kommocgame.src.entity.AI.event.AIEventPlaySound;
import net.kommocgame.src.entity.AI.thread.SchedulerAI.SteeringControl;
import net.kommocgame.src.entity.buffs.Buff;
import net.kommocgame.src.entity.component.CompAStar;
import net.kommocgame.src.entity.component.CompFactor;
import net.kommocgame.src.world.World;

public class EntityLiving extends EntityBase implements IControllable, ProximityCallback<Vector2>, IEntityRequest {
	
	protected float sqrt_val = 0;
	
	public DefaultStateMachine<EntityLiving, State<EntityLiving>> state_machine;
	
	private CompFactor compFactor;
	
	private boolean isTagged = false;
	private float max_speed = 15f;
	private float max_acceleration = 1f;
	private float max_rotation = 45f;
	private float max_rotation_acceleration = 45f;
	private float zero_linear = 0.05f;
	
	private CompAStar compAStar;
	
	/** Field of view by center of entity. In degrees. */
	private FieldOfView<Vector2> fieldOfView;
	
	private long lastTime_look = 0;
	private float prew_forceX = 0;
	private float prew_forceY = 0;
	
	private Vector2 force_to_center = new Vector2();
	
	/*** TEST ***/
	private boolean lin_exec = false;
	
	/*** TEST ***/
	
	////public SteeringAcceleration<Vector2> control = new SteeringAcceleration<Vector2>(new Vector2());
	public SteeringBehavior<Vector2> behavior;
	
	public SteeringControl control_steer;
	
	private boolean isTracked = false;
	public Array<Class<? extends RequerementImp>> requerements = new Array<Class<? extends RequerementImp>>();
	
	/******************************************************/
	private Vector2 vector_1 = new Vector2();
	
	public EntityLiving(World world, SpriteBatch batch) {
		this(world, batch, 0, 0, null, 10);
	}
	
	public EntityLiving(World world, SpriteBatch batch, float x, float y) {
		this(world, batch, x, y, Loader.objectsUnits("img_def_1.png"), 10);
	}
	
	public EntityLiving(World world, SpriteBatch batch, float x, float y, Texture tex, int hp) {
		super(world, batch, x, y, tex, hp);
		//compAStar = new CompAStar(this, world.getLevel().getGridNodes());
		//compAI = new CompAI(this);
		
		//add(compAI);
		//add(compAStar);
		compFactor = new CompFactor();
		//compAI.setGuild(Guild.G_NEUTRAL);
		
		this.setFieldOfView(360, 5f);
		this.setMaxLinearAcceleration(20f);
		this.setMaxLinearSpeed(20f);
		this.setMaxAngularSpeed(2f);
		this.setMaxAngularAcceleration(2f);
		this.compVelocity.maxMotionR = 35f;
		
		this.add(compFactor);
		
		control_steer = new SteeringControl(this);
		//setFieldOfView(360, 15f);
	}
	
	@Override
	public void toMove(float forceX, float forceY) {
		if(Game.getPlayer() != null && Game.getPlayer().equals(this)) {
			if(Float.isNaN(forceX) || Float.isNaN(forceY))
				return;
			
			if(forceX > compVelocity.maxMotionR) {
				forceX = compVelocity.maxMotionR;
			} else if(forceY > compVelocity.maxMotionR) {
				forceY = compVelocity.maxMotionR;
			}
			
			sqrt_val = (float) Math.sqrt(forceX * forceX + forceY * forceY);
			
			if(sqrt_val != 0) {
				compVelocity.vector_motion.x = forceX / sqrt_val;
				compVelocity.vector_motion.y = forceY / sqrt_val;
				
				if(sqrt_val > 0.75f) {
					compVelocity.motionX = compVelocity.vector_motion.x * compVelocity.maxMotionR;
					compVelocity.motionY = compVelocity.vector_motion.y * compVelocity.maxMotionR;
				} else {
					compVelocity.motionX = compVelocity.vector_motion.x * compVelocity.maxMotionR / 2.5f;
					compVelocity.motionY = compVelocity.vector_motion.y * compVelocity.maxMotionR / 2.5f;
				}
				
				vector_1.set(compVelocity.motionX, compVelocity.motionY);
				compPhysics.body.definition.applyForceToCenter(vector_1, true);
				
				float newOrientation = vectorToAngle(compVelocity.vector_motion);
				this.setRotation(newOrientation * MathUtils.radDeg + 90f);
			}
		}
	}
	
	@Override
	public void toLook(float forceX, float forceY) throws NullPointerException {
		if(forceX != 0 || forceY != 0) {
			lastTime_look = System.currentTimeMillis();
			this.setRotation(KeyBinding.getLookAngle(forceX, forceY) * MathUtils.radDeg + 90f);
			
			prew_forceX = forceX;
			prew_forceY = forceY;
		} else {
			if(lastTime_look + 1000 > System.currentTimeMillis()) {
				this.setRotation(KeyBinding.getLookAngle(prew_forceX, prew_forceY) * MathUtils.radDeg + 90f);
			}
		}
	}
	
	private void applySteering (SteeringAcceleration<Vector2> steering, float time) {
		boolean isAccel = false;
		float x = 0f, y = 0f;
		
		/*
		if(Math.abs(getLinearVelocity().x) > 0.0000001f)
			x = getLinearVelocity().x;
		if(Math.abs(getLinearVelocity().y) > 0.0000001f)
			y = getLinearVelocity().y;
		
		if(x != 0 || y != 0)
			lin_exec = true;
		else lin_exec = false;
		*/
		
		if(!steering.linear.isZero()) {
			angleToVector(force_to_center, getOrientation()).setLength(steering.linear.len());
			getDefinition().getBody().applyForceToCenter(force_to_center, true);
			
			//getDefinition().getBody().applyForceToCenter(steering.linear, true);
			//System.out.println("		EntityLiving.applySteering() ### linear: " + (getLinearVelocity().len()));
			isAccel = true;
		}
		
		if(steering.angular != 0) {
			setOrientation(getOrientation() + getAngularVelocity());
			compPhysics.body.definition.setAngularVelocity(steering.angular);
			
			//System.out.println("		EntityLiving.applySteering() ### angular: " + (getAngularVelocity()));
			isAccel = true;
		} else {
			isAccel = true;
			Vector2 linVel = this.getLinearVelocity();
			//System.out.println("		EntityLiving.applySteering() ### getAngularVelocity: " + (getAngularVelocity()));
			
			if(!linVel.isZero(getZeroLinearSpeedThreshold())) {
				float newOrientation = vectorToAngle(linVel);
				
				if(steering.linear != null) {
					newOrientation = vectorToAngle(steering.linear);
				}
				
				float angle_dif = ArithmeticUtils.wrapAngleAroundZero(newOrientation - getOrientation());
						
				if(Math.abs(angle_dif) < 0.05f) {
					compPhysics.body.definition.setAngularVelocity(0f);
					setOrientation(newOrientation);
				} else {
					compPhysics.body.definition.setAngularVelocity(getAngularVelocity() + angle_dif * getMaxAngularAcceleration());
				}
				
				//System.out.println("		EntityLiving.applySteering() ### angle_dif: " + (getMaxAngularAcceleration()));
			} else if(steering.linear.isZero(getZeroLinearSpeedThreshold())) {
				compPhysics.body.definition.setAngularVelocity(0f);
			}
		}
		
		if(isAccel) {
			Vector2 vel = this.getLinearVelocity();
			float currentSpeed = (float) Math.sqrt(vel.len2()) * Game.SCALE_WORLD_SPEED_VALUE;
			
			if(currentSpeed > getMaxLinearSpeed()) { }
			
			if(Math.abs(getAngularVelocity()) > getMaxAngularAcceleration()) {
				compPhysics.body.definition.setAngularVelocity(getAngularVelocity() > 0 ?
						getMaxAngularAcceleration() : -getMaxAngularAcceleration());
				
				//System.out.println("	EntityLiving.applySteering() ### MAX ROTATION!");
			}
		}
	}
	
	public void updateAI(float deltaTime) {
		applySteering(control_steer.control, Gdx.graphics.getDeltaTime());
		compFactor.update();
	}
	
	public void addBuff(Buff buff) {
		compFactor.addBuff(buff);
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
	public Vector2 getLinearVelocity() {
		float x = 0f, y = 0f;
		if(compPhysics.body.definition.getLinearVelocity().x > 0.0000001f)
			x = compPhysics.body.definition.getLinearVelocity().x;
		if(compPhysics.body.definition.getLinearVelocity().y > 0.0000001f)
			y = compPhysics.body.definition.getLinearVelocity().y;
		
		//return new Vector2(x, y);
		return compPhysics.body.definition.getLinearVelocity();
	}
	
	public Buff getBuff() {
		return compFactor.getResultBuff();
	}

	@Override
	public float getAngularVelocity() {
		return compPhysics.body.definition.getAngularVelocity();
	}

	@Override
	public float getBoundingRadius() {
		return getDefinition().getShape().getRadius();
	}

	@Override
	public Vector2 getPosition() {
		return compPhysics.body.definition.getPosition();
	}
	
	@Override
	public float getRotation() {
		return (getOrientation() * MathUtils.radDeg + 90f);
	}
	
	@Override
	public float getOrientation() {
		return ArithmeticUtils.wrapAngleAroundZero(getDefinition().getBody().getAngle() - MathUtils.PI / 2f) + MathUtils.PI2;
	}

	@Override
	public Location<Vector2> newLocation() {
		// TODO
		return this;
	}

	@Override
	public float getZeroLinearSpeedThreshold() {
		return zero_linear;
	}

	@Override
	public float getMaxLinearSpeed() {
		return max_speed;
	}

	@Override
	public float getMaxLinearAcceleration() {
		return max_acceleration;
	}

	@Override
	/** Returns the maximum angular speed. In radians */
	public float getMaxAngularSpeed() {
		return max_rotation;
	}

	@Override
	public float getMaxAngularAcceleration() {
		return max_rotation_acceleration;
	}
	
	public FieldOfView<Vector2> getFieldOfView() {
		return fieldOfView;
	}
	
	public SteeringBehavior<Vector2> getBehavior() {
		return behavior;
	}
	
	/** Return the pathFinder for this Entity. */
	public CompAStar getPathFinder() {
		return compAStar;
	}
	
	@Override
	public boolean isTagged() {
		return isTagged;
	}
	

	public boolean isTransparent() {
		return compPhysics.isTransparent;
	}
	
	public boolean isTracking() {
		return isTracked;
	}
	
	/*
	public Node getNodeEntity() {
		if(worldObj.getLevel().getGridNodes() != null)
			return Node.getNodePos(x, y)
	}*/
	
	@Override
	/** In degres. */
	public void setMaxAngularSpeed(float maxAngularSpeed) {
		max_rotation = maxAngularSpeed;
	}
	
	@Override
	public void setMaxLinearAcceleration(float maxLinearAcceleration) {
		this.compVelocity.maxMotionR = maxLinearAcceleration;
		max_acceleration = maxLinearAcceleration;
	}
	
	@Override
	public void setMaxLinearSpeed(float maxLinearSpeed) {
		max_speed = maxLinearSpeed;
	}

	@Override
	/** In degres. */
	public void setMaxAngularAcceleration(float maxAngularAcceleration) {
		max_rotation_acceleration = maxAngularAcceleration;
	}
	
	@Override
	public void setZeroLinearSpeedThreshold(float value) {
		zero_linear = value;
	}
	
	@Override
	public void setRotation(float angle) {
		super.setRotation(angle);
		//this.setOrientation(angle * MathUtils.degRad);
	}
	
	@Override
	/** Set rotation in radians. */
	public void setOrientation(float orientation) {
		//control.angular = orientation;
		setRotation(orientation * MathUtils.radDeg + 90f);
	}
	
	public void setBehavior(SteeringBehavior<Vector2> behavior) {
		this.behavior = behavior;
	}
	
	public void setTransparent(boolean par1) {
		compPhysics.isTransparent = par1;
	}
	
	/** @param angle - in degree. */
	public void setFieldOfView(float angle, float radius) {
		if(fieldOfView == null) {
			fieldOfView = new FieldOfView<Vector2>(this, worldObj.getSteerList(), radius, angle);
		} else {
			fieldOfView.setAngle(angle);
			fieldOfView.setRadius(radius);
		}
	}
	
	@Override
	public void setTagged(boolean tagged) {
		isTagged = tagged;
	}
	
	@Override
	public void setTracking(boolean par1) {
		isTracked = par1;
	}

	@Override
	public boolean reportNeighbor(Steerable<Vector2> neighbor) {
		return true;
	}
	
	@Override
	public void handleEvent(AIEvent event) {
		super.handleEvent(event);
		
		if(event instanceof AIEventPlaySound) {
			AIEventPlaySound soundEvent = (AIEventPlaySound)event;
			
			//System.out.println("	Hearing effect:" + soundEvent.sound.getHearingEffect(this.getPosition()));
		}
	}
	
	@Override
	public void deleteObject() {
		super.deleteObject();
		worldObj.getManagerAI().removeListener(this);
		worldObj.getSteerList().removeValue(this, false);
		//worldObj.getSchedulerAI().removeValue(this);
		
	}

	@Override
	public void applySteering(SteeringAcceleration<Vector2> steering) {
		this.control_steer.control.angular = steering.angular;
		this.control_steer.control.linear.set(steering.linear);
		//System.out.println("control: " + control_steer.control.linear);
	}

	@Override
	public void executeAction(Action action) {}
	
	@Override
	public void damage(int damage) {
		setHP(getHP() - (int) ((float) damage / getBuff().getProtection()));
	}
}
