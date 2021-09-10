package net.kommocgame.src.DeadArena.entity.mobs;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import net.kommocgame.src.Loader;
import net.kommocgame.src.entity.AI.Requerements.ZombieAI;
import net.kommocgame.src.world.World;

public class DAEntityMobZombieFat extends DAEntityMobZombie {

	public DAEntityMobZombieFat(World world, SpriteBatch batch, float x, float y) {
		super(world, batch, x, y);
		
		this.setShapeSize(1.3f);
		this.setTexture(Loader.objectsMobs("zombie_easy/icon_zombie_fat.png"), 1.25f);
		this.compSprite.sprite.setOriginCenter();
		this.setHP(195);
		this.setMaxLinearSpeed(25f);
		this.setMaxLinearAcceleration(35f);
		this.setMaxAngularSpeed(4f);
		this.setMaxAngularAcceleration(4f);
		
		this.setFieldOfView(230f, 15);
		
		right_attack	= 690;
		left_attack		= 870;
		
		distance_attack = 4f;
		
		//((ZombieAI) getAI().getRequerements().get(1)).setAttackRate(2500l);
		//((ZombieAI) getAI().getRequerements().get(1)).setAttackDistacne(3.3f);
		//((ZombieAI) getAI().getRequerements().get(1)).setConvergenceDistacne(4f);
		
		sprite_dead = Loader.impact("zm_fat_dead.png");
	}
	
	public void setAttackingStats(boolean par1) {
		super.setAttackingStats(par1);
		if(par1) {
			this.setMaxLinearSpeed(15f);
			this.setMaxLinearAcceleration(15f);
			this.setMaxAngularSpeed(1.6f);
			this.setMaxAngularAcceleration(1.6f);
		} else {
			this.setMaxLinearSpeed(25f);
			this.setMaxLinearAcceleration(35f);
			this.setMaxAngularSpeed(4f);
			this.setMaxAngularAcceleration(4f);
		}
	}

}
