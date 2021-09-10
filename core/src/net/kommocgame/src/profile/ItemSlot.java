package net.kommocgame.src.profile;

import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonValue;
import com.badlogic.gdx.utils.Json.Serializable;

public class ItemSlot implements Serializable {
	
	/**  */
	public ItemSlot() {}
	
	int items_index;
	
	public boolean isEmpty() {
		return items_index == -1;
	}
	
	public void setClear() {
		items_index = -1;
	}
	
	public void setIndex(int index) {
		items_index = index;
	}
	
	public void setItemStack(ItemStack itemStack, Array<ItemStack> array) {
		items_index = array.indexOf(itemStack, false);
	}
	
	public int getIndex() {
		return items_index;
	}
	
	public ItemStack getItemStack(Array<ItemStack> array) {
		if(getIndex() == -1 || array.size <= getIndex()) return null;
		
		return array.get(items_index);
	}

	@Override
	public void write(Json json) {
		json.writeValue("items_index", items_index);
	}

	@Override
	public void read(Json json, JsonValue jsonData) {
		items_index = jsonData.getInt("items_index");
	}
}
