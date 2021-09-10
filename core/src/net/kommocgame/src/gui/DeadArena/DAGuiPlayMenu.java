package net.kommocgame.src.gui.DeadArena;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ProgressBar;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;
import com.github.czyzby.kiwi.util.gdx.scene2d.Alignment;

import net.kommocgame.src.Function;
import net.kommocgame.src.Game;
import net.kommocgame.src.Loader;
import net.kommocgame.src.gui.GuiBase;
import net.kommocgame.src.gui.GuiInGame;
import net.kommocgame.src.item.Item;
import net.kommocgame.src.world.level.LevelTEST;

@Deprecated
public class DAGuiPlayMenu extends GuiBase {
	
	Function func_switch = new Function(400, Interpolation.exp5);
	Table canvas = new Table(Game.NEON_UI);
	
	boolean isGuiClose = false;
	
	Table table_wave_slots = new Table(Game.NEON_UI);
	
	Table table_shop = new Table(Game.NEON_UI);
	Button but_shop_player = new Button(Game.NEON_UI);
	Button but_shop_bunker = new Button(Game.NEON_UI);
	
	boolean isGuiOpen = false;
	
	boolean isGuiShopPlayerOpen = false;
	boolean isGuiShopBunkerOpen = false;
	
	DAGuiPlayerShop gui_shop_player;
	//DAGuiBunkerShop gui_shop_bunker;
	
	@Deprecated
	/** Set to statistic. */
	Button but_invenvtory	= new Button(Game.NEON_UI);
	Button but_play			= new Button(Game.NEON_UI);
	Button but_back			= new Button(Game.NEON_UI);
	
	Label label_wave = new Label("Wave:", Game.GENERATED_LABEL_STYLE_GOTTHARD_MID_TEXT);
	Label label_money = new Label("$:", Game.GENERATED_LABEL_STYLE_GOTTHARD_MID_TEXT);
	
	Label label_player_hp = new Label("PLAYER_HP", Game.GENERATED_LABEL_STYLE_GOTTHARD_LITTLE_TEXT_WHITE);
	Label label_bunker_hp = new Label("BUNKER_HP", Game.GENERATED_LABEL_STYLE_GOTTHARD_LITTLE_TEXT_WHITE);
	
	ProgressBar bar_player_hp = new ProgressBar(0, Game.profile.get_playerMaxHP(), 1, false, Game.NEUTRALIZER_UI);
	ProgressBar bar_bunker_hp = new ProgressBar(0, Game.profile.get_bunkerMaxHP(), 1, false, Game.NEUTRALIZER_UI);
	
