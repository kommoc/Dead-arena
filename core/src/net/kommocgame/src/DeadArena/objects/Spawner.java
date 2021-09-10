package net.kommocgame.src.DeadArena.objects;

import java.util.Random;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ArrayMap;
import com.badlogic.gdx.utils.reflect.ClassReflection;
import com.badlogic.gdx.utils.reflect.ReflectionException;

import net.kommocgame.src.Game;
import net.kommocgame.src.GameState;
import net.kommocgame.src.entity.EntityBase;
import net.kommocgame.src.trigger.ICondition;
import net.kommocgame.src.trigger.TriggerCircle;
import net.kommocgame.src.world.DataObject;
import net.kommocgame.src.world.World;

public class Spawner extends TriggerCircle {
	
	private long rate = 5000l;
	private long lastTime = System.currentTimeMillis();
	
	private int max_count = 5;
	public int current_entity_count;
	
	private ISpawn event;
	
	//private ArrayMap<Class<? extends EntityBase>, Float> pool_list = new ArrayMap<Class<? extends EntityBase>, Float>();
	private Array<SpawnerEntityNode> pool_list = new Array<SpawnerEntityNode>();
	
	public Spawner(World world, ICondition icondition, long time, float x, float y, BodyType type) {
		super(world, icondition, time, x, y, type);
		
		getAdditionalData().getParameters().add(new DataObject<Integer>("Max count of mobs", getMaxCount()) {
			@Override
			public void setParameter(Integer par1) {
				setMaxCount(par1);
			}

			@Override
			public Integer getParameter() {
				return getMaxCount();
			}
		});
		
		getAdditionalData().getParameters().add(new DataObject<Integer>("Rate", getRate()) {
			@Override
			public void setParameter(Integer par1) {
				setRate(par1);
			}

			@Override
			public Integer getParameter() {
				return getRate();
			}
		});
		
		getAdditionalData().getParameters().add(new DataObject<Array<SpawnerEntityNode>>("Pool list", getPoolList()) {
			@Override
			public void setParameter(Array<SpawnerEntityNode> par1) {
				setArrayOrder(par1);
			}

			@Override
			public Array<SpawnerEntityNode> getParameter() {
				return getPoolList();
			}
		});
		
		//pool_list.put(DAEntityMobZombieFast.class, 1f);
		//pool_list.put(DAEntityMobZombieFat.class, 2f);
		//pool_list.put(DAEntityMobZombie.class, 0.5f);
	}
	
	@Override
	public void update() {
		super.update();
		
		if(Game.getGameState() == GameState.CONTINUE) {
			if(current_entity_count < max_count && lastTime + rate < System.currentTimeMillis()) {	// to write delay.
				Class<? extends EntityBase> _class = getEntityToSpawn();
				if(_class != null) {
					try {
						EntityBase entity = (EntityBase) ClassReflection.getConstructor(_class, World.class, SpriteBatch.class,
								float.class, float.class).newInstance(worldObj, worldObj.getWorldBatch(), 
										getRand().add(getPosition()).x, getRand().add(getPosition()).y);
						
						entity.setSpawnerMark(this);
						worldObj.addEntityIntoWorld(entity);
						current_entity_count++;
						
						if(event != null)
							event.onSpawn(entity, this);
						
					} catch (ReflectionException e) {
						e.printStackTrace();
					}
				}
				
				lastTime = System.currentTimeMillis();
			}
		}
	}
	
	public Spawner setSpawnEvent(ISpawn ispawn) {
		this.event = ispawn;
		return this;
	}
	
	public void setArrayOrder(Array<SpawnerEntityNode> par1) {
		pool_list = par1;
	}
	
	public Spawner setMaxCount(int max) {
		max_count = max;
		return this;
	}
	
	public Spawner setRate(long rate) {
		this.rate = rate;
		return this;
	}
	
	public Array<SpawnerEntityNode> getPoolList() {
		return pool_list;
	}
	
	private Class<EntityBase> getEntityToSpawn() {
		if(pool_list.size > 0) {
			Random rand = new Random();
			float sum = 0, dx = 0, value = rand.nextFloat();
			
			for(int index = 0; index < pool_list.size; sum += pool_list.get(index++).getWeight());
			
			for(int i = 0; i < pool_list.size; i++) {
				if(value > dx && value <= dx + getValue(sum, i)) {
					return (Class<EntityBase>) pool_list.get(i).getClassInstance();
				} else dx += getValue(sum, i);
			}
		}
		
		return null;
	}
	
	private Vector2 getRand() {
		Random rand = new Random();
		
		return new Vector2(-getFixture().getShape().getRadius() + rand.nextFloat() * getFixture().getShape().getRadius() * 2f,
				-getFixture().getShape().getRadius() + rand.nextFloat() * getFixture().getShape().getRadius() * 2f);
	}
	
	public int getMaxCount() {
		return max_count;
	}
	
	public int getRate() {
		return (int) rate;
	}
	
	private float getValue(float sum, int index) {
		return pool_list.get(index).getWeight() / sum;//.getValueAt(index) / sum;
	}
	
	public interface ISpawn {
		void onSpawn(EntityBase entity, Spawner spawner);
	}
}
