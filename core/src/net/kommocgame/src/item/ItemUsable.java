package net.kommocgame.src.item;

import com.badlogic.gdx.graphics.Texture;

import net.kommocgame.src.entity.EntityLiving;
import net.kommocgame.src.profile.ItemStack;

public class ItemUsable extends Item {
	
	public ItemUsable(int id, Texture texture) {
		super(id, texture);
	}
	
	@Override
	public void onItemUse(ItemStack itemStack, EntityLiving entityLiving) {
		super.onItemUse(itemStack, entityLiving);
		
		if(!isItemUsing() && itemStack.canDamage()) {
			last_time_usage = System.currentTimeMillis();
			setItemInUse(true);
		}
	}
	
	@Override
	public void update(ItemStack itemStack, EntityLiving entityLiving) {
		super.update(itemStack, entityLiving);
		
		if(isItemUsing() && last_time_usage + timer < System.currentTimeMillis()) {
			onUsing(itemStack, entityLiving);
			setItemInUse(false);
		}
	}
	
	public void onUsing(ItemStack itemStack, EntityLiving entityLiving) {}
}
