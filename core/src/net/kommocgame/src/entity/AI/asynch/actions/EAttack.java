package net.kommocgame.src.entity.AI.asynch.actions;

public enum EAttack {
	ZM_RIGHT_HAND(0), ZM_LEFT_HAND(0);
	
	int priority;
	
	EAttack(int par1) {
		priority = par1;
	}
	
	public int get() {
		return priority;
	}
}
