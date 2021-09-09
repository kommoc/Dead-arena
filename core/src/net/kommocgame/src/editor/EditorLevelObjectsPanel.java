package net.kommocgame.src.editor;


import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

import net.kommocgame.src.Game;
import net.kommocgame.src.control.InputHandler;
import net.kommocgame.src.editor.nodes.ELevelObj;
import net.kommocgame.src.gui.GuiManager;

public class EditorLevelObjectsPanel extends Table {
	
	TextButton but_OBJECTS_Terrain = new TextButton("Terrain", Game.NEUTRALIZER_UI);
	TextButton but_OBJECTS_Entity = new TextButton("Entity", Game.NEUTRALIZER_UI);
	TextButton but_OBJECTS_Wall = new TextButton("Wall", Game.NEUTRALIZER_UI);
	TextButton but_OBJECTS_Props = new TextButton("Props", Game.NEUTRALIZER_UI);
	TextButton but_OBJECTS_Mobs = new TextButton("Mobs", Game.NEUTRALIZER_UI);
	TextButton but_OBJECTS_All = new TextButton("All", Game.NEUTRALIZER_UI);
	TextButton but_OBJECTS_Triggers = new TextButton("Triggers", Game.NEUTRALIZER_UI);
	TextButton but_OBJECTS_Misc = new TextButton("Misc", Game.NEUTRALIZER_UI);
	
	private static int 			tab			= 7;
	private static int 			prew_tab	= 7;
	
	Table table_tabs = new Table(Game.NEUTRALIZER_UI);
	
	Table scroll_table = new Table(Game.NEUTRALIZER_UI);
	ScrollPane scroll_pane = new ScrollPane(scroll_table, Game.NEUTRALIZER_UI);
	
	GuiEditor gui;
	private ClickListener clickListener;
	
	public EditorLevelObjectsPanel(GuiEditor gui) {
		this(gui, Game.NEUTRALIZER_UI);
	}

	public EditorLevelObjectsPanel(GuiEditor gui, Skin skin) {
		super(skin);
		this.setTouchable(Touchable.enabled);
		
		this.gui = gui;
		clickListener = new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				if(event.getTarget().isDescendantOf(table_tabs)) {
					EditorLevelObjectsPanel.this.setButton(GuiManager.getInstanceOf(table_tabs, TextButton.class, event.getTarget()));
				}
				
				ELevelObj target = GuiManager.getInstanceOf(getNodeTable(), ELevelObj.class, event.getTarget());
				if(target != null)
					EditorLevelObjectsPanel.this.gui.core.setNodeObject(target);
				
				super.clicked(event, x, y);
			}
		};
		
		this.addListener(clickListener);
		
		this.setClip(true);
		this.top().left();
		this.setBackground("panel");
		
		table_tabs.setBackground("panel");
		table_tabs.setColor(Color.CYAN);
		table_tabs.top().left();
		
		table_tabs.add(but_OBJECTS_Terrain);
		table_tabs.add(but_OBJECTS_Wall);
		table_tabs.add(but_OBJECTS_Props);
		table_tabs.add(but_OBJECTS_Mobs);
		table_tabs.add(but_OBJECTS_Entity);
		table_tabs.row();
		table_tabs.add(but_OBJECTS_Triggers);
		table_tabs.add(but_OBJECTS_Misc);
		table_tabs.add(but_OBJECTS_All);
		
		this.setButton(but_OBJECTS_All);
		
		this.add(table_tabs).top().left().height(but_OBJECTS_All.getHeight() * 2.1f).growX();
		this.row();
		
		this.add(scroll_pane).expand().top();
		scroll_pane.setScrollingDisabled(true, false);
	}
	
	@Override
	public void act(float delta) {
		super.act(delta);
		
		if(gui.core.getWorld() != null && gui.core.getWorld().getLevel() != null) {
			//int world_terrain = gui.core.getWorld().getLevel().spawn_list_terrainObj.size;
			//int world_entity = gui.core.getWorld().getEngine().getEntities().size();
			
			//int table_terrain = 0;
			//int table_entity = 0;
			//int table_trigger = 0;
			
			
			for(int i = 0; i < gui.panel_levelObjects.getNodeTable().getCells().size; i++) {
				ELevelObj level_node = (ELevelObj) gui.panel_levelObjects.getNodeTable().getCells().get(i).getActor();
				
				if(level_node != null) {
					if(level_node.getColor() != Color.WHITE && gui.core.getEAHandler().getChoosedObject() == null)
						level_node.setColor(Color.WHITE);
				}
			}
			
			/* Counter FIXME DELETE
			for(int i = 0; i < scroll_table.getCells().size; i++) {
				if(scroll_table.getCells().get(i).getActor() instanceof ELOTerrain)
					table_terrain++;
				if(scroll_table.getCells().get(i).getActor() instanceof ELOTrigger)
					table_trigger++;
				if(scroll_table.getCells().get(i).getActor() instanceof ELOEntity)
					table_entity++;
			}*/
			
			if(prew_tab != tab) {
				prew_tab = tab;
				scroll_table.clear();
			}
				
			
			switch(tab) {
				case 0: {	/* Terrain 	*/	
					
					
					break;
				}
				case 1: {	/* Wall 	*/	
					
					break;
				}
				case 2: {	/* Prop 	*/	
					
					break;
				}
				case 3: {	/* Mob 		*/	
					
					break;
				}
				case 4: {	/* Entity	*/	
					
					break;
				}
				case 5: {	/* Trigger	*/	
					
					break;
				}
				case 6: {	/* Misc		*/	
					
					break;
				}
				case 7: {	/* All		*/	
					
					break;
				}
			}
		}
		
		//13.06.19 - fix the unfocus scroll.
				if(getStage() != null && getStage().getRoot() != null &&
						this.getStage().hit(InputHandler.GUI_mouse_x, InputHandler.GUI_mouse_y, true) != null) {
					
					if(GuiManager.getInstanceOf(getStage().getRoot(), ScrollPane.class, 
								this.getStage().hit(InputHandler.GUI_mouse_x, InputHandler.GUI_mouse_y, true)) == scroll_pane) {
						getStage().setScrollFocus(scroll_pane);
					} else {
						getStage().unfocus(scroll_pane);
					}
				} else {
					getStage().unfocus(scroll_pane);
				}
	}
	
	
	
	public Table getNodeTable() {
		return scroll_table;
	}
	
	public void setButton(TextButton but) {
		for(int i = 0; i < table_tabs.getCells().size; i++) {
			if(table_tabs.getCells().get(i).getActor() == but) {
				table_tabs.getCells().get(i).getActor().setColor(Color.GREEN);
				tab = i;
			} else {
				table_tabs.getCells().get(i).getActor().setColor(Color.WHITE);
			}
		}
	}
}
