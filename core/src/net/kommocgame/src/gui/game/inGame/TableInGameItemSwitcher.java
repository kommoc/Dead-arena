package net.kommocgame.src.gui.game.inGame;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Button.ButtonStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ProgressBar;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.github.czyzby.kiwi.util.gdx.scene2d.Alignment;

import net.kommocgame.src.Game;
import net.kommocgame.src.Loader;
import net.kommocgame.src.gui.GuiBase;

public class TableInGameItemSwitcher extends Table {
	
	Table table_ammo			= new Table(Game.NEON_UI).center();
	Table table_items_bar		= new Table(Game.NEON_UI);
	Table table_item			= new Table(Game.NEON_UI);
	
	Label label_reload			= new Label("RELOAD", Game.GENERATED_LABEL_STYLE_GOTTHARD_LITTLE_TEXT_WHITE);
	Label label_ammo_current	= new Label("", Game.GENERATED_LABEL_STYLE_GOTTHARD_LITTLE_TEXT_WHITE);
	Label label_ammo_max		= new Label("", Game.GENERATED_LABEL_STYLE_GOTTHARD_LITTLE_TEXT_WHITE);
	
	ClickListener clickListener;
	float item_size;
	
	Button but_switch_next		= new Button();
	Button but_switch_previos	= new Button(Game.KOMMOC_UI, "switch");
	
	ProgressBar bar_reload		= new ProgressBar(0, 1, 0.01f, false, Game.NEUTRALIZER_UI);
	
	public TableInGameItemSwitcher() {
		this(Game.NEUTRALIZER_UI);
	}
	
	public TableInGameItemSwitcher(Skin skin) {
		super(skin);
		
		float scale_panel_menu = Gdx.graphics.getHeight() / 28f;
		but_switch_next.setStyle(getSwitcherStyle());
		but_switch_previos.setColor(but_switch_previos.getColor().r, but_switch_previos.getColor().g,
				but_switch_previos.getColor().b, 0.75f);
		but_switch_next.setColor(but_switch_next.getColor().r, but_switch_next.getColor().g,
				but_switch_next.getColor().b, 0.75f);
		
		NinePatch patch9_item = Game.SCI_FI_ATLAS.createPatch("panel1");
		patch9_item.scale(scale_panel_menu / 21f, scale_panel_menu / 21f);
		patch9_item.setColor(new Color(1, 1, 1, 0.6f));
		NinePatchDrawable nine9_item = new NinePatchDrawable(patch9_item);
		table_item.setBackground(nine9_item);
		table_item.setTouchable(Touchable.enabled);
		table_item.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				super.clicked(event, x, y);
				
				//System.out.println("	DAGuiInGame(new instance) ### RELOAD!");
			}
		});
		
		table_items_bar.add(but_switch_previos).size(GuiBase.getRatioY(1f/16f)).pad(Game.GUI_NUMBER_PANEL_GAP);
		table_items_bar.add(table_item).size(GuiBase.getRatioY(1f/6f), GuiBase.getRatio(1f/3.4f)).grow();
		table_items_bar.add(but_switch_next).size(GuiBase.getRatioY(1f/16f)).pad(Game.GUI_NUMBER_PANEL_GAP);
		
		this.add(table_items_bar);
		
		label_reload.setTouchable(Touchable.disabled);
		label_ammo_current.setTouchable(Touchable.disabled);
		label_ammo_current.setTouchable(Touchable.disabled);
		label_reload.setColor(Color.CHARTREUSE);
		
		this.row();
		this.add(label_reload);
		
		item_size = GuiBase.getRatio(1f/4.5f);
		
		this.row();
		this.add(table_ammo).grow().pad(Game.GUI_NUMBER_PANEL_GAP);
		
		Table table_ammo_counter = new Table(Game.NEON_UI);
		table_ammo.add(table_ammo_counter).growX();
		table_ammo_counter.add(label_ammo_current).left().uniformX();
		table_ammo_counter.add(new Label("/", Game.GENERATED_LABEL_STYLE_GOTTHARD_LITTLE_TEXT_WHITE));
		table_ammo_counter.add(label_ammo_max).left().uniformX();
		table_ammo.row();
		table_ammo.add(bar_reload).growX().pad(Game.GUI_NUMBER_PANEL_GAP / 2f).maxWidth(GuiBase.getRatioY(1f/6f));
		bar_reload.setColor(Color.CYAN);
		bar_reload.setAnimateDuration(0.025f);
		bar_reload.setAnimateInterpolation(Interpolation.linear);
		
	}
	
	public ButtonStyle getSwitcherStyle() {
		ButtonStyle style = new ButtonStyle();
		
		TextureRegionDrawable draw_up = new TextureRegionDrawable(new TextureRegion(Loader.guiIcon("icon_next.png")));
		draw_up.getRegion().flip(true, false);
		style.up = draw_up;
		
		TextureRegionDrawable draw_down = new TextureRegionDrawable(new TextureRegion(Loader.guiIcon("icon_next_pressed.png")));
		draw_down.getRegion().flip(true, false);
		style.down = draw_down;
		return style;
	}
	
	@Override
	public void act(float delta) {
		super.act(delta);
		
		
	}

}
