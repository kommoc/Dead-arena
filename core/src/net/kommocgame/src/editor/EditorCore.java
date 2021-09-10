package net.kommocgame.src.editor;

import java.io.FileWriter;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.reflect.ClassReflection;
import com.badlogic.gdx.utils.reflect.ReflectionException;

import net.kommocgame.src.Game;
import net.kommocgame.src.GameState;
import net.kommocgame.src.Loader;
import net.kommocgame.src.editor.EditorLayersTable.LayersTable.NodeLayer;
import net.kommocgame.src.editor.EditorLayersTable.ObjectsTable.NodeObject;
import net.kommocgame.src.editor.nodes.DHObjects;
import net.kommocgame.src.editor.nodes.ELevelObj;
import net.kommocgame.src.editor.objects.EOEntity;
import net.kommocgame.src.editor.objects.EOTerrain;
import net.kommocgame.src.editor.objects.EOTrigger;
import net.kommocgame.src.editor.objects.EObject;
import net.kommocgame.src.entity.EntityBase;
import net.kommocgame.src.entity.EntityLiving;
import net.kommocgame.src.entity.props.EntityProp;
import net.kommocgame.src.trigger.TriggerBase;
import net.kommocgame.src.world.World;
import net.kommocgame.src.world.level.ILevel;
import net.kommocgame.src.world.level.LevelBase;
import net.kommocgame.src.world.level.LevelEditor;
import net.kommocgame.src.world.level.LevelLayer;
import net.kommocgame.src.world.level.TerrainObject;
import net.kommocgame.src.world.physics.ITraceBody;

public class EditorCore implements ITraceBody {
	
	/** Current editing level. */
	private LevelEditor level;
	
	/** Free camera in world space. */
	private OrthographicCamera camera;
	private float CAMERA_DEFAULT_ZOOM;
	private float CAMERA_CURRENT_ZOOM;
	private int cam_max_amount = 100;
	private int cam_min_amount = -2;
	private int cam_amount = 0;
	
	/** Prototype world. */
	private World world;
	
	/** Current mode. */
	private int mode = EditorMode.VIEW_MODE.get();
	
	/** Main list of world objects. */
	private Array<EObject> list_objects = new Array<EObject>();
	
	/** Choosen entity in editor. */
	@Deprecated
	private EntityLiving choosen_entity;
	
	private GuiEditor gui;
	
	@Deprecated
	private Array<Class<? extends EntityLiving>> list_entity = new Array<Class<? extends EntityLiving>>();
	
	private EAHandler event_handler;
	
	public EditorCore(GuiEditor gui) {
		Game.CORE.setRedactorActivity(true);
		event_handler = new EAHandler(gui);
		
		if(Game.DH_OBJECTS == null)
			Game.DH_OBJECTS = new DHObjects();
		
		this.gui = gui;
		getGame().pause();
	}
	
	/** Set editor mode as VIEV. */
	public void setVievMode() {
		mode = EditorMode.VIEW_MODE.get();
	}
	
	/** Set editor mode as EDIT. */
	public void setEditMode() {
		mode = EditorMode.EDIT_MODE.get();
	}
	
	/** Set editor mode as EDIT. */
	public void setChooseMode() {
		mode = EditorMode.CHOOSE_MODE.get();
	}
	
	public void setGameState(GameState state) {
		if(state == GameState.CONTINUE) {
			gui.game.setGameState(GameState.CONTINUE);
		} else if(state == GameState.PAUSE) {
			gui.game.setGameState(GameState.PAUSE);
		}
	}
	
	/** Set object by node from panel_levelObjects. */
	public void setNodeObject(ELevelObj node) {
		System.out.println("	EditorCore. Size_list_nodes: " + list_objects.size);
		System.out.println("	EditorCore. Size_list_levelObj: " + gui.panel_levelObjects.getNodeTable().getCells().size);
		if(node == null)
			return;
		
		for(int i = 0; i < gui.panel_levelObjects.getNodeTable().getCells().size; i++) {
			if(gui.panel_levelObjects.getNodeTable().getCells().get(i).getActor() != null)
				gui.panel_levelObjects.getNodeTable().getCells().get(i).getActor().setColor(Color.WHITE);
		}
		
		node.setColor(Color.CYAN);
		getEAHandler().setObject(node.getNode());
	}
	
