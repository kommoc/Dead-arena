package net.kommocgame.src.editor.nodes;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.reflect.ClassReflection;
import com.badlogic.gdx.utils.reflect.ReflectionException;
import com.github.czyzby.kiwi.util.gdx.scene2d.Alignment;

import net.kommocgame.src.Game;
import net.kommocgame.src.editor.EditorLevelObjectsPanel;
import net.kommocgame.src.world.World;
import net.kommocgame.src.world.level.LevelBase;
import net.kommocgame.src.world.level.TerrainObject;

public class ENTerrain extends ENodeObj<TerrainObject> {
	
	Image img;
	Texture texture;
	String name;
	
	public ENTerrain(Texture tex, String name) {
		super(TerrainObject.class);
		texture = tex;
		img = new Image(tex);
		this.name = name;
	}

	@Override
	public void dragToWorld(float x, float y, World world) {
		if(instance == null) {
			System.out.println("	instance is null.");
			try {
				instance = (TerrainObject) ClassReflection.getConstructor(_class, LevelBase.class, Texture.class, String.class, float.class, float.class)
						.newInstance(world.getLevel(), texture, name, x, y);
				instance.getImage().setSize(instance.getImage().getWidth() * Game.SCALE_WORLD_VALUE_FINAL, 
						instance.getImage().getHeight()* Game.SCALE_WORLD_VALUE_FINAL);
			} catch (ReflectionException e) {
				e.printStackTrace();
			}
			
			world.getLevel().spawnTerrainObj(instance, x, y);
		} else {
			instance.set(x, y, Alignment.CENTER);
			System.out.println("	ENTTerrain: instance set.");
		}
	}

	@Override
	public void releaseInWorld(float x, float y) {
		instance = null;
	}

	@Override
	public void cancel(World world) {
		System.out.println("	cancel");
		
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
		return name;
	}

	@Override
	
	public void addToPanel(EditorLevelObjectsPanel panel) {
		if(instance == null)
			return;
		/*
		panel.getNodeTable().add(new ELOTerrain(instance));
		panel.getNodeTable().row();*/
	}

	@Override
	public void removeFromPanel(EditorLevelObjectsPanel panel) {
		// TODO Auto-generated method stub
		
	}

}
