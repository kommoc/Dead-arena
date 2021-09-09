package net.kommocgame.src;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.utils.ArrayMap;

public class SoundManager {
	
	private static final String sound_dir = "assets/sound/";
	
	//load sounds
	
	public static SoundSource sound_weapon_akm					= new SoundSource();
	public static SoundSource sound_ui_button					= new SoundSource();
	public static SoundSource sound_ui_button1					= new SoundSource();
	public static SoundSource sound_weapon_glock17_shoot_v2		= new SoundSource();
	public static SoundSource sound_weapon_glock17_shoot		= new SoundSource();
	public static SoundSource sound_weapon_m134_shoot			= new SoundSource();
	public static SoundSource sound_weapon_m134_shoot_2			= new SoundSource();
	public static SoundSource sound_weapon_m134_shoot_3			= new SoundSource();
	public static SoundSource sound_weapon_desertEagle_shoot	= new SoundSource();
	
	public static SoundSource sound_weapon_remington870_shoot	= new SoundSource();
	public static SoundSource sound_weapon_vector_shoot			= new SoundSource();
	public static SoundSource sound_weapon_m4a1_shoot_1			= new SoundSource();
	public static SoundSource sound_weapon_kpv					= new SoundSource();
	public static SoundSource sound_weapon_kpv_1				= new SoundSource();
	
	public static SoundSource sound_mobs_zm_roar_1				= new SoundSource();
	public static SoundSource sound_mobs_zm_roar_2				= new SoundSource();
	public static SoundSource sound_mobs_zm_roar_3				= new SoundSource();
	public static SoundSource sound_mobs_zm_roar_4				= new SoundSource();
	public static SoundSource sound_mobs_zm_roar_5				= new SoundSource();
	
	public static SoundSource sound_mobs_dead_1					= new SoundSource();
	
	public static SoundSource sound_bullet_hit_1				= new SoundSource();
	
	public static ArrayMap<Sound, Long> time = new ArrayMap<Sound, Long>();
	
	public SoundManager() {
		sound_weapon_akm				.setAsset(Game.assetManager.get(sound_dir + "weapon_akm.ogg", Sound.class));
		sound_ui_button					.setAsset(Game.assetManager.get(sound_dir + "ui_pickBut_v2.ogg", Sound.class));
		sound_ui_button1				.setAsset(Game.assetManager.get(sound_dir + "ui_pickBut.ogg", Sound.class));
		sound_weapon_glock17_shoot_v2	.setAsset(Game.assetManager.get(sound_dir + "weapon_glock17_shoot_v2.ogg", Sound.class));
		sound_weapon_glock17_shoot		.setAsset(Game.assetManager.get(sound_dir + "weapon_glock17_shoot.ogg", Sound.class));
		sound_weapon_m134_shoot			.setAsset(Game.assetManager.get(sound_dir + "weapon_M134_shoot.ogg", Sound.class));
		sound_weapon_m134_shoot_2 		.setAsset(Game.assetManager.get(sound_dir + "weapon_M134_shoot_2.ogg", Sound.class));
		sound_weapon_m134_shoot_3 		.setAsset(Game.assetManager.get(sound_dir + "weapon_M134_shoot_3.ogg", Sound.class));
		sound_weapon_desertEagle_shoot	.setAsset(Game.assetManager.get(sound_dir + "weapon_desertEagle_shoot.ogg", Sound.class));
		
		sound_weapon_remington870_shoot	.setAsset(Game.assetManager.get(sound_dir + "weapon_remington870_shoot.ogg", Sound.class));
		sound_weapon_vector_shoot		.setAsset(Game.assetManager.get(sound_dir + "weapon_vector_shoot.ogg", Sound.class));
		sound_weapon_m4a1_shoot_1		.setAsset(Game.assetManager.get(sound_dir + "weapon_m4a1_shoot_1.ogg", Sound.class));
		sound_weapon_kpv				.setAsset(Game.assetManager.get(sound_dir + "weapon_kpv_shoot.ogg", Sound.class));
		sound_weapon_kpv_1				.setAsset(Game.assetManager.get(sound_dir + "weapon_kpv_shoot_1.ogg", Sound.class));
		
		sound_mobs_zm_roar_1			.setAsset(Game.assetManager.get(sound_dir + "mobs_zm_roar_1.ogg", Sound.class));
		sound_mobs_zm_roar_2			.setAsset(Game.assetManager.get(sound_dir + "mobs_zm_roar_2.ogg", Sound.class));
		sound_mobs_zm_roar_3			.setAsset(Game.assetManager.get(sound_dir + "mobs_zm_roar_3.ogg", Sound.class));
		sound_mobs_zm_roar_4			.setAsset(Game.assetManager.get(sound_dir + "mobs_zm_roar_4.ogg", Sound.class));
		sound_mobs_zm_roar_5			.setAsset(Game.assetManager.get(sound_dir + "mobs_zm_roar_5.ogg", Sound.class));
		sound_mobs_dead_1				.setAsset(Game.assetManager.get(sound_dir + "mobs_dead_1.ogg", Sound.class));
		
		sound_bullet_hit_1				.setAsset(Game.assetManager.get(sound_dir + "bullet_hit_1.ogg", Sound.class));
		
		SoundManager.time.put(sound_ui_button1.sound, 						(long) 31);
		SoundManager.time.put(sound_weapon_akm.sound,						(long) 1435);
		SoundManager.time.put(sound_ui_button.sound,						(long) 411);
		SoundManager.time.put(sound_weapon_glock17_shoot.sound,				(long) 672);
		SoundManager.time.put(sound_weapon_glock17_shoot_v2.sound,			(long) 672);
		SoundManager.time.put(sound_weapon_m134_shoot.sound,				(long) 717);
		SoundManager.time.put(sound_weapon_m134_shoot_2.sound,				(long) 700);
		SoundManager.time.put(sound_weapon_m134_shoot_3.sound,				(long) 255);
		SoundManager.time.put(sound_weapon_desertEagle_shoot.sound,			(long) 1494);	
		
		SoundManager.time.put(sound_weapon_m4a1_shoot_1.sound,				(long) 797);
		SoundManager.time.put(sound_weapon_vector_shoot.sound,				(long) 300);
		SoundManager.time.put(sound_weapon_remington870_shoot.sound,		(long) 815);
		
		SoundManager.time.put(sound_weapon_kpv.sound,						(long) 2198);
		SoundManager.time.put(sound_weapon_kpv_1.sound,						(long) 2198);
		
		SoundManager.time.put(sound_mobs_zm_roar_1.sound,					(long) 1061);
		SoundManager.time.put(sound_mobs_zm_roar_2.sound,					(long) 1001);
		SoundManager.time.put(sound_mobs_zm_roar_3.sound,					(long) 684);
		SoundManager.time.put(sound_mobs_zm_roar_4.sound,					(long) 2126);
		SoundManager.time.put(sound_mobs_zm_roar_5.sound,					(long) 804);
		SoundManager.time.put(sound_mobs_dead_1.sound,						(long) 1223);
		
		SoundManager.time.put(sound_bullet_hit_1.sound,						(long) 300);
		
	}
	
	public static class SoundSource {
		private Sound sound;

		public SoundSource() { }
		
		/** Set the sound instance. */
		private void setAsset(Sound sound) {
			this.sound = sound;
		}
		
		/** Return the sound instance. */
		public Sound getSound() {
			return this.sound;
		}
	}
}
