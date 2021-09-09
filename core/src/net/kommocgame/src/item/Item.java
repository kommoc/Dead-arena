package net.kommocgame.src.item;

import com.badlogic.gdx.graphics.Texture;

import net.kommocgame.src.Loader;
import net.kommocgame.src.SoundManager;
import net.kommocgame.src.entity.EntityLiving;
import net.kommocgame.src.profile.ItemStack;

public class Item {
	
	public int ID;
	private Texture icon;
	private String itemName;
	private ItemType type;
	
	private int maxStackSize;
	private int maxUseSize;
	private boolean isRestorable;
	
	/** Need for magazine. Purchase be once. */
	private boolean isOncePurchase = false;
	
	/** Time usage. */
	long			timer = 1000l;
	long			last_time_usage;
	private boolean	isItemUsing = false;
	
	/** Dead Arena cost. */
	private int DA_cost;
	
	public static final int NULL = -1;
	
	public static Item[] itemList = new Item[128];
																	// id, icon, 						  maxAmmo, damage, penetr, distance, knockback rate
	public static final Item item_weapon_glock17		= new ItemWeapon(0, Loader.guiIcon("icon_glock17.png"))
			.setShootSound(SoundManager.sound_weapon_glock17_shoot_v2)
			.setRange(4f)
			.setVelocity(1.8f)
			//.setMaxAmmo(18)
			.setWeaponDamage(5)
			.setPenetrationValue(2)
			.setDistance(20)
			.setKnockback(30)
			.setRate(330)
			.setReloadSpeed(1500)
			.setItemName("Glock-17")
			.setItemType(ItemType.PISTOL)
			.setOncePurchase(true)
			.setMaxUseSize(18)
			.setCost(0);
	
	public static final Item item_weapon_desertEagle	= new ItemWeapon(1, Loader.guiIcon("icon_desertEagle.png"))
			.setShootSound(SoundManager.sound_weapon_desertEagle_shoot)
			.setRange(3f)
			.setVelocity(3f)
			//.setMaxAmmo(8)
			.setWeaponDamage(58)
			.setPenetrationValue(2)
			.setDistance(25)
			.setKnockback(50)
			.setRate(600)
			.setReloadSpeed(2200)
			.setItemName("Desert eagle")
			.setItemType(ItemType.PISTOL)
			.setOncePurchase(true)
			.setMaxUseSize(8)
			.setCost(3500);
	
	public static final Item item_weapon_vector	= new ItemWeapon(2, Loader.guiIcon("icon_vector.png"))
			.setShootSound(SoundManager.sound_weapon_vector_shoot)
			.setRange(10f)
			.setVelocity(2.5f)
			//.setMaxAmmo(45)
			.setWeaponDamage(25)
			.setPenetrationValue(2)
			.setDistance(21)
			.setKnockback(50)
			.setRate(55)
			.setReloadSpeed(1250)
			.setItemName("Kriss vector")
			.setItemType(ItemType.SUBMACHINEGUN)
			.setOncePurchase(true)
			.setMaxUseSize(45)
			.setCost(12500);
	
	public static final Item item_weapon_remington870	= new ItemWeapon(3, Loader.guiIcon("icon_remington870.png"))
			.setShootSound(SoundManager.sound_weapon_remington870_shoot)
			.setRange(50f)
			.setVelocity(1f)
			.setBullets(15)
			//.setMaxAmmo(7)
			.setWeaponDamage(15)
			.setPenetrationValue(2)
			.setDistance(11)
			.setKnockback(50)
			.setRate(1000)
			.setReloadSpeed(3500)
			.setItemName("Remington 870")
			.setItemType(ItemType.SHOTGUN)
			.setOncePurchase(true)
			.setMaxUseSize(7)
			.setCost(7500);
	
