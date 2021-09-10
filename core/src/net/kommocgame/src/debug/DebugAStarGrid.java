package net.kommocgame.src.debug;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.StringBuilder;

import net.kommocgame.src.Game;
import net.kommocgame.src.control.InputHandler;
import net.kommocgame.src.entity.AI.GraphPath;
import net.kommocgame.src.entity.AI.GridNodesAStar;
import net.kommocgame.src.entity.AI.Node;

public class DebugAStarGrid {
	
	private GridNodesAStar grid;
	
	private ShapeRenderer renderer;
	private SpriteBatch debug_batch;
	
	private DebugAStarPath debug_path;
	private Array<Vector2> grid_ver_begin;
	private Array<Vector2> grid_ver_end;
	
	private Array<Vector2> grid_hor_begin;
	private Array<Vector2> grid_hor_end;
	
	private Vector2 cross_begin1 = new Vector2();
	private Vector2 cross_begin2 = new Vector2();
	private Vector2 cross_end1 = new Vector2();
	private Vector2 cross_end2 = new Vector2();
	private Array<DebugAStarDisabledNode> dis_array = new Array<DebugAStarDisabledNode>();
	
	private boolean state = false;
	
	private Vector2 vector_1 = new Vector2();
	private Vector2 vector_2 = new Vector2();
	private Vector2 vector_3 = new Vector2();
	
	public DebugAStarGrid(ShapeRenderer renderer, SpriteBatch batch) {
		this.renderer = renderer;
		this.debug_batch = batch;
	}
	
	public void render() {
		if(!state || grid == null)
			return;
		
		renderer.setColor(0, 0, 1, 1f);
		
		for(int i = 0; i < grid_ver_begin.size; i++) {
			renderer.line(grid_ver_begin.get(i), grid_ver_end.get(i));
		} for(int i = 0; i < grid_hor_begin.size; i++) {
			renderer.line(grid_hor_begin.get(i), grid_hor_end.get(i));
		}
		
		renderer.setColor(Color.GOLD);
		setPositionCross(InputHandler.getNodeX(), InputHandler.getNodeY());
		
		renderer.setColor(Color.BROWN);
		renderDisabledNode();
		
		renderer.setColor(Color.GREEN);
		if(debug_path != null)
			debug_path.render(renderer);
	}
	
	private void renderDisabledNode() {
		for(int i = 0; i < dis_array.size; i++) {
			dis_array.get(i).render(renderer, grid.getScale());
		}
	}
	
	public void renderAB() {
		if(CommandLine.test_astar_pthf == null || grid == null || grid.getGraphPath() == null)
			return;
		
		if(CommandLine.test_astar_pthf.getFromNode() != null) {
			grid.getPosNodeByIndex(CommandLine.test_astar_pthf.getFromNode().getIndex(), vector_1);
			Game.FONT_CONSOLE_32.draw(debug_batch, "A", vector_1.x / Game.SCALE_WORLD_VALUE,
					(vector_1.y + 0.75f) / Game.SCALE_WORLD_VALUE);
		}
		
		if(CommandLine.test_astar_pthf.getToNode() != null) {
			grid.getPosNodeByIndex(CommandLine.test_astar_pthf.getToNode().getIndex(), vector_2);
			Game.FONT_CONSOLE_32.draw(debug_batch, "B", vector_2.x / Game.SCALE_WORLD_VALUE,
					(vector_2.y + 0.75f) / Game.SCALE_WORLD_VALUE);
		}
	}
	
	private void setPositionCross(int x, int y) {
		cross_begin1.set(x, y);
		cross_begin2.set(x, y + 1 * grid.getScale());
		cross_end1.set(x + 1 * grid.getScale(), y + 1 * grid.getScale());
		cross_end2.set(x + 1 * grid.getScale(), y);
		
		renderer.line(cross_begin1, cross_end1);
		renderer.line(cross_begin2, cross_end2);
		renderer.rect(cross_begin1.x, cross_begin1.y, 1f * grid.getScale(), 1f * grid.getScale());
	}
	
	public void addDisabledNode(Node node) {
		Gdx.app.log("DEBUG_ASTAR_GRID	", "Add node: [ " + node.getIndex() + " ] to dis_array.");
		grid.getPosNodeByIndex(node.getIndex(), vector_3);
		
		dis_array.add(new DebugAStarDisabledNode(vector_3.x, vector_3.y,
				node.getIndex(), grid.getScale()));
	}
	
	/** From debug list. */
	public void removeDisabledNode(Node node) {
		for(int i = 0; i < dis_array.size; i++) {
			if(dis_array.get(i).getIndexNode() == node.getIndex()) {
				dis_array.removeIndex(i);
				Gdx.app.log("DEBUG_ASTAR_GRID	", new StringBuilder().append("removing ->").append(node.getIndex()).append(" node from dis_array has success.").toString());
				return;
			}
		}
		Gdx.app.log("DEBUG_ASTAR_GRID	", new StringBuilder().append("removing ->").append(node.getIndex()).append(" node. diss_arrat dont have this node!").toString());
	}
	
	public void setGrid(GridNodesAStar grid) {
		if(grid == null) {
			this.grid = null;
			dis_array.clear();
			
			if(debug_path != null)
				debug_path.setGraphPath(null);
			
			return;
		}
		
		this.grid = grid;
		debug_path = new DebugAStarPath(grid);
		
		grid_ver_begin = new Array<Vector2>();
		grid_ver_end = new Array<Vector2>();
		grid_hor_begin = new Array<Vector2>();
		grid_hor_end = new Array<Vector2>();
		
		for(int i = 0; i <= grid.getWidth(); i++) {
			grid_ver_begin.add(new Vector2(i * grid.getScale() - grid.getWidth() / 2 * grid.getScale(), -grid.getHeight() / 2 * grid.getScale()));
			grid_ver_end.add(new Vector2(i * grid.getScale() - grid.getWidth() / 2 * grid.getScale(), grid.getHeight() / 2 * grid.getScale()));
		}
		
		for(int i = 0; i <= grid.getHeight(); i++) {
			grid_hor_begin.add(new Vector2(-grid.getWidth() / 2 * grid.getScale(), i * grid.getScale() - grid.getHeight() / 2 * grid.getScale()));
			grid_hor_end.add(new Vector2(grid.getWidth() / 2 * grid.getScale(), i * grid.getScale() - grid.getHeight() / 2 * grid.getScale()));
		}
		
		for(int i = 0; i < grid.getGraphPath().getCount(); i++) {
			if(!grid.getGraphPath().get(i).getState()) {
				dis_array.add(new DebugAStarDisabledNode(grid.getGraphPath().get(i).getX(), grid.getGraphPath().get(i).getY(), i, grid.getScale()));
			}
		}
	}
	
	public void setGraphPath(GraphPath path) {
		debug_path.setGraphPath(path);
	}
	
	public void setDebug(boolean par1) {
		state = par1;
	}
	
	public boolean getState() {
		return state;
	}
}
