package net.kommocgame.src.editor;

public enum EditorMode {
	EDIT_MODE(1), VIEW_MODE(2), CHOOSE_MODE(3), PLAY_MODE(4);
	
	private final int type;
	
	EditorMode(int par1) {
		type = par1;
	}
	
	public int get() {
		return type;
	}
}
