package net.kommocgame.src.world.physics;

import com.badlogic.gdx.physics.box2d.Fixture;

import net.kommocgame.src.entity.EntityLiving;
import net.kommocgame.src.entity.props.EntityProp;
import net.kommocgame.src.trigger.TriggerBase;
@Deprecated
public interface ITraceBody {
	
	void traceEntity(EntityLiving entity);
	void traceProp(EntityProp prop);
	void traceWall(Fixture fixture);
	void traceTrigger(TriggerBase trigger);
}
