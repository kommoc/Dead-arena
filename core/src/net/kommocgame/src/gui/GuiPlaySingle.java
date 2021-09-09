package net.kommocgame.src.gui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.ai.btree.decorator.Random;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.github.czyzby.kiwi.util.gdx.scene2d.Alignment;

import net.kommocgame.src.Function;
import net.kommocgame.src.Game;
import net.kommocgame.src.Loader;
import net.kommocgame.src.Path;
import net.kommocgame.src.world.level.LevelTEST;

@Deprecated
public class GuiPlaySingle extends GuiBase {
	
	public Texture tex_button = Loader.guiMenuBut("play/butPlay.png");
	
	public Function func_but_effect = new Function(1000, Interpolation.exp5Out);
	
	public Image img_company = new Image(tex_button);
	public Image img_other = new Image(tex_button);
	
	public Image sfx_company_label = new Image(Loader.guiMenuBut("play/butPlay_label.png"));
	public Image sfx_company_label_bleeding = new Image(Loader.guiMenuBut("play/butPlay_label_bleeding.png"));
	public Image sfx_company_effect_bleeding = new Image(Loader.guiMenuBut("play/butPlay_effect_bleeding.png"));
	public Image sfx_company_effect = new Image(Loader.guiMenuBut("play/butPlay_effect_2.png"));
	
	private long lastTime = 0;
	private long lastTimeEvent = 0;
	private int timer = 0;
	
	private final int STATE_BLINKING = 2;
	private final int STATE_BLEEDING = 1;
	private final int STATE_NORMAL = 0;
	private int STATE = STATE_NORMAL;
	
	private java.util.Random random = new java.util.Random();
	private int range_effect = 70;
	private int range_effect_bleeding = 50;
	private float deegre = 0f;
	
	public GuiPlaySingle(Game game, GuiMainMenuV2 gui) {
		super(game);
		img_company.setSize(getRatio(1f/1.5f), getRatio(1f/1.5f/2f));
		img_company.setPosition(Gdx.graphics.getWidth() / 2f, Gdx.graphics.getHeight() / 2f, Alignment.RIGHT.get());
		group_stage.addActor(img_company);
		
		img_other.setSize(getRatio(1f/1.5f), getRatio(1f/1.5f/2f));
		img_other.setRotation(180f);
		img_other.setPosition(Gdx.graphics.getWidth() / 2f + img_company.getWidth() - getRatio(1f/1.5f) / 7f,
				img_company.getY(Alignment.TOP.get()), Alignment.BOTTOMLEFT.get());
		group_stage.addActor(img_other);
		
		sfx_company_effect.setSize(getRatio(1f/1.5f), getRatio(1f/1.5f/2f));
		group_stage.addActor(sfx_company_effect);
		sfx_company_effect.setPosition(Gdx.graphics.getWidth() / 2f, Gdx.graphics.getHeight() / 2f, Alignment.RIGHT.get());
		
		sfx_company_label.setSize(getRatio(1f/1.5f), getRatio(1f/1.5f/2f));
		group_stage.addActor(sfx_company_label);
		sfx_company_label.setPosition(Gdx.graphics.getWidth() / 2f, Gdx.graphics.getHeight() / 2f, Alignment.RIGHT.get());
		
		sfx_company_label_bleeding.setSize(getRatio(1f/1.5f), getRatio(1f/1.5f/2f));
		group_stage.addActor(sfx_company_label_bleeding);
		sfx_company_label_bleeding.setPosition(Gdx.graphics.getWidth() / 2f, Gdx.graphics.getHeight() / 2f, Alignment.RIGHT.get());
		
		sfx_company_effect_bleeding.setSize(getRatio(1f/1.5f), getRatio(1f/1.5f/2f));
		group_stage.addActor(sfx_company_effect_bleeding);
		sfx_company_effect_bleeding.setPosition(Gdx.graphics.getWidth() / 2f, Gdx.graphics.getHeight() / 2f, Alignment.RIGHT.get());
		
		sfx_company_effect_bleeding.setColor(1, 1f, 1f, 0f);
		sfx_company_label_bleeding.setColor(1, 1f, 1f, 0f);
		
		func_but_effect.start();
		
		sfx_company_effect.setOrigin(Alignment.CENTER.get());
		sfx_company_effect_bleeding.setOrigin(Alignment.CENTER.get());
		sfx_company_label.setOrigin(Alignment.CENTER.get());
		sfx_company_label_bleeding.setOrigin(Alignment.CENTER.get());
		
		sfx_company_effect.setTouchable(Touchable.disabled);
		sfx_company_effect_bleeding.setTouchable(Touchable.disabled);
		sfx_company_label.setTouchable(Touchable.disabled);
		sfx_company_label_bleeding.setTouchable(Touchable.disabled);
	}
	
	public void initFunctions() {
		func_but_effect.init();
	}
	
	public void update(SpriteBatch batch) {
		if(Gdx.input.isKeyPressed(Keys.O)) {
			STATE = STATE_BLEEDING;
			timer = 200;
			lastTime = System.currentTimeMillis();
		}
		
		if(!func_but_effect.getPause()) {
			sfx_company_effect.setColor(1f, 1f, 1f, 0.5f + func_but_effect.getValue() / 2f);
		}
		
		if(func_but_effect.hasEnded()) {
			func_but_effect.reload();
			func_but_effect.switchBackward();
			func_but_effect.start();
		}
		
		if(lastTimeEvent + 1000 <= System.currentTimeMillis()) {
			lastTimeEvent = System.currentTimeMillis();
			
			if(random.nextInt(8) == 1) {
				STATE = STATE_BLEEDING;
				timer = 350;
				lastTime = System.currentTimeMillis();
			}
		}
		
		if(STATE == STATE_BLEEDING) {
			func_but_effect._pause();
			
			if(lastTime + timer >= System.currentTimeMillis()) {
				sfx_company_effect_bleeding.setColor(1, 1f, 1f, (float)range_effect_bleeding / 256f + (float)random.nextInt(150) / 256f);
				sfx_company_label_bleeding.setColor(1, 1f, 1f, 150f / 256f + (float)random.nextInt(range_effect) / 256f);
				sfx_company_effect.setColor(1f, 1f, 1f, (float)random.nextInt(256) / 256f);
				
				deegre = (30f - (float)random.nextInt(60)) / 32f;
				
				sfx_company_effect.setRotation(deegre);
				sfx_company_effect_bleeding.setRotation(deegre);
				sfx_company_label.setRotation(deegre);
				sfx_company_label_bleeding.setRotation(deegre);
			} else {
				deegre = 0;
				
				sfx_company_effect.setRotation(deegre);
				sfx_company_effect_bleeding.setRotation(deegre);
				sfx_company_label.setRotation(deegre);
				sfx_company_label_bleeding.setRotation(deegre);
				
				sfx_company_effect_bleeding.setColor(1, 1f, 1f, 0f);
				sfx_company_label_bleeding.setColor(1, 1f, 1f, 0f);
				STATE = STATE_NORMAL;
				func_but_effect._continue();
			}
		} if(STATE == STATE_BLINKING) {
			
			STATE = STATE_NORMAL;
		}
		
		initFunctions();
	}
	
	public void getActorPressedStage(Actor actor) {
		if(actor == img_company) {
			game.guiManager.reset();
			game.guiManager.addGui(new GuiInGame(game));
			game.world.setLevel(new LevelTEST(game.world));
		}
	}
	
}
