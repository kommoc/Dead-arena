package net.kommocgame.src.editor.objects;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonValue;
import com.badlogic.gdx.utils.reflect.ClassReflection;
import com.badlogic.gdx.utils.reflect.ReflectionException;
import com.github.czyzby.kiwi.util.gdx.scene2d.Alignment;

import net.kommocgame.src.Game;
import net.kommocgame.src.editor.EditorCore;
import net.kommocgame.src.entity.EntityBase;
import net.kommocgame.src.world.AdditionalData;
import net.kommocgame.src.world.DataObject;
import net.kommocgame.src.world.World;

public class EOEntity extends EObject<EntityBase> {
	
	private transient int offsetX = 0;
	private transient int offsetY = 0;
	
	/** This constructor for json. */
	public EOEntity() {}
	
	public EOEntity(World world, Class _class) {
		this(world, _class, null);
	}
	
	public EOEntity(World world, EntityBase inst) {
		this(world, inst.getClass(), inst);
	}
	
	public EOEntity(World world, Class _class, EntityBase inst) {
		super(world, _class, inst);
		
		if(instance != null) {
			image_object = new Image(instance.getSprite().getTexture());
			texturepath = Game.assetManager.getAssetFileName(instance.getSprite().getTexture());
			param_position = new Vector2(instance.getPosition().cpy());
		}
	}

	@Override
	public void translateObject(float x, float y) {
		this.translateObject(x, y, getAlignment());
	}

	@Override
	public void translateObject(float x, float y, Alignment alignment) {
		if(alignment == alignment.BOTTOMLEFT) {
			offsetX = 1;
			offsetY = 1;
		} else if(alignment == alignment.BOTTOM) {
			offsetX = 0;
			offsetY = 1;
		} else if(alignment == alignment.BOTTOMRIGHT) {
			offsetX = -1;
			offsetY = 1;
		} else if(alignment == alignment.LEFT) {
			offsetX = 1;
			offsetY = 0;
		} else if(alignment == alignment.CENTER) {
			offsetX = 0;
			offsetY = 0;
		} else if(alignment == alignment.RIGHT) {
			offsetX = -1;
			offsetY = 0;
		} else if(alignment == alignment.TOPLEFT) {
			offsetX = 1;
			offsetY = -1;
		} else if(alignment == alignment.TOP) {
			offsetX = 0;
			offsetY = -1;
		} else if(alignment == alignment.TOPRIGHT) {
			offsetX = -1;
			offsetY = -1;
		}
		
		if(instance != null) {
			instance.setPosition(x + getWidth() / 2f * offsetX, y + getHeight() / 2f * offsetY);
			//getSpawnPosition().set(instance.getPosition());
			//this.alignment_set = alignment;
		}
	}

	@Override
	public void rotateObject(float angle) {
		if(instance != null) {
			instance.setRotation(angle);
		}
	}

	@Override
	public void scaleObject(float scaleX, float scaleY) {
		//TODO
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
		
		if(instance.getDefinition().getShape() instanceof PolygonShape) {
			PolygonShape shape = (PolygonShape) instance.getDefinition().getShape();
			shape.getVertex(0, object_size);
			
			return object_size.x  * 2f;
		} else if(instance.getDefinition().getShape() instanceof CircleShape){
			CircleShape shape = (CircleShape) instance.getDefinition().getShape();
			
			return instance.getShapeSize() * 2f;
		}
		
		//XXX DEFAULT::
		return instance.getShapeSize() * 2f;
	}

	@Override
	public float getHeight() {
		if(instance == null)
			 return 1;
		
		if(instance.getDefinition().getShape() instanceof PolygonShape) {
			PolygonShape shape = (PolygonShape) instance.getDefinition().getShape();
			shape.getVertex(0, object_size);
			
			return object_size.y * 2f;
		} else if(instance.getDefinition().getShape() instanceof CircleShape){
			CircleShape shape = (CircleShape) instance.getDefinition().getShape();
			
			return instance.getShapeSize() * 2f;
		}
		
		return instance.getShapeSize() * 2f;
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
		
		return instance.getRotation();
	}

	@Override
	public EOEntity getCopy() {
		Json json = new Json();
		String str_json = json.toJson(this);
		
		EOEntity copy = json.fromJson(EOEntity.class, str_json);
		System.out.println("			EOEntity.getCopy() ### JSON: " + str_json);
		
		return copy;
	}
	
	@Override
	public Vector2 getDebugPosition() {
		return getPosition().cpy().add(-getWidth() /2f, -getHeight() / 2f);
	}

	@Override
	public void createInstance() {
		if(instance == null)
			try {
				instance = (EntityBase) ClassReflection.getConstructor(_class, World.class, SpriteBatch.class, float.class, float.class)
				.newInstance(world, world.getWorldBatch(), param_position.x, param_position.y);
				
				world.addEntityIntoWorld(instance);
				//object_data = instance.getAdditionalData();
				
				this.scaleObject(scale_x, scale_y);
				this.rotateObject(getSpawnRotation());
				this.translateObject(param_position.x, param_position.y, alignment_set);
			} catch (ReflectionException e) {
				e.printStackTrace();
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
		System.out.println("	EOEntity.write() ### name: " + instance);
		System.out.println("	EOEntity.write() ### texturePath: " + texturepath);
		System.out.println("	EOEntity.write() ### classpath: " + classpath);
		System.out.println("	EOEntity.write() ### spawn_pos: " + getSpawnPosition());
		System.out.println("	EOEntity.write() ### spawn_rot: " + getRotation());
		System.out.println("	EOEntity.write() ### spawn_scaleX: " + getScaleX());
		System.out.println("	EOEntity.write() ### spawn_scaleY: " + getScaleY());
		System.out.println("	&&&&&&&&&&&	");
		*/
		json.writeValue("texturepath", texturepath);
		json.writeValue("classpath", classpath);
		
		json.writeValue("spawn_position_x", getSpawnPosition().x);
		json.writeValue("spawn_position_y", getSpawnPosition().y);
		
		json.writeValue("spawn_rotation", getRotation());
		json.writeValue("spawn_scale_x", getScaleX());
		json.writeValue("spawn_scale_y", getScaleY());
		
		json.writeValue("alignment", alignment_set.get());
		
		if(param_name != null)
			json.writeValue("name", param_name);
		
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
		
		Vector2 vec_pos = new Vector2();
		vec_pos.x = jsonData.getFloat("spawn_position_x");
		vec_pos.y = jsonData.getFloat("spawn_position_y");
		
		float rot = jsonData.getFloat("spawn_rotation"); 
		scale_x = jsonData.getFloat("spawn_scale_x");
		scale_y = jsonData.getFloat("spawn_scale_y");
		
		if(jsonData.has("name"))
			param_name = jsonData.getString("name");
		
		if(jsonData.has("object_data"))
			object_data = (AdditionalData) json.readValue(AdditionalData.class, jsonData.get("object_data"));
		
		//setter
		param_position = new Vector2(vec_pos);
		alignment_set = Alignment.get(jsonData.getInt("alignment"));
		param_angle = rot;
	}
}
