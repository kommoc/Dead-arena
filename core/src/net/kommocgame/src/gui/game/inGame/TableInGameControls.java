package net.kommocgame.src.gui.game.inGame;

import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.Touchpad;

import net.kommocgame.src.Game;
import net.kommocgame.src.control.KeyBinding;
import net.kommocgame.src.gui.GuiBase;

public class TableInGameControls extends Table {
	
	Touchpad touchpad_left		= new Touchpad(GuiBase.getRatio(1f/32f), Game.NEON_UI);
	Touchpad touchpad_right		= new Touchpad(GuiBase.getRatio(1f/32f), Game.NEON_UI);
	
	Table table_clear = new Table(Game.NEUTRALIZER_UI);
	
	public TableInGameControls() {
		this(Game.NEUTRALIZER_UI);
	}

	public TableInGameControls(Skin skin) {
		super(skin);
		
		this.add(touchpad_left).size(GuiBase.getRatio(1f/3f)).left().pad(GuiBase.getRatio(1f/32f)).left().expand();
		this.add(touchpad_right).size(GuiBase.getRatio(1f/3f)).right().pad(GuiBase.getRatio(1f/32f)).right();

		touchpad_right.getStyle().knob.setMinWidth(GuiBase.getRatio(1f/8f));
		touchpad_right.getStyle().knob.setMinHeight(GuiBase.getRatio(1f/8f));
	}
	
	@Override
	public void act(float delta) {
		super.act(delta);
		
		if(Game.getPlayer() != null) {
			KeyBinding.setMoveKnob(touchpad_left.getKnobPercentX(), touchpad_left.getKnobPercentY(), touchpad_right.getKnobPercentX(), 
					touchpad_right.getKnobPercentY());
		}
	}
}
