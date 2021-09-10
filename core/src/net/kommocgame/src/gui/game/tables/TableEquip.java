package net.kommocgame.src.gui.game.tables;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;
import com.badlogic.gdx.utils.Align;

import net.kommocgame.src.Game;
import net.kommocgame.src.gui.GuiBase;
import net.kommocgame.src.gui.GuiManager;
import net.kommocgame.src.gui.DeadArena.DAGuiPlayerShop.ItemNode;
import net.kommocgame.src.gui.game.items.TableEquipItemInfoTitle;
import net.kommocgame.src.item.Item;
import net.kommocgame.src.item.ItemType;
import net.kommocgame.src.profile.InventoryPlayer;
import net.kommocgame.src.profile.ItemStack;

/** Rename to TableItem. */
public class TableEquip extends Table {
	
	/** Nodes by item. */
	private float tables_ratio = 0.5f;
	
	private boolean isItems = false;
	
	public Button but_back	= new Button(Game.NEON_UI);
	
	Button but_equipment	= new Button(Game.NEUTRALIZER_UI);
	Button but_items		= new Button(Game.NEUTRALIZER_UI);
	Button but_up			= new Button(Game.NEON_UI);
	
	Button but_set			= new Button(Game.NEON_UI);
	Button but_cancel		= new Button(Game.NEON_UI);
	Button but_inventory	= new Button(Game.NEON_UI);
	
	private Table	table_nodes;
	private Table	table_nodes_scroll;
	private Table	table_item;
	
	private Table	table_item_top;
	private Table	table_item_buttons;
	
	private ScrollPane scroll_table_nodes;
	ScrollPane scroll_table_item;
	
	private TableInventorySlots	table_inventory_slots;
	
	private Button current_item;
	private ClickListener clickListener;
	
	public TableEquip(NavigationTree tree) {
		this(Game.NEUTRALIZER_UI);
	}
	
