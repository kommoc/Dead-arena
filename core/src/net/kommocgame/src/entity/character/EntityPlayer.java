package net.kommocgame.src.entity.character;

import com.badlogic.gdx.ai.steer.SteeringAcceleration;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.Fixture;

import net.kommocgame.src.Game;
import net.kommocgame.src.Loader;
import net.kommocgame.src.entity.EntityBase;
import net.kommocgame.src.entity.EntityLiving;
import net.kommocgame.src.entity.AI.Guild;
import net.kommocgame.src.entity.props.EntityItem;
import net.kommocgame.src.item.Item;
import net.kommocgame.src.layer.EnumGameLayer;
import net.kommocgame.src.profile.ItemStack;
import net.kommocgame.src.trigger.ICondition;
import net.kommocgame.src.trigger.TriggerBase;
import net.kommocgame.src.trigger.TriggerCircle;
import net.kommocgame.src.world.World;

public class EntityPlayer extends EntityLiving {
	
	public float vel_compare = 1f;
	private TriggerCircle sensor_action;
	private ICondition sensor_logic;
	
	/** #DA current item. */
	private Item DA_currentItem = null;
	//private FieldOfViewProximity field_of_view = new FieldOfViewProximity(this, worldObj.getEngine().getEntities(), 15, 5f);
	
	public EntityPlayer(World world, SpriteBatch batch) {
		this(world, batch, 0, 0);
	}
	
	public EntityPlayer(World world, SpriteBatch batch, float x, float y) {
		this(world, batch, x, y, Loader.objectsUnits("worker_pistol.png"), 100);
	}
	
	public EntityPlayer(World world, SpriteBatch batch, float x, float y, Texture tex, int hp) {
		super(world, batch, x, y, tex, hp);
		//this.getAI().setGuild(Guild.PLAYER);
		
		this.setLayer(EnumGameLayer.Player);
		this.setScale(1.5f);
		this.setShapeSize(1.125f);
		this.spriteToCenter();
		//this.setSensor();
		this.setMaxHP(Game.profile.get_playerMaxHP());
		this.setHP(Game.profile.get_playerHP());
		
		this.setMaxLinearAcceleration(75f);
		this.setMaxLinearSpeed(75f);
	}
	
	@Override
	public void toLook(float forceX, float forceY) throws NullPointerException {
		super.toLook(forceX, forceY);
		
		if(DA_getCurrentItem() != null && (forceX != 0 || forceY != 0))
			DA_getCurrentItem().onItemUse(null, this);
	}
	
	public void defaultCam(OrthographicCamera cam) {
		
	}
	
	/** TODO vars */
	@Override
	public void onUpdate(float deltaTime) {
		this.updateInventory();
		
		if(DA_currentItem == null) {
			this.setMaxLinearSpeed(100f);
			this.setMaxLinearAcceleration(100f);
		} else {
			this.setMaxLinearAcceleration(75f);
			this.setMaxLinearSpeed(75f);
		}
	}
	
	/** #DA Return the current item in hand. */
	@Deprecated
	public Item DA_getCurrentItem() {
		return DA_currentItem;
	}
	
	/** #DA set the current item in hand. */
	@Deprecated
	public void DA_setCurrentItem(Item item) {
		DA_currentItem = item;
	}
	
	@Deprecated
	public ItemStack getCurrentItem() {
		if(worldObj.game.guiManager.getGuiInGame() != null) {
			return worldObj.game.guiManager.getGuiInGame().getCurrentItemInSlot();
		} else {
			return null;
		}
	}
	
	/** Return the player trigger. */
	public TriggerCircle getActionSensor() {
		return sensor_action;
	}
	
	@Deprecated
	public void updateInventory() {
		//#DA
		for(int i = 0; i < Item.itemList.length; i++) {
			if(Item.itemList[i] != null && Game.profile.get_itemPurchase(i)) {
				Item item = Item.itemList[i];
			
				item.update(null, this);
			}
		}
		
		/* #DH
		for(int i = 0; i < this.getInvenoty().length; i++) {
			if(this.getInvenoty()[i] != null) {
				Item item = this.getInvenoty()[i].getItem();
			
				item.update(this.getInvenoty()[i], this);
			}
		}*/
	}
	
	public void setSensor() {
		sensor_logic = new ICondition() {
			
			@Override
			public void execute(TriggerBase tr) {
				//System.out.println("PLAYER_EXEC");
				tr.setExec();
			}
			
			@Override
			public boolean condition(TriggerBase tr) {
				tr.reset();
				for(int i = 0; i < tr.getContactList().size; i++) {
					if(tr.getContactList().get(i) instanceof EntityItem) {
						//System.out.println(((EntityItem)tr.getContactList().get(i)).getItemStack().getItem().toString());
						
						return true;
					}
				}
				
				return false;
			}
		};
		
		sensor_action = (TriggerCircle) new TriggerCircle(worldObj, sensor_logic, 0, 0, 0, 3, BodyType.StaticBody).attachTo(this);
		worldObj.addTriggerIntoWorld(sensor_action);
	}
	
	@Override
	public boolean shouldCollideWithEntity(EntityBase entity) {
		//System.out.println("Entity che za hueta?");
		return true;
	}
	
	@Override
	public boolean shouldCollideWithFixture(Fixture fixture) {
		//System.out.println("Fixture che za hueta?");
		return true;
	}
	
	@Override
	public void onCollideWithEntity(EntityBase entity) {
		//System.out.println("CONTACT With: " + entity);
	}
	
	@Override
	public void onCollideWithFixture(Fixture entity) {
		
	}
	
	@Override
	public void onDead() {
		super.onDead();
		
		System.out.println("Game: Player dead!");
	}
	
	@Override
	public void deleteObject() {
		super.deleteObject();
		if(getActionSensor() != null && !getActionSensor().isDeleted())
			getActionSensor().deleteObject();
	}
	
	@Override
	public void applySteering(SteeringAcceleration<Vector2> steering) {}

}
