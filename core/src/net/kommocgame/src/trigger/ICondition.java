package net.kommocgame.src.trigger;

public interface ICondition {
	
	/** Condition for trigger. */
	boolean condition(TriggerBase tr);
	
	/** Execute commands will be apply after checking condition trigger. Anyone method should have tr.setExec() for 
	 * finish trigger execute. */
	void execute(TriggerBase tr);
}