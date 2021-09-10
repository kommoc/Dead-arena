package net.kommocgame.src.editor;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Tree;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.ScissorStack;
import com.github.czyzby.kiwi.util.gdx.scene2d.Alignment;

import net.kommocgame.src.Game;
import net.kommocgame.src.GameState;
import net.kommocgame.src.Loader;
import net.kommocgame.src.control.IMouse;
import net.kommocgame.src.control.InputHandler;
import net.kommocgame.src.editor.EditorObjectsPanel.PanelNode;
import net.kommocgame.src.gui.GuiBase;
import net.kommocgame.src.gui.game.DAGuiMainMenu_2;

public class GuiEditor extends GuiBase implements IMouse {
	
	private ScissorStack stack = new ScissorStack();
	public final int PANEL_INSPECTOR = 0, PANEL_PROPERTIES = 1;
	
	public ClickListener clickListener;
	
	/** Check an object at the cursor. */
	@Deprecated
	private boolean canDrag = false;
	@Deprecated
	private Actor touch_obj;
	
	@Deprecated
	private Actor touch_obj_INSPECTOR;
	@Deprecated
	private Actor prew_touch_obj_INSPECTOR;
	
	public Label label_worldTick = new Label("World tick: ", Game.NEUTRALIZER_UI);
	
	/** NOT USING */@Deprecated
	public EditorButton but_addObject = new EditorButton(Game.NEUTRALIZER_UI, Loader.guiEditor("but/but_addObject.png"));
	/** NOT USING */@Deprecated
	public EditorButton but_delete = new EditorButton(Game.NEUTRALIZER_UI, Loader.guiEditor("but/but_delete.png"));
	/** NOT USING */@Deprecated
	public EditorButton but_undo = new EditorButton(Game.NEUTRALIZER_UI, Loader.guiEditor("but/but_undo.png"));
	/** NOT USING */@Deprecated
	public EditorButton but_rotate = new EditorButton(Game.NEUTRALIZER_UI, Loader.guiEditor("but/but_rotate.png"));
	/** NOT USING */@Deprecated
	public EditorButton but_settings = new EditorButton(Game.NEUTRALIZER_UI, Loader.guiEditor("but/but_settings.png"));
	/** NOT USING */@Deprecated
	public EditorButton but_translation = new EditorButton(Game.NEUTRALIZER_UI, Loader.guiEditor("but/menu/icon_axis.png"));
	/** NOT USING */@Deprecated
	public EditorButton but_rotation = new EditorButton(Game.NEUTRALIZER_UI, Loader.guiEditor("but/menu/icon_rotate.png"));
	
	public Image icon_FILE_newFile = new Image(Loader.guiEditor("but/menu/icon_newFile.png"));
	public Image icon_FILE_openFile = new Image(Loader.guiEditor("but/menu/icon_openFile.png"));
	public Image icon_FILE_saveFile = new Image(Loader.guiEditor("but/menu/icon_save.png"));
	public Image icon_FILE_exit = new Image(Loader.guiEditor("but/menu/icon_cancel.png"));
	
	public Image icon_EXTENTION_loading = new Image(Loader.guiEditor("but/menu/icon_loading.png"));
	public Image icon_EXTENTION_entity = new Image(Loader.guiEditor("but/menu/icon_entity.png"));
	public Image icon_EXTENTION_prop = new Image(Loader.guiEditor("but/menu/icon_prop.png"));
	public Image icon_EXTENTION_trigger = new Image(Loader.guiEditor("but/menu/icon_trigger.png"));
	public Image icon_EXTENTION_wall = new Image(Loader.guiEditor("but/menu/icon_wall.png"));
	
	@Deprecated
	public EditorButton but_EDITOR_camera= new EditorButton(Game.NEUTRALIZER_UI, Loader.guiEditor("but/menu/icon_camera.png"));
	@Deprecated
	public EditorButton but_EDITOR_edit = new EditorButton(Game.NEUTRALIZER_UI, Loader.guiEditor("but/menu/icon_edit.png"));
	@Deprecated
	public EditorButton but_EDITOR_choose = new EditorButton(Game.NEUTRALIZER_UI, Loader.guiEditor("but/menu/icon_choose.png"));
	
