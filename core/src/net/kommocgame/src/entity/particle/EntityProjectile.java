package net.kommocgame.src.entity.particle;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.ParticleEmitter;
import com.badlogic.gdx.graphics.g2d.ParticleEmitter.ScaledNumericValue;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Pool.Poolable;
import com.brashmonkey.spriter.Timeline;

import net.kommocgame.src.entity.EntityFX;
import net.kommocgame.src.item.Bullet;
import net.kommocgame.src.layer.EnumGameLayer;
import net.kommocgame.src.render.RenderEngine;
import net.kommocgame.src.world.AdditionalData;
import net.kommocgame.src.world.World;
import net.kommocgame.src.world.WorldObject;

public class EntityProjectile implements WorldObject, Poolable {
	
	int game_layer;
	String name;
	World world;
	
	float angle = 0;
	
	boolean isDeleted = false;
	boolean isReleased = false;
	
	private EntityFX curr_emitter;
	private EntityFX default_emitter;
	
	Timeline.Key.Object firePoint;
	
	private Bullet bullet;
	
	private boolean isBullet;
	private Vector2 prew_pos = new Vector2(0, 0);
	
	private Vector2 begin_pos = new Vector2(0, 0);
	private float distance;
	
	private Vector2 position = new Vector2(0, 0);
	private Vector2 velocity = new Vector2(0, 0);
	
	private Vector2 offset_pos = new Vector2(0, 0);
	private float angle_offset = 0;
	
	private float emitter_scale = 1f;
	
	/******************************************************************/
	
	private boolean boolean_beginVecIsDefined = false;
	
	private boolean debug_raycast = false;
	
	public EntityProjectile() {}
	
	public EntityProjectile(EntityFX loaded_effect, World world) {
		this(loaded_effect, world, EnumGameLayer.Entity.getPriority());
	}
	
	public EntityProjectile(EntityFX loaded_effect, World world, int priority) {
		curr_emitter = new EntityFX(loaded_effect);
		default_emitter = loaded_effect;
		this.world = world;
		setLayer(priority);
		this.setPosition(0, 0);
		prew_pos.set(getPosition());
	}
	
	public EntityProjectile setInitialize(EntityFX loaded_effect, World world) {
		return setInitialize(loaded_effect, world, EnumGameLayer.Entity.getPriority());
	}
	
	public EntityProjectile setInitialize(EntityFX loaded_effect, World world, int priority) {
		curr_emitter = new EntityFX(loaded_effect);
		default_emitter = loaded_effect;
		this.world = world;
		setLayer(priority);
		this.setPosition(0, 0);
		prew_pos.set(getPosition());
		isReleased = false;
		
		return this;
	}
	
	/** Set the {@link EnumGameLayer}. */
	public void setLayer(int layer) {
		game_layer = layer;
	}
	
	/** Set's the name of this emitter. */
	public EntityProjectile setName(String name) {
		this.name = name;
		return this;
	}
	
	/** Set's the velocity at emitter. */
	public EntityProjectile setVelocity(float x, float y) {
		this.velocity.set(x, y);
		return this;
	}
	
	/** Set's the velocity at emitter. */
	public EntityProjectile setVelocity(Vector2 vec) {
		this.velocity.set(vec);
		return this;
	}
	
	public void setPosition(float x, float y) {
		curr_emitter.setPosition(x, y);
		position.set(x, y);
	}
	
	public EntityProjectile setBullet(Bullet bullet) {
		this.bullet = bullet;
		isBullet = true;
		return this;
	}
	
	/** DebugRaycast. */
	public void setDebugRaycast(boolean enable) {
		debug_raycast = enable;
	}
	
	public void setRotation(float angle) {
		this.angle = angle;
		for(int i = 0; i < curr_emitter.getEmitters().size; i++) {
			ScaledNumericValue angle_1 = curr_emitter.getEmitters().get(i).getAngle();
			ScaledNumericValue def_angle_1 = default_emitter.getEmitters().get(i).getAngle();
			
			ScaledNumericValue rot_1 = curr_emitter.getEmitters().get(i).getRotation();
			ScaledNumericValue def_rot_1 = default_emitter.getEmitters().get(i).getRotation();
			
			angle_1.setHigh(def_angle_1.getHighMin() + angle, def_angle_1.getHighMax() + angle);
			
			if(def_angle_1.getLowMin() != 0 && def_angle_1.getLowMax() != 0)
				angle_1.setLow(def_angle_1.getLowMin() + angle, def_angle_1.getLowMax() + angle);
			
			rot_1.setHigh(def_rot_1.getHighMin() + angle, def_rot_1.getHighMax() + angle);
			
			if(def_rot_1.getLowMin() != 0 && def_rot_1.getLowMax() != 0)
				rot_1.setLow(def_rot_1.getLowMin() + angle, def_rot_1.getLowMax() + angle);
			//System.out.println("	EntityFX.setRotation() ### emitter: "+ this.getEmitters().get(i).getName());
		}
		//System.out.println("	EntityFX.setRotation() ### size of emitters: "+ this.getEmitters().size);
	}
	
	/** Set position this particle to special point. */
	public EntityProjectile setToFirePoint(Timeline.Key.Object point) {
		setPosition(point.position.x + getOffsetPos().x, point.position.y + getOffsetPos().y);
		setRotation(point.angle + angle_offset);
		
		return this;
	}
	
	/** Set's the max distance how long move this effect. */
	public EntityProjectile setDistance(float dist) {
		distance = dist;
		begin_pos.set(getPosition());
		boolean_beginVecIsDefined = true;
		
		return this;
	}
	
