package net.kommocgame.src.entity;

import com.badlogic.gdx.graphics.g2d.ParticleEffect;

public class EntityFX extends ParticleEffect {
	/*
	public static final EntityFX fx_explosion_1	= new EntityFX("fx_explosion_1");
	public static final EntityFX fx_smoke_1		= new EntityFX("fx_smoke_1");
	public static final EntityFX fx_shoot_1		= new EntityFX("fx_shoot_1");
	public static final EntityFX fx_bullet_1_16	= new EntityFX("fx_bullet_1_16");
	public static final EntityFX fx_bullet_2_16	= new EntityFX("fx_bullet_2_16");
	public static final EntityFX fx_bullet_1	= new EntityFX("fx_bullet_1");
	public static final EntityFX fx_blood_red_1	= new EntityFX("fx_blood_red_1");
	public static final EntityFX fx_blood_red_2	= new EntityFX("fx_blood_red_2");
	*/
	public static final EntityFX fx_explosion_1		= new EntityFX(1);
	public static final EntityFX fx_smoke_1			= new EntityFX(2);
	@Deprecated
	public static final EntityFX fx_shoot_1			= new EntityFX(3);
	public static final EntityFX fx_bullet_1_16		= new EntityFX(4);
	public static final EntityFX fx_bullet_2_16		= new EntityFX(5);
	public static final EntityFX fx_shoot_bloom		= new EntityFX(6);
	public static final EntityFX fx_shoot_smoke		= new EntityFX(7);
	public static final EntityFX fx_bullet_1		= new EntityFX(8);
	public static final EntityFX fx_blood_red_1		= new EntityFX(9);
	public static final EntityFX fx_blood_red_2		= new EntityFX(10);
	private int FX_ID;
	
	public EntityFX(int id) {
		super();
		FX_ID = id;
	}
	
	public EntityFX(EntityFX entity) {
		super(entity);
		FX_ID = entity.getID();
	}
	
	public int getID() {
		return FX_ID;
	}
}