	public EditorButton but_GAME_play = new EditorButton(Game.NEUTRALIZER_UI, Loader.guiEditor("but/menu/icon_game_play.png"));
	public EditorButton but_GAME_pause = new EditorButton(Game.NEUTRALIZER_UI, Loader.guiEditor("but/menu/icon_game_pause.png"));
	public EditorButton but_GAME_stop = new EditorButton(Game.NEUTRALIZER_UI, Loader.guiEditor("but/menu/icon_game_stop.png"));
	
	public TextButton but_GAME_AI = new TextButton("AI", Game.NEUTRALIZER_UI);
	public TextButton but_PARAMS_LAYER = new TextButton("Layers", Game.NEUTRALIZER_UI);
	public TextButton but_GAME_DEBUG_BOX2D = new TextButton("Debug box2d", Game.NEUTRALIZER_UI);
	public TextButton but_GAME_DEBUG_PRINT = new TextButton("Debug entity", Game.NEUTRALIZER_UI);
	public EditorButton but_GAME_GRIDNODE = new EditorButton(Game.NEUTRALIZER_UI, Loader.guiEditor("but/menu/icon_gridNode.png"));
	public EditorUpperPanel panel = new EditorUpperPanel("File", 55, 55).openType(OpenTab.TAP);
	
	/** NOT USING */
	public EditorExtensionPanel panel_extention = new EditorExtensionPanel("Add object...", 0, 0).setAdditor(but_addObject).openType(OpenTab.TAP);
	/** NOT USING */
	public Table panel_inspector = new Table(Game.NEUTRALIZER_UI);
	
	public EditorParamsPanel 		panel_param = new EditorParamsPanel(this, "Parameters", Game.NEUTRALIZER_UI);
	public EditorObjectsPanel 		panel_objects;
	public EditorLevelObjectsPanel 	panel_levelObjects = new EditorLevelObjectsPanel(this);
	public EditorInfoLine 			panel_infoLine = new EditorInfoLine(this);
	public EditorLayersTable		panel_layer = new EditorLayersTable(this);
	
	public GuiDialog 				guiConfirm = null;
	public GuiEditorNewLevel 		guiNewLevel = null;
	public GuiEditorLoadLevel 		guiLoadLevel = null;
	public GuiEditorLevelSettings	guiLevelSetting = null; 
	
	@Deprecated
	public GuiEditorNewEntity guiNewEntity = null;
	
	public EditorCore core = new EditorCore(this);

	@Deprecated
	public Table table_inspector = new Table(Game.NEUTRALIZER_UI);
	@Deprecated
	public Tree tree_ENTITY = new Tree(Game.NEUTRALIZER_UI);
	@Deprecated
	public Tree tree_PROP = new Tree(Game.NEUTRALIZER_UI);
	@Deprecated
	public Tree tree_WALL = new Tree(Game.NEUTRALIZER_UI);
	@Deprecated
	public Tree tree_OBJECT = new Tree(Game.NEUTRALIZER_UI);
	@Deprecated
	public Tree tree_TERRAINOBJ = new Tree(Game.NEUTRALIZER_UI);
	
	public ScrollPane scroll_inspector;
	
	private boolean panel_layer_open = false;
	
	private float camera_prew_pos_x, camera_prew_pos_y, x_press, y_press;
	