	public TableEquip(Skin skin) {
		super(skin);
		table_nodes	= new Table(skin);
		table_item	= new Table(Game.NEON_UI);
		table_nodes_scroll	= new Table(skin);
		table_inventory_slots = new TableInventorySlots(Game.NEUTRALIZER_UI);
		
		but_back.add(new Label("Back", Game.GENERATED_LABEL_STYLE_GOTTHARD_BUTTON));
		but_up.add(new Label("UP", Game.GENERATED_LABEL_STYLE_GOTTHARD_BUTTON));
		but_set.add(new Label("Set", Game.GENERATED_LABEL_STYLE_GOTTHARD_MID_TEXT));
		but_cancel.add(new Label("Cancel", Game.GENERATED_LABEL_STYLE_GOTTHARD_MID_TEXT));
		but_inventory.add(new Label("player inventory", Game.GENERATED_LABEL_STYLE_GOTTHARD_LITTLE_TEXT_WHITE));
		
		float scale_panel_1	= Gdx.graphics.getHeight() / 54f;
		float scale_panel_3 = Gdx.graphics.getHeight() / 46f;
		NinePatch patch9_canvas = Game.SCI_FI_ATLAS.createPatch("panel1");
		patch9_canvas.scale(scale_panel_1 / 13f, scale_panel_1 / 13f);
		patch9_canvas.setColor(Color.LIGHT_GRAY);
		NinePatchDrawable nine9_canvas = new NinePatchDrawable(patch9_canvas);
		this.setBackground(nine9_canvas);
		
		table_item_top = new Table(Game.NEUTRALIZER_UI);
		table_item_buttons = new Table(Game.NEUTRALIZER_UI);
		final Table table_nodes_top = new Table(Game.NEUTRALIZER_UI);
		table_nodes_top.setBackground("panel");
		table_item_top.setBackground("panel");
		
		table_nodes_top.setColor(Game.COLOR_BACKGROUND);
		table_item_top.setColor(Game.COLOR_BACKGROUND);
		
		table_item_top.top();
		
		this.top();
		
		scroll_table_nodes	= new ScrollPane(table_nodes_scroll);
		scroll_table_item	= new ScrollPane(table_item);
		scroll_table_nodes.setOverscroll(false, false);
		
		table_nodes_top.add(scroll_table_nodes).expand().growX().top();
		
		Label label_equip = new Label("Equipment", Game.GENERATED_LABEL_STYLE_GOTTHARD_LITTLE_TEXT);
		Label label_items = new Label("Items", Game.GENERATED_LABEL_STYLE_GOTTHARD_LITTLE_TEXT);
		but_equipment.setColor(Game.COLOR_PANEL_INFO);
		but_items.setColor(Game.COLOR_PANEL_INFO);
		but_equipment.add(label_equip).expand().center();
		but_items.add(label_items).expand().center();
		Table table_buttons = new Table(Game.NEUTRALIZER_UI);
		table_buttons.add(but_equipment).grow().uniformX();
		table_buttons.add(but_items).grow().uniformX();
		table_nodes.add(table_buttons).colspan(2).growX().height(GuiBase.getRatio(1f/10f)).top();
		table_nodes.row();
		
		table_nodes.add(table_nodes_top).colspan(2).grow().top();
		table_nodes.row();
		table_nodes.add(but_back).bottom();
		table_nodes.add(but_up).right();
		
		table_item_top.add(scroll_table_item).expand().top().growX();
		
		this.add(table_nodes).prefWidth(Gdx.graphics.getWidth() * tables_ratio).grow().top();
		this.add(table_item_top).prefWidth(Gdx.graphics.getWidth() - Gdx.graphics.getWidth() * tables_ratio).grow().top();
		
		table_item_top.row();
		table_item_top.add(table_item_buttons).growX().align(Align.bottom).height(GuiBase.getRatio(1f/9f));
		this.setDescriptionTable();
		
		this.setInventory(table_nodes_scroll, Game.CORE.getInventory(), true);
		
		but_items.setColor(Color.GREEN);
		but_equipment.setColor(Game.COLOR_PANEL_INFO);
		
		clickListener = new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				if(GuiManager.getInstanceOf(table_nodes_top, Button.class, event.getTarget()) != null) {
					
					TableEquip.this.setActiveItem(GuiManager.getInstanceOf(table_nodes_top, Button.class, event.getTarget()));
					TableEquip.this.setDescriptionTable();
				}
				
				super.clicked(event, x, y);
			}
		};
		
		table_nodes.addListener(clickListener);
	}
	
	protected void setActiveItem(Button but) {
		if(current_item != null)
			current_item.setColor(Game.COLOR_TEXT_SELECTED);
		
		current_item = but;
		current_item.setColor(Color.GREEN);
	}
	
	public void setDescriptionTable() {
		table_item_buttons.clearChildren();
		table_item_buttons.add(but_inventory).grow();
		
		table_item.clearChildren();
		table_inventory_slots.setCurrentNode(null);
		
		if(current_item != null) {
			ItemStack itemStack = ((NodeItem)current_item.getChild(0)).itemStack;
			table_item.add(new TableEquipItemInfoTitle(itemStack));
		}
		//add table_item description
	}
	
	public void setInventoryTable() {
		table_item_buttons.clearChildren();
		table_item_buttons.add(but_set).grow();
		table_item_buttons.add(but_cancel).grow();
		
		table_item.clearChildren();
		table_item.add(table_inventory_slots).growX();
	}
	
	public void setInventory(Table table_inventory, InventoryPlayer inventory, boolean isItems) {	//TODO вставка определённых типов итемов
		table_inventory.clearChildren();
		
		int maxCount = (int) (Gdx.graphics.getWidth() * tables_ratio / GuiBase.getRatio(1f/4f));
		this.isItems = isItems;
		
		for(ItemType item_type : ItemType.values()) {
			int count = 0;
			
			if(!isItems) {
				if(item_type != ItemType.BODYARMOR && item_type != ItemType.OUTFIT)
					continue;
			} else {
				if(item_type == ItemType.BODYARMOR || item_type == ItemType.OUTFIT)
					continue;
			}
			
			System.out.println("	DAGuiInventory(new instance) ### maxCount: " + maxCount);
			System.out.println("	DAGuiInventory(new instance) ### scroll_table_nodes sizeX: ");
			
			Table table_tab = new Table(Game.NEUTRALIZER_UI);
			table_tab.setBackground("panel");
			table_tab.setColor(Color.SKY);
			table_tab.left();
			
			Table table_itemType = new Table(Game.NEUTRALIZER_UI);
			table_itemType.setBackground("panel");
			table_itemType.setColor(Color.LIGHT_GRAY);
			Label label_itemType = new Label(item_type.name(), Game.GENERATED_LABEL_STYLE_GOTTHARD_LITTLE_TEXT_WHITE);
			label_itemType.setColor(Color.CORAL);
			table_itemType.add(label_itemType).growX().pad(Game.GUI_NUMBER_PANEL_GAP);
			
			Table table_items = new Table(Game.NEON_UI);
			table_items.left();
			table_tab.add(table_itemType).growX();
			table_tab.row();
			table_tab.add(table_items).expandX().growX().left();
			
			//add bodyarmor equipped by player
			if(Game.CORE.getInventory().getBodyarmor() != null && Game.CORE.getInventory().getBodyarmor().getItem().getItemType() == item_type) {
				count++;
				Button but_node = new Button(Game.NEUTRALIZER_UI);
				NodeItem node = new NodeItem(Game.CORE.getInventory().getBodyarmor());
				node.setColor(Color.CYAN);
				but_node.add(node);
				but_node.setColor(Game.COLOR_TEXT_SELECTED);
				
				table_items.add(but_node).size((Gdx.graphics.getWidth() * tables_ratio - Game.GUI_NUMBER_PANEL_GAP * maxCount) / maxCount).left();
			}
			
			for(ItemStack itemStack : inventory.items) {
				if(count == maxCount) {
					table_items.row();
					count = 0;
				}
				
				if(itemStack != null && itemStack.getItem().getItemType() == item_type) {
					count++;
					Button but_node = new Button(Game.NEUTRALIZER_UI);
					NodeItem node = new NodeItem(itemStack);
					node.setColor(Color.LIGHT_GRAY);
					but_node.add(node);
					but_node.setColor(Game.COLOR_TEXT_SELECTED);
					
					table_items.add(but_node).size((Gdx.graphics.getWidth() * tables_ratio - Game.GUI_NUMBER_PANEL_GAP * maxCount) / maxCount).left();
				}
			}
			
			if(table_items.getCells().size > 0) {
				table_inventory.add(table_tab).growX().left();
				table_inventory.row();
			}
		}
	}
	
	public Table getNodesTable() {
		return table_nodes_scroll;
	}
	
	public Table getItemTable() {
		return table_item;
	}
	
	@Override
	public void act(float delta) {
		super.act(delta);
		
		//table_item_top.debugAll();
		
		if(but_up.isChecked()) {
			but_up.setChecked(false);
			
			scroll_table_nodes.setScrollY(0);
			scroll_table_nodes.setVelocityY(0);
		}
		
		if(but_inventory.isChecked()) {
			but_inventory.setChecked(false);
			
			this.setInventoryTable();
		}
		
		if(but_cancel.isChecked()) {
			but_cancel.setChecked(false);
			
			this.setDescriptionTable();
		}
		
		if(but_inventory.isChecked()) {
			but_inventory.setChecked(false);
			
			
		}
		
		if(but_equipment.isChecked()) {
			but_equipment.setChecked(false);
			
			but_equipment.setColor(Color.GREEN);
			but_items.setColor(Game.COLOR_PANEL_INFO);
			setInventory(table_nodes_scroll, Game.CORE.getInventory(), false);
		}
		
		if(but_items.isChecked()) {
			but_items.setChecked(false);
			
			but_items.setColor(Color.GREEN);
			but_equipment.setColor(Game.COLOR_PANEL_INFO);
			setInventory(table_nodes_scroll, Game.CORE.getInventory(), true);
		}
		
		if(but_set.isChecked()) {
			but_set.setChecked(false);
			
			if(current_item != null && table_inventory_slots.getCurrentNode() != null) {
				Game.CORE.getInventory().setItemSlot(((NodeItem)current_item.getChildren().first()).getItemStack(),
						table_inventory_slots.getCurrentNode().getItemStackSlot());
				Game.profile.updatePlayerInventory();
			}
		}
	}
}
