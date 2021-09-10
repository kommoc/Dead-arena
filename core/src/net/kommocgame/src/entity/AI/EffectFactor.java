package net.kommocgame.src.entity.AI;

public class EffectFactor {

	private float factor_move = 1f;				// Ц множитель скорости перемещени€.
	private float factor_damage = 1f;			// Ц множитель наносимого урона.
	private float factor_protection = 1f;		// Ц процент пропускаемого урона от полученного.
	
	private float factor_reloading_speed = 1f;	// Ц (»грок) множитель скорости перезар€дки.
	private int factor_additional_slot = 0;		// Ц (»грок) количество актуальных слотов.
	private int factor_max_ammopack = 0;		// Ц (»грок) количество носимого боекомплекта.
	
	private float Ai_factor_reactionSpeed = 1f;	// - множитель скорости реакции.
	private float Ai_factor_rotate = 1f;			// Ц множитель скорости поворота.
	private float Ai_factor_attackSpeed = 1f;	// - множитель скорости атаки.
	private float Ai_factor_fieldOfView = 1f;	// - множитель пол€ обзора.
	private float Ai_factor_agroRadius = 1f;		// - множитель агро-радиуса.
	private float Ai_factor_radiusFOV = 1f;		// - множитель дистанции обзора.
	
	public EffectFactor setMove(float f) {
		factor_move = f;
		return this;
	}
	
	public EffectFactor setDamage(float f) {
		factor_damage = f;
		return this;
	}
	
	public EffectFactor setProtection(float f) {
		factor_protection = f;
		return this;
	}
	
	public EffectFactor setReloadingSpeed(float f) {
		factor_reloading_speed = f;
		return this;
	}
	
	public EffectFactor setAdditionalSlot(int count) {
		factor_additional_slot = count;
		return this;
	}
	
	public EffectFactor setMaxAmmopack(int count) {
		factor_max_ammopack = count;
		return this;
	}
	
	public EffectFactor setAiRotate(float f) {
		Ai_factor_rotate = f;
		return this;
	}
	
	public EffectFactor setAiReactionSpeed(float f) {
		Ai_factor_reactionSpeed = f;
		return this;
	}
	
	public EffectFactor setAiAttackSpeed(float f) {
		Ai_factor_attackSpeed = f;
		return this;
	}
	
	public EffectFactor setAiFOV(float f) {
		Ai_factor_fieldOfView = f;
		return this;
	}
	
	public EffectFactor setAiAgroRadius(float f) {
		Ai_factor_agroRadius = f;
		return this;
	}
	
	public EffectFactor setAiRadiusFOV(float f) {
		Ai_factor_radiusFOV = f;
		return this;
	}
	
	public float get_move() {
		return factor_move;
	}
	
	public float get_damage() {
		return factor_damage;
	}
	
	public float get_protection() {
		return factor_protection;
	}
	
	public float get_reloadingSpeed() {
		return factor_reloading_speed;
	}
	
	public int get_additionalSlot() {
		return factor_additional_slot;
	}
	
	public int get_maxAmmopack() {
		return factor_max_ammopack;
	}
	
	public float get_AiRotate() {
		return Ai_factor_rotate;
	}
	
	public float get_AiReactionSpeed() {
		return Ai_factor_reactionSpeed;
	}
	
	public float get_AiAttackSpeed() {
		return Ai_factor_attackSpeed;
	}
	
	public float get_AiFov() {
		return Ai_factor_fieldOfView;
	}
	
	public float get_AiAgroRadius() {
		return Ai_factor_agroRadius;
	}
	
	public float get_AiRadiusFOV() {
		return Ai_factor_radiusFOV;
	}
	
	public EffectFactor set(float f) {
		factor_move = f;
		factor_damage = f;
		factor_protection = f;
		
		Ai_factor_reactionSpeed = f;
		Ai_factor_rotate = f;
		Ai_factor_attackSpeed = f;
		Ai_factor_fieldOfView = f;
		Ai_factor_agroRadius = f;
		Ai_factor_radiusFOV = f;	
		
		return this;
	}
}
