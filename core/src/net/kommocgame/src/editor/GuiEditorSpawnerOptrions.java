package net.kommocgame.src.editor;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.ui.TextField.TextFieldFilter;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ArrayMap;
import com.badlogic.gdx.utils.OrderedMap;
import com.github.czyzby.kiwi.util.gdx.scene2d.Alignment;

import net.kommocgame.src.Game;
import net.kommocgame.src.DeadArena.objects.SpawnerEntityNode;
import net.kommocgame.src.editor.ElementsGui.ButApply;
import net.kommocgame.src.editor.ElementsGui.ButCancel;
import net.kommocgame.src.editor.nodes.DHObjects;
import net.kommocgame.src.editor.nodes.ENEntity;
import net.kommocgame.src.editor.nodes.ENodeObj;
import net.kommocgame.src.editor.objects.EObject;
import net.kommocgame.src.entity.EntityBase;
import net.kommocgame.src.gui.GuiBase;
import net.kommocgame.src.gui.GuiManager;
import net.kommocgame.src.world.DataObject;

public class GuiEditorSpawnerOptrions extends GuiBase {
	
	private GuiEditor gui;
	
	/** Current object that have relation this.*/
	private EObject object;
	
	private ArrayMap<SpawnerNode, TextField> order_map;
	private DataObject<Array<SpawnerEntityNode>> data;
	
	Window window;
	
	/** Table having a list of all entity's. */
	private Table table_nodes;
	
	/** Table having an control buttons. */
	private Table table_actions;
	
	/** Table having map of entity and his weight. */
	private Table table_order;
	
	private ElementsGui.ButApply but_apply = new ButApply();
	private ElementsGui.ButCancel but_cancel = new ButCancel();
	
	private float GUI_WIDTH;
	
	private SpawnerNode current_node;
	
	Table table_nodes_list = new Table(Game.NEUTRALIZER_UI);
	Table table_order_entity_list = new Table(Game.NEUTRALIZER_UI);
	TextButton but_add = new TextButton(">", Game.NEUTRALIZER_UI);
	TextButton but_remove = new TextButton("<", Game.NEUTRALIZER_UI);
	TextButton but_removeAll = new TextButton("<<", Game.NEUTRALIZER_UI);
	ArrayMap<Class<? extends EntityBase>, Float> map_date;
	
	
	TextFieldFilter filter = new TextFieldFilter() {
		@Override
		public boolean acceptChar(TextField textField, char c) {
			if((c >= '0' && c <= '9') || c == '.' || c == '-') {
				if(textField.getText().contains(".") && c == '.')
					return false;
				
				if(textField.getText().contains("-") && c == '-')
					return false;
				
				return true;
			}
			
			return false;
		}
	};
	
	ClickListener clickListener;
	
