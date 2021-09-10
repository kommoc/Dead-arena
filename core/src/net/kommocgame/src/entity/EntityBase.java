package net.kommocgame.src.entity;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.ai.utils.Location;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.utils.StringBuilder;

import net.kommocgame.src.Loader;
import net.kommocgame.src.SoundEffect;
import net.kommocgame.src.DeadArena.objects.Spawner;
import net.kommocgame.src.editor.Editable;
import net.kommocgame.src.entity.AI.Node;
import net.kommocgame.src.entity.AI.event.AIEvent;
import net.kommocgame.src.entity.AI.event.AIEventListener;
import net.kommocgame.src.entity.component.CompCamera;
import net.kommocgame.src.entity.component.CompID;
import net.kommocgame.src.entity.component.CompLayer;
import net.kommocgame.src.entity.component.CompPhysics;
import net.kommocgame.src.entity.component.CompSprite;
import net.kommocgame.src.entity.component.CompTexture;
import net.kommocgame.src.entity.component.CompVelosity;
import net.kommocgame.src.entity.props.EntityProp;
import net.kommocgame.src.layer.EnumGameLayer;
import net.kommocgame.src.render.RenderEngine;
import net.kommocgame.src.render.entity.RenderEntity;
import net.kommocgame.src.world.AdditionalData;
import net.kommocgame.src.world.DataObject;
import net.kommocgame.src.world.World;
import net.kommocgame.src.world.WorldObject;
import net.kommocgame.src.world.physics.ICollisionListener;

public class EntityBase extends Entity implements ICollisionListener, AIEventListener, WorldObject, Location<Vector2> {

	public World worldObj;
	private SpriteBatch batch;
	protected AdditionalData userData;
	private int layer_index;
	
	/** Need to control spawn. */
	private Spawner spawner_mark;
	
	public CompID compID = new CompID();				// don't work
	public CompTexture compTexture = new CompTexture();
	public CompSprite compSprite = new CompSprite();
	public CompLayer compLayer = new CompLayer();
	public CompVelosity compVelocity = new CompVelosity();
	public CompPhysics compPhysics;
	public CompCamera compCamera;
	
	protected boolean flag_ignoreDamage = false;
	protected boolean flag_ignoreCollision = false;
	
	public Texture default_tex = Loader.objectsUnits("img_def_1.png");
	private int default_HP = 100;
	private int hp = default_HP;
	private int max_hp = 100;
	
	private float default_sprite_width;
	private float default_sprite_height;
	
	public float SPRITE_SCALE = 1f;
	
	private boolean isDeactive = false;
	private boolean isDeleted = false;
	private boolean isDead = false;
	
	private boolean render_sprite = true;
	
	public EntityBase(World world, SpriteBatch batch) {
		this(world, batch, 0, 0, null, 10);
	}
	
	public EntityBase(World world, SpriteBatch batch, float x, float y) {
		this(world, batch, x, y, null, 10);
	}
	
	public EntityBase(World world, SpriteBatch batch, float x, float y, Texture tex, int hp) {
		worldObj = world;
		compPhysics = new CompPhysics(worldObj.physics, x, y);
		userData = new AdditionalData();
		this.setLayerIndex(10);
		
		this.add(compID);
		this.add(compTexture);
		this.add(compSprite);
		this.add(compLayer);
		this.add(compPhysics);
		this.add(compVelocity);
		
		this.batch = batch;
		
		compPhysics.setCircle(this, 0.64f, BodyType.DynamicBody);
		
		if(tex != null) {
			//compTexture.path = tex.getTextureData().getFormat().name();
			compTexture.texture = tex;
		} else 
			compTexture.texture = default_tex;
		
		hp = default_HP;
		compSprite.sprite = new Sprite(compTexture.texture);
		getSprite().setSize(getSprite().getWidth() / 100f, getSprite().getHeight() / 100f);
		
		default_sprite_width = getSprite().getWidth();
		default_sprite_height = getSprite().getHeight();
		
		this.spriteToCenter();
		
		compSprite.setRender(this);
		compLayer.setEnum(EnumGameLayer.Entity);
		
		getAdditionalData().getParameters().add(new DataObject<Integer>("HP", getHP()) {
			@Override
			public Integer getParameter() {
				return getHP();
			}
			
			@Override
			public void setParameter(Integer par1) {
				setHP(par1);
			}
		});
		
		getAdditionalData().getParameters().add(new DataObject<Integer>("layer_index", getLayerIndex()) {
			@Override
			public void setParameter(Integer par1) {
				setLayerIndex(par1);
			}

			@Override
			public Integer getParameter() {
				return getLayerIndex();
			}
		});
		
		getAdditionalData().getParameters().add(new DataObject<Float>("sprite_size", getSprite().getScaleX()) {
			@Override
			public void setParameter(Float par1) {
				getSprite().setScale(par1);
			}

			@Override
			public Float getParameter() {
				return getSprite().getScaleX();
			}
		});
	}
	
