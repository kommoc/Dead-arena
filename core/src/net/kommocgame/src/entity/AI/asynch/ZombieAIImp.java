package net.kommocgame.src.entity.AI.asynch;

import java.util.Random;

import com.badlogic.gdx.ai.steer.behaviors.Arrive;
import com.badlogic.gdx.ai.steer.behaviors.BlendedSteering;
import com.badlogic.gdx.ai.steer.behaviors.Pursue;
import com.badlogic.gdx.ai.utils.Location;
import com.badlogic.gdx.math.Vector2;

import net.kommocgame.src.entity.AI.GraphPath;
import net.kommocgame.src.entity.AI.LocImp;
import net.kommocgame.src.entity.AI.Node;
import net.kommocgame.src.entity.AI.asynch.actions.ActionAttack;
import net.kommocgame.src.entity.AI.asynch.actions.EAttack;
import net.kommocgame.src.entity.AI.task.CollisionAvoid;
import net.kommocgame.src.entity.AI.task.EnumTaskPriority;


public class ZombieAIImp extends RequerementImp {
	
	public static final int STATE_IDLE		= 0;
	public static final int STATE_SEARCH	= 1;
	public static final int STATE_PURSUE	= 2;
	public static final int STATE_ATTACK	= 3;
	
	private int state = STATE_IDLE;
	
	private float distance_attack = 3f;
	private float distance_convergence = 6f;
	
	private long attack_rate = 2000l;
	private long lastTime = 0;
	
	private Location<Vector2> TARGET;
	
	private TaskSearch task_search;
	private TaskPursue task_pursue;
	//private TaskSearch task_attack;
	
	private final RaycastRequest rr_target;
	private final RaycastRequest rr_nodePos;
	
	/**********************************************************/
	
	private Vector2 node_pos			= new Vector2();
	private Vector2 node_pos1			= new Vector2();
	private Vector2 vector_currPosition	= new Vector2();;
	
	
	BlendedSteering<Vector2> steering;
	Arrive<Vector2> arrive;
	CollisionAvoid collisionAvoid;
	Pursue<Vector2> pursue;
	
	Location<Vector2> target;
	
	public ZombieAIImp(SteerableImp entity) {
		super(entity);
		
		steering		= new BlendedSteering<Vector2>(owner);
		arrive			= new Arrive<Vector2>(owner);
		collisionAvoid	= new CollisionAvoid(owner);
		pursue			= new Pursue<Vector2>(owner, null);
		
		steering.add(arrive, 1f);
		steering.add(collisionAvoid, 0.0f);
		steering.add(pursue, 0.3f);
		
		arrive.setEnabled(true);
		collisionAvoid.setEnabled(true);
		
		rr_target = new RaycastRequest();
		rr_nodePos = new RaycastRequest();
		entity.getRaycast_list().add(rr_target);
		entity.getRaycast_list().add(rr_nodePos);
	}

	@Override
	public void update() {
		setState(STATE_IDLE);
		/*
		if(TARGET != null) {
			if(owner.getFieldOfView().checkPoint(TARGET.getPosition())) {
				pursueTarget(TARGET);
				//System.out.println("ZombieAI.update() ### dst: " + owner.getPosition().dst(target.getPosition()));
				if(owner.getPosition().dst(TARGET.getPosition()) <= distance_convergence) {
					if(task_pursue != null) {
						
						task_pursue = null;
					}
					
					attackTarget(TARGET);
				}
			} else {
				searchTarget(TARGET);
			}
		} else if(Game.getPlayer() != null) {
			TARGET = Game.getPlayer();
		}*/
		
		if(TARGET != null) {
			rr_target.createNewRequest(owner.getPosition(), TARGET.getPosition());
			
			if(rr_target.getLastResult()) {
				pursueTarget(TARGET);
				//System.out.println("ZombieAI.update() ### dst: " + owner.getPosition().dst(target.getPosition()));
				if(owner.getPosition().dst(TARGET.getPosition()) <= distance_convergence) {
					if(task_pursue != null) {
						
						task_pursue = null;
					}
					
					attackTarget(TARGET);
				}
			} else {
				searchTarget(TARGET);
			}
		} else if(AIManager.getPlayer() != null) {
			TARGET = AIManager.getPlayer();
		}
	}
	