	/** Set object by right click. */
	public void setNodeObject(EObject eobject) {
		System.out.println("	EditorCore. Size_list_nodes: " + list_objects.size);
		System.out.println("	EditorCore. Size_list_levelObj: " + gui.panel_levelObjects.getNodeTable().getCells().size);
		
		for(int i = 0; i < gui.panel_levelObjects.getNodeTable().getCells().size; i++) {
			ELevelObj level_obj = (ELevelObj) gui.panel_levelObjects.getNodeTable().getCells().get(i).getActor();
			if(level_obj != null)
				if(level_obj.getNode() != eobject)
					level_obj.setColor(Color.WHITE);
				else  {
					System.out.println("hui");
					level_obj.setColor(Color.CYAN);
				}
		}
		
		if(eobject.getInstance() instanceof TerrainObject)
			for(LevelLayer layer : getLevel().getLayersList()) {
				
				if(layer.getObjectsList().contains((TerrainObject) eobject.getInstance(), false)) {
					gui.panel_layer.layers.setButton((NodeLayer) gui.panel_layer.layers.table_layers.getChildren().get(layer.getID()));
					
					for(int id = 0; id < gui.panel_layer.objects.table_objects.getChildren().size; id++) {
						NodeObject node = (NodeObject) gui.panel_layer.objects.table_objects.getChildren().get(id);
						
						if(node.getTerrainObject() == eobject.getInstance()) {
							gui.panel_layer.objects.setButton(node);
							break;
						}
					}
					
					break;
				}
			}
	}
	
	/** Creates new level. 
	 * @throws Exception */
	public void newLevel(String name, World world, OrthographicCamera cam, int grid_width, int grid_height, float grid_scale) {
		if(level == null) {
			this.world = world;
			this.camera = cam;
			level = new LevelEditor(world, this).setLevelName(name);
			level.createAStarGrid(grid_width, grid_height, grid_scale);
			world.setLevel(level);
			
			CAMERA_DEFAULT_ZOOM = camera.zoom;
			CAMERA_CURRENT_ZOOM = CAMERA_DEFAULT_ZOOM;
			
			gui.panel_layer.setLevel(level);
		}
	}
	
	public void reloadLevel() {
		LevelBase preLevel = this.level;
		gui.game.pause();
		
		LevelEditor newLevel = new LevelEditor(world, this);
		newLevel.entity_list = level.entity_list;
		newLevel.entity_map = level.entity_map;
		newLevel.prop_list = level.prop_list;
		newLevel.prop_map = level.prop_map;
		newLevel.trigger_list = level.trigger_list;
		newLevel.trigger_map = level.trigger_map;
		newLevel.wall_list = level.wall_list;
		newLevel.wall_map = level.wall_map;
		
		newLevel.spawn_list_entity = this.level.spawn_list_entity;
		newLevel.spawn_list_trigger = this.level.spawn_list_trigger;
		newLevel.spawn_list_terrainObj = this.level.spawn_list_terrainObj;
		
		newLevel.createAStarGrid(level.getGridNodes().getWidth(), level.getGridNodes().getHeight(), level.getGridNodes().getScale());
		
		level.destroy();
		level = newLevel;
		
		for(int i = 0; i < newLevel.spawn_list_entity.size; i++) {
			try {
				//render = ClassReflection.getConstructor(GameManager.instance.getEntityInstance(par1), par1.getClass(), SpriteBatch.class).newInstance(par1, par1.batch);
				EntityBase entityToSpawn = (EntityBase) ClassReflection.getConstructor(newLevel.spawn_list_entity.orderedKeys().get(i).getClass(), World.class, 
						SpriteBatch.class, float.class, float.class).newInstance(world, world.getWorldBatch(), 
								newLevel.spawn_list_entity.get(newLevel.spawn_list_entity.orderedKeys().get(i)).x,
								newLevel.spawn_list_entity.get(newLevel.spawn_list_entity.orderedKeys().get(i)).y);
				world.addEntityIntoWorld(entityToSpawn);
			} catch (ReflectionException e1) {
				e1.printStackTrace();
			}
		} for(int i = 0; i < newLevel.spawn_list_trigger.size; i++) {
			world.addTriggerIntoWorld(newLevel.spawn_list_trigger.orderedKeys().get(i));
		}
		
		newLevel.setLevelRender();
	}
	