	public GuiEditor(Game game) {
		super(game);
		InputHandler.registerIMouse(this);
		panel_objects = new EditorObjectsPanel(this);
		
		/** NOT USING */
		//group_stage.addActor(but_addObject);
		/** NOT USING */
		//group_stage.addActor(but_delete);
		/** NOT USING */
		//group_stage.addActor(but_undo);
		/** NOT USING */
		//group_stage.addActor(but_rotate);
		/** NOT USING */
		//group_stage.addActor(but_settings);
		/** NOT USING */
		//group_stage.addActor(but_translation);
		/** NOT USING */
		//group_stage.addActor(but_rotation);
		
		/** NOT USING */
		//group_stage.addActor(but_EDITOR_camera);
		/** NOT USING */
		//group_stage.addActor(but_EDITOR_edit);
		/** NOT USING */
		//group_stage.addActor(but_EDITOR_choose);
		
		but_addObject.setSize(getRatio(1f/16f), getRatio(1f/16f));
		but_delete.setSize(getRatio(1f/16f), getRatio(1f/16f));
		but_undo.setSize(getRatio(1f/16f), getRatio(1f/16f));
		but_rotate.setSize(getRatio(1f/16f), getRatio(1f/16f));
		but_settings.setSize(getRatio(1f/16f), getRatio(1f/16f));
		but_EDITOR_camera.setSize(getRatio(1f/16f), getRatio(1f/16f));
		but_EDITOR_edit.setSize(getRatio(1f/16f), getRatio(1f/16f));
		but_EDITOR_choose.setSize(getRatio(1f/16f), getRatio(1f/16f));
		but_translation.setSize(getRatio(1f/16f), getRatio(1f/16f));
		but_rotation.setSize(getRatio(1f/16f), getRatio(1f/16f));
		
		
		but_addObject.setPosition(getRatio(1f/32f), getRatio(8f/9f), Alignment.TOPLEFT.get());
		but_delete.setPosition(but_addObject.getX(), but_addObject.getY(), Alignment.TOPLEFT.get());
		but_undo.setPosition(but_addObject.getX(), but_delete.getY(), Alignment.TOPLEFT.get());
		but_rotate.setPosition(but_addObject.getX(), but_undo.getY(), Alignment.TOPLEFT.get());
		but_settings.setPosition(but_addObject.getX(), but_rotate.getY(), Alignment.TOPLEFT.get());
		
			but_translation.setPosition(but_addObject.getX(), but_addObject.getY(), Alignment.TOPLEFT.get());
			but_rotation.setPosition(but_addObject.getX(), but_translation.getY(), Alignment.TOPLEFT.get());
		
		but_EDITOR_camera.setPosition(but_addObject.getX(), getRatio(1f/6f) / 2f, Alignment.TOPLEFT.get());
		but_EDITOR_edit.setPosition(but_EDITOR_camera.getX(Alignment.RIGHT.get()) + but_EDITOR_camera.getHeight() / 16f, getRatio(1f/6f) / 2f, Alignment.TOPLEFT.get());
		but_EDITOR_choose.setPosition(but_EDITOR_edit.getX(Alignment.RIGHT.get()) + but_EDITOR_edit.getHeight() / 16f, getRatio(1f/6f) / 2f, Alignment.TOPLEFT.get());
		
		group_stage.addActor(panel);
		panel.addButton("New level", icon_FILE_newFile);
		panel.addButton("Open level", icon_FILE_openFile);
		panel.addButton("Save level", icon_FILE_saveFile);
		panel.addButton("Level settings", icon_EXTENTION_loading);
		panel.addButton("Exit to menu", icon_FILE_exit);
		
		/** NOT USING */
		//group_stage.addActor(panel_extention);
		panel_extention.addButton("Entity", icon_EXTENTION_entity);
		panel_extention.addButton("Prop", icon_EXTENTION_prop);
		panel_extention.addButton("Trigger", icon_EXTENTION_trigger);
		panel_extention.addButton("Wall", icon_EXTENTION_wall);
		panel_extention.addButton("Terrain obj", icon_EXTENTION_wall);
		
		
		panel.setPosition(0, getRatio(1f/2f), Alignment.TOPLEFT.get());
		panel_extention.setPosition(but_addObject.getX(Alignment.RIGHT.get()) / 2f, but_addObject.getY(Alignment.TOPRIGHT.get()) / 2f, Alignment.TOPLEFT.get());
		
		table_inspector.setSize(getRatio(1f/2f), getRatio(1f/2f));
		table_inspector.setPosition(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), Alignment.TOPRIGHT.get());
		table_inspector.top().left();
		table_inspector.setTransform(true);
		
		panel_inspector.setBackground("panel");
		
