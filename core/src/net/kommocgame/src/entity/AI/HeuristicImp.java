package net.kommocgame.src.entity.AI;

import com.badlogic.gdx.ai.pfa.Heuristic;

public class HeuristicImp implements Heuristic<Node> {

	@Override
	public float estimate(Node node, Node endNode) {
		int x_begin = node.getIndex() % GridNodesAStar.getWidth();
		int x_end = endNode.getIndex() % GridNodesAStar.getWidth();
		
		int y_begin = node.getIndex() / GridNodesAStar.getWidth();
		int y_end = endNode.getIndex() / GridNodesAStar.getWidth();
		
		// TODO translate to sqrt.
		
		return Math.abs(x_begin - x_end) + Math.abs(y_begin - y_end);			//Manhattan
		//return (float) Math.sqrt(Math.abs(x_begin - x_end) * Math.abs(x_begin - x_end)
		//		+ Math.abs(y_begin - y_end) * Math.abs(y_begin - y_end));			//Euclidean
		//return Math.max(Math.abs(x_begin - x_end), Math.abs(y_begin - y_end));	//Chebyshev
		
	}

}
