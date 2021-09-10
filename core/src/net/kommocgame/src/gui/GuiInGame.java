package net.kommocgame.src.gui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Button.ButtonStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.Touchpad;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.github.czyzby.kiwi.util.gdx.scene2d.Alignment;

import net.kommocgame.src.Function;
import net.kommocgame.src.Game;
import net.kommocgame.src.Loader;
import net.kommocgame.src.SoundManager;
import net.kommocgame.src.control.KeyBinding;
import net.kommocgame.src.entity.character.EntityPlayer;
import net.kommocgame.src.entity.props.EntityItem;
import net.kommocgame.src.item.ActiveSlot;
import net.kommocgame.src.profile.ItemStack;
import net.kommocgame.src.render.GameManager;
@Deprecated
public class GuiInGame extends GuiBase {
	
	public Label text_hp = new AnimText("HP: ", Game.LABEL_STYLE_CONSOLE_72);
	public Label text_money = new AnimText("$: ", Game.LABEL_STYLE_CONSOLE_72);
	public AnimText text_pda = new AnimText("PDA", Game.LABEL_STYLE_CONSOLE_72);
	public Touchpad touchpad = new Touchpad(1, Game.NEUTRALIZER_UI);
	
	public int hp;
	public int money;
	
	public Image img_pda = new Image(Loader.guiMenuBut("button_2.png"));
	
	public Table table_items_up = new Table();
	public Table table_items_down = new Table();
	
	public ActiveSlot[] container_down = new ActiveSlot[3];
	public ActiveSlot[] container_up = new ActiveSlot[3];
	
	private Table table_actionSensor;
	
	private ActiveSlot current_slot;
	
	private Function func_sensor = new Function(300, Interpolation.exp5Out);
	private Button but_switch_previos;
	private Button but_switch_next;
	private Image detected_item;
	private int item_index = 0;
	private int item_prev_index = 0;
	private int item_count = 0;
	private int last_item_count = 0;
	
	private Button but_take_item = new Button(Game.KOMMOC_UI);
	
	public GuiInGame(Game game) {
		super(game);
		
		img_pda.setSize(getRatio(1f/10f) * 4, getRatio(1f/10f));
		group_stage.addActor(img_pda);
		
		group_stage.addActor(text_hp);
		group_stage.addActor(text_money);
		group_stage.addActor(text_pda);
		
		group_stage.addActor(touchpad);
		touchpad.setName("TOUCHPAD");
		touchpad.setBounds(getRatio(1f/12f), getRatio(1f/12f), getRatio(1f/4f), getRatio(1f/4f));
		touchpad.addListener(game.guiManager.inputListener);
		this.setLeftSide(img_pda, false);
		
		text_pda.setSize(text_pda.getWidth() * 0.6f * GameManager.instance.SCALE, text_pda.getHeight() * 0.6f * GameManager.instance.SCALE);
		text_pda.setPosition(img_pda.getX(ALIGNMENT.CENTER.get()), img_pda.getY(ALIGNMENT.BOTTOM.get()) + img_pda.getHeight() / 6f, Alignment.BOTTOM.get());
		text_pda.setFontScale(0.7f * GameManager.instance.SCALE);
		
		table_items_down.setSize(getRatio(1.2f/6f) * 3f, getRatio(1.2f/6f));
		table_items_up.setSize(getRatio(1.2f/6f) * 3f, getRatio(1.2f/6f));
		
		group_stage.addActor(table_items_down);
		group_stage.addActor(table_items_up);
		
		table_items_down.setPosition(Gdx.graphics.getWidth() / 2f, 0, Alignment.BOTTOM.get());
		table_items_up.setPosition(Gdx.graphics.getWidth() / 2f, Gdx.graphics.getHeight(), Alignment.TOP.get());
		
		for(int i = 0; i < 3; i++) {
			container_down[i] = new ActiveSlot(36 + i);
			container_down[i].setSize(getRatio(1.2f/6f), getRatio(1.2f/6f));
			container_up[i] = new ActiveSlot(39 + i);
			container_up[i].setSize(getRatio(1.2f/6f), getRatio(1.2f/6f));
			
			container_down[i].setTouchable(Touchable.enabled);
			container_up[i].setTouchable(Touchable.enabled);
			
			table_items_down.add(container_down[i]).width(getRatio(1.2f/6f)).height(getRatio(1.2f/6f));
			table_items_up.add(container_up[i]).width(getRatio(1.2f/6f)).height(getRatio(1.2f/6f));
		}
		
		table_items_down.setColor(1, 1, 1, 0.6f);
		table_items_up.setColor(1, 1, 1, 0.6f);
		
		
		table_actionSensor = new Table(Game.KOMMOC_UI) {
			@Override
			protected void drawBackground (Batch batch, float parentAlpha, float x, float y) {
				super.drawBackground(batch, 0.6f, x, y);
			}
		};
		
		but_switch_previos = new Button(Game.KOMMOC_UI, "switch");
		ButtonStyle style = new ButtonStyle();
		
		TextureRegionDrawable draw_up = new TextureRegionDrawable(new TextureRegion(Loader.guiIcon("icon_next.png")));
		draw_up.getRegion().flip(true, false);
		style.up = draw_up;
		
		TextureRegionDrawable draw_down = new TextureRegionDrawable(new TextureRegion(Loader.guiIcon("icon_next_pressed.png")));
		draw_down.getRegion().flip(true, false);
		style.down = draw_down;
		but_switch_next = new Button(style);
		
		table_actionSensor.setBackground("panel1");
		table_actionSensor.setSize(Gdx.graphics.getWidth() - table_items_up.getX(Alignment.RIGHT.get()), getRatio(1.3f/5f));
		table_actionSensor.setPosition(Gdx.graphics.getWidth() + table_actionSensor.getWidth() * 0.0625f, Gdx.graphics.getHeight(), Alignment.TOPRIGHT.get());
		table_actionSensor.setSize(table_actionSensor.getWidth() * 14f / 16f, table_actionSensor.getHeight());
		
		table_actionSensor.add(but_switch_previos).size(table_actionSensor.getWidth()/5f);
		//table_actionSensor.add(detected_item).maxSize(table_actionSensor.getWidth()/5f * 3f);
		table_actionSensor.add().maxSize(table_actionSensor.getHeight()).minSize(table_actionSensor.getHeight());
		table_actionSensor.add(but_switch_next).size(table_actionSensor.getWidth()/5f);
		group_stage.addActor(table_actionSensor);
		
		but_take_item.setSize(table_actionSensor.getWidth() * 6f / 8f, table_actionSensor.getHeight() / 3f);
		but_take_item.add(new Label("Take item", Game.LABEL_STYLE_CONSOLE_48));
		but_take_item.setPosition(table_actionSensor.getX(Alignment.CENTER.get()), table_actionSensor.getY(Alignment.BOTTOM.get()) - but_take_item.getHeight(),
				Alignment.CENTER.get());
		group_stage.addActor(but_take_item);
		
		
		this.setActiveSlot(container_down[0]);
	}
	
