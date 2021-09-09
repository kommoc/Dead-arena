package net.kommocgame.src.entity.AI;

import com.badlogic.gdx.ai.GdxAI;
import com.badlogic.gdx.ai.steer.Steerable;
import com.badlogic.gdx.ai.steer.proximities.FieldOfViewProximity;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.RayCastCallback;
import com.badlogic.gdx.utils.Array;

import net.kommocgame.src.entity.EntityBase;
import net.kommocgame.src.entity.EntityLiving;
import net.kommocgame.src.entity.AI.asynch.SteerableImp;

public class FieldOfView<T extends Vector<T>> extends FieldOfViewProximity<T> implements RayCastCallback {
	
	private Steerable<T> owner;
	private Array<? extends Steerable<T>> agents;
	
	private EntityLiving reportedEntity;
	private boolean fov_report = false;
	
	/** If true - for FOV, if false - check. */
	private boolean switch_check = false;
	
	private final boolean MODE_CHECK = false;
	private final boolean MODE_FOV = true;
	
	private boolean check_report = false;
	
	protected float angle_view;
	protected float angle_owner;
	private float lastTime;
	private T ownerOrientation;
	private T toAgent;
	
	public FieldOfView(Steerable<T> owner, Array<? extends Steerable<T>> agents, float radius, float angle) {
		super(owner, agents, radius, angle);
		this.owner = owner;
		this.agents = agents;
		this.radius = radius;
		angle_view = angle / 2f;
		
		this.lastTime = 0;
		this.ownerOrientation = owner.getPosition().cpy().setZero();
		this.toAgent = owner.getPosition().cpy().setZero();
	}
	
	@Override
	public int findNeighbors (ProximityCallback<T> callback) {
		int neighborCount = 0;
		switch_check = MODE_FOV;

		if (this.lastTime != GdxAI.getTimepiece().getTime()) {
			this.lastTime = GdxAI.getTimepiece().getTime();
			
			owner.angleToVector(ownerOrientation, owner.getOrientation());
			
			for (int i = 0; i < agents.size; i++) {
				Steerable<Vector2> currentAgent = (Steerable<Vector2>) agents.get(i);

				if (currentAgent != owner) {
					toAgent.set((T) currentAgent.getPosition()).sub(owner.getPosition());
					float range = radius + currentAgent.getBoundingRadius();
					
					if (toAgent.len() < range) {
						//if (180f - Math.abs(((Vector2) ownerOrientation).angle((Vector2) ownerOrientation.cpy().sub(toAgent))) < angle_view) {
						if (180f - Math.abs(owner.getOrientation() * MathUtils.radDeg - ((Vector2) toAgent).angle()) < angle_view) {
							reportedEntity = (EntityLiving) currentAgent;	//raycast check
							fov_report = true;
							
							if(((Vector2) owner.getPosition()).dst(currentAgent.getPosition()) > 0)
								((EntityLiving)owner).worldObj.physics.rayCast(this, (Vector2) owner.getPosition(), currentAgent.getPosition());
							//((EntityLiving)owner).worldObj.physics.box2dWorld.rayCast(this, (Vector2) owner.getPosition().cpy(), currentAgent.getPosition().cpy());
							else System.out.println("	FOW.findNeighbors() ### null raycast!");
							
							if (fov_report && ((com.badlogic.gdx.ai.steer.Proximity.ProximityCallback<T>) owner).reportNeighbor((Steerable<T>) currentAgent)) {
								currentAgent.setTagged(true);
								neighborCount++;
								continue;
							}
						}
					}
				}

				currentAgent.setTagged(false);
			}
		} else {
			for (int i = 0; i < agents.size; i++) {
				Steerable<Vector2> currentAgent = (Steerable<Vector2>) agents.get(i);
				
				if (currentAgent != owner && currentAgent.isTagged()) {
					reportedEntity = (EntityLiving) currentAgent;
					fov_report = true;
					
					if(((Vector2) owner.getPosition()).dst(currentAgent.getPosition()) > 0)
						((EntityLiving)owner).worldObj.physics.rayCast(this, (Vector2) owner.getPosition(), currentAgent.getPosition());
					//((EntityLiving)owner).worldObj.physics.box2dWorld.rayCast(this, (Vector2) owner.getPosition().cpy(), currentAgent.getPosition().cpy());
					else System.out.println("	FOW.findNeighbors() ### null raycast!");
					
					if (fov_report && ((com.badlogic.gdx.ai.steer.Proximity.ProximityCallback<T>) owner).reportNeighbor((Steerable<T>) currentAgent)) {
						neighborCount++;
					}
				}
			}
		}
		
		return neighborCount;
	}
	
	/** Does entity see this point. */
	/*public boolean checkPoint(Vector2 vec) {
		this.switch_check = MODE_CHECK;
		check_report = true;
		
		((EntityLiving)owner).worldObj.physics.box2dWorld.rayCast(this, (Vector2)owner.getPosition(), vec);
		
		return check_report;
	}*/
	
	/** Does entity see this point. */
	public boolean checkPoint(final Vector2 vec) {
		this.switch_check = MODE_CHECK;
		check_report = true;
		
		if(((Vector2)owner.getPosition()).dst(vec) > 0)
			((SteerableImp)owner).worldObj.physics.rayCast(this, (Vector2)owner.getPosition(), vec);
		else System.out.println("FieldOfView.raycast() ### raycast is not created! dst == 0.");
		
		return check_report;
	}
	
	/** Sets the angle of this proximity in radians. */
	public void setAngle (float angle) {
		super.setAngle(angle);
		angle_view = angle / 2f;
	}

	@Override
	public float reportRayFixture(Fixture fixture, Vector2 point, Vector2 normal, float fraction) {
		// -1: ignore and contitnue
		// 	0: terminate ray return fraction
		//  1: don't clip and continue
		if(switch_check == MODE_FOV) {
			//System.out.println("MODE_FOV");
			if(!fov_report) 
				return 0;
			
			if(fixture.getBody().getUserData() instanceof EntityBase) {
				EntityBase entity = (EntityBase) fixture.getBody().getUserData();
			
				if(!entity.isTransparent()) {
					fov_report = false;
					return 0;
				}
			}
			
			return -1;
		} else if(switch_check == MODE_CHECK) {
			//System.out.println("MODE_CHECK");
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
		
		return 0;
	}
}
