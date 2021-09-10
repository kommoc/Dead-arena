package net.kommocgame.src.gui.game.tables;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;

import net.kommocgame.src.Game;
import net.kommocgame.src.gui.GuiManager;
import net.kommocgame.src.gui.game.items.ItemSlotNode;
import net.kommocgame.src.profile.ItemStack;

public class TableInventorySlots extends Table {
	
	Array<ItemSlotNode> items_node = new Array<ItemSlotNode>();
	
	/** Max node count on line. */
	int row_count = 2;
	
	Table		table_nodes;
	ScrollPane	scroll_table;
	
	ItemSlotNode current_node;
	
	ClickListener clickListener;
	
	public TableInventorySlots() {
		this(Game.NEUTRALIZER_UI);
	}

	public TableInventorySlots(Skin skin) {
		super(skin);
		clickListener = new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				if(GuiManager.getInstanceOf(TableInventorySlots.this, ItemSlotNode.class, event.getTarget()) != null) {
					
					TableInventorySlots.this.setCurrentNode(GuiManager.getInstanceOf(TableInventorySlots.this, ItemSlotNode.class, event.getTarget()));
				}
				
				super.clicked(event, x, y);
			}
		};
		this.addListener(clickListener);
		
		this.table_nodes = new Table(getSkin());
		this.scroll_table = new ScrollPane(table_nodes);
		
		this.add(scroll_table).grow();
		scroll_table.setOverscroll(false, false);
		setRowCount(2);
	}
	
	public void setRowCount(int count) {
		table_nodes.clear();
		row_count = count;
		initInventory();
	}
	
	public void setCurrentNode(ItemSlotNode node) {
		if(current_node != null)
			current_node.setColor(Game.COLOR_TEXT_SELECTED);
		
		current_node = node;
		
		if(current_node != null)
			current_node.setColor(Color.GREEN);
	}
	
	/** Return choosed node. */
	public ItemSlotNode getCurrentNode() {
		return current_node;
	}
	
	private void initInventory() { // TODO добавить проверку на количество доступных слотов. чтобы после экипирования разгруза и по возвращению в режим предметов обновился список слотов
		int current_count = 0;
		items_node.clear();
		table_nodes.clear();
		current_node = null;
		
		for(int i = 0; i < Game.CORE.getInventory().getMaxSlots(); i++) {
			ItemSlotNode node = new ItemSlotNode(getSkin(), i, Game.CORE.getInventory().getItemStackInSlot(i));
			items_node.add(node);
			node.setColor(Game.COLOR_TEXT_SELECTED);
			
			table_nodes.add(node).uniform().growX().fillY();
			
			if(current_count < row_count)
				current_count++;
			else {
				current_count = 0;
				table_nodes.row();
			}
		}
	}
	
	private void updateInventoryNodes() {
		if(items_node.size != Game.CORE.getInventory().getMaxSlots()) {
			initInventory();
		}
		
		for(int i = 0; i < items_node.size; i++) {
			ItemSlotNode node = items_node.get(i);
			
			table_nodes.getCell(node).maxWidth(scroll_table.getWidth() / row_count);
			if(node.getItemStack() != Game.CORE.getInventory().getItemStackInSlot(i))
				node.setItemStack(Game.CORE.getInventory().getItemStackInSlot(i));
			table_nodes.invalidate();
		}
	}
	
	@Override
	public void act(float delta) {
		super.act(delta);
		//debugAll();
		
		updateInventoryNodes();
	}
}