	/** Load level from file. */
	@Deprecated
	public void loadLevel(FileHandle fileHandle) {
		/*
		Json jsonReader = new Json();
		
		try {
			DataInput input_level = new DataInput(fileHandle.read());
			String json_file = StreamUtils.copyStreamToString(input_level);
			
			String json_level = json_file.substring(json_file.indexOf("#LB") + 3, json_file.indexOf("#LE"));
			String json_objects = json_file.substring(json_file.indexOf("#OB") + 3, json_file.indexOf("#OE"));
			input_level.close();
			
			System.out.println("	EditorCore.loadLevel() ### INPUT: " + json_file);
			//System.out.println("	EditorCore.loadLevel() ### LEVEL: " + json_level);
			//System.out.println("	EditorCore.loadLevel() ### OBJECTS: " + json_objects);
			
			LevelEditor levelBase = (LevelEditor) jsonReader.fromJson(LevelEditor.class, json_level);
			Array array = jsonReader.fromJson(Array.class, json_objects);
			
			this.clearListObject();
			
			if(world != null) {
				this.clearListObject();
				levelBase.setWorld(getWorld());
				levelBase.setLevelRender();
				world.setLevel(levelBase);
				world.getLevel().createAStarGrid(world.getLevel().grid_width, world.getLevel().grid_height);
				this.level = (LevelEditor) world.getLevel();
				
				for(int i = 0; i < array.size; i++) {
					System.out.println("\n	EditorCore.loadLevel() ### array.get(i): " + array.get(i));
					EObject object = (EObject)array.get(i);
					
					object.setWorld(getWorld());
					object.createInstance();
					
					for(int index = 0; index < object.getInstance().getAdditionalData().getParameters().size; index++) {
						DataObject data = object.getInstance().getAdditionalData().getParameters().get(index);
						System.out.println("	EditorCore.loadLevel() ### value_name: " + data.getName() + " is contains: "
								+ object.getAdditionalData().getMap().containsKey(data.getName()));
						
						if(object.getAdditionalData().getMap().containsKey(data.getName())) {
							data.setParameter(object.getAdditionalData().getMap().get(data.getName()));
						}
					}
					
					object.setAdditionalData(object.getInstance().getAdditionalData());
					this.addObject(object);
				}
				
				world.getLevel().name = fileHandle.file().getName();
				System.out.println("\n	EditorCore.loadLevel() ### level name: " + world.getLevel().getLevelName());
				
			}
			
		} catch(Exception e) {
			e.printStackTrace();
		}*/
		
		world = getGame().world;
		
		if(camera == null) {
			camera = getGame().mainCamera;
			CAMERA_DEFAULT_ZOOM = camera.zoom;
			CAMERA_CURRENT_ZOOM = CAMERA_DEFAULT_ZOOM;
		}
		
		ILevel ilevel = new ILevel() {
			@Override
			public void onConstruct(LevelBase levelBase) {
				if(world != null) {
					clearListObject();
					level = (LevelEditor) world.getLevel();
					
					for(TerrainObject terrainObject : levelBase.spawn_list_terrainObj.keys()) {
						addObject(terrainObject);
					}
					
					for(int i = 0; i < world.getEngine().getEntities().size(); i++) {
						if(world.getEngine().getEntities().get(i) instanceof EntityBase) {
							EntityBase entity = (EntityBase) world.getEngine().getEntities().get(i);
							addObject(entity);
						} else if(world.getEngine().getEntities().get(i) instanceof TriggerBase) {
							TriggerBase trigger = (TriggerBase) world.getEngine().getEntities().get(i);
							addObject(trigger);
						}
					}
					
					System.out.println("EditorCore.loadLevel() ### Load level has been done.");
					System.out.println("EditorCore.loadLevel() ### gridNodes: " + getLevel().getGridNodes());
					
					gui.panel_layer.setLevel(level);
				}
			}
		};
		
		getGame().world.getLevelManager().loadLevel(fileHandle, ilevel);
	}
	
