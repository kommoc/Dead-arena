package net.kommocgame.src.item;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Table;

import net.kommocgame.src.Game;
import net.kommocgame.src.Loader;
import net.kommocgame.src.profile.ItemStack;
import net.kommocgame.src.render.GameManager;

public class Slot extends Table {
	
	private Image img_item;
	private int maxStackSize;
	
	/** ItemStack containing inside this slot. */
	private ItemStack itemStack = null;
	
	/** Time needed to get slot item. In mls. */
	private long time_catch = 1000;
	
	/** Id -1 is empty belongs. [0-41] is inventory slot. */
	private int ID = -1;
	
	public Slot() {
		this(Loader.guiMenuSlot("inventory_slot.png"), -1);
	}
	
	public Slot(int id) {
		this(Loader.guiMenuSlot("inventory_slot.png"), id);
	}
	
	public Slot(Texture background, int id) {
		this.setBackground(GameManager.instance.getDrawable(background));
		this.ID = id;
	}
	
	@Override
	public void act(float deltaTime) {
		if(itemStack == null && img_item != null) {
			img_item.remove();
			img_item = null;
		}
	}
	
	/** Return the item icon. */
	public Image getItemIcon() {
		return img_item;
	}
	
	/** Return current itemStack containing inside this slot. */
	public ItemStack getItemStack() {
		return itemStack;
	}
	
	/** Return time needed to get slot item. In mls. */
	public long getCatchTime() {
		return time_catch;
	}
	
	/** Return the slot ID. -1 is other inventory. */
	public int getSlotID() {
		return this.ID;
	}
	
	public Slot setItemTimeRelease(long time) {
		this.time_catch = time;
		return this;
	}
	
	private Slot setItemIcon(ItemStack par1ItemStack) {
		Image img;
		img = new Image(par1ItemStack.getItem().getIcon());
		img.setSize(this.getWidth(), this.getHeight());
		img.setPosition(this.getX(), this.getY());
		img.setTouchable(Touchable.disabled);
		
		if(this.getCell(img_item) != null) {
			this.getCell(img_item).setActor(img);
		} else {
			this.add(img);
		}
		
		img_item = img;
		
		return this;
	}
	
	public void moveItemStackToSlot(Slot otherSlot) {
		if(otherSlot.getItemStack() == null && otherSlot.isInventory()) {
			Game.CORE.getInventory().setItemSlot(itemStack, otherSlot.ID);
			Game.CORE.getInventory().setItemSlot(null, ID);
			
			System.out.println("ID_consume: " + ID);
			System.out.println("ID_enter: " + otherSlot.ID);
			
			otherSlot.setItemStackFIX(itemStack);
			this.itemStack = null;
		}
	}
	
	public void setItemStackFIX(ItemStack par1ItemStack) {
		if(par1ItemStack == null) {
			itemStack = null;
			return;
		}
		
		int size2 = par1ItemStack.getStackSize();
		
		if(itemStack == null) {
			if(par1ItemStack.getStackSize() > maxStackSize) {
				itemStack = par1ItemStack;
				//par1ItemStack.setCurrentStackSize(size2 - maxStackSize);
			} else {
				itemStack = par1ItemStack;
				//par1ItemStack.setCurrentStackSize(0);
				//par1ItemStack = null;
			}
			
			setItemIcon(itemStack);
		} else {
			if(itemStack.itemID == par1ItemStack.itemID && itemStack.getStackSize() < maxStackSize) {
				int size = itemStack.getStackSize();
				int diff = maxStackSize - size;
				/*
				if(diff >= size2) {
					itemStack.setCurrentStackSize(size + size2);
					par1ItemStack.setCurrentStackSize(0);
					par1ItemStack = null;
				}

				par1ItemStack.setCurrentStackSize(size2 - diff);
				*/
			}
		}
	}
	
	public void deleteItemStack() {
		this.setItemStackFIX(null);
	}
	
	public boolean isInventory() {
		return ID != -1;
	}
}
