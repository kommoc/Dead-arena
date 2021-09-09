package net.kommocgame.src.editor;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.SelectBox;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.utils.Array;
import com.github.czyzby.kiwi.util.gdx.scene2d.Alignment;

import net.kommocgame.src.Game;
import net.kommocgame.src.Loader;
import net.kommocgame.src.Path;
import net.kommocgame.src.gui.GuiBase;

public class GuiEditorNewEntity extends GuiBase {
	
	private EditorCore core;
	
	private Window table = new Window("Create new entity", Game.NEUTRALIZER_UI);
	
	private TextField name = new TextField("", Game.NEUTRALIZER_UI); /// сделать проверку на пустое имя.
	private SelectBox<String> entity_type = new SelectBox<String>(game.NEUTRALIZER_UI);
	
	public Button but_apply = new Button(Game.NEUTRALIZER_UI);
	public Button but_cancel = new Button(Game.NEUTRALIZER_UI);
	
	private Image icon_apply = new Image(Loader.guiEditor("but/menu/icon_apply.png"));
	private Image icon_cancel = new Image(Loader.guiEditor("but/menu/icon_cancel.png"));
	
	public GuiEditorNewEntity(Game game, EditorCore core) {
		super(game);
		this.core = core;
		group_stage.addActor(table);
		
		table.setSize(getRatio(1f/1.5f), getRatio(1f/2f));
		table.align(Alignment.CENTER.get());
		table.setPosition(Gdx.graphics.getWidth() / 2f, Gdx.graphics.getHeight() /2f, Alignment.CENTER.get());
		table.top();
		
		table.add(name).width(table.getWidth() / 2f);
		table.row();
		
		entity_type.getStyle().listStyle.selection.setRightWidth(10);
		entity_type.getStyle().listStyle.selection.setLeftWidth(20);
		Array<String> str_array = new Array<String>();
		for(Class entity_class : core.getListEntity()) {
			str_array.add(entity_class.getSimpleName());
		}
		
		entity_type.setItems(str_array);
		
		table.add(entity_type);
		table.row();
		
		Label label_apply = new Label("OK", Game.NEUTRALIZER_UI);
		Label label_cancel = new Label("CANCEL", Game.NEUTRALIZER_UI);
		
		but_apply.add(icon_apply).width(but_apply.getHeight()).height(but_apply.getHeight());
		but_apply.add(label_apply).padLeft(but_apply.getHeight() / 8f).padRight(but_apply.getHeight() / 8f);
		
		but_cancel.add(icon_cancel).width(but_cancel.getHeight()).height(but_cancel.getHeight());
		but_cancel.add(label_cancel).padLeft(but_cancel.getHeight() / 8f).padRight(but_cancel.getHeight() / 8f);
		
		but_apply.align(Alignment.CENTER.get());
		but_cancel.align(Alignment.CENTER.get());
		
		table.add(but_apply).right().bottom().expand();
		table.add(but_cancel).left().bottom().expand();
	}
	
	@Override
	public void update(SpriteBatch batch) {
		table.debugAll();
		if(but_cancel.isChecked())
			game.guiManager.removeGui();
		
		if(but_apply.isChecked()) {
			core.addEntity(name.getText(), entity_type.getSelected());
			
			game.guiManager.removeGui();
		}
	}
}
