package net.kommocgame.src.gui.DeadArena;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.CheckBox;
import com.badlogic.gdx.scenes.scene2d.ui.Container;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Slider;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;
import com.github.czyzby.kiwi.util.gdx.scene2d.Alignment;

import net.kommocgame.src.Function;
import net.kommocgame.src.Game;
import net.kommocgame.src.gui.GuiBase;

public class DAGuiSettings extends GuiBase {
	
	Function func_blend = new Function(450, Interpolation.exp5);
	
	Table canvas = new Table(Game.NEON_UI);
	ScrollPane scroll_pane;
	
	Slider slider_zoom		= new Slider(0, 1f, 0.01f, false, Game.NEON_UI);
	Slider slider_music		= new Slider(0, 1f, 0.01f, false, Game.NEON_UI);
	Slider slider_sounds	= new Slider(0, 1f, 0.01f, false, Game.NEON_UI);
	
	CheckBox box_minimap		= new CheckBox("", Game.NEON_UI);
	CheckBox box_TEST_LEVEL		= new CheckBox("", Game.NEON_UI);
	CheckBox box_debug_bounds	= new CheckBox("", Game.NEON_UI);
	CheckBox box_debug_text		= new CheckBox("", Game.NEON_UI);
	CheckBox box_debug_fps		= new CheckBox("", Game.NEON_UI);
	CheckBox box_shadows		= new CheckBox("", Game.NEON_UI);
	CheckBox box_vsync			= new CheckBox("", Game.NEON_UI);
	
	Button but_back			= new Button(Game.NEON_UI);
	Button but_debug		= new Button(Game.NEON_UI);
	
	private float scale_panel_1	= Gdx.graphics.getHeight() / 54f;
	private float minHeight		= 2f * scale_panel_1;
	float scale_ratio_slider = Gdx.graphics.getHeight() / 32f;
	
