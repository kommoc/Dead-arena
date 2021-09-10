package net.kommocgame.src.entity.AI;

import com.badlogic.gdx.ai.pfa.Connection;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

import net.kommocgame.src.debug.CommandLine;
import net.kommocgame.src.debug.DebugAStarGrid;
import net.kommocgame.src.render.RenderEngine;

public class Node {
	
	private int x, y;
	private int index_id;
	protected Array<Connection<Node>> connections;
	
	/** true - is enabled on grid. */
	private boolean state = true;
	
	/***********************************************************/
	private static Vector2 vector_node_position = new Vector2();
	
	public Node(int x, int y, int index) {
		this.index_id = index;
		this.x = x;
		this.y = y;
		connections = new Array<Connection<Node>>();
	}
	
	public void addConnection(Node toNode, int cost) {
		// 1 2 3
		// 4   6
		// 7 8 9
		connections.add(new ConnectionImp(this, toNode, cost));
	}
	
	public void deleteConnection(Node node) {
		for(int i = 0; i < connections.size; i++) {
			if(connections.get(i).getToNode() == node) {
				connections.removeIndex(i);
			}
		}
	}
	
	public void disableNode(GridNodesAStar grid) {
		grid.setStateNode(index_id, false);
	}
	
	public void enableNode(GridNodesAStar grid) {
		grid.setStateNode(index_id, true);
	}
	
	protected void setEnable(boolean bool) {
		this.state = bool;
	}
	
	public boolean getState() {
		return state;
	}
	
	public int getIndex() {
		return index_id;
	}
	
	/** Current position of this node(Y). Position is offset! */
	public int getX() {
		return x;
	}
	
	/** Current position of this node(X). Position is offset! */
	public int getY() {
		return y;
	}
	
	@Override
	public String toString() {
		return "	Node	[ " + x + "; " + y + "]\n" + "	state:	" + state +"\n" + "	connections: " + connections.toString("\n");
	}
	
	public static Vector2 getNodePos(float x, float y, Vector2 toVector) {
		int x_int, y_int;
		x_int = (int)x;
		y_int = (int)y;
		
		if(x < 0) {
			x_int = (int) Math.floor(x);
		} if(x < 0 && y < 0) {
			x_int = (int) Math.floor(x);
		}
		
		if(y < 0) {
			y_int = (int) Math.floor(y);
		} if(x < 0 && y < 0) {
			y_int = (int) Math.floor(y);
		}
		
		return toVector.set(x_int, y_int);
	}
	
	public static Node getNodeByPos(GridNodesAStar grid, float x, float y) {
		getNodePos(x, y, vector_node_position);
		
		if(grid.checkGrid(x, y))
			return grid.getNodeByPos((int)vector_node_position.x, (int)vector_node_position.y);
		
		System.out.println("Node.getNodeByPos() ### current position[" + x + " ," + y + "] is out of grid["
				+ grid.getWidth() + " ," + grid.getHeight() + "]!");
		return null;
	}
	
	public static boolean checkNode(GridNodesAStar grid, float x, float y) {
		return grid.checkGrid(x, y);
	}
}
