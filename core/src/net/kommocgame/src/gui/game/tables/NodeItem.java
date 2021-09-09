package net.kommocgame.src.gui.game.tables;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;
import com.badlogic.gdx.utils.Align;
import com.github.czyzby.kiwi.util.gdx.scene2d.Alignment;

import net.kommocgame.src.Game;
import net.kommocgame.src.profile.ItemStack;

public class NodeItem extends Table {
	
	Label label_name;
	Label label_count;
	Label label_bullet;
	Image icon;
	
	ItemStack itemStack;
	
	boolean isWrapping = false;
	
	public NodeItem(ItemStack itemStack) {
		this(Game.NEUTRALIZER_UI, itemStack);
	}

	public NodeItem(Skin skin, ItemStack itemStack) {
		super(skin);
		this.itemStack = itemStack;
		
		float scale_panel_1	= Gdx.graphics.getHeight() / 54f;
		float scale_panel_3 = Gdx.graphics.getHeight() / 46f;
		NinePatch patch9_canvas = Game.SCI_FI_ATLAS.createPatch("slot1");
		patch9_canvas.scale(scale_panel_1 / 13f, scale_panel_1 / 13f);
		patch9_canvas.setColor(Color.LIGHT_GRAY);
		NinePatchDrawable nine9_canvas = new NinePatchDrawable(patch9_canvas);
		this.setBackground(nine9_canvas);
		
		label_name = new Label("" + itemStack.getItem().getName(), Game.GENERATED_LABEL_STYLE_GOTTHARD_LITTLE_TEXT_WHITE);
		label_name.setWrap(true);
		
		label_count = new Label("" + itemStack.getStackSize(), Game.GENERATED_LABEL_STYLE_GOTTHARD_LITTLE_TEXT_WHITE);
		label_bullet = new Label("" + 1500, Game.GENERATED_LABEL_STYLE_GOTTHARD_LITTLE_TEXT_WHITE); // TODO
		icon = new Image(itemStack.getItem().getIcon());
		
		this.add(label_name).grow().center().colspan(2);//.prefWidth(Gdx.graphics.getWidth() / 16f).prefHeight(GuiBase.getRatio(1f/32f))
		this.row();
		this.add(icon).prefSize(getPrefWidth() / 1.5f).colspan(2).expand().center();
		this.row();
		this.add(label_count).expand().top().expand();//.prefHeight(getPrefHeight() / 8f)
		this.add(label_bullet).expand().bottom().expand();//.prefHeight(GuiBase.getRatio(1f/32f))
	}
	
	public ItemStack getItemStack() {
		return itemStack;
	}
	
	@Override
	public void act(float delta) {
		super.act(delta);
		
		if(this.getCell(icon).getActorWidth() != this.getCell(icon).getActorHeight()) {
			if(this.getCell(icon).getActorWidth() > this.getCell(icon).getActorHeight()) {
				this.getCell(icon).size(this.getCell(icon).getActorHeight());
			} else {
				this.getCell(icon).size(this.getCell(icon).getActorWidth());
			}
			
			if(label_name.getGlyphLayout().runs.size > 1) icon.setScale(1.6f);
				else icon.setScale(1.3f);
			
			icon.setOrigin(Alignment.CENTER.get());
			this.invalidate();
		}
	}

}
