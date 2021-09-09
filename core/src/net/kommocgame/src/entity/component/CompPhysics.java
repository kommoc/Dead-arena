package net.kommocgame.src.entity.component;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;

import net.kommocgame.src.entity.EntityBase;
import net.kommocgame.src.world.physics.BodyBase;
import net.kommocgame.src.world.physics.Box2dWorld;

public class CompPhysics implements Component {
	
	public BodyBase body;
	private Box2dWorld world;
	
	private float posX = 0f;
	private float posY = 0f;
	
	public float linearDamping = 10f;
	
	public boolean canCollide = true;
	public boolean canCollideWithEntity = true;
	public boolean canCollideWithFixture = true;
	
	/** Cross-beam. */
	public boolean isTransparent = true;
	
	public CompPhysics(Box2dWorld world, float x, float y) {
		posX = x;
		posY = y;
		this.world = world;
	}
	
	public float getDamping() {
		return linearDamping;
	}
	
	public void deleteBody() {
		if(body != null) {
			body.worldObj.getBox2d().destroyBody(body.definition);
		}
	}
	
	public void setCircle(EntityBase entity, float radius, BodyType type) {
		deleteBody();
		body = new BodyBase(world, radius, type, posX, posY);
		body.setEntityLink(entity);
	}
	
	public void setSimpleRect(EntityBase entity, float hx, float hy, BodyType type) {
		deleteBody();
		body = new BodyBase(world, hx, hy, type, posX, posY);
		body.setEntityLink(entity);
	}
	
	public void setRect(EntityBase entity, float hx, float hy, BodyType type, Vector2 center, float angle) {
		deleteBody();
		body = new BodyBase(world, hx, hy, center, angle, type, posX, posY);
		body.setEntityLink(entity);
	}

}
