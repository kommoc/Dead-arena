package net.kommocgame.src.world.level;

import com.badlogic.gdx.ai.pfa.indexed.IndexedAStarPathFinder;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.Json.Serializable;
import com.badlogic.gdx.utils.JsonValue;
import com.badlogic.gdx.utils.OrderedMap;

import net.kommocgame.src.debug.CommandLine;
import net.kommocgame.src.editor.EditorCore;
import net.kommocgame.src.entity.EntityBase;
import net.kommocgame.src.entity.AI.GridNodesAStar;
import net.kommocgame.src.entity.AI.Node;
import net.kommocgame.src.render.RenderEngine;
import net.kommocgame.src.render.RenderLevel;
import net.kommocgame.src.trigger.TriggerBase;
import net.kommocgame.src.world.World;

public class LevelBase implements Serializable {
	
	public transient World world;
	public transient RenderLevel terrain;
	public transient ILevel ilevel;
	
	private Array<LevelLayer> layers_list = new Array<LevelLayer>();
	private transient LevelLayer layer_other;
	private int main_layer_id = 0;
	
	@Deprecated
	/** Main list of entity's spawn. */
	public transient OrderedMap<EntityBase, Vector2> 		spawn_list_entity = new OrderedMap<EntityBase, Vector2>();;
	@Deprecated
	/** Main list of trigger's spawn. */
	public transient OrderedMap<TriggerBase, Vector2> 		spawn_list_trigger = new OrderedMap<TriggerBase, Vector2>();
	
	/** Main list of terrainObj spawn. */
	public transient OrderedMap<TerrainObject, Vector2> 	spawn_list_terrainObj = new OrderedMap<TerrainObject, Vector2>();
	
	public transient int grid_width;
	public transient int grid_height;
	public transient float grid_scale;
	
	public String name;
	
	/** 'Serialized JSON file. */
	public final String _SUFF = ".json";
	
	private GridNodesAStar node_grid;
	private Array<Boolean> j_node_grid;
	
	/** Flag for right set TerrainObjects to layers. */
	private boolean IS_OBJECTS_LOADING = false;
	
	public LevelBase(World world) {
		this.world = world;
	}
	
	/** This instance for json. */
	public LevelBase() { }
	
	/** Main method which in creates obj into world. */
	public void createLevel() {}
	
	public void createAStarGrid(int width, int height, float scale) {
		node_grid = new GridNodesAStar(width, height, scale);
		RenderEngine.debug_AStar.setGrid(node_grid);
		
		world.IASPF = new IndexedAStarPathFinder<Node>(node_grid.getGraphPath(), true);
		//world.pathFinder_queue = new PathFinderQueue<Node>(world.IASPF);
		
		//world.message_handler.addListener(world.pathFinder_queue, 0);
		world.message_handler.addListener(CommandLine.test_astar_pthf, 1);
		
		if(j_node_grid != null && j_node_grid.size > 0) {
			for(int i = 0; i < getGridNodes().getGraphPath().getCount(); i++) {
				if(!j_node_grid.get(i)) {
					getGridNodes().getNodeByIndex(i).disableNode(getGridNodes());
				}
			}
		}
		
		j_node_grid = null;
	}
	
	public void addLevelLayer(String name, Color color) {
		LevelLayer layer = new LevelLayer(name/*, -1*/, this);
		layer.setColor(color);
		this.layers_list.add(layer);
		//layer.setID(layers_list.indexOf(layer, false));
	}
	
	public void addLevelLayer(String name, Color color, int index) {
		LevelLayer layer = new LevelLayer(name/*, -1*/, this);
		layer.setColor(color);
		this.layers_list.insert(index, layer);
		//layer.setID(layers_list.indexOf(layer, false));
	}
	
	public void spawnTerrainObj(TerrainObject obj, float x, float y) {
		this.spawnTerrainObj(obj, new Vector2(x, y));
	}
	
	public void spawnTerrainObj(TerrainObject obj, Vector2 vec) {
		System.out.println("	Level aTO [" + obj + "] - is " + !terrain.objects.getChildren().contains(obj.getImage(), false));
		System.out.println("		[" + obj.getImage() + "]");
		
		checkMainLayer();
		if(!terrain.objects.getChildren().contains(obj.getImage(), false)) {
			spawn_list_terrainObj.put(obj, vec);
			terrain.addObject(obj.getImage());
			
			getMainLayer().getObjectsList().add(obj);
			terrain.updateLayers();
			//obj.setLayerIndex(getMainLayer().getID());
		} else System.out.println("	Level - that actor is added to list yet.");
	}
	
