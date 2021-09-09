package net.kommocgame.src.gui.game.tables;

import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Array;

public class NavigationNode {
	
	private String node_name;
	
	private Table 	container_node;
	private Table	container_item;
	
	private Array<NavigationNode> array_nodes;
	
	private NavigationNode parent_node;
	
	public NavigationNode(Table container_node, Table container_item) {
		this.container_node = container_node;
		this.container_item = container_item;
		
		array_nodes = new Array<NavigationNode>();
	}
	
	public void setContainers(Table container_node, Table container_item) {
		this.container_node = container_node;
		this.container_item = container_item;
	}
	
	public NavigationNode addNode(Table container_node, Table container_item) {
		NavigationNode node = new NavigationNode(container_node, container_item);
		node.setParentNode(this);
		array_nodes.add(node);
		
		return node;
	}
	
	public NavigationNode setNodeName(String name) {
		node_name = name;
		return this;
	}
	
	private void setParentNode(NavigationNode node) {
		parent_node = node;
	}
	
	public String getNodeName() {
		return node_name;
	}
	
	public NavigationNode getParentNode() {
		return parent_node;
	}
	
	public Table getNodeContainer() {
		return container_node;
	}
	
	public Table getItemContainer() {
		return container_item;
	}
	
	public Array<NavigationNode> getNodes() {
		return array_nodes;
	}
}
