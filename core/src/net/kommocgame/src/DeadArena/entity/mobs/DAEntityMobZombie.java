package net.kommocgame.src.DeadArena.entity.mobs;

import com.badlogic.gdx.ai.steer.Steerable;
import com.badlogic.gdx.ai.utils.ArithmeticUtils;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.github.czyzby.kiwi.util.gdx.scene2d.Alignment;

import net.kommocgame.src.Game;
import net.kommocgame.src.Loader;
import net.kommocgame.src.SoundManager;
import net.kommocgame.src.DeadArena.render.RenderDAEntityMobZombie;
import net.kommocgame.src.entity.DamageThrowable;
import net.kommocgame.src.entity.EntityBase;
import net.kommocgame.src.entity.EntityFX;
import net.kommocgame.src.entity.EntityLiving;
import net.kommocgame.src.entity.AI.asynch.AIManager;
import net.kommocgame.src.entity.AI.asynch.ZombieAIImp;
import net.kommocgame.src.entity.AI.asynch.actions.Action;
import net.kommocgame.src.entity.AI.asynch.actions.ActionAttack;
import net.kommocgame.src.entity.AI.asynch.actions.EAttack;
import net.kommocgame.src.world.World;
import net.kommocgame.src.world.level.TerrainObject;

public class DAEntityMobZombie extends EntityLiving {

	private Texture tex_body = Loader.objectsMobs("zombie_easy/zombie_body.png");
	private float SCALE_VALUE = 0.18f;
	private EntityBase target;

	float distance_attack = 5f;
	float max_angle_attack = 45f * MathUtils.degRad;
	private long anim_time = 0;

	private boolean isAttackingRight = false;
	private boolean isAttackingLeft = false;
	
	private boolean isAttackDone = false;
	private boolean isAttacking = false;

	int right_attack = 400;
	int left_attack = 360;

	Texture sprite_dead = Game.assetManager.get("android/assets/texture/objects/impacts/zm_dead.png");

	public DAEntityMobZombie(World world, SpriteBatch batch) {
		this(world, batch, 0, 0);
	}

	public DAEntityMobZombie(World world, SpriteBatch batch, float x, float y) {
		super(world, batch, x, y);

		//this.getAI().setGuild(Guild.ZOMBIE);

		this.setShapeSize(1f);
		this.setTexture(tex_body, 1.25f);
		this.compSprite.sprite.setOriginCenter();
		this.setHP(80);
		this.setMaxLinearSpeed(35f);
		this.setMaxLinearAcceleration(35f);
		this.setMaxAngularSpeed(4f);
		this.setMaxAngularAcceleration(4f);

		this.setFieldOfView(230f, 15);

		//this.getAI().addRequerement(new RRandomWalking(this, 5000, 10000));
		//this.getAI().addRequerement(new ZombieAI(this));
		requerements.add(ZombieAIImp.class);
	}

	@Override
	public boolean reportNeighbor(Steerable<Vector2> neighbor) {
		return true;
	}

	public void attackRightHand(EntityBase entity) {
		if(isAttacking)
			return;
		
		target = entity;
		this.isAttackingRight = true;
		setAttackingStats(true);
	}

	public void attackLeftHand(EntityBase entity) {
		if(isAttacking)
			return;
		
		target = entity;
		this.isAttackingLeft = true;
		setAttackingStats(true);
	}

	public boolean isAttackRight() {
		return isAttackingRight;
	}

	public boolean isAttackLeft() {
		return isAttackingLeft;
	}

	public void setAttackingStats(boolean par1) {
		if (par1) {
			this.setMaxLinearSpeed(18f);
			this.setMaxLinearAcceleration(18f);
			this.setMaxAngularSpeed(2f);
			this.setMaxAngularAcceleration(2f);
		} else {
			isAttackingRight = false;
			isAttackingLeft = false;
			this.setMaxLinearSpeed(35f);
			this.setMaxLinearAcceleration(35f);
			this.setMaxAngularSpeed(4f);
			this.setMaxAngularAcceleration(4f);
		}
	}

	public void onCollideWithEntity(EntityBase entity) {
		super.onCollideWithEntity(entity);
		
	}

