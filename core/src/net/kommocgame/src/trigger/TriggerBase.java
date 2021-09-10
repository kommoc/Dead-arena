package net.kommocgame.src.trigger;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.StringBuilder;
import com.badlogic.gdx.utils.TimeUtils;
import com.badlogic.gdx.utils.Json.Serializable;
import com.badlogic.gdx.utils.JsonValue;

import net.kommocgame.src.entity.EntityBase;
import net.kommocgame.src.world.AdditionalData;
import net.kommocgame.src.world.DataObject;
import net.kommocgame.src.world.World;
import net.kommocgame.src.world.WorldObject;
import net.kommocgame.src.world.physics.ICollisionListener;
import net.kommocgame.src.world.physics.TriggerBody;

public class TriggerBase extends Entity implements ICollisionListener, WorldObject {
	
	public World worldObj;
	private AdditionalData userData;
	private int layer_index;
	
	/** Object body. */
	public TriggerBody bounds;	//
	
	/** For fix trigger delete. */
	private boolean deleted = false;
	
	/** Name of trigger. */
	private String name;
	
	/** Show the trigger is active now. */
	private boolean active;
	
	/** Show the trigger has activated. */
	private boolean activated;
	
	/** Can it be repeat again. */
	private boolean canRepeat;
	
	/** Trigger will be activate by timeBefore has ended. 
	 * @param timeBefore - time before start. */
	private long timeBefore = 0;
	
	/** Time, among that have apply condition. */
	protected  long timeIn;
	
	/** Progress executing a script. */
	private boolean execute = false;
	
	private long currentTime = TimeUtils.millis();
	private long activateConditionTime = TimeUtils.millis();
	private long activateTriggerTime = TimeUtils.millis();
	
	private EntityBase attachEntity;
	
	/** Return condition. */
	private boolean cond = false;
	
	/** Trigger image for KomEditor. */
	protected Image image_trigger;
	
	/** Condition of trigger and execute method. ICondition should have a method setExec(). */
	protected ICondition condition;
	
	/** Full list of entities which contact with trigger. */
	private Array<EntityBase> contacts = new Array<EntityBase>();
	
	public TriggerBase(ICondition icondition, float x, float y) {
		this(icondition, x, y, "trigger", false);
	}
	
	public TriggerBase(ICondition icondition, float x, float y, String name, boolean canRepeat) {
		//body.x = x;
		//body.y = y;
		userData = new AdditionalData();
		this.setLayerIndex(10);
		
		this.name = name;
		this.canRepeat = canRepeat;
		condition = icondition;
		
		getAdditionalData().getParameters().add(new DataObject<Integer>("layer_index", getLayerIndex()) {
			@Override
			public void setParameter(Integer par1) {
				setLayerIndex(par1);
			}

			@Override
			public Integer getParameter() {
				return getLayerIndex();
			}
		});
		
		getAdditionalData().getParameters().add(new DataObject<String>("name", getName()) {
			@Override
			public void setParameter(String par1) {
				setName(par1);
			}

			@Override
			public String getParameter() {
				return getName();
			}
		});
	}
	
	/** Put in contacts list entityObject. */
	public void contactPut(EntityBase entity) {
		if(!this.contacts.contains(entity, true))
			this.contacts.add(entity);
	}
	
	/** Remove from contacts list entityObject. */
	public void contactDelete(EntityBase entity) {
		if(this.contacts.contains(entity, true))
			this.contacts.removeValue(entity, true);
	}
	
