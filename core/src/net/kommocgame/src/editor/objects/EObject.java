package net.kommocgame.src.editor.objects;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.utils.Json.Serializable;
import com.github.czyzby.kiwi.util.gdx.scene2d.Alignment;

import net.kommocgame.src.Loader;
import net.kommocgame.src.editor.IEditorActions;
import net.kommocgame.src.world.AdditionalData;
import net.kommocgame.src.world.World;
import net.kommocgame.src.world.WorldObject;

public abstract class EObject<T extends WorldObject> implements IEditorActions, Serializable {
	
	/** May be null. */
	transient T instance;
	
	/** Main class of spawned instance. */
	transient Class _class;
	
	/** Main image showing at the EditorLevelObjects. */
	transient Image image_object;
	
	/** Main texture of the main image. */
	transient Texture img_texture;
	
	/** Bounds size of the object. Need for debug editor render. */
	transient Vector2 object_size = new Vector2(2, 2);
	
	/** Additional data of current instance. */
	AdditionalData object_data;
	
	/** Classpath for Json. */
	String classpath;
	
	/** Texture path for Json. */
	String texturepath;
	
	/** Last alignment set. */
	Alignment alignment_set;
	
	/** Choosed rotation alignment set. */
	Alignment rotation_alignment_set;
	
	/** Spawn position of this instance. */
	Vector2 param_position = new Vector2(0, 0);
	
	/** Rotation of the object. */
	float param_angle = 0f;
	
	/** The name of this node. */
	String param_name;
	
	/** Scale by axis X of this object. */
	float scale_x = 1f;
	
	/** Scale by axis X of this object. */
	float scale_y = 1f;
	
	/** World instance. */
	transient World world;
	//params
	
	/** This constructor for json. */
	public EObject() {}
	
	public EObject(World world, Class _class) {
		this(world, _class, null);
	}
	
	public EObject(World world, T inst) {
		this(world, inst.getClass(), inst);
	}
	
	public EObject(World world, Class _class, T inst) {
		this._class = _class;
		classpath = _class.getName();
		System.out.println("	EObject(new instance) ### classpath: " + classpath);
		
		instance = inst;
		object_data = inst.getAdditionalData();
		
		this.world = world;
		
		if(alignment_set == null)
			alignment_set = Alignment.CENTER;
		
		if(rotation_alignment_set == null)
			rotation_alignment_set = Alignment.CENTER;
	}
	
	/** Create new instance of this class. */
	public abstract void createInstance();
	
	/** Delete current instance. */
	public abstract void deleteInstance();
	
	/** Return true if instance is active(box2d). */
	public abstract boolean isActive();
	
	/** Main image of this node. */
	public Image getImage() {
		try {
			if(image_object == null && texturepath != null) {
				img_texture = Loader.get(texturepath);
				image_object = new Image(img_texture);
			}
		} catch(NullPointerException e) {
			System.out.println("		ERROR " + this.getClass().getSimpleName() + ".getImage() ### texturepath: " + texturepath);
		}
		
		return image_object;
	}
	
	/** Return the class_type. */
	public Class getType() {
		return _class;
	}
	
	/** Return last set alignment. */
	public Alignment getAlignment() {
		return alignment_set;
	}
	
	/** Return the rotation alignment set. */
	public Alignment getRotationAlignment() {
		return rotation_alignment_set;
	}
	
	/** Return the instance of this class_type. */
	public T getInstance() {
		return instance;
	}
	
	public float getScaleX() {
		return scale_x;
	}
	
	public float getScaleY() {
		return scale_y;
	}
	
	public float getSpawnRotation() {
		return param_angle;
	}
	
	public String getName() {
		return param_name;
	}
	
	/** Editor width. */
	public abstract float getWidth();
	
	/** Editor height. */
	public abstract float getHeight();
	
	public abstract Vector2 getSpawnPosition();
	
	public abstract Vector2 getPosition();
	
	public abstract float getRotation();
	
	public abstract EObject<T> getCopy();
	
	/** Debug rectangle. */
	public abstract Vector2 getDebugPosition();
	
	public AdditionalData getAdditionalData() {
		return object_data;
	}
	
	/** Method needed for json deserialization. */
	public void setWorld(World world) {
		this.world = world;
	}
	
	public void setAlignment(Alignment alignment) {
		this.alignment_set = alignment;
	}
	
	public void setRotationAlignment(Alignment alignment) {
		this.rotation_alignment_set = alignment;
	}
	
	public void setAdditionalData(AdditionalData data) {
		object_data = data;
	}
	
}
