package net.kommocgame.src.debug;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;

public class DebugAStarDisabledNode extends Vector2 {
	
	private int index_id;
	private float scale;
	private Vector2 b_1 = new Vector2();
	private Vector2 b_2 = new Vector2();
	private Vector2 b_3 = new Vector2();
	private Vector2 e_1 = new Vector2();
	private Vector2 e_2 = new Vector2();
	private Vector2 e_3 = new Vector2();
	
	public DebugAStarDisabledNode(int index) {
		index_id = index;
	}

	public DebugAStarDisabledNode(Vector2 v, int index) {
		super(v);
		index_id = index;
		this.set(v.x, v.y);
	}

	public DebugAStarDisabledNode(float x, float y, int index, float scale) {
		super(x, y);
		index_id = index;
		this.scale = scale;
		this.set(x, y);
	}
	
	@Override
	public Vector2 set (float x, float y) {
		this.x = x;
		this.y = y;
		
		b_1.set(this.x, this.y + 1f/4f * scale);
		b_2.set(this.x, this.y + 2f/4f * scale);
		b_3.set(this.x, this.y + 3f/4f * scale);
		e_1.set(this.x + 1f * scale, this.y + 1f/4f * scale);
		e_2.set(this.x + 1f * scale, this.y + 2f/4f * scale);
		e_3.set(this.x + 1f * scale, this.y + 3f/4f * scale);
		
		return this;
	}
	
	public void render(ShapeRenderer renderer, float scale) {
		renderer.rect(this.x, this.y, 1f * scale, 1f * scale);
		renderer.line(b_1, e_1);
		renderer.line(b_2, e_2);
		renderer.line(b_3, e_3);
	}
	
	public int getIndexNode() {
		return index_id;
	}
}
