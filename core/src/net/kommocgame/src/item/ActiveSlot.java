package net.kommocgame.src.item;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;

import net.kommocgame.src.Game;

public class ActiveSlot extends Slot {
	
	private boolean active = false;
	private Texture tex_active_1 = Game.KOMMOC_UI.getRegion("slot1a").getTexture();
	private Texture tex_disabled_1 = Game.KOMMOC_UI.getRegion("slot1").getTexture();
	
	private Drawable tex_disabled = Game.KOMMOC_UI.getDrawable("slot1");
	private Drawable tex_active = Game.KOMMOC_UI.getDrawable("slot1a");
	
	public ActiveSlot(int id) {
		super(id);
		
		this.setBackground(tex_disabled);
		this.setColor(1, 1, 1, 0.2f);
	}
	
	public void setActive(boolean par1) {
		this.active = par1;
		
		if(active)
			this.setBackground(tex_active);
		else this.setBackground(tex_disabled);
	}
	
	@Override
	protected void drawChildren (Batch batch, float parentAlpha) {
		super.drawChildren(batch, 1);
	}
	
	@Override
	public void act(float deltaTime) {
		super.act(deltaTime);
		
		if(this.isInventory() && this.getItemStack() != Game.CORE.getInventory().getItemStackInSlot(this.getSlotID())) {
			this.setItemStackFIX(Game.CORE.getInventory().getItemStackInSlot(this.getSlotID()));
		}
		
		if(getItemIcon() != null) {
			this.getItemIcon().setColor(1, 1, 1, 1f);
		}
		this.setColor(1, 1, 1, 1);
	}
}
