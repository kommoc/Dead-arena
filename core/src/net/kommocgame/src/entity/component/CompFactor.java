package net.kommocgame.src.entity.component;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.utils.Array;

import net.kommocgame.src.entity.buffs.Buff;

public class CompFactor implements Component {
	
	private Buff result_buff;
	private Array<Buff> buffs = new Array<Buff>();
	
	public CompFactor() {
		result_buff = new Buff().setInfinite();
	}
	
	public void addBuff(Buff buff) {
		buffs.add(buff);
		buff.setLastTime(System.currentTimeMillis());
	}
	
	public void update() {
		result_buff.reset();
		
		for(Buff buff : buffs) {
			if(!buff.isInfinite() && buff.getLastTime() + buff.getTimer() < System.currentTimeMillis()) {
				buffs.removeValue(buff, false);
				continue;
			}
			
			result_buff.setAdditionalSlot(result_buff.getAdditionalSlot() + buff.getAdditionalSlot());
			result_buff.setAiAgroRadius(result_buff.getAiAgroRadius() + buff.getAiAgroRadius() - 1f);
			result_buff.setAiAttackSpeed(result_buff.getAiAttackSpeed() + buff.getAiAttackSpeed() - 1f);
			result_buff.setAiFOV(result_buff.getAiFov() + buff.getAiFov() - 1f);
			result_buff.setAiRadiusFOV(result_buff.getAiRadiusFOV() + buff.getAiRadiusFOV() - 1f);
			result_buff.setAiReactionSpeed(result_buff.getAiReactionSpeed() + buff.getAiReactionSpeed() - 1f);
			result_buff.setAiRotate(result_buff.getAiRotate() + buff.getAiRotate() - 1f);
			result_buff.setDamage(result_buff.getDamage() + buff.getDamage() - 1f);
			result_buff.setProtection(result_buff.getProtection() + buff.getProtection() - 1f);
			result_buff.setMove(result_buff.getMove() + buff.getMove() - 1f);
			result_buff.setReloadingSpeed(result_buff.getReloadingSpeed() + buff.getReloadingSpeed() - 1f);
			
		}
	}
	
	public Buff getResultBuff() {
		return result_buff;
	}
	
	public Array<Buff> getBuffs() {
		return buffs;
	}
}