	/** Save current level to file. */
	public void saveLevel() {
		try {
			
			for(int i = 0; i < list_objects.size; i++) {
				if(list_objects.get(i).getInstance() instanceof TerrainObject) {
					TerrainObject tobject = (TerrainObject) list_objects.get(i).getInstance();
					
					tobject.setLayerIndex(tobject.getLayerIndex());
					tobject.setLayerPositionID(tobject.getLayerPositionID());
				}
			}
			
			System.out.println("	### SAVE_LEVEL ###	");
			Json jsonWriter = new Json();
			
			String info_level = jsonWriter.toJson(getLevel(), LevelBase.class);
			String info_objects = jsonWriter.toJson(getListNodes(), Array.class);
			
			System.out.println("	### JSON ###	" + info_level);
			System.out.println("	### JSON ###	" + info_objects);
			
			for(int i = 0; i < Loader.getGameFile("android/assets/SJF/").list().length; i++) {
				System.out.println(Loader.getGameFile("android/assets/SJF/").list()[i]);
			}
			
			FileHandle file_level = Loader.getGameFile("android/assets/SJF/" + getWorld().getLevel().getLevelName() + level._SUFF);
			
			if(file_level.file().createNewFile())
				System.out.println("	EditorCore.saveLevel() ### save level. Create new File.");
			else System.out.println("	EditorCore.saveLevel() ### save level. Rewrite the file such created yet. \n\t\t\tFILE[" + 
					file_level.name() + "]");
			
			FileWriter out = new FileWriter(file_level.file());
			out.flush();
			out.write("#LB");			// LevelBegin
			out.write(info_level);
			out.write("#LE");			// LevelEnd
			out.write("#OB");			// ObjectsBegin
			out.write(info_objects);
			out.write("#OE");			// ObjectsEnd
			out.close();
			
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	/** Set's the camera position. */
	public void moveCamera(float x, float y) {
		if(camera == null || mode != EditorMode.VIEW_MODE.get())
			return;
		
		camera.position.x = x;
		camera.position.y = y;
	}
	
	/** Set's the camera zoom. */
	public void zoomCamera(int amount) {
		if(camera == null || mode != EditorMode.VIEW_MODE.get())
			return;
		
		cam_amount += amount;
		
		if(cam_amount <= cam_min_amount)
			cam_amount = cam_min_amount;
		else if(cam_amount >= cam_max_amount)
			cam_amount = cam_max_amount;
		
		CAMERA_CURRENT_ZOOM = CAMERA_DEFAULT_ZOOM + cam_amount / 100f;
		camera.zoom = CAMERA_CURRENT_ZOOM;
		
		Game.SCALE_GUI_VALUE = CAMERA_CURRENT_ZOOM;
		Game.SCALE_WORLD_VALUE = CAMERA_CURRENT_ZOOM * Game.ratio();
		
		System.out.println("cam_amount: " + cam_amount);
	}
	
	/** Dispose editor. */
	public void destroyCore() {
		System.out.println("		DESTROY_CORE");
		
		if(camera != null) {
			System.out.println("	Camera_zoom_default: " + CAMERA_DEFAULT_ZOOM);
			
			camera.zoom = CAMERA_DEFAULT_ZOOM;
			Game.SCALE_GUI_VALUE = CAMERA_DEFAULT_ZOOM;
			Game.SCALE_WORLD_VALUE = CAMERA_DEFAULT_ZOOM * Game.ratio();
		}
		
		this.camera = null;
		this.choosen_entity = null;
		this.gui = null;
		world.deleteLevel();
		level = null;
		this.world = null;
		Game.CORE.setRedactorActivity(false);
	}
	
	/** Add entity to world. */
	@Deprecated
	public void addEntity(EntityLiving entity) {
		if(this.level != null) {
			Label label_entity = new Label("Entity_" + level.entity_list.size, Game.NEUTRALIZER_UI);
			if(entity.compID.name != null && entity.compID.name.length() > 3 && !entity.compID.name.isEmpty()) {
				label_entity.setText(entity.compID.name);
			}
			
			world.addEntityIntoWorld(entity);
			this.level.entity_list.add(entity);
			this.level.entity_map.put(label_entity, entity);
			this.level.spawn_list_entity.put(entity, new Vector2(entity.getPosition()));
			
			//FIXME
			//gui.tree_ENTITY.getNodes().get(0).add(new Node(label_entity));
		}
	}
	
	/** Add entity to world. */
	@Deprecated
	public void addEntity(String name, String entityType) {
		if(this.level != null) {
			Label label_entity = new Label("Entity_" + level.entity_list.size, Game.NEUTRALIZER_UI);
			if(name != null && name.length() > 3 && !name.isEmpty()) {
				label_entity.setText(name);
			}
			
			for(int i = 0; i < list_entity.size; i++) {
				if(list_entity.get(i).getSimpleName().contains(entityType)) {	//FIXME may be trable конфликт имён
					
					try {
						this.level.entity_list.add((EntityLiving) ClassReflection.getConstructor(list_entity.get(i), World.class, SpriteBatch.class, Float.TYPE, Float.TYPE)
								.newInstance(getWorld(), getWorld().getWorldBatch(), camera.position.x, camera.position.y));
					} catch (ReflectionException e) {
						e.printStackTrace();
					}
					
					this.level.entity_map.put(label_entity, this.level.entity_list.peek());
					getWorld().addEntityIntoWorld(this.level.entity_list.peek());
					
					//FIXME
					//gui.tree_ENTITY.getNodes().get(0).add(new Node(label_entity));
					
					Vector2 vec = new Vector2(this.level.entity_list.peek().getPosition());
					this.level.spawn_list_entity.put(this.level.entity_list.peek(), vec);
				}
			}
		}
	}
	
	/** Add object to editor world. */
	public void addObject(Object obj) {
		if(obj instanceof EntityBase) {
			addEntity((EntityBase)obj);
		} else if(obj instanceof TerrainObject) {
			addTerrainObj((TerrainObject)obj);
		} else if(obj instanceof TriggerBase) {
			addTrigger((TriggerBase)obj);
		} else if(obj instanceof EObject) {
			list_objects.add((EObject)obj);
			
			if(((EObject) obj).getInstance() != null) {
				gui.panel_levelObjects.getNodeTable().add(new ELevelObj(gui, (EObject) obj));
				gui.panel_levelObjects.getNodeTable().row();
			}
			
			System.out.println("		EditorCore.addObject(EObject) ### _list_nodes_size: " + list_objects.size);
			return;
		}
		
		if(obj != null) {
			gui.panel_levelObjects.getNodeTable().add(new ELevelObj(gui, list_objects.peek()));
			gui.panel_levelObjects.getNodeTable().row();
		}
		
		System.out.println("		EditorCore.addObject(Entity) ### " + list_objects.size);
	}
	
	/** Add entity instance to level. */
	public void addEntity(EntityBase entity) {
		list_objects.add(new EOEntity(getWorld(), entity));
	}
	
	/** Add terrainObject to level. */
	public void addTerrainObj(TerrainObject obj) {
		list_objects.add(new EOTerrain(getWorld(), obj));
	}
	
	/** Add trigger instance to level. */
	public void addTrigger(TriggerBase trigger) {
		list_objects.add(new EOTrigger(getWorld(), trigger));
	}
	
	/** Remove object from world. */
	public void removeObject(EObject object) {
		if(list_objects.contains(object, false)) {
			list_objects.removeValue(object, false);
			object.deleteInstance();
		}
	}
	
	/** Remove object from world. */
	public void clearListObject() {
		while(getListNodes().size != 0) {
			if(getListNodes().contains(getListNodes().get(0), false)) {
				getListNodes().get(0).deleteInstance();
				getListNodes().removeValue(getListNodes().get(0), false);
			}
		}
	}
	
	/** Update tick method. */
	public void update() {
		event_handler.update();
		
		if(gui.but_GAME_AI.isChecked()) {
			gui.but_GAME_AI.setChecked(false);
			
			//--------------------------------------------- TODO
			/*
			if(event_handler.getChoosedObject() != null && event_handler.getChoosedObject() instanceof EOTerrain) {
				EOTerrain object = (EOTerrain) event_handler.getChoosedObject();
				int index = getLevel().terrain.objects.getChildren().indexOf(object.getInstance().getImage(), true);
				
				getLevel().terrain.objects.swapActor(index, index < getLevel().terrain.objects.getChildren().size ? index + 1 : index);
			}*/
		}
	}
	
	/** Set's the current editable object(ENTITY). */
	@Deprecated
	public void pickEntity(Label label) {
		if(level.entity_map.containsKey(label)) {
			this.choosen_entity = level.entity_map.get(label);
			this.gui.panel_param.setEntityLiving(choosen_entity);
		}
	}
	
	/** Return picked entity in editor. */
	@Deprecated
	public EntityBase getPickedEntity() {
		return this.choosen_entity;
	}
	
	public int getEditorMode() {
		return this.mode;
	}
	
	/** Return the camera. */
	public OrthographicCamera getCamera() {
		return this.camera;
	}
	
	/** Return the worldObj. */
	public World getWorld() {
		return this.world;
	}
	
	public EAHandler getEAHandler() {
		return event_handler;
	}
	
	/** Return the levelEditor. */
	public LevelEditor getLevel() {
		return this.level;
	}
	
	/** Return the worldObj. */
	public Game getGame() {
		return this.gui.game;
	}
	
	public long getWorldTick() {
		return world.world_tick;
	}
	
	/** Return the EObject node. */
	public EObject getNodeByObject(Object obj) {
		for(int i = 0; i < list_objects.size; i++) {
			if(obj == list_objects.get(i).getInstance()) {
				return list_objects.get(i);
			}
		}
		
		return null;
	}
	
	public Array<EObject> getListNodes() {
		return list_objects;
	}
	
	/** Return the worldObj. */
	@Deprecated
	public Array<Class<? extends EntityLiving>> getListEntity() {
		return this.list_entity;
	}

	@Override
	@Deprecated
	public void traceEntity(EntityLiving entity) {
		//System.out.println("Trace Entity: " + entity);
		if(getEditorMode() != EditorMode.CHOOSE_MODE.get())
			return;
		
		this.choosen_entity = entity;
		gui.panel_param.setEntityLiving(choosen_entity);
	}
	
	@Deprecated
	@Override
	public void traceProp(EntityProp prop) {
		//System.out.println("Trace Prop: " + prop);
		if(getEditorMode() != EditorMode.CHOOSE_MODE.get())
			return;
	}
	
	@Deprecated
	@Override
	public void traceWall(Fixture fixture) {
		//System.out.println("Trace fixture: " + fixture);
		if(getEditorMode() != EditorMode.CHOOSE_MODE.get())
			return;
	}
	
	@Deprecated
	@Override
	public void traceTrigger(TriggerBase trigger) {
		//System.out.println("Trace trigger: " + trigger);
		if(getEditorMode() != EditorMode.CHOOSE_MODE.get())
			return;
	}
}
