package net.kommocgame.src.gui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;

import net.kommocgame.src.Game;
import net.kommocgame.src.Path;
import net.kommocgame.src.gui.DeadArena.DAGuiSettings;
import net.kommocgame.src.gui.game.DAGuiMainMenu_2;

@Deprecated
public class GuiMainMenuInGame extends GuiBase {
	
	public Image backGround = new Image(new Texture(Path.gui("mainMenuInGame.png")));			//TODO
	
	public Image resume = new Image(new Texture(Path.gui("button1.png")));						//TODO
	public Image saveGame = new Image(new Texture(Path.gui("button1.png")));					//TODO
	public Image loadGame = new Image(new Texture(Path.gui("button1.png")));					//TODO
	public Image settings = new Image(new Texture(Path.gui("button1.png")));					//TODO
	public Image exitToMain = new Image(new Texture(Path.gui("button1.png")));					//TODO
	public Image exit = new Image(new Texture(Path.gui("button1.png")));						//TODO
	
	public Label label_resume = new Label("RESUME", Game.NEUTRALIZER_UI);
	public Label label_saveGame = new Label("SAVE GAME", Game.NEUTRALIZER_UI);
	public Label label_loadGame = new Label("LOAD GAME", Game.NEUTRALIZER_UI);
	public Label label_settings = new Label("SETTINGS", Game.NEUTRALIZER_UI);
	public Label label_exitToMain = new Label("EXIT TO MENU", Game.NEUTRALIZER_UI);
	public Label label_exit = new Label("EXIT", Game.NEUTRALIZER_UI);
	
	public GuiMainMenuInGame(Game game) {
		super(game);
		this.group_stage.addActor(backGround);
		backGround.setSize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		
		this.addNames();
		this.addButtons();
		this.addLabels();
	}
	
	public void addButtons() {
		group_stage.addActor(resume);
		this.setLeftSide(resume, true);
		translate(resume, 0, -getRatio(1f/6f));
		resume.setSize(getRatio(1f), getRatio(1f/12f));
		
		group_stage.addActor(saveGame);
		this.setLeftSide(saveGame, true);
		translate(saveGame, 0, -getRatio(1f/7f) * 2);
		saveGame.setSize(getRatio(1f), getRatio(1f/12f));
		
		group_stage.addActor(loadGame);
		this.setLeftSide(loadGame, true);
		translate(loadGame, 0, -getRatio(1f/7f) * 3);
		loadGame.setSize(getRatio(1f), getRatio(1f/12f));
		
		group_stage.addActor(settings);
		this.setLeftSide(settings, true);
		translate(settings, 0, -getRatio(1f/7f) * 4);
		settings.setSize(getRatio(1f), getRatio(1f/12f));
		
		group_stage.addActor(exitToMain);
		this.setLeftSide(exitToMain, true);
		translate(exitToMain, 0, -getRatio(1f/7f) * 5);
		exitToMain.setSize(getRatio(1f), getRatio(1f/12f));
		
		group_stage.addActor(exit);
		this.setLeftSide(exit, true);
		translate(exit, 0, -getRatio(1f/7f) * 6);
		exit.setSize(getRatio(1f), getRatio(1f/12f));
	}
	
	public void addLabels() {
		this.setLeftSide(label_resume, true);
		label_resume.setTouchable(Touchable.disabled);
		translate(label_resume, getRatio(1f/4f), -getRatio(1.1f/6f));
		label_resume.setFontScale(getRatio(1f/256f));
		label_resume.setColor(Color.LIGHT_GRAY);
		group_stage.addActor(label_resume);
		
		this.setLeftSide(label_saveGame, true);
		label_saveGame.setTouchable(Touchable.disabled);
		translate(label_saveGame, getRatio(1f/4f), -getRatio(1f/7f) * 2.1f);
		label_saveGame.setFontScale(getRatio(1f/256f));
		label_saveGame.setColor(Color.LIGHT_GRAY);
		group_stage.addActor(label_saveGame);
		
		this.setLeftSide(label_loadGame, true);
		label_loadGame.setTouchable(Touchable.disabled);
		translate(label_loadGame, getRatio(1f/4f), -getRatio(1f/7f) * 3.1f);
		label_loadGame.setFontScale(getRatio(1f/256f));
		label_loadGame.setColor(Color.LIGHT_GRAY);
		group_stage.addActor(label_loadGame);
		
		this.setLeftSide(label_settings, true);
		label_settings.setTouchable(Touchable.disabled);
		translate(label_settings, getRatio(1f/4f), -getRatio(1f/7f) * 4.1f);
		label_settings.setFontScale(getRatio(1f/256f));
		label_settings.setColor(Color.LIGHT_GRAY);
		group_stage.addActor(label_settings);
		
		this.setLeftSide(label_exitToMain, true);
		label_exitToMain.setTouchable(Touchable.disabled);
		translate(label_exitToMain, getRatio(1f/4f), -getRatio(1f/7f) * 5.1f);
		label_exitToMain.setFontScale(getRatio(1f/256f));
		label_exitToMain.setColor(Color.LIGHT_GRAY);
		group_stage.addActor(label_exitToMain);
		
		this.setLeftSide(label_exit, true);
		label_exit.setTouchable(Touchable.disabled);
		translate(label_exit, getRatio(1f/4f), -getRatio(1f/7f) * 6.1f);
		label_exit.setFontScale(getRatio(1f/256f));
		label_exit.setColor(Color.LIGHT_GRAY);
		group_stage.addActor(label_exit);
	}
	
	public void addNames() {
		backGround.setName("mainMenuInGame");
		resume.setName("resume");
		saveGame.setName("saveGame");
		loadGame.setName("loadGame");
		settings.setName("settings");
		exitToMain.setName("exitToMain");
		exit.setName("exit");
	}
	
	@Override
	public void getActorPressedStage(Actor actor) {
		if(actor == resume)
			game.guiManager.removeGui();
		
		if(actor == saveGame)
			System.out.println(game.guiManager.toString());
		
		if(actor == settings) {
			System.out.println("SIZE OF WORLD ENTITYLIST: " + game.world.getEngine().getEntities().size());
			
			game.guiManager.addGui(new DAGuiSettings(game));
		}
		
		if(actor == exitToMain) {
			game.world.deleteLevel();
			
			game.guiManager.reset();
			game.guiManager.addGui(new DAGuiMainMenu_2(game));
		}
	}
	
	@Override
	public void update(SpriteBatch batch) {
		if(this.getObjInCursor() == resume) {
			label_resume.setColor(Color.RED);
		} else {
			label_resume.setColor(Color.LIGHT_GRAY);
		}
		
		if(this.getObjInCursor() == saveGame) {
			label_saveGame.setColor(Color.RED);
		} else {
			label_saveGame.setColor(Color.LIGHT_GRAY);
		}
		
		if(this.getObjInCursor() == loadGame) {
			label_loadGame.setColor(Color.RED);
		} else {
			label_loadGame.setColor(Color.LIGHT_GRAY);
		}
		
		if(this.getObjInCursor() == settings) {
			label_settings.setColor(Color.RED);
		} else {
			label_settings.setColor(Color.LIGHT_GRAY);
		}
		
		if(this.getObjInCursor() == exitToMain) {
			label_exitToMain.setColor(Color.RED);
		} else {
			label_exitToMain.setColor(Color.LIGHT_GRAY);
		}
		
		if(this.getObjInCursor() == exit) {
			label_exit.setColor(Color.GREEN);
		} else {
			label_exit.setColor(Color.RED);
		}
	}
}
