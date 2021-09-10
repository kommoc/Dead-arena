package net.kommocgame.src.gui.DeadArena;


import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.List.ListStyle;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane.ScrollPaneStyle;
import com.badlogic.gdx.scenes.scene2d.ui.SelectBox;
import com.badlogic.gdx.scenes.scene2d.ui.SelectBox.SelectBoxStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;

import net.kommocgame.src.Game;
import net.kommocgame.src.entity.character.EntityPlayer;

public class DATableDebug extends Table {
	
	Button but_back	= new Button(Game.NEON_UI);
	
	ScrollPane	scroll_pane;
	Table		scroll_table = new Table(Game.NEON_UI);
	SelectBox	selector_player_armor_grade;

	public DATableDebug(Skin skin) {
		super(skin);
		this.setBackground("panel");
		scroll_pane = new ScrollPane(scroll_table, Game.NEON_UI);
		this.add(scroll_pane).grow();
		this.row();
		this.add(but_back);
		
		ListStyle listStyle = Game.NEUTRALIZER_UI.get(ListStyle.class);
		listStyle.font = Game.FONT_CONSOLE_72;
		
		SelectBoxStyle style = new SelectBoxStyle(Game.FONT_CONSOLE_72, Color.CYAN, skin.getDrawable("panel"),
				Game.NEON_UI.get(ScrollPaneStyle.class), listStyle);
		
		selector_player_armor_grade = new SelectBox(style);
		selector_player_armor_grade.setItems("NULL", "CLEAR", "LIGHT", "MEDIUM");
		
		scroll_table.top();
		scroll_table.add(selector_player_armor_grade).growX();
		scroll_table.row();
		
		but_back.add(new Label("Back", Game.LABEL_STYLE_CONSOLE_72)).growX();
		
		if(Game.getPlayer() != null) {
			EntityPlayer player = (EntityPlayer) Game.getPlayer();
			
			selector_player_armor_grade.setSelectedIndex(1);
		} else {
			selector_player_armor_grade.setTouchable(Touchable.disabled);
			selector_player_armor_grade.setSelectedIndex(0);
		}
	}
	
	@Override
	public void act(float delta) {
		super.act(delta);
		
		if(but_back.isChecked()) {
			but_back.setChecked(false);
			
			if(Game.getPlayer() != null) {
				EntityPlayer player = (EntityPlayer) Game.getPlayer();
				
			} 
			
			this.remove();
		}
	}
}
