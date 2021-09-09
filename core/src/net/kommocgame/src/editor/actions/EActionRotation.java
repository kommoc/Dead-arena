package net.kommocgame.src.editor.actions;

import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.github.czyzby.kiwi.util.gdx.scene2d.Alignment;

import net.kommocgame.src.VecUtils;
import net.kommocgame.src.control.InputHandler;
import net.kommocgame.src.control.KeyBinding;
import net.kommocgame.src.editor.IEditorActions;
import net.kommocgame.src.editor.objects.EObject;

public class EActionRotation extends EditorAction {
	
	IEditorActions object;
	private float prew_rotation;
	
	private Vector2 source_pos;
	
	private Vector2 prew_pos;
	private Vector2 curr_pos;
	
	private Alignment alignment;
	private Alignment prew_alignment;
	
	private boolean setAlignment = false;
	
	public EActionRotation(IEditorActions obj) {
		this.object = obj;
		prew_rotation = ((EObject)object).getRotation();
		prew_alignment = ((EObject)object).getRotationAlignment();
		alignment = prew_alignment;
		
		source_pos = ((EObject)object).getPosition().cpy();
		prew_pos = new Vector2(InputHandler.BOX2D_mouse_x, InputHandler.BOX2D_mouse_y).add(-source_pos.x, -source_pos.y);
		curr_pos = prew_pos.cpy();
		
		System.out.println("	EActionRotation(new instance) ### ");
	}
	
	@Override
	public void update() {
		if(object != null) {
			Vector2 vec_angle = new Vector2();
			VecUtils.angleToVector(vec_angle, prew_rotation - 90f * MathUtils.degRad);
			
			Vector2 point = new Vector2(InputHandler.BOX2D_mouse_x, InputHandler.BOX2D_mouse_y).add(-source_pos.x, -source_pos.y);
			float current_angle = point.angle(source_pos);
			
			curr_pos.set(InputHandler.BOX2D_mouse_x, InputHandler.BOX2D_mouse_y).add(-source_pos.x, -source_pos.y);
			
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
			
			//System.out.println("PREW_ANGLE: 	" + (prew_rotation * MathUtils.radDeg));
			//System.out.println("	source_pos: 	" + source_pos);
			//System.out.println("	point: 		" + point);
			//System.out.println("Angle: 		" + current_angle);
			//System.out.println("Angle_difference: 		" + vec_angle.angle(curr_pos));
			((EObject)object).setRotationAlignment(alignment);
			
			if(isCtrl())
				object.rotateObject((vec_angle.angle(curr_pos) - vec_angle.angle(prew_pos) + prew_rotation) - 
						((vec_angle.angle(curr_pos) - vec_angle.angle(prew_pos) + prew_rotation)) % 15);
			else object.rotateObject(vec_angle.angle(curr_pos) - vec_angle.angle(prew_pos) + prew_rotation);
			
		} else cancel();
	}
	
	@Override
	public void cancel() {
		super.cancel();
		
		if(object != null) {
			((EObject)object).setRotationAlignment(prew_alignment);
			object.rotateObject(prew_rotation);
		}
	}
}
