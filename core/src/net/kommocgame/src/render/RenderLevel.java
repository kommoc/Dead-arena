package net.kommocgame.src.render;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.ui.Image;

import net.kommocgame.src.world.level.LevelBase;
import net.kommocgame.src.world.level.LevelLayer;
import net.kommocgame.src.world.level.TerrainObject;

public class RenderLevel {
	
	public SpriteBatch worldBatch;
	public Group objects;
	private LevelBase level;
	
	public RenderLevel(SpriteBatch batch, LevelBase level) {
		worldBatch = batch;
		objects = new Group();
		this.level = level;
	}
	
	/** Add non entity object(image). */
	public void addObject(Actor actor) {
		if(objects.getChildren().contains(actor, false)) {
			System.out.println("	RenderLevel - that actor is added yet.");
			return;
		}
		
		objects.addActor(actor);
	}
	
	/** Remove sprite from group batch. */
	public void removeObject(Actor actor) {
		if(!objects.getChildren().contains(actor, false)) {
			System.out.println("	RenderLevel - Actor isn't held in group.");
			return;
		}
		
		objects.removeActor(actor);
	}
	
	/** Get the TerrainObject instance by image. */
	public TerrainObject getTerrainObject(Image img) {
		for(int i = 0; i < level.spawn_list_terrainObj.size; i++) {
			if(level.spawn_list_terrainObj.keys().toArray().get(i).getImage() == img)
				return level.spawn_list_terrainObj.keys().toArray().get(i);
		}
		
		return null;
	}
	
	public void setUpTerrainObject(Actor actor) {
		int actor_id = objects.getChildren().indexOf(actor, false);
		
		objects.getChildren().swap(actor_id, actor_id + 1);
	}
	
	public void setDownTerrainObject(Actor actor) {
		int actor_id = objects.getChildren().indexOf(actor, false);
		
		objects.getChildren().swap(actor_id, actor_id - 1);
	}
	
	public void updateLayers() {
		int counter = 0;
		
		for(LevelLayer layer_set : level.getLayersList()) {
			for(int i = 0; i < layer_set.getObjectsList().size; i++) {
				TerrainObject object = layer_set.getObjectsList().get(i);
				
				System.out.println("TO SET: " + counter);
				System.out.println("SIZE: " + objects.getChildren().size);
				System.out.println("obj is null: " + (object == null));
				
				objects.getChildren().set(counter, object.getImage());
				counter++;
			}
		}
	}
	
	public void render() {
		objects.draw(worldBatch, 1);
		
		//System.out.println("objects.size: " + objects.getChildren().size);
	}
}
