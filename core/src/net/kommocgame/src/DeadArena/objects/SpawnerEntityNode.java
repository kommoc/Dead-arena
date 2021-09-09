package net.kommocgame.src.DeadArena.objects;

import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.Json.Serializable;
import com.badlogic.gdx.utils.JsonValue;
import com.badlogic.gdx.utils.reflect.ClassReflection;
import com.badlogic.gdx.utils.reflect.ReflectionException;

import net.kommocgame.src.entity.EntityBase;

public class SpawnerEntityNode implements Serializable {
	
	float weight = 0;
	String classpath;
	
	transient Class<? extends EntityBase> class_instance;
	
	/** For json. */
	public SpawnerEntityNode() {}
	
	public SpawnerEntityNode(Class<? extends EntityBase> _class, float weight) {
		this.weight = weight;
		class_instance = _class;
		classpath = class_instance.getName();
	}
	
	public Class<? extends EntityBase> getClassInstance() {
		return class_instance;
	}
	
	public float getWeight() {
		return weight;
	}

	@Override
	public void write(Json json) {
		json.writeValue("weight", weight);
		json.writeValue("classpath", classpath);
	}

	@Override
	public void read(Json json, JsonValue jsonData) {
		weight = jsonData.getFloat("weight");
		classpath = jsonData.getString("classpath");
		
		try {
			class_instance = ClassReflection.forName(classpath);
		} catch (ReflectionException e) {
			e.printStackTrace();
		}
	}
}
