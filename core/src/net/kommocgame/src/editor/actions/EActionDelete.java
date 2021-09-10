package net.kommocgame.src.editor.actions;

import net.kommocgame.src.editor.EditorCore;
import net.kommocgame.src.editor.IEditorActions;

public class EActionDelete extends EditorAction {
	
	IEditorActions object;
	EditorCore core;
	
	public EActionDelete(IEditorActions obj, EditorCore core) {
		this.object = obj;
		this.core = core;
		
		System.out.println("	EActionDelete(new instance) ### ");
	}
	
	/** Haven't super(update) */
	@Override 
	public void update() {
		if(object != null) {
			object.deleteObject(core);
			this.apply();
		}
	}
	
	@Override
	public void cancel() {
		super.cancel();
	}
	
	@Override
	public String toString() {
		return super.toString() + "";
	}

}
