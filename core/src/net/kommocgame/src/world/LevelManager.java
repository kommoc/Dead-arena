package net.kommocgame.src.world;

import com.badlogic.gdx.files.FileHandle;

import net.kommocgame.src.world.level.ILevel;
import net.kommocgame.src.world.level.LevelBase;
import net.kommocgame.src.world.level.LevelLayer;
import net.kommocgame.src.world.level.TerrainObject;

public class LevelManager {
	
	private World world;
	
	private FileHandle LEVEL_LOAD		= null;
	private ILevel LEVEL_LOAD_ILEVEL	= null;
	
	private LevelBase LEVEL_SET		= null;
	private boolean WORLD_DELETE	= false;
	
	public LevelManager(World world) {
		this.world = world;
	}
	
	public void setLevel(LevelBase level) {
		if(world.getLevel() != null) {
			deleteLevel();
		}
		
		LEVEL_SET = level;
	}
	
	public void deleteLevel() {
		if(world.getLevel() != null) {
			world.getManagerAI().resetAll(world);
			WORLD_DELETE = true;
		}
	}
	
	public void loadLevel(FileHandle fileHandle, ILevel ilevel) {
		if(LEVEL_LOAD == null) {
			LEVEL_LOAD = fileHandle;
			LEVEL_LOAD_ILEVEL = ilevel;
			deleteLevel();
		}
	}
	
	public void update() {
		if(WORLD_DELETE && world.getManagerAI().trackerIsEmpty()) {
			System.out.println("LevelManager.update(WORLD_DELETE)");
			
			world.getLevel().destroy();
			world.level = null;
			WORLD_DELETE = false;
		}
		
		if(world.getLevel() == null && world.getManagerAI().trackerIsEmpty()) {
			if(LEVEL_SET != null) {
				world.world_tick = 0l;
				world.level = LEVEL_SET;
				world.getLevel().createLevel();
				world.getLevel().initOnConstruct();
				
				LEVEL_SET = null;
			} else if(LEVEL_LOAD != null) {
				world.loadLevel(LEVEL_LOAD);
				world.getLevel().setILevel(LEVEL_LOAD_ILEVEL);
				world.getLevel().setObjectsDoneLoading(false);
				System.out.println("LevelManager.update(load level) entity's size: " + world.getEngine().getEntities().size());
				System.out.println("LevelManager.update(load level) grid A* size: " + world.getLevel().getGridNodes().getGraphPath().getNodeCount());
				
				world.getLevel().initOnConstruct();
				world.getLevel().terrain.updateLayers();
				
				for(LevelLayer layer_set : world.getLevel().getLayersList()) {
					for(int i = 0; i < layer_set.getObjectsList().size; i++) {
						TerrainObject object = layer_set.getObjectsList().get(i);
						
						object.setLayerIndex(-1);
						object.setLayerPositionID(-1);
					}
				}
				
				LEVEL_LOAD_ILEVEL = null;
				LEVEL_LOAD = null;
			}
		}
	}

}