		table_inspector.add(tree_ENTITY).top().left();
		//FIXME
		//tree_ENTITY.add(new Node(new Label("Entity", Game.NEUTRALIZER_UI)));
		tree_ENTITY.pack();
		table_inspector.row();
		table_inspector.add(tree_PROP).top().left();
		//FIXME
		//tree_PROP.add(new Node(new Label("Prop", Game.NEUTRALIZER_UI)));
		tree_PROP.pack();
		table_inspector.row();
		table_inspector.add(tree_OBJECT).top().left();
		//FIXME
		//tree_OBJECT.add(new Node(new Label("Object", Game.NEUTRALIZER_UI)));
		tree_OBJECT.pack();
		table_inspector.row();
		table_inspector.add(tree_WALL).top().left();
		//FIXME
		//tree_WALL.add(new Node(new Label("Wall", Game.NEUTRALIZER_UI)));
		tree_WALL.pack();
		table_inspector.add(tree_TERRAINOBJ).top().left();
		//FIXME
		//tree_TERRAINOBJ.add(new Node(new Label("Terrain objects", Game.NEUTRALIZER_UI)));
		tree_TERRAINOBJ.pack();
		
		scroll_inspector = new ScrollPane(table_inspector);
		scroll_inspector.setFillParent(true);
		
