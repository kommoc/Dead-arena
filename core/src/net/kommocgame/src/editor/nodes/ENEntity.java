package net.kommocgame.src.editor.nodes;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.reflect.ClassReflection;
import com.badlogic.gdx.utils.reflect.ReflectionException;

import net.kommocgame.src.editor.EditorLevelObjectsPanel;
import net.kommocgame.src.entity.EntityBase;
import net.kommocgame.src.world.World;

public class ENEntity extends ENodeObj<EntityBase> {
	
	Image img;
	
	public ENEntity(Class _class, Texture texture) {
		super(_class);
		img = new Image(texture);
	}

	@Override
	public void dragToWorld(float x, float y, World world) {
		if(instance == null) {
			try {
				instance = (EntityBase) ClassReflection.getConstructor(_class, World.class, SpriteBatch.class, float.class, float.class)
						.newInstance(world, world.getWorldBatch(), x, y);
				
			} catch (ReflectionException e) {
				e.printStackTrace();
			}
			
			world.addEntityIntoWorld(instance);
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
		panel.getNodeTable().add(new ELOEntity(instance));
		panel.getNodeTable().row();*/
	}

	@Override
	public void removeFromPanel(EditorLevelObjectsPanel panel) {
		
	}
}
