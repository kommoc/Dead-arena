package net.kommocgame.src.entity.AI.Requerements;

import net.kommocgame.src.entity.EntityLiving;
import net.kommocgame.src.entity.AI.EffectFactor;
import net.kommocgame.src.entity.component.EnumAIState;

public class RZMRoaming extends Requerement {
	
	/** Scope of hanger, if be under that value. */
	private int HANGER_YELLOW = 4500;
	private int HANGER_RED = 1500;
	
	private int max_hanger_scope = 1000;
	private int hanger_scope = 1000;
	private int step_reduction = 15;
	
	private float step_reduction_factor = 1f;
	
	private long decrease_timer = 1000l;
	private long lastTime = 0l;
	
	private float f_calm;
	private float f_alert;
	private float f_agressive;
	
	private EffectFactor buff;
	
	public RZMRoaming(EntityLiving entity) {
		super(entity);
		buff = new EffectFactor();
		buff.set(0f);
	}

	@Override
	public void update() {
		/*if(lastTime + decrease_timer < System.currentTimeMillis()) {
			if(owner.getAI().getAiState() == EnumAIState.CALM.get()) {
				step_reduction_factor = f_calm;
			} else if(owner.getAI().getAiState() == EnumAIState.ALERT.get()) {
				step_reduction_factor = f_alert;
			} else {
				step_reduction_factor = f_agressive;
			}
			
			hanger_scope -= step_reduction * step_reduction_factor;
		}
		
		if(hanger_scope < HANGER_YELLOW && hanger_scope >= HANGER_RED) {
			//TaskFindEat once at 10 timer
			if(owner.getAI().getAiState() == EnumAIState.CALM.get()) {
				owner.getAI().setEntityState(EnumAIState.ALERT.get());
			}
		} else if(hanger_scope < HANGER_RED) {
			//TaskFindEat once at 5 timer
			owner.getAI().setEntityState(EnumAIState.AGRESSIVE.get());
			
		}*/
	}

	@Override
	public boolean isHangry() {
		return hanger_scope < HANGER_YELLOW;
	}
	
	public void setStepCalm(float f) {
		f_calm = f;
	}
	
	public void setStepAlert(float f) {
		f_alert = f;
	}
	
	public void setStepAgressive(float f) {
		f_agressive = f;
	}
}
