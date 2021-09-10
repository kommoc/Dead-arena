package net.kommocgame.src.editor;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.ui.TextField.TextFieldFilter;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.github.czyzby.kiwi.util.gdx.scene2d.Alignment;

import net.kommocgame.src.Game;
import net.kommocgame.src.editor.ElementsGui.ButApply;
import net.kommocgame.src.editor.ElementsGui.ButCancel;
import net.kommocgame.src.gui.GuiBase;

public class GuiEditorLevelSettings extends GuiBase {
	
	private EditorCore core;
	
	Window window;
	
	private float GUI_WIDTH;
	
	ButApply but_apply = new ButApply();
	ButCancel but_cancel = new ButCancel();
	
	TextField text_width_a_star = new TextField("", Game.NEUTRALIZER_UI);
	TextField text_height_a_star = new TextField("", Game.NEUTRALIZER_UI);
	ButApply but_a_star_apply = new ButApply();
	
	TextField text_level_name = new TextField("", Game.NEUTRALIZER_UI);
	
	TextField text_scale_a_star = new TextField("", Game.NEUTRALIZER_UI);
	
	TextFieldFilter filter_aStar = new TextFieldFilter() {
		@Override
		public boolean acceptChar(TextField textField, char c) {
			if(c >= '1' && c <= '9' || c == '0')
				return true;
			
			return false;
		}
	};
	
	TextFieldFilter filter_float = new TextFieldFilter() {
		@Override
		public boolean acceptChar(TextField textField, char c) {
			if(c >= '1' && c <= '9' || c == '0' || c == '.' && !textField.getText().contains("."))
				return true;
			
			return false;
		}
	};
	
	public GuiEditorLevelSettings(Game game, EditorCore core) {
		super(game);
		this.core = core;
		
		window = new Window("Level settings", Game.NEUTRALIZER_UI);
		
		this.group_stage.addActor(window);
		GUI_WIDTH = getRatio(1f/0.9f);
		window.setSize(GUI_WIDTH, getRatio(1f/1.4f));
		window.setPosition(Gdx.graphics.getWidth() / 2f, Gdx.graphics.getHeight() / 2f, Alignment.CENTER.get());
		
		Table table_main = new Table(Game.NEUTRALIZER_UI);
		ScrollPane scroll_pane = new ScrollPane(table_main, Game.NEUTRALIZER_UI);
		window.add(scroll_pane).grow().left().colspan(2);
		window.row();
		window.add(but_apply).right().bottom();
		window.add(but_cancel).left().bottom();
		table_main.left().top();
		window.left().top();
		
		table_main.add(new Label("A* settings", Game.NEUTRALIZER_UI)).center().expandX();
		table_main.row();
		
		Table table_a_star_settings = new Table(Game.NEUTRALIZER_UI);
		table_a_star_settings.add(new Label("width: ", Game.NEUTRALIZER_UI));
		table_a_star_settings.add(text_width_a_star);
		table_a_star_settings.row();
		table_a_star_settings.add(new Label("height: ", Game.NEUTRALIZER_UI));
		table_a_star_settings.add(text_height_a_star);
		table_a_star_settings.add(new Label("scale: ", Game.NEUTRALIZER_UI));
		table_a_star_settings.add(text_scale_a_star);
		table_main.add(table_a_star_settings).expandX().left();
		table_main.add(but_a_star_apply);
		table_main.row();
		text_width_a_star.setTextFieldFilter(filter_aStar);
		text_height_a_star.setTextFieldFilter(filter_aStar);
		text_scale_a_star.setTextFieldFilter(filter_float);
		
		table_main.add(new Label("--------------------------------------------------------------", Game.NEUTRALIZER_UI)).center().expandX();
		table_main.row();
		table_main.add(new Label("Level name: ", Game.NEUTRALIZER_UI)).left();
		table_main.add(text_level_name).width(GUI_WIDTH * 1/3).left();
		
		if(core.getLevel() != null) {
			text_height_a_star.setText("" + core.getLevel().getGridNodes().getWidth());
			text_width_a_star.setText("" + core.getLevel().getGridNodes().getWidth());
			text_scale_a_star.setText("" + core.getLevel().getGridNodes().getScale());
			
			text_level_name.setText("" + core.getLevel().getLevelName());
		} else {
			text_height_a_star.setText("");
			text_width_a_star.setText("");
			text_scale_a_star.setText("");
			text_level_name.setText("Level is not construct");
		}
	}
	
	@Override
	public void update(SpriteBatch batch) {
		super.update(batch);
		
		if(but_apply.isChecked()) {
			but_apply.setChecked(false);
			removeGui();
			
			core.getLevel().setLevelName(text_level_name.getText());
		}
		
		if(but_cancel.isChecked()) {
			but_cancel.setChecked(false);
			removeGui();
		}
		
		if(core.getLevel() != null) {
			if(but_a_star_apply.isChecked()) {
				but_a_star_apply.setChecked(false);
				System.out.println("GuiEditorLevelSettings.update() ### A* grid is not recreated!");
				core.getLevel().createAStarGrid(Integer.valueOf(text_width_a_star.getText()),
						Integer.valueOf(text_height_a_star.getText()), Float.valueOf(text_scale_a_star.getText()));
			}
		}
	}

}
