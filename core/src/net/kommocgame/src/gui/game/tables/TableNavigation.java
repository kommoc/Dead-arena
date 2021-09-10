package net.kommocgame.src.gui.game.tables;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Array;

import net.kommocgame.src.Game;

public class TableNavigation extends Table {
	
	private Table 	table_container	= new Table(Game.NEUTRALIZER_UI);
	private Button 	but_reset		= new Button(Game.NEUTRALIZER_UI);
	
	private NavigationTree tree;
	private NavigationNode current_node;
	
	private Array<NButton> buttons	= new Array<NButton>();
	
	public TableNavigation(NavigationTree tree) {
		this(Game.NEUTRALIZER_UI, tree);
	}

	public TableNavigation(Skin skin, NavigationTree tree) {
		super(skin);
		this.tree = tree;
		this.setBackground("panel");
		this.add(table_container).expand().growY().left();
		this.setColor(Color.BROWN);
		
		float height = Gdx.graphics.getHeight() / 10f;
		this.add(but_reset).growY().right().width(height).height(height);
		
		but_reset.add(new Label("X", Game.GENERATED_LABEL_STYLE_GOTTHARD_MID_TEXT));
		but_reset.setColor(Game.COLOR_PANEL_INFO);
	}
	
	public void addButton(NButton but) {
		buttons.add(but);
		but.add(new Label(but.node.getNodeName(), Game.GENERATED_LABEL_STYLE_GOTTHARD_LITTLE_TEXT_WHITE)).grow();
		but.setColor(Game.COLOR_PANEL_INFO);
		
		table_container.add(but).growY().left();
	}
	
	@Override
	public void act(float delta) {
		super.act(delta);
		
		if(but_reset.isChecked()) {
			but_reset.setChecked(false);
			
			tree.openNode(tree.getNodeBegin());
			current_node = tree.getCurrentNode();
		}
		
		if(buttons.isEmpty() || buttons.size != tree.getOpenedNodes().size || current_node != tree.getCurrentNode()) {
			table_container.clear();
			buttons.clear();
			
			for(NavigationNode node : tree.getOpenedNodes()) {
				System.out.println("TableNavigation.act() ### name:" + node.getNodeName());
				NButton but = new NButton(node, tree);
				this.addButton(but);
			}
			
			current_node = tree.getCurrentNode();
		}
		
		tree.nodeCheck(current_node);
		
		for(int i = 0; i < buttons.size; i++) {
			NButton but = buttons.get(i);
			
			if(but.isChecked()) {
				but.setChecked(false);
				System.out.println("TableNavigation.act() ### but_checked: " + but.node.getNodeName());
				
				tree.openNode(but.node);
				current_node = tree.getCurrentNode();
			}
		}
	}
	
	protected class NButton extends Button {
		NavigationNode node;
		NavigationTree tree;
		
		public NButton(NavigationNode node, NavigationTree tree) {
			this(Game.NEUTRALIZER_UI, node, tree);
		}
		
		public NButton(Skin skin, NavigationNode node, NavigationTree tree) {
			super(Game.NEUTRALIZER_UI);
			this.node = node;
			this.tree = tree;
		}
		
		public NavigationNode getNavigationNode() {
			return node;
		}
		
		@Override
		public void act(float delta) {
			super.act(delta);
			
		}
	}
}
