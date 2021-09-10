package net.kommocgame.src.DeadArena.core;

import net.kommocgame.src.Game;
import net.kommocgame.src.Loader;
import net.kommocgame.src.entity.IControllable;
import net.kommocgame.src.entity.character.EntityPlayer;
import net.kommocgame.src.item.Item;
import net.kommocgame.src.profile.InventoryPlayer;
import net.kommocgame.src.profile.ItemStack;
import net.kommocgame.src.world.World;
import net.kommocgame.src.world.level.ILevel;
import net.kommocgame.src.world.level.LevelBase;

public class DACore {
	
	long time_session = 0;
	int player_cost;
	int level;
	int wave;
	
	private World world;
	
	private boolean isGameProcessActive = false;
	private boolean isGameRedactorActive = false;
	
	private EntityPlayer	player;
	
	private InventoryPlayer player_session_inventory;
	private InventoryPlayer	warehouse_inventory;
	private DAProfile		profile;
	
	public DACore(World world, DAProfile profile) {
		this.world = world;
		this.profile = profile;
		warehouse_inventory = profile.getInventory();
	}
	
	/** This method will prepare game settings, balance and world. */
	public void constructLevel() {
		isGameProcessActive = true;
		
		world.game.resume();
		
		ILevel ilevel = new ILevel() {
			@Override
			public void onConstruct(LevelBase level) {
				DACore.this.createInventorySample();
				
				for(int i = 0; i < level.world.getEngine().getEntities().size(); i++) {
					if(level.world.getEngine().getEntities().get(i) instanceof EntityPlayer) {
						Game.CORE.player = (EntityPlayer) world.getEngine().getEntities().get(i);
						Game.CORE.world.game.setPlayer((IControllable)player);
					}
				}
			}
		};
		
		if(Game.profile.settings_shootingByMonitorIsEnable())
			world.getLevelManager().loadLevel(Loader.getLevel("Level_test.json"), ilevel);
		else world.getLevelManager().loadLevel(Loader.getLevel("Level_1.json"), ilevel);
	}
	
	public void deconstructLevel() {
		destroyInventory();
		world.deleteLevel();
		
		isGameProcessActive = false;
	}
	
	/** This method returning all items has been taking from warehouse. */
	private void destroyInventory() {
		if(getSessionInventory() == null)
			return;
		
		//getInventory().bodyarmor = getSessionInventory().bodyarmor;			//TODO
		//getInventory().outfit = getSessionInventory().outfit;					//TODO
		
		for(int i = 0; i < getSessionInventory().getMaxSlots(); i++) {
			getInventory().addItemStack(getSessionInventory().getItemStackInSlot(i));
			getInventory().setItemSlot(getSessionInventory().getItemStackInSlot(i), i);
		}
		
		player_session_inventory = null;
	}
	
	public void UPDATE_GAME_CORE() {
		warehouse_inventory.updateInventory();
	}
	
	public void updateWorldCore() {
		//inventory_player.updateInventory();
	}
	
	/** Creating inventory sample for session. */
	private void createInventorySample() {
		player_session_inventory = new InventoryPlayer(getInventory().getMaxSlots());
		
		//player_session_inventory.bodyarmor = getInventory().bodyarmor;		//TODO
		//player_session_inventory.outfit = getInventory().outfit;				//TODO
		
		//getInventory().bodyarmor = null;										//TODO
		//getInventory().outfit = null;											//TODO
		
		for(int i = 0; i < getInventory().getMaxSlots(); i++) {
			player_session_inventory.setItemSlot(getInventory().getItemStackInSlot(i), i);
			getInventory().removeItemStack(player_session_inventory.getItemStackInSlot(i));
		}
	}
	
	public boolean purchaseItemStack(Item item) {
		if(profile.get_money() > item.getCost()) {
			getInventory().addItem(item);
			profile.set_player_money(profile.get_money() - item.getCost());
			profile.updatePlayerInventory();
			return true;
		}
		profile.updatePlayerInventory();
		return false;
	}
	
	public boolean purchaseItemStack(ItemStack itemStack) {
		if(itemStack != null && profile.get_money() > itemStack.getItem().getCost()) {
			getInventory().addItemStack(itemStack);
			profile.set_player_money(profile.get_money() - itemStack.getItem().getCost());
			profile.updatePlayerInventory();
			return true;
		}
		profile.updatePlayerInventory();
		return false;
	}
	
	public void setRedactorActivity(boolean par1) {
		isGameRedactorActive = par1;
	}
	
	public int getCurrentWave() {
		return wave;
	}
	
	/** Return warehouse inventory. */
	public InventoryPlayer getInventory() {
		return warehouse_inventory;
	}
	
	/** Return session player inventory. */
	public InventoryPlayer getSessionInventory() {
		return player_session_inventory;
	}
	
	public EntityPlayer getPlayer() {
		return player;
	}
	
	public int getPlayerCost() {
		return player_cost;
	}
	
	public int getLevelNumber() {
		return level;
	}
	
	public boolean isGameProcessActive() {
		return isGameProcessActive;
	}
	
	public boolean isGameRedactorActive() {
		return isGameRedactorActive;
	}
}
