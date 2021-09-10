package net.kommocgame.src.entity.AI.task;

public enum EnumTaskPriority {
	
	LOW(0), MEDIUM(1), HIGH(2), SCRIPTED(3), OTHER(4);
	
	int priority;
	
	EnumTaskPriority(int par1) {
		priority = par1;
	}
	
	public int get() {
		return priority;
	}
}
