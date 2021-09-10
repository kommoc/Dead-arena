package net.kommocgame.src.gui.DeadArena;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.Array;
import org.jrenner.smartfont.SmartFontGenerator;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.ProgressBar;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;
import com.github.czyzby.kiwi.util.gdx.scene2d.Alignment;

import net.kommocgame.src.Function;
import net.kommocgame.src.Game;
import net.kommocgame.src.Loader;
import net.kommocgame.src.gui.GuiBase;
import net.kommocgame.src.gui.GuiHandler;
import net.kommocgame.src.gui.game.DAGuiMainMenu_2;

public class DAGuiLoadingMenu extends GuiBase implements GuiHandler.PreInit {
	
	Table canvas = new Table(Game.COMMODORE_64_UI);
	ProgressBar bar = new ProgressBar(0, 10, 0.1f, false, Game.COMMODORE_64_UI);
	Button but_continue;
	Label label_loading;
	
	Function func_blend = new Function(1000, Interpolation.exp5);
	Function func_switch = new Function(1000, Interpolation.exp5);
	
	boolean isLoaded = false;
	boolean isContinued = false;
	
	public DAGuiLoadingMenu(Game game) {
		super(game);
		group_stage.addActor(canvas);
		
		// 15f default scale
		float scale_panel_1 = Gdx.graphics.getHeight() / 28f;
		System.out.println("DAGuiLoadingMenu(new instance) ### scale_panel_1: " + scale_panel_1);
		
		canvas.setSize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		
		NinePatch patch9 = Game.SCI_FI_ATLAS.createPatch("panel1");
		patch9.scale(scale_panel_1 / 15f, scale_panel_1 / 15f);
		NinePatchDrawable nine9 = new NinePatchDrawable(patch9);
		canvas.setBackground(nine9);
		canvas.setColor(1f, 1f, 1f, 0);
		
		//bar.setColor(Color.YELLOW);
		canvas.setPosition(Gdx.graphics.getWidth() / 2f, Gdx.graphics.getHeight() / 2f, Alignment.CENTER.get());
		
		SmartFontGenerator fontGenerator = new SmartFontGenerator();
		BitmapFont font_large = fontGenerator.createFont(Loader.getFontFile("gotthard"), "gotthard",
				(int)  (Gdx.graphics.getWidth() / 11.327f));
		
		Game.GENERATED_LABEL_STYLE_GOTTHARD_LARGE = new LabelStyle(font_large, Game.COLOR_TEXT);
		Game.GENERATED_LABEL_STYLE_GOTTHARD_LARGE_WHITE = new LabelStyle(font_large, Color.WHITE);
		
		bar.setAnimateInterpolation(Interpolation.linear);
		bar.setColor(Color.CYAN);
		
		label_loading = new Label("LOADING", Game.GENERATED_LABEL_STYLE_GOTTHARD_LARGE);
		
		canvas.add(label_loading);
		canvas.row();
		canvas.add(bar).center().growX().padLeft(getRatio(1f/8f * Game.ratio())).padRight(getRatio(1f/8f * Game.ratio()))
				.padTop(getRatio(1f/12f * Game.ratio()));
		
		label_loading.setPosition(Gdx.graphics.getWidth() / 2f, Gdx.graphics.getHeight() / 2f, Alignment.BOTTOM.get());
		func_blend.start();

	}
	
	@Override
	public void update(SpriteBatch batch) {
		game.assetManager.update();
		func_blend.init();
		func_switch.init();
		
		if(canvas.getColor().a != 1)
			canvas.setColor(1f, 1f, 1f, func_blend.getValue());
		
		bar.setValue(Game.assetManager.getProgress() * 10f);
		//System.out.println("DAGuiLoadingMenu.update() ### assetLoader: " + Game.assetManager.getProgress());
		
		if(game.assetManager.getProgress() == 1f && isLoaded == false) {
			game.setParams();
			isLoaded = true;
			
			func_blend.switchBackward();
			func_blend.reload();
			func_blend.start();
			
			but_continue = new Button(Game.NEON_UI);
			group_stage.addActor(but_continue);
			Label label_continue = new Label("CONTINUE", Game.GENERATED_LABEL_STYLE_GOTTHARD_LARGE);
			but_continue.add(label_continue);
			but_continue.setSize(label_continue.getWidth() + label_continue.getWidth() / 20f,
					label_continue.getHeight() + label_continue.getHeight());
			but_continue.setPosition(label_loading.getX(Alignment.CENTER.get()), label_loading.getY(Alignment.CENTER.get()),
					Alignment.CENTER.get());
			but_continue.setTouchable(Touchable.disabled);
			but_continue.setColor(1f, 1f, 1f, 0);
		} else if(isLoaded == true && label_loading.getColor().a != 0) {
			label_loading.setColor(1f, 1f, 1f, func_blend.getValue());
			
			if(func_blend.ended()) {
				func_blend.switchBackward();
				func_blend.reload();
				func_blend.start();
			}
		} else if(isLoaded == true && label_loading.getColor().a == 0 && but_continue.getColor().a != 1) {
			but_continue.setColor(1f, 1f, 1f, func_blend.getValue());
			
			if(func_blend.getValue() > 0.35f)
				but_continue.setTouchable(Touchable.enabled);
		}
		
		if(but_continue != null && but_continue.isChecked()) {
			but_continue.setChecked(false);
			
			isContinued = true;
			func_switch.start();
		}
		
		if(isContinued) {
			canvas.setColor(1, 1, 1, 1f - func_switch.getValue());
			but_continue.setColor(1, 1, 1, 1f - func_switch.getValue());
			
			if(func_switch.getValue() == 1) {
				getGuiManager().reset();
				getGuiManager().addGui(new DAGuiMainMenu_2(game));
			}
		}
	}

	@Override
	public void preInit() {
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
	}
}