	public static final Item item_weapon_m4a1	= new ItemWeapon(4, Loader.guiIcon("icon_m4a1.png"))
			.setShootSound(SoundManager.sound_weapon_m4a1_shoot_1)
			.setRange(6f)
			.setVelocity(4f)
			//.setMaxAmmo(60)
			.setWeaponDamage(45)
			.setPenetrationValue(2)
			.setDistance(45)
			.setKnockback(50)
			.setRate(67)
			.setReloadSpeed(1800)
			.setItemName("M4A1")
			.setItemType(ItemType.RIFLE)
			.setOncePurchase(true)
			.setMaxUseSize(60)
			.setCost(32000);
	
	public static final Item item_weapon_ak12			= new ItemWeapon(5, Loader.guiIcon("icon_ak12.png"))
			.setShootSound(SoundManager.sound_weapon_akm)
			.setRange(7f)
			.setVelocity(3.1f)
			//.setMaxAmmo(45)
			.setWeaponDamage(60)
			.setPenetrationValue(2)
			.setDistance(40)
			.setKnockback(70)
			.setRate(100)
			.setReloadSpeed(2200)
			.setItemName("Ak-12")
			.setItemType(ItemType.RIFLE)
			.setOncePurchase(true)
			.setMaxUseSize(45)
			.setCost(7500);
	
	public static final Item item_weapon_m134			= new ItemWeapon(6, Loader.guiIcon("icon_m134.png"))
			.setShootSound(SoundManager.sound_weapon_m134_shoot_3)
			.setRange(15f)
			.setVelocity(2.8f)
			.setBullets(3)
			//.setMaxAmmo(999)
			.setWeaponDamage(12)
			.setPenetrationValue(2)
			.setDistance(45)
			.setKnockback(50)
			.setRate(25)
			.setReloadSpeed(8000)
			.setItemName("M-134")
			.setItemType(ItemType.MACHINEGUN)
			.setOncePurchase(true)
			.setMaxUseSize(999)
			.setCost(48350);
	
	public static final Item item_weapon_kpv			= new ItemWeapon(7, Loader.guiIcon("icon_kpv.png"))
			.setShootSound(SoundManager.sound_weapon_kpv_1)
			.setRange(10f)
			.setVelocity(2.8f)
			.setBullets(1)
			//.setMaxAmmo(250)
			.setWeaponDamage(250)
			.setPenetrationValue(2)
			.setDistance(75)
			.setKnockback(350)
			.setRate(110)
			.setReloadSpeed(12000)
			.setItemName("KPV")
			.setItemType(ItemType.MACHINEGUN)
			.setOncePurchase(true)
			.setMaxUseSize(250)
			.setCost(180350);
	
	public static final Item	item_armor_light		= new ItemArmor(32, Loader.guiIcon("armor/icon_bodyArmor_light.png"))
			.setAdditionalHp(75)
			.setAdditionalSlot(0)
			.setDamageReduction(0.25f)
			.setSpeedBoost(0.9f)
			.setItemName("Light armor")
			.setMaxStackSize(1)
			.setItemType(ItemType.BODYARMOR)
			.setOncePurchase(true)
			.setCost(15500);
	
	public static final Item 	item_armor_medium		= new ItemArmor(33, Loader.guiIcon("armor/icon_bodyArmor_medium.png"))
			.setAdditionalHp(165)
			.setAdditionalSlot(-1)
			.setDamageReduction(0.50f)
			.setSpeedBoost(0.75f)
			.setItemName("Medium armor")
			.setMaxStackSize(1)
			.setItemType(ItemType.BODYARMOR)
			.setOncePurchase(true)
			.setCost(89100);
	
	public static final Item	item_aidkit_light		= new ItemAidkit(64, Loader.guiIcon("other/icon_aid_kit_little.png"))
			.setRestoreHp(120)
			.setMaxStackSize(5)
			.setMaxUseSize(1)
			.setCost(280)
			.setItemName("Aidkit light")
			.setTimeUsage(2500);
	