	public DAGuiPlayMenu(Game game) {
		super(game);
		group_stage.addActor(canvas);
		canvas.setSize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		canvas.setPosition(0, 0);
		canvas.setTransform(false);
		
		float scale_panel_1 = Gdx.graphics.getHeight() / 28f;
		NinePatch patch9 = Game.SCI_FI_ATLAS.createPatch("panel1");
		patch9.scale(scale_panel_1 / 15f, scale_panel_1 / 15f);
		NinePatchDrawable nine9 = new NinePatchDrawable(patch9);
		canvas.setBackground(nine9);
		
		but_invenvtory.add(new Label("Statistic", Game.GENERATED_LABEL_STYLE_GOTTHARD_BUTTON));
		Label label_but_play = new Label("Play", Game.GENERATED_LABEL_STYLE_GOTTHARD_BUTTON_WHITE);
		label_but_play.setColor(Color.CHARTREUSE);
		but_play.add(label_but_play);
		but_back.add(new Label("Back", Game.GENERATED_LABEL_STYLE_GOTTHARD_MID_TEXT));
		
		Table table_upperPane = new Table(Game.NEON_UI);
		Table table_money = new Table(Game.NEON_UI);
		table_money.setBackground("button");
		table_money.add(label_money).growX();
		table_money.setColor(Color.CYAN);
		table_upperPane.add(table_money).growX();
		table_upperPane.add(but_back);
		
		canvas.add(table_upperPane).growX().left().colspan(2);
		canvas.row();
		
		Table table_shop = new Table(Game.NEON_UI);
		table_shop.top();
		float scale_panel_2 = Gdx.graphics.getHeight() / 54f;
		NinePatch patch9_shop = Game.SCI_FI_ATLAS.createPatch("panel2");
		patch9_shop.scale(scale_panel_2 / 13f, scale_panel_2 / 13f);
		NinePatchDrawable nine9_shop = new NinePatchDrawable(patch9_shop);
		table_shop.setBackground(nine9_shop);
		
		Table table_label_shop = new Table(Game.NEON_UI);
		table_label_shop.setBackground("button");
		table_label_shop.setColor(Color.CYAN);
		table_label_shop.add(new Label("SHOP", Game.GENERATED_LABEL_STYLE_GOTTHARD_BUTTON));
		
		table_shop.add(table_label_shop).colspan(2).top().growX().maxWidth(getRatioY(1.9f/3f));
		table_shop.row();
		table_shop.add(but_shop_player).fill().maxWidth(getRatioY(0.9f/3f)).right().expandY();
		table_shop.add(but_shop_bunker).fill().maxWidth(getRatioY(0.9f/3f)).left();
		Image img_player = new Image(Loader.objectsUnits("worker_body_weapon_pistol.png"));
		Image img_bunker = new Image(Loader.objectsUnits("building_bunker.png"));
		
		but_shop_player.add(img_player).maxSize(getRatioY(0.4f/3f));
		but_shop_bunker.add(img_bunker).maxSize(getRatioY(0.4f/3f));
		table_shop.row();
		
		Table table_player_hp = new Table(Game.NEON_UI);
		table_player_hp.setBackground("button");
		table_player_hp.setColor(Color.LIGHT_GRAY);
		
		Label player_hp = new Label("HP", Game.GENERATED_LABEL_STYLE_GOTTHARD_MID_TEXT_WHITE);
		Label bunker_hp = new Label("HP", Game.GENERATED_LABEL_STYLE_GOTTHARD_MID_TEXT_WHITE);
		player_hp.setColor(Color.CORAL);
		bunker_hp.setColor(Color.CORAL);
		bar_bunker_hp.setColor(Color.GREEN);
		bar_player_hp.setColor(Color.GREEN);
		
		Table table_player_HP_bar = new Table(Game.NEON_UI);
		Table table_bunker_HP_bar = new Table(Game.NEON_UI);
		this.label_player_hp.setText(Game.profile.get_playerHP() + "/" + Game.profile.get_playerMaxHP());
		this.label_bunker_hp.setText(Game.profile.get_bunkerHP() + "/" + Game.profile.get_bunkerMaxHP());
		
		table_player_HP_bar.add(bar_player_hp).spaceLeft(getRatioY(1f/32f)).width(getRatioY(0.35f/3f));
		table_player_HP_bar.row();
		table_player_HP_bar.add(this.label_player_hp).right().expandX();
		
		table_bunker_HP_bar.add(bar_bunker_hp).spaceLeft(getRatioY(1f/32f)).width(getRatioY(0.35f/3f));
		table_bunker_HP_bar.row();
		table_bunker_HP_bar.add(this.label_bunker_hp).right().expandX();
		
		table_player_hp.add(player_hp).left().expandX();
		////table_player_hp.add(bar_player_hp).spaceLeft(getRatioY(1f/32f)).width(getRatioY(0.35f/3f));
		/***/table_player_hp.add(table_player_HP_bar).spaceLeft(getRatioY(1f/32f)).width(getRatioY(0.35f/3f)).right();
		
		Table table_bunker_hp = new Table(Game.NEON_UI);
		table_bunker_hp.setBackground("button");
		table_bunker_hp.setColor(Color.LIGHT_GRAY);
		
		table_bunker_hp.add(bunker_hp).left().expandX();
		////table_bunker_hp.add(bar_bunker_hp).spaceLeft(getRatioY(1f/32f)).width(getRatioY(0.35f/3f));
		/***/table_bunker_hp.add(table_bunker_HP_bar).spaceLeft(getRatioY(1f/32f)).width(getRatioY(0.35f/3f)).right();
		
		bar_player_hp.setValue(Game.profile.get_playerHP());
		bar_bunker_hp.setValue(Game.profile.get_bunkerHP());
		
		table_shop.add(table_player_hp).fill().right();
		table_shop.add(table_bunker_hp).fill().left();
		canvas.add(table_shop).grow().left().maxWidth(getRatioY(2f/3f)).top();
		
		Table table_wave = new Table(Game.NEON_UI);
		float scale_panel_slots = Gdx.graphics.getHeight() / 44f;
		NinePatch patch9_slots = Game.SCI_FI_ATLAS.createPatch("slot1");
		patch9_slots.scale(scale_panel_slots / 14f, scale_panel_slots / 14f);
		NinePatchDrawable nine_slots = new NinePatchDrawable(patch9_slots);
		//table_slots.setBackground(nine_slots);
		for(int i = 1; i <= 3; i++) {
			Table table_slot = new Table(Game.NEON_UI);
			table_slot.setBackground(nine_slots);
			
			if(Game.profile.get_slotItem(i) != Item.NULL) {
				Image img = new Image(Item.getItemByID(Game.profile.get_slotItem(i)).getIcon());
				img.setTouchable(Touchable.disabled);
				//img.setSize(getRatioY(0.90f/3f), getRatioY(0.90f/3f));
				table_slot.add(img).size(getRatioY(0.90f/3f/3f));
			}
			
			table_wave_slots.add(table_slot).maxSize(getRatioY(0.95f/3f)).uniform().top();
		}
		table_wave.add(table_wave_slots).top();
		table_wave.row();
		
		table_wave.add(label_wave).top().left().expandX();
		
		canvas.add(table_wave).fill().top();
		canvas.row();
		
		Table table_buts = new Table(Game.NEON_UI);
		table_buts.add(but_invenvtory).left();
		table_buts.add(but_play).right().expand().growX().maxWidth(getRatioY(0.9f/3f)).right();
		canvas.add(table_buts).colspan(2).growX().padTop(getRatio(1f/32f));
		
	}
	
