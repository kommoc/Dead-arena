package net.kommocgame.src.editor.nodes.params;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.SelectBox;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Stack;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.ui.VerticalGroup;
import com.github.czyzby.kiwi.util.gdx.scene2d.Alignment;

import net.kommocgame.src.Game;
import net.kommocgame.src.Loader;
import net.kommocgame.src.editor.EditorButton;
import net.kommocgame.src.editor.GuiEditor;
import net.kommocgame.src.editor.objects.EObject;

public class EPSpawnPosition extends EParamObj {
	
	EditorButton but_apply = new EditorButton(Game.NEUTRALIZER_UI, Loader.guiEditor("but/menu/icon_apply.png"));
	Table table = new Table(Game.NEUTRALIZER_UI);
	
	Label 		label_title = new Label("Spawn position", Game.NEUTRALIZER_UI);
	TextField 	text_x = new TextField("", Game.NEUTRALIZER_UI);
	TextField 	text_y = new TextField("", Game.NEUTRALIZER_UI);
	
	Label 		label_alignment = new Label("Alignment", Game.NEUTRALIZER_UI);
	Table table_alignment = new Table(Game.NEUTRALIZER_UI);
	SelectBox selector_alignment = new SelectBox(Game.NEON_UI);
	
	public EPSpawnPosition(GuiEditor gui) {
		this(gui, Game.NEUTRALIZER_UI);
	}

	public EPSpawnPosition(GuiEditor gui, Skin skin) {
		super(gui, skin);
		this.setBackground("button");
		this.add(table).expand().left();
		this.add(but_apply).center().right();
		
		table.add(label_title).colspan(5);
		table.row();
		
		table.add(new Label("X: ", skin)).center();
		table.add(text_x).width(100f);
		table.add(new Label("Y: ", skin)).center();
		table.add(text_y).width(100f);
		
		text_x.setTextFieldFilter(filter);
		text_y.setTextFieldFilter(filter);
		text_x.setText("" + object.getSpawnPosition().x);
		text_y.setText("" + object.getSpawnPosition().y);
		this.row();
		
		table_alignment.add(label_alignment).left().padRight(50f);
		table_alignment.add(selector_alignment).expandX().colspan(2).right();
		
		selector_alignment.setItems(Alignment.BOTTOMLEFT, Alignment.BOTTOM, Alignment.BOTTOMRIGHT, Alignment.LEFT, Alignment.CENTER, Alignment.RIGHT
				, Alignment.TOPLEFT, Alignment.TOP, Alignment.TOPRIGHT);
		selector_alignment.getSelection().set(object.getAlignment());
		
		this.add(table_alignment).colspan(2).expand().left();
	}
	
	@Override
	public void act(float delta) {
		super.act(delta);
		try {
			//System.out.println("		EPSpawnPosition.act() ### spawn_vec: " + object.getSpawnPosition());
			//System.out.println("		EPSpawnPosition.act() ### current_vec: " + object.getPosition());
			
			if(Float.valueOf(text_x.getText()) != object.getSpawnPosition().x) {
				text_x.setColor(Color.RED);
			} else {
				text_x.setColor(Color.WHITE);
			} if(Float.valueOf(text_y.getText()) != object.getSpawnPosition().y) {
				text_y.setColor(Color.RED);
			} else {
				text_y.setColor(Color.WHITE);
			}
			
			if(but_apply.isChecked()) {
				but_apply.setChecked(false);
				object.setAlignment((Alignment)selector_alignment.getSelected());
				object.translateObject(Float.valueOf(text_x.getText()), Float.valueOf(text_y.getText()));
				
				System.out.println("		EPSpawnPosition.act() ### current_vec: " + object.getPosition());
			}
			
			if(object.getAlignment() != selector_alignment.getSelected()) {
				selector_alignment.setColor(Color.BLACK);
			} else {
				selector_alignment.setColor(Color.WHITE);
			}
		} catch (Exception e) {}
	}
}
