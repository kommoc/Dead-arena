package net.kommocgame.src.world.level;

import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ObjectMap;
import com.github.czyzby.kiwi.util.gdx.scene2d.Alignment;

import net.kommocgame.src.Game;
import net.kommocgame.src.Loader;
import net.kommocgame.src.editor.EditorCore;
import net.kommocgame.src.entity.EntityBase;
import net.kommocgame.src.entity.EntityLiving;
import net.kommocgame.src.entity.props.EntityProp;
import net.kommocgame.src.render.RenderLevel;
import net.kommocgame.src.trigger.TriggerBase;
import net.kommocgame.src.world.World;
import net.kommocgame.src.world.physics.BodyBase;

public class LevelEditor extends LevelBase {
	
	@Deprecated
	public transient Array<EntityLiving> entity_list = new Array<EntityLiving>();
	@Deprecated
	public transient Array<EntityLiving> prop_list = new Array<EntityLiving>();
	@Deprecated
	public transient Array<TriggerBase> trigger_list = new Array<TriggerBase>();
	@Deprecated
	public transient Array<BodyBase> wall_list = new Array<BodyBase>();
	
	
	@Deprecated
	public transient Array<TerrainObject> 	level_list_terrainObj = new Array<TerrainObject>();
	@Deprecated
	public transient Array<EntityBase> 		level_list_entity = new Array<EntityBase>();
	@Deprecated
	public transient Array<EntityProp> 		level_list_props = new Array<EntityProp>();
	@Deprecated
	public transient Array<EntityLiving> 	level_list_mobs = new Array<EntityLiving>();
	@Deprecated
	public transient Array<Object> 			level_list_all = new Array<Object>();
	@Deprecated
	public transient Array<TriggerBase> 	level_list_triggers = new Array<TriggerBase>();
	//* That list is not be defined. */
	//public transient Array<EntityWall>	 	list_wall = new Array<EntityWall>();
	//* That list is not be defined. */
	//public transient Array<Object> 			list_misc = new Array<Object>();
	
	@Deprecated
	public transient ObjectMap<Label, EntityLiving> entity_map = new ObjectMap<Label, EntityLiving>();
	@Deprecated
	public transient ObjectMap<Label, EntityLiving> prop_map = new ObjectMap<Label, EntityLiving>();
	@Deprecated
	public transient ObjectMap<Label, TriggerBase> trigger_map = new ObjectMap<Label, TriggerBase>();
	@Deprecated
	public transient ObjectMap<Label, BodyBase> wall_map = new ObjectMap<Label, BodyBase>();
	
	public transient TerrainObject obj_level = new TerrainObject(this, Loader.level("level_1/level_plan.png"), 0, 0);
	public transient TerrainObject obj_axis = new TerrainObject(this, Loader.level("level_editor/ort_axis.png"), 0, 0);
	
	private transient EditorCore core;
	
	/** For Json. */
	private LevelEditor() { }
	
	public LevelEditor(World world, EditorCore core) {
		super(world);
		this.core = core;
		
		terrain = new RenderLevel(world.getWorldBatch(), this);
		obj_axis.getImage().setSize(obj_axis.getImage().getWidth() * 0.04f, obj_axis.getImage().getHeight() * 0.04f);
		spawnTerrainObj(obj_level, 0, 0);
		spawnTerrainObj(obj_axis, 0, 0);
		
		obj_axis.set(0, 0, Alignment.CENTER);
		obj_level.set(0, 0, Alignment.CENTER);
		
		core.addObject(obj_level);
		core.addObject(obj_axis);
		core.getNodeByObject(obj_level).getSpawnPosition().set(obj_level.getPosition());
		core.getNodeByObject(obj_axis).getSpawnPosition().set(obj_axis.getPosition());	
	}
	
	public LevelEditor setLevelName(String name) {
		this.name = name;
		return this;
	}
	
	public void setEditorCore(EditorCore core) {
		this.core = core;
	}
}
