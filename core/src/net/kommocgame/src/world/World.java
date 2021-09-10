package net.kommocgame.src.world;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ai.GdxAI;
import com.badlogic.gdx.ai.msg.MessageDispatcher;
import com.badlogic.gdx.ai.msg.MessageManager;
import com.badlogic.gdx.ai.pfa.indexed.IndexedAStarPathFinder;
import com.badlogic.gdx.ai.steer.Steerable;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactFilter;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.badlogic.gdx.physics.box2d.QueryCallback;
import com.badlogic.gdx.physics.box2d.RayCastCallback;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ArrayMap;
import com.badlogic.gdx.utils.DataInput;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.Pool;
import com.badlogic.gdx.utils.StreamUtils;

import box2dLight.PointLight;
import net.kommocgame.src.Game;
import net.kommocgame.src.GameState;
import net.kommocgame.src.SoundEffect;
import net.kommocgame.src.SoundManager.SoundSource;
import net.kommocgame.src.control.InputHandler;
import net.kommocgame.src.editor.objects.EObject;
import net.kommocgame.src.entity.EntityBase;
import net.kommocgame.src.entity.EntityFX;
import net.kommocgame.src.entity.EntityLiving;
import net.kommocgame.src.entity.AI.Node;
import net.kommocgame.src.entity.AI.asynch.AIManager;
import net.kommocgame.src.entity.AI.asynch.IEntityRequest;
import net.kommocgame.src.entity.AI.event.AIEvent;
import net.kommocgame.src.entity.component.CompCamera;
import net.kommocgame.src.entity.component.CompPhysics;
import net.kommocgame.src.entity.particle.EntityProjectile;
import net.kommocgame.src.entity.props.EntityProp;
import net.kommocgame.src.layer.EnumGameLayer;
import net.kommocgame.src.trigger.TriggerBase;
import net.kommocgame.src.world.level.LevelBase;
import net.kommocgame.src.world.physics.Box2dWorld;
import net.kommocgame.src.world.physics.ICollisionListener;
import net.kommocgame.src.world.physics.ITraceBody;

public class World extends EntitySystem implements ContactListener, ContactFilter, RayCastCallback, QueryCallback/*, Telegraph */{
	
	private ArrayMap<Long, SoundEffect>	sound_world_list = new ArrayMap<Long, SoundEffect>();
	private final Pool<SoundEffect>		pool_sound_world = new Pool<SoundEffect>() {
		@Override
		protected SoundEffect newObject() {
			return new SoundEffect();
		}
	};
	
	private Array<Steerable<Vector2>> 	steerable_world_list = new Array<Steerable<Vector2>>();
	private Array<WorldObject> 			TODELETE_objects_list = new Array<WorldObject>();
	
	/** Scheduler-calculator AI. */
	//private SchedulerAI scheduler_AI;
	
	private AIManager manager_ai;
	private RayManager manager_raycast;
	
	/** List have once-cycle particle. */
	private Array<EntityFX> particle_world_list = new Array<EntityFX>();
	private Array<EntityFX> emitter_world_list = new Array<EntityFX>();
	
	private Array<EntityProjectile> projectile_world_list = new Array<EntityProjectile>();
	private final Pool<EntityProjectile> pool_projectile_world = new Pool<EntityProjectile>() {
		@Override
		protected EntityProjectile newObject() {
			return new EntityProjectile();
		}
	};
	
	//public BulletRaycast bulletRayHandler;
	
	private ITraceBody iTraceBody;
	
	/** Callback for EntityBase. */
	private boolean callback_e = false;
	/** Instance of object callback result. */
	private Object callback_object = null;
	private Class callback_object_type;
	
	private final long CALLBACK_TIME = 30l;
	
	public Game game;
	public Box2dWorld physics;
	public LevelBase level = null;
	public MessageDispatcher message_handler;
	//public PathFinderQueue<Node> pathFinder_queue;
	public IndexedAStarPathFinder<Node> IASPF;
	private WorldHelper worldHelper;
	private LevelManager level_manager;
	