	public void loadTerrainObj(TerrainObject obj, Vector2 vec) {
		System.out.println("	Level aTO [" + obj + "] - is " + !terrain.objects.getChildren().contains(obj.getImage(), false));
		System.out.println("		[" + obj.getImage() + "]");
		
		if(!terrain.objects.getChildren().contains(obj.getImage(), false)) {
			spawn_list_terrainObj.put(obj, vec);
			terrain.addObject(obj.getImage());
			
			//System.out.println("");
			//obj.setLayerIndex(getMainLayer().getID());
		} else System.out.println("	Level - that actor is added to list yet.");
	}
	
	public void removeTerrainObj(TerrainObject obj) {
		if(layers_list.size == 0) {
			System.out.println("	Level rTO [" + obj.getImage() + "] - is " + "GHOST DELETE!");
			return;
		}
		
		System.out.println("	Level rTO [" + obj.getImage() + "] - is " + terrain.objects.getChildren().contains(obj.getImage(), false));
		
		if(terrain.objects.getChildren().contains(obj.getImage(), false)) {
			spawn_list_terrainObj.remove(obj);
			terrain.removeObject(obj.getImage());
			
			if(obj.getLayerIndex() != -1)
				layers_list.get(obj.getLayerIndex()).getObjectsList().removeValue(obj, false);
		}
	}
	
	/** Remove layer from world.*/
	public void removeLevelLayer(LevelLayer layer) {
		removeLevelLayer(layer.getID());
	}
	
	/** Remove layer from world.*/
	public void removeLevelLayer(int id) {
		for(TerrainObject object : layers_list.get(id).getObjectsList())
			this.removeTerrainObj(object);
		
		this.layers_list.removeIndex(id);
		
		if(layers_list.size <= main_layer_id) {
			main_layer_id = layers_list.size - 1;
			setCurrentLayer(main_layer_id);
		}
	}
	
	/** Remove layer from world. ONLY for editor use. */
	public void removeLevelLayer(LevelLayer layer, EditorCore core) {
		removeLevelLayer(layer.getID(), core);
	}
	
	/** Remove layer from world. ONLY for editor use. */
	public void removeLevelLayer(int id, EditorCore core) {
		for(TerrainObject object : layers_list.get(id).getObjectsList())
			core.removeObject(core.getNodeByObject(object));
		
		this.layers_list.removeIndex(id);
		
		if(layers_list.size == 0)
			return;
		
		if(layers_list.size <= main_layer_id) {
			main_layer_id = layers_list.size - 1;
			setCurrentLayer(main_layer_id);
		}
	}
	
	/** Lift up layer priority. */
	public void setLayerUp(LevelLayer layer) {
		int id = layer.getID();
		
		if(id != 0) {
			layers_list.swap(id, id - 1);
			terrain.updateLayers();
		}
	}
	
	/** Lift down layer priority. */
	public void setLayerDown(LevelLayer layer) {
		int id = layer.getID();
		
		if(id != layers_list.size) {
			layers_list.swap(id, id + 1);
			terrain.updateLayers();
		}
	}
	
	public void setTerrainObjectUp(TerrainObject tobject) {
		LevelLayer layer = getLayersList().get(tobject.getLayerIndex());
		int index = layer.getObjectsList().indexOf(tobject, false);
		
		layer.getObjectsList().swap(index, index + 1);
		terrain.setUpTerrainObject(tobject.getImage());
	}
	
	public void setTerrainObjectDown(TerrainObject tobject) {
		LevelLayer layer = getLayersList().get(tobject.getLayerIndex());
		int index = layer.getObjectsList().indexOf(tobject, false);
		
		layer.getObjectsList().swap(index, index - 1);
		terrain.setDownTerrainObject(tobject.getImage());
	}
	
	public void setTerrainObjectToMainLayer(TerrainObject tobject) {
		LevelLayer layer = getLayersList().get(tobject.getLayerIndex());
		if(layer == getMainLayer())
			return;
		
		int index = layer.getObjectsList().indexOf(tobject, false);
		
		layer.getObjectsList().removeIndex(index);
		getMainLayer().getObjectsList().add(tobject);
		terrain.updateLayers();
	}
	
	/** Set's the render of this level after setLevel(). */
	public void setLevelRender() {
		if(terrain != null) {
			System.out.println("	setLevelRender() - level renderer is created yet.");
			return;
		}
		
		terrain = new RenderLevel(world.getWorldBatch(), this);
		
		//FIXME to delete
		for(int i = 0; i < spawn_list_terrainObj.size; i++) {
			terrain.addObject(spawn_list_terrainObj.keys().toArray().get(i).getImage());
		}
	}
	
	public void setWorld(World world) {
		this.world = world;
	}
	
	public void setILevel(ILevel ilevel) {
		this.ilevel = ilevel;
	}
	
	public void setCurrentLayer(int id) {
		for(LevelLayer layer : layers_list) {
			layer.setAsCurrent(layer.getID() != id ? false : true);
		}
		
		main_layer_id = id;
	}
	
