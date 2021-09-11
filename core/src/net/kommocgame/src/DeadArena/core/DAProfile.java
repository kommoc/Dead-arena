package net.kommocgame.src.DeadArena.core;

import java.io.FileWriter;
import java.io.IOException;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.utils.DataInput;
import com.badlogic.gdx.utils.Json;

import net.kommocgame.src.Loader;
import net.kommocgame.src.item.Item;
import net.kommocgame.src.profile.InventoryPlayer;

public class DAProfile {
	
	private static Preferences preferences = Gdx.app.getPreferences("DeadArena_settings");
	private static Preferences profile = Gdx.app.getPreferences("DeadArena_profile");
	private static InventoryPlayer inventory;
	private static boolean newProfile = false;
	
	private static String profile_version = "0.4.27";
	
	public DAProfile() {
		if(!checkProfile()) {
			createProfile();
		}
	}
	
	public void createProfile() {
		preferences.clear();
		profile.clear();
		preferences.flush();
		profile.flush();
		
		System.out.println("### DAProfile(new instance) ### create new profile. ");
		System.out.println("### DAProfile(new instance) ### new version is: " + profile_version);
		newProfile = true;
		profile.putBoolean("profile_create", true);
		profile.putString("profile_version", profile_version);
		
		this.set_bunkerHP(1500);
		this.set_bunkerMaxHP(1500);
		this.set_game_wave(1);
		this.set_player_money(450000);
		this.set_player_cryptcoin(250);
		this.set_playerHP(200);
		this.set_playerMaxHP(200);
		
		this.set_slotItem(1, Item.NULL);
		this.set_slotItem(2, Item.NULL);
		this.set_slotItem(3, Item.NULL);
		
		preferences.putBoolean("preferences_create", true);
		preferences.putFloat("settings_volume_music", 0.5f);
		preferences.putFloat("settings_volume_sound", 0.5f);
		preferences.putFloat("settings_game_zoom", 0.04f);
		
		this.set_settings_debug_text(false);
		this.set_settings_debug_bounds(false);
		this.set_settings_minimap(false);
		this.set_settings_shootingByMonitor(false);
		this.set_settings_debug_fpsCounter(true);
				
		this.set_settings_vsync(false);
		this.set_settings_shadows(true);
		
		preferences.flush();
		profile.flush();
	}
	
	public Preferences getProfile() {
		return profile;
	}
	
	public Preferences getPreferences() {
		return preferences;
	}
	
	protected InventoryPlayer getInventory() {
		return inventory;
	}
	
	public void reset() {
		profile.remove("profile_create");
		preferences.remove("preferences_create");
		
		createProfile();
		setNewProfile_items();
	}
	
	/** Set method should call after assetManager has been loaded. */
	public void setNewProfile_items() {
		if(!newProfile)
			return;
		
		for(int i = 0; i < Item.itemList.length; i++) {
			if(Item.itemList[i] != null) {
				this.set_itemParchase(Item.itemList[i].ID, false);
				System.out.println("DAProfile.setNewProfile_items() ### ID: " + i);
			}
		}
		
		this.set_itemParchase(Item.item_weapon_glock17.ID, true);
		this.set_itemParchase(Item.item_weapon_desertEagle.ID, true);
		this.set_itemParchase(Item.item_weapon_ak12.ID, true);
		this.set_itemParchase(Item.item_weapon_m134.ID, true);
		this.set_itemParchase(Item.item_weapon_m4a1.ID, true);
		this.set_itemParchase(Item.item_weapon_kpv.ID, true);
		this.set_itemParchase(Item.item_weapon_vector.ID, true);
		this.set_itemParchase(Item.item_weapon_remington870.ID, true);
		
		this.set_slotItem(1, Item.item_weapon_desertEagle.ID);
		this.set_slotItem(2, Item.item_weapon_vector.ID);
		
		//this.loadInventoryPlayer();
	}
	
