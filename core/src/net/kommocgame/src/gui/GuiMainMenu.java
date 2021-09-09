package net.kommocgame.src.gui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;

import box2dLight.PointLight;
import net.kommocgame.src.Game;
import net.kommocgame.src.Path;
import net.kommocgame.src.control.InputHandler;
import net.kommocgame.src.world.level.LevelTEST;

@Deprecated
public class GuiMainMenu extends GuiBase {
	
	public Image mainScreen = new Image(new Texture(Path.gui("mainMenu_new.png")));					//TODO
	
	public Image newGame = new Image(new Texture(Path.main("button_idle.png")));					//TODO
	public Image indicator_newGame = new Image(new Texture(Path.main("green_indicator.png")));		//TODO
	public PointLight indicator_light;
	public PointLight light_TEST;
	
	public Image loadGame = new Image(new Texture(Path.main("button_idle.png")));					//TODO
	public Image settings = new Image(new Texture(Path.main("button_idle.png")));					//TODO
	
	public Label label_newGame = new Label("NEW GAME", Game.NEUTRALIZER_UI);
	public Label label_loadGame = new Label("LOAD GAME", Game.NEUTRALIZER_UI);
	public Label label_settings = new Label("SETTINGS", Game.NEUTRALIZER_UI);
	
	public GuiMainMenu(Game game) {
		super(game);
		group_stage.addActor(mainScreen);
		
		indicator_light = new PointLight(game.world.physics.guiRayHandler, 32, Color.GREEN, 3, 0, 0);
		this.addButtons();
		this.addNames();
		
		mainScreen.setPosition(0, 0);
		mainScreen.setSize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		
		//this.addLabels();
		light_TEST = new PointLight(game.world.physics.guiRayHandler, 32, Color.RED, 10, 0, 0);
		
		game.world.physics.guiRayHandler.setAmbientLight(0, 0, 0, 0);
		game.world.physics.guiRayHandler.setAmbientLight(1f);					//FIXME
	}
	
	public void addButtons() {
		group_stage.addActor(newGame);
		this.setLeftSide(newGame, false);
		//translate(newGame, getRatio(1f/7f), -getRatio(1f/16f)*2);
		newGame.setSize(getRatio(1f/2.5f), getRatio(1f/2.5f) / 4);
		
		group_stage.addActor(indicator_newGame);
		this.setLeftSide(indicator_newGame, false);
		translate(indicator_newGame, getRatio(1f/24f), -getRatio(1f/16f)*2 + getRatio(1f/64f));
		indicator_light.setPosition(debug_x, debug_y);
		indicator_light.setColor(Color.GREEN);
		indicator_newGame.setSize(getRatio(1f/14f), getRatio(1f/14f));
		
		//group.addActor(loadGame);
		this.setLeftSide(loadGame, false);
		translate(loadGame, 0, -getRatio(1f/12f) * 2);
		loadGame.setSize(getRatio(1f), getRatio(1f/8f));
		
		//group.addActor(settings);
		this.setLeftSide(settings, true);
		translate(settings, 0, -getRatio(1f/4f) * 3);
		settings.setSize(getRatio(1f), getRatio(1f/8f));
	}
	
	public void addLabels() {
		this.setLeftSide(label_newGame, true);
		label_newGame.setTouchable(Touchable.disabled);
		translate(label_newGame, getRatio(1f/4f), -getRatio(1f/4f));
		label_newGame.setFontScale(getRatio(1f/256f));
		label_newGame.setColor(Color.LIGHT_GRAY);
		group_stage.addActor(label_newGame);
		
		this.setLeftSide(label_loadGame, true);
		label_loadGame.setTouchable(Touchable.disabled);
		translate(label_loadGame, getRatio(1f/4f), -getRatio(1f/4f) * 2);
		label_loadGame.setFontScale(getRatio(1f/256f));
		label_loadGame.setColor(Color.LIGHT_GRAY);
		group_stage.addActor(label_loadGame);
		
		this.setLeftSide(label_settings, true);
		label_settings.setTouchable(Touchable.disabled);
		translate(label_settings, getRatio(1f/4f), -getRatio(1f/4f) * 3);
		label_settings.setFontScale(getRatio(1f/256f));
		label_settings.setColor(Color.LIGHT_GRAY);
		group_stage.addActor(label_settings);
	}
	
	public void addNames() {
		mainScreen.setName("mainMenu");
		newGame.setName("newGame");
		loadGame.setName("loadGame");
		settings.setName("settings");
	}
	
	@Override
	public void update(SpriteBatch batch) {
		//this.debug(0.01f);
		if(this.getObjInCursor() == newGame) {
			label_newGame.setColor(Color.RED);
		} else {
			label_newGame.setColor(Color.LIGHT_GRAY);
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
		
		newGame.setSize(getRatio(1f/2.5f), getRatio(1f/2.5f) / 4);
		newGame.setPosition(getRatioY(1f/4f), getRatio(1f/1.4f), ALIGNMENT.CENTER.get());
		indicator_newGame.setPosition(getRatioY(1f/18f), getRatio(1f/1.4f), ALIGNMENT.CENTER.get());
		
		indicator_light.setPosition(getBox2dRatioX(1f/19.05f),getBox2dRatioY(1f/1.4f));
		indicator_light.setDistance(game.LIGHT_VALUE / 8f);
		light_TEST.setPosition((float)InputHandler.GUI_mouse_x * game.mainCamera.zoom, (float)InputHandler.GUI_mouse_y * game.mainCamera.zoom);
		System.out.println("GDX: " + (float)Gdx.graphics.getWidth());
		System.out.println("CAM: " + game.mainCamera.viewportWidth);
		game.world.physics.guiRayHandler.setShadows(false);
		game.world.physics.guiRayHandler.setCombinedMatrix(game.mainCamera);
		game.world.physics.guiRayHandler.updateAndRender();
	}
	
	@Override
	public void getActorPressedStage(Actor actor) {
		if(actor == this.newGame) {
			game.guiManager.reset();
			game.guiManager.addGui(new GuiInGame(game));
			game.world.setLevel(new LevelTEST(game.world));
		}
	}
	
	@Override
	public void drawGui(SpriteBatch batch) {
		////group.draw(batch, 1f);
		this.update(batch);
	}
	
	@Override
	public void dispose() {
		
	}
}
