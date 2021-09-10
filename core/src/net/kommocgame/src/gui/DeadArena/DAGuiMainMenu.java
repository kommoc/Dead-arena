package net.kommocgame.src.gui.DeadArena;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextArea;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;
import com.github.czyzby.kiwi.util.gdx.scene2d.Alignment;

import net.kommocgame.src.Function;
import net.kommocgame.src.Game;
import net.kommocgame.src.Loader;
import net.kommocgame.src.editor.GuiEditor;
import net.kommocgame.src.gui.GuiBase;
import net.kommocgame.src.gui.GuiHandler;

@Deprecated
public class DAGuiMainMenu extends GuiBase implements GuiHandler.PreInit {
	
	@Deprecated
	Function func_blend = new Function(750, Interpolation.exp5);
	@Deprecated
	Function func_switch = new Function(400, Interpolation.exp5);
	
	boolean isGuiOpen = false;
	private DAGuiPlayMenu gui_play;
	
	Table canvas = new Table(Game.NEON_UI);
	
	Image image_background = new Image(Loader.guiMenuBackground("main_background.png"));
	Image img_logo = new Image(Loader.guiMenuBackground("Main_menu_logo.png"));
	Image img_label_logo = new Image(Loader.guiIcon("Main_menu_label.png"));
	
	@Deprecated
	Button but_play		= new Button(Game.NEON_UI);
	@Deprecated
	Button but_settings	= new Button(Game.NEON_UI);
	@Deprecated
	Button but_exit		= new Button(Game.NEON_UI);
	@Deprecated
	Button but_credits	= new Button(Game.NEON_UI);
	
	@Deprecated
	Button but_editor	= new Button(Game.NEON_UI);
	@Deprecated
	Button but_profile	= new Button(Game.NEON_UI);
	
	@Deprecated
	Button but_reset_profile		= new Button(Game.NEON_UI);
	@Deprecated
	Button but_reset_preferences	= new Button(Game.NEON_UI);
	
	/** Test item. */
	Table table_profile = new Table(Game.NEON_UI);
	TextArea text_profile = new TextArea("", Game.NEON_UI);
	TextArea text_preferences = new TextArea("", Game.NEON_UI);
	
