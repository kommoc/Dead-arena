package net.kommocgame.src.entity.AI;

import com.badlogic.gdx.ai.pfa.Connection;
import com.badlogic.gdx.ai.pfa.DefaultGraphPath;
import com.badlogic.gdx.ai.pfa.indexed.IndexedGraph;
import com.badlogic.gdx.utils.Array;

public class GraphPath extends DefaultGraphPath<Node> implements IndexedGraph<Node> {

	@Override
	public Array<Connection<Node>> getConnections(Node fromNode) {
		return fromNode.connections;
	}

	@Override
	public int getIndex(Node node) {
		return node.getIndex();
	}

	@Override
	public int getNodeCount() {
		return getCount();
	}
}
