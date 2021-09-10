package net.kommocgame.src.world.level;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.Json.Serializable;
import com.badlogic.gdx.utils.JsonValue;
import com.badlogic.gdx.utils.JsonWriter.OutputType;

import net.kommocgame.src.editor.objects.EOTerrain;

public class LevelLayer implements Serializable {
	
	private String name;
	//private int id_layer;
	private Color editor_color;
	private boolean isCurrent;
	public int size;
	
	/** Array contains ONLY editor terrain objects. Only for editor use! */
	private transient Array<EOTerrain> terrain_array = new Array<EOTerrain>();
	
	/** Array contains common instances of terrain object. Intended for common use. */
	private transient Array<TerrainObject> objects_array = new Array<TerrainObject>();
	
	private transient LevelBase level;
	
	/** For JSON serialization. */
	public LevelLayer() { }
	
	public LevelLayer(String name/*, int id*/, LevelBase level) {
		this(name/*, id*/, Color.WHITE, false, level);
	} 
	
	public LevelLayer(String name/*, int id*/, Color color, boolean isCurrent, LevelBase level) {
		this.level = level;
		setName(name);
		//setID(id);
		setColor(color);
		setAsCurrent(isCurrent);
	}
	
	public LevelLayer setLevelBase(LevelBase level) {
		this.level = level;
		return this;
	}
	
	public LevelLayer setName(String name) {
		this.name = name;
		return this;
	}
	
	public LevelLayer setColor(Color color) {
		this.editor_color = color;
		return this;
	}
	
	/*public LevelLayer setID(int id) {
		this.id_layer = id;
		return this;
	}*/
	
	public LevelLayer setAsCurrent(boolean isCurrent) {
		this.isCurrent = isCurrent;
		return this;
	}
	
	public Array<EOTerrain> getEditorObjectsList() {
		return terrain_array;
	}
	
	public Array<TerrainObject> getObjectsList() {
		return objects_array;
	}
	
	/** Return the layer name. */
	public String getName() {
		return name;
	}
	
	/** Return the index such this layer placed. */
	public int getID() {
		//return id_layer;
		if(level.getLayersList().contains(this, false))
			return level.getLayersList().indexOf(this, false);
		else return -1;
	}
	
	/** Return color that showing in editor. */
	public Color getEditorColor() {
		return editor_color;
	}
	
	/** Return that all {@link TerrainObject} will spawn in this layer.
	 * For editor. */
	public boolean isCurrent() {
		return isCurrent;
	}
	
	/** Only for loading level. */
	public int getSize() {
		return size;
	}

	@Override
	public void write(Json json) {
		json.writeValue("layer_name", getName());
		//json.writeValue("layer_id", getID());
		json.writeValue("layer_color", getEditorColor());
		json.writeValue("layer_isCurrent", isCurrent());
		json.writeValue("layer_object_size", getObjectsList().size);
	}

	@Override
	public void read(Json json, JsonValue jsonData) {
		setName(jsonData.getString("layer_name"));
		//setID(jsonData.getInt("layer_id"));
		//setColor(EColor.valueOf(jsonData.getString("layer_color")).getColor());
		editor_color = json.fromJson(Color.class, jsonData.get("layer_color").toJson(OutputType.json));
		setAsCurrent(jsonData.getBoolean("layer_isCurrent"));
		size = jsonData.getInt("layer_object_size");
	}
}
