package net.kommocgame.src.editor.objects;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.TransformDrawable;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonValue;
import com.badlogic.gdx.utils.reflect.ClassReflection;
import com.badlogic.gdx.utils.reflect.ReflectionException;
import com.github.czyzby.kiwi.util.gdx.scene2d.Alignment;

import net.kommocgame.src.Game;
import net.kommocgame.src.Loader;
import net.kommocgame.src.editor.EditorCore;
import net.kommocgame.src.entity.EntityBase;
import net.kommocgame.src.world.AdditionalData;
import net.kommocgame.src.world.World;
import net.kommocgame.src.world.level.TerrainObject;

public class EOTerrain extends EObject<TerrainObject> {
	
	private float param_scale_x = 1f;
	private float param_scale_y = 1f;
	
	private float default_width;
	private float default_height;
	
	/** This constructor for json. */
	public EOTerrain() {}
	
	public EOTerrain(World world, Class _class) {
		this(world, _class, null);
	}
	
	public EOTerrain(World world, TerrainObject inst) {
		this(world, inst.getClass(), inst);
	}
	
	public EOTerrain(World world, Class _class, TerrainObject inst) {
		super(world, _class, inst);
		
		if(instance != null) {
			image_object = new Image(instance.getImage().getDrawable());
			img_texture = instance.getTexture();
			texturepath = Game.assetManager.getAssetFileName(img_texture);
			param_position = new Vector2(instance.getPosition().cpy());
			
			default_width = instance.getImage().getWidth();
			default_height = instance.getImage().getHeight();
		}
	}

	@Override
	public void translateObject(float x, float y) {
		this.translateObject(x, y, getAlignment());
	}

	@Override
	public void translateObject(float x, float y, Alignment alignment) {
		if(instance != null) {
			instance.set(x, y, alignment);
			//getSpawnPosition().set(instance.getPosition());
			
			this.alignment_set = alignment;
		}
		
		//System.out.println("Alignment: " + getAlignment());
	}

	@Override
	public void rotateObject(float angle) {
		if(instance != null) {
			System.out.println("		EOTerrain.rotateObject() ### getRotationAlignment(): " + getRotationAlignment());
			instance.getImage().setOrigin(getRotationAlignment().get());
			instance.getImage().setRotation(angle);
		}
	}

	@Override
	public void scaleObject(float scaleX, float scaleY) {
		if(instance != null) {
			param_scale_x = scaleX;
			param_scale_y = scaleY;
			
			this.scale_x = param_scale_x;
			this.scale_y = param_scale_y;
			
			instance.getImage().setSize(default_width * param_scale_x, default_height * param_scale_y);
			instance.set(getPosition(), alignment_set);
			instance.getImage().setOrigin(getRotationAlignment().get());
			
		} else {
			System.out.println("	EOTerrain.scaleObject() ### scaled to defaults. ");
			this.scale_x = 1f;
			this.scale_y = 1f;
			
			param_scale_x = 1f;
			param_scale_y = 1f;
		}
	}

	@Override
	public void deleteObject(EditorCore core) {
		core.removeObject(this);
	}

	@Override
	public boolean isActive() {
		
		return false;
	}

	@Override
	public float getWidth() {
		if(instance == null)
			 return 1;
		
		return instance.getImage().getWidth();
	}

	@Override
	public float getHeight() {
		if(instance == null)
			 return 1;
		
		return instance.getImage().getHeight();
	}
	
	public float getDefaultWidth() {
		if(instance == null)
			 return 1;
		
		return default_width;
	}

	public float getDefaultHeight() {
		if(instance == null)
			 return 1;
		
		return default_height;
	}
	
	@Override
	public Vector2 getSpawnPosition() {
		if(instance == null)
			 return null;
		
		return instance.getPosition();
	}
	
	@Override
	public Vector2 getPosition() {
		if(instance == null)
			 return null;
		
		return instance.getPosition();
	}
	
	@Override
	public float getRotation() {
		if(instance == null)
			 return 0f;
		
		return instance.getImage().getRotation();
	}
	
	@Override
	public EOTerrain getCopy() {
		Json json = new Json();
		String str_json = json.toJson(this);
		
		EOTerrain copy = json.fromJson(EOTerrain.class, str_json);
		
		return copy;
	}
	