	public DAGuiMainMenu(Game game) {
		super(game);
		group_stage.addActor(image_background); //@Deprecated
		group_stage.addActor(img_logo);
		group_stage.addActor(img_label_logo);
		group_stage.addActor(canvas);
		canvas.setSize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		canvas.setPosition(0, 0);
		
		// @Deprecated
		image_background.setSize(Gdx.graphics.getWidth(), Gdx.graphics.getWidth());
		this.setToCenter(image_background);
		image_background.setTouchable(Touchable.disabled);
		image_background.setColor(1f, 1f, 1f, 0);
		
		img_logo.setSize(Gdx.graphics.getWidth() / 2f, Gdx.graphics.getWidth() / 2f);
		img_logo.setPosition(Gdx.graphics.getWidth() / 4f, Gdx.graphics.getHeight() / 2f, Alignment.CENTER.get());
		img_logo.setTouchable(Touchable.disabled);
		img_logo.setColor(1f, 1f, 1f, 0);
		
		img_label_logo.setSize(img_label_logo.getWidth() * getRatio(1f/300f), img_label_logo.getHeight() * getRatio(1f/300f));
		img_label_logo.setPosition(Gdx.graphics.getWidth() / 4f, Gdx.graphics.getHeight() / 2f, Alignment.CENTER.get());
		img_label_logo.setTouchable(Touchable.disabled);
		
		canvas.setColor(1f, 1f, 1f, 0);
		
		Label label_play = new Label("Play", Game.GENERATED_LABEL_STYLE_GOTTHARD_BUTTON);
		Label label_settings = new Label("Settings", Game.GENERATED_LABEL_STYLE_GOTTHARD_BUTTON);
		Label label_exit = new Label("Exit", Game.GENERATED_LABEL_STYLE_GOTTHARD_LARGE);
		Label label_credits = new Label("Credits", Game.GENERATED_LABEL_STYLE_GOTTHARD_BUTTON);
		Label label_editor = new Label("Editor", Game.GENERATED_LABEL_STYLE_GOTTHARD_BUTTON);
		Label label_profile = new Label("Profile", Game.GENERATED_LABEL_STYLE_GOTTHARD_BUTTON);
		
		but_play.add(label_play).left();
		but_settings.add(label_settings).left();
		but_exit.add(label_exit);
		but_credits.add(label_credits).left();
		but_editor.add(label_editor).left();
		but_profile.add(label_profile).left();
		
		but_play.setSize(label_settings.getWidth(), label_settings.getHeight());
		but_settings.setSize(label_settings.getWidth(), label_settings.getHeight());
		but_exit.setSize(label_exit.getWidth(), label_exit.getHeight());
		but_credits.setSize(label_credits.getWidth(), label_credits.getHeight());
		but_editor.setSize(label_editor.getWidth(), label_editor.getHeight());
		but_profile.setSize(label_profile.getWidth(), label_profile.getHeight());
		
		Table table_game = new Table(Game.NEON_UI);
		float scale_panel_1 = Gdx.graphics.getHeight() / 28f;
		NinePatch patch9 = Game.SCI_FI_ATLAS.createPatch("panel1");
		patch9.scale(scale_panel_1 / 15f, scale_panel_1 / 15f);
		NinePatchDrawable nine9 = new NinePatchDrawable(patch9);
		table_game.setBackground(nine9);
		
		System.out.println("DAGuiLoadingMenu(new instance) ### scale_panel_1: " + scale_panel_1);
		
		Table table_editor = new Table(Game.NEON_UI);
		float scale_panel_2 = Gdx.graphics.getHeight() / 54f;
		NinePatch patch9_editor = Game.SCI_FI_ATLAS.createPatch("panel2");
		patch9_editor.scale(scale_panel_2 / 13f, scale_panel_2 / 13f);
		NinePatchDrawable nine9_editor = new NinePatchDrawable(patch9_editor);
		table_editor.setBackground(nine9_editor);
		table_editor.add(but_editor).growX();
		table_editor.row();
		table_editor.add(but_profile).growX();
		
		canvas.add(table_game).right().expand();
		canvas.row();
		canvas.add(table_editor).right().expand();
		canvas.row();
		table_game.add(but_play).growX();
		table_game.row();
		table_game.add(but_settings);
		
		//canvas.row(); @Deprecated
		canvas.setPosition(Gdx.graphics.getWidth() / 2f, Gdx.graphics.getHeight() / 2f, Alignment.CENTER.get());
		
		canvas.add(but_credits).right().expand();
		canvas.row();
		canvas.add(but_exit).right().expand();
		
		group_stage.addActor(table_profile);
		table_profile.setSize(Gdx.graphics.getHeight() / 2f, Gdx.graphics.getHeight() / 2f);
		table_profile.add(text_profile).left().grow();
		text_profile.setText(Game.profile.getProfile().get().toString());
		table_profile.setTouchable(Touchable.disabled);
		table_profile.setColor(1, 1, 1, 0);
		table_profile.setPosition(0, 0, Alignment.BOTTOMLEFT.get());
		
		group_stage.addActor(text_preferences);
		text_preferences.setSize(Gdx.graphics.getHeight() / 2f, Gdx.graphics.getHeight() / 2f);
		text_preferences.setPosition(0, 0, Alignment.BOTTOMLEFT.get());
		text_preferences.setText(Game.profile.getPreferences().get().toString());
		text_preferences.setVisible(false);
		
		Label label_resetProfile = new Label("Reset profile", Game.GENERATED_LABEL_STYLE_GOTTHARD_LITTLE_TEXT_WHITE);
		label_resetProfile.setColor(Color.RED);
		but_reset_profile.add(label_resetProfile).grow();
		but_reset_profile.setSize(getRatioY(1f/12f), getRatioY(1f/12f));
		but_reset_profile.setPosition(table_profile.getWidth() / 2f, text_profile.getY(Alignment.TOP.get()), Alignment.BOTTOM.get());
		but_reset_profile.setVisible(false);
		group_stage.addActor(but_reset_profile);
		
		func_blend.start();
		
	}
	
