package net.kommocgame.src.editor.nodes;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.reflect.ClassReflection;
import com.badlogic.gdx.utils.reflect.ReflectionException;

import net.kommocgame.src.editor.EditorLevelObjectsPanel;
import net.kommocgame.src.trigger.ICondition;
import net.kommocgame.src.trigger.TriggerBase;
import net.kommocgame.src.world.World;

public class ENTrigger extends ENodeObj<TriggerBase> {
	
	Image img;
	
	public ENTrigger(Class _class, Texture tex) {
		super(_class);
		img = new Image(tex);
	}

	@Override
	public void dragToWorld(float x, float y, World world) {
		if(instance == null) {
			try {
				instance = (TriggerBase) ClassReflection.getConstructor(_class, World.class, ICondition.class, long.class, float.class, float.class, BodyType.class)
				.newInstance(world, new ICondition() {
					public void execute(TriggerBase tr) {}
					
					public boolean condition(TriggerBase tr) {
						return false;
					}
				}, 1000, x, y, BodyType.StaticBody);
				
			} catch (ReflectionException e) {
				e.printStackTrace();
			}
			
			world.addTriggerIntoWorld(instance);
		} else {
			instance.setPosition(x, y);
		}
	}

	@Override
	public void releaseInWorld(float x, float y) {
		instance = null;
	}

	@Override
	public void cancel(World world) {
		
		if(instance != null)
			instance.deleteObject();
		instance = null;
	}

	@Override
	public Table checkInfo() {
		return null;
	}

	@Override
	public Image getImage() {
		return img;
	}

	@Override
	public String getName() {
		return _class.getSimpleName();
	}

	@Override
	public void addToPanel(EditorLevelObjectsPanel panel) {
		if(instance == null)
			return;
		/*
		panel.getNodeTable().add(new ELOTrigger(instance));
		panel.getNodeTable().row(); */
	}

	@Override
	public void removeFromPanel(EditorLevelObjectsPanel panel) {
		// TODO Auto-generated method stub
		
	}

}
