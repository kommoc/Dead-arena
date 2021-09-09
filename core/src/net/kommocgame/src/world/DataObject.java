package net.kommocgame.src.world;

public abstract class DataObject<T> {
	
	String name;
	T parameter;
	
	/** For json. */
	public DataObject() { }
	
	public DataObject(String name, T parameter) {
		this.name = name;
		this.parameter = parameter;
	}
	
	/** Set this parameter to new value. */
	public abstract void setParameter(T par1);
	
	/** Return the method such this parameter was attached. */
	public abstract T getParameter();
	
	/** Return the name of this parameter. */
	public String getName() {
		return name;
	}
}
