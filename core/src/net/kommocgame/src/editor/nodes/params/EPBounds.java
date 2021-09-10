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
import net.kommocgame.src.editor.ElementsGui;
import net.kommocgame.src.editor.GuiEditor;
import net.kommocgame.src.editor.objects.EOTerrain;

public class EPBounds extends EParamObj {
	
	EditorButton but_apply = new EditorButton(Game.NEUTRALIZER_UI, Loader.guiEditor("but/menu/icon_apply.png"));
	
	Label label_title = new Label("Bounds", Game.NEUTRALIZER_UI);
	
	TextField text_current_w = new TextField("", Game.NEUTRALIZER_UI);
	TextField text_current_h = new TextField("", Game.NEUTRALIZER_UI);
	
	Label label_scale_x = new Label("Y:", Game.NEUTRALIZER_UI);
	Label label_scale_y = new Label("X:", Game.NEUTRALIZER_UI);
	
	Label label_defaults_width = new Label("W: ", Game.NEUTRALIZER_UI);
	Label label_defaults_height = new Label("H: ", Game.NEUTRALIZER_UI);
	
	public EPBounds(GuiEditor gui) {
		this(gui, Game.NEUTRALIZER_UI);
	}

	public EPBounds(GuiEditor gui, Skin skin) {
		super(gui, skin);
		this.setBackground("button");
		Table table_param = new Table(Game.NEUTRALIZER_UI);
		Table table_button = new Table(Game.NEUTRALIZER_UI);
		
		this.add(label_title);
		this.row();
		this.left();
		this.add(table_param).expand().left();
		this.add(table_button).right();
		
		Table table_defaults = new Table(Game.NEUTRALIZER_UI);
		Table table_current = new Table(Game.NEUTRALIZER_UI);
		Table table_scale = new Table(Game.NEUTRALIZER_UI);
		
		Label label_defaults = new Label("Default", Game.NEUTRALIZER_UI);
		Label label_current = new Label("Spawn", Game.NEUTRALIZER_UI);
		Label label_scale = new Label("Scale", Game.NEUTRALIZER_UI);
		
		table_param.add(label_defaults).padRight(32f).padLeft(16f).padTop(6f);
		table_param.add(table_defaults).left().padTop(6f);
		table_param.row();
		
		table_param.add(label_current).padRight(32f).padLeft(16f).padTop(6f);
		table_param.add(table_current).left().expand().padTop(6f);
		table_param.row();
		
		table_param.add(label_scale).padRight(32f).padLeft(16f).padTop(6f);
		table_param.add(table_scale).left().padTop(6f);
		
		table_defaults.add(label_defaults_width).left().padTop(6f);
		table_defaults.row();
		table_defaults.add(label_defaults_height).left().padTop(6f);
		
		table_current.add(new Label("W: ", Game.NEUTRALIZER_UI)).left().padTop(6f);
		table_current.add(text_current_w).padTop(6f);
		table_current.row();
		table_current.add(new Label("H: ", Game.NEUTRALIZER_UI)).left().padTop(6f);
		table_current.add(text_current_h).padTop(6f);
		
		table_scale.add(label_scale_x).padTop(6f);
		table_scale.row();
		table_scale.add(label_scale_y).padTop(6f);
		
		table_button.add(but_apply);
		
		text_current_h.setTextFieldFilter(filter);
		text_current_w.setTextFieldFilter(filter);
		
		text_current_w.setText("" + object.getWidth());
		text_current_h.setText("" + object.getHeight());
	}
	
	@Override
	public void act(float delta) {
		super.act(delta);
		
		if(object instanceof EOTerrain) {
			label_defaults_width.setText("W:" + ((EOTerrain)object).getDefaultWidth());
			label_defaults_height.setText("H:" + ((EOTerrain)object).getDefaultHeight());
		}
		
		label_scale_x.setText("" + object.getScaleX());
		label_scale_y.setText("" + object.getScaleY());
		
		try {
			if(Float.valueOf(text_current_w.getText()) != object.getWidth()) {
				text_current_w.setColor(Color.RED);
			} else {
				text_current_w.setColor(Color.WHITE);
			} if(Float.valueOf(text_current_h.getText()) != object.getHeight()) {
				text_current_h.setColor(Color.RED);
			} else {
				text_current_h.setColor(Color.WHITE);
			}
		} catch(Exception e) {}
		
		if(but_apply.isChecked()) {
			but_apply.setChecked(false);
			
			if(object instanceof EOTerrain) {
				object.scaleObject(Float.valueOf(text_current_w.getText()) / ((EOTerrain)object).getDefaultWidth(),
						Float.valueOf(text_current_h.getText()) / ((EOTerrain)object).getDefaultHeight());
			}
			//System.out.println("		EPBounds.act() ### : " + object.getPosition());
		}
	}

}
