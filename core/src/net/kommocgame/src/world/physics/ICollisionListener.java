package net.kommocgame.src.world.physics;

import com.badlogic.gdx.physics.box2d.Fixture;

import net.kommocgame.src.entity.EntityBase;

public interface ICollisionListener {
	
	boolean shouldCollideWithEntity(EntityBase entity);
	
	boolean shouldCollideWithFixture(Fixture fixture);
	
	void onCollideWithEntity(EntityBase entity);
	
	void onCollideWithFixture(Fixture entity);
	
}
