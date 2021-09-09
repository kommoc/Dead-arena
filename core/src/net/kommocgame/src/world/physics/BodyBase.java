package net.kommocgame.src.world.physics;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;

import net.kommocgame.src.entity.EntityBase;
import net.kommocgame.src.trigger.TriggerBase;

public class BodyBase {
	
	/** This is the main part of body. */
	public Body definition;
	public BodyDef body;
	public FixtureDef fDef;
	
	public PolygonShape figurePolygon;
	public CircleShape figureCircle;
	
	public Box2dWorld worldObj;
	
	/*****************************************************************/
	private Vector2 vector_transform = new Vector2();
	
	/** Create circle figure. */
	public BodyBase(Box2dWorld world, float radius, BodyType type, float x, float y) {
		worldObj = world;
		body = new BodyDef();
		fDef = new FixtureDef();
		body.type = type;
		
		body.position.set(x, y);
		
		figureCircle = new CircleShape();
		figureCircle.setRadius(radius);
		
		fDef.shape = figureCircle;
		
		definition = worldObj.getBox2d().createBody(body);
		definition.createFixture(fDef);
		
		figureCircle.dispose();
	}
	
	/** Create simple rectangle figure. */
	public BodyBase(Box2dWorld world, float hx, float hy, BodyType type, float x, float y) {
		this(world, hx, hy, new Vector2(0, 0), 0, type, x, y);
	}
	
	/** Create rectangle figure.
	 * @param angle - in degrees. */
	public BodyBase(Box2dWorld world, float hx, float hy, Vector2 center, float angle, BodyType type, float x, float y) {
		worldObj = world;
		body = new BodyDef();
		fDef = new FixtureDef();
		body.type = type;
		
		body.position.set(x, y);
		
		figurePolygon = new PolygonShape();
		figurePolygon.setAsBox(hx, hy, center, MathUtils.degreesToRadians * angle);
		
		fDef.shape = figurePolygon;
		
		definition = worldObj.getBox2d().createBody(body);
		definition.createFixture(fDef);
		
		figurePolygon.dispose();
	}
	
	/** Move on (x, y) local position in Box2d world. */
	public void translatePosition(float x, float y) {
		definition.setTransform(this.getPosistion().add(x, y), this.getRotation());
	}
	
	@Deprecated
	public void setEntityLink(EntityBase entity) {
		definition.setUserData(entity);
	}
	
	@Deprecated
	public void setTriggerLink(TriggerBase tr) {
		definition.setUserData(tr);
	}
	
	/** Set local position in Box2d world. */
	public void setPosition(float x, float y) {
		vector_transform.set(x, y);
		definition.setTransform(vector_transform, this.getRotation() * MathUtils.degreesToRadians);
	}
	
	/** Set rotation in degrees. */
	public void setRotation(float angle) {
		definition.setTransform(definition.getPosition(), angle * MathUtils.degreesToRadians);
	}
	
	/** Return local position in Box2d world.  */
	public Vector2 getPosistion() {
		return definition.getPosition();
	}
	
	/** Get rotation in degrees. */
	public float getRotation() {
		return definition.getTransform().getRotation() * MathUtils.radiansToDegrees;
	}
	
	public boolean deleteBody() {
		worldObj.getBox2d().destroyBody(definition);
		return true;
	}
	
	/*
	System.out.println("NEW BODY");
	//////////////////////////////////////
	BodyDef bodyDef = new BodyDef();
	// We set our body to dynamic, for something like ground which doesn't move we would set it to StaticBody
	bodyDef.type = BodyType.DynamicBody;
	// Set our body's starting position in the world
	bodyDef.position.set(100, 300);

	// Create our body in the world using our body definition
	Body body = worldObj.box2dWorld.createBody(bodyDef);

	// Create a circle shape and set its radius to 6
	CircleShape circle = new CircleShape();
	circle.setRadius(6f);

	// Create a fixture definition to apply our shape to
	FixtureDef fixtureDef = new FixtureDef();
	fixtureDef.shape = circle;
	fixtureDef.density = 0.5f; 
	fixtureDef.friction = 0.4f;
	fixtureDef.restitution = 0.6f; // Make it bounce a little bit

	// Create our fixture and attach it to the body
	Fixture fixture = body.createFixture(fixtureDef);

	// Remember to dispose of any shapes after you're done with them!
	// BodyDef and FixtureDef don't need disposing, but shapes do.
	circle.dispose();
	*/
}
