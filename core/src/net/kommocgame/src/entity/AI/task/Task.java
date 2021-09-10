package net.kommocgame.src.entity.AI.task;

import com.badlogic.gdx.utils.ArrayMap;

import net.kommocgame.src.entity.EntityLiving;
import net.kommocgame.src.world.World;

public abstract class Task {
	
	/** Main list. */
	private ArrayMap<Task, Integer> task_list;
	
	/** Owner of this task. */
	private EntityLiving owner;
	
	/** WorldObj. */
	private World world;
	
	/** Priority of this task. */
	private int priority;
	
	/** State of this task. */
	private int state = Task.TASK_IDLE;
	
	public final static int TASK_IDLE = 0;
	public final static int TASK_EXECUTING = 1;
	public final static int TASK_CANCEL = 2;
	public final static int TASK_FAIL = 3;
	public final static int TASK_DONE = 4;
	
	public Task(EntityLiving entity, int priority) {
		owner = entity;
		world = entity.worldObj;
		this.priority = priority;
		//this.task_list = entity.getAI().getTaskList();
	}
	
	public void exec() {
		if(state > 0) {
			System.out.println("	TASK: " + this + "\n	is already started. STATE: " + (state == 1 ? "EXECUTING" :
				state == 2 ? "CANCEL" : state == 3 ? "FAIL" : state == 4 ? "DONE" : "ERROR"));
			return;
		} else if(world == null || owner == null || owner.isDead()) {
			System.out.println("	TASK: " + this + "\n	condition is not been done.");
			return;
		}
		
		this.state = Task.TASK_EXECUTING;
	}
	
	public abstract void execute(World world, EntityLiving entity, float deltaTime);
	
	public int getState() {
		return state;
	}
	
	public void setTaskDone() {
		this.state = Task.TASK_DONE;
		
		// REMOVE FROM TASK_LIST
		//task_list.removeKey(task_list.keys().toArray().get(task_list.keys().toArray().indexOf(this, false)));
		if(task_list.containsKey(this))
			task_list.removeIndex(task_list.keys().toArray().indexOf(this, false));
		
		System.out.println("	" + this + " : is done.");
	}
	
	public void setTaskFail() {
		this.state = Task.TASK_FAIL;
		
		// REMOVE FROM TASK_LIST
		if(task_list.containsKey(this))
			task_list.removeIndex(task_list.keys().toArray().indexOf(this, false));
		
		System.out.println("	" + this + " : is fail.");

	}
	
	public void setTaskCancel() {
		this.state = Task.TASK_CANCEL;
		
		// REMOVE FROM TASK_LIST
		if(task_list.containsKey(this))
			task_list.removeIndex(task_list.keys().toArray().indexOf(this, false));
		
		System.out.println("	" + this + " : has canceled.");
	}
	
	public void setIdle(EntityLiving entity) {
		System.out.println("		TASK: [" + this + "] set Entity [" + entity + "] to idle state.");
		entity.compPhysics.body.definition.setAngularVelocity(0);
		entity.compPhysics.body.definition.setLinearVelocity(0, 0);
	}
	
	public boolean isIdle() {
		return state == 0 ? true : false;
	}
	
	public boolean isExecuting() {
		return state == 1 ? true : false;
	}
	
	public boolean isCancel() {
		return state == 2 ? true : false;
	}
	
	public boolean isFail() {
		return state == 3 ? true : false;
	}
	
	public boolean isDone() {
		return state == 4 ? true : false;
	}
	
	public boolean isFinish() {
		return isCancel() || isDone() || isFail();
	}
}