	public DAGuiSettings(Game game) {
		super(game);
		Table table_canvas = new Table(Game.NEON_UI);
		scroll_pane = new ScrollPane(table_canvas, Game.NEON_UI);
		scroll_pane.setOverscroll(false, false);
		scroll_pane.setScrollingDisabled(true, false);
		scroll_pane.setScrollBarPositions(false, false);
		
		group_stage.addActor(canvas);
		canvas.setSize(getRatioY(1f/1.5f), getRatioY(1f/2f));
		canvas.setPosition(Gdx.graphics.getWidth() / 2f, Gdx.graphics.getHeight() / 2f, Alignment.CENTER.get());
		canvas.top();
		canvas.setClip(true);
		
		Table table_settings = new Table(Game.NEON_UI);
		Label label_settings = new Label("Settings", Game.GENERATED_LABEL_STYLE_GOTTHARD_MID_TEXT_WHITE);
		label_settings.setColor(Color.CHARTREUSE);
		table_settings.add(label_settings).expandX();
		canvas.add(table_settings).growX();
		canvas.row();
		
		NinePatch patch9_canvas = Game.SCI_FI_ATLAS.createPatch("panel1");
		patch9_canvas.scale(scale_panel_1 / 13f, scale_panel_1 / 13f);
		patch9_canvas.setColor(Color.LIGHT_GRAY);
		NinePatchDrawable nine9_canvas = new NinePatchDrawable(patch9_canvas);
		canvas.setBackground(nine9_canvas);
		
		Table table_zoom =		tableSlider("Zoom", Color.CYAN, slider_zoom);
		Table table_music =		tableSlider("Music", Color.CYAN, slider_music);
		Table table_sound =		tableSlider("Sound", Color.CYAN, slider_sounds);
		Table table_box_minimap =			tableCheckbox("Enable minimap", Color.CYAN, box_minimap);
		Table table_box_shootByMonitor =	tableCheckbox("TEST LEVEL", Color.CYAN, box_TEST_LEVEL);
		Table table_box_debug_bounds =		tableCheckbox("DEBUG_BOUNDS", Color.LIGHT_GRAY, box_debug_bounds);
		Table table_box_debug_text =		tableCheckbox("DEBUG_TEXT", Color.CHARTREUSE, box_debug_text);
		Table table_box_debug_fps =			tableCheckbox("FPS counter", Color.LIME, box_debug_fps);
		Table table_box_shadows =			tableCheckbox("Enable shadows", Color.CYAN, box_shadows);
		Table table_box_vsync =				tableCheckbox("Enable VSync", Color.CYAN, box_vsync);
		Table table_but_open_debug =		tableButton("Open debug table", Color.CYAN, but_debug);
		
		
		table_canvas.add(table_zoom).growX().center();
		table_canvas.row();
		table_canvas.add(table_music).growX().center();
		table_canvas.row();
		table_canvas.add(table_sound).growX().center();
		table_canvas.row();
		table_canvas.add(table_box_minimap).growX().center();
		table_canvas.row();
		table_canvas.add(table_box_shootByMonitor).growX().center();
		table_canvas.row();
		table_canvas.add(table_box_shadows).growX().center();
		table_canvas.row();
		table_canvas.add(table_box_vsync).growX().center();
		table_canvas.row();
		table_canvas.add(table_but_open_debug).growX().center();
		table_canvas.row();
		
		canvas.add(scroll_pane).grow();
		table_canvas.add(table_box_debug_bounds).growX().center();
		table_canvas.row();
		table_canvas.add(table_box_debug_text).growX().center();
		table_canvas.row();
		table_canvas.add(table_box_debug_fps).growX().center();
		table_canvas.row();
		
		
		table_box_debug_bounds.setColor(Color.CHARTREUSE);
		table_box_debug_text.setColor(Color.CHARTREUSE);
		
		slider_music.setValue(Game.profile.settings_volume_music());
		slider_sounds.setValue(Game.profile.settings_volume_sound());
		slider_zoom.setValue(Game.profile.settings_zoom());
		box_minimap.setChecked(Game.profile.settings_minimapIsEnable());
		box_TEST_LEVEL.setChecked(Game.profile.settings_shootingByMonitorIsEnable());
		box_debug_text.setChecked(Game.profile.settings_debug_text());
		box_debug_bounds.setChecked(Game.profile.settings_debug_bounds());
		box_debug_fps.setChecked(Game.profile.settings_debug_fpsCounter());
		box_shadows.setChecked(Game.profile.settings_shadows());
		box_vsync.setChecked(Game.profile.settings_vsync());
		
		slider_music.setAnimateDuration(0.05f);
		slider_zoom.setAnimateDuration(0.05f);
		slider_sounds.setAnimateDuration(0.05f);
		
		canvas.row();
		canvas.add(but_back).right().bottom();
		but_back.add(new Label("Back", Game.GENERATED_LABEL_STYLE_GOTTHARD_BUTTON));
		
		but_debug.add(new Label("Open DEBUG menu", Game.GENERATED_LABEL_STYLE_GOTTHARD_MID_TEXT_WHITE));
		canvas.layout();
		
		canvas.setTouchable(Touchable.disabled);
		canvas.getColor().a = 0;
		
		func_blend.start();
	}
	
