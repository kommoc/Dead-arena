package net.kommocgame.src.item;

import com.badlogic.gdx.graphics.Texture;

public class ItemArmor extends Item {
	
	private float	speed_boost			= 1f;
	private float	damage_reduction	= 0f;
	private float	reload_boost		= 1f;
	
	private int		additional_slots	= 0;
	private int		additional_hp		= 0;
	
	private int MAX_AMMO_SHOTGUN		= 0;
	private int MAX_AMMO_PP				= 0;
	private int MAX_AMMO_RIFLE			= 0;
	private int MAX_AMMO_MACHINEGUN		= 0;
	private int MAX_AMMO_LAUNCHER		= 0;
	private int MAX_AMMO_GRENADE		= 0;
	
	public ItemArmor(int id, Texture texture) {
		super(id, texture);
	}
	
	public ItemArmor setDamageReduction(float reduce) {
		damage_reduction = reduce;
		return this;
	}
	
	public ItemArmor setSpeedBoost(float boost) {
		speed_boost = boost;
		return this;
	}
	
	public ItemArmor setReloadBoost(float boost) {
		reload_boost = boost;
		return this;
	}
	
	public ItemArmor setAdditionalSlot(int slots) {
		additional_slots = slots;
		return this;
	}
	
	public ItemArmor setAdditionalHp(int hp) {
		additional_hp = hp;
		return this;
	}
	
	public ItemArmor setAdditionalAmmoShotgun(int ammo) {
		MAX_AMMO_SHOTGUN = ammo;
		return this;
	}
	
	public ItemArmor setAdditionalAmmoPp(int ammo) {
		MAX_AMMO_PP = ammo;
		return this;
	}
	
	public ItemArmor setAdditionalAmmoRifle(int ammo) {
		MAX_AMMO_RIFLE = ammo;
		return this;
	}
	
	public ItemArmor setAdditionalAmmoMachinegun(int ammo) {
		MAX_AMMO_MACHINEGUN= ammo;
		return this;
	}
	
	public ItemArmor setAdditionalAmmoLauncher(int ammo) {
		MAX_AMMO_LAUNCHER = ammo;
		return this;
	}
	
	public ItemArmor setAdditionalAmmoGrenade(int ammo) {
		MAX_AMMO_GRENADE = ammo;
		return this;
	}
	
	public float getDamagereduction() {
		return damage_reduction;
	}
	
	public float getSpeedBoost() {
		return speed_boost;
	}
	
	public float getReloadBoost() {
		return reload_boost;
	}
	
	public int getAdditionalSlot() {
		return additional_slots;
	}
	
	public int getAdditionalHp() {
		return additional_hp;
	}
	
	public int getMaxAmmoShotgun() {
		return MAX_AMMO_SHOTGUN;
	}
	
	public int getMaxAmmoRifle() {
		return MAX_AMMO_RIFLE;
	}
	
	public int getMaxAmmoMachineGun() {
		return MAX_AMMO_MACHINEGUN;
	}
	
	public int getMaxAmmoLauncher() {
		return MAX_AMMO_LAUNCHER;
	}
	
	public int getMaxAmmoGrenade() {
		return MAX_AMMO_GRENADE;
	}
	
	public int getMaxAmmoPp() {
		return MAX_AMMO_PP;
	}
}