	/** Global world time. */
	public long world_tick = 0l;
	
	PointLight light;
	
	private static World instance;
	
	public World(Game game) {
		super();
		instance = this;
		this.game = game;
		physics = new Box2dWorld();
		message_handler = MessageManager.getInstance();
		worldHelper = new WorldHelper(this);
		
		//scheduler_AI = new SchedulerAI(15);
		manager_ai = new AIManager();
		manager_raycast = new RayManager(this);
		level_manager = new LevelManager(this);
		//bulletRayHandler = new BulletRaycast(this);
	}
	
	public World(int priority) {
		super(priority);
	}
	
	public void setLevel(LevelBase level) {
		level_manager.setLevel(level);
	}
	
	public void deleteLevel() {
		level_manager.deleteLevel();
	}
	
	/** Add any EntityBase into world. */
	public void addEntityIntoWorld(EntityBase entity) {
		this.getEngine().addEntity(entity);
		
		if(entity instanceof EntityLiving) {
			//scheduler_AI.addWithAutomaticPhasing(((EntityLiving) entity).control_steer, 15);
			//scheduler_AI.setRecord(((EntityLiving) entity).control_steer);
			steerable_world_list.add((Steerable<Vector2>)entity);
			message_handler.addListener(((EntityLiving)entity).getPathFinder(), 1);
			
			manager_ai.addListener((EntityLiving) entity);
		}
	}
	
	/** Add any TriggerBase into world. */
	public void addTriggerIntoWorld(TriggerBase trigger) {
		this.getEngine().addEntity(trigger);
	}
	
	/** Spawn specific projectile into world. */
	public void spawnProjectileIntoWorld(EntityProjectile projectile, float scale) {
		projectile.getCurrentParticle().scaleEffect(Game.SCALE_WORLD_VALUE_FINAL * scale);
		projectile.getCurrentParticle().reset();
		projectile.getCurrentParticle().start();
		projectile_world_list.add(projectile);
	}
	
	/** Spawn projectile into world. */
	public void spawnProjectileIntoWorld(EntityFX fx, float x, float y) {
		this.spawnProjectileIntoWorld(fx, x, y, EnumGameLayer.Entity.getPriority(), 0);
	}
	
	/** Spawn projectile into world by angle. */
	public void spawnProjectileIntoWorld(EntityFX fx, float x, float y, float angle) {
		this.spawnProjectileIntoWorld(fx, x, y, EnumGameLayer.Entity.getPriority(), angle, 1);
	}
	
	/** Spawn projectile into world by angle, scale. */
	public void spawnProjectileIntoWorld(EntityFX fx, float x, float y, float angle, float scale) {
		this.spawnProjectileIntoWorld(fx, x, y, EnumGameLayer.Entity.getPriority(), angle, scale);
	}
	
	/** Spawn projectile into world by specific layer. */
	public void spawnProjectileIntoWorld(EntityFX fx, float x, float y, int layer, float angle, float scale) {
		EntityProjectile projectile = get_projectile(fx, this, layer);
		
		projectile.getCurrentParticle().scaleEffect(Game.SCALE_WORLD_VALUE_FINAL * scale);
		projectile.setPosition(x, y);
		projectile.setRotation(angle);
		projectile.getCurrentParticle().reset();
		projectile.getCurrentParticle().start();
		projectile_world_list.add(projectile);
		
		System.out.println("	World projectile [" + "] spawned at[" + x + "; " + y + "]");
	}
	
	public void addAIEvent(AIEvent event) {
		for(int i = 0; i < getEngine().getEntities().size(); i++) {
			if(event.owner == getEngine().getEntities().get(i))
				continue;
			
			if(getEngine().getEntities().get(i) instanceof EntityBase)
				((EntityBase)getEngine().getEntities().get(i)).handleEvent(event);
		}
	}
	
