package net.kommocgame.src.gui.DeadArena;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.Map;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Button.ButtonStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ProgressBar;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.Touchpad;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.github.czyzby.kiwi.util.gdx.scene2d.Alignment;

import net.kommocgame.src.Function;
import net.kommocgame.src.Game;
import net.kommocgame.src.Loader;
import net.kommocgame.src.control.KeyBinding;
import net.kommocgame.src.entity.character.EntityPlayer;
import net.kommocgame.src.gui.GuiBase;
import net.kommocgame.src.gui.GuiManager;
import net.kommocgame.src.gui.DeadArena.DAGuiPlayerShop.ItemNode;
import net.kommocgame.src.gui.game.DAGuiMainMenu_2;
import net.kommocgame.src.item.Item;
import net.kommocgame.src.item.ItemWeapon;
import net.kommocgame.src.profile.ItemStack;

@Deprecated
public class DAGuiInGame extends GuiBase {
	
	
	Function func_open_menu	= new Function(250, Interpolation.exp5);
	Function func_blink		= new Function(400, Interpolation.circle).setBouncing(true);
	@Deprecated
	Function func_open_map	= new Function(750, Interpolation.exp5);
	
	Table canvas = new Table(Game.NEON_UI);
	ClickListener clickListener;
	
	Table table_items_bar	= new Table(Game.NEON_UI);
	Table table_item		= new Table(Game.NEON_UI);
	float item_size;
	Button but_switch_next		= new Button();
	Button but_switch_previos	= new Button(Game.KOMMOC_UI, "switch");
	
	Table table_ammo = new Table(Game.NEON_UI).center();
	Label label_reload			= new Label("RELOAD", Game.GENERATED_LABEL_STYLE_GOTTHARD_LITTLE_TEXT_WHITE);
	Label label_ammo_current	= new Label("", Game.GENERATED_LABEL_STYLE_GOTTHARD_LITTLE_TEXT_WHITE);
	Label label_ammo_max		= new Label("", Game.GENERATED_LABEL_STYLE_GOTTHARD_LITTLE_TEXT_WHITE);
	Label label_player_hp		= new Label("", Game.GENERATED_LABEL_STYLE_GOTTHARD_LITTLE_TEXT);
	@Deprecated
	Label label_bunker_hp		= new Label("", Game.GENERATED_LABEL_STYLE_GOTTHARD_LITTLE_TEXT);
	
	ProgressBar bar_progress	= new ProgressBar(0, 1, 0.01f, false, Game.NEUTRALIZER_UI);
	ProgressBar bar_hp_player	= new ProgressBar(0, Game.profile.get_playerMaxHP(), 1, false, Game.NEUTRALIZER_UI);
	@Deprecated
	ProgressBar bar_hp_bunker	= new ProgressBar(0, Game.profile.get_bunkerMaxHP(), 1, false, Game.NEUTRALIZER_UI);
	ProgressBar bar_reload		= new ProgressBar(0, 1, 0.01f, false, Game.NEUTRALIZER_UI);
	
	Table table_menu = new Table(Game.NEON_UI);
	Label label_menu = new Label("+ Menu", Game.GENERATED_LABEL_STYLE_GOTTHARD_MID_TEXT_WHITE);
	Button but_menu				= new Button(Game.NEON_UI);
	Button but_menu_inventory	= new Button(Game.NEON_UI);
	Button but_menu_setting		= new Button(Game.NEON_UI);
	Button but_menu_exit		= new Button(Game.NEON_UI);
	
	Table table_touchpad = new Table(Game.NEON_UI);
	Touchpad touchpad_left		= new Touchpad(getRatio(1f/32f), Game.NEON_UI);
	Touchpad touchpad_right		= new Touchpad(getRatio(1f/32f), Game.NEON_UI);
	Touchpad touchpad_grenade	= new Touchpad(getRatio(1f/16f), Game.NEON_UI);
	
	ItemNode slot_1	= new ItemNode(Item.getItemByID(Game.profile.get_slotItem(1)));
	ItemNode slot_2	= new ItemNode(Item.getItemByID(Game.profile.get_slotItem(2)));
	ItemNode slot_3	= new ItemNode(Item.getItemByID(Game.profile.get_slotItem(3)));
	ItemNode slot_hotBar;
	
	ItemStack current_item;//Item current_item;
	
