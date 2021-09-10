package net.kommocgame.src.editor;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.github.czyzby.kiwi.util.gdx.scene2d.Alignment;

import net.kommocgame.src.Game;
import net.kommocgame.src.Loader;
import net.kommocgame.src.gui.GuiBase;
import net.kommocgame.src.gui.GuiManager;

public class GuiEditorLoadLevel extends GuiBase {
	
	Window window = new Window("Load level", Game.NEUTRALIZER_UI);
	
	Table scroll_table = new Table(Game.NEUTRALIZER_UI);
	ScrollPane scroll_pane = new ScrollPane(scroll_table, Game.NEUTRALIZER_UI);
	
	ElementsGui.ButApply but_apply = new ElementsGui.ButApply();
	ElementsGui.ButCancel but_cancel = new ElementsGui.ButCancel();
	
	LevelNode choosed_node;
	
	EditorCore core;
	
	public GuiEditorLoadLevel(Game game, EditorCore core) {
		super(game);
		this.core = core;
		group_stage.addActor(window);
		
		window.setSize(Gdx.graphics.getWidth() / 2f, Gdx.graphics.getHeight() / 2f);
		window.setPosition(Gdx.graphics.getWidth() / 2f, Gdx.graphics.getHeight() / 2f, Alignment.CENTER.get());
		window.setMovable(false);
		
		window.add(scroll_pane).expand().colspan(2).left().top();
		window.row();
		window.add(but_apply).expandX().right().bottom();
		window.add(but_cancel).bottom().expandX().left();
		
		scroll_table.left().top();
		
		for(int i = 0; i < Loader.getLevel("").list().length; i++) {
			scroll_table.add(new LevelNode(Loader.getLevel("").list()[i])).growX();
			scroll_table.row();
		}
		
		window.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				LevelNode target = GuiManager.getInstanceOf(scroll_table, LevelNode.class, event.getTarget());
				if(target != null) {
					System.out.println("	GuiLoadLevel.ClickListener().clicked() ### setNode(): " + target);
					GuiEditorLoadLevel.this.setNode(target);;
				}
				
				super.clicked(event, x, y);
			}
		});
	}
	
	void setNode(LevelNode node) {
		System.out.println("	GuiLoadLevel.setNode ### setColor for nodes. ");
		for(int i = 0; i < scroll_table.getChildren().size; i++) {
			scroll_table.getChildren().get(i).setColor(Color.WHITE);
		}
		
		choosed_node = node;
		node.setColor(Color.RED);
	}
	
	@Override
	public void update(SpriteBatch batch) {
		super.update(batch);
		if(but_cancel.isChecked()) {
			but_cancel.setChecked(false);
			
			game.guiManager.removeGui();
		}
		
		if(but_apply.isChecked()) {
			but_apply.setChecked(false);
			
			if(choosed_node != null)
				core.loadLevel(choosed_node.getFile());
		}
		
		window.debugAll();
	}
	
	class LevelNode extends Table {
		
		private String levelpath;
		private FileHandle file;
		
		private TextButton but_level = new TextButton("level_", Game.NEUTRALIZER_UI);
		
		public LevelNode(FileHandle fileHandle) {
			file = fileHandle;
			this.levelpath = file.path();
			if(!file.exists())
				System.out.println("	GuiLoadLevel.LevelNode(new instance) ### file not found!");
			
			this.add(but_level);
			but_level.setText(file.file().getName());
		}
		
		@Override
		public void setColor(Color color) {
			super.setColor(color);
			but_level.setColor(color);
		}
		
		public String getPath() {
			return levelpath;
		}
		
		public FileHandle getFile() {
			return file;
		}
	}
	
}