	public static final Item	item_aidkit_medium		= new ItemAidkit(65, Loader.guiIcon("other/icon_aid_kit_medium.png"))
			.setRestoreHp(350)
			.setMaxStackSize(2)
			.setMaxUseSize(1)
			.setCost(1300)
			.setItemName("Aidkit medium")
			.setTimeUsage(4800);
	
	public static final Item	item_aidkit_large		= new ItemAidkit(66, Loader.guiIcon("other/icon_aid_kit_large.png"))
			.setRestoreHp(1000)
			.setMaxStackSize(1)
			.setMaxUseSize(5)
			.setCost(4000)
			.setItemName("Aidkit large")
			.setTimeUsage(9000);
	
	public Item(int id, Texture texture) {
		this.ID = id;
		this.icon = texture;
		type = ItemType.OTHER;
		maxStackSize = 1;
		maxUseSize = 1;
		isRestorable = false;
		
		if(itemList[id] != null) {
			try {
				throw new Exception("CONFLICT in ITEM_LIST: " + id);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		itemList[id] = this;;
	}
	
	/** Set the item name. */
	public Item setItemName(String name) {
		this.itemName = name;
		return this;
	}
	
	/** Set the item type. */
	public Item setItemType(ItemType type) {
		this.type = type;
		return this;
	}
	
	/** Set the cost of this item. */
	public Item setCost(int cost) {
		this.DA_cost = cost;
		return this;
	}
	
	public Item setMaxStackSize(int size) {
		maxStackSize = size;
		return this;
	}
	
	public Item setMaxUseSize(int size) {
		maxUseSize = size;
		return this;
	}
	
	/** Set the usage time need per once use. */
	public Item setTimeUsage(long time) {
		timer = time;
		return this;
	}
	
	/** Set the once store purchase. */
	public Item setOncePurchase(boolean isOnce) {
		isOncePurchase = isOnce;
		return this;
	}
	
	@Deprecated
	public void setItemInUse(boolean using) {
		isItemUsing = using;
	}
	
	public Item setRestorable(boolean isRestorable) {
		this.isRestorable = isRestorable;
		return this;
	}
	
	/** Return that item is stackable. */
	public boolean canStack( ) {
		return maxStackSize > 1;
	}
	
	/** Calling this method when item has been using. */
	public void onItemUse(ItemStack itemStack, EntityLiving entityLiving) {}
	
	/** Update method. Calling while item have at entity. */
	public void update(ItemStack itemStack, EntityLiving entityLiving) {}
	
	/** Return the texture item icon. */
	public Texture getIcon() {
		return icon;
	}
	
	/** Return the ItemType. If item type is not defined for return OTHER. */
	public ItemType getItemType() {
		return type;
	}
	
	/** Return the max stack size of current item. */
	public int getMaxStackSize() {
		return maxStackSize;
	}
	
	public int getMaxUseSize() {
		return maxUseSize;
	}
	
	/** Return the item name. */
	public String getName() {
		//return this.itemName != null ? "{" +  itemName + "}" : "{" + "unnamed" + "}";
		return this.itemName != null ? itemName : "unnamed";
		
	}
	
	/** Return the cost of this item. */
	public int getCost() {
		return DA_cost;
	}
	
	/** Return usage time need per once use. */
	public long getUsageTime() {
		return timer;
	}
	
	/** Return state of item. */
	public boolean isItemUsing() {
		return isItemUsing;
	}
	
	/** Return purchase be once. If true once sample of this item can bought in store. Need for store. */
	public boolean isOncePurchase() {
		return isOncePurchase;
	}
	
	/***/
	public boolean isRestorable() {
		return isRestorable;
	}
	
	public static Item getItemByID(int id) {
		if(id == NULL)
			return null;
		
		if(itemList[id] != null) {
			return itemList[id];
		} else
			try {
				throw new Exception("Item is not found in itemList: " + id);
			} catch (Exception e) {
				e.printStackTrace();
			}
		
		return null;
	}
	
	@Override
	public String toString() {
		return "@Item " + (itemName != null ? "{" + itemName + "}" : "{unnamed}") + " ID->" + ID;
	}
}