	public void setTimeIn(long time) {
		this.timeIn = time;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public void setTimeBefore(long time) {
		this.timeIn = time;
	}
	
	/** Need to finish executing trigger. */
	public void setExec() {
		execute = false;
	}
	
	/** Set position of the Trigger. */
	public void setPosition(float x, float y) {
		//System.out.println("x->" + x + " y->" + y);
		//System.out.println("position: " + bounds.getPosistion());
		bounds.definition.setTransform(x, y, 0);
	}
	
	@Override
	public void setLayerIndex(int par1) {
		layer_index = par1;
	}
	
	/** Attach to current entity. */
	public TriggerBase attachTo(EntityBase entity) {
		if(entity != null) {
			this.attachEntity = entity;
		}
		
		return this;
	}
	
	/** Full list of entities which contact with trigger. */
	public Array<EntityBase> getContactList() {
		return contacts;
	}
	
	/** Condition of trigger and execute method. ICondition should have a method setExec(). */
	public ICondition getICondition() {
		return condition;
	}
	
	/** Progress executing a script. */
	public boolean isExecuting() {
		return execute;
	}
	
	/** Time, among that have apply condition. */
	public long getTimeIn() {
		return timeIn;
	}
	
	/** Trigger will be activate by timeBefore has ended. */
	public long getTimeBefore() {
		return timeBefore;
	}
	
	/** Can it be repeat again. */
	public boolean canRepeat() {
		return canRepeat;
	}
	
	/** Name of trigger. */
	public String getName() {
		return name;
	}
	
	/** Object body. */
	public Body getDefinition() {
		return bounds.definition;
	}
	
	@Override
	public AdditionalData getAdditionalData() {
		return userData;
	}
	
	/** Return the position of this trigger. */
	public Vector2 getPosition() {
		return getDefinition().getPosition();
	}
	
	/** Get rotation in radians. */
	public float getRotation() {
		return getDefinition().getTransform().getRotation();
	}
	
	public Fixture getFixture() {
		if(bounds.definition != null && bounds.definition.getFixtureList().size > 0)
			return bounds.definition.getFixtureList().get(0);
		else return null;
	}
	
	/** Return attached entity. */
	public EntityBase getAttachedEntity() {
		return this.attachEntity;
	}
	
	@Override
	public int getLayerIndex() {
		return layer_index;
	}
	
	/** Return the trigger image for KomEditor. */
	public Image getImage() {
		return image_trigger;
	}
	
	/** Show the trigger is active now. */
	public boolean isActive() {
		return this.active;
	}
	
	/** Show the trigger has activated. */
	public boolean isActiveted() {
		return this.activated;
	}
	
	@Override
	public boolean isDeleted() {
		return deleted;
	}
	
	/** Reset trigger. */
	public void reset() {
		activated = false;
		active = false;
		execute = false;
		
		activateConditionTime = currentTime;
		activateTriggerTime = currentTime;
	}
	
	protected boolean checkRepeat() {
		return canRepeat || !activated;
	}
	
	protected boolean checkCondTime() {
		if(currentTime - timeIn >= activateConditionTime)
			return true;
		
		return false;
	}
	
	protected boolean checkExecTime() {
		if(activateTriggerTime + timeBefore <= currentTime)
			return true;
		
		return false;
	}
	
	public boolean shouldCollideWithEntity(EntityBase entity) {
		return true;
	}
	
	public boolean shouldCollideWithFixture(Fixture fixture) {
		return true;
	}
	
	public void onCollideWithEntity(EntityBase entity) {}
	
	public void onCollideWithFixture(Fixture entity) {}
	
	/** TODO add if-instance if the instance is deleted. */
	public void update() {
		if(attachEntity != null) {
			this.setPosition(attachEntity.getPosition().x, attachEntity.getPosition().y);
		}
		
		currentTime = TimeUtils.millis();
		
		if(!cond && this.condition.condition(this))
			activateConditionTime = currentTime;
		
		cond = condition.condition(this);
		
		if(cond && checkCondTime() && checkRepeat()) {
			activateConditionTime = currentTime;
			activateTriggerTime = currentTime;
			active = true;
			activated = true;
			execute = true;
		} else {
			active = false;
		}
		
		if(execute && checkExecTime()) {
			condition.execute(this);
		}
	}
	
	/** Delete trigger from the world. */
	@Deprecated
	private void deleteTrigger() {
		if(isDeleted()) {
			System.out.println("		TRIGGER. trying to delete deleted instance!");
			
			return;
		}
		
		Array<Fixture> array = new Array<Fixture>();
		worldObj.physics.getBox2d().getFixtures(array);
		
		if(array.contains(getFixture(), false)) {
			System.out.println("		TRIGGER. Delete trigger");
			worldObj.physics.getBox2d().destroyBody(bounds.definition);
		} else {
			System.out.println("		TRIGGER. that trigger is not in world!");
		}
		
		if(worldObj.getEngine().getEntities().contains(this, false))
			worldObj.getEngine().removeEntity(this);
		deleted = true;
	}
	
	@Override
	public String toString() {
		return new StringBuilder("Trigger name: ").append(name).append("; isActive: " + isActive()).append("; is Activated: " + isActiveted()).toString();
	}

	@Override
	public void deleteObject() {
		worldObj.getDeleteList().add(this);
	}

	@Override
	public void del() {
		this.deleteTrigger();
	}
}