package net.kommocgame.src.gui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.github.czyzby.kiwi.util.gdx.scene2d.Alignment;

import net.kommocgame.src.Function;
import net.kommocgame.src.Game;
import net.kommocgame.src.Loader;
import net.kommocgame.src.editor.EditorButton;
import net.kommocgame.src.editor.GuiEditor;
import net.kommocgame.src.gui.DeadArena.DAGuiLoadingMenu;
@Deprecated
public class GuiMainMenuV2 extends GuiBase {
	
	public Texture tex_Menu_leftPanel_1 = Loader.guiMenuPanel("left_panel_1.png");

	public Image image_leftPanel = new Image(tex_Menu_leftPanel_1);
	public Image image_rightPanel = new Image(tex_Menu_leftPanel_1);
	public Image image_background = new Image(Loader.guiMenuBackground("main_background.png"));
	public Image image_profile = new Image(Loader.guiMenuPanel("rect_panel_1.png"));
	
	public EditorButton but_setNewGui = new EditorButton(Game.NEON_UI, Loader.objectsUnits("img_def_0.png"));
	
	public Function func = new Function(200, Interpolation.smooth);
	public Function func_openProfile = new Function(200, Interpolation.smooth);
	public Function func_openPlay = new Function(200, Interpolation.smooth);
	
	public float x_settings = getRatio(1f/2f);
	public float y_settings = getRatio(8f/12f);
	public float x1_settings = getRatio(1.1f) - x_settings;
	public float y1_settings = getRatio(8f/12f) - y_settings;
	
	public final int STAGE_ALL_CLOSE = 1;
	public final int STAGE_PLAY_MOVING = 2;
	public final int STAGE_PLAY_OPEN = 3;
	public final int STAGE_PLAY_CLOSING = 4;
	public final int STAGE_SETTINGS_MOVING = 5;
	public final int STAGE_SETTINGS_OPEN = 6;
	public final int STAGE_SETTINGS_CLOSING = 7;
	
	public int x_leftPanel;
	public int x_butLeftPanel;
	
	public int STAGE = STAGE_ALL_CLOSE;
	
	public boolean open_PLAY = false;
	public boolean open_SETTINGS = false;
	public boolean open_PROFILE = false;
	public boolean open_PLAY_single = false;
	
	public Button but;
	public Button but_settings;
	public Button but_play;
	public Button but_editor;
	public Curtains curtains;
	
	public float x = 0;
	public float x_but = 0;
	public float y = 0;
	
	public long lastTime = 0;
	public long lastTime_but = 0;
	
	/*
	 * FIXME TODO MOVING AND CLOSING STABILITY
	 */
	
