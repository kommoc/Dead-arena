package net.kommocgame.src.control;

/** Gui helper. */
public interface IMouse {
	
	boolean touchDown(float x, float y);
	boolean touchDragged(float offsetX, float offsetY);
	boolean touchUp();
	
	void scrolled(int scroll);
}
