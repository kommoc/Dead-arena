package net.kommocgame.src.gui.game.items;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Align;

import net.kommocgame.src.Game;
import net.kommocgame.src.Loader;
import net.kommocgame.src.gui.GuiBase;
import net.kommocgame.src.profile.ItemStack;

public class ItemSlotNode extends Button {
	
	Label label_item_name;
	Image icon;
	
	ItemIconCharacter	param_item_state	= new ItemIconCharacter("item_state", Loader.guiIcon("params/tiny_icon_state.png"));
	ItemIconCharacter	param_item_ammo		= new ItemIconCharacter("item_ammo", Loader.guiIcon("params/tiny_icon_ammo.png"));
	ItemIconCharacter	param_item_count	= new ItemIconCharacter("item_count", Loader.guiIcon("params/tiny_icon_count.png"));
	
	ItemStack itemStack;
	int slot = -1;
	
	public ItemSlotNode(Skin skin, int slot, ItemStack itemStack) {
		super(skin);
		this.slot = slot;
		
		this.itemStack = itemStack;
		this.setItemStack(itemStack);
	}
	
	public void setItemStack(ItemStack itemStack) {
		this.clearChildren();
		if(itemStack != null) {
			float ICON_SIZE = GuiBase.getRatio(1f/7f);
			
			icon = new Image(itemStack.getItem().getIcon());
			label_item_name = new Label("" + itemStack.getItem().getName(), Game.GENERATED_LABEL_STYLE_GOTTHARD_TINY_TEXT);
			
			this.add(label_item_name).left().growX().colspan(2).pad(Game.GUI_NUMBER_PANEL_GAP);
			this.row();
			this.add(icon).size(ICON_SIZE).align(Align.center).pad(Game.GUI_NUMBER_PANEL_GAP / 2f);
			
			Table table_characters = new Table(Game.NEUTRALIZER_UI);
			table_characters.setBackground("panel");
			table_characters.setColor(Game.COLOR_NODE_TEXT);
			
			table_characters.add(param_item_state).growX().left();
			table_characters.row();
			param_item_state.setMaxValue(itemStack.getMaxUseSize());
			param_item_state.setValue(itemStack.getUseSize());
			table_characters.add(param_item_ammo).growX().left();
			table_characters.row();
			table_characters.add(param_item_count).growX().left();
			table_characters.row();
			param_item_count.setMaxValue(itemStack.getMaxStackSize());
			param_item_count.setValue(itemStack.getStackSize());
			
			this.add(table_characters).grow().left().pad(Game.GUI_NUMBER_PANEL_GAP);
		} else {
			itemStack = null;
			this.add(new Label("CLEAR", Game.GENERATED_LABEL_STYLE_GOTTHARD_TINY_TEXT)).minHeight(GuiBase.getRatio(1f/8.5f));
		}
	}
	
	public ItemStack getItemStack() {
		return itemStack;
	}
	
	public int getItemStackSlot() {
		return slot;
	}
	
	public class ItemIconCharacter extends Table {
		
		Image tiny_icon;
		Label label_character;
		int value, max_value;
		
		String name;
		
		public ItemIconCharacter(String name, Texture texture) {
			super();
			this.name = name;
			max_value = 0;
			value = 0;
			
			icon = new Image(texture);
			label_character = new Label("0/0", Game.GENERATED_LABEL_STYLE_GOTTHARD_TINY_TEXT);
			label_character.setColor(Game.COLOR_TEXT_SELECTED);
			label_character.setWrap(true);
			
			this.add(icon).size(GuiBase.getRatio(1f/24f)).left();
			this.add(label_character).left().growX();//.maxWidth(GuiBase.getRatio(4f/24f));
		}
		
		public ItemIconCharacter setMaxValue(int max) {
			max_value = max;
			label_character.setText("" + value +"/" + max_value);
			return this;
		}
		
		public ItemIconCharacter setValue(int value) {
			this.value = value;
			label_character.setText("" + value +"/" + max_value);
			return this;
		}
		
		public ItemIconCharacter setParamName(String name) {
			this.name = name;
			return this;
		}
		
		public int getValue() {
			return value;
		}
		
		public int getMaxValue() {
			return max_value;
		}
		
		public String getParamName() {
			return name;
		}
	}
}