	/** Add sound to world. */
	public void addSoundToWorld(SoundEffect sound) {
		this.sound_world_list.put(sound.play(game.mainCamera.position.x, game.mainCamera.position.y), sound);
	}
	
	public void update (float deltaTime) {
		//System.out.println("	World.update() ### player isn't null: " + (game.getPlayer() != null));
		message_handler.update();
		
		//profile camera_zoom synchronization
		if(Game.CORE != null && !Game.CORE.isGameRedactorActive())
			game.mainCamera.zoom = game.SCALE_WORLD_VALUE_FINAL / game.ratio() * 2f / 3f + Game.profile.settings_zoom() * game.SCALE_WORLD_VALUE_FINAL;			
		
		if(game.getGameState() == GameState.CONTINUE && projectile_world_list.size > 0) {	/// post-update projectiles for preview-render.
			for(EntityProjectile projectile : projectile_world_list) 
				projectile.update();
		}
		
		if(game.guiManager.stage != null)
			game.guiManager.stage.act(Gdx.graphics.getDeltaTime());
		
		if(game.getGameState() == GameState.CONTINUE) {
			world_tick++;
			physics.getBox2d().step(Gdx.graphics.getDeltaTime(), 6, 2);			// CHANGE 20.02.19 1arg - gdx.getDeltatime
			updateSounds();
			
			GdxAI.getTimepiece().update(Gdx.graphics.getDeltaTime());				// CHANGE 20.02.19 1arg - gdx.getDeltatime
			
			for(int i = 0; i < getEngine().getEntities().size(); ++i) {
				if(getEngine().getEntities().get(i) instanceof EntityBase) {
					EntityBase entityBase = (EntityBase)getEngine().getEntities().get(i);
					
					this.updateEntityPosition(entityBase);
					entityBase.onUpdate(Gdx.graphics.getDeltaTime());
					
					if(entityBase instanceof EntityLiving) {
						((EntityLiving)entityBase).updateAI(Gdx.graphics.getDeltaTime());
					}
					
					if(entityBase.getComponent(CompCamera.class) != null) {
						entityBase.getComponent(CompCamera.class).updateCamera(entityBase.getComponent(CompPhysics.class).body.definition.getPosition().x,
								entityBase.getComponent(CompPhysics.class).body.definition.getPosition().y);
					}
					
					if(entityBase.isDead()) {					//FIXME should call exception. (entity == null)
						//entityBase.deleteObject();
						entityBase.deactiveEntity();
					}
				} else if(getEngine().getEntities().get(i) instanceof TriggerBase) {
					TriggerBase trigger = (TriggerBase)getEngine().getEntities().get(i);
					
					trigger.update();
				}
			}
		}
		
		if(getLevel() != null && getLevel().getGridNodes() != null) {
			//System.out.println("GDRIIDIDIDI");
		}
		
		if(Game.CORE != null)
			Game.CORE.updateWorldCore();
		
		updateAStar();
		DELETE_METHOD();
		updateProjectiles();
		
		//System.out.println("	World.update() ### size of particles: " + particle_world_list.size);
		//System.out.println("	World.update() ### sound_world_pool.peak: " + pool_sound_world.peak);
		//System.out.println("	World.update() ### WORLD_DELETE_LIST_SIZE: " + getDeleteList().size);
		//System.out.println("	World.update() ### WORLD_ENTITY_LIST_SIZE: " + getEngine().getEntities().size());
		/**
		if(getLevel() != null && game.getPlayer() != null) {
			RayCastCallback raycastCallback = new RayCastCallback() {
				
				@Override
				public float reportRayFixture(Fixture fixture, Vector2 point, Vector2 normal, float fraction) {
					System.out.println("SHIT");
					return 0;
				}
			};
			
			Vector2 from = new Vector2(game.getPlayer().getPosition());
			Vector2 to = new Vector2();
			game.getPlayer().angleToVector(to, game.getPlayer().getOrientation()).setLength(15f).add(from);
			
			RenderEngine.debug_world.debugLine(from, to, Color.CYAN, Color.CYAN);
			physics.rayCast(raycastCallback, from, to);
		}*/
		
		//System.out.println("	World.update() ### WORLD_LIST_PROJECTILE_SIZE: " + getProjectileList().size);
		//System.out.println("	World.update() ### WORLD_POOL_PROJECTILE_SIZE: " + getProjectilePool().peak);
		
		
	}
	
