package net.kommocgame.src.entity.AI.asynch.actions;

public class Action {
	
	private boolean isChecked = false;
	
	private boolean isDone = false;
	
	/** Return the state of this request. */
	public boolean isChecked() {
		return isChecked;
	}
	/** Return the result success execution this action. */
	public boolean isDone() {
		return isDone;
	}
	
	/** Set the state of success action. */
	public void actionDone(boolean isDone) {
		isChecked = true;
		
		isDone = true;
	}
}
