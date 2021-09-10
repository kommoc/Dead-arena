package net.kommocgame.src.editor.actions;

import com.badlogic.gdx.math.Vector2;

import net.kommocgame.src.control.InputHandler;
import net.kommocgame.src.editor.IEditorActions;
import net.kommocgame.src.editor.objects.EObject;

public class EActionScale extends EditorAction {
	
	IEditorActions object;
	
	private float scale_x = 1f;
	private float scale_y = 1f;
	
	private float prew_scale_x;
	private float prew_scale_y;
	
	private float lin_distance_default;
	private float lin_distance_current;
	
	private Vector2 point;
	
	public EActionScale(IEditorActions obj) {
		this.object = obj;
		
		point = new Vector2(InputHandler.BOX2D_mouse_x, InputHandler.BOX2D_mouse_y);
		lin_distance_default = ((EObject)object).getPosition().cpy().dst(point);
		lin_distance_current = lin_distance_default;
		
		prew_scale_x = ((EObject)object).getScaleX();
		prew_scale_y = ((EObject)object).getScaleY();
		
		System.out.println("	EActionScale(new instance) ### prew_scale_x: " + prew_scale_x);
		System.out.println("	EActionScale(new instance) ### prew_scale_y: " + prew_scale_y);
	}
	
	@Override
	public void update() {
		if(object != null) {
			point.set(InputHandler.BOX2D_mouse_x, InputHandler.BOX2D_mouse_y);
			lin_distance_current = ((EObject)object).getPosition().cpy().dst(point);
			scale_x = lin_distance_current / lin_distance_default - 1f;
			scale_y = lin_distance_current / lin_distance_default - 1f;
			
			if(isCtrl())
				object.scaleObject((int)(scale_x + prew_scale_x), (int)(scale_y + prew_scale_y));
			else object.scaleObject(scale_x + prew_scale_x, scale_y + prew_scale_y);
			
			System.out.println("	EActionScale.update() ### scale: " + scale_x);
			System.out.println("	EActionScale.update() ### scale: " + scale_y);
		}
	}
	
	@Override
	public void cancel() {
		super.cancel();
		object.scaleObject(prew_scale_x, prew_scale_y);
	}

}