	public void getActorPressedStage(Actor actor) {
		if(img_pda == actor) {
			//SoundManager.playSound(SoundManager.sound_ui_button);
			game.guiManager.addGui(new GuiInGameMenuV2(game));
		}
		
		if(actor instanceof ActiveSlot) {
			this.setActiveSlot((ActiveSlot)actor);
		}
	}
	
	public ItemStack getCurrentItemInSlot() {
		return current_slot != null ? current_slot.getItemStack() : null;
	}
	
	public Actor getIconItemSensor() {
		return table_actionSensor.getCells().get(1).getActor(); /////TODO дописать выбор иконки рассматриваемого итема
	}
	
	public void setActiveSlot(ActiveSlot slot) {
		this.current_slot = slot;
		
		for(int i = 0; i < 3; i++) {
			container_down[i].setActive(false);
			container_up[i].setActive(false);
		}
		
		current_slot.setActive(true);
	}
	
	public void setIconItemSensor(Actor icon) {
		table_actionSensor.getCells().get(1).setActor(icon);
		
		if(table_actionSensor.getCells().get(1).getActorWidth() != table_actionSensor.getCells().get(1).getActorHeight() && icon != null) {
			if(table_actionSensor.getCells().get(1).getActorWidth() < table_actionSensor.getCells().get(1).getActorHeight())
				table_actionSensor.getCells().get(1).width(table_actionSensor.getCells().get(1).getActorWidth());
			else table_actionSensor.getCells().get(1).height(table_actionSensor.getCells().get(1).getActorWidth());
		}
	}
	
	public void update(SpriteBatch batch) {
		func_sensor.init();
		
		if(but_switch_next.isChecked()) {
			but_switch_next.setChecked(false);
			item_index++;
		}
		
		if(but_switch_previos.isChecked()) {
			but_switch_previos.setChecked(false);
			item_index--;
		}
		
		if(game.getPlayer() != null && game.getPlayer() instanceof EntityPlayer) {
			EntityPlayer player = (EntityPlayer)game.getPlayer();
			
			if(player.getActionSensor().isActive()) {
				item_count = 0;
				
				if(but_take_item.isChecked()) {
					int _item = 0;
					
					for(int i = 0; i < player.getActionSensor().getContactList().size; i++) {
						if(player.getActionSensor().getContactList().get(i) instanceof EntityItem) {
							System.out.println("_item: " + _item);
							if(_item != item_index) {
								_item++;
								continue;
							} else {
								
								break;
							}
						}
					}
				}
				
				for(int i = 0; i < player.getActionSensor().getContactList().size; i++) {
					if(player.getActionSensor().getContactList().get(i) instanceof EntityItem) {
						item_count++;
					}
				}
				
				
				if(item_index >= item_count) {
					item_index = item_count - 1;
				}
				
				if(item_index < 0) {
					item_index = 0;
				}
				
				
				
				if(item_index != item_prev_index || last_item_count != item_count) {
					int _item = 0;
					System.out.println("\n	item_index: " + item_index);
					System.out.println("	item_prev_index: " + item_prev_index);
					
					
					for(int i = 0; i < player.getActionSensor().getContactList().size; i++) {
						if(player.getActionSensor().getContactList().get(i) instanceof EntityItem) {
							System.out.println("_item: " + _item);
							if(_item != item_index) {
								_item++;
								continue;
							} else {
								System.out.println("	SETICON");
								this.setIconItemSensor(new Image(((EntityItem)player.getActionSensor().getContactList().get(i)).getItemStack().getItem().getIcon()));
								item_prev_index = item_index;
								break;
							}
						}
					}
				}
				
				last_item_count = item_count;
				
			} else {
				item_prev_index = -1;
				item_index = 0;
				
				if(getIconItemSensor() != null)
					this.setIconItemSensor(null);
			}
		}
		
		if(but_take_item.isChecked()) {
			but_take_item.setChecked(false);
		}
		
		if(this.game.guiManager.guiList.peek() == this && GameManager.instance.cursor != 1)
			GameManager.instance.setCursorCrosshair();
		
		touchpad.debug();
		KeyBinding.setMoveKnob(touchpad.getKnobPercentX(), touchpad.getKnobPercentY(), 0, 0);
		
		//table_actionSensor.debug();
		//group_stage.debugAll();
	}

}
