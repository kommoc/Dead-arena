package net.kommocgame.src.trigger;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.scenes.scene2d.ui.Image;

import net.kommocgame.src.Loader;
import net.kommocgame.src.world.World;
import net.kommocgame.src.world.physics.TriggerBody;

public class TriggerRect extends TriggerBase {
	
	public TriggerRect(World world, ICondition icondition, long time, float x, float y, BodyType type) {
		//this(world, icondition, time, x, y, "trigger", false, 5, type);
		this(world, icondition, time, x, y, "trigger", false, 5, 5, 0, type);
	}

	/** Trigger rectangle.
	 * @param time 1 - 1/1000 second. */
	public TriggerRect(World world, ICondition icondition, long time, float x, float y, float hx, float hy, BodyType type) {
		super(icondition, x, y);
		this.worldObj = world;
		this.bounds = new TriggerBody(worldObj.physics, hx, hy, new Vector2(0, 0), 0, type, x, y);
		this.bounds.setTriggerLink(this);
		this.timeIn = time;
		
		this.image_trigger = new Image(Loader.guiIcon("icon_trigger_rect.png"));
	}
	
	/** Trigger rectangle.
	 * @param time 1 - 1/1000 second. */
	public TriggerRect(World world, ICondition icondition, long time, float x, float y, String name, boolean canRepeat, float hx, float hy, float angle, BodyType type) {
		super(icondition, x, y, name, canRepeat);
		this.worldObj = world;
		this.bounds = new TriggerBody(worldObj.physics, hx, hy, new Vector2(0, 0), angle, type, x, y);
		this.bounds.setTriggerLink(this);
		this.timeIn = time;
		
		this.image_trigger = new Image(Loader.guiIcon("icon_trigger_rect.png"));
	}
	
	/** Trigger rectangle.
	 * @param time 1 - 1/1000 second. */
	public TriggerRect(World world, ICondition icondition, long time, float x, float y, String name, boolean canRepeat, float hx, float hy, float angle, Vector2 center,BodyType type) {
		super(icondition, x, y, name, canRepeat);
		this.worldObj = world;
		this.bounds = new TriggerBody(worldObj.physics, hx, hy, center, angle, type, x, y);
		this.bounds.setTriggerLink(this);
		this.timeIn = time;
		
		this.image_trigger = new Image(Loader.guiIcon("icon_trigger_rect.png"));
	}
}