	public void loadInventoryPlayer() {
		Json json = new Json();
		
		try {
			if(Loader.getGameFile("android/assets/player.json").exists() && !newProfile) {
				
				DataInput input_level = new DataInput(Loader.getGameFile("android/assets/player.json").read());
				inventory = (InventoryPlayer) json.fromJson(InventoryPlayer.class, input_level);
				input_level.close();
				
				System.out.println("DAProfile.loadInventoryPlayer() ### inventory has loaded succesfully.");
				//TODO PLAYER INVENTORY
			} else {
				System.out.println("DAProfile.loadInventoryPlayer() ### NEW_INVENTORY_CREATE");
				inventory = new InventoryPlayer();
				
				updatePlayerInventory();
			}
			
		} catch (IOException e) { e.printStackTrace(); }
	}
	
	public void updatePlayerInventory() {
		Json json = new Json();
		
		try {
			if(inventory.getItemStackInSlot(0) != null)
				System.out.println("DAProfile.updatePlayerInventory() ### [0]" + inventory.getItemStackInSlot(0).getItem().getName());
			if(inventory.getItemStackInSlot(1) != null)
				System.out.println("DAProfile.updatePlayerInventory() ### [1]" + inventory.getItemStackInSlot(1).getItem().getName());
			if(inventory.getItemStackInSlot(2) != null)
				System.out.println("DAProfile.updatePlayerInventory() ### [2]" + inventory.getItemStackInSlot(2).getItem().getName());
			
			String json_info = json.toJson(inventory, InventoryPlayer.class);
			json_info = json.prettyPrint(json_info);

			FileWriter out = new FileWriter(Loader.getGameFile("android/assets/player.json").file());
			out.flush();
			out.write(json_info);
			out.close();
			
			System.out.println("DAProfile.updatePlayerInventory() INVENTORY_SAVED");
		} catch (IOException e) { e.printStackTrace(); }
	}
	
	private boolean checkProfile() {
		return checkVersion() && checkPreferences();
	}
	
	private boolean checkVersion() {
		System.out.println("Profile.checkVersion() ### " + profile.getString("profile_version"));
		return profile.contains("profile_create") && profile.contains("profile_version") 
				&& profile.getString("profile_version").contentEquals(profile_version);
	}
	
	private boolean checkPreferences() {
		return preferences.contains("preferences_create");
	}
	
	public int get_playerHP() {
		return profile.getInteger("player_hp");
	}
	
	public int get_playerMaxHP() {
		return profile.getInteger("player_max_hp");
	}
	
	public int get_money() {
		return profile.getInteger("player_money");
	}
	
	public int get_cryptcoin() {
		return profile.getInteger("player_cryptcoin");
	}
	
	@Deprecated
	public int get_bunkerHP() {
		return profile.getInteger("bunker_hp");
	}
	
	@Deprecated
	public int get_bunkerMaxHP() {
		return profile.getInteger("bunker_max_hp");
	}
	
	public int get_wave() {
		return profile.getInteger("game_wave");
	}
	
	@Deprecated
	public boolean get_itemPurchase(int id) {
		if(!profile.contains("item_" + id)) {
			//System.out.println("#DAProfile.get_itemPurchase() ### Profile have'nt current item_ID[" + id + "] in the map.");
			return false;
		}
		
		return profile.getBoolean("item_" + id);
	}
	
	@Deprecated
	public int get_slotItem(int slotID) {
		if(slotID >= 1 && slotID <= 3)
			return profile.getInteger("fast_slot_" + slotID);
		else {
			System.out.println("#DAProfile.get_slotItem() ### uncorrect slotID!");
			return -1; //empty item
		}
	}
	
	public float settings_volume_sound() {
		return preferences.getFloat("settings_volume_sound");
	}
	
	public float settings_volume_music() {
		return preferences.getFloat("settings_volume_music");
	}
	
	public float settings_zoom() {
		return preferences.getFloat("settings_game_zoom");
	}
	
