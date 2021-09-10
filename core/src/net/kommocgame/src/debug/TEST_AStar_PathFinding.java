package net.kommocgame.src.debug;

import com.badlogic.gdx.ai.msg.Telegram;
import com.badlogic.gdx.ai.msg.Telegraph;
import com.badlogic.gdx.ai.pfa.PathFinderRequest;

import net.kommocgame.src.entity.AI.GraphPath;
import net.kommocgame.src.entity.AI.Node;

public class TEST_AStar_PathFinding implements Telegraph {
	//https://www.youtube.com/watch?time_continue=52&v=DZDCzTJxTZY
	//http://i.imgur.com/tNtSN.jpg
	private Node fromNode;
	private Node toNode;
	
	public GraphPath pathTo = new GraphPath();
	public PathFinderRequest<Node> request;
	
	public void setFromNode(Node node) {
		fromNode = node;
	}
	
	public void setToNode(Node node) {
		toNode = node;
	}
	
	public Node getFromNode() {
		return fromNode;
	}
	
	public Node getToNode() {
		return toNode;
	}
	
	public void reset() {
		fromNode = null;
		toNode = null;
		pathTo.clear();
	}

	@Override
	public boolean handleMessage(Telegram msg) {
		System.out.println("		TELEGRAM: \n	" + msg.extraInfo);
		pathTo = (GraphPath) ((PathFinderRequest<Node>)msg.extraInfo).resultPath;
		System.out.println("		info: \n	" + pathTo.getCount());
		return true;
	}

}
