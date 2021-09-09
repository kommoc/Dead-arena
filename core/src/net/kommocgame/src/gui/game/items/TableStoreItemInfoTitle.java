package net.kommocgame.src.gui.game.items;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.ui.Cell;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Align;

import net.kommocgame.src.Game;
import net.kommocgame.src.item.Item;
import net.kommocgame.src.profile.ItemStack;

public class TableStoreItemInfoTitle extends Table {
	
	private Label label_price;
	private Label label_count;
	
	private Image icon;
	private ButtonStoreBuy but_buy;
	
	private	float ICON_SIZE	= Gdx.graphics.getHeight() / 10f * 4f;
	private int count	= 0;
	
	private Item item;
	
	public TableStoreItemInfoTitle(Item item) {
		this(Game.NEUTRALIZER_UI, item);
	}
	
	public TableStoreItemInfoTitle(Skin skin, Item item) {
		super(skin);
		this.item = item;
		
		for(ItemStack itemStack : Game.CORE.getInventory().items)
			if(itemStack != null && itemStack.itemID == item.ID)
				count += itemStack.getStackSize();
		
		System.out.println("TableStoreItemsInfoTitle (new instance) ### count: " + count);
		
		label_count = new Label("Count in warehouse: " + count, Game.GENERATED_LABEL_STYLE_GOTTHARD_LITTLE_TEXT_WHITE);
		label_price = new Label("Price: " + item.getCost(), Game.GENERATED_LABEL_STYLE_GOTTHARD_MID_TEXT);
		
		addItemIconTable(item).size(ICON_SIZE).align(Align.topLeft);
		addButBuyTable(item).grow().align(Align.topLeft);
		this.row();
		addLabelCount().colspan(2);
	}
	
	private Cell addLabelCount() {
		label_count.setText("Count in warehouse: []");
		Cell cell = this.add(label_count).growX();
		return cell;
	}
	
	private Cell addButBuyTable(Item item) {
		Table table_iteract = new Table(getSkin());
		
		but_buy = new ButtonStoreBuy(this.getSkin());
		table_iteract.add(but_buy).grow().pad(Game.GUI_NUMBER_PANEL_GAP);
		table_iteract.row();
		table_iteract.add(label_price).grow();
		
		Cell cell = this.add(table_iteract).pad(Game.GUI_NUMBER_PANEL_GAP);
		
		if(isItemPurchase() && item.isOncePurchase()) {
			but_buy.setButton_PURCHASED();
		}
		
		if(isPotentialBuy()) {
			//(item.isOncePurchase() && !isItemPurchase() || !item.isOncePurchase())
			if(item.isOncePurchase()) {
				if(!isItemPurchase()) {
					but_buy.setButton_BUY();
				} else but_buy.setButton_PURCHASED();
			} else {
				but_buy.setButton_BUY();
			}
			
		} else but_buy.setButton_ENOUGH_MONEY();
		
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
	
	/** Return true if current item has already in warehouse (purchased) */
	private boolean isItemPurchase() {
		for(ItemStack itemStack : Game.CORE.getInventory().items) {
			if(itemStack != null && itemStack.itemID == item.ID)
				return true;
		}
		
		return false;
	}
	
	/** Return true if current item has already in warehouse (purchased) */
	private boolean isPotentialBuy() {
		if(Game.profile.get_money() > item.getCost())
			return true;
		
		return false;
	}
	
	@Override
	public void act(float delta) {
		super.act(delta);
		count = 0;
		
		if(but_buy.isChecked()) {
			but_buy.setChecked(false);
			
			Game.CORE.purchaseItemStack(item);
			
			if(isItemPurchase() && item.isOncePurchase()) {
				but_buy.setButton_PURCHASED();
			}
			
			if(isPotentialBuy()) {
				//(item.isOncePurchase() && !isItemPurchase() || !item.isOncePurchase())
				if(item.isOncePurchase()) {
					if(!isItemPurchase()) {
						but_buy.setButton_BUY();
					} else but_buy.setButton_PURCHASED();
				} else {
					but_buy.setButton_BUY();
				}
				
			} else but_buy.setButton_ENOUGH_MONEY();
		}
		
		for(ItemStack itemStack : Game.CORE.getInventory().items) {
			if(itemStack != null && itemStack.itemID == item.ID)
				count += itemStack.getStackSize();
		}
		
		label_count.setText("Count in warehouse: " + count);
	}
	
	public void setPrice(int price) {
		label_price.setText("Price: \n" + price + " $");
	}
}
