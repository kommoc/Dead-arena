package net.kommocgame.src.item;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.brashmonkey.spriter.Timeline;

import net.kommocgame.src.SoundEffect;
import net.kommocgame.src.SoundManager.SoundSource;
import net.kommocgame.src.VecUtils;
import net.kommocgame.src.entity.EntityFX;
import net.kommocgame.src.entity.EntityLiving;
import net.kommocgame.src.entity.AI.event.AIEventPlaySound;
import net.kommocgame.src.entity.character.EntityPlayer;
import net.kommocgame.src.entity.component.CompCamera;
import net.kommocgame.src.entity.particle.EntityProjectile;
import net.kommocgame.src.profile.ItemStack;
import net.kommocgame.src.world.World;

public class ItemWeapon extends Item {
	
	/** #DA */
	private boolean isReloading = false;
	/** #DA */
	private long lastTime_reload = 0;
	/** #DA range 0f - 1f. */
	private float reload_progress = 0;
	
	private long reload;
	private long rate;
	private long lastTime = 0;
	
	private int damage;
	private int penetration;
	private float distance;
	private float knockback;
	private float velocity = 3f;
	private float range = 5f;
	private int bullets = 1;
	//private EntityEffect[] buff; 			// ADDING LATER.
	
	private SoundSource sound_shoot;
	private SoundSource sound_reload;
	private SoundSource sound_missfire;
	
	private SoundEffect last_sound_shoot;
	
	private ItemWeapon(int id, Texture icon, int maxAmmo, int damage, int penetration, float distance, float knockback, long rate, long reload) {
		super(id, icon);
		//this.maxAmmo = maxAmmo;
		//this.damage = damage;
		//this.penetration = penetration;
		//this.distance = distance;
		//this.knockback = knockback;
		//this.rate = rate;
		//this.reload = reload;
		
		//currentAmmo = maxAmmo;
	}
	
	public ItemWeapon(int id, Texture icon) {
		super(id, icon);
		this.setRestorable(true);
		//this.maxAmmo = maxAmmo;
		//this.damage = damage;
		//this.penetration = penetration;
		//this.distance = distance;
		//this.knockback = knockback;
		//this.rate = rate;
		//this.reload = reload;
		
		//currentAmmo = maxAmmo;
	}
	
