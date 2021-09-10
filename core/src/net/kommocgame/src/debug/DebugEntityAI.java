package net.kommocgame.src.debug;

import com.badlogic.gdx.ai.steer.behaviors.Arrive;
import com.badlogic.gdx.ai.steer.behaviors.ReachOrientation;
import com.badlogic.gdx.ai.utils.Location;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

import net.kommocgame.src.VecUtils;
import net.kommocgame.src.entity.EntityLiving;
import net.kommocgame.src.entity.AI.GraphPath;
import net.kommocgame.src.entity.AI.LocImp;
import net.kommocgame.src.world.World;

public class DebugEntityAI {
	
	private ShapeRenderer renderer;
	private SpriteBatch batch;
	private World world;
	
	private Array<EntityLiving> entity_d_list = new Array<EntityLiving>();
	private Array<FOV> fov_list = new Array<FOV>();
	private Array<Crosshair> targetNode_list = new Array<DebugEntityAI.Crosshair>();
	private Array<Crosshair> target_list = new Array<DebugEntityAI.Crosshair>();
	private Array<Vector2> lastSeenNode_list = new Array<Vector2>();
	
	private Vector2 vector_1 = new Vector2();
	
	public DebugEntityAI(ShapeRenderer renderer, SpriteBatch batch, World world) {
		this.renderer = renderer;
		this.batch = batch;
		this.world = world;
	}
	
	public void addEntity(EntityLiving entity) {
		if(entity_d_list.contains(entity, false))
			System.err.println("	DEBUG_ENTITY_AI: entity was added.");
		else {
			entity_d_list.add(entity);
			fov_list.add(new FOV(entity));
			targetNode_list.add(new Crosshair(entity));
			target_list.add(new Crosshair(entity));
		}
	}
	
	public void removeEntity(EntityLiving entity) {
		if(entity_d_list.contains(entity, false)) {
			entity_d_list.removeValue(entity, false);
			
			for(int i = 0; i < fov_list.size; i++) {
				if(fov_list.get(i).debug_entity == entity) {
					fov_list.removeIndex(i);
					targetNode_list.removeIndex(i);
					target_list.removeIndex(i);
				}
			}
		} else
			System.err.println("	DEBUG_ENTITY_AI: that entity is not debug.");
	}
	
	public void renderShape() {
		for(int i = 0; i < entity_d_list.size; i++) {
			if(entity_d_list.get(i) == null || entity_d_list.get(i).isDead() || entity_d_list.get(i).isDeleted()) {
				removeEntity(entity_d_list.get(i));
				continue;
			}
			
			if(world.getSteerList().size == 0 || entity_d_list.size == 0 )
				return;
			
			renderer.setColor(Color.LIME);
			renderBox(entity_d_list.get(i));
			
			renderer.setColor(Color.GREEN);
			renderGraphPath(entity_d_list.get(i));
			
			renderer.setColor(Color.NAVY);
			renderTargetNode(i);
			
			renderer.setColor(Color.RED);
			renderFieldOfView(i);
			//TODO render last seen Node.
			
			renderer.setColor(Color.CYAN);
			renderTarget(i);
		}
	}
	
	public void renderBatch() {
		
	}
	
	private void renderFieldOfView(int i) {
		if(entity_d_list.get(i).getFieldOfView() != null) {
			fov_list.get(i).renderFOV(renderer);
		}
	}
	
	private void renderBox(EntityLiving entity) {
		float r = entity.getBoundingRadius();
		renderer.rect(entity.getPosition().x - r, entity.getPosition().y - r, r * 2f, r * 2f);
	}
	
	private void renderTargetNode(int i) {		
		if(targetNode_list.get(i).debug_entity.getPathFinder().getToNode() != null) {
			targetNode_list.get(i).set(entity_d_list.get(i).getPathFinder().getToNode().getX() + 0.5f,
					entity_d_list.get(i).getPathFinder().getToNode().getY() + 0.5f);
			targetNode_list.get(i).renderCrosshairNode(renderer);
		}
	}
	