	public GuiMainMenuV2(Game game) {
		super(game);
		//добавить полноразмерные шрифты
		group_stage.addActor(image_background);
		
		but_play = new Button(group_stage, 3, getRatio(0.5f/1f), (int)getRatio(1f/2f), (int)getRatio(9f/12f), new String[] {"BACK", "SINGLE", "COOP"}).backward();
		but_play.setPosition(getRatio(1f/2f), getRatio(9.5f/12f));

		but_editor = new Button(group_stage, 1, getRatio(0.5f/1f), (int)getRatio(1f/16f), (int)getRatio(1.8f/2f), new String[] {"Editor"});
		
		but_settings = new Button(group_stage, 4, getRatio(1f/2f), (int)getRatio(1f/2f), (int)getRatio(8f/12f), new String[] {"BACK", "CONTROL", "SCREEN", "SOUND"}).backward();
		group_stage.addActor(image_leftPanel);
		
		but = new Button(group_stage, 5, getRatio(1f/1.5f), (int)getRatio(1f/16f), (int)getRatio(8f/12f), new String[] {"PLAY", "SETTINGS", "PROFILE", "CREDITS", "EXIT"});
		curtains = new Curtains(group_stage, 0, 0, Gdx.graphics.getWidth()/2, Gdx.graphics.getHeight()/2, Gdx.graphics.getWidth()/2, getRatio(1f/2f));

		image_background.setSize(Gdx.graphics.getWidth(), Gdx.graphics.getWidth());
		this.setToCenter(image_background);
		image_background.setTouchable(Touchable.disabled);
		
		image_leftPanel.setPosition((int)getRatio(1f/64f), getRatio(1f/24f));
		image_leftPanel.setSize(getRatio(4f/5f), getRatio(4f/5f));
		image_leftPanel.setTouchable(Touchable.disabled);
		
		x_butLeftPanel = (int)but.getX();
		x_leftPanel = (int)image_leftPanel.getX();
		
		
		group_stage.addActor(but_setNewGui);
		but_setNewGui.setPosition(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), Alignment.TOPRIGHT.get());
		
	}
	
	public void initFunctions() {
		curtains.init();
		func.init();
		func_openProfile.init();
		func_openPlay.init();
	}
	
	@Override
	public void update(SpriteBatch batch) {
		
		//System.out.println("OBJ ::::: " + this.getObjInCursor() != null ? this.getObjInCursor() : null);
		if(lastTime + 2000 < System.currentTimeMillis()) {
			x = 0;
			lastTime = System.currentTimeMillis();
		} else {
			x = (float)(System.currentTimeMillis() - lastTime) / 1000f;
		}
		
		for(int i = 0; i < but.labels.size; i++) {
			y = MathUtils.sin(x * MathUtils.PI) * 68f;
			but.labels.get(i).setColor(62f/255f, 190f/255f, (187f + y)/255f, 1f);	//119 to 255
		}
		
		for(int i = 0; i < but_settings.labels.size; i++) {
			but_settings.labels.get(i).setColor(62f/255f, 190f/255f, (187f + y)/255f, 1f);	//119 to 255
		}
		
		for(int i = 0; i < but_play.labels.size; i++) {
			but_play.labels.get(i).setColor(62f/255f, 190f/255f, (187f + y)/255f, 1f);	//119 to 255
		}
		
		if(open_PROFILE) {
			open_SETTINGS = false;
			open_PLAY = false;
			open_PLAY_single = false;
			
			if(STAGE == STAGE_ALL_CLOSE) {
				open_PROFILE = false;
				func_openProfile.setBackward(false);
				func_openProfile.reload();
				func_openProfile.start();
				
				game.guiManager.addGui(new GuiProfileV2(game, this));
			}
		}
		
		if(open_PLAY_single) {
			open_PROFILE = false;
			open_SETTINGS = false;
			open_PLAY = false;
			
			if(STAGE == STAGE_ALL_CLOSE) {
				open_PLAY_single = false;
				func_openPlay.setBackward(false);
				func_openPlay.reload();
				func_openPlay.start();
				
				game.guiManager.addGui(new GuiPlaySingle(game, this));
			}
		}
		
		if(x_leftPanel + Gdx.graphics.getWidth() * func_openProfile.getValue() != image_leftPanel.getX()) {
			but.setPosition(x_butLeftPanel + Gdx.graphics.getWidth() * func_openProfile.getValue(), but.getY());
			image_leftPanel.setPosition(x_leftPanel + Gdx.graphics.getWidth() * func_openProfile.getValue(), image_leftPanel.getY());
		}
		
		initButtons();
		initFunctions();
		
		if(but_setNewGui.isChecked()) {
			but_setNewGui.setChecked(false);
			
			game.guiManager.reset();
			game.guiManager.addGui(new DAGuiLoadingMenu(game));
			
			//getGuiManager().setGui(new DAGuiLoadingMenu(game));
		}
	}
	
	public void initButtons() {
		if(STAGE == STAGE_ALL_CLOSE) {
			if(open_PLAY) {
				STAGE = STAGE_PLAY_MOVING;
				func.start();
			} else if(open_SETTINGS) {
				STAGE = STAGE_SETTINGS_MOVING;
				func.start();
			}
			
			if(but_play.isVisible() || but_settings.isVisible()) {
				but_play.setVisible(false);
				but_settings.setVisible(false);
			}
		}
		
		if(STAGE == STAGE_PLAY_MOVING) {
			if(!but_play.isVisible()) {
				but_play.setVisible(true);
			}
			
			but_play.setPosition(getRatio(1f/2f) + getRatio(1f/1.5f) * func.getValue(), getRatio(9.5f/12f));
			
			if(func.hasEnded()) {
				STAGE = STAGE_PLAY_OPEN;
				func.reload();
				func.setBackward(true);
			}
			
			if(!open_PLAY) {
				STAGE = STAGE_PLAY_CLOSING;
				
				if(!func.ended()) {
					func._pause();
					func.setBackward(true);
					func._continue();
				}
			}
		}
		
		if(STAGE == STAGE_PLAY_OPEN) {
			if(!open_PLAY) {
				STAGE = STAGE_PLAY_CLOSING;
				func.start();
			}
		}
		
		if(STAGE == STAGE_PLAY_CLOSING) {
			but_play.setPosition(getRatio(1f/2f) + getRatio(1f/1.61f) * func.getValue(), getRatio(9.5f/12f));
			
			if(func.hasEnded()) {
				STAGE = STAGE_ALL_CLOSE;
				func.reload();
				func.setBackward(false);
			}
			
			if(open_PLAY) {
				STAGE = STAGE_PLAY_MOVING;
				
				if(!func.ended()) {
					func._pause();
					func.setBackward(false);
					func._continue();
				}
			}
		}
		
		if(STAGE == STAGE_SETTINGS_MOVING) {
			if(!but_settings.isVisible()) {
				but_settings.setVisible(true);
			}
			
			but_settings.setPosition(getRatio(1f/2f) + getRatio(1f/1.6f) * func.getValue(), getRatio(9.5f/12f));
			
			if(func.hasEnded()) {
				STAGE = STAGE_SETTINGS_OPEN;
				func.reload();
				func.setBackward(true);
			}
			
			if(!open_SETTINGS) {
				STAGE = STAGE_SETTINGS_CLOSING;
				
				if(!func.ended()) {
					func._pause();
					func.setBackward(true);
					func._continue();
				}
			}
		}
		
		if(STAGE == STAGE_SETTINGS_OPEN) {
			if(!open_SETTINGS) {
				STAGE = STAGE_SETTINGS_CLOSING;
				func.start();
			}
		}
		
		if(STAGE == STAGE_SETTINGS_CLOSING) {
			but_settings.setPosition(getRatio(1f/2f) + getRatio(1f/1.7f) * func.getValue(), getRatio(9.5f/12f));
			
			if(func.hasEnded()) {
				STAGE = STAGE_ALL_CLOSE;
				func.reload();
				func.setBackward(false);
			}
			
			if(open_SETTINGS) {
				STAGE = STAGE_SETTINGS_MOVING;
				
				if(!func.ended()) {
					func._pause();
					func.setBackward(false);
					func._continue();
				}
			}
		}
		/*
		System.out.println("func is active: " + func.getState());
		System.out.println("STAGE: " + (STAGE == 1 ? "STAGE_ALL_CLOSE" : STAGE == 2 ? "STAGE_PLAY_MOVING" : STAGE == 3 ? "STAGE_PLAY_OPEN" : STAGE == 4
				? "STAGE_PLAY_CLOSING" : STAGE == 5 ? "STAGE_SETTINGS_MOVING" : STAGE == 6 ? "STAGE_SETTINGS_OPEN" : STAGE == 7 ? "STAGE_SETTINGS_CLOSING" : ""));
		System.out.println("func.getValue(): " + func.getValue());*/
	}
	
	@Override
	public void getActorPressedStage(Actor actor) {
		if(but.pressedButton(actor, 0)) {							//PLAY
			//SoundManager.playSound(SoundManager.sound_ui_button);
			open_SETTINGS = false;
			open_PLAY = true;
			open_PROFILE = false;
			//game.guiManager.reset();
			//game.guiManager.addGui(new GuiInGame(game));
			//game.world.setLevel(new LevelTEST(game.world));
		}
		
		if(but.pressedButton(actor, 1)) {							//SETTINGS
			//SoundManager.playSound(SoundManager.sound_ui_button);
			open_SETTINGS = true;
			open_PLAY = false;
			open_PROFILE = false;
		}
		
		if(but.pressedButton(actor, 2)) {							//PROFILE
			//SoundManager.playSound(SoundManager.sound_ui_button);
			open_PROFILE = true;
		}
		
		if(but.pressedButton(actor, 3)) {							//CREDITS
			//SoundManager.playSound(SoundManager.sound_ui_button);
		}
		
		if(but.pressedButton(actor, 4)) {							//EXIT
			//SoundManager.playSound(SoundManager.sound_ui_button);
			
			Gdx.app.exit();
		}
		
		if(but_settings.pressedButton(actor, 0)) {
			//SoundManager.playSound(SoundManager.sound_ui_button);
					//FIXME
		}
		
		if(but_settings.pressedButton(actor, 1)) {
			//SoundManager.playSound(SoundManager.sound_ui_button);
						//FIXME
		}
		
		if(but_settings.pressedButton(actor, 2)) {
			//SoundManager.playSound(SoundManager.sound_ui_button);
						//FIXME
		}
		
		if(but_settings.pressedButton(actor, 3)) {
			//SoundManager.playSound(SoundManager.sound_ui_button);
						//FIXME
			open_SETTINGS = false;
		}
		
		if(but_play.pressedButton(actor, 0)) {
			//SoundManager.playSound(SoundManager.sound_ui_button);
					//FIXME
			
			open_PLAY_single = true;
		}
		
		if(but_play.pressedButton(actor, 1)) {
			//SoundManager.playSound(SoundManager.sound_ui_button);
						//FIXME
		}
		
		if(but_play.pressedButton(actor, 2)) {
			//SoundManager.playSound(SoundManager.sound_ui_button);
						//FIXME
			open_PLAY = false;
		}
		
		if(but_editor.pressedButton(actor, 0)) {
			game.guiManager.reset();
			game.guiManager.addGui(new GuiEditor(game));
		}
	}
	
	@Override
	public void drawGui(SpriteBatch batch) {
		this.update(batch);
		////group.draw(batch, 1f);
		
		
	}
	
	@Override
	public void dispose() {
		
	}
	
	/*
	 * добавить случайное событие с мен€ющейс€ надписью на пару миллисекунд.
	 * 
	 * 
	 */
}