	public boolean canCollide() {
		return compPhysics.canCollide;
	}
	
	public boolean isDead() {
		return isDead = hp > 0 ? false : true;
	}
	
	@Editable
	public void setCollision(boolean par1) {
		compPhysics.canCollide = par1;
	}
	
	public void spriteToCenter() {
		getSprite().setOriginCenter();
	}
	
	@Editable
	public void setPosition(float x, float y) {
		compPhysics.body.setPosition(x, y);
		getSprite().setPosition(x - getSprite().getOriginX(), y - getSprite().getOriginY());
	}
	
	@Editable
	public void setRotation(float angle) {
		compPhysics.body.setRotation(angle);
		getSprite().setRotation(angle);
	}
	
	public void setLinearImpulse(Vector2 vec) {
		this.getDefinition().getBody().getLinearVelocity().add(vec);
	}
	
	public void setLinearImpulse(float x, float y) {
		this.getDefinition().getBody().getLinearVelocity().add(x, y);
	}
	
	@Editable
	public void setLayer(EnumGameLayer layer) {
		compLayer.setEnum(layer);
	}
	
	/** Set the size of sprite. */
	public void setScale(float ratio) {
		SPRITE_SCALE = ratio;
		getSprite().setSize(default_sprite_width * ratio, default_sprite_height * ratio);
	}
	
	/** Set's the physic size. */
	public void setShapeSize(float par1) {
		compPhysics.body.definition.getFixtureList().get(0).getShape().setRadius(par1);
	}
	
	/** Set the main part of body. */
	public void setTexture(Texture tex, float ratio) {
		getSprite().setTexture(tex);
		this.setScale(ratio);
		//compSprite.sprite.setScale(Game.SCALE_WORLD_VALUE * ratio);
	}

	/** Parameter is need for Raycast. If transparent false raycast should not across that entity.*/
	public void setTransparent(boolean par1) {
		compPhysics.isTransparent = par1;
	}
	
	/** Parameter responding for render in world. */
	public void setRenderSprite(boolean par1) {
		render_sprite = par1;
	}
	
	public void setDead() {
		hp = -1;
		isDead = true;
	}
	
	@Editable
	public void setCollideWithEntity(boolean par1) {
		compPhysics.canCollideWithEntity = par1;
	}
	
	@Editable
	public void setCollideWithFixture(boolean par1) {
		compPhysics.canCollideWithFixture = par1;
	}
	
	@Override
	public void setLayerIndex(int par1) {
		layer_index = par1;
	}
	
	public void setMaxHP(int par1) {
		this.max_hp = par1;
		
		if(hp > max_hp)
			hp = max_hp;
	}
	
	@Editable
	public void setHP(int par1) {
		hp = par1;
	}
	
	/** Set the spawner mark. */
	public EntityBase setSpawnerMark(Spawner spawner) {
		this.spawner_mark = spawner;
		return this;
	}
	
	public void damage(int damage) {
		hp -= damage;
	}
	
	public int heal(int par1) {
		if(!isDead) {
			if(hp + par1 > getMaxHP())
				hp = getMaxHP();
			else hp += par1;
		}
		
		return getHP(); 
	}
	
	public int getHP() {
		return hp;
	}
	
	public int getMaxHP() {
		return max_hp;
	}
	
