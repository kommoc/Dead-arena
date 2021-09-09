package net.kommocgame.src.DeadArena.entity.mobs;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import net.kommocgame.src.Loader;
import net.kommocgame.src.entity.AI.Requerements.ZombieAI;
import net.kommocgame.src.world.World;

public class DAEntityMobZombieFast extends DAEntityMobZombie {

	public DAEntityMobZombieFast(World world, SpriteBatch batch, float x, float y) {
		super(world, batch, x, y);

		this.setShapeSize(1.0f);
		this.setTexture(Loader.objectsMobs("zombie_easy/icon_zombie_fast.png"), 1.25f);
		this.compSprite.sprite.setOriginCenter();
		this.setHP(60);
		this.setMaxLinearSpeed(100f);
		this.setMaxLinearAcceleration(100f);
		this.setMaxAngularSpeed(7.5f);
		this.setMaxAngularAcceleration(7.5f);
		
		this.setFieldOfView(230f, 15);
		
		right_attack	= 325;
		left_attack		= 370;
		
		distance_attack = 3f;
		
		//((ZombieAI) getAI().getRequerements().get(1)).setAttackRate(1500l);
		//((ZombieAI) getAI().getRequerements().get(1)).setAttackDistacne(3.3f);
		//((ZombieAI) getAI().getRequerements().get(1)).setConvergenceDistacne(4f);
		
		sprite_dead = Loader.impact("zm_fast_dead.png");
	}
	
	public void setAttackingStats(boolean par1) {
		super.setAttackingStats(par1);
		if(par1) {
			this.setMaxLinearSpeed(45f);
			this.setMaxLinearAcceleration(45f);
			this.setMaxAngularSpeed(3.5f);
			this.setMaxAngularAcceleration(3.5f);
		} else {
			this.setMaxLinearSpeed(100f);
			this.setMaxLinearAcceleration(100f);
			this.setMaxAngularSpeed(7.5f);
			this.setMaxAngularAcceleration(7.5f);
		}
	}

}
