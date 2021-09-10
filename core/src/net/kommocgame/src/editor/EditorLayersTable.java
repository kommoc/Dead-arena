package net.kommocgame.src.editor;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Cell;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

import net.kommocgame.src.Game;
import net.kommocgame.src.control.InputHandler;
import net.kommocgame.src.editor.EditorLayersTable.LayersTable.NodeLayer;
import net.kommocgame.src.gui.GuiBase;
import net.kommocgame.src.gui.GuiManager;
import net.kommocgame.src.world.level.LevelBase;
import net.kommocgame.src.world.level.LevelLayer;
import net.kommocgame.src.world.level.TerrainObject;

public class EditorLayersTable extends Table {
	
	GuiEditor gui;
	
	LayersTable layers = new LayersTable(Game.NEUTRALIZER_UI);
	ObjectsTable objects = new ObjectsTable(Game.NEUTRALIZER_UI);
	
	public EditorLayersTable(GuiEditor gui) {
		this(gui, Game.NEUTRALIZER_UI);
	}
	
	public EditorLayersTable(GuiEditor gui, Skin skin) {
		super(skin);
		this.setTouchable(Touchable.enabled);
		
		this.gui = gui;
		
		this.setClip(true);
		this.top().left();
		this.setBackground("panel");
		
		this.add(new Label("Layers", Game.NEUTRALIZER_UI)).expandX().left();
		this.row();
		this.add(layers).grow().height(GuiBase.getRatio(1f/2f)).left().top();
		this.row();
		this.add(objects).grow().left().top();
	}
	
	@Override
	public void act(float delta) {
		super.act(delta);
		
	}
	
	public void setLevel(LevelBase level) {
		layers.table_layers.reset();
		layers.id_node = -1;
		layers.current_node = null;
		
		objects.table_objects.reset();
		objects.id_node = -1;
		objects.current_node = null;
		
		for(int i = 0; i < level.getLayersList().size; i++) {
			LevelLayer layer = level.getLayersList().get(i);
			NodeLayer node = layers.addNewLayer(layer.getName(), layer.getEditorColor());
			
			if(layer.isCurrent())
				layers.setCurrentLayer(node);
		}
	}
	
	class LayersTable extends Table {
		
		Table table_title = new Table(Game.NEUTRALIZER_UI);
		Table table_layers = new Table(Game.NEUTRALIZER_UI);
		TextButton but_setLayer = new TextButton("set layer as CURRENT", Game.NEUTRALIZER_UI);
		ScrollPane scroll_pane_layers = new ScrollPane(table_layers, Game.NEUTRALIZER_UI);
		
		NodeLayer current_node;
		
		TextButton but_add = new TextButton(" + ", Game.NEUTRALIZER_UI);
		TextButton but_remove = new TextButton(" - ", Game.NEUTRALIZER_UI);
		TextButton but_up = new TextButton("up", Game.NEUTRALIZER_UI);
		TextButton but_down = new TextButton("down", Game.NEUTRALIZER_UI);
		ClickListener clickListener;
		
		/** Index of pressed layer. */
		int id_node = -1;
		//int id_current_node = -1;
		
		public LayersTable(Skin skin) {
			super(skin);
			this.setTouchable(Touchable.enabled);
			this.top().left();
			
			clickListener = new ClickListener() {
				@Override
				public void clicked(InputEvent event, float x, float y) {
					if(event.getTarget().isDescendantOf(table_layers)) {
						LayersTable.this.setButton(GuiManager.getInstanceOf(table_layers, NodeLayer.class, event.getTarget()));
					}
					
					super.clicked(event, x, y);
				}
			};
			
			this.addListener(clickListener);
			this.add(table_title).growX().top().left();
			this.row();
			table_title.add(new Label("Actions", skin)).left().growX();
			table_title.add(but_add).right();
			table_title.add(but_remove).right();
			table_title.add(but_up).right();
			table_title.add(but_down).right();
			table_title.setBackground("button");
			
			this.add(scroll_pane_layers).grow().left().top().expand();
			this.row();
			scroll_pane_layers.setScrollingDisabled(true, false);
			
			this.add(but_setLayer).growX().padTop(8f);
		}
		
