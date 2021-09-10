package net.kommocgame.src.entity.AI.Requerements;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;

import net.kommocgame.src.entity.EntityLiving;
import net.kommocgame.src.entity.AI.task.EnumTaskPriority;
import net.kommocgame.src.entity.AI.task.TaskFaceTo;
import net.kommocgame.src.entity.AI.task.TaskMoveTo;

public class RRandomWalking extends Requerement {
	
	private long lastTime = System.currentTimeMillis();
	private long timer = 5000l;
	private int threshold = 5000;
	private long rand;
	
	private float radius = 10f;
	private TaskMoveTo task_move;
	private TaskFaceTo task_face;
	
	/************** TEST *************/
	
	
	public RRandomWalking(EntityLiving entity, long timer, int threshold) {
		super(entity);
		this.timer = timer;
		this.threshold = threshold;
		
		rand = MathUtils.random(-threshold / 2, threshold / 2);
	}

	@Override
	@Deprecated
	public void update() {
		/*
		if(lastTime + timer + rand < System.currentTimeMillis()) {
			rand = MathUtils.random(-threshold / 2, threshold / 2);
			lastTime = System.currentTimeMillis();
			
			Vector2 vec = getRandomVec();
			
			if(task_face == null && task_move == null) {
				task_face = new TaskFaceTo(owner, EnumTaskPriority.LOW.get(), vec, true);
				owner.getAI().getTaskList().put(task_face, EnumTaskPriority.LOW.get());
				
				task_move = new TaskMoveTo(owner, EnumTaskPriority.LOW.get(), vec, true);
				owner.getAI().getTaskList().put(task_move, EnumTaskPriority.LOW.get());
			} else if(task_face != null && task_move != null && task_face.isFinish() && task_move.isFinish()) {
				task_face = new TaskFaceTo(owner, EnumTaskPriority.LOW.get(), vec, true);
				owner.getAI().getTaskList().put(task_face, EnumTaskPriority.LOW.get());
				
				task_move = new TaskMoveTo(owner, EnumTaskPriority.LOW.get(), vec, true);
				owner.getAI().getTaskList().put(task_move, EnumTaskPriority.LOW.get());
			}
		}*/
	}
	
	private Vector2 getRandomVec() {
		return new Vector2(MathUtils.random(-radius, radius), MathUtils.random(-radius, radius));
	}

	@Override
	public boolean isHangry() {
		return lastTime + timer + rand < System.currentTimeMillis();
	}
	
	public RRandomWalking setRadius(float r) {
		radius = r;
		return this;
	}

}
