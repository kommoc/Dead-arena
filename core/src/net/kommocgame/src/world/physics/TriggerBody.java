package net.kommocgame.src.world.physics;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;

public class TriggerBody extends BodyBase {
	
	/** Create circle figure. */
	public TriggerBody(Box2dWorld world, float radius, BodyType type, float x, float y) {
		super(world, radius, type, x, y);
		fDef.isSensor = true;
		
	}
	
	/** Create simple rectangle figure. */
	public TriggerBody(Box2dWorld world, float hx, float hy, BodyType type, float x, float y) {
		this(world, hx, hy, new Vector2(0, 0), 0, type, x, y);
	}
	
	/** Create rectangle figure.
	 * @param angle - in degrees. */
	public TriggerBody(Box2dWorld world, float hx, float hy, Vector2 center, float angle, BodyType type, float x,
			float y) {
		super(world, hx, hy, center, angle, type, x, y);
		fDef.isSensor = true;
	}
}
