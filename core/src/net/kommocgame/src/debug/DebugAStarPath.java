package net.kommocgame.src.debug;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

import net.kommocgame.src.entity.AI.GraphPath;
import net.kommocgame.src.entity.AI.GridNodesAStar;

public class DebugAStarPath {
	
	private GraphPath path;
	private Array<Vector2> vecs = new Array<Vector2>();
	private GridNodesAStar grid;
	
	public DebugAStarPath(GridNodesAStar grid) {
		this.grid = grid;
	}
	
	public void setGraphPath(GraphPath path) {
		vecs.clear();
		if(this.path != null)
			this.path.clear();
		
		this.path = path;
		
		if(this.path == null)
			return;
			
		for(int i = 0; i < path.getCount(); i++) {
			Vector2 vec = new Vector2();
			vecs.add(grid.getPosNodeByIndex(path.get(i).getIndex(), vec));
		}
	}
	
	public GraphPath getGraphPath() {
		return path;
	}
	
	public void render(ShapeRenderer renderer) {
		if(path != null) {
			for(int i = 0; i < path.getCount(); i++) {
				renderer.rect(vecs.get(i).x, vecs.get(i).y, 1f * grid.getScale(), 1f * grid.getScale());
			}
		}
	}

}
