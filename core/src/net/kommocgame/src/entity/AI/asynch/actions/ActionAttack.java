package net.kommocgame.src.entity.AI.asynch.actions;

import net.kommocgame.src.entity.AI.asynch.SteerableImp;

public class ActionAttack extends Action {
	
	private final SteerableImp target;
	public int id_attack;
	
	public ActionAttack(SteerableImp target, int id_attack) {
		this.target = target;
		this.id_attack = id_attack;
	}
	
	public SteerableImp getTarget() {
		return target;
	}
}
