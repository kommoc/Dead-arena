package net.kommocgame.src.gui.game.inGame;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;

import net.kommocgame.src.Game;

public class TableInGameMenu extends Table {
	
	Table table_buttons = new Table(Game.NEUTRALIZER_UI);
	
	public Button	but_exit		= new Button(Game.NEON_UI);
	public Button	but_settings	= new Button(Game.NEON_UI);
	public Button	but_inventory	= new Button(Game.NEON_UI);
	
	public Button	but_menu	= new Button(Game.NEON_UI);
	
	Label	label_menu	= new Label("+ Menu", Game.GENERATED_LABEL_STYLE_GOTTHARD_MID_TEXT);
	boolean isOpened	= false;
	
	public TableInGameMenu() {
		this(Game.NEON_UI);
	}
	
	public TableInGameMenu(Skin skin) {
		super(skin);
		
		float scale_panel_menu = Gdx.graphics.getHeight() / 50f;
		NinePatch patch9_menu = Game.SCI_FI_ATLAS.createPatch("panel1");
		patch9_menu.scale(scale_panel_menu / 15f, scale_panel_menu / 15f);
		patch9_menu.setColor(new Color(1, 1, 1, 0.6f));
		NinePatchDrawable nine9_menu = new NinePatchDrawable(patch9_menu);
		this.setBackground(nine9_menu);
		
		but_exit.add(new Label("Exit", Game.GENERATED_LABEL_STYLE_GOTTHARD_MID_TEXT));
		but_settings.add(new Label("Settings", Game.GENERATED_LABEL_STYLE_GOTTHARD_MID_TEXT));
		but_inventory.add(new Label("Inventory", Game.GENERATED_LABEL_STYLE_GOTTHARD_MID_TEXT));
		but_menu.add(label_menu);
		
		table_buttons.setBackground("panel");
		table_buttons.setColor(Game.COLOR_PANEL_INFO);
		table_buttons.add(but_inventory).fillX();
		table_buttons.row();
		table_buttons.add(but_settings).fillX();
		table_buttons.row();
		table_buttons.add(but_exit).fillX();
		table_buttons.row();
	}
	
	/** Set the menu state - true to opened, false to closing. */
	public void menuState(boolean opened) {
		this.clear();
		this.isOpened = opened;
		
		if(opened) {
			this.add(but_menu).fillX();
			label_menu.setText("- Menu");
			this.row();
			this.add(table_buttons).padTop(Game.GUI_NUMBER_PANEL_GAP);
		} else {
			label_menu.setText("+ Menu");
			this.add(but_menu).fillX();
		}
	}
	
	public boolean isOpened() {
		return isOpened;
	}
}