		@Override
		public void act(float delta) {
			super.act(delta);
			
			if(gui.core.getWorld() == null)
				return;
			
			if(but_up.isChecked()) {
				but_up.setChecked(false);
				
				if(id_node != 0) {
					gui.core.getLevel().setLayerUp(gui.core.getLevel().getLayersList().get(id_node));
					
					Actor node = table_layers.getCells().get(id_node).getActor();
					Actor swap_node = table_layers.getCells().get(id_node - 1).getActor();
					
					table_layers.getCells().get(id_node).setActor(swap_node);
					table_layers.addActor(node);
					table_layers.getCells().get(id_node - 1).setActor(node);
					table_layers.addActor(swap_node);
					
					id_node--;
				}
			}
			
			if(but_setLayer.isChecked()) {
				but_setLayer.setChecked(false);
				
				this.setCurrentLayer();
			}
			
			if(but_down.isChecked()) {
				but_down.setChecked(false);
				
				if(id_node != table_layers.getCells().size - 1) {
					gui.core.getLevel().setLayerDown(gui.core.getLevel().getLayersList().get(id_node));
					Actor node = table_layers.getCells().get(id_node).getActor();
					Actor swap_node = table_layers.getCells().get(id_node + 1).getActor();
					
					table_layers.getCells().get(id_node + 1).setActor(node);
					table_layers.addActor(swap_node);
					table_layers.getCells().get(id_node).setActor(swap_node);
					table_layers.addActor(node);
					
					id_node++;
				}
				
				System.out.println("id_node: " + id_node);
			}
			
			if(but_add.isChecked()) {
				but_add.setChecked(false);
				
				Table table_toSwap = new Table(getSkin());
				scroll_pane_layers.setWidget(table_toSwap);
				
				if(table_layers.getCells().size != 0)
					for(int i = 0; i < table_layers.getCells().size; i++) {
						table_toSwap.add(table_layers.getCells().get(i).getActor()).growX().left();
						table_toSwap.row();
						
						if(id_node == i) {
							NodeLayer node = addNewLayer(table_toSwap, "new_layer", Color.WHITE);
							gui.core.getLevel().addLevelLayer(node.name, node.color, i + 1);
						}
					}
				else {
					NodeLayer node = addNewLayer(table_toSwap, "new_layer", Color.WHITE);
					gui.core.getLevel().addLevelLayer(node.name, node.color);
				}
				
				table_layers.reset();
				table_layers.remove();
				table_layers = table_toSwap;
			}
			
			if(but_remove.isChecked()) {
				but_remove.setChecked(false);
				
				Table table_toSwap = new Table(getSkin());
				scroll_pane_layers.setWidget(table_toSwap);
				
				for(int i = 0; i < gui.core.getLevel().getLayersList().size; i++) {
					if(id_node == i) {
						if(table_layers.getCells().get(i).getActor() == current_node)
							current_node = null;
						
						continue;
					}
					
					LevelLayer layer = gui.core.getLevel().getLayersList().get(i);
					
					addNewLayer(table_toSwap, layer.getName(), layer.getEditorColor());
					//table_toSwap.add(table_layers.getCells().get(i).getActor()).growX().left();
					//table_toSwap.row();
				}
				
				table_layers.reset();
				table_layers.remove();
				table_layers = table_toSwap;
				
				gui.core.getLevel().removeLevelLayer(id_node, gui.core);
				
				if(id_node < 0)
					id_node = 0;
				
				if(gui.core.getLevel().getMainLayer().getID() != -1) {
					System.out.println("table_layers.getChildren().size: " + table_layers.getChildren().size);
					if(gui.core.getLevel().getMainLayer().getID() < table_layers.getChildren().size)
						setCurrentLayer((NodeLayer) table_layers.getChildren().get(gui.core.getLevel().getMainLayer().getID()));
						objects.setLayerObjects(gui.core.getLevel().getMainLayer());
						objects.current_layer = gui.core.getLevel().getMainLayer();
				}
			}
			
			if(table_layers.getChildren().size != gui.core.getLevel().getLayersList().size) {
				updateLayers();
			}
			
			if(getStage() != null && getStage().getRoot() != null &&
					this.getStage().hit(InputHandler.GUI_mouse_x, InputHandler.GUI_mouse_y, true) != null) {
				
				if(GuiManager.getInstanceOf(getStage().getRoot(), ScrollPane.class, 
							this.getStage().hit(InputHandler.GUI_mouse_x, InputHandler.GUI_mouse_y, true)) == scroll_pane_layers) {
					getStage().setScrollFocus(scroll_pane_layers);
				} else {
					getStage().unfocus(scroll_pane_layers);
				}
			} else {
				getStage().unfocus(scroll_pane_layers);
			}
		}
		