	public void setCurrentLayer(LevelLayer current_layer) {
		for(LevelLayer layer : layers_list) {
			layer.setAsCurrent(layer.getID() != current_layer.getID() ? false : true);
		}
		
		main_layer_id = current_layer.getID();
	}
	
	public void setObjectsDoneLoading(boolean isLoading) {
		IS_OBJECTS_LOADING = isLoading;
	}
	
	public synchronized GridNodesAStar getGridNodes() {
		return node_grid;
	}
	
	public String getLevelName() {
		if(name.endsWith(_SUFF))
			return name.substring(0, name.indexOf(_SUFF));
		
		return name;
	}
	
	/** Return the current main layer. */
	public LevelLayer getMainLayer() {
		return layers_list.get(main_layer_id);
	}
	
	public Array<LevelLayer> getLayersList() {
		return layers_list;
	}
	
	/** Return the level state meaning TerrainObjects loading.*/
	public boolean isObjectsLoading() {
		return IS_OBJECTS_LOADING;
	}
	
	private void checkMainLayer() {
		if(layers_list.size == 0) {
			System.out.println("LevelBase.checkMainLayer() ### Created new layer! ");
			layer_other = new LevelLayer("Main_other"/*, 0*/, Color.WHITE, true, this);
			layers_list.add(layer_other);
			main_layer_id = 0;
		}
	}
	
	public LevelBase initOnConstruct() {
		if(ilevel != null)
			ilevel.onConstruct(this);
		return this;
	}
	
	public void destroy() {
		System.out.println("	###	LEVEL_DESTROY_BEGIN	###");
		
		world.resetParticleEmitterList();
		world.message_handler.removeListener(CommandLine.test_astar_pthf, 1);
		//world.message_handler.removeListener(world.pathFinder_queue, 0);
		
		world.level = null;
		node_grid = null;
		RenderEngine.debug_AStar.setGrid(node_grid);
		//world.pathFinder_queue = null; 
		world.IASPF = null;
		
		world.physics.getBox2d().clearForces();
		for(int i = 0; i < world.getEngine().getEntities().size(); i++) {
			if(world.getEngine().getEntities().get(i) instanceof EntityBase) {
				((EntityBase)world.getEngine().getEntities().get(i)).deleteObject();
			} else if(world.getEngine().getEntities().get(i) instanceof TriggerBase) {
				((TriggerBase)world.getEngine().getEntities().get(i)).deleteObject();
			}
		}
		
		world.getEngine().removeAllEntities();
		System.out.println("	###	LEVEL_DESTROY_END	###");
	}

	@Override
	public void write(Json json) {
		json.writeValue("level_name", name);
		json.writeValue("grid_width", getGridNodes().getWidth());
		json.writeValue("grid_height", getGridNodes().getHeight());
		json.writeValue("grid_scale", getGridNodes().getScale());
		
		j_node_grid = new Array<Boolean>();
		
		if(getGridNodes() == null)
			System.out.println("ERROR LevelBase.write() ### Saving file failed! Grid is not defined!");
		
		for(int i = 0; i < getGridNodes().getGraphPath().getCount(); i++) {
			j_node_grid.add(getGridNodes().getNodeByIndex(i).getState());
		}
		
		json.writeValue("grid_nodes", j_node_grid, Array.class, Boolean.class);
		json.writeValue("level_suff", _SUFF);
		json.writeValue("main_layer_id", main_layer_id);
		json.writeValue("layers_list", layers_list);
	}

	@Override
	public void read(Json json, JsonValue jsonData) {
		this.name = jsonData.getString("level_name");
		grid_width = jsonData.getInt("grid_width");
		grid_height = jsonData.getInt("grid_height");
		
		if(jsonData.get("grid_scale") != null)
			grid_scale = jsonData.getFloat("grid_scale");
		else grid_scale = 1f;
		
		j_node_grid = new Array<Boolean>();
		
		if(jsonData.has("main_layer_id"))
			main_layer_id = jsonData.getInt("main_layer_id");
		else main_layer_id = 0;
		
		if(jsonData.get("layers_list") != null) {
			layers_list = json.readValue("layers_list", Array.class, jsonData);
			
			for(LevelLayer layer : layers_list) {
				layer.setLevelBase(this);
				layer.getObjectsList().setSize(layer.getSize());
				System.out.println("getSize(): "+ layer.getSize());
			}
		}
		this.setCurrentLayer(main_layer_id);
		
		for(boolean node_state : jsonData.get("grid_nodes").asBooleanArray())
			j_node_grid.add(node_state);
		
		//createAStarGrid(grid_width, grid_height);			AFTER SETTING WORLD
	}
}
