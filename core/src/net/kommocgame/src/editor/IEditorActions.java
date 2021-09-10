package net.kommocgame.src.editor;

import com.github.czyzby.kiwi.util.gdx.scene2d.Alignment;

public interface IEditorActions {
	void translateObject(float x, float y);
	
	void translateObject(float x, float y, Alignment alignment);
	
	void rotateObject(float angle);
	
	void scaleObject(float scaleX, float scaleY);
	
	void deleteObject(EditorCore core);
	
}