		panel_inspector.setSize(getRatio(1f/2f), getRatio(1f/2f));
		panel_inspector.setPosition(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), Alignment.TOPRIGHT.get());
		
		panel_inspector.setTransform(true);
		panel_inspector.add(table_inspector).width(panel_inspector.getWidth() * 9f / 10f).height(panel_inspector.getHeight()).fill();
		panel_inspector.add(scroll_inspector).width(panel_inspector.getWidth() / 10f).fill();
		/** NOT USING */
		//group_stage.addActor(panel_inspector);
		
		panel_levelObjects.setSize(getRatio(1f/2f), getRatio(1f/2f));
		panel_levelObjects.setPosition(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), Alignment.TOPRIGHT.get());
		panel_levelObjects.setTransform(true);
		group_stage.addActor(panel_levelObjects);
		
		panel_param.setSize(getRatio(1f/2f), getRatio(1f/2f));
		panel_param.setPosition(panel_inspector.getX(Alignment.LEFT.get()), panel_inspector.getY(Alignment.BOTTOM.get()), Alignment.TOPLEFT.get());
		group_stage.addActor(panel_param);
		
		panel_layer.setSize(getRatio(1f/2f), getRatio(1f));
		panel_layer.setPosition(panel_inspector.getX(Alignment.LEFT.get()), panel_inspector.getY(Alignment.TOP.get()), Alignment.TOPLEFT.get());
		group_stage.addActor(panel_layer);
		panel_layer.setTouchable(Touchable.disabled);
		panel_layer.setVisible(false);
		if(panel_layer_open) 
			but_PARAMS_LAYER.setColor(Color.GREEN);
		else but_PARAMS_LAYER.setColor(Color.RED);
		
		but_GAME_stop.setSize(getRatio(1f/20f), getRatio(1f/20f));
		but_GAME_play.setSize(getRatio(1f/20f), getRatio(1f/20f));
		but_GAME_pause.setSize(getRatio(1f/20f), getRatio(1f/20f));
		but_GAME_pause.setPosition(Gdx.graphics.getWidth() / 2f, getRatio(15f/16f), Alignment.CENTER.get());
		group_stage.addActor(but_GAME_pause);
		but_GAME_play.setPosition(but_GAME_pause.getX(), but_GAME_pause.getY(), Alignment.BOTTOMRIGHT.get());
		group_stage.addActor(but_GAME_play);
		but_GAME_stop.setPosition(but_GAME_pause.getX(Alignment.RIGHT.get()), but_GAME_pause.getY(), Alignment.BOTTOMLEFT.get());
		group_stage.addActor(but_GAME_stop);
		
		but_GAME_AI.setPosition(but_GAME_stop.getX(Alignment.RIGHT.get()) + but_GAME_stop.getHeight() / 2f, but_GAME_stop.getY());
		group_stage.addActor(but_GAME_AI);
		
		but_GAME_GRIDNODE.setSize(getRatio(1f/20f), getRatio(1f/20f));
		but_GAME_GRIDNODE.setPosition(but_GAME_AI.getX(Alignment.RIGHT.get()) + but_GAME_stop.getHeight() / 2f, but_GAME_stop.getY());
		group_stage.addActor(but_GAME_GRIDNODE);
		
		but_PARAMS_LAYER.setPosition(but_GAME_GRIDNODE.getX(Alignment.LEFT.get()), but_GAME_GRIDNODE.getY() - but_GAME_GRIDNODE.getHeight());
		group_stage.addActor(but_PARAMS_LAYER);
		
		but_GAME_DEBUG_BOX2D.setPosition(but_GAME_play.getX(Alignment.LEFT.get()) - but_GAME_stop.getHeight(), but_GAME_stop.getY()
				+ but_GAME_DEBUG_BOX2D.getHeight(), Alignment.RIGHT.get());
		group_stage.addActor(but_GAME_DEBUG_BOX2D);
		but_GAME_DEBUG_PRINT.setPosition(but_GAME_play.getX(Alignment.LEFT.get()) - but_GAME_stop.getHeight(), but_GAME_DEBUG_BOX2D.getY(),
				Alignment.TOPRIGHT.get());
		group_stage.addActor(but_GAME_DEBUG_PRINT);
		
		if(Game.profile.settings_debug_bounds())
			but_GAME_DEBUG_BOX2D.setColor(Color.GREEN);
		else but_GAME_DEBUG_BOX2D.setColor(Color.RED);
		
		if(Game.profile.settings_debug_text())
			but_GAME_DEBUG_PRINT.setColor(Color.GREEN);
		else but_GAME_DEBUG_PRINT.setColor(Color.RED);
		
		group_stage.addActor(label_worldTick);
		label_worldTick.setPosition(but_GAME_play.getX(Alignment.CENTER.get()), but_GAME_play.getY() - but_GAME_play.getHeight() / 2f,
				Alignment.CENTER.get());
		label_worldTick.setColor(Color.RED);
		
		group_stage.addActor(panel_objects);
		panel_objects.setPosition(0, 0);
		panel_objects.setSize(panel_param.getX(), Gdx.graphics.getHeight() / 3f);
		
		group_stage.addActor(panel_infoLine);
		panel_infoLine.setPosition(panel_levelObjects.getX() - panel_levelObjects.getWidth() / 32f,
				panel_objects.getY(Alignment.TOP.get()) + panel_levelObjects.getWidth() / 32f, Alignment.BOTTOMRIGHT.get());
		
		/** TEST FIXME DELETE */
			for(int i = 0; i < game.DH_OBJECTS.getNodes().size; i++) {
				PanelNode node = panel_objects.createNewNode(game.DH_OBJECTS.getNodes().get(i));
				panel_objects.addNodeToTable(node);
			}
		
		this.initCore();
		
		clickListener = new ClickListener();
		group_stage.addListener(clickListener);
	}
	
	@Deprecated
	public void initCore() {
		switch (core.getEditorMode()) {
			case 3: {
				but_EDITOR_camera.setColor(Color.SKY);
				but_EDITOR_edit.setColor(Color.SKY);
				but_EDITOR_choose.setColor(Color.RED);
				break;
			} case 2: {
				but_EDITOR_camera.setColor(Color.RED);
				but_EDITOR_edit.setColor(Color.SKY);
				but_EDITOR_choose.setColor(Color.SKY);
				break;
			} case 1: {
				but_EDITOR_camera.setColor(Color.SKY);
				but_EDITOR_choose.setColor(Color.SKY);
				but_EDITOR_edit.setColor(Color.RED);
				break;
			}
		}
		
		label_worldTick.setText("World time: " + (core.getWorld() != null ? core.getWorldTick() : "NULL"));
	}
	
	public void update(SpriteBatch batch) {
		this.initCore();
		core.update();
		
		if(game.guiManager.guiList.peek().equals(this)) {
			group_stage.setTouchable(Touchable.enabled);
			guiNewLevel = null;
			guiLoadLevel = null;
			guiNewEntity = null;
			guiLevelSetting = null;
		}
		
		if(but_EDITOR_camera.isPressed()) {
			core.setVievMode();
		} else if(but_EDITOR_edit.isPressed()) {
			core.setEditMode();
		} else if(but_EDITOR_choose.isPressed()) {
			core.setChooseMode();
		}
		
		if(panel_extention.getButton(1).isPressed() && guiNewEntity == null) {
			guiNewEntity = new GuiEditorNewEntity(game, core);
			game.guiManager.addGui(guiNewEntity);
			group_stage.setTouchable(Touchable.disabled);
		}
		
		if(panel.getButton(0).isPressed() && guiNewLevel == null) {
			guiNewLevel = new GuiEditorNewLevel(game, core);
			game.guiManager.addGui(guiNewLevel);
			group_stage.setTouchable(Touchable.disabled);
		}
		
		if(panel.getButton(1).isPressed() && guiLoadLevel == null) {
			guiLoadLevel = new GuiEditorLoadLevel(game, core);
			game.guiManager.addGui(guiLoadLevel);
			group_stage.setTouchable(Touchable.disabled);
		}
		
		if(panel.getButton(3).isPressed() && guiLevelSetting == null) {
			guiLevelSetting = new GuiEditorLevelSettings(game, core);
			game.guiManager.addGui(guiLevelSetting);
			group_stage.setTouchable(Touchable.disabled);
		}
		
		if(panel.getButton(4).isPressed() && guiConfirm == null) {
			guiConfirm = new GuiDialog(game, "Do you want to exit?");
			System.out.println("new gui");
			game.guiManager.addGui(guiConfirm);
		}
		
		if(guiConfirm != null && guiConfirm.dialog.isActive()) {
			if(guiConfirm.dialog._apply()) {
				if(core.getWorld() != null)
					core.destroyCore();
				
				game.guiManager.reset();
				game.guiManager.addGui(new DAGuiMainMenu_2(game));
				guiConfirm = null;
			} else if(guiConfirm.dialog._cancel()) {
				game.guiManager.removeGui();
				guiConfirm = null;
			}
		}
		
		if(panel.getButton(2).isChecked()) {
			panel.getButton(2).setChecked(false);
			core.saveLevel();
		}
		
		this.initGameButton(batch);
		//group_stage.debugAll();
		//panel_objects.debugAll();
		//panel_levelObjects.debugAll();
		//System.out.println("touch_obj: " + touch_obj);
		if(but_GAME_DEBUG_PRINT.isChecked()) {
			but_GAME_DEBUG_PRINT.setChecked(false);
			Game.profile.set_settings_debug_text(!Game.profile.settings_debug_text());

			if(Game.profile.settings_debug_text())
				but_GAME_DEBUG_PRINT.setColor(Color.GREEN);
			else but_GAME_DEBUG_PRINT.setColor(Color.RED);
			
		} if(but_GAME_DEBUG_BOX2D.isChecked()) {
			but_GAME_DEBUG_BOX2D.setChecked(false);
			Game.profile.set_settings_debug_bounds(!Game.profile.settings_debug_bounds());
			
			if(Game.profile.settings_debug_bounds())
				but_GAME_DEBUG_BOX2D.setColor(Color.GREEN);
			else but_GAME_DEBUG_BOX2D.setColor(Color.RED);
		} if(but_GAME_GRIDNODE.isChecked()) {
			but_GAME_GRIDNODE.setChecked(false);
			Game.command_line.RE.debug_AStar.setDebug(!Game.command_line.RE.debug_AStar.getState());
			
			if(Game.command_line.RE.debug_AStar.getState()) {
				but_GAME_GRIDNODE.setColor(Color.GREEN);
				
			} else but_GAME_GRIDNODE.setColor(Color.SKY);
		}
		
		if(but_PARAMS_LAYER.isChecked()) {
			but_PARAMS_LAYER.setChecked(false);
			
			panel_layer_open = !panel_layer_open;
			if(panel_layer_open) {
				but_PARAMS_LAYER.setColor(Color.GREEN);
				
				panel_layer.setTouchable(Touchable.enabled);
				panel_layer.setVisible(true);
				
				panel_param.setVisible(false);
				panel_param.setTouchable(Touchable.disabled);
			} else {
				but_PARAMS_LAYER.setColor(Color.RED);
				
				panel_layer.setTouchable(Touchable.disabled);
				panel_layer.setVisible(false);
				
				panel_param.setVisible(true);
				panel_param.setTouchable(Touchable.enabled);
			}
		}
		
	}
	
	private boolean initGameButton(SpriteBatch batch) {
		if(but_GAME_play.isChecked()) {
			but_GAME_play.setChecked(false);
			core.setGameState(GameState.CONTINUE);
			
			return true;
		} else if(but_GAME_pause.isChecked()) {
			but_GAME_pause.setChecked(false);
			core.setGameState(GameState.PAUSE);
			
			return true;
		} else if(but_GAME_stop.isChecked()) {
			but_GAME_stop.setChecked(false);
			core.reloadLevel();
			
			return true;
		}
		
		return false;
	}
	
	@Override
	public void getActorPressedStage(Actor actor) {
		
	}
	
	private void checkParent(Actor actor) {
		if(actor.getParent() != null && actor.getParent() != group_stage) {
			
			if(actor.getParent() == panel_inspector && this.checkBounds(panel_inspector))
				this.initButton(PANEL_INSPECTOR);
			else if(actor instanceof PanelNode) {
				PanelNode node = (PanelNode) actor;
				
				System.out.println("	GuiEditor.checkParent()# null action");
			} else if(actor.getParent() instanceof PanelNode) {
				
				System.out.println("	GuiEditor.checkParent()# null action");
			} else {
				System.out.println("	GuiEditor.checkParent()# checkParent()");
				this.checkParent(actor.getParent());
			}
		}
	}
	
	public boolean checkBounds(Actor check) {
		if(check != null && 
				check.getX(Alignment.LEFT.get()) < x_press &&
				x_press < check.getX(Alignment.RIGHT.get()) &&
				check.getY(Alignment.BOTTOM.get()) < y_press &&
				y_press < check.getY(Alignment.TOP.get())) {
			 
					return true;
		} else return false;
	}
	
	private void initButton(int par1) {
		switch(par1) {
			case PANEL_INSPECTOR: {
				if(touch_obj.getParent() == tree_ENTITY) {
					if(prew_touch_obj_INSPECTOR != null)
						prew_touch_obj_INSPECTOR.setColor(Color.WHITE);
					
					touch_obj_INSPECTOR = touch_obj;
					core.pickEntity((Label)touch_obj);
					touch_obj_INSPECTOR.setColor(Color.YELLOW);
					prew_touch_obj_INSPECTOR = touch_obj_INSPECTOR;
				}
			} case PANEL_PROPERTIES: {
				
			} default: {
				
			};
		}
	}
	
	/** Get the choosed Object at world. */
	public Object getChoosedObject(float x, float y) {
		core.getWorld().traceQueryAABB(core, core.getCamera().position.x + x * Game.SCALE_GUI_VALUE,
				core.getCamera().position.y + y * Game.SCALE_GUI_VALUE,
				core.getCamera().position.x + x * Game.SCALE_GUI_VALUE, core.getCamera().position.y + y * Game.SCALE_GUI_VALUE);
		return null;
	}

	@Override
	public boolean touchDown(float x, float y) {
		x_press = x;
		y_press = y;
		
		if(core.getCamera() != null) {
			camera_prew_pos_x = core.getCamera().position.x;
			camera_prew_pos_y = core.getCamera().position.y;
		}
		
		touch_obj = group_stage.hit(x, y, false);
		
		if(touch_obj == null && game.guiManager.guiList.peek().equals(this)) {
			canDrag = true;
			this.initButton(-1);
			
			if(core.getEditorMode() == EditorMode.CHOOSE_MODE.get())
				this.getChoosedObject(x_press - Gdx.graphics.getWidth() / 2f, y_press - Gdx.graphics.getHeight() / 2f);
		} else {
			canDrag = false;
			
			if(touch_obj != null)
				this.checkParent(touch_obj);
			
		}
		
		return true;
	}

	@Override
	public boolean touchDragged(float offsetX, float offsetY) {
		if(core.getCamera() != null && canDrag && core.getEditorMode() == EditorMode.VIEW_MODE.get()) {
			core.moveCamera(camera_prew_pos_x + offsetX * Game.SCALE_GUI_VALUE, camera_prew_pos_y + offsetY * Game.SCALE_GUI_VALUE);
		}
		
		return true;
	}

	@Override
	public boolean touchUp() {
		return true;
	}

	@Override
	public void scrolled(int scroll) {
		if(core.getCamera() != null && group_stage.hit(InputHandler.GUI_mouse_x, InputHandler.GUI_mouse_y, true) == null
				&& getGuiManager().guiList.peek() == this) {
			if(Gdx.input.isKeyPressed(Keys.CONTROL_LEFT))
				scroll *= 10;
			System.out.println("	GuiEditor.scrolled()# by: " + scroll);
			core.zoomCamera(scroll);
		}
	}
}
