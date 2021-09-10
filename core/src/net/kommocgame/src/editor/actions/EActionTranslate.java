package net.kommocgame.src.editor.actions;

import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.math.Vector2;
import com.github.czyzby.kiwi.util.gdx.scene2d.Alignment;

import net.kommocgame.src.control.InputHandler;
import net.kommocgame.src.control.KeyBinding;
import net.kommocgame.src.editor.IEditorActions;
import net.kommocgame.src.editor.objects.EObject;

public class EActionTranslate extends EditorAction {
	
	IEditorActions object;
	private float offsetX = 0f;
	private float offsetY = 0f;
	
	private Vector2 source_pos;
	
	private Alignment alignment;
	private Alignment prew_alignment;
	
	private boolean setAlignment = false;
	
	public EActionTranslate(IEditorActions object) {
		this.object = object;
		alignment = ((EObject) object).getAlignment();
		
		prew_alignment = alignment;
		
		source_pos = ((EObject) object).getPosition().cpy();
		
		offsetX = ((EObject) object).getPosition().x - InputHandler.BOX2D_mouse_x;
		offsetY = ((EObject) object).getPosition().y - InputHandler.BOX2D_mouse_y;
		System.out.println("	EActionTranslate(new instance) ### alignment: " + alignment);
	}
	
	/** Haven't super(update) */
	@Override 
	public void update() {
		if(object != null) {
			if(KeyBinding.getKeyRealised(Keys.NUMPAD_1)) {
				alignment = alignment.BOTTOMLEFT;
				setAlignment = true;
			} else if(KeyBinding.getKeyRealised(Keys.NUMPAD_2)) {
				alignment = alignment.BOTTOM;
				setAlignment = true;
			} else if(KeyBinding.getKeyRealised(Keys.NUMPAD_3)) {
				alignment = alignment.BOTTOMRIGHT;
				setAlignment = true;
			} else if(KeyBinding.getKeyRealised(Keys.NUMPAD_4)) {
				alignment = alignment.LEFT;
				setAlignment = true;
			} else if(KeyBinding.getKeyRealised(Keys.NUMPAD_5)) {
				alignment = alignment.CENTER;
				setAlignment = true;
			} else if(KeyBinding.getKeyRealised(Keys.NUMPAD_6)) {
				alignment = alignment.RIGHT;
				setAlignment = true;
			} else if(KeyBinding.getKeyRealised(Keys.NUMPAD_7)) {
				alignment = alignment.TOPLEFT;
				setAlignment = true;
			} else if(KeyBinding.getKeyRealised(Keys.NUMPAD_8)) {
				alignment = alignment.TOP;
				setAlignment = true;
			} else if(KeyBinding.getKeyRealised(Keys.NUMPAD_9)) {
				alignment = alignment.TOPRIGHT;
				setAlignment = true;
			} else if(KeyBinding.getKeyRealised(Keys.NUMPAD_0)) {
				alignment = alignment.CENTER;
				setAlignment = false;
			}
			
			if(isCtrl())
				object.translateObject((int)InputHandler.BOX2D_mouse_x + (int)(setAlignment ? 0 : offsetX),
						(int)InputHandler.BOX2D_mouse_y + (int)(setAlignment ? 0 : offsetY), alignment);
			else object.translateObject(	InputHandler.BOX2D_mouse_x + (setAlignment ? 0 : offsetX),
									InputHandler.BOX2D_mouse_y + (setAlignment ? 0 : offsetY), alignment);
		}
	}
	
	@Override
	public void cancel() {
		super.cancel();
		
		if(object != null) {
			object.translateObject(source_pos.x, source_pos.y, prew_alignment);
		}
	}
	
	@Override
	public String toString() {
		return super.toString() + "(common)";
	}

}
