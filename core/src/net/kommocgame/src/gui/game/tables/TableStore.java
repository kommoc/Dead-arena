package net.kommocgame.src.gui.game.tables;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;

import net.kommocgame.src.Game;

/** Rename to TableItem. */
public class TableStore extends Table {
	
	/** Nodes by item. */
	private float tables_ratio = 0.3f;
	
	public Button but_back	= new Button(Game.NEON_UI);
	
	private TableNavigation navigation_panel;
	private NavigationTree	navigation_tree;
	
	private Table	table_nodes;
	private Table	table_nodes_scroll;
	private Table	table_item;
	
	private ScrollPane scroll_table_nodes;
	ScrollPane scroll_table_item;
	
	public TableStore(NavigationTree tree) {
		this(Game.NEUTRALIZER_UI, tree);
	}
	
	public TableStore(Skin skin, NavigationTree tree) {
		super(skin);
		navigation_tree	= tree;
		table_nodes	= new Table(skin);
		table_item	= new Table(Game.NEON_UI);
		table_nodes_scroll	= new Table(skin);
		navigation_panel	= new TableNavigation(tree);
		
		but_back.add(new Label("Back", Game.GENERATED_LABEL_STYLE_GOTTHARD_BUTTON));
		
		float scale_panel_1	= Gdx.graphics.getHeight() / 54f;
		float scale_panel_3 = Gdx.graphics.getHeight() / 46f;
		NinePatch patch9_canvas = Game.SCI_FI_ATLAS.createPatch("panel1");
		patch9_canvas.scale(scale_panel_1 / 13f, scale_panel_1 / 13f);
		patch9_canvas.setColor(Color.LIGHT_GRAY);
		NinePatchDrawable nine9_canvas = new NinePatchDrawable(patch9_canvas);
		this.setBackground(nine9_canvas);
		
		Table table_item_top = new Table(Game.NEUTRALIZER_UI);
		Table table_nodes_top = new Table(Game.NEUTRALIZER_UI);
		table_nodes_top.setBackground("panel");
		table_item_top.setBackground("panel");
		
		table_nodes_top.setColor(Game.COLOR_BACKGROUND);
		table_item_top.setColor(Game.COLOR_BACKGROUND);
		
		table_item_top.top();
		
		this.top();
		this.add(navigation_panel).growX().prefHeight(Gdx.graphics.getHeight()/10f).colspan(2);
		this.row();
		
		scroll_table_nodes	= new ScrollPane(table_nodes_scroll);
		scroll_table_item	= new ScrollPane(table_item);
		
		/**
		table_nodes.add(scroll_table_nodes).grow().top();
		table_nodes.row();
		table_nodes.add(but_back).expand().bottom();
		
		this.add(table_nodes).prefWidth(Gdx.graphics.getWidth() * tables_ratio).grow().top();
		this.add(scroll_table_item).prefWidth(Gdx.graphics.getWidth() - Gdx.graphics.getWidth() * tables_ratio).grow().top();
		*/
		
		table_nodes_top.add(scroll_table_nodes).expand().top().growX();
		table_nodes.add(table_nodes_top).grow().top();
		table_nodes.row();
		table_nodes.add(but_back).bottom();
		
		table_item_top.add(scroll_table_item).expand().top().growX();
		
		this.add(table_nodes).prefWidth(Gdx.graphics.getWidth() * tables_ratio).grow().top();
		this.add(table_item_top).prefWidth(Gdx.graphics.getWidth() - Gdx.graphics.getWidth() * tables_ratio).grow().top();
				
		tree.setTables(getNodesTable(), getItemTable());
		//navigation_tree.openNode(navigation_tree.getNodeBegin());
		//setNavigationNode(navigation_tree.getCurrentNode());
	}
	
	public Table getNodesTable() {
		return table_nodes_scroll;
	}
	
	public Table getItemTable() {
		return table_item;
	}
}
