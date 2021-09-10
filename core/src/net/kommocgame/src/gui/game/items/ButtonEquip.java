package net.kommocgame.src.gui.game.items;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

import net.kommocgame.src.Game;

public class ButtonEquip extends Button {

	Label label_equip		= new Label("EQUIP", Game.GENERATED_LABEL_STYLE_GOTTHARD_BUTTON);
	Label label_isEquiped	= new Label("item_type is equipped", Game.GENERATED_LABEL_STYLE_GOTTHARD_LITTLE_TEXT);
	
	public ButtonEquip(Skin skin) {
		super(skin);
	}
	
	public void setButton_EQUIP() {
		this.clearChildren();
		this.add(label_equip);
		this.setTouchable(Touchable.enabled);
		this.setColor(Color.RED);
		
		System.out.println("ButtonStoreBuy.setButton_EQUIP() ### EQUIP");
	}
	
	public void setButton_IS_EQUIPPED() {
		this.clearChildren();
		this.add(label_isEquiped);
		this.setTouchable(Touchable.disabled);
		this.setColor(Game.COLOR_PANEL_INFO);
		
		System.out.println("ButtonStoreBuy.setButton_IS_EQUIPPED() ### EQUIPPED");
	}
}