	public GuiEditorSpawnerOptrions(Game game, GuiEditor gui, EObject object, DataObject<Array<SpawnerEntityNode>> data) {
		super(game);
		this.gui = gui;
		this.object = object;
		this.data = data;
		order_map = new ArrayMap<GuiEditorSpawnerOptrions.SpawnerNode, TextField>();
		
		window = new Window("Spawner order", Game.NEUTRALIZER_UI);
		this.group_stage.addActor(window);
		GUI_WIDTH = getRatio(1f/0.9f);
		window.setSize(GUI_WIDTH, getRatio(1f/1.4f));
		window.setPosition(Gdx.graphics.getWidth() / 2f, Gdx.graphics.getHeight() / 2f, Alignment.CENTER.get());
		
		table_nodes		= new Table(Game.NEUTRALIZER_UI);
		table_actions	= new Table(Game.NEUTRALIZER_UI);
		table_order		= new Table(Game.NEUTRALIZER_UI);
		
		window.add(table_nodes).expand().left().minWidth(GUI_WIDTH * 2.5f/6).top();
		window.add(table_actions);
		window.add(table_order).expand().right().minWidth(GUI_WIDTH * 2.5f/6).top();
		
		//--------------- TABLE_NODES --------------------//
		Label label_nodes_title = new Label("Entity's", Game.NEUTRALIZER_UI);
		table_nodes.add(label_nodes_title);
		table_nodes.row();
		
		//Table table_nodes_list = new Table(Game.NEUTRALIZER_UI);
		
		for(int i = 0; i < DHObjects.getNodes().size; i++) {
			if(DHObjects.getNodes().get(i) instanceof ENEntity) {
				table_nodes_list.add(new SpawnerNode(DHObjects.getNodes().get(i))).growX();
				table_nodes_list.row();
			}
		}
		
		ScrollPane scroll_nodes_list = new ScrollPane(table_nodes_list, Game.NEUTRALIZER_UI);
		
		table_nodes.add(scroll_nodes_list).expand().left();
		
		//--------------- TABLE_ACTIONS --------------------//
		
		//TextButton but_add = new TextButton(">", Game.NEUTRALIZER_UI);
		//TextButton but_remove = new TextButton("<", Game.NEUTRALIZER_UI);
		//TextButton but_removeAll = new TextButton("<<", Game.NEUTRALIZER_UI);
		
		table_actions.add(but_add);
		table_actions.row();
		table_actions.add(but_remove);
		table_actions.row();
		table_actions.add(but_removeAll);
		
		//--------------- TABLE_ORDER --------------------//
		
		table_order.add(new Label("Entity's", Game.NEUTRALIZER_UI)).expand();
		table_order.add(new Label("Weight", Game.NEUTRALIZER_UI)).expand();
		table_order.row();
		
		//Table table_order_entity_list = new Table(Game.NEUTRALIZER_UI);
		//Table table_order_weight_list = new Table(Game.NEUTRALIZER_UI);
		
		Table table_order_scroll = new Table(Game.NEUTRALIZER_UI);
		table_order_scroll.add(table_order_entity_list);
		
		ScrollPane scroll_order = new ScrollPane(table_order_scroll, Game.NEUTRALIZER_UI);
		table_order.add(scroll_order).colspan(2);
		
		//--------------- WINDOW -------------------------//
		
		window.row();
		
		Table table_buttons = new Table(Game.NEUTRALIZER_UI);
		table_buttons.add(but_apply);
		table_buttons.add(but_cancel);
		
		window.add(table_buttons).colspan(3);
		
		clickListener = new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				super.clicked(event, x, y);
				System.out.println("GuiEditorSpawnerOptions(new instance) ### clickListener is captured.");
				
				if(GuiManager.getInstanceOf(window, SpawnerNode.class, event.getTarget()) != null) {
					System.out.println("GuiEditorSpawnerOptions(new instance) ### has a equal.");
					if(current_node != null) {
						current_node.setChecked(false);
						current_node.setColor(Color.WHITE);
					}
					
					current_node = GuiManager.getInstanceOf(window, SpawnerNode.class, event.getTarget());
					current_node.setColor(Color.GREEN);
				}
			}
		};
		
		window.addListener(clickListener);
		
		map_date = new ArrayMap<Class<? extends EntityBase>, Float>();
		for(SpawnerEntityNode node : data.getParameter())
			map_date.put(node.getClassInstance(), node.getWeight());
		
		
		if(map_date.size > 0)
		for(int i = 0; i < map_date.size; i++) {
			ENodeObj enodeObj = null;
			
			for(int j = 0; j < DHObjects.getNodes().size; j++) {
				if(DHObjects.getNodes().get(j).getInstanceClass() == map_date.getKeyAt(i)) {
					enodeObj = DHObjects.getNodes().get(j);
				}
			}
			
			this.addToPool(new SpawnerNode(enodeObj), map_date.getValueAt(i));
		}
	}
	
	public void removeAll() {
		for(int i = 0; i < order_map.size; i++) {
			this.removeFromPool(order_map.getKeyAt(i));
		}
	}
	
	public void addToPool(SpawnerNode node) {
		this.addToPool(node, 1);
	}
	
	public void addToPool(SpawnerNode node, float value) {
		if(order_map.containsKey(node))
			return;
		
		node.remove();
		
		table_order_entity_list.add(node).expand().left().minWidth(GUI_WIDTH * 2.0f/6).top();
		
		TextField field = new TextField(String.valueOf(value), Game.NEUTRALIZER_UI);
		field.setTextFieldFilter(filter);
		
		order_map.put(node, field);
		table_order_entity_list.add(field).minWidth(GUI_WIDTH * 0.5f/6);
		table_order_entity_list.row();
	}
	
	public void removeFromPool(SpawnerNode node) {
		if(!order_map.containsKey(node))
			return;
		
		node.remove();
		order_map.get(node).remove();
		table_nodes_list.add(node).growX();
		table_nodes_list.row();
		
		order_map.removeKey(node);
	}
	
	@Override
	public void update(SpriteBatch batch) {
		super.update(batch);
		
		if(but_add.isChecked()) {
			but_add.setChecked(false);
			
			if(current_node != null)
				this.addToPool(current_node);
		} if(but_remove.isChecked()) {
			but_remove.setChecked(false);
			
			if(current_node != null)
				this.removeFromPool(current_node);
		} if(but_removeAll.isChecked()) {
			but_removeAll.setChecked(false);
			this.removeAll();
		}
		
		if(but_apply.isChecked()) {
			but_apply.setChecked(false);
			
			data.getParameter().clear();
			
			if(order_map.size > 0)
			for(int i = 0; i < order_map.size; i++) {
				SpawnerNode node = order_map.getKeyAt(i);
				data.getParameter().add(new SpawnerEntityNode(node.class_instance, Float.valueOf(order_map.get(node).getText())));
			}
			
			game.guiManager.removeGui();
		}
		
		if(but_cancel.isChecked()) {
			but_apply.setChecked(false);
			
			game.guiManager.removeGui();
		}
		
		//window.debugAll();
	}
	
	public class SpawnerNode extends Button {
		
		private Image texture_obj;
		private Label label_name;
		private Class<? extends EntityBase> class_instance;
		
		public SpawnerNode(ENodeObj eobject) {
			super(Game.NEUTRALIZER_UI);
			
			class_instance = eobject.getInstanceClass();
			texture_obj = new Image(eobject.getImage().getDrawable());
			label_name = new Label(eobject.getName(), Game.NEUTRALIZER_UI);
			
			this.add(label_name).expand().left();
			this.add(texture_obj).width(GuiEditorSpawnerOptrions.getRatio(1f/16f)).height(GuiEditorSpawnerOptrions.getRatio(1f/18f))
			.pad(GuiEditorSpawnerOptrions.getRatio(1f/64f));
		}
	}

}
