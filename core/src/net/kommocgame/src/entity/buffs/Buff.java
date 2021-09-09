package net.kommocgame.src.entity.buffs;

public class Buff {
	
	private long last_time = 0;
	private long timer = 0;
	private boolean isInfinite = false;
	
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
	
	public Buff() {
		this(1000);
	}
	
	public Buff(long timer) {
		this.timer = timer;
	}
	
	public Buff reload() {
		this.setLastTime(System.currentTimeMillis());
		return this;
	}
	
	public Buff reset() {
		factor_move = 1f;
		factor_damage = 1f;
		factor_protection = 1f;
		
		factor_reloading_speed = 1f;
		factor_additional_slot = 0;
		factor_max_ammopack = 0;
		
		Ai_factor_reactionSpeed = 1f;
		Ai_factor_rotate = 1f;
		Ai_factor_attackSpeed = 1f;
		Ai_factor_fieldOfView = 1f;
		Ai_factor_agroRadius = 1f;
		Ai_factor_radiusFOV = 1f;
		
		return this;
	}
	
	public Buff setMove(float f) {
		factor_move = f;
		return this;
	}
	
	public Buff setDamage(float f) {
		factor_damage = f;
		return this;
	}
	
	public Buff setProtection(float f) {
		factor_protection = f;
		return this;
	}
	
	public Buff setReloadingSpeed(float f) {
		factor_reloading_speed = f;
		return this;
	}
	
	public Buff setAdditionalSlot(int count) {
		factor_additional_slot = count;
		return this;
	}
	
	public Buff setMaxAmmopack(int count) {
		factor_max_ammopack = count;
		return this;
	}
	
	public Buff setAiRotate(float f) {
		Ai_factor_rotate = f;
		return this;
	}
	
	public Buff setAiReactionSpeed(float f) {
		Ai_factor_reactionSpeed = f;
		return this;
	}
	
	public Buff setAiAttackSpeed(float f) {
		Ai_factor_attackSpeed = f;
		return this;
	}
	
	public Buff setAiFOV(float f) {
		Ai_factor_fieldOfView = f;
		return this;
	}
	
	public Buff setAiAgroRadius(float f) {
		Ai_factor_agroRadius = f;
		return this;
	}
	
	public Buff setAiRadiusFOV(float f) {
		Ai_factor_radiusFOV = f;
		return this;
	}
	
	public Buff setLastTime(long lastTime) {
		this.last_time = lastTime;
		return this;
	}
	
	public Buff setInfinite() {
		isInfinite = true;
		return this;
	}
	
	public float getMove() {
		return factor_move;
	}
	
	public float getDamage() {
		return factor_damage;
	}
	
	public float getProtection() {
		return factor_protection;
	}
	
	public float getReloadingSpeed() {
		return factor_reloading_speed;
	}
	
	public int getAdditionalSlot() {
		return factor_additional_slot;
	}
	
	public int getMaxAmmopack() {
		return factor_max_ammopack;
	}
	
	public float getAiRotate() {
		return Ai_factor_rotate;
	}
	
	public float getAiReactionSpeed() {
		return Ai_factor_reactionSpeed;
	}
	
	public float getAiAttackSpeed() {
		return Ai_factor_attackSpeed;
	}
	
	public float getAiFov() {
		return Ai_factor_fieldOfView;
	}
	
	public float getAiAgroRadius() {
		return Ai_factor_agroRadius;
	}
	
	public float getAiRadiusFOV() {
		return Ai_factor_radiusFOV;
	}
	
	public long getLastTime() {
		return last_time;
	}
	
	public long getTimer() {
		return timer;
	}
	
	public boolean isInfinite() {
		return isInfinite;
	}
}
