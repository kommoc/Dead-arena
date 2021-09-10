package net.kommocgame.src;

import com.badlogic.gdx.math.Interpolation;

public class Function {
	
	private Interpolation func;
	
	private long lastTime = 0;
	private long deltaTime = 0;
	
	public long timer = 0;
	public float x = 0;
	
	private boolean state				= false;
	private boolean hasEnded			= false;
	private boolean ended				= false;
	private boolean hasStarted			= false;
	private boolean backward			= false;
	private boolean hasStopped			= false;
	private boolean changed_backward	= false;
	
	/** Reload function. */
	private boolean repeateble	= false;
	private boolean bouncing	= false;
	
	private float val_start;
	private float val_end;
	
	/** This class needs for calculate current func.
	 * @param time - in mills. */
	public Function(long time, Interpolation function) {
		this(time, 0, 1, function, false);
	}
	
	/** This class needs for calculate current func.
	 * @param time - in mills.
	 * @param values - range. */
	public Function(long time, float valueStart, float valueEnd, Interpolation function) {
		this(time, valueStart, valueEnd, function, false);
	}
	
	/** This class needs for calculate current func.
	 * @param time - in mills.
	 * @param values - range.
	 * @param repeat - can repeat. */
	public Function(long time, float valueStart, float valueEnd, Interpolation function, boolean repeat) {
		func = function;
		timer = time;
		repeateble = repeat;
		
		val_start = valueStart;
		val_end = valueEnd;
	}
	
	public void init() {
		hasEnded = false;
		
		if(state == true && !backward) {
			if(lastTime + timer > System.currentTimeMillis()) {
				x = (float)(System.currentTimeMillis() - lastTime) / 1000f;
			} else {
				x = (float)timer / 1000f;
				
				if(bouncing) {
					this._pause();
					this.switchBackward();
					this._continue();
					return;
				}
				
				if(repeateble == true) {
					lastTime = System.currentTimeMillis();
				} else {
					end();
				}
			}
		} else if(state == true && backward) {
			if(lastTime + timer > System.currentTimeMillis()) {
				x = (float)(lastTime + timer - System.currentTimeMillis()) / 1000f;
			} else {
				x = 0;
				
				if(bouncing) {
					this._pause();
					this.switchBackward();
					this._continue();
					return;
				}
				
				if(repeateble == true) {
					lastTime = System.currentTimeMillis();
				} else {
					end();
				}
			}
		}
	}
	
	public float getValue() {
		return func.apply(val_start, val_end, x / timer * 1000f);
	}
	
	/** Shows state current function: true - func working(calculating), false - func is at rest. */
	public boolean getState() {
		return state;
	}
	
	public boolean getPause() {
		return hasStopped;
	}
	
	public boolean getBouncing() {
		return bouncing;
	}
	
	/** Return true if function goes backward. */
	public boolean isBackward() {
		return backward;
	}
	
	/** End processing of function. Called once.*/
	public boolean hasEnded() {
		return hasEnded;
	}
	
	/** Return end processing. */
	public boolean ended() {
		return ended;
	}
	
	public Function setBouncing(boolean par1) {
		bouncing = par1;
		return this;
	}
	
	public void start() {
		if(!hasStarted) {
			hasStarted = true;
			state = true;
			lastTime = System.currentTimeMillis();
		}
	}
	
	/** Set pause. */
	public void _pause() {
		if(!hasStopped && hasStarted && !ended) {
			state = false;
			hasStopped = true;
			
			deltaTime = System.currentTimeMillis() - lastTime;
		}
	}
	
	/** Continue the stopped function. */
	public void _continue() {
		if(hasStopped) {
			state = true;
			hasStopped = false;
			
			if(!changed_backward)
				lastTime = System.currentTimeMillis() - deltaTime;
			else 
				lastTime = System.currentTimeMillis() - timer + deltaTime;
			
			changed_backward = false;
			deltaTime = 0;
		}
	}
	
	public void end() {
		state = false;
		hasEnded = true;
		ended = true;
	}
	
	public void setRepeat(boolean canRepeat) {
		repeateble = canRepeat;
	}
	
	/** Can use only at end of function or at pause. */
	public void setBackward(boolean bool) {
		lastTime = System.currentTimeMillis();
		backward = bool;
		
		if(hasStopped) {
			changed_backward = true;
		}
	}
	
	/** Can use only at end of function or at pause. */
	public void switchBackward() {
		lastTime = System.currentTimeMillis();
		backward = !backward;
		
		if(hasStopped) {
			changed_backward = true;
		}
	}
	
	/** Reload function with received values.*/
	public void reload() {
		hasStarted = false;
		state = false;
		ended = false;
	}
	
	/** Reset function. */
	public void reset() {
		hasStarted = false;
		state = false;
		ended = false;
		
		lastTime = 0;
		deltaTime = 0;
		x = 0;
	}
	
	@Override
	public String toString() {
		return getValue() + "\nFunction has ended: " + hasEnded;
	}
}