	public void searchTarget(Location<Vector2> target) {
		setState(STATE_SEARCH);
		
		if(task_search != null) {
			if(task_search.isFinish()) task_search = null;
		} else {
			task_search = new TaskSearch(owner, this.TARGET, EnumTaskPriority.MEDIUM.get());
			owner.getAI().getTaskList().put(task_search, EnumTaskPriority.MEDIUM.get());
		}
	}
	
	public void pursueTarget(Location<Vector2> target) {
		setState(STATE_PURSUE);
		
		if(task_pursue != null) {
			if(task_pursue.isFinish()) task_pursue = null;
		} else {
			if(!(owner.getPosition().dst(target.getPosition()) <= distance_convergence)) {
				task_pursue = new TaskPursue(owner, this.TARGET, EnumTaskPriority.HIGH.get());
				owner.getAI().getTaskList().put(task_pursue, EnumTaskPriority.HIGH.get());
			}
		}
	}
	
	public void attackTarget(Location<Vector2> target) {
		setState(STATE_ATTACK);
		
		if(lastTime + attack_rate < System.currentTimeMillis()) {
			lastTime = System.currentTimeMillis();
			Random rand = new Random();
			
			if(rand.nextBoolean()) {
				//((DAEntityMobZombie) owner).attackLeftHand(target);
				owner.getActionList().add(new ActionAttack((SteerableImp)TARGET, EAttack.ZM_LEFT_HAND.get()));
			} else {
				//((DAEntityMobZombie) owner).attackRightHand(target);
				owner.getActionList().add(new ActionAttack((SteerableImp)TARGET, EAttack.ZM_RIGHT_HAND.get()));
			}
		}
	}
	
	/** Set the entity state. */
	public void setState(int state) {
		this.state = state;
	}
	
	/** Set the main target. */
	public void setTarget(SteerableImp target) {
		this.TARGET = target;
	}
	
	public void setAttackDistacne(float distance) {
		this.distance_attack = distance;
	}
	
	public void setAttackRate(long rate) {
		this.attack_rate = rate;
	}
	
	public void setConvergenceDistacne(float distance) {
		this.distance_convergence = distance;
	}
	
	/** Return the current state of this entity. */
	public int getState() {
		return state;
	}

	@Override
	public boolean isHangry() {
		return true;
	}
	
	public class TaskSearch extends TaskAI {
		
		GraphPath path;
		Location<Vector2> targ;
		//Location<Vector2> target;
		
		int prew_x, prew_y, node_index = 0;
		
		//BlendedSteering<Vector2> steering;
		//Arrive<Vector2> arrive;
		//CollisionAvoid collisionAvoid;
		
		private long lastTime	= 0;
		private long timer		= 2000l;
		
		public TaskSearch(SteerableImp entity, Location<Vector2> target_entity, int priority) {
			super(entity, priority);
			/*steering = new BlendedSteering<Vector2>(owner);
			arrive = new Arrive<Vector2>(entity);
			
			steering.add(new CollisionAvoid(owner), 1f);
			steering.add(arrive, 1f);
			*/
			steering.get(0).setWeight(1f);
			steering.get(1).setWeight(0.8f);
			steering.get(2).setWeight(0.3f);
			pursue.setEnabled(false);
			
			targ = target_entity;
			
			//System.out.println("ZombieAI.TaskSearch(new instance) ###");
		}
		
		public TaskSearch setTimer(long time) {
			this.timer = time;
			return this;
		}
		
