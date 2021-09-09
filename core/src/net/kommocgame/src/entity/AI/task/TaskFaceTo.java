package net.kommocgame.src.entity.AI.task;

import com.badlogic.gdx.math.Vector2;

import net.kommocgame.src.entity.EntityLiving;
import net.kommocgame.src.entity.AI.LocImp;
import net.kommocgame.src.world.World;

public class TaskFaceTo extends Task {
	
	private Vector2 vec_target;
	private LocImp target = new LocImp(0, 0);
	private FaceTo face;
	
	private boolean setInitWhenExec = false;
	private boolean inited = false;
	
	public TaskFaceTo(EntityLiving entity, int priority, Vector2 target, boolean postInit) {
		super(entity, priority);
		vec_target = target;
		setInitWhenExec = postInit;
		
		if(!postInit) {
			this.target = new LocImp(vec_target);
			
			face = new FaceTo(entity, this.target, 15f);
			face.setEnabled(true);
			//System.out.println("	preExec");
			inited = true;
		}
	}

	@Override
	public void execute(World world, EntityLiving entity, float deltaTime) {
		if(setInitWhenExec && !inited) {
			this.target = new LocImp(vec_target.cpy().add(entity.getPosition()));
			
			face = new FaceTo(entity, this.target, 15f);
			face.setEnabled(true);
			//System.out.println("	postExec");
			inited = true;
		}
		
		if(entity.getBehavior() != face) {
			entity.setBehavior(face);
		}
		
		if(face.isExecuted()) {
			entity.setBehavior(null);
			this.setIdle(entity);
			this.setTaskDone();
		}
	}

}
