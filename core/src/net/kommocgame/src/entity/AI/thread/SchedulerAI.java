package net.kommocgame.src.entity.AI.thread;

import com.badlogic.gdx.ai.sched.LoadBalancingScheduler;
import com.badlogic.gdx.ai.sched.Schedulable;
import com.badlogic.gdx.ai.steer.SteeringAcceleration;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

import net.kommocgame.src.entity.EntityLiving;
import net.kommocgame.src.world.World;

public class SchedulerAI extends LoadBalancingScheduler {
	
	//World world;
	
	public SchedulerAI(int dryRunFrames) {
		super(dryRunFrames);
		//this.world = world;
	}
	
	public void removeValue(EntityLiving entity) {
		if(schedulableRecords.contains(entity.control_steer.getRecord(), false)) {
			schedulableRecords.removeValue(entity.control_steer.getRecord(), false);
		}
		
		if(runList.contains(entity.control_steer.getRecord(), false)) {
			runList.removeValue(entity.control_steer.getRecord(), false);
		}
	}
	
	public void setRecord(SteeringControl scheduler) {
		scheduler.setRecord(schedulableRecords.peek());
	}
	
	public static class SteeringControl implements Schedulable {
		public SteeringAcceleration<Vector2> control = new SteeringAcceleration<Vector2>(new Vector2());
		private SchedulableRecord record;
		private EntityLiving entity;
		
		public SteeringControl(EntityLiving entity) {
			this.entity = entity;
		}
		
		public void setRecord(SchedulableRecord record) {
			this.record = record;
		}
		
		public SchedulableRecord getRecord() {
			return record;
		}
		
		@Override
		public void run(long nanoTimeToRun) {
			if (entity.getBehavior() != null) {
				//BehaviorManager.addSteeringRequest(entity, entity.control_steer.control, entity.getBehavior());
			}
		}
	}
}
