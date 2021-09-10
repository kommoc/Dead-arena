package net.kommocgame.src.gui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Cell;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;

import net.kommocgame.src.Game;
import net.kommocgame.src.control.KeyBinding;
import net.kommocgame.src.debug.CommandLine;

public class GuiConsole extends GuiBase {
	
	private Table screen_space = new Table(Game.COMMODORE_64_UI);
	private TextButton but_exit = new TextButton("EXIT", Game.COMMODORE_64_UI);
	private TextField command_line = new TextField(null, Game.COMMODORE_64_UI);
	private Table table_history = new Table(Game.COMMODORE_64_UI);
	private ScrollPane scroll_history;
	
	/** Need to closing processing. */
	private boolean flag = true;
	
	public GuiConsole(Game game) {
		super(game);
		
		but_exit.setSize(getRatio(1f/16f), getRatio(1f/16f));
		
		group_stage.addActor(screen_space);
		screen_space.setSize(Gdx.graphics.getWidth(), getRatio(1f/4f));
		screen_space.top().padLeft(getRatio(1f/16f));
		screen_space.top().padRight(getRatio(1f/16f));
		screen_space.setBackground("window");
		
		screen_space.setPosition(0, 0);
		//screen_space.add(table_history).expandY().width(Gdx.graphics.getWidth() / 1.4f);
		scroll_history = new ScrollPane(table_history, Game.NEUTRALIZER_UI);
		
		screen_space.add(scroll_history).expandY().width(Gdx.graphics.getWidth() / 1.4f);
		table_history.left();
		table_history.top();
		screen_space.row();
		screen_space.add(command_line).growX();
		screen_space.add(but_exit).top().right();
		
		for(int i = 0; i < CommandLine.command_history.size; i++) {
			Label label = CommandLine.command_history.get(i);
			table_history.add(label).left();
			table_history.row();
		}
		
		if(command_line != null)
			getStage().setKeyboardFocus(command_line);
		
	}
	
	@Override
	public void getActorPressedStage(Actor actor) {
		
		if(actor instanceof Label) {
			this.command_line.setText(((Label)actor).getText().toString());
			this.command_line.setCursorPosition(((Label)actor).getText().toString().length());
		}
	}
	
	@Override
	public void update(SpriteBatch batch) {
		if(flag) {
			scroll_history.setScrollY(scroll_history.getMaxY());
			flag = false;
		}
		
		if(but_exit.isChecked()) {
			System.out.println("resume");
			this.removeGui();
		}
		
		if(KeyBinding.getKeyRealised(Keys.ENTER)) {
			CommandLine.command_history.add(new Label(command_line.getText(), Game.COMMODORE_64_UI));
			table_history.add(CommandLine.command_history.peek()).left();
			
			try {
				CommandLine.instance.exec(this.command_line.getText());
			} catch(Exception e) {
				e.printStackTrace();
			}
			
			table_history.row();
			this.command_line.setText("");
			flag = true;
		}
	}
	
	public Table getHistory() {
		return table_history;
	}

}
