package net.kommocgame.src.profile;

import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.Json.Serializable;
import com.badlogic.gdx.utils.JsonValue;

import net.kommocgame.src.item.Item;
import net.kommocgame.src.item.ItemArmor;

public class InventoryPlayer implements Serializable {
	
	public Array<ItemStack> items = new Array<ItemStack>();
	private Array<ItemSlot> slots = new Array<ItemSlot>();
	
	private ItemStack bodyarmor;
	private ItemStack outfit;
	
	/** Maximum ammo the player carry on. */
	private int MAX_PLAYER_AMMO_SHOTGUN		= 24;
	private int MAX_PLAYER_AMMO_PP			= 180;
	private int MAX_PLAYER_AMMO_RIFLE		= 120;
	private int MAX_PLAYER_AMMO_MACHINEGUN	= 0;
	private int MAX_PLAYER_AMMO_LAUNCHER	= 0;
	private int MAX_PLAYER_AMMO_GRENADE		= 4;
	
	private int MAX_PLAYER_SLOTS			= 2;
	
	private final int MAX_SLOTS				= 16;
	private int MAX_ITEMS					= 512;
	//private int CUR_AMMO_SHOTGUN;
	//private int CUR_AMMO_PP;
	//private int CUR_AMMO_RIFLE;
	//private int CUR_AMMO_MACHINEGUN;
	//private int CUR_AMMO_LAUNCHER;
	//private int CUR_AMMO_GRENADE;
	
	public InventoryPlayer() {
		bodyarmor = null;
		outfit = null;
		slots.setSize(MAX_SLOTS);
		for(int i = 0; i < MAX_SLOTS; i++) {
			slots.set(i, new ItemSlot());
		}
	}
	
	/** Create game session instance. */
	public InventoryPlayer(int count) {
		bodyarmor = null;
		outfit = null;
		slots.setSize(count);
		
		MAX_ITEMS = count;
		for(int i = 0; i < count; i++) {
			slots.set(i, new ItemSlot());
		}
	}
	
	/** Set itemStack to the slot. */
	public void setItemSlot(ItemStack itemStack, int slot) {
		// add condition for items list.
		if(itemStack == null) {
			slots.get(slot).setClear();
			return;
		}
		
		if(!items.contains(itemStack, false)) {
			System.out.println("InventoryPlayer.setItemSlot() ### This itemStack wasn't added to items! Added to items.");
			//System.out.println("	Added to items: " + addItemStack(itemStack));
		}
			
		for(int i = 0; i < slots.size; i++) {
			if(slots.get(i).getItemStack(items) == itemStack) {
				slots.get(i).setClear();
			}
		}
		
		slots.get(slot).setItemStack(itemStack, items);
	}
	
	/** Set the bodyarmor to player. ItemStack must be in items list. */
	public void setBodyarmor(ItemStack itemStack) {
		if(itemStack != null) {
			if(items.contains(itemStack, false)) {
				if(bodyarmor != null)
					addItemStack(bodyarmor);
				
				items.removeValue(itemStack, false);
				bodyarmor = itemStack;
			} else System.out.println("InventoryPlayer.setBodyarmor() ### itemStack is not placed in items list.");
		} else {
			if(bodyarmor != null) {
				addItemStack(bodyarmor);
				bodyarmor = null;
			}
		}
	}
	
	/** Set the outfit to player. ItemStack must be in items list. */
	public void setOutfit(ItemStack itemStack) {
		if(itemStack != null) {
			if(items.contains(itemStack, false)) {
				if(outfit != null)
					addItemStack(outfit);
					
				items.removeValue(itemStack, false);
				outfit = itemStack;
			} else System.out.println("InventoryPlayer.setBodyarmor() ### itemStack is not placed in items list.");
		} else {
			if(outfit != null) {
				addItemStack(outfit);
				outfit = null;
			}
		}
	}
	
	/** Return the item by slot. */
	public Item getItemInSlot(int slot) {
		return items.get(slots.get(slot).items_index).getItem();
	}
	
	/** Return the itemStack by slot. */
	public ItemStack getItemStackInSlot(int slot) {
		if(slot >= slots.size)
			return null;
		
		return slots.get(slot).getItemStack(items);
	}
	
	public int getMaxAmmoShotgun() {
		return MAX_PLAYER_AMMO_SHOTGUN + (bodyarmor != null ? ((ItemArmor) bodyarmor.getItem()).getMaxAmmoShotgun() : 0)
				+ (outfit != null ? ((ItemArmor) outfit.getItem()).getMaxAmmoShotgun() : 0);
	}
	
	public int getMaxAmmoRifle() {
		return MAX_PLAYER_AMMO_RIFLE + (bodyarmor != null ? ((ItemArmor) bodyarmor.getItem()).getMaxAmmoRifle() : 0)
				+ (outfit != null ? ((ItemArmor) outfit.getItem()).getMaxAmmoRifle() : 0);
	}
	
	public int getMaxAmmoMachineGun() {
		return MAX_PLAYER_AMMO_MACHINEGUN + (bodyarmor != null ? ((ItemArmor) bodyarmor.getItem()).getMaxAmmoMachineGun() : 0)
				+ (outfit != null ? ((ItemArmor) outfit.getItem()).getMaxAmmoMachineGun() : 0);
	}
	
	public int getMaxAmmoLauncher() {
		return MAX_PLAYER_AMMO_LAUNCHER + (bodyarmor != null ? ((ItemArmor) bodyarmor.getItem()).getMaxAmmoLauncher() : 0)
				+ (outfit != null ? ((ItemArmor) outfit.getItem()).getMaxAmmoLauncher() : 0);
	}
	