	public void shoot(EntityLiving entityLiving) {
		Vector2 vecPos = entityLiving.getPosition().cpy();
		
		if(last_sound_shoot != null && !last_sound_shoot.isFinished()) {
			last_sound_shoot.setFinished();
			last_sound_shoot = null;
		}
		
		last_sound_shoot = World.get_sound(sound_shoot, vecPos.x, vecPos.y).setDistance(distance).setLinearFade(0.5f);
		
		//System.out.println("ItemWeapon.shoot() ### " + sound_shoot);
		
		if(sound_shoot.getSound() != null) {
			entityLiving.playSoundAtEntity(last_sound_shoot);
			entityLiving.worldObj.addAIEvent(new AIEventPlaySound(entityLiving, entityLiving.worldObj, last_sound_shoot));
		}
		
		if(entityLiving.compSprite.getFirePoint() != null) {
			Timeline.Key.Object point = entityLiving.compSprite.getFirePoint();
			Vector2 vec_FX_point = VecUtils.angleToVector_1(point.angle).limit(1);
			
			EntityProjectile fx_shoot_bloom = World.get_projectile(EntityFX.fx_shoot_bloom, entityLiving.worldObj).attachToFirePoint(point)
					.offsetPos(vec_FX_point).offsetAngle(-90f);
			
			EntityProjectile fx_shoot_smoke = World.get_projectile(EntityFX.fx_shoot_smoke, entityLiving.worldObj).attachToFirePoint(point)
					.offsetPos(vec_FX_point).offsetAngle(-90f);
			
			entityLiving.worldObj.spawnProjectileIntoWorld(fx_shoot_bloom, 1);
			entityLiving.worldObj.spawnProjectileIntoWorld(fx_shoot_smoke, 1);
			
			for(int i = 0; i < bullets; i++) {
				float rand_dist = (float) MathUtils.random(distance) * 0.15f;
				float rand_range = range / 2f - MathUtils.random(range);
				float rand_vel = velocity * 0.1f - MathUtils.random(velocity) * 0.1f;
				Vector2 vec_fire = VecUtils.angleToVector_1(point.angle + rand_range).scl(3f).limit(2.5f);
				Vector2 vec_bullet_start = new Vector2(point.position.x, point.position.y).add(-vec_fire.x, -vec_fire.y);
				Vector2 vec_bullet_end = vecPos.cpy().add(VecUtils.angleToVector_1(entityLiving.getRotation()).setLength(distance));
				
				EntityProjectile fx_bullet = World.get_projectile(EntityFX.fx_bullet_1_16, entityLiving.worldObj).offsetAngle(90f)
						.offsetPos(vec_FX_point.cpy())
						.setToFirePoint(point).prew_position(vec_bullet_start)
						.setVelocity(vec_fire.cpy().setLength(velocity + rand_vel))
						.setDistance(distance + rand_dist);
				entityLiving.worldObj.spawnProjectileIntoWorld(fx_bullet, 2);
				
				Bullet bullet = new Bullet(this, entityLiving.worldObj, entityLiving, fx_bullet, vec_bullet_start, vec_bullet_end);
				fx_bullet.setBullet(bullet);
				
				//@SETUP 29.01.20 					//BAD_CODE
				//BulletProjectile bullet = new BulletProjectile(this, entityLiving.worldObj, entityLiving, fx_bullet,
				//		vec_bullet_start, vec_bullet_end);
				//fx_bullet.setBullet(bullet);
			}
		} else {
			System.out.println("	ItemWeapon.shoot() ### Have'nt firePoint!");
		}
		
		/*
		if(entityLiving.compSprite.getFirePoint() != null) {
			Timeline.Key.Object point = entityLiving.compSprite.getFirePoint();
			Vector2 vec_FX_point = VecUtils.angleToVector_1(point.angle).limit(1);
			
			EntityFX fx_shoot = new EntityFX(EntityFX.fx_shoot_1, entityLiving.worldObj).attachToFirePoint(point)
					.offsetPos(vec_FX_point).offsetAngle(-90f);
					
			entityLiving.worldObj.spawnFXintoWorld(fx_shoot, 1);
			
			for(int i = 0; i < bullets; i++) {
				float rand_dist = (float) MathUtils.random(distance) * 0.15f;
				float rand_range = range / 2f - MathUtils.random(range);
				float rand_vel = velocity * 0.1f - MathUtils.random(velocity) * 0.1f;
				Vector2 vec_fire = VecUtils.angleToVector_1(point.angle + rand_range).scl(3f).limit(2.5f);
				Vector2 vec_bullet_start = new Vector2(point.position.x, point.position.y).add(-vec_fire.x, -vec_fire.y);
				Vector2 vec_bullet_end = vecPos.cpy().add(VecUtils.angleToVector_1(entityLiving.getRotation()).setLength(distance));
				
				EntityFX fx_bullet = new EntityFX(EntityFX.fx_bullet_1_16, entityLiving.worldObj).offsetAngle(90f)
						.offsetPos(vec_FX_point.cpy())
						.setToFirePoint(point).prew_position(vec_bullet_start)
						.setVelocity(vec_fire.cpy().setLength(velocity + rand_vel))
						.setDistance(distance + rand_dist);
				entityLiving.worldObj.spawnFXintoWorld(fx_bullet, 2);
				
				Bullet bullet = new Bullet(this, entityLiving.worldObj, entityLiving, fx_bullet, vec_bullet_start, vec_bullet_end);
				fx_bullet.setBullet(bullet);
			}
		} else {
			System.out.println("	ItemWeapon.shoot() ### Have'nt firePoint!");
		}*/
		
		//entityLiving.worldObj.physics.box2dWorld.rayCast(new Bullet(this), entityLiving.getPosition(),
		//		vecPos.add(VecUtils.angleToVector_1(entityLiving.getRotation()).setLength(distance)));
	}
	
