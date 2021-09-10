package net.kommocgame.src.DeadArena.render;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ai.utils.ArithmeticUtils;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.brashmonkey.spriter.Player;
import com.brashmonkey.spriter.gdx.Drawer;

import net.kommocgame.src.Game;
import net.kommocgame.src.DeadArena.entity.mobs.DAEntityMobZombieFast;
import net.kommocgame.src.render.RenderEngine;

public class RenderDAEntityMobZombieFast extends RenderDAEntityMobZombie {
	
	public RenderDAEntityMobZombieFast(DAEntityMobZombieFast par1, SpriteBatch batch, Drawer drawer) {
		super(par1, batch, drawer);
		entityLiving = par1;
		
		sprite_player = new Player(RenderEngine.getSCMLData("zombie_fast"));
		sprite_player.setScale(Game.SCALE_WORLD_VALUE_FINAL);
		sprite_player.setAnimation("idle_agro");
		
		sprite_boots = new Player(RenderEngine.getSCMLData("zombie_fast_feet"));
		sprite_boots.setScale(Game.SCALE_WORLD_VALUE_FINAL);
		
		sprite_player.setPosition(entityLiving.getPosition().x, entityLiving.getPosition().y);
		sprite_player.setScale(Game.SCALE_WORLD_VALUE_FINAL * 0.53f);
		sprite_boots.setScale(Game.SCALE_WORLD_VALUE_FINAL * 0.55f);
		sprite_boots.setPosition(entityLiving.getPosition().x, entityLiving.getPosition().y);
		
		//if(sprite_boots.characterMaps == null)
		//	sprite_boots.characterMaps = new Entity.CharacterMap[2];
		
		//sprite_boots.characterMaps[0] = sprite_boots.getEntity().getCharacterMap("Zm_fat");
	}
	
	@Override
	public void render(OrthographicCamera camera) {
		if(!frustumCheck(camera))
			return;
		
		sprite_player.setAngle(entityLiving.getRotation() - 90f);
		sprite_player.setPosition(entityLiving.getPosition().x, entityLiving.getPosition().y);
		
		sprite_boots.setPosition(entityLiving.getPosition().x, entityLiving.getPosition().y);
		
		sprite_player.update();
		sprite_boots.update();
		
		if(entityLiving.getLinearVelocity().len() > 1f) {
			if(lastTimeLine == -1)
				if(entityLiving.getLinearVelocity().len() < 4f) {
					if(!sprite_player.getAnimation().name.equals("walking_agro"))
						sprite_player.setAnimation("walking_agro");
					sprite_player.setTime(sprite_boots.getTime());
				} else {
					if(!sprite_player.getAnimation().name.equals("running_agro"))
						sprite_player.setAnimation("running_agro");
					sprite_player.setTime(sprite_boots.getTime());
				}
			
			dif_angle = ArithmeticUtils.wrapAngleAroundZero(entityLiving.getRotation() * MathUtils.degRad
					- entityLiving.getLinearVelocity().angle() * MathUtils.degRad)  * MathUtils.radDeg;
			
			if(Math.abs(dif_angle) > 45f && Math.abs(dif_angle) < 135f) {
				sprite_boots.setAngle(dif_angle > 0 ? entityLiving.getLinearVelocity().angle() : entityLiving.getLinearVelocity().angle() + 180f);
				
				int anim_time = sprite_boots.getTime();
				if(!sprite_boots.getAnimation().name.equals("walking_right")) {
					if(entityLiving.getLinearVelocity().len() > 4f) {
						if(!sprite_boots.getAnimation().name.equals("running_forward"))
							sprite_boots.setAnimation("running_forward");
					} else {
						if(!sprite_boots.getAnimation().name.equals("walking_forward"))
							sprite_boots.setAnimation("walking_forward");
					}
					
					sprite_boots.setTime(anim_time);
				}
			} else {
				sprite_boots.setAngle(Math.abs(dif_angle) < 135f ? entityLiving.getLinearVelocity().angle() - 90f :
					entityLiving.getLinearVelocity().angle() + 90f);
				
				int anim_time = sprite_boots.getTime();
				if(!sprite_boots.getAnimation().name.equals("walking_forward") || 
						!sprite_boots.getAnimation().name.equals("running_forward")) {
					if(entityLiving.getLinearVelocity().len() > 4f) {
						if(!sprite_boots.getAnimation().name.equals("running_forward"))
							sprite_boots.setAnimation("running_forward");
					} else {
						if(!sprite_boots.getAnimation().name.equals("walking_forward"))
							sprite_boots.setAnimation("walking_forward");
					}
					
					sprite_boots.setTime(anim_time);
				}
			}
		} else {
			dif_angle = ArithmeticUtils.wrapAngleAroundZero(entityLiving.getRotation() * MathUtils.degRad
					- sprite_boots.getAngle() * MathUtils.degRad)  * MathUtils.radDeg;
			
			sprite_boots.setAngle(dif_angle - 90f > 45f ? entityLiving.getRotation() - 45f - 90f : dif_angle - 90f < -45f
					? entityLiving.getRotation() + 45f - 90f : sprite_boots.getAngle());
			if(!sprite_boots.getAnimation().name.equals("stay_idle"))
				sprite_boots.setAnimation("stay_idle");
			
			if(lastTimeLine == -1)
				if(!sprite_player.getAnimation().name.equals("idle_agro"))
					sprite_player.setAnimation("idle_agro");
		}
		
		if(entityLiving.isAttackRight()) {
			if(!sprite_player.getAnimation().name.equals("attack_right"))
				sprite_player.setAnimation("attack_right");
			
			if(lastTimeLine < sprite_player.getTime())
				lastTimeLine = sprite_player.getTime();
			
			if(lastTimeLine > sprite_player.getTime()) {
				lastTimeLine = -1;
				entityLiving.setAttackingStats(false);
			}
		} else if(entityLiving.isAttackLeft()) {
			if(!sprite_player.getAnimation().name.equals("attack_left"))
				sprite_player.setAnimation("attack_left");
			
			if(lastTimeLine < sprite_player.getTime())
				lastTimeLine = sprite_player.getTime();
			
			if(lastTimeLine > sprite_player.getTime()) {
				lastTimeLine = -1;
				entityLiving.setAttackingStats(false);
			}
		}
		//System.out.println("sprite_player.getAnimation().id:" + sprite_player.getTime());
		//if(sprite_boots.getTime() != sprite_player.getTime());
		//	sprite_boots.setTime(sprite_player.getTime());
		//System.out.println("sprite_player.speed:" + sprite_player.speed);
		sprite_player.speed = (int) (15f / (1f/60f / Gdx.graphics.getDeltaTime()));
		sprite_boots.speed = (int) (15f / (1f/60f / Gdx.graphics.getDeltaTime()));
		
		drawer.draw(sprite_boots);
		drawer.draw(sprite_player);
	}
}
