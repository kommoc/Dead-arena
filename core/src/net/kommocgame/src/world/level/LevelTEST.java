package net.kommocgame.src.world.level;

import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.github.czyzby.kiwi.util.gdx.scene2d.Alignment;

import net.kommocgame.src.Game;
import net.kommocgame.src.Loader;
import net.kommocgame.src.SoundEffect;
import net.kommocgame.src.SoundManager;
import net.kommocgame.src.debug.CommandLine;
import net.kommocgame.src.entity.EntityLiving;
import net.kommocgame.src.entity.IControllable;
import net.kommocgame.src.entity.AI.event.AIEventPlaySound;
import net.kommocgame.src.entity.character.EntityPlayer;
import net.kommocgame.src.render.RenderLevelTEST;
import net.kommocgame.src.trigger.ICondition;
import net.kommocgame.src.trigger.TriggerBase;
import net.kommocgame.src.trigger.TriggerCircle;
import net.kommocgame.src.world.World;

public class LevelTEST extends LevelBase {
	private TerrainObject img_main = new TerrainObject(this, Loader.level("level_terrain/terrain_2009.png"), 0, 0);
	
	public LevelTEST(World world) {
		super(world);
		terrain = new RenderLevelTEST(world.getWorldBatch(), this);
		this.spawnTerrainObj(img_main, 0, 0);
		img_main.getImage().setSize(img_main.getImage().getWidth() * Game.SCALE_WORLD_VALUE_FINAL,
				img_main.getImage().getHeight() * Game.SCALE_WORLD_VALUE_FINAL);
		
		img_main.set(0, 0, Alignment.CENTER);
		
	}
	
	public void createLevel() {
		this.createAStarGrid(50, 50, 1f);
		/*
		world.addEntityIntoWorld(new WallConcrete(world, world.getWorldBatch(), -25, -25));
		world.addEntityIntoWorld(new WallConcrete(world, world.getWorldBatch(), -25, -15));
		world.addEntityIntoWorld(new WallConcrete(world, world.getWorldBatch(), -25, -5));
		world.addEntityIntoWorld(new WallConcrete(world, world.getWorldBatch(), -25, 5));
		world.addEntityIntoWorld(new WallConcrete(world, world.getWorldBatch(), -25, 15));
		world.addEntityIntoWorld(new WallConcrete(world, world.getWorldBatch(), -25, 25));
		
		world.addEntityIntoWorld(new WallConcrete(world, world.getWorldBatch(), 25, -25));
		world.addEntityIntoWorld(new WallConcrete(world, world.getWorldBatch(), 25, -15));
		world.addEntityIntoWorld(new WallConcrete(world, world.getWorldBatch(), 25, -5));
		world.addEntityIntoWorld(new WallConcrete(world, world.getWorldBatch(), 25, 5));
		world.addEntityIntoWorld(new WallConcrete(world, world.getWorldBatch(), 25, 15));
		world.addEntityIntoWorld(new WallConcrete(world, world.getWorldBatch(), 25, 25));
		
		world.addEntityIntoWorld(new WallConcrete(world, world.getWorldBatch(), -15, 25));
		world.addEntityIntoWorld(new WallConcrete(world, world.getWorldBatch(), -5, 25));
		world.addEntityIntoWorld(new WallConcrete(world, world.getWorldBatch(), 5, 25));
		world.addEntityIntoWorld(new WallConcrete(world, world.getWorldBatch(), 15, 25));
		world.addEntityIntoWorld(new WallConcrete(world, world.getWorldBatch(), 25, 25));
		
		world.addEntityIntoWorld(new WallConcrete(world, world.getWorldBatch(), -15, -25));
		world.addEntityIntoWorld(new WallConcrete(world, world.getWorldBatch(), -5, -25));
		world.addEntityIntoWorld(new WallConcrete(world, world.getWorldBatch(), 5, -25));
		world.addEntityIntoWorld(new WallConcrete(world, world.getWorldBatch(), 15, -25));
		world.addEntityIntoWorld(new WallConcrete(world, world.getWorldBatch(), 25, -25));
		*/
		
		CommandLine.instance._spawn("DAEntityMobZombie 0 -15");
		
		System.out.println("ddd");
		world.addEntityIntoWorld(new EntityLiving(world, world.game.spriteBatch));
		
		EntityPlayer player = new EntityPlayer(world, world.game.spriteBatch, 1, 1, Loader.objectsUnits("worker_pistol.png"), 10);
		world.game.setPlayer((IControllable) player);
		world.addEntityIntoWorld(player);
		
		TriggerCircle trigger_circle = new TriggerCircle(world, new ICondition() {
			
			@Override
			public void execute(TriggerBase tr) {
				//EntityMobZombie entity = new EntityMobZombie(world, world.getWorldBatch(), 5, 5);
				//world.addEntityIntoWorld(entity);
				System.out.println("	:EXEC:");
				//world.game.setPlayer(entity);
				tr.setPosition(-5, -5);
				tr.setExec();
				tr.reset();
			}
			
			@Override
			public boolean condition(TriggerBase tr) {
				if(tr.getContactList().size > 0)
					return true;
				
				return false;
			}
		}, 5000, 0, 0, 1, BodyType.StaticBody);
		
		System.out.println("	LevelTEST.createLevel() ### trigger.icondition.getClass(): " + trigger_circle.getICondition().getClass());
		
		world.addTriggerIntoWorld(trigger_circle);
		world.addTriggerIntoWorld(new TriggerCircle(world, new ICondition() {
			long lastTime = System.currentTimeMillis();
			
			@Override
			public void execute(TriggerBase tr) {
				//tr.getContactList().get(0).setShapeSize(2f);
				System.out.println("TRIGGER-2 exec::");
				tr.setExec();
			}
			
			@Override
			public boolean condition(TriggerBase tr) {
				if(lastTime + 1000 < System.currentTimeMillis() && false) {
					lastTime = System.currentTimeMillis();
					
					SoundEffect sound = new SoundEffect(SoundManager.sound_weapon_m134_shoot_2.getSound(),
							tr.getDefinition().getPosition().x, tr.getDefinition().getPosition().y).setDistance(6).setLinearFade(1f);
					tr.worldObj.addSoundToWorld(sound);
					tr.worldObj.addAIEvent(new AIEventPlaySound(tr.worldObj, sound));
					
					//System.out.println("position: " + tr.getDefinition().getPosition());
				}
				
				if(tr.getContactList().size > 1)
					return true;
				
				return false;
			}
		}, 3000, 7.07f, 5.65f, 5, BodyType.StaticBody));
	}
}
