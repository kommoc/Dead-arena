package net.kommocgame.src.entity.AI.asynch;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ai.pfa.PathFinderRequest;
import com.badlogic.gdx.math.Vector2;

import net.kommocgame.src.entity.AI.GraphPath;
import net.kommocgame.src.entity.AI.GridNodesAStar;
import net.kommocgame.src.entity.AI.Node;
import net.kommocgame.src.entity.AI.thread.Pather;
import net.kommocgame.src.entity.AI.thread.PathfindingManager;

public class PathFinderImp implements Pather<Node> {
	
	private GridNodesAStar grid_nodes;
	private GraphPath path;
	private PathFinderRequest<Node> request;
	
	private boolean recive = false;
	
	private Node fromNode;
	private Node toNode;
	private Node currentNode;
	
	private SteerableImp entity;
	
	private Vector2 vector_next_point = new Vector2();
	
	public PathFinderImp(SteerableImp entity, GridNodesAStar grid) {
		grid_nodes = grid;
		this.entity = entity;
		path = new GraphPath();
	}
	
	public GraphPath getGraphPath() {
		return path;
	}
	
	public Node getFromNode() {
		return fromNode;
	}
	
	public Node getToNode() {
		return toNode;
	}
	
	public GridNodesAStar getGridAStar() {
		return grid_nodes;
	}
	
	public Vector2 getNextPoint(Vector2 toVector) {
		if(isEmpty()) {
			Gdx.app.log("ENTITY A*: ", "Empty path.");
			return null;
		}
		
		grid_nodes.getPosNodeByIndex(path.nodes.first().getIndex(), vector_next_point);
		currentNode = path.nodes.first();
		
		if(path.nodes.size != 1)
			path.nodes.removeIndex(0);
		
		return vector_next_point;
	}
	
	public void createPath(float x, float y) {
		clear();
		recive = false;
		
		if(Node.checkNode(grid_nodes, entity.getPosition().x, entity.getPosition().y)) {
			fromNode = Node.getNodeByPos(grid_nodes, entity.getPosition().x, entity.getPosition().y);
		} else {
			recive = true;
			System.out.println("	compAStar: from node is out of grid! [" + x + ", " + y + "]");
			return;
		}
		
		if(Node.checkNode(grid_nodes, x, y)) {
			toNode = Node.getNodeByPos(grid_nodes, x, y);
		} else {
			PathfindingManager.getInstance().requestPathfinding(this, entity.worldObj.IASPF, fromNode, fromNode, path);
			System.out.println("	compAStar: to node is out of grid! [" + x + ", " + y + "]");
			return;
		}
		
		PathfindingManager.getInstance().requestPathfinding(this, entity.worldObj.IASPF, fromNode, toNode, path);
	}
	
	public boolean isEmpty() {
		return path.getCount() == 0;
	}
	
	public boolean isRecive() {
		return recive;
	}
	
	public void clear() {
		if(path == null) {
			System.err.println("	compAStar: path if not defined!");
			return;
		}
		
		path.clear();
		fromNode = null;
		toNode = null;
	}

	@Override
	public void acceptPath(PathFinderRequest<Node> request) {
		path = (GraphPath)((PathFinderRequest<Node>)request).resultPath;
		recive = true;
		
		//System.out.println("	RECIVE NEW SYSTEM: " + path.getCount());
	}
}
