package net.kommocgame.src.debug;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

public class DebugWorld {
	
	private Array<DebugLine> array_line = new Array<DebugWorld.DebugLine>();
	private int max_line_debug = 256;
	
	public static DebugWorld instance;
	ShapeRenderer renderer;
	
	public DebugWorld(ShapeRenderer renderer) {
		instance = this;
		this.renderer = renderer;
		array_line.setSize(max_line_debug);
	}
	
	public void renderDebugWorld() {
		if(renderer == null)
			return;
		
		for(int i = 0; i < array_line.size; i++) {
			if(array_line.get(i) != null) {
				DebugLine line = array_line.get(i);
				renderer.line(line.vec_begin.x, line.vec_begin.y, line.vec_end.x, line.vec_end.y, line.color_begin, line.color_end);
			}
		}
		
		array_line.clear();
	}
	
	public static void debugLine(Vector2 vec_begin, Vector2 vec_end) {
		DebugWorld.debugLine(vec_begin, vec_end, Color.WHITE);
	}
	
	public static void debugLine(Vector2 vec_begin, Vector2 vec_end, Color color) {
		DebugWorld.debugLine(vec_begin, vec_end, color, Color.WHITE);
	}
	
	public static void debugLine(Vector2 vec_begin, Vector2 vec_end, Color color_begin, Color color_end) {
		instance.addDebugLine(vec_begin, vec_end, color_begin, color_end);
	}
	
	private void addDebugLine(Vector2 vec_begin, Vector2 vec_end, Color color_begin, Color color_end ) {
		array_line.add(new DebugLine(vec_begin, vec_end, color_begin, color_end));
	}
	
	final class DebugLine {
		Color color_begin;
		Color color_end;
		Vector2 vec_begin;
		Vector2 vec_end;
		
		public DebugLine(Vector2 vec_begin, Vector2 vec_end) {
			this(vec_begin, vec_end, Color.WHITE);
		}
		
		public DebugLine(Vector2 vec_begin, Vector2 vec_end, Color color_begin) {
			this(vec_begin, vec_end, color_begin, Color.WHITE);
		}
		
		public DebugLine(Vector2 vec_begin, Vector2 vec_end, Color color_begin, Color color_end) {
			this.vec_begin = vec_begin;
			this.vec_end = vec_end;
			this.color_begin = color_begin;
			this.color_end = color_end;
		}
		
		public DebugLine setBeginColor(Color color) {
			this.color_begin = color;
			return this;
		}
		
		public DebugLine setEndColor(Color color) {
			this.color_end = color;
			return this;
		}
	}
}
