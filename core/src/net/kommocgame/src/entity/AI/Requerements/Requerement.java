package net.kommocgame.src.entity.AI.Requerements;

import net.kommocgame.src.entity.EntityLiving;

public abstract class Requerement {
	
	protected EntityLiving owner;
	private boolean active = true;
	
	public Requerement(EntityLiving entity) {
		owner = entity;
	}
	
	public boolean isActive() {
		return active;
	}
	
	public abstract void update();
	
	/**  */
	public abstract boolean isHangry();
	
	public void setActive(boolean active) {
		this.active = active;
	}
	
}
