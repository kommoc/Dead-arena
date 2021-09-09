package net.kommocgame.src.gui.game.items;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.kotcrab.vis.ui.building.utilities.Alignment;

import net.kommocgame.src.Game;

public class TableItemsInfoName extends Table {
	
	private Label label_name;
	
	public TableItemsInfoName(String name) {
		this(Game.NEUTRALIZER_UI, name, Game.COLOR_TEXT_SELECTED);
	}
	
	public TableItemsInfoName(String name, Color color) {
		this(Game.NEUTRALIZER_UI, name, color);
	}

	public TableItemsInfoName(Skin skin, String name, Color color) {
		super(skin);
		label_name = new Label(name, Game.GENERATED_LABEL_STYLE_GOTTHARD_MID_TEXT_WHITE);
		label_name.setWrap(true);
		this.add(label_name).grow().align(Alignment.TOP_LEFT.getAlignment());
	}
	
	public String getName() {
		return label_name.getText() + "";
	}
	
	public void setName(String name) {
		label_name.setText(name);
	}
	
	public void setColor(Color color) {
		label_name.setColor(color);
	}
}