	/** Cleanup the level. */
	public void DELETE_METHOD() {
		if(getDeleteList().size > 0) {
			for(int i = 0; i < getDeleteList().size; i++) {
				if(getDeleteList().get(i) instanceof IEntityRequest) {
					IEntityRequest ientityRequest = (IEntityRequest) getDeleteList().get(i);
					
					if(ientityRequest.isTracking()) {
						System.out.println("	ENTITY IS TRACKING! " + getDeleteList().get(i).getClass().getSimpleName());
						continue;
					}
				}
					
				System.out.println("	WORLD_DELETE: " + getDeleteList().get(i).getClass().getSimpleName());
				getDeleteList().get(i).del();
				getDeleteList().removeIndex(i);
			}
		}
	}
	
	/** Calculate bounds of static entity's and fix nodes. */
	public void calcAStarGrid() {
		worldHelper.calculateLevelAStarBounds();
	}
	
	/** Load level from file. */
	protected void loadLevel(FileHandle fileHandle) {
		Json jsonReader = new Json();
		
		try {
			DataInput input_level = new DataInput(fileHandle.read());
			String json_file = StreamUtils.copyStreamToString(input_level);
			
			String json_level = json_file.substring(json_file.indexOf("#LB") + 3, json_file.indexOf("#LE"));
			String json_objects = json_file.substring(json_file.indexOf("#OB") + 3, json_file.indexOf("#OE"));
			input_level.close();
			
			//System.out.println("	World.loadLevel() ### INPUT: " + json_file);
			
			LevelBase levelBase = (LevelBase) jsonReader.fromJson(LevelBase.class, json_level);
			Array array = jsonReader.fromJson(Array.class, json_objects);
			
			//this.clearListObject();		#EDITOR_CORE
			
			//this.clearListObject();		#EDITOR_CORE
			levelBase.setWorld(this);
			levelBase.setLevelRender();
			//this.setLevel(levelBase);	//CONFLICT
			
			world_tick = 0l;
			this.level = levelBase;
			this.level.createLevel();
			
			getLevel().createAStarGrid(levelBase.grid_width, levelBase.grid_height, levelBase.grid_scale);
			System.out.println("World.loadLevel(FileHandle) ### Nodes size: " +  array.size);
			System.out.println("World.loadLevel(FileHandle) ### GRID A* width: " +  levelBase.grid_width);
			System.out.println("World.loadLevel(FileHandle) ### GRID A* height: " +  levelBase.grid_height);
			
			getLevel().setObjectsDoneLoading(true);
			for(int i = 0; i < array.size; i++) {
				//System.out.println("\n	World.loadLevel() ### array.get(i): " + array.get(i));
				EObject object = (EObject)array.get(i);
				
				object.setWorld(this);
				object.createInstance();
				
				for(int index = 0; index < object.getInstance().getAdditionalData().getParameters().size; index++) {
					DataObject data = object.getInstance().getAdditionalData().getParameters().get(index);
					//System.out.println("	World.loadLevel() ### value_name: " + data.getName() + " is contains: "
					//		+ object.getAdditionalData().getMap().containsKey(data.getName()));
					
					if(object.getAdditionalData().getMap().containsKey(data.getName())) {
						data.setParameter(object.getAdditionalData().getMap().get(data.getName()));
					}
				}
				
				object.setAdditionalData(object.getInstance().getAdditionalData());
				//this.addObject(object);		#EDITOR_CORE
			}
			
			getLevel().name = fileHandle.file().getName();
			//System.out.println("\n	World.loadLevel() ### level name: " + levelBase.getLevelName());
			
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	@Deprecated
	private void updateAStar() {
		//if(pathFinder_queue != null)
		//	pathFinder_queue.run(9999999l);
	}
	
	private void updateSounds() {
		if(this.sound_world_list.size > 0)
		for(SoundEffect sound : sound_world_list.values()) {
			sound.update(game.mainCamera.position.x, game.mainCamera.position.y);
			
			if(sound.isFinished()) {
				sound_world_list.removeValue(sound, false);
				pool_sound_world.free(sound);
			}
		}
	}
	
	private void updateProjectiles() {
		if(projectile_world_list.size > 0)
			for(EntityProjectile projectile : projectile_world_list) {
				if(projectile.getCurrentParticle().isComplete() || projectile.isReleased()) {
					projectile_world_list.removeValue(projectile, false);
					pool_projectile_world.free(projectile);
					System.out.println("	World.updateProjectiles() ### projectile free.");
				}
			}
	}
	
	private void updateEntityPosition(EntityBase entityBase) {
		//entityBase.setPosition(entityBase.compPhysics.body.definition.getPosition().x, entityBase.compPhysics.body.definition.getPosition().y);
		//XXX IS FIXME
		if(entityBase instanceof EntityLiving) {
			EntityLiving entityLiving = (EntityLiving) entityBase;
			
			if(entityLiving.behavior == null) {
				entityLiving.getDefinition().getBody().setAngularVelocity(0);
			}
		} else {
			//**********// // Changed 15.06.19 - fix the dynamic rotation.
			if(!(entityBase instanceof EntityProp && entityBase.getDefinition().getBody().getType() == BodyType.DynamicBody)) {
				entityBase.getDefinition().getBody().setAngularVelocity(0);
				entityBase.setRotation(entityBase.getRotation());
			} else {
				entityBase.getDefinition().getBody().setAngularDamping(10f);
			}
			
			//entityBase.getDefinition().getBody().setAngularVelocity(0);
			//entityBase.setRotation(entityBase.getRotation());
			
			//**********//
		}
		entityBase.compPhysics.body.definition.setLinearDamping(entityBase.compPhysics.linearDamping);		
		
		/********** DEBUG **********/ /*FIXME*/
	}
	
	public LevelBase getLevel() {
		return level;
	}
	
	public LevelManager getLevelManager() {
		return level_manager;
	}
	
	public static World getInstance() {
		return instance;
	}
	
	public Array<WorldObject> getDeleteList() {
		return TODELETE_objects_list;
	}
	
	/** Return once-used particles list. */
	public Array<EntityFX> getParticlesList() {
		return particle_world_list;
	}
	
	/** Return partical-emitter list. */
	public Array<EntityFX> getEmitterList() {
		return emitter_world_list;
	}
	
	/** Return projectile-entity list. */
	public Array<EntityProjectile> getProjectileList() {
		return projectile_world_list;
	}
	
	/** Return projectile-entity list. */
	public Pool<EntityProjectile> getProjectilePool() {
		return pool_projectile_world;
	}
	
	public AIManager getManagerAI() {
		return manager_ai;
	}
	
	public RayManager getRayManager() {
		return manager_raycast;
	}
	
	/** Return schedulerAI. */
	//public SchedulerAI getSchedulerAI() {
	//	return scheduler_AI;
	//}
	
	/** Get the soundEffect instance from pool. */
	public static SoundEffect get_sound(SoundSource soundSource, float x, float y) {
		return World.getInstance().pool_sound_world.obtain().setInitialize(soundSource, x, y);
	}
	
	/** Get the entity_projectile instance from pool. */
	public static EntityProjectile get_projectile(EntityFX loaded_effect, World world) {
		return World.get_projectile(loaded_effect, world, EnumGameLayer.Entity.getPriority());
	}
	
	/** Get the entity_projectile instance from pool. */
	public static EntityProjectile get_projectile(EntityFX loaded_effect, World world, int priority) {
		return World.getInstance().pool_projectile_world.obtain().setInitialize(loaded_effect, world, priority);
	}
	
	public void getBulletDebug(Matrix4 matrx4) {
		physics.debugRenderer.render(physics.getBox2d(), matrx4);
		//player.worldObj.physics.debugRenderer.AABB_COLOR.set(Color.RED);
		//player.worldObj.physics.debugRenderer.JOINT_COLOR.set(Color.RED);
		//player.worldObj.physics.debugRenderer.SHAPE_AWAKE.set(Color.RED);
		//player.worldObj.physics.debugRenderer.SHAPE_KINEMATIC.set(Color.RED);
		//player.worldObj.physics.debugRenderer.SHAPE_NOT_ACTIVE.set(Color.RED);
		//player.worldObj.physics.debugRenderer.SHAPE_NOT_AWAKE.set(Color.RED);
		//player.worldObj.physics.debugRenderer.SHAPE_STATIC.set(Color.RED);
		//player.worldObj.physics.debugRenderer.VELOCITY_COLOR.set(Color.RED);
	}
	
	public SpriteBatch getWorldBatch() {
		return this.game.spriteBatch;
	}
	
	public Array<Steerable<Vector2>> getSteerList() {
		return steerable_world_list;
	}
	
	/** Search first Entity fixture. */
	public Object getEntityByCursor() {
		return this.getObjectByCursor(EntityBase.class);
	}
	
	/** Search first Object fixture. */
	public <T extends Entity> T getObjectByCursor(Class _class) {
		if(getLevel() != null) {
			//if(_class == TerrainObject)
			callback_object = null;
			callback_object_type = _class;
			
			this.physics.getBox2d().QueryAABB(this, InputHandler.BOX2D_mouse_x, InputHandler.BOX2D_mouse_y,
					InputHandler.BOX2D_mouse_x, InputHandler.BOX2D_mouse_y);
			
			long curTime = System.currentTimeMillis();
			
			while(System.currentTimeMillis() < curTime + CALLBACK_TIME) {
				if(callback_e) {
					callback_e = false;
					callback_object_type = null;
					
					return (T) callback_object;
				}
			}
		} else {
			System.err.println("	World level not launch. ");
		}
		
		return null;
	}
	
	public <T> T getObjectByFixture(Fixture fixture) {
		return (T) fixture.getBody().getUserData();
	}
	
	/** Reset emitter's and particle list. */
	public void resetParticleEmitterList() {
		this.emitter_world_list.clear();
		this.particle_world_list.clear();
		this.pool_projectile_world.clear();
		this.particle_world_list.clear();
	}
	
	/************************************************* Telegram *************************************************/
	/*
	@Override
	public boolean handleMessage(Telegram msg) {
		System.out.println("	World.handleMessage() ### receive message!");
		
		if(msg.extraInfo instanceof PathFinderRequest) {
			path_bounds.add((GraphPath)((PathFinderRequest<Node>)msg.extraInfo).resultPath);
			line_count++;
			
			if(line_count == requests) {
				receive = true;
				
				//System.out.println("	World.messageHandler() ### all requests is received.");
				for(int i = 0; i < requests; i++) {
					for(int l = 0; l < path_bounds.get(i).getCount(); l++) {
						path_bounds.get(i).get(l).disableNode(getLevel().getGridNodes());
						
						//System.out.println("	World.messageHandler() ### Node[" +
						//		getLevel().getGridNodes().getNodeByIndex(path_bounds.get(i).get(l).getIndex()).getX() + ", " +
						//		getLevel().getGridNodes().getNodeByIndex(path_bounds.get(i).get(l).getIndex()).getY() + "] is disabled ID: "
						//		+ path_bounds.get(i).get(l).getIndex());
					}
				}
				
				path_bounds.clear();
			}
		}
		
		return true;
	}
	*/
	/************************************************* QueryCallback *************************************************/
	
	
	public boolean reportFixture (Fixture fixture) {
		if(iTraceBody != null) {
			if(fixture.getBody().getUserData() != null) {
				if(fixture.getBody().getUserData() instanceof EntityLiving) {
					System.out.println("Report: EntityLiving");
					iTraceBody.traceEntity((EntityLiving) fixture.getBody().getUserData());
				} else if(fixture.getBody().getUserData() instanceof EntityProp) {
					System.out.println("Report: EntityProp");
					iTraceBody.traceProp((EntityProp) fixture.getBody().getUserData());
				} else if(fixture.getBody().getUserData() instanceof TriggerBase) {
					System.out.println("Report: Trigger");
					iTraceBody.traceTrigger((TriggerBase) fixture.getBody().getUserData());
				}
			} else {
				System.out.println("Report: Fixture");
				iTraceBody.traceWall(fixture);
			}
			
			iTraceBody = null;
		}
		
		if(!callback_e) {
			callback_object = fixture.getBody().getUserData();
			System.err.println("Report entity: " + callback_object);
			System.err.println("	WORLD _isInstance: " + callback_object_type.isAssignableFrom(callback_object.getClass()));
			
			if(callback_object_type.isAssignableFrom(callback_object.getClass())) {
				callback_e = true;
				return false;
			}
		}
		
		return true;
	}
	
	public void traceQueryAABB(ITraceBody itracebody, float lowerX, float lowerY, float upperX, float upperY) {
		this.iTraceBody = itracebody;
		physics.getBox2d().QueryAABB(this, lowerX, lowerY, upperX, upperY);
	}
	
	/************************************************* RayCastCallback *************************************************/
	
	/** return -1: ignore this fixture and continue return 0: terminate the ray cast return fraction: clip the ray to this point return 1: 
	 * don't clip the ray and continue. The Vector2 instances passed to the callback will be reused for future calls so make a copy of them!
	 */
	public float reportRayFixture (Fixture fixture, Vector2 point, Vector2 normal, float fraction) {
		System.out.println("	fixture: " + fixture.getBody().getUserData() + " fraction: " + fraction);
		System.out.println("	VECTOR: " + point);
		System.out.println("	NORMAL: " + normal);
		
		return 1;
	}
	
	/************************************************* ContactListener *************************************************/
	
	/** Called when two fixtures begin to touch. */
	public void beginContact (Contact contact) {
		//System.out.println("	World.endContact() ### BEGIN.");
		
		if(contact.getFixtureA().getBody().getUserData() instanceof WorldObject) {
			WorldObject object = (WorldObject) contact.getFixtureA().getBody().getUserData();
			
			if(object.isDeleted())
				return;
		}
		
		if(contact.getFixtureA().getBody().getUserData() instanceof TriggerBase && contact.getFixtureB().getBody().getUserData() instanceof EntityBase) {
			TriggerBase tr = (TriggerBase) contact.getFixtureA().getBody().getUserData();
			tr.contactPut((EntityBase)contact.getFixtureB().getBody().getUserData());
		} else if(contact.getFixtureB().getBody().getUserData() instanceof TriggerBase && contact.getFixtureA().getBody().getUserData() instanceof EntityBase) {
			TriggerBase tr = (TriggerBase) contact.getFixtureB().getBody().getUserData();
			tr.contactPut((EntityBase)contact.getFixtureA().getBody().getUserData());
		}
	}

	/** Called when two fixtures cease to touch. */
	public void endContact (Contact contact) {
		//System.out.println("	World.endContact() ### END.");
		
		if(contact.getFixtureA().getBody().getUserData() instanceof WorldObject) {
			WorldObject object = (WorldObject) contact.getFixtureA().getBody().getUserData();
			
			if(object.isDeleted())
				return;
		}
		
		if(contact.getFixtureA().getBody().getUserData() instanceof TriggerBase && contact.getFixtureB().getBody().getUserData() instanceof EntityBase) {
			TriggerBase tr = (TriggerBase) contact.getFixtureA().getBody().getUserData();
			tr.contactDelete((EntityBase)contact.getFixtureB().getBody().getUserData());
		} else if(contact.getFixtureB().getBody().getUserData() instanceof TriggerBase && contact.getFixtureA().getBody().getUserData() instanceof EntityBase) {
			TriggerBase tr = (TriggerBase) contact.getFixtureB().getBody().getUserData();
			tr.contactDelete((EntityBase)contact.getFixtureA().getBody().getUserData());
		}
	}
	
	public void preSolve (Contact contact, Manifold oldManifold) {
		Fixture fixtureA = contact.getFixtureA();
		Fixture fixtureB = contact.getFixtureB();
		
		if(fixtureA.getBody().getUserData() != null) {
			ICollisionListener entityLiving = (ICollisionListener) fixtureA.getBody().getUserData();
			
			if(fixtureB.getBody().getUserData() instanceof EntityBase) {
				EntityBase entityOther = (EntityBase) fixtureB.getBody().getUserData();
				entityLiving.onCollideWithEntity(entityOther);
			} else
				entityLiving.onCollideWithFixture(fixtureB);
		} if(fixtureB.getBody().getUserData() != null) {
			ICollisionListener entityLiving = (ICollisionListener) fixtureB.getBody().getUserData();
			
			if(fixtureA.getBody().getUserData() instanceof EntityBase) {
				EntityBase entityOther = (EntityBase) fixtureA.getBody().getUserData();
				entityLiving.onCollideWithEntity(entityOther);
			} else 
				entityLiving.onCollideWithFixture(fixtureA);
		}
		
		if(contact.getFixtureA().getBody().getUserData() instanceof TriggerBase || contact.getFixtureB().getBody().getUserData() instanceof TriggerBase)
			contact.setEnabled(false);
	}

	public void postSolve (Contact contact, ContactImpulse impulse) {
		
	}
	
	/************************************************* ContactFilter *************************************************/
	
	@Deprecated
	public boolean shouldCollide (Fixture fixtureA, Fixture fixtureB) {
		//System.out.println("fixtureA.getUserData(): " + fixtureA.getBody().getUserData());
		//System.out.println("fixtureB.getUserData(): " + fixtureB.getBody().getUserData());
		boolean shouldCollision = true;
		EntityLiving entity;
		if(fixtureA.getBody().getUserData() != null) {
			ICollisionListener entityLiving = (ICollisionListener) fixtureA.getBody().getUserData();
			
			if(fixtureB.getBody().getUserData() instanceof EntityBase) {
				ICollisionListener entityOther = (ICollisionListener) fixtureB.getBody().getUserData();
				
				shouldCollision &= entityLiving.shouldCollideWithEntity((EntityBase) entityOther);
			} else 
				shouldCollision &= entityLiving.shouldCollideWithFixture(fixtureB);
		} if(fixtureB.getBody().getUserData() != null) {
			ICollisionListener entityLiving = (ICollisionListener) fixtureB.getBody().getUserData();
			
			if(fixtureA.getBody().getUserData() instanceof EntityBase) {
				ICollisionListener entityOther = (ICollisionListener) fixtureA.getBody().getUserData();
				//System.out.println(entityLiving.shouldCollideWithEntity((EntityLiving) entityOther));
				shouldCollision &= entityLiving.shouldCollideWithEntity((EntityBase) entityOther);
			} else 
				shouldCollision &= entityLiving.shouldCollideWithFixture(fixtureA);
		}
		
		return shouldCollision;
	}
	
	/************************************************* Entity System *************************************************/
	
	@Override
	public void addedToEngine (Engine engine) { }
	
	@Override
	public void removedFromEngine (Engine engine) { }
	
}
