package net.kommocgame.src.gui.game.tables;

import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Array;

public abstract class NavigationTree {
	
	private NavigationNode node_begin;
	private NavigationNode node_current;
	
	private Array<NavigationNode> array_opened_nodes = new Array<NavigationNode>();
	
	private Table table_nodes;
	private Table table_item;
	
	public NavigationTree() {}
	
	public void openNode(NavigationNode node) {
		table_nodes.reset();
		table_item.reset();
		array_opened_nodes.clear();
		
		Array<NavigationNode> nodes;
		if(node.getNodes().isEmpty() && node.getParentNode() != null)
			nodes = node.getParentNode().getNodes();
		else nodes = node.getNodes();
		
		for(NavigationNode child_node : nodes) {
			table_nodes.add(child_node.getNodeContainer()).growX();
			table_nodes.row();
		}
		
		table_item.add(node.getItemContainer()).grow();
		
		node_current = node;
		node_getParents(node, array_opened_nodes);
	}
	
	/** Check ContainerNode pressed event.*/
	public void nodeCheck(NavigationNode node) {
		Array<NavigationNode> nodes;
		if(node.getNodes().isEmpty() && node.getParentNode() != null)
			nodes = node.getParentNode().getNodes();
		else nodes = node.getNodes();
		
		for(int i = 0; i < nodes.size; i++) {
			NavigationNode child_node = nodes.get(i);
			
			if(child_node.getNodeContainer() instanceof Button) {
				Button but = (Button) child_node.getNodeContainer();
				
				if(but.isChecked()) {
					but.setChecked(false);
					
					openNode(child_node);
				}
			}
		}
	}
	
	public void setTables(Table table_nodes, Table table_item) {
		this.table_nodes	= table_nodes;
		this.table_item		= table_item;
	}
	
	public abstract NavigationNode getNodeBegin();
	
	public NavigationNode getCurrentNode() {
		return node_current;
	}
	
	public Array<NavigationNode> getOpenedNodes() {
		return array_opened_nodes;
	}
	
	private void node_getParents(NavigationNode node, Array<NavigationNode> array) {
		array.insert(0, node);
		
		if(node.getParentNode() != null)
			node_getParents(node.getParentNode(), array);
	}
	
	public void reset() {
		this.openNode(node_begin);
	}
}
