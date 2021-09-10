package net.kommocgame.src.entity.AI.asynch;

import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ArrayMap;

import net.kommocgame.src.entity.AI.EffectFactor;
import net.kommocgame.src.entity.AI.Guild;
import net.kommocgame.src.entity.component.EnumAIState;

public class TaskManager {

	private SteerableImp steerable;
	private float Ai_chance_toHear;			// - веро€тность услышать звук.
	
	private Array<EffectFactor> Ai_factor_list = new Array<EffectFactor>();
	private EffectFactor Ai_factor; 				// - результирующий эффект.
	private EffectFactor Ai_FACTOR_CALM; 			// - модификаторы в спокойном режиме.
	private EffectFactor Ai_FACTOR_ALERT; 			// - модификаторы в тревожном режиме.
	private EffectFactor Ai_FACTOR_AGRESSIVE; 		// - модификаторы в агрессивном режиме.
	
	private Guild Ai_guild;							// - гильди€ entity.
	
	private Array<RequerementImp> Ai_requerements = new Array<RequerementImp>();	// - лист потребностей.
	
	private float Ai_deltaTime;				// Ц врем€ шанса (дл€ того, чтобы шанс был одинаковым на прот€жении всего времени)
	
	private float Ai_reactionSpeed;			// Ц скорость реакции Entity на происход€щие событи€.
	private float Ai_REACTION_CALM;			// ... в спокойном режиме.
	private float Ai_REACTION_ALERT;		// ... в тревожном режиме.
	private float Ai_REACTION_AGRESSIVE;	// ... в агрессивном режиме.
	
	private int Ai_argoscore;				// - очки беспокойства.
	
	private int Ai_entityState = EnumAIState.CALM.get();
	
	private ArrayMap<TaskAI, Integer> Ai_taskList = new ArrayMap<TaskAI, Integer>();
	private TaskAI Ai_currentTask;
	
	public TaskManager(SteerableImp steerable) {
		this.steerable = steerable;
		Ai_factor = new EffectFactor().set(1f);
		
		Ai_FACTOR_CALM = new EffectFactor().set(1f);
		Ai_FACTOR_ALERT = new EffectFactor().set(1.3f);
		Ai_FACTOR_AGRESSIVE = new EffectFactor().set(1.5f);
	}
	
	public void update(float deltaTime) {
		for(int i = 0; i < Ai_requerements.size; i ++) {
			Ai_requerements.get(i).update();
		}
		
		if(getAiState() == EnumAIState.CALM.get()) {
			Ai_factor = Ai_FACTOR_CALM;
		} else if(getAiState() == EnumAIState.ALERT.get()) {
			Ai_factor = Ai_FACTOR_ALERT;
		} else if(getAiState() == EnumAIState.AGRESSIVE.get()) {
			Ai_factor = Ai_FACTOR_AGRESSIVE;
		}
		
		if(Ai_currentTask != null) {
			//System.out.println("	Current_TASK: " + Ai_currentTask);
			//System.out.println("	list: " + getTaskList());
			
			if(Ai_currentTask.getState() == TaskAI.TASK_EXECUTING)
				Ai_currentTask.execute(steerable, deltaTime);
			
			if(Ai_currentTask.isCancel() || Ai_currentTask.isDone() || Ai_currentTask.isFail())
				Ai_currentTask = null;
		}
		
		if(!(getTaskList().size == 0)) {
			TaskAI pTask = getPriorityTask();
			
			if(Ai_currentTask != null && pTask != null & Ai_currentTask != pTask) {
				Ai_currentTask.setTaskCancel();
				
				Ai_currentTask = pTask;
				Ai_currentTask.exec();
			} else if(Ai_currentTask == null) {
				Ai_currentTask = pTask;
				
				Ai_currentTask.exec();
			}
		} else {
			steerable.setBehavior(null);
		}
	}
	
	/** Add effect to general sum. */
	public void addEffect(EffectFactor effect) {
		if(Ai_factor_list.size > 0) {
			if(Ai_factor_list.contains(effect, false)) {
				System.out.println("	AI_EFFECT: factor_list is having that effect yet.");
			} else Ai_factor_list.add(effect);
		} else Ai_factor_list.add(effect);
	}
	
	public void addRequerement(RequerementImp req) {
		if(Ai_requerements.contains(req, false)) {
			System.out.println("		AI_REQUEREMENTS: that requerement have yet.");
			return;
		}
		
		Ai_requerements.add(req);
	}
	
	/** Remove effect from general sum. */
	public void removeEffect(EffectFactor effect) {
		if(Ai_factor_list.size > 0) {
			if(Ai_factor_list.contains(effect, false))
				Ai_factor_list.removeValue(effect, false);
			else System.out.println("	AI_EFFECT: that effect don't store in factor_list.");
		} else System.out.println("	AI_EFFECT: factor_list is empty.");
	}
	
	public TaskAI getPriorityTask() {
		if(getTaskList().size > 0) {
			int priorityValue = 0;
			int indexTask = 0;
			
			for(int i = 0 ; i < getTaskList().values().toArray().size; i++) {
				if(priorityValue < getTaskList().values().toArray().get(i)) {
					priorityValue = getTaskList().values().toArray().get(i);
					indexTask = i;
				}
			}
			
			return getTaskList().getKeyAt(indexTask);
		}
		
		return null;
	}
	
	public TaskAI getCurrentTask() {
		return Ai_currentTask;
	}
	
	public Array<RequerementImp> getRequerements() {
		return Ai_requerements;
	}
	
	public ArrayMap<TaskAI, Integer> getTaskList() {
		return Ai_taskList;
	}
	
	public EffectFactor getEffectCalm() {
		return Ai_FACTOR_CALM;
	}
	
	public EffectFactor getEffectAlert() {
		return Ai_FACTOR_ALERT;
	}
	
	public EffectFactor getEffectAgressive() {
		return Ai_FACTOR_AGRESSIVE;
	}
	
	public int getAiState() {
		return Ai_entityState;
	}
	
	public Guild getGuild() {
		return Ai_guild;
	}
	
	/** Set the entity state (CALM, ALERT, AGRESSIVE) */
	public void setEntityState(int state) {
		this.Ai_entityState = state;
		
		if(state == EnumAIState.CALM.get())
			Ai_reactionSpeed = Ai_REACTION_CALM;
		else if(state == EnumAIState.ALERT.get())
			Ai_reactionSpeed = Ai_REACTION_ALERT;
		else Ai_reactionSpeed = Ai_REACTION_AGRESSIVE;
	}
	
	public void setGuild(Guild guild) {
		Ai_guild = guild;
	}
}
