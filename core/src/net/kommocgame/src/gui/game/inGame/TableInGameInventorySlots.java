package net.kommocgame.src.gui.game.inGame;

import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Array;

import net.kommocgame.src.Game;
import net.kommocgame.src.gui.game.tables.NodeItem;
import net.kommocgame.src.profile.InventoryPlayer;

public class TableInGameInventorySlots extends Table {
	
	InventoryPlayer inventory;
	Array<NodeItem> nodes = new Array<NodeItem>();
	
	Table table_nodes;
	ScrollPane scroll_table_nodes;
	
	public TableInGameInventorySlots(InventoryPlayer inventory) {
		this(inventory, Game.NEUTRALIZER_UI);
	}

	public TableInGameInventorySlots(InventoryPlayer inventory, Skin skin) {
		super(skin);
		this.inventory = inventory;
		table_nodes = new Table(skin);
		scroll_table_nodes = new ScrollPane(table_nodes);
		this.add(scroll_table_nodes).grow();
		
		this.setBackground("panel");
		this.setColor(Game.COLOR_PANEL_INFO);
		
		initInventory();
	}
	
	private void initInventory() {
		for(int i = 0, j = 0; i < inventory.getMaxSlots(); i++) {
			
			if(inventory.getItemStackInSlot(i) != null) {
				if(!(j < 3)) {
					j = 0;
					table_nodes.row();
				}
					
				NodeItem node = new NodeItem(inventory.getItemStackInSlot(i));
				table_nodes.add(node).uniformX().growY();
				
				j++;
			}
		}
	}
	
	@Override
	public void act(float delta) {
		super.act(delta);
	}

}
