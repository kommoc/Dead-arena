package net.kommocgame.src.editor.nodes.params;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;

import net.kommocgame.src.Game;
import net.kommocgame.src.Loader;
import net.kommocgame.src.editor.EditorButton;
import net.kommocgame.src.editor.GuiEditor;

@Deprecated
public class EPName extends EParamObj {
	
	TextField text_name = new TextField("", Game.NEUTRALIZER_UI);
	EditorButton but_apply = new EditorButton(Game.NEUTRALIZER_UI, Loader.guiEditor("but/menu/icon_apply.png"));
	
	Label label_title = new Label("Name", Game.NEUTRALIZER_UI);
	
	public EPName(GuiEditor gui) {
		this(gui, Game.NEUTRALIZER_UI);
	}

	public EPName(GuiEditor gui, Skin skin) {
		super(gui, skin);
		this.setBackground("button");
		this.left();
		
		Table table_param = new Table(skin);
		Table table_button = new Table(skin);
		
		this.add(label_title);
		this.row();
		this.add(table_param).expandX();
		this.add(table_button);
		
		table_param.add(new Label("Name: ", skin));
		table_param.add(text_name);
		
		text_name.setText("" + object.getName());
		//text_name.setTextFieldFilter(filter);
		table_button.add(but_apply);
	}
	
	@Override
	public void act(float delta) {
		super.act(delta);
		
		try {
			if(text_name.getText() != object.getName()) {
				text_name.setColor(Color.RED);
			} else {
				text_name.setColor(Color.WHITE);
			}
			
			if(but_apply.isChecked()) {
				but_apply.setChecked(false);
				
				//object.setName(text_name.getText());
			}
		} catch(Exception e) {}
	}
}
