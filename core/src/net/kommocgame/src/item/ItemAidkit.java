package net.kommocgame.src.item;

import com.badlogic.gdx.graphics.Texture;

import net.kommocgame.src.entity.EntityLiving;
import net.kommocgame.src.profile.ItemStack;

public class ItemAidkit extends ItemUsable {
	
	/** Value restore hp. */
	private int restore_hp = 0;
	
	public ItemAidkit(int id, Texture texture) {
		super(id, texture);
		
	}
	
	@Override
	public void onUsing(ItemStack itemStack, EntityLiving entityLiving) {
		super.onUsing(itemStack, entityLiving);
		
		itemStack.damage(1);
		entityLiving.heal(getRestoreHp());
	}
	
	public ItemAidkit setRestoreHp(int hp) {
		restore_hp = hp;
		return this;
	}
	
	public int getRestoreHp() {
		return restore_hp;
	}
}
