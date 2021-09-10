package net.kommocgame.src.world;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.PolygonShape;

import net.kommocgame.src.control.InputHandler;
import net.kommocgame.src.entity.EntityBase;
import net.kommocgame.src.entity.AI.Node;

public class WorldHelper {
	
	World world;
	
	public WorldHelper(World world) {
		this.world = world;
	}
	
	public void calculateLevelAStarBounds() {
		if(world.getLevel() == null) {
			System.out.println("	WORLD_CALCULATION_A*_BOUNDS isn't possibylity.");
			return;
		}
		
		if(world.getLevel().getGridNodes() != null) {
			//	Make copy of array.
			//Node[] grid = world.getLevel().getGridNodes().getGraphPath().nodes.toArray(Node.class);
			
			//	Set grid visibility true.
			for(int i = 0; i < world.getLevel().getGridNodes().getGraphPath().getCount(); i++) {
				world.getLevel().getGridNodes().setStateNode(i, true);
			}
			
			for(int i = 0; i < world.getEngine().getEntities().size(); i++) {
				if(world.getEngine().getEntities().get(i) instanceof EntityBase) {
					EntityBase entity = (EntityBase) world.getEngine().getEntities().get(i);
					
					if(entity.getDefinition().getBody().getType() == BodyType.StaticBody) {
						//System.out.println("	FIXTURE[SHAPE]: " + entity.getDefinition().getShape());
						
						if(entity.getDefinition().getShape() instanceof PolygonShape) {
							PolygonShape shape = (PolygonShape) entity.getDefinition().getShape();
							Vector2 prew_vertex = new Vector2();
							Vector2 vertex = new Vector2();
							
							//	last loop will bind begin and end vertex.
							for(int l = 0; l <= shape.getVertexCount(); l++) {
								if(l == 0) {
									shape.getVertex(l, prew_vertex);
									//System.out.println("	PREW_VERTEX [" + prew_vertex + "]");
									//System.out.println("	PREW_VERTEX.len() [" + prew_vertex.len() + "]");
									
									prew_vertex.set(MathUtils.cosDeg(entity.getRotation() + prew_vertex.angle()) * prew_vertex.len(),
											MathUtils.sinDeg(entity.getRotation() + prew_vertex.angle()) * prew_vertex.len())
											.add(entity.getPosition());
									continue;
								}
								
								if(l != shape.getVertexCount()) {
									shape.getVertex(l, vertex);
									//System.out.println("	VERTEX [" + vertex + "]");
									//System.out.println("	VERTEX.len() [" + vertex.len() + "]");
									
									vertex.set(MathUtils.cosDeg(entity.getRotation() + vertex.angle()) * vertex.len(),
											MathUtils.sinDeg(entity.getRotation() + vertex.angle()) * vertex.len()).add(entity.getPosition());
								} else {
									shape.getVertex(0, prew_vertex);
									//System.out.println("	PREW_VERTEX [" + prew_vertex + "]");
									//System.out.println("	PREW_VERTEX.len() [" + prew_vertex.len() + "]");
									
									prew_vertex.set(MathUtils.cosDeg(entity.getRotation() + prew_vertex.angle()) * prew_vertex.len(),
											MathUtils.sinDeg(entity.getRotation() + prew_vertex.angle()) * prew_vertex.len())
											.add(entity.getPosition());
								}
								
								this.calc_line(prew_vertex.cpy(), vertex.cpy());
								//System.out.println("	Line [" + prew_vertex + "] [" + vertex + "]");
								prew_vertex.set(vertex);
							}
						} else if(entity.getDefinition().getShape() instanceof CircleShape) {
							CircleShape shape = (CircleShape) entity.getDefinition().getShape();
							
							int lines = (Math.round(shape.getRadius()) + (int) Math.sqrt(Math.round(shape.getRadius()))) * 2;
							
							Vector2 prew_vertex = new Vector2(shape.getRadius(), 0).add(entity.getPosition());
							Vector2 vertex = new Vector2(shape.getRadius(), 0);
							
							for(int j = 0; j <= lines; j++) {
								vertex.set(MathUtils.cos(MathUtils.PI2 * j / lines), MathUtils.sin(MathUtils.PI2 * j / lines))
										.scl(shape.getRadius()).add(entity.getPosition());
								
								this.calc_line(prew_vertex.cpy(), vertex.cpy());
								
								prew_vertex.set(vertex);
							}
						}
					}
				}
			}
		} else System.out.println("ERROR WorldHelper.calculateLevelAStarBounds() ### Grid is not defined!");
	}
	