		private boolean targetMoved(Vector2 node_pos) {
			if(path == null || (prew_x != (int) node_pos.x || prew_y != (int) node_pos.y) && lastTime + timer < System.currentTimeMillis()) {
				lastTime = System.currentTimeMillis();
				return true;
			}
			
			return false;
		}

		@Override
		public void execute(SteerableImp entity, float deltaTime) {
			if(entity.getBehavior() != steering)
				entity.setBehavior(steering);
			
			//if(entity.getFieldOfView().checkPoint(targ.getPosition())) {
			if(rr_target.getLastResult()) {
				this.setTaskDone();
			} else {
				Node.getNodePos(targ.getPosition().x, targ.getPosition().y, node_pos);
				
				if(targetMoved(node_pos)) {
					
					entity.getPathFinder().createPath(node_pos.x, node_pos.y);
					path = entity.getPathFinder().getGraphPath();
					//System.out.println("ZombieAI.TaskSearch.execute() ### createPath");
					prew_x = (int) node_pos.x;
					prew_y = (int) node_pos.y;
					node_index = 0;
				}
				
				if(path != null) {
					if(entity.getPathFinder().isRecive()) {
						for(int i = node_index; i < path.getCount(); i++) {
							owner.worldObj.getLevel().getGridNodes().getPosNodeByIndex(path.get(node_index).getIndex(), node_pos1)
									.add(owner.worldObj.getLevel().getGridNodes().getScale() * 0.5f,
											owner.worldObj.getLevel().getGridNodes().getScale() * 0.5f);
							
							rr_nodePos.createNewRequest(owner.getPosition(), node_pos1);
							
							if(rr_nodePos.getResult()) {
								node_index++;
								vector_currPosition.set(node_pos1);
							}
							/*
							if(entity.getFieldOfView().checkPoint(node_pos1)) {
								node_index++;
								vector_currPosition.set(node_pos1);
							}*/
						}
						
						if(vector_currPosition != null) {
							if(target == null || !(target instanceof LocImp))
								target = new LocImp(vector_currPosition);
							
							target.getPosition().set(vector_currPosition);
							arrive.setTarget(target);
						}
					}
				}
			}
			
			if(arrive.getTarget() != null) {
				steering.setEnabled(true);
			} else steering.setEnabled(false);
		}
	}
	
	public class TaskPursue extends TaskAI {
		
		Location<Vector2> targ;
		//Location<Vector2> target;
		
		//BlendedSteering<Vector2> steering;
		//Arrive<Vector2> arrive;
		//Pursue<Vector2> pursue;
		
		public TaskPursue(SteerableImp entity, Location<Vector2> entity_target, int priority) {
			super(entity, priority);
			//steering = new BlendedSteering<Vector2>(owner);
			targ = entity_target;
			target = entity_target;
			
			if(entity_target != null) {
				/*
				arrive = new Arrive<Vector2>(owner, target);
				steering.add(new CollisionAvoid(owner), 0.5f);
				steering.add(arrive, 0.7f);
				
				if(entity_target instanceof EntityLiving) {
					pursue = new Pursue<Vector2>(owner, (EntityLiving) target);
					steering.add(pursue, 0.3f);
				}
				*/
				
				arrive.setTarget(target);
				if(entity_target instanceof SteerableImp) {
					pursue.setTarget((SteerableImp) target);
					pursue.setEnabled(true);
				}
				
				steering.get(0).setWeight(0.7f);
				steering.get(1).setWeight(0.5f);
				steering.get(2).setWeight(0.3f);
			}
			//System.out.println("ZombieAI.TaskPursue(new instance) ###");
		}

		@Override
		public void execute(SteerableImp entity, float deltaTime) {
			if(owner.getBehavior() != steering)
				owner.setBehavior(steering);
			
			if(rr_target.getLastResult()) {
				if(target == null) {
					setTaskCancel();
				}
				
				if(targ.getPosition().dst(owner.getPosition()) < ZombieAIImp.this.distance_convergence) {
					//deincrease velocity
				}
			} else {
				this.setTaskFail();
			}
		}
	}
}

