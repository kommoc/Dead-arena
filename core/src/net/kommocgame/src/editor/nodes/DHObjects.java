package net.kommocgame.src.editor.nodes;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.Array;

import net.kommocgame.src.Game;
import net.kommocgame.src.Loader;
import net.kommocgame.src.DeadArena.entity.Bunker;
import net.kommocgame.src.DeadArena.entity.Tree;
import net.kommocgame.src.DeadArena.entity.mobs.DAEntityMobZombie;
import net.kommocgame.src.DeadArena.entity.mobs.DAEntityMobZombieFast;
import net.kommocgame.src.DeadArena.entity.mobs.DAEntityMobZombieFat;
import net.kommocgame.src.DeadArena.entity.props.DProp_wooden_box;
import net.kommocgame.src.DeadArena.entity.props.SProp_barrikade_large;
import net.kommocgame.src.DeadArena.entity.props.SProp_bricks_01;
import net.kommocgame.src.DeadArena.entity.props.SProp_rust_01;
import net.kommocgame.src.DeadArena.entity.props.SProp_rust_02;
import net.kommocgame.src.DeadArena.entity.props.SProp_sandBlock_1;
import net.kommocgame.src.DeadArena.objects.Spawner;
import net.kommocgame.src.entity.EntityBase;
import net.kommocgame.src.entity.EntityLiving;
import net.kommocgame.src.entity.character.EntityPlayer;
import net.kommocgame.src.entity.props.EntityProp;
import net.kommocgame.src.entity.props.WallConcrete;
import net.kommocgame.src.trigger.TriggerCircle;
import net.kommocgame.src.trigger.TriggerRect;

public class DHObjects {

	private static Array<ENodeObj> list_nodes = new Array<ENodeObj>();
	
	static {
		list_nodes.add(new ENEntity(EntityBase.class, Loader.objectsMobs("img_def_0.png")));
		list_nodes.add(new ENEntity(EntityProp.class,  Loader.objectsProps("zone.png")));
		list_nodes.add(new ENEntity(SProp_sandBlock_1.class,  Loader.objectsProps("block_01.png")));
		list_nodes.add(new ENEntity(SProp_rust_02.class,  Loader.objectsProps("rubbish_rust_02.png")));
		list_nodes.add(new ENEntity(SProp_barrikade_large.class,  Loader.objectsProps("barrikade_01.png")));
		list_nodes.add(new ENEntity(DProp_wooden_box.class,  Loader.objectsProps("box_wooden_01.png")));
		list_nodes.add(new ENEntity(SProp_bricks_01.class,  Loader.objectsProps("bricks_01.png")));
		list_nodes.add(new ENEntity(SProp_rust_01.class,  Loader.objectsProps("rubbish_rust_01.png")));
		
		list_nodes.add(new ENEntity(EntityLiving.class, Loader.objectsMobs("img_def_1.png")));
		list_nodes.add(new ENEntity(EntityPlayer.class, Loader.objectsUnits("worker_body_weapon_pistol.png")));
		list_nodes.add(new ENEntity(DAEntityMobZombie.class, Loader.objectsMobs("zombie_easy/icon_zombie_human.png")));
		list_nodes.add(new ENEntity(DAEntityMobZombieFat.class, Loader.objectsMobs("zombie_easy/icon_zombie_fat.png")));
		list_nodes.add(new ENEntity(DAEntityMobZombieFast.class, Loader.objectsMobs("zombie_easy/icon_zombie_fast.png")));
		
		list_nodes.add(new ENEntity(WallConcrete.class, Loader.objectsProps("wall_hazard.png")));
		list_nodes.add(new ENEntity(Tree.class, Loader.objectsProps("trees/tree1.png")));
		list_nodes.add(new ENEntity(Bunker.class, Loader.objectsUnits("building_bunker.png")));
		
		list_nodes.add(new ENTrigger(TriggerCircle.class, Loader.guiIcon("icon_trigger_circle.png")));
		list_nodes.add(new ENTrigger(TriggerRect.class, Loader.guiIcon("icon_trigger_rect.png")));
		list_nodes.add(new ENTrigger(Spawner.class, Loader.guiIcon("icon_spawner.png")));
		
		for(int i = 0; i < Game.assetManager.getLoadedAssets(); i++) {
			try {
				if(Game.assetManager.get((Game.assetManager.getAssetNames().get(i))) instanceof Texture)
				list_nodes.add(new ENTerrain(Loader.get(Game.assetManager.getAssetNames().get(i)), Game.assetManager.getAssetNames().get(i)));
			} catch(Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	public static Array<ENodeObj> getNodes() {
		return list_nodes;
	}
}