	private void calc_line(Vector2 begin_vert, Vector2 end_vert) {
		float scale = world.getLevel().getGridNodes().getScale();
		
		if(begin_vert.x > end_vert.x) {
			Vector2 swap_vector = new Vector2(end_vert);
			
			end_vert.set(begin_vert);
			begin_vert.set(swap_vector);
		}
		
		Vector2 length = new Vector2(begin_vert).sub(end_vert);
		
		Vector2 node_begin = getVec(begin_vert);
		Vector2 node_end = getVec(end_vert);
		
		if(Node.checkNode(world.getLevel().getGridNodes(), node_begin.x, node_begin.y)) {
			world.getLevel().getGridNodes().setStateNode((int) node_begin.x, (int) node_begin.y, false);
		} if(Node.checkNode(world.getLevel().getGridNodes(), node_end.x, node_end.y)) {
			world.getLevel().getGridNodes().setStateNode((int) node_end.x, (int) node_end.y, false);
		}
		
		Vector2 node_curr = getVec(end_vert);
		Vector2 node_prew = getVec(begin_vert);
		
		System.out.println("	WorldHelper.calc_line() ### node_begin[" + node_begin.x + "; " + node_begin.y + "]");
		System.out.println("	WorldHelper.calc_line() ### node_end[" + node_end.x + "; " + node_end.y + "]");
		
		float k = length.y / length.x;
		float b = begin_vert.y - k * begin_vert.x;
		
		float f_diff = (node_begin.x - node_end.x) / scale;
		int difference = f_diff + (node_begin.y - node_end.y) % scale != 0 ? ((node_begin.y - node_end.y) >= 0 ? 1 : -1) : 0;
		
		System.out.println("	WorldHelper.calc_line() ### TEST ### f_diff:" + f_diff);
		System.out.println("	WorldHelper.calc_line() ### TEST ### difference:" + difference);
		
		for(float i = node_begin.x; i <= (int) node_end.x; i += scale) {
			Vector2 func = getFunc_y(k, i + scale, b);
			
			if(func.x <= end_vert.x)
				node_curr = func;
			else node_curr.set(node_end);
			
			node_curr = getVec(node_curr);
			
			int x = (int) node_prew.x;
			
			//	vertical check
			if(node_curr.y - node_prew.y > 0) {
				//	to up
				
				for(float j = node_prew.y; j <= node_curr.y; j += scale) {
					if(Node.checkNode(world.getLevel().getGridNodes(), x, j))
						world.getLevel().getGridNodes().setStateNode(x, j, false);
				}
			} else if(node_curr.y - node_prew.y < 0) {
				//	to down
				
				for(float j = node_curr.y; j <= node_prew.y; j += scale) {
					if(Node.checkNode(world.getLevel().getGridNodes(), x, j))
						world.getLevel().getGridNodes().setStateNode(x, j, false);
				}
			} else {
				if(Node.checkNode(world.getLevel().getGridNodes(), x, (int) node_curr.y))
					world.getLevel().getGridNodes().setStateNode(x, (int) node_curr.y, false);
			}
			
			node_prew.set(node_curr);
		}
	}
	
	private Vector2 getFunc_y(float k, float x, float b) {
		return new Vector2(x, k * x + b);
	}
	
	/** Return the node-vector. */
	private Vector2 getVec(Vector2 vec) {
		return this.getVec(vec.x, vec.y);
	}
	
	/** Return the node-vector. */
	private Vector2 getVec(float pos_x, float pos_y) {
		int x, y;
		float scale = world.getLevel().getGridNodes().getScale();
		
		x = (int) (pos_x / scale);
		y = (int) (pos_y / scale);
		
		if(pos_y < 0) {
			y = (int) Math.floor(pos_y / scale);
		} if(pos_x < 0 && pos_y < 0) {
			y = (int) Math.floor(pos_y / scale);
		}
		
		if(pos_x < 0) {
			x = (int) Math.floor(pos_x / scale);
		}if(pos_x < 0 && pos_y < 0) {
			x = (int) Math.floor(pos_x / scale);
		}
		
		return new Vector2( (int) (x * scale), (int) (y * scale));
	}
}