	@Override
	public void update(SpriteBatch batch) {
		super.update(batch);
		func_switch.init();
		
		label_money.setText("$ " + Game.profile.get_money());
		label_wave.setText("Wave: " + Game.profile.get_wave());
		
		if(but_play.isChecked()) {
			but_play.setChecked(false);
			
			game.guiManager.reset();
			game.guiManager.addGui(new DAGuiInGame(game));
			game.CORE.constructLevel();
		}
		
		if(but_back.isChecked()) {
			but_back.setChecked(false);
			isGuiClose = true;
			System.out.println("	DAGuiPlayeMenu.update() ### but_back is pressed.");
		}
		
		this.label_player_hp.setText(Game.profile.get_playerHP() + "/" + Game.profile.get_playerMaxHP());
		this.label_bunker_hp.setText(Game.profile.get_bunkerHP() + "/" + Game.profile.get_bunkerMaxHP());
		
		for(int i = 1; i <= 3; i++) {
			Table table_slot = ((Table)table_wave_slots.getCells().get(i-1).getActor());
			
			if(Game.profile.get_slotItem(i) != Item.NULL) {
				Image img = new Image(Item.getItemByID(Game.profile.get_slotItem(i)).getIcon());
				img.setTouchable(Touchable.disabled);
				//img.setSize(getRatioY(0.90f/3f), getRatioY(0.90f/3f));
				if(table_slot.getCells().size > 0)
					table_slot.getCells().get(0).setActor(img).size(getRatioY(0.90f/3f/3f));
				else table_slot.add(img).size(getRatioY(0.90f/3f/3f));
			} else {
				table_slot.reset();
			}
		}
		
		updateButShopPlayer();
	}
	
	private void updateButShopPlayer() {
		if(but_shop_player.isChecked()) {
			but_shop_player.setChecked(false);
			
			func_switch.setBackward(false);
			func_switch.start();
			isGuiOpen = true;
			
			gui_shop_player = new DAGuiPlayerShop(game);
			getGuiManager().addGui(gui_shop_player);
			gui_shop_player.group_stage.setTouchable(Touchable.disabled);
		}
		
		if(isGuiOpen) {
			if(gui_shop_player != null) {
				gui_shop_player.canvas.setPosition(0,
					-Gdx.graphics.getHeight() + Gdx.graphics.getHeight() * func_switch.getValue());
			}
			
			canvas.setPosition(0,
					Gdx.graphics.getHeight() * func_switch.getValue());
			
			if(func_switch.hasEnded()) {
				func_switch.reload();
				func_switch.switchBackward();
				isGuiOpen = false;
				gui_shop_player.group_stage.setTouchable(Touchable.enabled);
				System.out.println("hasEnded");
			}
		}
		
		if(gui_shop_player != null && getGuiManager().guiList.peek() == gui_shop_player && gui_shop_player.isGuiClose) {
			func_switch.start();
			gui_shop_player.canvas.setPosition(0,
					-Gdx.graphics.getHeight() + Gdx.graphics.getHeight() * func_switch.getValue());
			
			canvas.setPosition(0,
					Gdx.graphics.getHeight() * func_switch.getValue());
			
			if(func_switch.hasEnded()) {
				func_switch.reload();
				func_switch.switchBackward();
				getGuiManager().removeGui();
			}
		}
	}
}
