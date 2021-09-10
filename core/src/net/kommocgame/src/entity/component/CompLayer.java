package net.kommocgame.src.entity.component;

import com.badlogic.ashley.core.Component;

import net.kommocgame.src.layer.EnumGameLayer;
import net.kommocgame.src.render.GameManager;

public class CompLayer implements Component {

	public int priority;
	
	public EnumGameLayer getEnum() {
		return EnumGameLayer.getEnum(priority);
	}
	
	public void setEnum(EnumGameLayer _enum) {
		priority = _enum.getPriority();
	}
	
}