	public int getMaxAmmoGrenade() {
		return MAX_PLAYER_AMMO_GRENADE + (bodyarmor != null ? ((ItemArmor) bodyarmor.getItem()).getMaxAmmoGrenade() : 0)
				+ (outfit != null ? ((ItemArmor) outfit.getItem()).getMaxAmmoGrenade() : 0);
	}
	
	public int getMaxAmmoPp() {
		return MAX_PLAYER_AMMO_PP + (bodyarmor != null ? ((ItemArmor) bodyarmor.getItem()).getMaxAmmoPp() : 0)
				+ (outfit != null ? ((ItemArmor) outfit.getItem()).getMaxAmmoPp() : 0);
	}
	
	/** Return the max player slots have.*/
	public int getMaxSlots() {
		return MAX_PLAYER_SLOTS + (bodyarmor != null ? ((ItemArmor) bodyarmor.getItem()).getAdditionalSlot() : 0)
				+ (outfit != null ? ((ItemArmor) outfit.getItem()).getAdditionalSlot() : 0);
	}
	
	/** Return the equipped bodyarmor. */
	public ItemStack getBodyarmor() {
		return bodyarmor;
	}
	
	/** Return the equipped outfit.*/
	public ItemStack getOutfit() {
		return outfit;
	}
	
	/** Add new itemStack to the items. */
	public boolean addItemStack(ItemStack par1ItemStack) {
		if(items.contains(par1ItemStack, false) && items.size < MAX_ITEMS)
			return false;
		
		items.add(par1ItemStack);
		return true;
	}
	
	/** Add new itemStack to the items. */
	public boolean addItem(int id) {
		return this.addItem(id, 1);
	}
	
	/** Add new itemStack to the items. */
	public boolean addItem(Item item) {
		return this.addItem(item.ID, 1);
	}
	
	/** Add new itemStack to the items. */
	public boolean addItem(int id, int count) {
		Item item = Item.getItemByID(id);
		if(!checkInventoryItem(item) && items.size < MAX_ITEMS) {
			System.out.println("InventoryPlayer.addItem() ### new ItemStack added.");
			items.add(new ItemStack(item, count, 0));
			return true;
		}
		
		int toStack = 0;
		System.out.println("InventoryPlayer.addItem() ### count: " + count);
		for(ItemStack itemStack : items) {
			if(itemStack.itemID == id) {
				if(itemStack.getStackSize() + count < itemStack.getMaxStackSize()) {
					System.out.println("InventoryPlayer.addItem() ### itemStack set to stack size.		TO_STACK: " + toStack);
					itemStack.setCurrentStackSize(itemStack.getStackSize() + count);
					count = 0;
					return true;
				} else {
					System.out.println("InventoryPlayer.addItem() ### itemStack is maximalized.		TO_STACK: " + toStack);
					toStack = itemStack.getMaxStackSize() - itemStack.getStackSize();
					itemStack.setCurrentStackSize(itemStack.getStackSize() + toStack);
					count -= toStack;
				}
			}
		}
		System.out.println("InventoryPlayer.addItem() ### ravenstvo: " + ((float)count / (float)item.getMaxStackSize()));
		System.out.println("InventoryPlayer.addItem() ### ceil: " + (Math.ceil((float)count / (float)item.getMaxStackSize())));
		System.out.println("InventoryPlayer.addItem() ### after_count: " + count);
		
		for(int i = 0; i < Math.ceil((float) count / (float) item.getMaxStackSize()); i++) {
			if(count < item.getMaxStackSize()) {
				System.out.println("InventoryPlayer.addItem() ### new ItemStack with stackSize[" + count + "] added.");
				ItemStack itemStack = new ItemStack(item, count, 0);
				addItemStack(itemStack);
				return true;
			} else {
				System.out.println("InventoryPlayer.addItem() ### new ItemStack with max stack_size added.");
				ItemStack itemStack = new ItemStack(item, item.getMaxStackSize(), 0);
				addItemStack(itemStack);
				count -= item.getMaxStackSize();
			}
		}
		
		return true;
	}
	
	/** Remove itemStack instance from inventory. */
	public boolean removeItemStack(ItemStack par1ItemStack) {
		if(!items.contains(par1ItemStack, false))
			return false;
		
		items.removeValue(par1ItemStack, false);
		return true;
	}
	
	/** Return true if that item has already. */
	private boolean checkInventoryItem(Item item) {
		for(ItemStack itemStack : items) {
			if(itemStack != null && itemStack.itemID == item.ID) return true;
		}
			
		return false;
	}
	
	/** Session inventory update. */
	public void updateInventory() {
		for(ItemStack itemStack : items) {
			if(itemStack != null) {
				if(itemStack.getCurrentDamage() >= itemStack.getMaxUseSize() && !itemStack.isRestorable()) {
					if(itemStack.getStackSize() > 1)
						itemStack.setCurrentStackSize(itemStack.getStackSize() - 1);
					else items.removeValue(itemStack, false);
				}
			} else items.removeValue(itemStack, false);
		}
	}
	
	@Override
	public void write(Json json) {
		json.writeValue("items", items);
		json.writeValue("slots", slots);
		
		json.writeValue("bodyarmor", bodyarmor);
		json.writeValue("outfit", outfit);
	}

	@Override
	public void read(Json json, JsonValue jsonData) {
		items = json.readValue("items", Array.class, jsonData);
		slots = json.readValue("slots", Array.class, jsonData);
		slots.setSize(16);
		bodyarmor = json.readValue("bodyarmor", ItemStack.class, jsonData);
		outfit = json.readValue("outfit", ItemStack.class, jsonData);
		
		if(bodyarmor.itemID == Item.NULL) 
			bodyarmor = null;
		
		if(outfit.itemID == Item.NULL) 
			outfit = null;
	}
}
