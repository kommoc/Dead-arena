package net.kommocgame.src.trigger;

import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.scenes.scene2d.ui.Image;

import net.kommocgame.src.Loader;
import net.kommocgame.src.world.World;
import net.kommocgame.src.world.physics.TriggerBody;

public class TriggerCircle extends TriggerBase {
	
	/** Trigger circle.
	 * @param time 1 - 1/1000 second. */
	public TriggerCircle(World world, ICondition icondition, long time, float x, float y, BodyType type) {
		this(world, icondition, time, x, y, "trigger", false, 5, type);
	}
	
	/** Trigger circle.
	 * @param time 1 - 1/1000 second. */
	public TriggerCircle(World world, ICondition icondition, long time, float x, float y, float radius, BodyType type) {
		this(world, icondition, time, x, y, "trigger", false, radius, type);
	}
	
	/** Trigger circle.
	 * @param time 1 - 1/1000 second. */
	public TriggerCircle(World world, ICondition icondition, long time, float x, float y, String name, boolean canRepeat, float radius, BodyType type) {
		super(icondition, x, y, name, canRepeat);
		this.worldObj = world;
		this.bounds = new TriggerBody(world.physics, radius, type, x, y);
		this.bounds.setTriggerLink(this);
		this.timeIn = time;
		this.image_trigger = new Image(Loader.guiIcon("icon_trigger_circle.png"));
	}
}
