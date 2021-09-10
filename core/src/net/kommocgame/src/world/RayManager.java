package net.kommocgame.src.world;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.RayCastCallback;

import net.kommocgame.src.entity.EntityBase;
import net.kommocgame.src.entity.AI.task.CollisionAvoid.RayCastCollisionAvoidance;

public class RayManager {

	private boolean check_report = false;
	private RayCastCollisionAvoidance RCCA;
	
	World world;
	
	RayCastCallback callback_checkPoint;
	RayCastCallback callback_collisionAvoid;
	
	
	public RayManager(World world) {
		this.world = world;
		
		callback_checkPoint = new RayCastCallback() {
			
			@Override
			public float reportRayFixture(Fixture fixture, Vector2 point, Vector2 normal, float fraction) {
				// -1: ignore and contitnue
				// 	0: terminate ray return fraction
				//  1: don't clip and continue
				if(!check_report) 
					return 0;
				
				if(fixture.getBody().getUserData() instanceof EntityBase) {
					EntityBase entity = (EntityBase) fixture.getBody().getUserData();
				
					if(!entity.isTransparent()) {
						check_report = false;
						return 0;
					}
				}
				
				return -1;
			}
		};
		
		callback_collisionAvoid = new RayCastCallback() {
			
			@Override
			public float reportRayFixture(Fixture fixture, Vector2 point, Vector2 normal, float fraction) {
				if(fixture.getBody().getType() == BodyType.StaticBody && fixture.getBody().getUserData() instanceof EntityBase) {
					RCCA.reportRayFixture(point, normal);
				}
				
				return 0;
			}
		};
	}
	
	public void collisionAvoidRaycast(RayCastCollisionAvoidance RCCA, Vector2 fromPoint, Vector2 toPoint) {
		this.RCCA = RCCA;
		
		if(fromPoint.dst(toPoint) > 0)
			world.physics.rayCast(callback_collisionAvoid, fromPoint, toPoint);
		//else System.out.println("FieldOfView.raycast() ### raycast is not created! dst == 0.\n	fromPoint	" + fromPoint + 
		//		"\n	toPoint	" + toPoint + "\n--------------------------------------------------");
	}

	/** Does entity see this point. */
	public boolean checkPoint(Vector2 fromPoint, Vector2 toPoint) {
		check_report = true;
		
		if(fromPoint.dst(toPoint) > 0)
			world.physics.rayCast(callback_checkPoint, fromPoint, toPoint);
		//else System.out.println("FieldOfView.raycast() ### raycast is not created! dst == 0.\n	fromPoint	" + fromPoint + 
		//		"\n	toPoint	" + toPoint + "\n--------------------------------------------------");
		
		return check_report;
	}
}
