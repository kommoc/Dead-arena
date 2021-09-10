package net.kommocgame.src.DeadArena.entity;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;

import net.kommocgame.src.trigger.ICondition;
import net.kommocgame.src.trigger.TriggerRect;
import net.kommocgame.src.world.World;

public class DATriggerSpawner extends TriggerRect {

	public DATriggerSpawner(World world, ICondition icondition, long time, float x, float y, BodyType type) {
		super(world, icondition, time, x, y, type);
		
	}

	public DATriggerSpawner(World world, ICondition icondition, long time, float x, float y, float hx, float hy,
			BodyType type) {
		super(world, icondition, time, x, y, hx, hy, type);
		
	}

	public DATriggerSpawner(World world, ICondition icondition, long time, float x, float y, String name,
			boolean canRepeat, float hx, float hy, float angle, BodyType type) {
		super(world, icondition, time, x, y, name, canRepeat, hx, hy, angle, type);
		
	}

	public DATriggerSpawner(World world, ICondition icondition, long time, float x, float y, String name,
			boolean canRepeat, float hx, float hy, float angle, Vector2 center, BodyType type) {
		super(world, icondition, time, x, y, name, canRepeat, hx, hy, angle, center, type);
		
	}

}
