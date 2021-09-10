package net.kommocgame.src.editor;

public enum OpenTab {
	
	TAP(1), HIT(2);
	
	private final int type;
	
	OpenTab(int par1) {
		type = par1;
	}
	
	public int get() {
		return type;
	}
}
