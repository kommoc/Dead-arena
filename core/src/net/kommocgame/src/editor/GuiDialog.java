package net.kommocgame.src.editor;

import net.kommocgame.src.Game;
import net.kommocgame.src.gui.GuiBase;

public class GuiDialog extends GuiBase {
	
	public DialogScreen dialog;
	
	public GuiDialog(Game game, String title) {
		super(game);
		
		dialog = (DialogScreen) new DialogScreen("Confirm", Game.NEUTRALIZER_UI).text(title);
		group_stage.addActor(dialog);
	}
}
