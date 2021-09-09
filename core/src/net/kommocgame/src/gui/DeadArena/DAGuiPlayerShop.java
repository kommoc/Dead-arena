package net.kommocgame.src.gui.DeadArena;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;

import net.kommocgame.src.Game;
import net.kommocgame.src.gui.GuiBase;
import net.kommocgame.src.gui.GuiManager;
import net.kommocgame.src.item.Item;
import net.kommocgame.src.item.ItemType;

@Deprecated
public class DAGuiPlayerShop extends GuiBase {
	
	Table canvas = new Table(Game.NEON_UI);
	ClickListener clickListener;
	
	boolean isGuiClose = false;
	
	Button but_back			= new Button(Game.NEON_UI);
	Button but_buy			= new Button(Game.NEON_UI);
	@Deprecated
	Button but_filter		= new Button(Game.NEON_UI);
	
	ItemNode slot_1	= new ItemNode(Item.getItemByID(Game.profile.get_slotItem(1)));
	ItemNode slot_2	= new ItemNode(Item.getItemByID(Game.profile.get_slotItem(2)));
	ItemNode slot_3	= new ItemNode(Item.getItemByID(Game.profile.get_slotItem(3)));
	
	Table 		scroll_tableNodes	= new Table(Game.NEON_UI);
	ScrollPane	scroll_paneNodes	= new ScrollPane(scroll_tableNodes);
	
	Table 		table_slots			= new Table(Game.NEON_UI);
	Table 		scroll_tableDescr	= new Table(Game.NEON_UI);
	ScrollPane	scroll_paneDescr	= new ScrollPane(scroll_tableDescr);
	
	Label label_money = new Label("$:", Game.GENERATED_LABEL_STYLE_GOTTHARD_MID_TEXT);
	Label label_buy = new Label("BUY", Game.GENERATED_LABEL_STYLE_GOTTHARD_MID_TEXT);
	
	ItemNode slot_itemsBar;
	ItemNode slot_hotBar;
	