	@Override
	public void onDead() {
		super.onDead();
		TerrainObject obj = new TerrainObject(worldObj.getLevel(), sprite_dead, this.getPosition().x,
				this.getPosition().y);
		playSoundAtEntity(worldObj.get_sound(SoundManager.sound_mobs_dead_1, getPosition().x, getPosition().y)
				.setDistance(15f).setLinearFade(2f));

		obj.setScale(Game.SCALE_WORLD_VALUE_FINAL / 1.75f * 4f);
		obj.getImage().setPosition(getPosition().x, getPosition().y, Alignment.CENTER.get());
		obj.getImage().setOrigin(Alignment.CENTER.get());
		obj.getImage().setRotation(getRotation() - 90f);
		obj.setDeleteTimer(15000l);

		worldObj.getLevel().spawnTerrainObj(obj, obj.getPosition());
	}

	@Override
	public void onUpdate(float par1) {
		super.onUpdate(par1);
		//System.out.println("DAEntityMobZombie.onUpdate() ### anim_time: " + anim_time);
		
		if (isAttackLeft()) {
			anim_time = ((RenderDAEntityMobZombie) compSprite.render).sprite_player.getTime();
			isAttacking = true;
			
			if(!((RenderDAEntityMobZombie) compSprite.render).sprite_player.getAnimation().name.equals("attack_left"))
				anim_time = 0;
			
			if (anim_time > left_attack && !isAttackDone) {
				Vector2 vectoTarget = angleToVector(new Vector2(), getOrientation());
				boolean check_angle = Math.abs(ArithmeticUtils.wrapAngleAroundZero(
						getOrientation() + 90f * MathUtils.degRad - vectoTarget.angleRad())) < max_angle_attack;

				if (target != null && getPosition().dst(target.getPosition()) < distance_attack && check_angle) {
					DamageThrowable.throwDamage(this, target, 10);
					
					worldObj.spawnProjectileIntoWorld(EntityFX.fx_blood_red_1, target.getPosition().x, target.getPosition().y,
							target.getPosition().cpy().sub(this.getPosition()).angle(), 1f);
					
					anim_time = 0;
				}
				
				isAttackDone = true;
			}
		} else if (isAttackRight()) {
			anim_time = ((RenderDAEntityMobZombie) compSprite.render).sprite_player.getTime();
			isAttacking = true;
			
			if(!((RenderDAEntityMobZombie) compSprite.render).sprite_player.getAnimation().name.equals("attack_right"))
				anim_time = 0;
			
			if (anim_time > right_attack && !isAttackDone) {
				Vector2 vectoTarget = angleToVector(new Vector2(), getOrientation());
				boolean check_angle = Math.abs(ArithmeticUtils.wrapAngleAroundZero(
						getOrientation() + 90f * MathUtils.degRad - vectoTarget.angleRad())) < max_angle_attack;

				if (target != null && getPosition().dst(target.getPosition()) < distance_attack && check_angle) {
					DamageThrowable.throwDamage(this, target, 10);
					
					worldObj.spawnProjectileIntoWorld(EntityFX.fx_blood_red_1, target.getPosition().x, target.getPosition().y,
							target.getPosition().cpy().sub(this.getPosition()).angle(), 1f);
					
					//System.out.println("DAEntityMobZombie.onUpdate() ### anim_time: " + anim_time);
					//System.out.println("DAEntityMobZombie.onUpdate() ### DAMAGE anim_time: " +
					//		((RenderDAEntityMobZombie) compSprite.render).sprite_player.getAnimation().length);
					anim_time = 0;
				}
				
				isAttackDone = true;
			}
		} else {
			isAttacking = false;
			target = null;
			isAttackDone = false;
			anim_time = 0;
		}
	}
	
	@Override
	public boolean shouldCollideWithEntity(EntityBase entity) {
		if(entity instanceof DAEntityMobZombie)
			return false;
		
		return true;
	}
	
	@Override
	public void executeAction(Action action) {
		if(action.isChecked())
			return;
		
		if(action instanceof ActionAttack) {
			ActionAttack attack_action = (ActionAttack) action;
			
			this.target = worldObj.getManagerAI().getListenersMap().get(attack_action.getTarget());
			System.out.println("DAEntityMobZombie.executeAction() ### Target: " + target);
			
			if(attack_action.id_attack == EAttack.ZM_LEFT_HAND.get())
				this.attackLeftHand(target);
			else if(attack_action.id_attack == EAttack.ZM_RIGHT_HAND.get())
				this.attackRightHand(target);
			
			action.actionDone(true);
		}
		
		action.actionDone(false);
	}
}
