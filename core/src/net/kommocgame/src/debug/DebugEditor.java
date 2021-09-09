package net.kommocgame.src.debug;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;

import net.kommocgame.src.editor.EAHandler;

public class DebugEditor {
	
	ShapeRenderer renderer;
	
	private Vector2 choosen_obj = new Vector2(0, 0);
	private EAHandler handler;
	
	public DebugEditor(ShapeRenderer renderer) {
		this(null, renderer);
	}
	
	public DebugEditor(EAHandler handler, ShapeRenderer renderer) {
		this.renderer = renderer;
		this.handler = handler;
	}
	
	private void drawChoosenObj() {
		if(handler.getChoosedObject() != null && handler.getChoosedObject().getInstance() != null
				&& handler.getChoosedObject().getDebugPosition() != null && handler.readyToDraw()) {
			choosen_obj.set(handler.getChoosedObject().getDebugPosition());
					//.add(-handler.getChoosedObject().getWidth() / 2f, -handler.getChoosedObject().getHeight() / 2f);
			
			renderer.setColor(Color.GOLD);
			//renderer.rect(choosen_obj.x, choosen_obj.y, handler.getChoosedObject().getWidth(), handler.getChoosedObject().getHeight());
			renderer.rect(choosen_obj.x, choosen_obj.y, handler.getChoosedObject().getWidth() / 2f, handler.getChoosedObject().getHeight() / 2f,
					handler.getChoosedObject().getWidth(), handler.getChoosedObject().getHeight(),
					1, 1, handler.getChoosedObject().getRotation());
			renderer.circle(choosen_obj.x + handler.getChoosedObject().getWidth() / 2f, choosen_obj.y + handler.getChoosedObject().getHeight() / 2f,
					handler.getChoosedObject().getWidth() / 4f, 6);
		}
		
	}
	
	public void render() {
		if(handler == null)
			return;
		
		drawChoosenObj();
	}
	
	public void setHandler(EAHandler handler) {
		this.handler = handler;
	}
	
	public EAHandler getHandler() {
		return handler;
	}
}
