package net.kommocgame.src.entity.AI.event;

import net.kommocgame.src.entity.EntityBase;
import net.kommocgame.src.world.World;

public class AIEvent {
	
	/** May be null. */
	public EntityBase owner;
	
	/** WorldObj instance. */
	public World world;
	
	boolean isCanceled = false;
	boolean isCancelable = true;
	
	public AIEvent(World world) {
		this(null, world);
	}
	
	public AIEvent(EntityBase owner, World world) {
		this.owner = owner;
		this.world = world;
	}
}
