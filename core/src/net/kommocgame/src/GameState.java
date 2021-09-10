package net.kommocgame.src;

public enum GameState {
	PAUSE(0), CONTINUE(1);
	
	private final int state;
	
	private GameState(int par1) {
		state = par1;
	}
	
	public int get() {
		return state;
	}
}
