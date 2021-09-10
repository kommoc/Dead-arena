package net.kommocgame.src.entity;

public class DamageThrowable {

	public DamageThrowable() {}
	
	/** Calls when need to damage any entity. Owner is entity such called this method. Target - entity to damage. */
	public static boolean throwDamage(EntityBase owner, EntityBase target, int damage) {
		if(target != null && !target.isDead()) {
			if(!target.getFlagImmunity()) {
				if(target.getHP() - damage <= 0)
					target.onDead();
				
				target.damage(damage);
				return true;
			}
		}
		
		return false;
	}
}