	@Override
	public void onItemUse(ItemStack itemStack, EntityLiving entityLiving) {
		//System.out.println("	ItemWeapon.onItemUse() ### current ammo: " + currentAmmo);
		if(itemStack.getUseSize() > 0 && lastTime + rate < System.currentTimeMillis() && !isReloading) {
			lastTime = System.currentTimeMillis();
			itemStack.damage(1);
			this.shoot(entityLiving);
			//System.out.println("	ItemWeapon.onItemUse() ### compCamera: );
			entityLiving.getComponent(CompCamera.class).shakeCam(damage);
		}
	}
	
	@Override
	public void update(ItemStack itemStack, EntityLiving entityLiving) {
		super.update(itemStack, entityLiving);
		
		//#DA
		if(isReloading && ((EntityPlayer)entityLiving).DA_getCurrentItem() == this) {
			if(System.currentTimeMillis() > lastTime_reload + reload) {
				//currentAmmo = maxAmmo;
				itemStack.setDamage(0);
				isReloading = false;
			}
			
			reload_progress = (float) (System.currentTimeMillis() - lastTime_reload) / (float) reload;
		} else {
			isReloading = false;
			reload_progress = 0;
		}
	}
	
	public ItemWeapon setShootSound(SoundSource sound) {
		this.sound_shoot = sound;
		return this;
	}
	
	/** Set the max range. */
	public ItemWeapon setRange(float angle) {
		this.range = angle;
		return this;
	}
	
	/** Set the max velocity. */
	public ItemWeapon setVelocity(float vel) {
		this.velocity = vel;
		return this;
	}
	
	/** Set the count of bullets in one shot. */
	public ItemWeapon setBullets(int par1) {
		bullets = par1;
		return this;
	}
	
	public ItemWeapon setWeaponDamage(int damage) {
		this.damage = damage;
		
		return this;
	}
	
	public ItemWeapon setPenetrationValue(int penetration) {
		this.penetration = penetration;
		
		return this;
	}
	
	public ItemWeapon setDistance(float distance) {
		this.distance = distance;
		
		return this;
	}
	
	public ItemWeapon setKnockback(float knockback) {
		this.knockback = knockback;
		
		return this;
	}
	
	public ItemWeapon setRate(int rate) {
		this.rate = rate;
		
		return this;
	}
	
	public ItemWeapon setReloadSpeed(int reload) {
		this.reload = reload;
		
		return this;
	}
	
	/** #DA Task reload if current item == this. */
	public void reload() {
		if(!isReloading)
			lastTime_reload = System.currentTimeMillis();
		isReloading = true;
	}
	
	/** #DA */
	public boolean isReloading() {
		return isReloading;
	}
	
	/** #DA Return the progress of the reload. */
	public float getReloadProgress() {
		return reload_progress;
	}
	
	/** Return the current ammo value. */
	/*public int getCurrentAmmo() {
		return ;
	}*/
	
	/** Return the time needed for reload. In ms. */
	public long getReloadTime() {
		return reload;
	}
	
	/** Return cooldown between shoots. In ms. */
	public long getRate() {
		return rate;
	}
	
	/** Return the max inertion of the bullet. */
	public float getVelocity() {
		return velocity;
	}
	
	/** Return the max razbros. */
	public float getRange() {
		return range;
	}
	
	/** Return the bullet damage. */
	public int getDamage() {
		return damage;
	}
	
	/** Return count of bullets in one shot. */
	public int getBullets() {
		return bullets;
	}
	
	/** Return the penetration of the bullet. */
	public int getPenetration() {
		return penetration;
	}
	
	/** Return the distance to shoot. */
	public float getDistance() {
		return distance;
	}
	
	/** Knockback value when the bullet hit. */
	public float getKnockback() {
		return knockback;
	}
	
}
