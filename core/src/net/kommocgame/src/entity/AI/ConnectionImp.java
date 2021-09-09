package net.kommocgame.src.entity.AI;

import com.badlogic.gdx.ai.pfa.Connection;

public class ConnectionImp implements Connection<Node> {
	
	private Node from_node;
	private Node to_node;
	private int cost;
	
	public ConnectionImp(Node from, Node to, int cost) {
		this.from_node = from;
		this.to_node = to;
		this.cost = cost;
	}

	@Override
	public float getCost() {
		return cost;
	}

	@Override
	public Node getFromNode() {
		return from_node;
	}

	@Override
	public Node getToNode() {
		return to_node;
	}
	
	@Override
	public String toString() {
		return "Connected_to: " + "[ " + to_node.getX() + "; " + to_node.getY() + " ]";
	}
}