	@Override
	public void update(SpriteBatch batch) {
		super.update(batch);
		func_blend.init();
		func_switch.init();
		
		img_logo.setOrigin(Alignment.CENTER.get());
		img_logo.rotateBy(Gdx.graphics.getDeltaTime() * 3f);
		img_logo.setScale(1f + MathUtils.cos((float) (System.currentTimeMillis() / 50 % (360)) * MathUtils.degreesToRadians) * 0.08f);
		//System.out.println("::::" + ((System.currentTimeMillis() % 2000)));
		// @Deprecated
		if(image_background.getColor().a != 1) {
			image_background.setColor(1, 1, 1, func_blend.getValue());
			canvas.setColor(1, 1, 1, func_blend.getValue());
		}//
		if(img_logo.getColor().a != 1) {
			img_logo.setColor(1, 1, 1, func_blend.getValue());
			canvas.setColor(1, 1, 1, func_blend.getValue());
		}
		
		if(but_exit.isChecked()) {
			but_exit.setChecked(false);
			
			Gdx.app.exit();
		}
		
		if(but_credits.isChecked()) {
			but_credits.setChecked(false);
			
			text_preferences.setVisible(!text_preferences.isVisible());
		}
		
		if(but_play.isChecked()) {
			but_play.setChecked(false);
			
			func_switch.setBackward(false);
			func_switch.start();
			isGuiOpen = true;
			
			gui_play = new DAGuiPlayMenu(game);
			getGuiManager().addGui(gui_play);
			gui_play.group_stage.setTouchable(Touchable.disabled);
		}
		
		if(isGuiOpen) {
			if(gui_play != null) {
				gui_play.canvas.setPosition(Gdx.graphics.getWidth() - Gdx.graphics.getWidth() * func_switch.getValue(),
					canvas.getY());
			}
			
			canvas.setPosition(Gdx.graphics.getWidth() * -func_switch.getValue(),
					canvas.getY());
			
			if(func_switch.hasEnded()) {
				func_switch.reload();
				func_switch.switchBackward();
				isGuiOpen = false;
				gui_play.group_stage.setTouchable(Touchable.enabled);
			}
		}
		
		if(gui_play != null && getGuiManager().guiList.peek() == gui_play && gui_play.isGuiClose) {
			func_switch.start();
			gui_play.canvas.setPosition(Gdx.graphics.getWidth() - Gdx.graphics.getWidth() * func_switch.getValue(),
					canvas.getY());
			
			canvas.setPosition(Gdx.graphics.getWidth() * -func_switch.getValue(),
					canvas.getY());
			
			if(func_switch.hasEnded()) {
				func_switch.reload();
				func_switch.switchBackward();
				getGuiManager().removeGui();
			}
		}
		
		if(but_settings.isChecked()) {
			but_settings.setChecked(false);
			
			getGuiManager().addGui(new DAGuiSettings(game));
		}
		
		if(but_editor.isChecked()) {
			but_editor.setChecked(false);
			
			game.guiManager.reset();
			game.guiManager.addGui(new GuiEditor(game));
		}
		
		if(but_profile.isChecked()) {
			but_profile.setChecked(false);
			
			if(table_profile.getColor().a == 0) {
				table_profile.setTouchable(Touchable.enabled);
				table_profile.setColor(1, 1, 1, 1);
				but_reset_profile.setVisible(true);
			} else if(table_profile.getColor().a == 1) {
				table_profile.setColor(1, 1, 1, 0);
				table_profile.setTouchable(Touchable.disabled);
				but_reset_profile.setVisible(false);
			}
		}
		
		if(but_reset_profile.isChecked()) {
			but_reset_profile.setChecked(false);
			
			Game.profile.reset();
		}
		
		if(table_profile.getColor().a == 1)
			text_profile.setText(Game.profile.getProfile().get().toString());		
		//group_stage.debugAll();
	}
	
	@Override
	public void preInit() {
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
	}
}
