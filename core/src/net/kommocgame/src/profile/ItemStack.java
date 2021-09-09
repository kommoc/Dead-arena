package net.kommocgame.src.profile;

import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.Json.Serializable;

import net.kommocgame.src.item.Item;

import com.badlogic.gdx.utils.JsonValue;

public class ItemStack implements Serializable {
	
	public int itemID = Item.NULL;
	
	private boolean isRestorable;
	
	private int currentDamage;
	private int maxUseSize;
	
	private int maxStackSize;
	private int stackSize;
	
	/** JSON non-argument constructor. */
	public ItemStack() { }
	
	/** Copy of current ItemStack. */
	public ItemStack(ItemStack par1ItemStack) {
		this.itemID = par1ItemStack.itemID;
		this.maxStackSize = par1ItemStack.maxStackSize;
		this.maxUseSize = par1ItemStack.maxUseSize;
		this.currentDamage = par1ItemStack.currentDamage;
		this.stackSize = par1ItemStack.stackSize;
		this.isRestorable = par1ItemStack.isRestorable();
	}
	
	/** Once sample of current item. */
	public ItemStack(int id) {
		this(Item.getItemByID(id));
	}
	
	/** Once sample of current item. */
	public ItemStack(Item item) {
		this(item, 1, 0);
	}
	
	public ItemStack(Item item, int count, int damage) {
		itemID = item.ID;
		this.maxStackSize = item.getMaxStackSize();
		this.maxUseSize = item.getMaxUseSize();
		this.isRestorable = item.isRestorable();
		
		currentDamage = damage;
		stackSize = count;
	}
	
	public int getStackSize() {
		return stackSize;
	}
	
	public int getMaxStackSize() {
		return maxStackSize;
	}
	
	public int getUseSize() {
		return getMaxUseSize() - currentDamage;
	}
	
	public int getMaxUseSize() {
		return maxUseSize;
	}
	
	public Item getItem() {
		return Item.getItemByID(itemID);
	}
	
	public int getCurrentDamage() {
		return currentDamage;
	}
	
	public boolean isRestorable() {
		return isRestorable;
	}
	
	public boolean canDamage() {
		return currentDamage < maxUseSize;
	}
	
	public ItemStack damage(int dmg) {
		currentDamage += dmg;
		return this;
	}
	
	public void setDamage(int damage) {
		currentDamage = damage;
	}
	
	protected ItemStack setCurrentStackSize(int size) {
		if(size > maxStackSize)
			stackSize = maxStackSize;
		else
			stackSize = size;
		return this;
	}
	
	protected boolean addToItemStack(ItemStack par1ItemStack) {
		if(this.getItem() == par1ItemStack.getItem()) {
			this.stackSize += par1ItemStack.stackSize;
			
			return true;
		}
		
		return false;
	}

	@Override
	public void write(Json json) {
		json.writeValue("itemID", itemID);
		json.writeValue("stack_size", stackSize);
		json.writeValue("maxStack_size", maxStackSize);
		json.writeValue("damage", currentDamage);
		json.writeValue("maxUse_size", maxUseSize);
		json.writeValue("isRestorable", isRestorable);
	}

	@Override
	public void read(Json json, JsonValue jsonData) {
		if(!jsonData.has("itemID")) {
			itemID = Item.NULL;
			return;
		} else itemID = jsonData.getInt("itemID");
		
		stackSize = jsonData.getInt("stack_size");
		maxStackSize = jsonData.getInt("maxStack_size");
		currentDamage = jsonData.getInt("damage");
		maxUseSize = jsonData.getInt("maxUse_size");
		isRestorable = jsonData.getBoolean("isRestorable");
	}
}
