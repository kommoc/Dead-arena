package net.kommocgame.src.gui.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Container;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ProgressBar;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;
import com.github.czyzby.kiwi.util.gdx.scene2d.Alignment;

import net.kommocgame.src.Game;
import net.kommocgame.src.gui.GuiBase;
import net.kommocgame.src.gui.game.tables.TableResourceState;

public class DAGuiStartMenu extends GuiBase {
	
	Button but_back			= new Button(Game.NEON_UI);
	Button but_store		= new Button(Game.NEON_UI);
	Button but_equip		= new Button(Game.NEON_UI);
	Button but_characters	= new Button(Game.NEON_UI);
	Button but_start		= new Button(Game.NEON_UI);
	
	Label label_player_level		= new Label("level 16", Game.GENERATED_LABEL_STYLE_GOTTHARD_MID_TEXT);
	Label label_player_nextLevel	= new Label("17", Game.GENERATED_LABEL_STYLE_GOTTHARD_MID_TEXT_WHITE);
	Label label_player_current_exp	= new Label("251/500", Game.GENERATED_LABEL_STYLE_GOTTHARD_MID_TEXT);
	
	Label label_player_name			= new Label("player1", Game.GENERATED_LABEL_STYLE_GOTTHARD_MID_TEXT);
	
	ProgressBar progressBar_player_exp	= new ProgressBar(0, 1f, 0.01f, false, Game.NEUTRALIZER_UI);
	
	TableResourceState table_resources	= new TableResourceState(getRatio(1f/14f));
	
	private float scale_panel_1	= Gdx.graphics.getHeight() / 54f;
	float scale_panel_3 = Gdx.graphics.getHeight() / 46f;
	float scale_ratio_slider = Gdx.graphics.getHeight() / 32f;
	
	public DAGuiStartMenu(Game game) {
		super(game);
		group_stage.addActor(table_resources);
		group_stage.addActor(but_back);
		but_back.setPosition(0, 0, Alignment.BOTTOMLEFT.get());
		
		table_resources.setSize(getRatioY(1f), getRatio(1f/10f));
		table_resources.setPosition(getRatioY(1f/2f), getRatio(1f), Alignment.TOP.get());
		
		Table table_right = new Table(Game.KOMMOC_UI);
		group_stage.addActor(table_right);
		table_right.setSize(getRatioY(1f/2f), getRatio(9f/10f));
		table_right.setPosition(getRatioY(1f/2f), 0, Alignment.BOTTOMLEFT.get());
		
		NinePatch patch9_editor = Game.SCI_FI_ATLAS.createPatch("panel3");
		patch9_editor.scale(scale_panel_3 / 13f, scale_panel_3 / 13f);
		NinePatchDrawable nine9_editor = new NinePatchDrawable(patch9_editor);
		table_right.setBackground(nine9_editor);
		//table_right.setColor(Game.COLOR_TEXT);
		
		but_store.add(new Label("Store", Game.GENERATED_LABEL_STYLE_GOTTHARD_BUTTON));
		but_equip.add(new Label("Equip", Game.GENERATED_LABEL_STYLE_GOTTHARD_BUTTON));
		but_characters.add(new Label("Characters", Game.GENERATED_LABEL_STYLE_GOTTHARD_MID_TEXT));
		
		Label label_but_start = new Label("START", Game.GENERATED_LABEL_STYLE_GOTTHARD_BUTTON_WHITE);
		label_but_start.setColor(Game.COLOR_TEXT);
		but_start.add(label_but_start).pad(getRatio(1f/16f));
		but_back.add(new Label("Back", Game.GENERATED_LABEL_STYLE_GOTTHARD_BUTTON));
		
		table_right.add(but_store).growX();
		table_right.row();
		table_right.add(but_equip).growX();
		table_right.row();
		table_right.add(but_characters).growX().height(but_equip.getPrefHeight());
		table_right.row();
		table_right.add(but_start).expandY().center();
		
		Table table_level = new Table(Game.KOMMOC_UI);
		group_stage.addActor(table_level);
		table_level.setSize(getRatioY(1f/2f), getRatio(3f/10f));
		table_level.setPosition(0, getRatio(6f/10f), Alignment.BOTTOMLEFT.get());
		
		NinePatch patch9_canvas = Game.SCI_FI_ATLAS.createPatch("panel1");
		patch9_canvas.scale(scale_panel_1 / 13f, scale_panel_1 / 13f);
		patch9_canvas.setColor(Color.LIGHT_GRAY);
		NinePatchDrawable nine9_canvas = new NinePatchDrawable(patch9_canvas);
		table_level.setBackground(nine9_canvas);
		
		progressBar_player_exp.setValue(251f/500f);
		progressBar_player_exp.setColor(Game.COLOR_TEXT);
		
		Container container_progress_exp = new Container(progressBar_player_exp).width(table_level.getWidth() / scale_ratio_slider * 8f);
		container_progress_exp.setTransform(true);
		container_progress_exp.setScale(scale_ratio_slider / 10f);
		container_progress_exp.setOrigin(container_progress_exp.getPrefWidth(), container_progress_exp.getPrefHeight() / 2f);
		
		table_level.add(label_player_level).colspan(2).expandX();
		table_level.row();
		table_level.add(container_progress_exp).expandX().right();
		table_level.add(label_player_nextLevel);
		label_player_nextLevel.setColor(Game.COLOR_TEXT);
		table_level.row();
		table_level.add(label_player_current_exp).colspan(2).expandX().center();
	}
	
	@Override
	public void update(SpriteBatch batch) {
		super.update(batch);
		
		if(but_store.isChecked()) {
			but_store.setChecked(false);
			game.guiManager.removeGui();
			
			game.guiManager.addGui(new DAGuiStoreMenu(game));
		}
		
		if(but_equip.isChecked()) {
			but_equip.setChecked(false);
			game.guiManager.removeGui();
			
			game.guiManager.addGui(new DAGuiEquipMenu(game));
		}
		
		if(but_back.isChecked()) {
			but_back.setChecked(false);
			
			game.guiManager.removeGui();
		}
		
		if(but_start.isChecked()) {
			but_start.setChecked(false);
			
			game.guiManager.reset();
			game.guiManager.addGui(new DAGuiInGame_3(game));
			game.CORE.constructLevel();
		}
	}
}