	@Override
	public void update(SpriteBatch batch) {
		super.update(batch);
		func_blend.init();
		//canvas.debugAll();
		
		if(canvas.getColor().a != 1 || (func_blend.isBackward() && func_blend.getState())) {
			canvas.getColor().a = func_blend.getValue();
			canvas.setSize(canvas.getWidth(), getRatioY(1f/2f) * func_blend.getValue() > minHeight ? getRatioY(1f/2f) * func_blend.getValue() :
				minHeight);
			
			canvas.setPosition(Gdx.graphics.getWidth() / 2f, getRatio(15f/16f), Alignment.TOP.get());
			
			if(func_blend.hasEnded()) {
				if(func_blend.isBackward()) {
					Game.profile.set_settings_minimap(box_minimap.isChecked());
					Game.profile.set_settings_shootingByMonitor(box_TEST_LEVEL.isChecked());
					Game.profile.set_settings_volume_music((int) (slider_music.getValue() * 100f));
					Game.profile.set_settings_volume_sound((int) (slider_sounds.getValue() * 100f));
					Game.profile.set_settings_zoom((int) (slider_zoom.getValue() * 100f));
					Game.profile.set_settings_debug_bounds((box_debug_bounds.isChecked()));
					Game.profile.set_settings_debug_text((box_debug_text.isChecked()));
					Game.profile.set_settings_debug_fpsCounter(box_debug_fps.isChecked());
					Game.profile.set_settings_shadows(box_shadows.isChecked());
					Game.profile.set_settings_vsync(box_vsync.isChecked());
					
					getGuiManager().removeGui();					
				}
				
				canvas.setTouchable(Touchable.enabled);
				func_blend.reload();
				func_blend.switchBackward();
			}
		}
		
		if(slider_music.isDragging()) {
			scroll_pane.cancel();
			Game.profile.set_settings_volume_music((int) (slider_music.getValue() * 100f));
		} else if(slider_sounds.isDragging()) {
			scroll_pane.cancel();
			Game.profile.set_settings_volume_sound((int) (slider_sounds.getValue() * 100f));
		} else if(slider_zoom.isDragging()) {
			scroll_pane.cancel();
			Game.profile.set_settings_zoom((int) (slider_zoom.getValue() * 100f));
		}
		
		if(Game.profile.settings_debug_fpsCounter() != box_debug_fps.isChecked()) {
			Game.profile.set_settings_debug_fpsCounter(box_debug_fps.isChecked());
		}
		
		if(but_back.isChecked()) {
			but_back.setChecked(false);
			
			func_blend.start();
		}
		
		if(but_debug.isChecked()) {
			but_debug.setChecked(false);
			
			DATableDebug tdebug = new DATableDebug(Game.NEUTRALIZER_UI);
			group_stage.addActor(tdebug);
			tdebug.setSize(getRatioY(1f/1.5f), getRatioY(1f/2f));
			tdebug.setPosition(Gdx.graphics.getWidth() / 2f, Gdx.graphics.getHeight() / 2f, Alignment.CENTER.get());
			tdebug.top();
			tdebug.setClip(true);
			
		}
	}
	
	Table tableSlider(String name, Color color, Slider slider) {
		Table table_slider = new Table(Game.NEON_UI);
		table_slider.setBackground("button");
		table_slider.setColor(Color.LIGHT_GRAY);
		
		Container container_slider = new Container(slider).width(canvas.getWidth() / scale_ratio_slider * 5f);
		container_slider.setTransform(true);
		container_slider.setScale(scale_ratio_slider / 10f);
		container_slider.setOrigin(container_slider.getPrefWidth(), container_slider.getPrefHeight() / 2f);
		
		Label label_name = new Label(name, Game.GENERATED_LABEL_STYLE_GOTTHARD_MID_TEXT_WHITE);
		label_name.setColor(color);
		table_slider.add(label_name);
		table_slider.add(container_slider).expandX().right();
		return table_slider;
	}
	
	Table tableButton(String name, Color color, Button but) {
		Table table_button = new Table(Game.NEON_UI);
		table_button.setBackground("button");
		table_button.setColor(Color.LIGHT_GRAY);
		
		Label label_name = new Label(name, Game.GENERATED_LABEL_STYLE_GOTTHARD_MID_TEXT_WHITE);
		label_name.setColor(color);
		table_button.add(label_name);
		table_button.row();
		table_button.add(but).grow().right();
		return table_button;
	}
	
	Table tableCheckbox(String name, Color color, CheckBox box) {
		Table table_checkbox = new Table(Game.NEON_UI);
		table_checkbox.setBackground("button");
		table_checkbox.setColor(Color.LIGHT_GRAY);
		
		Container container_box = new Container(box).size(getRatioY(1f/48f)).width(getRatioY(1f/40f));
		container_box.setTransform(true);
		container_box.setScale(scale_ratio_slider / 8f);
		container_box.setOrigin(container_box.getPrefWidth(), container_box.getPrefHeight() / 2f);
		
		Label label_name = new Label(name, Game.GENERATED_LABEL_STYLE_GOTTHARD_MID_TEXT_WHITE);
		label_name.setColor(color);
		table_checkbox.add(label_name);
		table_checkbox.add(container_box).grow().right();
		return table_checkbox;
	}
}
