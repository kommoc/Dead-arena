package net.kommocgame.src.item;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.RayCastCallback;

import net.kommocgame.src.SoundManager;
import net.kommocgame.src.entity.DamageThrowable;
import net.kommocgame.src.entity.EntityBase;
import net.kommocgame.src.entity.EntityFX;
import net.kommocgame.src.entity.particle.EntityProjectile;
import net.kommocgame.src.world.World;

public class Bullet implements RayCastCallback {	//TODO
	
	private ItemWeapon bullet_item;
	private EntityBase owner;
	private World world;
	
	private Vector2 vec_begin;
	private Vector2 vec_end;
	
	private int max_step = 50;
	private int step = 0;
	
	private EntityProjectile entity_projectile;
	
	public Bullet(ItemWeapon item, World world, EntityProjectile particle, Vector2 begin, Vector2 end) {
		this(item, world, null, particle, begin, end);
	}
	
	public Bullet(ItemWeapon item, World world, EntityBase owner, EntityProjectile particle, Vector2 begin, Vector2 end) {
		bullet_item = item;
		this.owner = owner;
		this.entity_projectile = particle;
		this.world = world;
		
		vec_begin = begin;
		vec_end = end;
		
		if(entity_projectile.getPosition().dst(vec_begin) > 0f)
			world.physics.rayCast(this, vec_begin, entity_projectile.getPosition());
		else System.out.println("	Bullet new instance() ### cheking raycast between pre-post bullet position is missed!");
		
		//RenderEngine.debug_line_start = vec_begin.cpy();
		//RenderEngine.debug_line_end = entity_particle.getPosition().cpy();
	}
	
	@Override
	public float reportRayFixture(Fixture fixture, Vector2 point, Vector2 normal, float fraction) {
		step++;
		if(entity_projectile.isDeleted() || entity_projectile.isReleased()) {
			System.out.println("	Bullet ### particle is deleted!");
			return 0;
		}
		
		//System.out.println("	Bullet ### FIXTURE: " + fixture);
		//System.out.println("	Bullet ### point: " + point);
		//System.out.println("	Bullet ### step: " + step);
		
		if(fixture.getBody().getUserData() != null && fixture.getBody().getUserData() instanceof EntityBase) {
			System.out.println("	Bullet ### :ENTITY:");
			EntityBase entity = (EntityBase)fixture.getBody().getUserData();
			
			if(!entity.getFlagImmunity() && owner != entity) {
				//entity.getDefinition().getBody().applyForceToCenter(normal.rotate(180f).setLength(bullet_item.getKnockback() * 10f), true);
				// CHANGED 30.07.19 previos this;
				
				//world.spawnProjectileIntoWorld(EntityFX.fx_blood_red_1, entity.getPosition().x, entity.getPosition().y,
				//		vec_end.cpy().sub(vec_begin).angle(), 1f + bullet_item.getDamage() / 100f);
				
				if(DamageThrowable.throwDamage(owner, entity, bullet_item.getDamage())) {
					entity.getDefinition().getBody().applyForceToCenter(normal.rotate(180f).setLength(bullet_item.getKnockback() * 10f), true);
					
					world.spawnProjectileIntoWorld(EntityFX.fx_blood_red_1, entity.getPosition().x, entity.getPosition().y,
							vec_end.cpy().sub(vec_begin).angle(), 1f + bullet_item.getDamage() / 100f);
					
					entity.playSoundAtEntity(world.get_sound(SoundManager.sound_bullet_hit_1, entity.getPosition().x, entity.getPosition().y)
							.setDistance(75f).setLinearFade(2f));

					//TODO prnetration;
					////entity_projectile.deleteObject();
					entity_projectile.setReleased();
					return 0;			// CHANGED 30.07.19 previos (1)
				} else {
					System.out.println("	Bullet ### Can't thrown damage. Continue.");
					return 1;			// CHANGED 30.07.19 previos (-1)
				}
			} else {
				System.out.println("	Bullet ### is owner || flag immunity. Continue.");
				return 1;			// CHANGED 30.07.19 previos (-1)
			}
		}
		
		return 1;
	}
}