	/** Set to free queue. */
	public void setReleased() {
		isReleased = true;
	}
	
	public void setScale(float scale) {
		curr_emitter.scaleEffect(scale);
		this.emitter_scale = scale;
	}
	
	/** Offset position. */
	public EntityProjectile offsetPos(float x, float y) {
		return this.offsetPos(new Vector2(x, y));
	}
	
	/** Offset position. */
	public EntityProjectile offsetPos(Vector2 vec) {
		this.offset_pos.x = vec.x;
		this.offset_pos.y = vec.y;
		
		return this;
	}
	
	/** Offset angle. */
	public EntityProjectile offsetAngle(float angle) {
		this.angle_offset = angle;
		return this;
	}
	
	/** Return the {@link EnumGameLayer}. */
	public int getLayer() {
		return game_layer;
	}
	
	/** Return the name of this emitter. */
	public String getName() {
		return name;
	}
	
	public float getRotation() {
		return angle;
	}
	
	public Vector2 getPosition() {
		return position;
	}
	
	public Vector2 getOffsetPos() {
		return offset_pos;
	}
	
	public float getOffsetAngle() {
		return angle_offset;
	}
	
	/** Return firepoint. */
	public Timeline.Key.Object getAttachedFirePoint() {
		return firePoint;
	}
	
	/** Return the current effect. */
	public EntityFX getCurrentParticle() {
		return curr_emitter;
	}
	
	/** Return the parent effect. */
	public EntityFX getDefaultParticle() {
		return default_emitter;
	}
	
	public float getScale() {
		return emitter_scale;
	}
	
	/** Return the state of this object (free or be taken) */
	public boolean isReleased() {
		return isReleased;
	}
	
	/** Attached this particle to special point. */
	public EntityProjectile attachToFirePoint(Timeline.Key.Object point) {
		this.firePoint = point;
		
		return this;
	}
	
	/** Need for bullet. */
	public EntityProjectile prew_position(Vector2 vec) {
		prew_pos = vec.cpy();
		return this;
	}
	
	/** Dont overrided. */
	public void draw(Batch spriteBatch, OrthographicCamera camera) {
		this.draw(spriteBatch, Gdx.graphics.getDeltaTime(), camera);
	}
	
	/** Dont overrided. */
	public void draw(Batch spriteBatch, float delta, OrthographicCamera camera) {
		if(isDeleted || isReleased)
			return;
		
		if(camera.frustum.boundsInFrustum(curr_emitter.getBoundingBox())) {
			curr_emitter.draw(spriteBatch, delta);
			//for(ParticleEmitter emitter : curr_emitter.getEmitters()) {emitter.update(delta);}
		} else for(ParticleEmitter emitter : curr_emitter.getEmitters()) {
			emitter.update(delta);
		}
	}
	
	/** Update method. */
	public void update() {
		if(isDeleted || isReleased)
			return;
		
		if(firePoint != null) {
			setPosition(firePoint.position.x + getOffsetPos().x, firePoint.position.y + getOffsetPos().y);
			setRotation(firePoint.angle + angle_offset);
		}
		
		if(!velocity.isZero()) {
			setPosition(getPosition().x + velocity.x, getPosition().y + velocity.y);
		}
		
		if(isBullet && bullet != null && !getPosition().epsilonEquals(prew_pos, 0.05f)) {
			if(getPosition().dst(prew_pos) < 2f / (1f/60f / Gdx.graphics.getDeltaTime())) {
				prew_pos = getPosition().cpy().sub(getPosition().cpy().sub(prew_pos).setLength(2f / (1f/60f / Gdx.graphics.getDeltaTime())));
			}
			
			if(prew_pos.dst(getPosition()) > 0f) {
				world.physics.rayCast(bullet, prew_pos, getPosition());
				RenderEngine.debug_world.debugLine(prew_pos.cpy(), getPosition().cpy(), Color.RED);
			} else System.out.println("	EntityProjectile.update() ### bullet raycast is missed!");
		}
		
		prew_pos.set(getPosition());
		
		if(boolean_beginVecIsDefined) {
			if(begin_pos.dst(getPosition()) > distance) {
				this.isReleased = true;
			}
		}
	}

	@Override
	public void deleteObject() {
		this.world.getDeleteList().add(this);
	}

	@Override
	public void del() {
		if(isDeleted()) {
			System.out.println("	EntityFX.del() ### trying to delete deleted instance.");
			return;
		}
		
		if(world.getProjectileList().contains(this, false)) {
			world.getProjectileList().removeValue(this, false);
			System.out.println("	EntityFX.del() ### EntityFX(particle) was deleted. ");
		}
		
		isDeleted = true;
		curr_emitter.dispose();
	}
	
	@Override
	public boolean isDeleted() {
		return isDeleted;
	}

	@Override
	public AdditionalData getAdditionalData() {
		return null;
	}

	@Override
	public int getLayerIndex() { return 0; }

	@Override
	public void setLayerIndex(int par1) {}

	@Override
	public void reset() {
		game_layer = EnumGameLayer.Default.getPriority();
		world = null;
		
		curr_emitter.dispose();
		curr_emitter = null;
		default_emitter = null;
		
		angle = 0;
		
		firePoint = null;
		bullet = null;
		isBullet = false;
		prew_pos.setZero();
		
		begin_pos.setZero();
		
		position.setZero();
		velocity.setZero();
		
		offset_pos.setZero();
		angle_offset = 0;
		
		boolean_beginVecIsDefined = false;
	}
}