	private void renderTarget(int i) {
		if(target_list.get(i).debug_entity.getBehavior() != null)  {
			if(target_list.get(i).debug_entity.getBehavior() instanceof ReachOrientation) {
				ReachOrientation behaivor = ((ReachOrientation)target_list.get(i).debug_entity.getBehavior());
				
				this.target_list.get(i).set(behaivor.getTarget());
				this.target_list.get(i).renderCrosshairTarget(renderer);
			} else if(target_list.get(i).debug_entity.getBehavior() instanceof Arrive) {
				Arrive behaivor = ((Arrive)target_list.get(i).debug_entity.getBehavior());
				
				this.target_list.get(i).set(behaivor.getTarget());
				this.target_list.get(i).renderCrosshairTarget(renderer);
			}
		}
	}
	
	private void renderGraphPath(EntityLiving entity) {
		if(entity.getPathFinder().getGraphPath() != null) {
			GraphPath path = entity.getPathFinder().getGraphPath();
			
			for(int i = 0; i < path.getCount(); i++) {
				entity.getPathFinder().getGridAStar().getPosNodeByIndex(path.nodes.get(i).getIndex(), vector_1);
				renderer.rect(vector_1.x, vector_1.y, 1f, 1f);
			}
		}
	}

	private class Crosshair extends Vector2 {
		private EntityLiving debug_entity;
		private LocImp loc = new LocImp(0, 0);
		private float scale = 1f;
		
		private Vector2 hor_line_1 = new Vector2();
		private Vector2 hor_line_2 = new Vector2();
		private Vector2 ver_line_1 = new Vector2();
		private Vector2 ver_line_2 = new Vector2();
		
		Crosshair(EntityLiving entity) {
			debug_entity = entity;
		}
		
		private Vector2 set(Location location) {
			loc.set((Vector2) location.getPosition());
			
			return this.set((Vector2)loc);
		}
		
		@Override
		public Vector2 set(float x, float y) {
			hor_line_1.set(x - 0.25f * scale, y);
			hor_line_2.set(x + 0.25f * scale, y);
			ver_line_1.set(x, y - 0.25f * scale);
			ver_line_2.set(x, y + 0.25f * scale);
			scale = 55f;
			
			return super.set(x, y);
		}
		
		public void renderCrosshairNode(ShapeRenderer renderer) {
			this.set(debug_entity.getPathFinder().getToNode().getX(), debug_entity.getPathFinder().getToNode().getY());
			
			renderer.line(hor_line_1, hor_line_2);
			renderer.line(ver_line_1, ver_line_2);
		}
		
		public void renderCrosshairTarget(ShapeRenderer renderer) {
			renderer.line(x, y, debug_entity.getPosition().x, debug_entity.getPosition().y);
			renderer.circle(this.x, this.y, 0.36f);
		}
	}
	
	/** Vector means angle under self. */
	private class FOV extends Vector2 {
		private EntityLiving debug_entity;
		
		private Vector2 line1 = new Vector2();
		private float angle_1 = 0f;
		
		private Vector2 line2 = new Vector2();
		private float angle_2 = 0f;
		
		FOV(EntityLiving entity) {
			debug_entity = entity;
		}
		
		@Override
		public Vector2 set(float x, float y) {
			angle_1 = debug_entity.getOrientation() * MathUtils.radDeg - debug_entity.getFieldOfView().getAngle() / 2f;
			angle_2 = debug_entity.getOrientation() * MathUtils.radDeg + debug_entity.getFieldOfView().getAngle() / 2f;
			
			VecUtils.angleToVector(line1, debug_entity.getOrientation());			
			line1.setLength(debug_entity.getBoundingRadius());
			line1.add(x, y);
			//System.out.println("LINE_1: " + line1);
			
			return super.set(x, y);
		}
		
		public void renderFOV(ShapeRenderer renderer) {
			this.set(debug_entity.getPosition().x, debug_entity.getPosition().y);
			float arc_leght = MathUtils.PI * debug_entity.getFieldOfView().getRadius() / 180f;
			//arc_leght / 2f
			renderer.setColor(Color.CORAL);
			renderer.arc(line1.x, line1.y, debug_entity.getFieldOfView().getRadius(),
					-debug_entity.getFieldOfView().getAngle() / 2f + debug_entity.compPhysics.body.getRotation(),
					debug_entity.getFieldOfView().getAngle(), 32);
			
		}
	}

}