	private Vector2 pos_center = new Vector2();
	
	Table table_map = new Table(Game.NEON_UI);
	Map map = new Map();
	@Deprecated
	Button but_map = new Button(Game.NEON_UI);
	@Deprecated
	Label label_map = new Label("+ Map", Game.GENERATED_LABEL_STYLE_GOTTHARD_MID_TEXT_WHITE);
	
	public DAGuiInGame(Game game) {
		super(game);
		canvas.setSize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		canvas.setPosition(0, 0);
		group_stage.addActor(canvas);
		
		group_stage.addActor(table_touchpad);
		table_touchpad.setSize(Gdx.graphics.getWidth(), getRatio(1f/2.5f));
		table_touchpad.add(touchpad_left).size(getRatio(1f/3f)).left().pad(getRatio(1f/32f));
		
		Table table_hotbar = new Table(Game.NEON_UI);
		table_hotbar.getColor().set(1, 1, 1, 0.85f);
		int maxCount = (int) (Gdx.graphics.getWidth() / 2f / getRatio(1f/6f));
		
		table_hotbar.add(slot_1).size(Gdx.graphics.getWidth() / 2f / maxCount).center().space(getRatioY(1f/128f));
		table_hotbar.add(slot_2).size(Gdx.graphics.getWidth() / 2f / maxCount).center().space(getRatioY(1f/128f));
		table_hotbar.add(slot_3).size(Gdx.graphics.getWidth() / 2f / maxCount).center().space(getRatioY(1f/128f));
		table_touchpad.add(table_hotbar).expandX().bottom().padBottom(getRatio(1f/32f));
		
		table_touchpad.add(touchpad_right).size(getRatio(1f/3f)).right().pad(getRatio(1f/32f));
		//table_touchpad.setPosition(0, getRatio(1f/32f));
		
		touchpad_right.getStyle().knob.setMinWidth(getRatio(1f/8f));
		touchpad_right.getStyle().knob.setMinHeight(getRatio(1f/8f));
		/** drawable once for all sticks. */
		
		group_stage.addActor(table_map);
		float scale_panel_map = Gdx.graphics.getHeight() / 54f;
		NinePatch patch9_map = Game.SCI_FI_ATLAS.createPatch("panel2");
		patch9_map.scale(scale_panel_map / 13f / 1.5f, scale_panel_map / 13f / 1.5f);
		patch9_map.setColor(new Color(1, 1, 1, 0.6f));
		NinePatchDrawable nine9_editor = new NinePatchDrawable(patch9_map);
		table_map.setBackground(nine9_editor);
		but_map.add(label_map).left().expandX();
		but_map.setColor(1, 1, 1, 0.8f);
		label_map.setColor(Color.CYAN);
		table_map.setSize(Gdx.graphics.getWidth() / 5f, Gdx.graphics.getWidth() / 5f);
		//table_map.setSize(Gdx.graphics.getWidth() / 4f, Gdx.graphics.getHeight() / 6f);
		//table_map.add(but_map).growX();
		//table_map.setPosition(0, Gdx.graphics.getHeight() / 2f, Alignment.LEFT.get());
		
		but_menu.add(label_menu).left().expandX();
		but_menu.setColor(1, 1, 1, 0.8f);
		label_menu.setColor(Color.CYAN);
		table_menu.top().left();
		table_menu.add(but_menu).growX();;
		float scale_panel_menu = Gdx.graphics.getHeight() / 28f;
		NinePatch patch9_menu = Game.SCI_FI_ATLAS.createPatch("panel1");
		patch9_menu.scale(scale_panel_menu / 15f, scale_panel_menu / 15f);
		patch9_menu.setColor(new Color(1, 1, 1, 0.6f));
		NinePatchDrawable nine9_menu = new NinePatchDrawable(patch9_menu);
		table_menu.setBackground(nine9_menu);
		group_stage.addActor(table_menu);
		table_menu.setSize(Gdx.graphics.getWidth() / 4f, Gdx.graphics.getHeight() / 5f);
		table_menu.setPosition(0, Gdx.graphics.getHeight(), Alignment.TOPLEFT.get());
		table_menu.setClip(true);
		but_menu_inventory.add(new Label("Inventory", Game.GENERATED_LABEL_STYLE_GOTTHARD_MID_TEXT));
		but_menu_setting.add(new Label("Settings", Game.GENERATED_LABEL_STYLE_GOTTHARD_MID_TEXT));
		but_menu_exit.add(new Label("Exit", Game.GENERATED_LABEL_STYLE_GOTTHARD_MID_TEXT));
		table_menu.row();
		table_menu.add(but_menu_inventory).left().padTop(Gdx.graphics.getHeight() / 32f).fillX();
		table_menu.row();
		table_menu.add(but_menu_setting).left().fillX();
		table_menu.row();
		table_menu.add(but_menu_exit).left().fillX();
		table_map.setPosition(0, table_menu.getY(Alignment.BOTTOM.get()), Alignment.TOPLEFT.get());
		
		ButtonStyle style = new ButtonStyle();
		
		TextureRegionDrawable draw_up = new TextureRegionDrawable(new TextureRegion(Loader.guiIcon("icon_next.png")));
		draw_up.getRegion().flip(true, false);
		style.up = draw_up;
		
		TextureRegionDrawable draw_down = new TextureRegionDrawable(new TextureRegion(Loader.guiIcon("icon_next_pressed.png")));
		draw_down.getRegion().flip(true, false);
		style.down = draw_down;
		
		but_switch_next.setStyle(style);
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
				
				///System.out.println("	DAGuiInGame(new instance) ### RELOAD!");
				if(current_item != null && current_item.getItem() instanceof ItemWeapon) {
					int ammo_curr = current_item.getUseSize();
					int ammo_max = 0;//((ItemWeapon)current_item.getItem()).getMaxAmmo();
					
					if(ammo_curr != ammo_max) {
						System.out.println("	DAGuiInGame(new instance) ### RELOAD! : " + current_item);
						((ItemWeapon)current_item.getItem()).reload();
					}
					
				}
			}
		});
		
		table_items_bar.add(but_switch_previos).size(getRatioY(1f/16f));
		table_items_bar.add(table_item).size(getRatioY(1f/6f), getRatio(1f/3.4f)).grow();
		table_items_bar.add(but_switch_next).size(getRatioY(1f/16f));
		
		group_stage.addActor(table_items_bar);
		table_items_bar.setBounds(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), getRatioY(1f/3.3f), getRatio(1f/3.3f));
		table_items_bar.setPosition(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), Alignment.TOPRIGHT.get());
		
		clickListener = new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				super.clicked(event, x, y);
				//System.out.println("	DAGuiInventory.new ClickListener().clicked() ### event.getTarget(): " + event.getTarget());
				//System.out.println("	DAGuiInventory.new ClickListener().clicked() ### event.getTarget().getParent(): " + event.getTarget().getParent());
				
				ItemNode node_hotBar = GuiManager.getInstanceOf(table_touchpad, ItemNode.class, event.getTarget());
				if(node_hotBar != null) {
					//System.out.println("	DAGuiInventory.new ClickListener().clicked() ### event.getTarget() is descedant of HOT_BAR");
					
					if(slot_hotBar == null) {
						slot_hotBar = node_hotBar;
						
						
					} else if(slot_hotBar != null) {
						if(slot_hotBar != node_hotBar)
							slot_hotBar.setChecked(false);
						
						//slot_hotBar == node_hotBar
						if(node_hotBar.isChecked()) {
							slot_hotBar = node_hotBar;
						} else slot_hotBar.setChecked(true);
					}
					
					if(slot_hotBar != null && slot_hotBar.getItem() != null) {
						//setCurrentItem(slot_hotBar.getItem().ID);
						slot_hotBar.setChecked(true);
					} else setCurrentItem(null);
				}
			}
		};
		table_touchpad.addListener(clickListener);
		
		label_reload.setTouchable(Touchable.disabled);
		label_ammo_current.setTouchable(Touchable.disabled);
		label_ammo_current.setTouchable(Touchable.disabled);
		label_reload.setColor(Color.CHARTREUSE);
		group_stage.addActor(label_reload);
			
		//set the help-vector
			pos_center.set(table_items_bar.getX(Alignment.CENTER.get()),
					table_items_bar.getY(Alignment.BOTTOM.get()));
		
		label_reload.setPosition(pos_center.x, pos_center.y + label_reload.getHeight() * 1.5f, Alignment.CENTER.get());
		
		table_ammo.setSize(table_items_bar.getWidth(), label_ammo_current.getHeight() * 1.5f);
		Table table_ammo_counter = new Table(Game.NEON_UI);
		table_ammo.add(table_ammo_counter).growX();
		table_ammo_counter.add(label_ammo_current).left().uniformX();
		table_ammo_counter.add(new Label("/", Game.GENERATED_LABEL_STYLE_GOTTHARD_LITTLE_TEXT_WHITE));
		table_ammo_counter.add(label_ammo_max).left().uniformX();
		table_ammo.row();
		table_ammo.add(bar_reload).growX().padTop(label_ammo_max.getHeight()).maxWidth(table_ammo.getWidth() / 1.8f);
		bar_reload.setColor(Color.CYAN);
		bar_reload.setAnimateDuration(0.025f);
		bar_reload.setAnimateInterpolation(Interpolation.linear);
		group_stage.addActor(table_ammo);
		table_ammo.setPosition(pos_center.x, pos_center.y - label_reload.getHeight() * 0.55f, Alignment.TOP.get());
		
		item_size = getRatio(1f/4.5f);
		
		Table table_bars = new Table(Game.NEON_UI).top();
		group_stage.addActor(table_bars);
		System.out.println("	DAGuiInGame(new instance) ### table_item.getX() " + table_menu.getHeight());
		table_bars.setSize(table_ammo.getX() - Gdx.graphics.getWidth() / 4f, table_menu.getHeight());
		table_bars.setPosition(Gdx.graphics.getWidth() / 4f, Gdx.graphics.getHeight(), Alignment.TOPLEFT.get());
		
		//table_bars.add(new Label("Bunker", Game.GENERATED_LABEL_STYLE_GOTTHARD_LITTLE_TEXT)).padTop(getRatio(1f/48f));
		table_bars.add(new Label("Player", Game.GENERATED_LABEL_STYLE_GOTTHARD_LITTLE_TEXT)).padTop(getRatio(1f/48f));
		table_bars.row();
		//table_bars.add(bar_hp_bunker).growX().uniformX().pad(Gdx.graphics.getWidth() / 48f).padTop(getRatio(1f/64f)).padBottom(getRatio(1f/64f));
		table_bars.add(bar_hp_player).growX().uniformX().pad(Gdx.graphics.getWidth() / 48f).padTop(getRatio(1f/64f)).padBottom(getRatio(1f/64f));
		//bar_hp_bunker.setColor(Color.GREEN);
		bar_hp_player.setColor(Color.GREEN);
		bar_hp_player.setValue(Game.profile.get_playerHP());
		//bar_hp_bunker.setValue(Game.profile.get_bunkerHP());
		table_bars.row();
		//label_bunker_hp.setText("" + Game.profile.get_bunkerHP() + "/" + Game.profile.get_bunkerMaxHP());
		label_player_hp.setText("" + Game.profile.get_playerHP() + "/" + Game.profile.get_playerMaxHP());
		//table_bars.add(label_bunker_hp).expandX().center();
		table_bars.add(label_player_hp).expandX().center();
		
		//table_bars.setDebug(true);
		func_blink.start();
	}
	
	@Override
	public void update(SpriteBatch batch) {
		super.update(batch);
		func_blink.init();
		func_open_menu.init();
		func_open_map.init();
		//group_stage.debugAll();
		//table_items_bar.debugAll();
		//table_touchpad.debugAll();
		//table_ammo.debugAll();
		
		if(but_menu.isChecked()) {
			but_menu.setChecked(false);
			
			if(func_open_menu.getValue() == 0) {
				label_menu.setText("- Menu");
			} else if(func_open_menu.getValue() == 1) {
				label_menu.setText("+ Menu");
			}
			
			func_open_menu.start();
		}
		
		if(func_open_menu.getState()) {
			table_menu.setSize(Gdx.graphics.getWidth() / 4f + Gdx.graphics.getWidth() / 10f * func_open_menu.getValue(),
					Gdx.graphics.getHeight() / 5f + Gdx.graphics.getHeight() / 2.5f * func_open_menu.getValue());
			table_menu.setPosition(0, Gdx.graphics.getHeight(), Alignment.TOPLEFT.get());
			
			table_map.setColor(1, 1, 1, 1f - func_open_menu.getValue());
		} else if(func_open_menu.hasEnded()) {
			func_open_menu.reload();
			func_open_menu.switchBackward();
		}
		
		if(func_open_menu.getValue() == 1) {
			//menu buttons init.
			if(but_menu_exit.isChecked()) {
				but_menu_exit.setChecked(true);
				
				Game.CORE.deconstructLevel();
				game.guiManager.reset();
				game.guiManager.addGui(new DAGuiMainMenu_2(game));
			}
		}
		
		if(Game.getPlayer() != null) {
			KeyBinding.setMoveKnob(touchpad_left.getKnobPercentX(), touchpad_left.getKnobPercentY(), touchpad_right.getKnobPercentX(), 
					touchpad_right.getKnobPercentY());
		}
		
		butSwitchUpdate();
		butSlotsUpdate();
		mapUpdate();
		labelHpUpdate();
		
		if(but_menu_inventory.isChecked()) {
			but_menu_inventory.setChecked(false);
			
			getGuiManager().addGui(new DAGuiInventory(game));
		}
		
		if(but_menu_setting.isChecked()) {
			but_menu_setting.setChecked(false);
			
			getGuiManager().addGui(new DAGuiSettings(game));
		}
		
		if(current_item != null && current_item.getItem() instanceof ItemWeapon) {
			int ammo_curr = current_item.getUseSize();
			int ammo_max = 0;//((ItemWeapon)current_item.getItem()).getMaxAmmo();
			label_ammo_current.setText("" + ammo_curr);
			label_ammo_max.setText("" + ammo_max);
			table_ammo.setVisible(true);
			
			if(ammo_curr != ammo_max) {
				label_reload.setVisible(true);
				label_reload.getColor().a = 0.4f + 0.6f * func_blink.getValue();
			} else label_reload.setVisible(false);
		} else {
			label_reload.setVisible(false);
			table_ammo.setVisible(false);
		}
		
		if(current_item.getItem() instanceof ItemWeapon && ((ItemWeapon)current_item.getItem()).isReloading()) {
			if(!bar_reload.isVisible()) {
				bar_reload.setVisible(true);
			}
			
			label_reload.getColor().a = 1f;
			bar_reload.setValue(((ItemWeapon)current_item.getItem()).getReloadProgress());
		} else { 
			if(bar_reload.isVisible()) {
				bar_reload.setVisible(false);
				bar_reload.setValue(0);
			}
		}
	}
	
	public void mapUpdate() {
		if(!Game.profile.settings_minimapIsEnable()) {
			if(table_map.isVisible())
				table_map.setVisible(false);
			
			return;
		} else {
			if(!table_map.isVisible())
				table_map.setVisible(true);
		}
		
	}
	
	public void labelHpUpdate() {
		if(Game.CORE.getPlayer() != null && !Game.CORE.getPlayer().isDead()) {
			label_player_hp.setText(Game.CORE.getPlayer().getHP() + "/" + Game.CORE.getPlayer().getMaxHP());
			bar_hp_player.setValue(Game.CORE.getPlayer().getHP());
			bar_hp_player.setColor(1f - (float) Game.CORE.getPlayer().getHP() / (float) Game.CORE.getPlayer().getMaxHP(),
					(float) Game.CORE.getPlayer().getHP() / (float) Game.CORE.getPlayer().getMaxHP(), 0, 1);
		} else {
			label_player_hp.setText("Player is dead");
		}
		
		/**if(Game.CORE.getBunker() != null && !Game.CORE.getBunker().isDead()) {
			label_bunker_hp.setText(Game.CORE.getBunker().getHP() + "/" + Game.CORE.getBunker().getMaxHP());
			bar_hp_bunker.setValue(Game.CORE.getBunker().getHP());
			bar_hp_bunker.setColor(1f - (float) Game.CORE.getBunker().getHP() / (float) Game.CORE.getBunker().getMaxHP(),
					(float) Game.CORE.getBunker().getHP() / (float) Game.CORE.getBunker().getMaxHP(), 0, 1);
		} else {
			label_bunker_hp.setText("Bunker is destroyed");
		}*/
	}
	
	private void butSlotsUpdate() {
		//for changing slots in inventory
		/*if(slot_1.isChecked() && slot_hotBar != null && (slot_hotBar != slot_1 || slot_1.getItem() != current_item)) {
			slot_1.setChecked(false);
		} else if(slot_2.isChecked() && slot_hotBar != null && (slot_hotBar != slot_2 || slot_3.getItem() != current_item)) {
			slot_2.setChecked(false);
		} else if(slot_3.isChecked() && slot_hotBar != null && (slot_hotBar != slot_3 || slot_3.getItem() != current_item)) {
			slot_3.setChecked(false);
		}*/
		
		if(slot_1.getItem() != Item.getItemByID(Game.profile.get_slotItem(1))) {
			slot_1.setItem(Item.getItemByID(Game.profile.get_slotItem(1)));
		} else if(slot_2.getItem() != Item.getItemByID(Game.profile.get_slotItem(2))) {
			slot_2.setItem(Item.getItemByID(Game.profile.get_slotItem(2)));
		} else if(slot_3.getItem() != Item.getItemByID(Game.profile.get_slotItem(3))) {
			slot_3.setItem(Item.getItemByID(Game.profile.get_slotItem(3)));
		}
		
		//for but switching
		/*if(!slot_1.isChecked() && slot_1.getItem() == current_item) {
			if(slot_1.getItem() != null) {
				slot_hotBar = slot_1;
				slot_hotBar.setChecked(true);
			}
		} else if(!slot_2.isChecked() && slot_2.getItem() == current_item) {
			if(slot_2.getItem() != null) {
				slot_hotBar = slot_2;
				slot_hotBar.setChecked(true);
			}
		} else if(!slot_3.isChecked() && slot_3.getItem() == current_item) {
			if(slot_3.getItem() != null) {
				slot_hotBar = slot_3;
				slot_hotBar.setChecked(true);
			}
		}*/
	}
	/* Introduce slots checking (after changing in inventory). */
	private void butSwitchUpdate() {
		int id = Item.NULL;
		
		if(but_switch_next.isChecked()) {
			but_switch_next.setChecked(false);
			/*
			if(((EntityPlayer)Game.getPlayer()).DA_getCurrentItem() != null) {
				id = searchNext(((EntityPlayer)Game.getPlayer()).DA_getCurrentItem().ID, true);
				setCurrentItem(id);
			} else setCurrentItem(0);*/
		}
		
		if(but_switch_previos.isChecked()) {
			but_switch_previos.setChecked(false);
			/*
			if(((EntityPlayer)Game.getPlayer()).DA_getCurrentItem() != null) {
				id = searchNext(((EntityPlayer)Game.getPlayer()).DA_getCurrentItem().ID, false);
				setCurrentItem(id);
			} else setCurrentItem(0);*/
		}
	}
	
	private int searchNext(int par1, boolean next) {
		int last_index = par1;
		
		if(next) {
			for(int i = ++par1; i <= Item.itemList.length; i++) {
				if(Game.profile.get_itemPurchase(i))
					return i;
			}
			
			for(int i = 0; i <= last_index; i++) {
				if(Game.profile.get_itemPurchase(i))
					return i;
			}
		} else {
			for(int i = --par1; i >= 0; i--) {
				if(Game.profile.get_itemPurchase(i))
					return i;
			}
			
			for(int i = Item.itemList.length; i >= last_index; i--) {
				if(Game.profile.get_itemPurchase(i))
					return i;
			}
		}
		
		return Item.NULL;
	}
	
	private void setCurrentItem(ItemStack item) {
		if(slot_hotBar != null) slot_hotBar.setChecked(false);
		
		if(table_item.getCells().size > 0) {
			if(item != null) {
				Image img = new Image(item.getItem().getIcon());
				img.setTouchable(Touchable.disabled);
				
				table_item.getCells().get(0).setActor(img).size(item_size);
				((EntityPlayer)Game.getPlayer()).DA_setCurrentItem(item.getItem());
				current_item = item;
			} else {
				((EntityPlayer)Game.getPlayer()).DA_setCurrentItem(item.getItem());
				table_item.getCells().get(0).setActor(null).size(item_size);
				current_item = item;
			}
		} else {
			if(item != null) {
				Image img = new Image(item.getItem().getIcon());
				img.setTouchable(Touchable.disabled);
				
				table_item.add(img).size(item_size);
				((EntityPlayer)Game.getPlayer()).DA_setCurrentItem(item.getItem());
				current_item = item;
			}
		}
	}
}
