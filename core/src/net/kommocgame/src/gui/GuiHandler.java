package net.kommocgame.src.gui;

import com.badlogic.gdx.scenes.scene2d.Actor;

public interface GuiHandler {
	
	interface ActionListener {
		/** Call's if mouse has been pressed. */
		void mouseClicked();
		void escReleased();
		void consoleReleased();
	}
	
	interface ActorPressed {
		/** Call's if someone Actor has been pressed. */
		void getActorPressedStage(Actor actor);
	}
	
	interface PreInit {
		/** This method is intended for preDrawing initialization. */
		void preInit();
	}
}
