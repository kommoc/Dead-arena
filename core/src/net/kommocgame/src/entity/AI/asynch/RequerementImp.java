package net.kommocgame.src.entity.AI.asynch;

public abstract class RequerementImp {
	
	protected SteerableImp owner;
	private boolean active = true;
	
	public RequerementImp(SteerableImp entity) {
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

