package net.kommocgame.src.editor.nodes.params;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.github.czyzby.kiwi.util.gdx.scene2d.Alignment;

import net.kommocgame.src.Game;
import net.kommocgame.src.Loader;
import net.kommocgame.src.editor.EditorButton;
import net.kommocgame.src.editor.GuiEditor;

public class EPRotation extends EParamObj {
	
	Label label_title = new Label("Rotation", Game.NEUTRALIZER_UI);
	EditorButton but_apply = new EditorButton(Game.NEUTRALIZER_UI, Loader.guiEditor("but/menu/icon_apply.png"));
	
	TextField text_angle = new TextField("", Game.NEUTRALIZER_UI);
	
	public EPRotation(GuiEditor gui) {
		this(gui, Game.NEUTRALIZER_UI);
	}

	public EPRotation(GuiEditor gui, Skin skin) {
		super(gui, skin);
		this.setBackground("button");
		this.left();
		Table table_param = new Table(skin);
		Table table_button = new Table(skin);
		
		this.add(label_title);
		this.row();
		this.add(table_param).expandX();
		this.add(table_button);
		
		table_param.add(new Label("Angle: ", skin));
		table_param.add(text_angle);
		
		text_angle.setText("" + object.getRotation());
		text_angle.setTextFieldFilter(filter);
		table_button.add(but_apply);
	}
	
	@Override
	public void act(float delta) {
		super.act(delta);
		
		try {
			if(Float.valueOf(text_angle.getText()) != object.getRotation()) {
				text_angle.setColor(Color.RED);
			} else {
				text_angle.setColor(Color.WHITE);
			}
			
			if(but_apply.isChecked()) {
				but_apply.setChecked(false);
				
				object.rotateObject(Float.valueOf(text_angle.getText()));
			}
		} catch(Exception e) {}
	}

}
