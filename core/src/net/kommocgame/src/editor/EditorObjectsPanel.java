package net.kommocgame.src.editor;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.github.czyzby.kiwi.util.gdx.scene2d.Alignment;

import net.kommocgame.src.Game;
import net.kommocgame.src.control.InputHandler;
import net.kommocgame.src.editor.nodes.ENodeObj;
import net.kommocgame.src.gui.GuiManager;
import net.kommocgame.src.world.World;

public class EditorObjectsPanel extends Table {
	
	TextButton but_OBJECTS_Terrain = new TextButton("Terrain", Game.NEUTRALIZER_UI);
	TextButton but_OBJECTS_Entity = new TextButton("Entity", Game.NEUTRALIZER_UI);
	TextButton but_OBJECTS_Wall = new TextButton("Wall", Game.NEUTRALIZER_UI);
	TextButton but_OBJECTS_Props = new TextButton("Props", Game.NEUTRALIZER_UI);
	TextButton but_OBJECTS_Mobs = new TextButton("Mobs", Game.NEUTRALIZER_UI);
	TextButton but_OBJECTS_All = new TextButton("All", Game.NEUTRALIZER_UI);
	TextButton but_OBJECTS_Triggers = new TextButton("Triggers", Game.NEUTRALIZER_UI);
	TextButton but_OBJECTS_Misc = new TextButton("Misc", Game.NEUTRALIZER_UI);
	
	Table table_tabs = new Table(Game.NEUTRALIZER_UI);
	
	Table scroll_table = new Table(Game.NEUTRALIZER_UI);
	ScrollPane scroll_pane = new ScrollPane(scroll_table, Game.NEUTRALIZER_UI);
	ClickListener panel_listener;
	
	private static int 			tab			= 7;
	
	EditorCore core;
	GuiEditor gui;
	boolean isGrab = false;
	
	public float node_size = 1f;
	
	public EditorObjectsPanel(GuiEditor gui) {
		this(gui, Game.NEUTRALIZER_UI);
	}

	public EditorObjectsPanel(GuiEditor gui, Skin skin) {
		super(skin);
		this.setTouchable(Touchable.enabled);
		
		this.core = gui.core;
		this.gui = gui;
		
		this.setBackground("panel");
		this.top().left();
		
		table_tabs.setBackground("panel");
		table_tabs.setColor(Color.CYAN);
		table_tabs.top().left();
		
		table_tabs.add(but_OBJECTS_Terrain);
		table_tabs.add(but_OBJECTS_Wall);
		table_tabs.add(but_OBJECTS_Props);
		table_tabs.add(but_OBJECTS_Mobs);
		table_tabs.add(but_OBJECTS_Entity);
		table_tabs.add(but_OBJECTS_Triggers);
		table_tabs.add(but_OBJECTS_All);
		table_tabs.add(but_OBJECTS_Misc);
		
		this.add(table_tabs).growX().left().height(but_OBJECTS_Entity.getHeight() * 1.1f);
		this.row();
		
		this.add(scroll_pane).left().top().expandX();
		scroll_table.left().top();
		
		this.setButton(but_OBJECTS_All);
		
		panel_listener = new ClickListener() {
			@Override
			public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
				if(event.getTarget() != null) {
					if(event.getTarget().getParent() instanceof PanelNode) {
						PanelNode node = (PanelNode) event.getTarget().getParent();
						
						ClickListener click_event = node.clickListener;
						click_event.touchDown(event, x, y, pointer, button);
					} else if(event.getTarget() instanceof PanelNode) {
						PanelNode node = (PanelNode) event.getTarget();
						
						ClickListener click_event = node.clickListener;
						click_event.touchDown(event, x, y, pointer, button);
					} else if(event.getTarget().getParent().getParent() instanceof PanelNode) {
						PanelNode node = (PanelNode) event.getTarget().getParent().getParent();
						
						ClickListener click_event = node.clickListener;
						click_event.touchDown(event, x, y, pointer, button);
					}
				}
				
				return super.touchDown(event, x, y, pointer, button);
			}
			
			@Override
			public void touchDragged(InputEvent event, float x, float y, int pointer) {
				super.touchDragged(event, x, y, pointer);
				
				if(event.getTarget() != null) {
					if(event.getTarget().getParent() instanceof PanelNode) {
						PanelNode node = (PanelNode) event.getTarget().getParent();
						
						ClickListener click_event = node.clickListener;
						click_event.touchDragged(event, x, y, pointer);
					} else if(event.getTarget() instanceof PanelNode) {
						PanelNode node = (PanelNode) event.getTarget();
						
						ClickListener click_event = node.clickListener;
						click_event.touchDragged(event, x, y, pointer);
					} else if(event.getTarget().getParent().getParent() instanceof PanelNode) {
						PanelNode node = (PanelNode) event.getTarget().getParent().getParent();
						
						ClickListener click_event = node.clickListener;
						click_event.touchDragged(event, x, y, pointer);
					}
				}
			}
			
			@Override
			public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
				super.touchUp(event, x, y, pointer, button);
				
				if(event.getTarget() != null) {
					if(event.getTarget().getParent() instanceof PanelNode) {
						PanelNode node = (PanelNode) event.getTarget().getParent();
						
						ClickListener click_event = node.clickListener;
						click_event.touchUp(event, x, y, pointer, button);
					} else if(event.getTarget() instanceof PanelNode) {
						PanelNode node = (PanelNode) event.getTarget();
						
						ClickListener click_event = node.clickListener;
						click_event.touchUp(event, x, y, pointer, button);
					} else if(event.getTarget().getParent().getParent() instanceof PanelNode) {
						PanelNode node = (PanelNode) event.getTarget().getParent().getParent();
						
						ClickListener click_event = node.clickListener;
						click_event.touchUp(event, x, y, pointer, button);
					}
				}
			}
			