	public DAGuiPlayerShop(Game game) {
		super(game);
		group_stage.addActor(canvas);
		canvas.setSize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		canvas.setPosition(0, 0);
		
		float scale_panel_1 = Gdx.graphics.getHeight() / 28f;
		NinePatch patch9 = Game.SCI_FI_ATLAS.createPatch("panel1");
		patch9.scale(scale_panel_1 / 15f, scale_panel_1 / 15f);
		NinePatchDrawable nine9 = new NinePatchDrawable(patch9);
		canvas.setBackground(nine9);
		
		Table table_upperPane = new Table(Game.NEON_UI);
		Table table_money = new Table(Game.NEON_UI);
		table_money.setBackground("button");
		table_money.add(label_money).growX();
		table_money.setColor(Color.CYAN);
		table_upperPane.add(table_money).growX();
		
		but_back.add(new Label("Back", Game.GENERATED_LABEL_STYLE_GOTTHARD_MID_TEXT));
		table_upperPane.add(but_back);
		
		canvas.add(table_upperPane).growX().left().colspan(2);
		canvas.row();
		
		canvas.add(scroll_paneNodes).grow().uniformX();
		scroll_tableNodes.setBackground("button");
		scroll_tableNodes.setColor(Color.CYAN);
		scroll_tableNodes.left();
		scroll_tableDescr.setBackground("button");
		scroll_tableDescr.setColor(Color.CYAN);
		canvas.add(scroll_paneDescr).grow().uniformX();
		scroll_paneNodes.setOverscroll(false, false);
		scroll_paneDescr.setOverscroll(false, false);
		scroll_paneNodes.setScrollingDisabled(true, false);
		scroll_paneDescr.setScrollingDisabled(true, false);
		
		canvas.row();
		
		int maxCount = (int) (Gdx.graphics.getWidth() / 2f / getRatio(1f/6f));
		
		for(int i = 0; i < ItemType.values().length; i++) {
			int count = 0;
			
			System.out.println("DAGuiPlayerShop(new instance) ### maxCount: " + maxCount);
			System.out.println("DAGuiPlayerShop(new instance) ### scroll_table_nodes sizeX: ");
			
			Table table_tab = new Table(Game.NEON_UI);
			table_tab.setBackground("button");
			table_tab.setColor(Color.SKY);
			table_tab.left();
			
			Table table_itemType = new Table(game.NEON_UI);
			table_itemType.setBackground("button");
			table_itemType.setColor(Color.LIGHT_GRAY);
			Label label_itemType = new Label(ItemType.values()[i].name(), Game.GENERATED_LABEL_STYLE_GOTTHARD_LITTLE_TEXT_WHITE);
			label_itemType.setColor(Color.CORAL);
			table_itemType.add(label_itemType).growX();
			
			scroll_tableNodes.add(table_tab).growX().left();
			scroll_tableNodes.row();
			
			Table table_items = new Table(Game.NEON_UI);
			table_items.left();
			table_tab.add(table_itemType).growX();
			table_tab.row();
			table_tab.add(table_items).expandX().growX().left();
			
			for(int id = 0; id < Item.itemList.length; id++) {
				if(count == maxCount - 1) {
					table_items.row();
					count = 0;
				}
				
				if(Item.itemList[id] != null && Item.itemList[id].getItemType() == ItemType.values()[i]) {
					count++;
					ItemNode node = new ItemNode(Item.getItemByID(id));
					if(Game.profile.get_itemPurchase(id))
						node.setColor(Color.GREEN);
					
					table_items.add(node).size(Gdx.graphics.getWidth() / 2f / maxCount).left();
				}
			}
		}
		
		Table table_but_buy = new Table(Game.NEON_UI);
		but_buy.add(label_buy).fillX();
		table_but_buy.add(but_buy).grow();
		//table_but_buy.add(but_filter);	DELETE
		
		canvas.add(table_but_buy).grow();
		
		table_slots.add(slot_1).size(Gdx.graphics.getWidth() / 2f / maxCount);
		table_slots.add(slot_2).size(Gdx.graphics.getWidth() / 2f / maxCount);;
		table_slots.add(slot_3).size(Gdx.graphics.getWidth() / 2f / maxCount);;
		
		slot_1.setItemByID(Game.profile.get_slotItem(1));
		slot_2.setItemByID(Game.profile.get_slotItem(2));
		slot_3.setItemByID(Game.profile.get_slotItem(3));
		
		canvas.add(table_slots);
		
		clickListener = new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				super.clicked(event, x, y);
				
				ItemNode node_itemsBar = GuiManager.getInstanceOf(scroll_tableNodes, ItemNode.class, event.getTarget());
				if(node_itemsBar != null) {
					System.out.println("	DAGuiPlayerShop.new ClickListener().clicked() ### event.getTarget() is descedant of ITEMS_BAR");
					
					if(slot_itemsBar == null) {
						slot_itemsBar = node_itemsBar;
					} else if(slot_itemsBar != null) {
						if(slot_itemsBar != node_itemsBar)
							slot_itemsBar.setChecked(false);
						
						if(node_itemsBar.isChecked())
							slot_itemsBar = node_itemsBar;
						else slot_itemsBar = null;
					}
				}
				
				ItemNode node_hotBar = GuiManager.getInstanceOf(table_slots, ItemNode.class, event.getTarget());
				if(node_hotBar != null) {
					System.out.println("	DAGuiPlayerShop.new ClickListener().clicked() ### event.getTarget() is descedant of HOT_BAR");
					
					if(slot_hotBar == null) {
						slot_hotBar = node_hotBar;
					} else if(slot_hotBar != null) {
						if(slot_hotBar != node_hotBar)
							slot_hotBar.setChecked(false);
						
						if(node_hotBar.isChecked())
							slot_hotBar = node_hotBar;
						else slot_hotBar = null;
					}
				}
			}
		};
		canvas.addListener(clickListener);
	}
	
	@Override
	public void update(SpriteBatch batch) {
		super.update(batch);
		
		label_money.setText("$ " + Game.profile.get_money());
		
		if(but_back.isChecked()) {
			but_back.setChecked(false);
			isGuiClose = true;
		}
		
		if(but_buy.isChecked()) {
			if(slot_itemsBar != null) {
				boolean isPurchased = Game.profile.get_itemPurchase(slot_itemsBar.getItem().ID);
				boolean canBuy = Game.profile.get_money() >= slot_itemsBar.getItem().getCost();
				
				if(!isPurchased && canBuy) {
					Game.profile.set_itemParchase(slot_itemsBar.getItem().ID, true);
					Game.profile.set_player_money(Game.profile.get_money() - slot_itemsBar.getItem().getCost());
					slot_itemsBar.setColor(Color.GREEN);
				}
			}
		}
		
		if(slot_itemsBar != null && Game.profile.get_itemPurchase(slot_itemsBar.getItem().ID)) {
			if(slot_itemsBar.isChecked() && slot_hotBar != null) {
				label_buy.setText("SET");
				
				if(but_buy.isChecked()) {
					setItemToHotBar();
				}
			}
		} else {
			label_buy.setText("BUY");
		}
		
		//System.out.println("\n	DAGuiPlayerShop.update() ### slot_items != null: " + (slot_itemsBar != null));
		//System.out.println("	DAGuiPlayerShop.update() ### slot_hotBar != null: " + (slot_hotBar != null));
		
		//canvas.debugAll();
		
		/** That move need to init 2 action. */
		but_buy.setChecked(false);
	}
	
	private void setItemToHotBar() {
		if(slot_hotBar == slot_1) {
			if(slot_1.getItem() != null && slot_itemsBar.getItem() != null && slot_itemsBar.getItem().ID == slot_1.getItem().ID) {
				Game.profile.set_slotItem(1, Item.NULL);
				slot_1.setItem(null);
			} else {
				Game.profile.set_slotItem(1, slot_itemsBar.getItem().ID);
				slot_1.setItem(slot_itemsBar.getItem());
			}
			
			if(slot_1.getItem() != null) {
				if(slot_2.getItem() != null && slot_2.getItem().ID == slot_1.getItem().ID) {
					Game.profile.set_slotItem(2, Item.NULL);
					slot_2.setItem(null);
				} else if(slot_3.getItem() != null && slot_3.getItem().ID == slot_1.getItem().ID) {
					Game.profile.set_slotItem(3, Item.NULL);
					slot_3.setItem(null);
				}
			}
		} else if(slot_hotBar == slot_2) {
			if(slot_2.getItem() != null && slot_itemsBar.getItem() != null && slot_itemsBar.getItem().ID == slot_2.getItem().ID) {
				Game.profile.set_slotItem(2, Item.NULL);
				slot_2.setItem(null);
			} else {
				Game.profile.set_slotItem(2, slot_itemsBar.getItem().ID);
				slot_2.setItem(slot_itemsBar.getItem());
			}
			
			if(slot_2.getItem() != null) {
				if(slot_1.getItem() != null && slot_1.getItem().ID == slot_2.getItem().ID) {
					Game.profile.set_slotItem(1, Item.NULL);
					slot_1.setItem(null);
				} else if(slot_3.getItem() != null && slot_3.getItem().ID == slot_2.getItem().ID) {
					Game.profile.set_slotItem(3, Item.NULL);
					slot_3.setItem(null);
				}
			}
		} else if(slot_hotBar == slot_3) {
			if(slot_3.getItem() != null && slot_itemsBar.getItem() != null && slot_itemsBar.getItem().ID == slot_3.getItem().ID) {
				Game.profile.set_slotItem(3, Item.NULL);
				slot_3.setItem(null);
			} else {
				Game.profile.set_slotItem(3, slot_itemsBar.getItem().ID);
				slot_3.setItem(slot_itemsBar.getItem());
			}
			
			if(slot_3.getItem() != null) {
				if(slot_2.getItem() != null && slot_2.getItem().ID == slot_3.getItem().ID) {
					Game.profile.set_slotItem(2, Item.NULL);
					slot_2.setItem(null);
				} else if(slot_1.getItem() != null && slot_1.getItem().ID == slot_3.getItem().ID) {
					Game.profile.set_slotItem(1, Item.NULL);
					slot_1.setItem(null);
				}
			}
		}
	}
	
	public static class ItemNode extends Button {
		
		Item item;
		Image item_image;
		
		public ItemNode() {
			this(null);
		}
		
		public ItemNode(Item item) {
			this(Game.KOMMOC_UI, item);
		}
		
		public ItemNode(Skin skin, Item item) {
			super(skin);
			this.item = item;
			
			setItem(item);
		}
		
		public Item getItem() {
			return item;
		}
		
		public void setItemByID(int itemID) {
			this.setItem(Item.getItemByID(itemID));
		}
		
		public void setItem(Item item) {
			if(item_image != null && this.getCell(item_image) != null) {
				this.reset();
			}
			
			this.item = null;
			this.item_image = null;
			
			if(item != null) {
				this.item = item;
				item_image = new Image(item.getIcon());
				this.add(item_image).grow();
			}
		}
	}
}