	public boolean settings_minimapIsEnable() {
		return preferences.getBoolean("settings_game_minimap");
	}
	
	public boolean settings_shootingByMonitorIsEnable() {
		return preferences.getBoolean("settings_game_shootingByMonitor");
	}
	
	/** Return enable bounds around entity. */
	public boolean settings_debug_bounds() {
		return preferences.getBoolean("settings_debug_bounds");
	}
	
	/** Return enable debug_text at everything. */
	public boolean settings_debug_text() {
		return preferences.getBoolean("settings_debug_text");
	}
	
	public boolean settings_debug_fpsCounter() {
		return preferences.getBoolean("settings_debug_fpsCounter");
	}
	
	public boolean settings_shadows() {
		return preferences.getBoolean("settings_shadows");
	}
	
	public boolean settings_vsync() {
		return preferences.getBoolean("settings_vsync");
	}
	
	public void set_playerHP(int par1) {
		profile.putInteger("player_hp", par1);
		profile.flush();
	}
	
	public void set_playerMaxHP(int par1) {
		profile.putInteger("player_max_hp", par1);
		profile.flush();
	}
	
	public void set_player_money(int par1) {
		profile.putInteger("player_money", par1);
		profile.flush();
	}
	
	public void set_player_cryptcoin(int par1) {
		profile.putInteger("player_cryptcoin", par1);
		profile.flush();
	}
	
	public void set_game_wave(int par1) {
		profile.putInteger("game_wave", par1);
		profile.flush();
	}
	
	@Deprecated
	public void set_bunkerHP(int par1) {
		profile.putInteger("bunker_hp", par1);
		profile.flush();
	}
	
	@Deprecated
	public void set_bunkerMaxHP(int par1) {
		profile.putInteger("bunker_max_hp", par1);
		profile.flush();
	}
	
	@Deprecated
	public void set_itemParchase(int id, boolean parchase) {
		profile.putBoolean("item_" + id, parchase);
		profile.flush();
	}
	
	/** @param slotID - (1 - 3)
	 *  @param itemID if itemID (-1) it mean that slot is empty. */
	@Deprecated
	public void set_slotItem(int slotID, int itemID) {
		if(slotID >= 1 && slotID <= 3)
			profile.putInteger("fast_slot_" + slotID, itemID);
		else System.out.println("#DAProfile.set_slotItem() ### slot_id is uncorrect!");
		profile.flush();
	}
	
	public void set_settings_volume_sound(int par1) {
		preferences.putFloat("settings_volume_sound", (float) par1 / 100f);
		preferences.flush();
	}
	
	public void set_settings_volume_music(int par1) {
		preferences.putFloat("settings_volume_music", (float) par1 / 100f);
		preferences.flush();
	}
	
	public void set_settings_zoom(int par1) {
		preferences.putFloat("settings_game_zoom", (float) par1 / 100f);
		preferences.flush();
	}
	
	public void set_settings_minimap(boolean par1) {
		preferences.putBoolean("settings_game_minimap", par1);
		preferences.flush();
	}
	
	public void set_settings_shootingByMonitor(boolean par1) {
		preferences.putBoolean("settings_game_shootingByMonitor", par1);
		preferences.flush();
	}
	
	public void set_settings_debug_bounds(boolean par1) {
		preferences.putBoolean("settings_debug_bounds", par1);
		preferences.flush();
	}
	
	public void set_settings_debug_text(boolean par1) {
		preferences.putBoolean("settings_debug_text", par1);
		preferences.flush();
	}
	
	public void set_settings_debug_fpsCounter(boolean par1) {
		preferences.putBoolean("settings_debug_fpsCounter", par1);
		preferences.flush();
	}
	
	public void set_settings_shadows(boolean par1) {
		preferences.putBoolean("settings_shadows", par1);
		preferences.flush();
	}
	
	public void set_settings_vsync(boolean par1) {
		preferences.putBoolean("settings_vsync", par1);
		preferences.flush();
	}
}
