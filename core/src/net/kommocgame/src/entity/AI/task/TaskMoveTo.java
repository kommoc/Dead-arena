package net.kommocgame.src.entity.AI.task;

import com.badlogic.gdx.ai.steer.behaviors.Arrive;
import com.badlogic.gdx.ai.steer.limiters.LinearLimiter;
import com.badlogic.gdx.ai.utils.Location;
import com.badlogic.gdx.math.Vector2;

import net.kommocgame.src.entity.EntityLiving;
import net.kommocgame.src.entity.AI.GraphPath;
import net.kommocgame.src.entity.AI.LocImp;
import net.kommocgame.src.world.World;

public class TaskMoveTo extends Task {
	
	private Vector2 vec_target;
	
	private Location<Vector2> target;
	private LocImp to_pos = new LocImp(0, 0);
	private Arrive<Vector2> moveTo;
	private LinearLimiter limiter;
	
	private GraphPath path;
	private Vector2 prev_pos;
	private Vector2 curr_pos;
	
	private boolean init_path = false;
	
	private boolean setInitWhenExec = false;
	private boolean inited = false;
	
	public TaskMoveTo(EntityLiving entity, int priority, Vector2 target) {
		this(entity, priority, target, false);
	}
	
	public TaskMoveTo(EntityLiving entity, int priority, Vector2 target, boolean postInit) {
		super(entity, priority);
		vec_target = target;
		setInitWhenExec = postInit;
		
		if(!postInit) {
			this.target = new LocImp(target);
			
			moveTo = new Arrive<Vector2>(entity, this.target);
			
			if(!entity.getFieldOfView().checkPoint(target)) {
				entity.getPathFinder().createPath(target.x, target.y);
				path = entity.getPathFinder().getGraphPath();
			}
			
			limiter = new LinearLimiter(entity.getMaxLinearAcceleration(), entity.getMaxLinearSpeed());
			//System.out.println("Linear speed: " + entity.getMaxLinearSpeed());
			//System.out.println("Linear acceleration: " + entity.getMaxLinearAcceleration());
			
			moveTo.setDecelerationRadius(entity.getBoundingRadius());
			moveTo.setLimiter(limiter);
			moveTo.setEnabled(true);
			
			inited = true;
		}
	}

	@Override
	public void execute(World world, EntityLiving entity, float deltaTime) {
		if(setInitWhenExec && !inited) {
			this.target = new LocImp(vec_target.cpy().add(entity.getPosition()));
			
			moveTo = new Arrive<Vector2>(entity, this.target);
			
			if(!entity.getFieldOfView().checkPoint(vec_target)) {
				entity.getPathFinder().createPath(vec_target.x, vec_target.y);
				path = entity.getPathFinder().getGraphPath();
			}
			
			limiter = new LinearLimiter(entity.getMaxLinearAcceleration(), entity.getMaxLinearSpeed());
			
			moveTo.setDecelerationRadius(entity.getBoundingRadius());
			moveTo.setLimiter(limiter);
			moveTo.setEnabled(true);
			
			inited = true;
		}
		
		if(!init_path && entity.getPathFinder().isRecive()) {
			init_path = true;
			
			for(int i = 0; i < entity.getPathFinder().getGraphPath().nodes.size; i++) {
				entity.getPathFinder().getNextPoint(curr_pos).add(0.5f, 0.5f);
				
				if(entity.getFieldOfView().checkPoint(curr_pos)) {
					prev_pos = curr_pos;
					to_pos = new LocImp(prev_pos);
				} else {
					break;
				}
			}
		}
		
		if(path != null && entity.getPathFinder().isRecive()) {
			if(moveTo.getTarget().getPosition() != prev_pos)
				moveTo.setTarget(to_pos);
			
			if(curr_pos != null && entity.getFieldOfView().checkPoint(curr_pos)) {
				prev_pos = curr_pos;
				to_pos = new LocImp(prev_pos);
				entity.getPathFinder().getNextPoint(curr_pos).add(0.5f, 0.5f);
			}
		}
		
		if(entity.getBehavior() != moveTo) {
			entity.setBehavior(moveTo);
		}
		
		if(target.getPosition().dst(moveTo.getOwner().getPosition()) <= moveTo.getDecelerationRadius()) {
			entity.setBehavior(null);
			this.setTaskDone();
		}
		
		if(!entity.getFieldOfView().checkPoint(target.getPosition()) && path == null) {
			//System.out.println("path.");
		}
	}
}
