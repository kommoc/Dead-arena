package net.kommocgame.src.gui.game.items;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.ui.Cell;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Align;
import com.brashmonkey.spriter.Drawer;

import net.kommocgame.src.Game;
import net.kommocgame.src.item.Item;
import net.kommocgame.src.item.ItemType;
import net.kommocgame.src.profile.ItemStack;
import net.kommocgame.src.render.RenderEngine;

public class TableEquipItemInfoTitle extends Table {
	
	private Label label_price;
	private Label label_count;
	
	private Image icon;
	private ButtonEquip	but_equip;
	
	private TablePlayerSprite table_sprite;
	
	private	float ICON_SIZE	= Gdx.graphics.getHeight() / 10f * 3.5f;
	private int count	= 0;
	
	private ItemStack itemStack;
	
	public TableEquipItemInfoTitle(ItemStack itemStack) {
		this(Game.NEUTRALIZER_UI, itemStack);
	}
	
	public TableEquipItemInfoTitle(Skin skin, ItemStack itemStack) {
		super(skin);
		this.itemStack = itemStack;
		
		label_count = new Label("Count in warehouse: " + count, Game.GENERATED_LABEL_STYLE_GOTTHARD_LITTLE_TEXT_WHITE);
		label_price = new Label("Price: " + itemStack.getItem().getCost(), Game.GENERATED_LABEL_STYLE_GOTTHARD_MID_TEXT);
		
		addItemIconTable(itemStack.getItem()).size(ICON_SIZE).align(Align.topLeft);
		addPlayerSprite(RenderEngine.drawer_world).size(ICON_SIZE);
		this.row();
		addLabelCount().colspan(2).growX();
		this.row();
		
		if(itemStack.getItem().getItemType() == ItemType.BODYARMOR) {
			addButEquipTable(itemStack).grow().align(Align.topLeft).colspan(2);
		
			if(Game.CORE.getInventory().getBodyarmor() == itemStack) {
				but_equip.setButton_IS_EQUIPPED();
			} else {
				but_equip.setButton_EQUIP();
			}
		}
	}
	
	private Cell addLabelCount() {
		label_count.setText("Count in warehouse: []");
		Cell cell = this.add(label_count).growX();
		return cell;
	}
	
	private Cell addButEquipTable(ItemStack itemStack) {
		Table table_iteract = new Table(getSkin());
		
		but_equip = new ButtonEquip(this.getSkin());
		table_iteract.add(but_equip).grow().pad(Game.GUI_NUMBER_PANEL_GAP);
		table_iteract.row();
		table_iteract.add(label_price).grow();
		
		Cell cell = this.add(table_iteract).pad(Game.GUI_NUMBER_PANEL_GAP);
		
		return cell;
	}
	
	private Cell addPlayerSprite(Drawer drawer) {
		table_sprite = new TablePlayerSprite(RenderEngine.drawer_gui);
		
		Table table_icon = new Table(this.getSkin());
		table_icon.setBackground("panel");
		table_icon.add(this.table_sprite).prefSize(ICON_SIZE).center().pad(Game.GUI_NUMBER_PANEL_GAP);
		Cell  cell = this.add(table_icon).pad(Game.GUI_NUMBER_PANEL_GAP);
		table_icon.setColor(Game.COLOR_BACKGROUND);
		
		return cell;
	}
	
	private Cell addItemIconTable(Item item) {
		icon = new Image(item.getIcon());
		
		Table table_icon = new Table(this.getSkin());
		table_icon.setBackground("panel");
		table_icon.add(this.icon).prefSize(ICON_SIZE).center().pad(Game.GUI_NUMBER_PANEL_GAP);
		Cell  cell = this.add(table_icon).pad(Game.GUI_NUMBER_PANEL_GAP);
		table_icon.setColor(Game.COLOR_BACKGROUND);
		
		return cell;
	}
	
	private void buttonCheck() {
		if(Game.CORE.getInventory().getBodyarmor() == itemStack) {
			but_equip.setButton_IS_EQUIPPED();
		} else {
			but_equip.setButton_EQUIP();
		}
	}
	
	@Override
	public void act(float delta) {
		super.act(delta);
		
		if(itemStack != null)
			table_sprite.setItem(itemStack.getItem());
		
		if(but_equip != null && but_equip.isChecked()) {
			but_equip.setChecked(false);
			
			Game.CORE.getInventory().setBodyarmor(itemStack);
			Game.profile.updatePlayerInventory();
			buttonCheck();
		}
	}
	
	public void setPrice(int price) {
		label_price.setText("Price: \n" + price + " $");
	}
}
