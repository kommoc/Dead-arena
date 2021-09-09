package net.kommocgame.src.entity.AI.task;

import com.badlogic.gdx.ai.steer.Limiter;
import com.badlogic.gdx.ai.steer.behaviors.Alignment;
import com.badlogic.gdx.ai.steer.behaviors.Arrive;
import com.badlogic.gdx.ai.steer.behaviors.BlendedSteering;
import com.badlogic.gdx.ai.steer.behaviors.LookWhereYouAreGoing;
import com.badlogic.gdx.ai.steer.behaviors.Pursue;
import com.badlogic.gdx.ai.steer.behaviors.Seek;
import com.badlogic.gdx.ai.steer.behaviors.Separation;
import com.badlogic.gdx.ai.steer.behaviors.Wander;
import com.badlogic.gdx.ai.steer.limiters.LinearLimiter;
import com.badlogic.gdx.ai.utils.Location;
import com.badlogic.gdx.math.Vector2;

import net.kommocgame.src.Game;
import net.kommocgame.src.entity.EntityLiving;
import net.kommocgame.src.entity.AI.GraphPath;
import net.kommocgame.src.entity.AI.LocImp;
import net.kommocgame.src.world.World;

public class TaskTest extends Task {
	
	private Seek<Vector2> seek;
	private Alignment<Vector2> alignment;
	private Location<Vector2> target;
	private LinearLimiter limiter;
	private BlendedSteering<Vector2> steerB;
	
	GraphPath path;
	
	public TaskTest(EntityLiving entity, int priority, Vector2 vec) {
		super(entity, priority);
		target = new LocImp(vec);
		
		seek = new Seek<Vector2>(entity, target);
		alignment = new Alignment<Vector2>(entity, entity.getFieldOfView());
		
		limiter = new LinearLimiter(entity.getMaxLinearAcceleration(), entity.getMaxLinearSpeed());
		seek.setLimiter(limiter);
		seek.setEnabled(true);
		
		alignment.setEnabled(true);
		
		Limiter limiter_entity = entity;
		steerB = new BlendedSteering<Vector2>(entity);
		//steerB.add(new Arrive<Vector2>(entity, Game.getPlayer()), 0.7f);
		//steerB.add(new Pursue<Vector2>(entity, Game.getPlayer()), 0.3f);
		//Wander wander = new Wander(entity);
		//wander.setTarget(Game.getPlayer()).setWanderRadius(5f);
		//steerB.add(wander, 1f);
		//steerB.add(new Arrive(entity, Game.getPlayer()), 1f);
		//LookWhereYouAreGoing look = new LookWhereYouAreGoing(entity);
		//steerB.add(look, 1f);
		//Separation separ = new Separation(entity, entity.getFieldOfView());
		//separ.setDecayCoefficient(60f);
		//steerB.add(separ, 1f);
		steerB.add(new CollisionAvoid(entity), 1f);
		//Face face = new Face(entity, Game.getPlayer());
		
		steerB.setEnabled(true);
		steerB.setLimiter(limiter_entity);
	}

	@Override
	public void execute(World world, EntityLiving entity, float deltaTime) {
		if(entity.getBehavior() != steerB) {
			entity.setBehavior(steerB);
		}
		
		if(false)
			this.setTaskDone();
	}

}
