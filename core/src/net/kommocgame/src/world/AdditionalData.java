package net.kommocgame.src.world;

import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.Json.Serializable;
import com.badlogic.gdx.utils.JsonValue;
import com.badlogic.gdx.utils.OrderedMap;

public class AdditionalData implements Serializable {
	
	/** Main array of writable parameters. */
	Array<DataObject> params = new Array<DataObject>();
	
	/** The read parameters from json. */
	OrderedMap<String, Object> readed_map = new OrderedMap<String, Object>();
	
	public AdditionalData() { }
	
	public Array<DataObject> getParameters() {
		return params;
	}
	
	public OrderedMap<String, Object> getMap() {
		return readed_map;
	}
	
	@Override
	/** Write custom parameters for current type. */
	public void write(Json json) {
		if(params.size > 0) {
			//json.writeValue("additionalData", params, Array.class);
			for(int i = 0; i < params.size; i++) {
				//System.out.println("		AdditionalData.write() ### " + params.get(i).name);			
				//System.out.println("		AdditionalData.write() ### " + params.get(i).getParameter().getClass());
				
				json.writeValue(params.get(i).name, params.get(i).getParameter(), params.get(i).getClass());
				//json.writeObjectStart(params.get(i).name);
				//json.writeValue("param_value", params.get(i).getParameter(), params.get(i).getClass());
				//json.writeObjectEnd();
			}
		}
	}

	@Override
	/** Apply custom parameters for current instance. */
	public void read(Json json, JsonValue jsonData) {
		//System.out.println("		AdditionalData.read() ### jsonData.size: " + jsonData.size);
		
		for(int i = 0; i < jsonData.size; i++) {
			String name = jsonData.get(i).name;
			Object parameter = json.readValue(null, jsonData.get(i));
			
			getMap().put(name, parameter);
			//System.out.println("		AdditionalData.read() ### read param [" + name + " : " + parameter + "]");
		}
	}
	
}