		private NodeLayer addNewLayer(Table table, String name, Color color) {
			NodeLayer node = new NodeLayer(getSkin(), name, color);
			table.add(node).growX().left();
			table.row();
			return node;
		}
		
		private NodeLayer addNewLayer(String name, Color color) {
			NodeLayer node = new NodeLayer(getSkin(), name, color);
			table_layers.add(node).growX().left();
			table_layers.row();
			return node;
		}
		
		private void updateLayers() {
			table_layers.reset();
			id_node = -1;
			current_node = null;
			
			for(LevelLayer layer : gui.core.getLevel().getLayersList()) {
				NodeLayer node = addNewLayer(layer.getName(), layer.getEditorColor());
				
				if(layer.isCurrent())
					setCurrentLayer(node);
			}
		}
		
		private void editLayer(NodeLayer node) {
			node.row();
			
			EditTable edit_window = new EditTable(Game.NEUTRALIZER_UI, node);
			node.add(edit_window).colspan(3).growX();
			node.isEditing = true;
		}
		
		public void setButton(NodeLayer node) {
			for(int i = 0; i < table_layers.getCells().size; i++) {
				if(table_layers.getCells().get(i).getActor() == node) {
					if(id_node != i) {
						LevelLayer layer = gui.core.getLevel().getLayersList().get(i);
						
						objects.setLayerObjects(layer);
						objects.current_layer = layer;
					}
					
					table_layers.getCells().get(i).getActor().setColor(Color.GREEN);
					id_node = i;
					
				} else if(table_layers.getCells().get(i) != null && table_layers.getCells().get(i).getActor() != null) {
					if(current_node != table_layers.getCells().get(i).getActor())
						table_layers.getCells().get(i).getActor().setColor(Color.WHITE);
					else table_layers.getCells().get(i).getActor().setColor(Color.CYAN);
				}
			}
		}
		
		/** For select. */
		private void setCurrentLayer() {
			if(current_node != null) {
				NodeLayer node = (NodeLayer) table_layers.getCells().get(id_node).getActor();
				
				current_node.setCurrent(false);
				node.setCurrent(true);
				gui.core.getLevel().setCurrentLayer(id_node);
				
				current_node = node;
			} else {
				NodeLayer node = (NodeLayer) table_layers.getCells().get(id_node).getActor();
				
				node.setCurrent(true);
				gui.core.getLevel().setCurrentLayer(id_node);
				
				current_node = node;
			}
			
			setButton(current_node);
		}
		
		/** For loading layers. */
		private void setCurrentLayer(NodeLayer node) {
			node.setCurrent(true);
			current_node = node;
			setButton(current_node);
		}
		
		class EditTable extends Table {
			TextField text_name = new TextField("", Game.NEUTRALIZER_UI);
			ElementsGui.SelectorColor selector_color = new ElementsGui.SelectorColor();
			
			ElementsGui.ButApply but_apply = new ElementsGui.ButApply();
			ElementsGui.ButCancel but_cancel = new ElementsGui.ButCancel();
			
			NodeLayer node;
			
			public EditTable(Skin skin, NodeLayer node) {
				super(skin);
				this.setBackground("button");
				this.setTouchable(Touchable.enabled);
				this.top().left();
				
				this.node = node;
				
				text_name.setText(node.name);
				this.add(new Label("Name:", Game.NEUTRALIZER_UI));
				this.add(text_name);
				this.row();
				
				selector_color.setSelectorColor(node.color);
				this.add(new Label("Color:", Game.NEUTRALIZER_UI));
				this.add(selector_color);
				this.row();
				
				this.add(but_apply);
				this.add(but_cancel);
			}
			
