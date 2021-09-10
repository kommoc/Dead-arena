package net.kommocgame.src.world;

public interface WorldObject {
	
	/** Delete this object from world. */
	void deleteObject();
	
	/** Don't call this method if you want to delete that object! Put in this method delete logic, such called in the end. */
	void del();
	
	/** Return the state of the current instance from world_delete list. */
	boolean isDeleted();
	
	/** Return the additional data. Need for json loading. */
	public AdditionalData getAdditionalData();
	
	/** Return the index of layer current instance in current priority. */
	public int getLayerIndex();
	
	/** Set the index of layer. */
	@Deprecated
	public void setLayerIndex(int par1);
	
}
