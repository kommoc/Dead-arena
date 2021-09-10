package net.kommocgame.src.editor;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.github.czyzby.kiwi.util.gdx.scene2d.Alignment;

import net.kommocgame.src.Game;
import net.kommocgame.src.Loader;
import net.kommocgame.src.Path;
import net.kommocgame.src.gui.GuiBase;

public class GuiEditorNewLevel extends GuiBase {
	
	public Table table = new Table(Game.NEUTRALIZER_UI);
	public TextField field_name = new TextField("Level_1", Game.NEUTRALIZER_UI);
	
	public Button but_text = new Button(Game.NEUTRALIZER_UI);
	
	public Button but_apply = new Button(Game.NEUTRALIZER_UI);
	public Button but_cancel = new Button(Game.NEUTRALIZER_UI);
	
	private Image icon_apply = new Image(Loader.guiEditor("but/menu/icon_apply.png"));
	private Image icon_cancel = new Image(Loader.guiEditor("but/menu/icon_cancel.png"));
	
	/***** post edit *****/
	private Window window_newLevel = new Window("New level", Game.NEUTRALIZER_UI);
	
	private Label label_gridAStar = new Label("grid A*", Game.NEUTRALIZER_UI);
	private Label label_gridAStar_width= new Label("widht:", Game.NEUTRALIZER_UI);
	private Label label_gridAStar_height = new Label("height:", Game.NEUTRALIZER_UI);
	private Label label_level = new Label("name:", Game.NEUTRALIZER_UI);
	
	private TextField textField_width = new TextField("100", Game.NEUTRALIZER_UI);
	private TextField textField_height = new TextField("100", Game.NEUTRALIZER_UI);
	private TextField textField_scale = new TextField("1.0", Game.NEUTRALIZER_UI);
	
	/***** post edit *****/
	
	private EditorCore core;
	
	public GuiEditorNewLevel(Game game, EditorCore core) {
		super(game);
		this.core = core;
		
		Label label_apply = new Label("OK", Game.NEUTRALIZER_UI);
		Label label_cancel = new Label("CANCEL", Game.NEUTRALIZER_UI);
		Label label_text = new Label("New level", Game.NEUTRALIZER_UI);
		/*
		table.align(Alignment.CENTER.get());
		table.setBackground("panel");
		//group_stage.addActor(table);
		
		//but_text.add(label_text).align(Alignment.LEFT.get()).growX();
		//table.add(but_text).top().fillX().colspan(1);
		//table.row();
		
		but_apply.add(icon_apply).width(but_apply.getHeight()).height(but_apply.getHeight());
		but_apply.add(label_apply).padLeft(but_apply.getHeight() / 8f).padRight(but_apply.getHeight() / 8f);
		
		but_cancel.add(icon_cancel).width(but_cancel.getHeight()).height(but_cancel.getHeight());
		but_cancel.add(label_cancel).padLeft(but_cancel.getHeight() / 8f).padRight(but_cancel.getHeight() / 8f);
		
		but_apply.align(Alignment.CENTER.get());
		but_cancel.align(Alignment.CENTER.get());
		
		table.setSize(getRatio(1f/1.5f), getRatio(1f/2f));
		table.setPosition(Gdx.graphics.getWidth() / 2f, Gdx.graphics.getHeight() / 2f, Alignment.CENTER.get());
		table.add(field_name).center().expand();
		table.row();
		table.add(but_apply).right();
		table.add(but_cancel);
		*/
		
		/***** post edit *****/
		group_stage.addActor(window_newLevel);
		window_newLevel.align(Alignment.CENTER.get());
		
		but_apply.add(icon_apply).width(but_apply.getHeight()).height(but_apply.getHeight());
		but_apply.add(label_apply).padLeft(but_apply.getHeight() / 8f).padRight(but_apply.getHeight() / 8f);
		
		but_cancel.add(icon_cancel).width(but_cancel.getHeight()).height(but_cancel.getHeight());
		but_cancel.add(label_cancel).padLeft(but_cancel.getHeight() / 8f).padRight(but_cancel.getHeight() / 8f);
		
		but_apply.align(Alignment.CENTER.get());
		but_cancel.align(Alignment.CENTER.get());
		
		window_newLevel.setSize(getRatio(1f/1.5f), getRatio(1f/2f));
		window_newLevel.setPosition(Gdx.graphics.getWidth() / 2f, Gdx.graphics.getHeight() / 2f, Alignment.CENTER.get());
		window_newLevel.top().left();
		window_newLevel.add(label_level).left().width(window_newLevel.getWidth() / 6f).top();
		window_newLevel.add(field_name).colspan(2).expandX().left();
		window_newLevel.row();
		
		window_newLevel.add(label_gridAStar).colspan(3).center().padTop(window_newLevel.getHeight() / 6f).padBottom((window_newLevel.getHeight() / 12f));
		window_newLevel.row();
		
		window_newLevel.add(label_gridAStar_width).left().width(window_newLevel.getWidth() / 6f);
		window_newLevel.add(textField_width).colspan(2).expandX().left();
		window_newLevel.row();
		
		window_newLevel.add(label_gridAStar_height).left().width(window_newLevel.getWidth() / 6f);
		window_newLevel.add(textField_height).colspan(2).expandX().left();
		window_newLevel.row();
		
		window_newLevel.add(new Label("Grid scale:", Game.NEUTRALIZER_UI)).left().width(window_newLevel.getWidth() / 6f);
		window_newLevel.add(textField_scale).colspan(2).expandX().left();
		window_newLevel.row();
		
		window_newLevel.add(but_apply).colspan(2).right().expand().bottom();
		window_newLevel.add(but_cancel).expand().bottom().left();
		/***** post edit *****/
	}
	
	@Override
	public void update(SpriteBatch batch) {
		if(but_cancel.isChecked())
			game.guiManager.removeGui();
		
		if(but_apply.isChecked()) {
			System.out.println("	GuiEditorNewLevel.update  ### but_apply is pressed.");
			
			core.newLevel(field_name.getText(), game.world, game.mainCamera,
					Integer.valueOf(textField_width.getText()), Integer.valueOf(textField_height.getText()), 
					Float.valueOf(textField_scale.getText()));
			
			game.guiManager.removeGui();
			group_stage.setTouchable(Touchable.disabled);
		}
	}
}