	@Override
	public Vector2 getDebugPosition() {
		if(instance == null)
			return null;
		
		return new Vector2(instance.getImage().getX(), instance.getImage().getY());
	}

	@Override
	public void createInstance() {
		if(instance == null) {
			instance = new TerrainObject(world.getLevel(), img_texture, param_position);
			instance.getImage().setSize(default_width, default_height);
			
			if(world.getLevel().isObjectsLoading()) {
				System.out.println("EOTerrain.createInstance() ### (1) loading instance.");
				world.getLevel().loadTerrainObj(instance, param_position);
			} else {
				System.out.println("EOTerrain.createInstance() ### (2) create instance after level loading objects.");
				world.getLevel().spawnTerrainObj(instance, param_position);		//FIXME CHANGED(16.07.19) ADD LAYERs
			}
			
			default_width = instance.getImage().getWidth();
			default_height = instance.getImage().getHeight();
			
			//object_data = instance.getAdditionalData();
			
			this.scaleObject(scale_x, scale_y);
			this.rotateObject(getSpawnRotation());
			this.translateObject(param_position.x, param_position.y, alignment_set);
		}
	}

	@Override
	public void deleteInstance() {
		if(instance != null) {
			instance.deleteObject();
			instance = null;
		}
	}

	@Override
	public void write(Json json) {
		/*
		System.out.println("	###########	");
		System.out.println("	EOTerrain.write() ### name: " + instance.getName());
		System.out.println("	EOTerrain.write() ### texturepath: " + texturepath);
		System.out.println("	EOTerrain.write() ### spawn_pos: " + getSpawnPosition());
		System.out.println("	EOTerrain.write() ### spawn_rot: " + getRotation());
		System.out.println("	EOTerrain.write() ### spawn_scale_x: " + getScaleX());
		System.out.println("	EOTerrain.write() ### spawn_scale_x: " + getScaleY());
		System.out.println("	&&&&&&&&&&&	");
		*/
		json.writeValue("texturepath", texturepath);
		json.writeValue("classpath", classpath);
		
		json.writeValue("img_width", this.default_width);
		json.writeValue("img_height", this.default_height);
		
		json.writeValue("spawn_position_x", getSpawnPosition().x);
		json.writeValue("spawn_position_y", getSpawnPosition().y);
		
		json.writeValue("spawn_rotation", getRotation());
		json.writeValue("spawn_scale_x", getScaleX());
		json.writeValue("spawn_scale_y", getScaleY());
		
		json.writeValue("alignment", alignment_set.get());
		json.writeValue("rot_alignment", rotation_alignment_set.get());
		
		if(instance.getAdditionalData() != null)
			json.writeValue("object_data", object_data);
	}

	@Override
	public void read(Json json, JsonValue jsonData) {
		texturepath = jsonData.getString("texturepath");
		classpath = jsonData.getString("classpath");
		
		try {
			_class = ClassReflection.forName(classpath);
		} catch (ReflectionException e) {
			e.printStackTrace();
		}
		
		default_width = jsonData.getFloat("img_width");
		default_height = jsonData.getFloat("img_height");
		
		Vector2 vec_pos = new Vector2();
		vec_pos.x = jsonData.getFloat("spawn_position_x");
		vec_pos.y = jsonData.getFloat("spawn_position_y");
		
		float rot = jsonData.getFloat("spawn_rotation"); 
		scale_x = jsonData.getFloat("spawn_scale_x");
		scale_y = jsonData.getFloat("spawn_scale_y");
		
		if(jsonData.has("object_data"))
			object_data = (AdditionalData) json.readValue(AdditionalData.class, jsonData.get("object_data"));
		
		//setter
		param_position = new Vector2(vec_pos);
		img_texture = Loader.get(texturepath);
		
		this.setAlignment(Alignment.get(jsonData.getInt("alignment")));
		
		if(jsonData.has("rot_alignment")) {
			this.setRotationAlignment(Alignment.get(jsonData.getInt("rot_alignment")));
			System.out.println("	EOTerrain.read() ### rotation_alignment_set: " + rotation_alignment_set);
		} else {
			this.setRotationAlignment(Alignment.CENTER);
		}
		
		System.out.println("	EOTerrain.read() ### Alignment_set: " + alignment_set);
		param_angle = rot;
	}
}
