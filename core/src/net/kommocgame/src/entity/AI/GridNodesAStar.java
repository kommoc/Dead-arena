package net.kommocgame.src.entity.AI;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;

import net.kommocgame.src.render.RenderEngine;

public class GridNodesAStar {
	
	/** 1 node equals 1f velocity in Box2d.*/ 
	private float scale = 1f;
	
	/** Width of grid. */
	private static int w;
	
	/** Height of grid. */
	private static int h;
	
	/** 'Serializable.*/
	private int width;
	
	/** 'Serializable.*/
	private int height;
	
	private transient GraphPath graphPath;
	
	public GridNodesAStar(int width, int height, float scale) {
		w = width;
		h = height;
		this.scale = scale;
		
		if(w % 2 != 0) {
			Gdx.app.error("ERROR: [A* GRID]	- ", "Width must be even!");
			w++;
			Gdx.app.error("[A* GRID]	- ", "Current width: " + w);
		}
		
		if(h % 2 != 0) {
			Gdx.app.error("ERROR: [A* GRID]	- ", "Height must be even!");
			h++;
			Gdx.app.error("[A* GRID]	- ", "Current height: " + h);
		}
		
		this.height = h;
		this.width = w;
		
		graphPath = new GraphPath();
		
		for(int i = 0; i < w * h; i++) {
			int x = i % w;
			int y = i / w;
			graphPath.add(new Node(x, y, i));
		}
		
		for(int i = 0; i < w * h; i++) {
			int x = i % w;
			int y = i / w;
			Node fromNode = graphPath.get(i);
			
			boolean no1 = false, no2 = false, no3 = false, no4 = false, no6 = false, no7 = false, no8 = false, no9 = false;
			
			if(x == 0) {
				//noLeft = true;
				no1 = true;
				no4 = true;
				no7 = true;
			} if(x == (w - 1)) {
				//noRight = true;
				no3 = true;
				no6 = true;
				no9 = true;
			} if(y == 0) {
				//noDown = true;
				no7 = true;
				no8 = true;
				no9 = true;
			} if(y == (h - 1)) {
				//noUp = true;
				no1 = true;
				no2 = true;
				no3 = true;
			}
			
			if(!no1) {
				fromNode.addConnection(graphPath.get((x - 1) + (y + 1) * w), 14);
			} if(!no2) {
				fromNode.addConnection(graphPath.get((x) + (y + 1) * w), 10);
			} if(!no3) {
				fromNode.addConnection(graphPath.get((x + 1) + (y + 1) * w), 14);
			} if(!no4) {
				fromNode.addConnection(graphPath.get((x - 1) + (y) * w), 10);
			} if(!no6) {
				fromNode.addConnection(graphPath.get((x + 1) + (y) * w), 10);
			} if(!no7) {
				fromNode.addConnection(graphPath.get((x - 1) + (y - 1) * w), 14);
			} if(!no8) {
				fromNode.addConnection(graphPath.get((x) + (y - 1) * w), 10);
			} if(!no9) {
				fromNode.addConnection(graphPath.get((x + 1) + (y - 1) * w), 14);
			}
		}
	}
	
	public void setStateNode(int index, boolean enable) {
		this.setStateNode(getNodeByIndex(index), enable);
	}
	
	public void setStateNode(float x, float y, boolean enable) {
		this.setStateNode(Node.getNodeByPos(this, x, y), enable);
	}
	
	public void setStateNode(Node node, boolean enable) {
		//System.out.println("			set_state_node.");
		try {
			Node currentNode = node;
			
			if(currentNode == null || currentNode.getState() == enable)
				return;;
				
			currentNode.setEnable(enable);
			int x = currentNode.getX();
			int y = currentNode.getY();
			
			boolean no1 = false, no2 = false, no3 = false, no4 = false, no6 = false, no7 = false, no8 = false, no9 = false;
			
			if(x == 0) {
				//noLeft = true;
				no1 = true;
				no4 = true;
				no7 = true;
			} if(x == (w - 1)) {
				//noRight = true;
				no3 = true;
				no6 = true;
				no9 = true;
			} if(y == 0) {
				//noDown = true;
				no7 = true;
				no8 = true;
				no9 = true;
			} if(y == (h - 1)) {
				//noUp = true;
				no1 = true;
				no2 = true;
				no3 = true;
			}
			
			if(enable == false) {
				if(!no1) {
					graphPath.get((x - 1) + (y + 1) * w).deleteConnection(currentNode);
				} if(!no2) {
					graphPath.get((x) + (y + 1) * w).deleteConnection(currentNode);
				} if(!no3) {
					graphPath.get((x + 1) + (y + 1) * w).deleteConnection(currentNode);
				} if(!no4) {
					graphPath.get((x - 1) + (y) * w).deleteConnection(currentNode);
				} if(!no6) {
					graphPath.get((x + 1) + (y) * w).deleteConnection(currentNode);
				} if(!no7) {
					graphPath.get((x - 1) + (y - 1) * w).deleteConnection(currentNode);
				} if(!no8) {
					graphPath.get((x) + (y - 1) * w).deleteConnection(currentNode);
				} if(!no9) {
					graphPath.get((x + 1) + (y - 1) * w).deleteConnection(currentNode);
				}
				
				RenderEngine.debug_AStar.addDisabledNode(currentNode);
			} else {
				if(!no1) {
					graphPath.get((x - 1) + (y + 1) * w).addConnection(currentNode, 14);
				} if(!no2) {
					graphPath.get((x) + (y + 1) * w).addConnection(currentNode, 10);
				} if(!no3) {
					graphPath.get((x + 1) + (y + 1) * w).addConnection(currentNode, 14);
				} if(!no4) {
					graphPath.get((x - 1) + (y) * w).addConnection(currentNode, 10);
				} if(!no6) {
					graphPath.get((x + 1) + (y) * w).addConnection(currentNode, 10);
				} if(!no7) {
					graphPath.get((x - 1) + (y - 1) * w).addConnection(currentNode, 14);
				} if(!no8) {
					graphPath.get((x) + (y - 1) * w).addConnection(currentNode, 10);
				} if(!no9) {
					graphPath.get((x + 1) + (y - 1) * w).addConnection(currentNode, 14);
				}
				
				RenderEngine.debug_AStar.removeDisabledNode(currentNode);
			}
		} catch(Exception e) {
			e.printStackTrace();
		};
	}
	
	/** Set the scale of the rig. */
	public void setScale(float scale) {
		this.scale = scale;
	}
	
	public static int getWidth() {
		return w;
	}
	
	public static int getHeight() {
		return h;
	}
	
	public Node getNodeByPos(int x, int y) {
		x /= scale;
		y /= scale;
		
		x += w / 2;
		y += h / 2;
		return graphPath.get(x + y * w);
	}
	
	public Node getNodeByIndex(int index) {
		return this.graphPath.get(index);
	}
	
	public Vector2 getPosNodeByIndex(int index, Vector2 toVector) {
		return toVector.set(graphPath.get(index).getX() * scale - w / 2 * scale, graphPath.get(index).getY() * scale - h / 2 * scale);
	}
	
	public GraphPath getGraphPath() {
		return graphPath;
	}
	
	public float getScale() {
		return scale;
	}
	
	public boolean checkGrid(float x, float y) {
		return x / scale < w / 2 && x / scale > -w / 2 && y / scale < h / 2 && y / scale > -h / 2;
	}
}