			public Color getLayerColor() {
				return selector_color.getSelectorColor();
			}
			
			public String getLayerName() {
				return text_name.getText();
			}
			
			@Override
			public void act(float delta) {
				super.act(delta);
				if(but_apply.isChecked()) {
					node.isEditing = false;
					node.setLayerColor(selector_color.getSelectorColor());
					node.setLayerName(text_name.getText());
					
					gui.core.getLevel().getLayersList().get(node.getLayerID()).setColor(getLayerColor());
					gui.core.getLevel().getLayersList().get(node.getLayerID()).setName(getLayerName());
					
					this.remove();
				} else if(but_cancel.isChecked()) {
					node.isEditing = false;
					this.remove();
				}
			}
		}
		
		class NodeLayer extends Table {
			String name;
			Color color;
			
			boolean current_layer = false;
			boolean isEditing = false;
			
			Label label_name = new Label("Layer_", Game.NEUTRALIZER_UI);
			TextButton but_edit = new TextButton("Edit", Game.NEUTRALIZER_UI);
			Table table_color = new Table(Game.NEUTRALIZER_UI);
			
			public NodeLayer(Skin skin, String name, Color color) {
				super(skin);
				this.setBackground("button");
				this.name = name;
				this.color = color;
				this.setSize(192f, 48f);
				
				this.setTouchable(Touchable.enabled);
				this.top().left();
				
				label_name.setText(name);
				this.add(label_name).expand().left().padLeft(8f);;
				
				table_color.setBackground("panel");
				table_color.setColor(color);
				this.add(table_color).width(48f).height(48f);
				
				this.add(but_edit).padRight(16f).padLeft(8f);
			}
			
			public void setLayerColor(Color color) {
				this.color = color;
				table_color.setColor(color);
			}
			
			public void setLayerName(String name) {
				label_name.setText(name);
				this.name = name;
			}
			
			public void setCurrent(boolean isCurrent) {
				if(isCurrent) this.label_name.setColor(Color.CYAN);
				else this.label_name.setColor(Color.WHITE);
			}
			
			public int getLayerID() {
				for(Cell cell : table_layers.getCells()) {
					if(cell.getActor() == this) {
						return table_layers.getCells().indexOf(cell, false);
					}
				}
				
				return -1;
			}
			
			@Override
			public void act(float delta) {
				super.act(delta);
				
				if(but_edit.isChecked()) {
					but_edit.setChecked(false);
					
					if(!isEditing)
						editLayer(this);
				}
			}
		}
	}
	
	class ObjectsTable extends Table {
		
		Table table_title = new Table(Game.NEUTRALIZER_UI);
		TextButton but_up = new TextButton("up", Game.NEUTRALIZER_UI);
		TextButton but_down = new TextButton("down", Game.NEUTRALIZER_UI);
		ClickListener clickListener;
		
		Table table_objects = new Table(Game.NEUTRALIZER_UI);
		ScrollPane scroll_pane_objects = new ScrollPane(table_objects, Game.NEUTRALIZER_UI);
		
		int id_node = -1;
		NodeObject current_node;
		
		LevelLayer current_layer;
		LevelLayer prew_layer;
		
		public ObjectsTable(Skin skin) {
			super(skin);
			this.setTouchable(Touchable.enabled);
			this.top().left();
			
			clickListener = new ClickListener() {
				@Override
				public void clicked(InputEvent event, float x, float y) {
					if(event.getTarget().isDescendantOf(table_objects)) {
						NodeObject node = GuiManager.getInstanceOf(table_objects, NodeObject.class, event.getTarget());
						ObjectsTable.this.setButton(node);
						EditorLayersTable.this.gui.core.getEAHandler()
								.setObject(EditorLayersTable.this.gui.core.getNodeByObject(node.getTerrainObject()));
					}
					
					super.clicked(event, x, y);
				}
			};
			
			this.addListener(clickListener);
			this.add(table_title).growX().top().left();
			this.row();
			table_title.add(new Label("Actions", skin)).left().expandX();
			table_title.add(but_up).right();
			table_title.add(but_down).right();
			table_title.setBackground("button");
			
			scroll_pane_objects.setScrollingDisabled(true, false);
			this.add(scroll_pane_objects).grow().left().top().expand();
			this.row();
		}
		
