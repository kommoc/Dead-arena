package net.kommocgame.src.editor.nodes;

import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Table;

import net.kommocgame.src.editor.EditorLevelObjectsPanel;
import net.kommocgame.src.world.World;

public abstract class ENodeObj<T> {
	
	Class _class;
	public T instance;
	
	public ENodeObj(Class _class) {
		this._class = _class;
	}
	
	public abstract void dragToWorld(float x, float y, World world);
	
	public abstract void releaseInWorld(float x, float y);
	
	/** Called when action has been canceled. */
	public abstract void cancel(World world);
	
	public abstract Table checkInfo();
	
	public abstract Image getImage();
	
	public abstract String getName();
	
	public Class getInstanceClass() {
		return _class;
	}
	
	@Deprecated
	public abstract void addToPanel(EditorLevelObjectsPanel panel);
	
	@Deprecated
	public abstract void removeFromPanel(EditorLevelObjectsPanel panel);

}
