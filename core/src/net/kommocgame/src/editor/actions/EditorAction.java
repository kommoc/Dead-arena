package net.kommocgame.src.editor.actions;

public class EditorAction {
	
	private boolean isDeactive = false;
	
	private boolean isCtrl	= false;
	private boolean isAlt	= false;
	private boolean isShift	= false;
	
	public void update() { }
	
	public void apply() {
		isDeactive = true;
	}
	
	public void cancel() {
		isDeactive = true;
	}
	
	public boolean isDeactive() {
		return isDeactive;
	}
	
	/** Return the event CTRL was press. */
	public boolean isCtrl() {
		return isCtrl;
	}
	
	/** Return the event ALT was press. */
	public boolean isAlt() {
		return isAlt;
	}
	
	/** Return the event SHIFT was press. */
	public boolean isShift() {
		return isShift;
	}
	
	public void setAlt(boolean par1) {
		isAlt = par1;
	}
	
	public void setShift(boolean par1) {
		isShift = par1;
	}
	
	public void setCtrl(boolean par1) {
		isCtrl = par1;
	}
	
	@Override
	public String toString() {
		return this.getClass().getSimpleName();
	}
}