		@Override
		public void act(float delta) {
			super.act(delta);
			
			if(current_layer != null && table_objects.getChildren().size != current_layer.getObjectsList().size
					|| prew_layer != current_layer) {
				this.setLayerObjects(current_layer);
			}
			
			if(but_up.isChecked()) {
				but_up.setChecked(false);
				
				if(id_node != 0 && table_objects.getChildren().size > 0) {
					NodeObject node = (NodeObject) table_objects.getCells().get(id_node).getActor();
					NodeObject swap_node = (NodeObject) table_objects.getCells().get(id_node - 1).getActor();
					
					table_objects.getCells().get(id_node).setActor(swap_node);
					table_objects.addActor(node);
					table_objects.getCells().get(id_node - 1).setActor(node);
					table_objects.addActor(swap_node);
					
					id_node--;
					
					gui.core.getLevel().setTerrainObjectDown(node.tobject);
				}
			}
			
			if(but_down.isChecked()) {
				but_down.setChecked(false);
				
				if(id_node != table_objects.getChildren().size - 1 && table_objects.getChildren().size > 0) {
					NodeObject node = (NodeObject) table_objects.getCells().get(id_node).getActor();
					NodeObject swap_node = (NodeObject) table_objects.getCells().get(id_node + 1).getActor();
					
					table_objects.getCells().get(id_node + 1).setActor(node);
					table_objects.addActor(swap_node);
					table_objects.getCells().get(id_node).setActor(swap_node);
					table_objects.addActor(node);
					
					id_node++;
					
					gui.core.getLevel().setTerrainObjectUp(node.tobject);	
				}
				
				System.out.println("id_node: " + id_node);
			}
			
			
			prew_layer = current_layer;
		}
		
		public void setLayerObjects(LevelLayer layer) {
			table_objects.reset();
			table_objects.clear();
			
			for(int i = 0; i < layer.getObjectsList().size; i++) {
				table_objects.add(new NodeObject(layer.getObjectsList().get(i))).growX().top().left();
				table_objects.row();
			}
		}
		
		public void setButton(NodeObject node) {
			for(int i = 0; i < table_objects.getCells().size; i++) {
				if(table_objects.getCells().get(i).getActor() == node) {
					table_objects.getCells().get(i).getActor().setColor(Color.GREEN);
					id_node = i;
				} else if(table_objects.getCells().get(i) != null && table_objects.getCells().get(i).getActor() != null) {
					if(current_node != table_objects.getCells().get(i).getActor())
						table_objects.getCells().get(i).getActor().setColor(Color.WHITE);
					else table_objects.getCells().get(i).getActor().setColor(Color.CYAN);
				}
			}
		}
		
		class NodeObject extends Table {
			
			private TerrainObject tobject;
			private Image img;
			
			private TextButton but_swapToMain = new TextButton("Drag to\n main", Game.NEUTRALIZER_UI);
			private Label label_position = new Label("position", Game.NEUTRALIZER_UI);
			
			/** TEST */
			public NodeObject(TerrainObject tobject) {
				super(Game.NEUTRALIZER_UI);
				this.setBackground("button");
				this.setSize(192f, 48f);
				this.tobject = tobject;
				
				this.label_position.setText("" + tobject.getPosition());
				
				this.setTouchable(Touchable.enabled);
				this.top().left();
				
				this.add().size(48f);
				setImage(new Image(tobject.getTexture()));
				this.add(label_position).expandX();
				this.add(but_swapToMain).width(64f);
			}
			
			public TerrainObject getTerrainObject() {
				return tobject;
			}
			
			private void setImage(Image img) {
				float ratio = img.getWidth() / img.getHeight();
				
				this.getCells().first().setActor(img);
				this.img = img;
				
				this.getCells().first().size(48f * ratio * 1f, 48f * 1f);
			}
			
			@Override
			public void act(float delta) {
				super.act(delta);
				
				if(but_swapToMain.isChecked()) {
					but_swapToMain.setChecked(false);
					
					gui.core.getLevel().setTerrainObjectToMainLayer(tobject);
				}
			}
		}
	}
}
