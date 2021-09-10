package net.kommocgame.src.entity.AI.event;

import net.kommocgame.src.SoundEffect;
import net.kommocgame.src.entity.EntityBase;
import net.kommocgame.src.world.World;

public class AIEventPlaySound extends AIEvent {
	
	public SoundEffect sound;
	
	public AIEventPlaySound(World world, SoundEffect sound) {
		this(null, world, sound);
	}

	public AIEventPlaySound(EntityBase owner, World world, SoundEffect sound) {
		super(owner, world);
		this.sound = sound;
	}
}
