package net.kommocgame.src.entity.props;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import net.kommocgame.src.profile.ItemStack;
import net.kommocgame.src.world.World;

public class EntityItem extends EntityProp {
	
	private ItemStack itemStack;
	
	public EntityItem(World world, SpriteBatch batch, ItemStack itemStack) {
		this(world, batch, 0, 0, null, 100, itemStack);
	}

	public EntityItem(World world, SpriteBatch batch, float x, float y, ItemStack itemStack) {
		this(world, batch, x, y, null, 100, itemStack);
	}

	public EntityItem(World world, SpriteBatch batch, float x, float y, Texture tex, int hp, ItemStack itemStack) {
		super(world, batch, x, y, tex, hp);
		this.setCollision(false);
		this.flag_ignoreDamage = true;
		
		System.out.println("INIT");
		compPhysics.body.setEntityLink(this);
		this.itemStack = itemStack;
		this.setTexture(itemStack.getItem().getIcon(), 3f);
		this.spriteToCenter();
	}
	
	public ItemStack getItemStack() {
		return itemStack;
	}

}