	public Vector2 getPosition() {
		return compPhysics.body.getPosistion();
	}
	
	public SpriteBatch getSpriteBatch() {
		return batch;
	}
	
	public boolean isTransparent() {
		return compPhysics.isTransparent;
	}
	
	/** Return the entity sprite. */
	public Sprite getSprite() {
		return compSprite.sprite;
	}
	
	/** Return the current node such entity stay at. */
	public Node getEntityNode() {
		return worldObj.getLevel().getGridNodes() != null ? Node.getNodeByPos(worldObj.getLevel().getGridNodes(), getPosition().x,
				getPosition().y) : null;
	}
	
	public AdditionalData getAdditionalData() {
		return userData;
	}
	
	/** Return the current fixture. */
	public Fixture getDefinition() {
		return compPhysics.body.definition.getFixtureList().get(0);
	}
	
	/** Get the physic size. */
	public float getShapeSize() {
		return compPhysics.body.definition.getFixtureList().get(0).getShape().getRadius();
	}
	
	/** Return rotation in degrees. */
	public float getRotation() {
		return compPhysics.body.getRotation();
	}
	
	@Override
	public int getLayerIndex() {
		return layer_index;
	}
	
	public Vector2 getLinearVelocity() {
		float x = 0f, y = 0f;
		if(compPhysics.body.definition.getLinearVelocity().x > 0.0000001f)
			x = compPhysics.body.definition.getLinearVelocity().x;
		if(compPhysics.body.definition.getLinearVelocity().y > 0.0000001f)
			y = compPhysics.body.definition.getLinearVelocity().y;
		
		//return new Vector2(x, y);
		return compPhysics.body.definition.getLinearVelocity();
	}
	
	public boolean getFlagImmunity() {
		return this.flag_ignoreDamage;
	}
	
	/** Return boolean such responding for render object. */
	public boolean isSpriteRendering() {
		return render_sprite;
	}
	
	public void onUpdate(float par1) { }
	
	/** Delete entity from the world. */
	private void deleteEntity() {
		if(isDeleted()) {
			System.out.println("	EntityBase.deleteEntity() ### trying to delete deleted instance.");
			return;
		}
		
		worldObj.getEngine().removeEntity(this);
		compPhysics.body.worldObj.getBox2d().destroyBody(compPhysics.body.definition);
		isDeleted = true;
		
		if(spawner_mark != null)
			spawner_mark.current_entity_count--;
	}
	
	/** This method calls when entity is dead. */
	public void deactiveEntity() {
		if(!isDeactive && getDefinition() != null && worldObj.getEngine().getEntities().contains(this, false)) {
			isDeactive = true;
			System.out.println("	" + this.getClass().getSimpleName() + ": is deactivated.");
			
			getDefinition().getBody().setActive(false);
			this.deleteObject();
		}
	}
	
	/** Return false if the entity is alive. */
	public boolean isDeactivated() {
		return isDeactive;
	}
	
	@Override
	public boolean isDeleted() {
		return isDeleted;
	}
	
	public void playSoundAtEntity(SoundEffect sound) {
		worldObj.addSoundToWorld(sound);
	}
	
	public boolean shouldCollideWithEntity(EntityBase entity) {
		return compPhysics.canCollideWithEntity;
	}
	
	public boolean shouldCollideWithFixture(Fixture fixture) {
		return compPhysics.canCollideWithFixture;
	}
	
	public void onCollideWithEntity(EntityBase entity) {}
	
	public void onCollideWithFixture(Fixture entity) {}
	
	public void onDead() { }
	
	public String toString() {
		return new StringBuilder().append("Entity: ").append(this.getClass().getName()).toString();
	}

	@Override
	public void handleEvent(AIEvent event) { }

	@Override
	public void deleteObject() {
		worldObj.getDeleteList().add(this);
	}

	@Override
	public void del() {
		this.deleteEntity();
	}

	@Override
	public float getOrientation() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void setOrientation(float orientation) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public float vectorToAngle(Vector2 vector) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public Vector2 angleToVector(Vector2 outVector, float angle) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Location<Vector2> newLocation() {
		return this;
	}
}
