package net.kommocgame.src.entity.component;

public enum EnumAIState {
	
	CALM(0), ALERT(1), AGRESSIVE(2);
	private int index;
	
	EnumAIState(int par1) {
		index = par1;
	}
	
	public int get() {
		return index;
	}
}