			@Override
			public void clicked(InputEvent event, float x, float y) {
				if(event.getTarget().isDescendantOf(table_tabs)) {
					EditorObjectsPanel.this.setButton(GuiManager.getInstanceOf(table_tabs, TextButton.class, event.getTarget()));
				}
				
				super.clicked(event, x, y);
			}
		};
		this.addListener(panel_listener);
	}
	
	@Override
	public void act(float delta) {
		super.act(delta);
		if(isGrab) {
			scroll_pane.cancel();
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
		}
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
	
	public void addNodeToTable(PanelNode node) {//TODO
		int countInLine = (int)this.getWidth() / (int)(PanelNode.WIDTH_NODE * node_size);
		
		if(countInLine != 0 && scroll_table.getCells().size % countInLine == 0) {
			scroll_table.row();
			scroll_table.add(node).width(node.getWidth()).height(node.getHeight());
		} else scroll_table.add(node).width(node.getWidth()).height(node.getHeight());
	}
	
	/** New Instance of node. */
	public PanelNode createNewNode(ENodeObj node) {
		return new PanelNode(node);
	}
	
	public Table getScrollTable() {
		return scroll_table;
	}
	
	public class PanelNode extends Table {
		
		private static final float WIDTH_NODE = 96f;
		private static final float HEIGHT_NODE = 64f;
		
		public Label label_name = new Label("name_", Game.NEUTRALIZER_UI);
		public Image main_image;
		private Table img_canvas = new Table(Game.KOMMOC_UI);
		private ENodeObj node_obj;
		
		private Image drag_image;
		private boolean spawned = false;
		
		private long lastTime = 0l;
		private ClickListener clickListener;
		
		private boolean isCaptured = false;
		
		public PanelNode(ENodeObj node) {
			this(node.getImage(), node.getName());
			this.node_obj = node;
			
			setTouchable(Touchable.enabled);
			addListener(clickListener = new ClickListener() {
				public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
					if(core.getWorld() == null)
						return false;
					System.out.println("		drag_image: " + drag_image);
					
					if(drag_image!= null) {
						drag_image.remove();
						drag_image = null;
					}
					
					if(drag_image == null) {
						System.out.println("		drag_image set new instance");
						
						drag_image = new Image(node_obj.getImage().getDrawable());
						drag_image.setSize(main_image.getWidth(), main_image.getHeight());
						drag_image.setPosition(InputHandler.GUI_mouse_x, InputHandler.GUI_mouse_y, Alignment.CENTER.get());
						drag_image.setTouchable(Touchable.disabled);
						
						PanelNode.this.getStage().addActor(drag_image);
					}
					scroll_pane.setCancelTouchFocus(true);
					isCaptured = true;
					isGrab = true;
					
					System.out.println("	event:	touchDown");
					return super.touchDown(event, x, y, pointer, button);
				}

				public void touchDragged (InputEvent event, float x, float y, int pointer) {
					if(core.getWorld() == null)
						return;
					
					super.touchDragged(event, x, y, pointer);
					drag_image.setPosition(InputHandler.GUI_mouse_x, InputHandler.GUI_mouse_y, Alignment.CENTER.get());
					
					if(!panel_listener.isOver()) {
						spawned = true;
					}
					
					if(spawned) {
						node_obj.dragToWorld(InputHandler.BOX2D_mouse_x, InputHandler.BOX2D_mouse_y, EditorObjectsPanel.this.core.getWorld());
					}
					
					isCaptured = true;
				}

				public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
					if(core.getWorld() == null)
						return;
					
					if(!panel_listener.isOver()) {
						gui.core.addObject(node_obj.instance);
						//node_obj.addToPanel(gui.panel_levelObjects);
						node_obj.releaseInWorld(InputHandler.BOX2D_mouse_x, InputHandler.BOX2D_mouse_y);
					} else node_obj.cancel(core.getWorld());
					
					scroll_pane.setCancelTouchFocus(false);
					
					if(drag_image != null)
						drag_image.remove();
					
					isGrab = false;
					isCaptured = false;
					drag_image = null;
					spawned = false;
					System.out.println("	event:	touchUp");
				}
			});
			
			//System.out.println("	EditorObjectsPanel: add click listener.");
		}
		
		private PanelNode(Image img, String name) {
			this(img, name, Game.NEUTRALIZER_UI);
		}
		
		@Override
		public void act(float delta) {
			super.act(delta);
		}
		
		private PanelNode(Image img, String name, Skin skin) {
			super(skin);
			this.setBackground("button");
			main_image = img;
			
			this.setClip(true);
			this.setSize(WIDTH_NODE * node_size, HEIGHT_NODE * node_size);
			this.add(img_canvas).expand().center();
			this.row();
			this.add(label_name).center();
			label_name.setText(name);
			this.setImage(img);
		}
		
		private void setImage(Image img) {
			main_image = img;
			float ratio = img.getWidth() / img.getHeight();
			
			img_canvas.clear();
			img_canvas.add(main_image);
			img_canvas.getCell(main_image).size(HEIGHT_NODE * ratio * (node_size - 0.3f), HEIGHT_NODE * (node_size - 0.3f));
		}
		
		public ENodeObj getDHNode() {
			return node_obj;
		}
		
		public boolean isCaptured() {
			return false;
		}
	}
}
