package net.kommocgame.src.gui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ScissorStack;
import com.github.czyzby.kiwi.util.gdx.scene2d.Alignment;

import net.kommocgame.src.Function;
import net.kommocgame.src.Game;
import net.kommocgame.src.Loader;
import net.kommocgame.src.control.InputHandler;
import net.kommocgame.src.item.Slot;
import net.kommocgame.src.render.GameManager;

@Deprecated
public class GuiInGameInventory extends GuiBase {
	
	private Curtains curtains;
	private GuiInGameMenu menu;
	
	public float back_w;
	public float back_h;
	
	public Button but_weapons;
	public Button but_items;
	//public Button but_features; DELETE
	//public Button but_description; DELETE
	public Button but_back;
	public Button but_deleteSlot;
	
	public Image hotBar_setSlot;//
	
	public Image line_center;
	public Image line_bot;
	public Image line_top;
	
	public Image background_items;
	public Image background_description;
	public EmblemPda emblem = new EmblemPda(Game.NEUTRALIZER_UI);
	
	public Actor slot_focus;
	public Image img_bezel = new Image(Loader.guiMenuSlot("slot_bezel.png"));
	
	public Table table_items_active = new Table(Game.NEUTRALIZER_UI);
	private Slot[] container_active_inv = new Slot[6];
	
	public Table field_items = new Table(game.COMMODORE_64_UI);
	public Table table_items = new Table(game.COMMODORE_64_UI);
	private Slot[] container_inv = new Slot[36];
	private Corner corner_items = new Corner(table_items);
	
	public ScissorStack stack = new ScissorStack();
	public ScrollPane field_items_scroll;
	public int count;

	public Function func_blend = new Function(200, Interpolation.exp5Out);
	public Function func_anim = new Function(2000, Interpolation.linear).setBouncing(true);
	
	public GuiInGameInventory(Game game, Curtains curtains, GuiInGameMenu menu) {
		super(game);
		this.curtains = curtains;
		this.menu = menu;
		curtains.expand();
	}
	
	public void addElements() {
		back_w = curtains.img_background.getWidth();
		back_h = curtains.img_background.getHeight();
		
		float wight = back_w / 3f;
		
		for(int i = 0; i < container_inv.length; i++) {
			//img_slot[i] = new Image(Loader.guiMenuSlot("inventory_slot.png"));
			//slot_inv[i] = new Table(Game.NEUTRALIZER_UI);
			//slot_inv[i].setBackground(GameManager.instance.getDrawable(Loader.guiMenuSlot("inventory_slot.png")));
			//slot_inv[i].setSize(32f * 4f * GameManager.instance.SCALE, 32f * 4f * GameManager.instance.SCALE);
			//container_inv[i] = new Slot(slot_inv[i]);
			container_inv[i] = new Slot();
			container_inv[i].setSize(32f * 4f * GameManager.instance.SCALE, 32f * 4f * GameManager.instance.SCALE);
			container_inv[i].setItemStackFIX(Game.CORE.getInventory().getItemStackInSlot(i));
			container_inv[i].setTouchable(Touchable.enabled);
			//slot_inv[i].add(container_inv[i]);
		}
		
		for(int i = 0; i < container_active_inv.length; i++) {
			//slot_active_slot[i] = new Table(Game.NEUTRALIZER_UI);
			//slot_active_slot[i].setBackground(GameManager.instance.getDrawable(Loader.guiMenuSlot("inventory_slot.png")));
			//slot_active_slot[i].setSize(32f * 4f * GameManager.instance.SCALE, 32f * 4f * GameManager.instance.SCALE);
			//container_active_inv[i] = new Slot(slot_active_slot[i]);
			container_active_inv[i] = new Slot();
			container_active_inv[i].setSize(32f * 4f * GameManager.instance.SCALE, 32f * 4f * GameManager.instance.SCALE);
			container_active_inv[i].setItemStackFIX(Game.CORE.getInventory().getItemStackInSlot(i));
			container_active_inv[i].setTouchable(Touchable.enabled);
			//slot_active_slot[i].add(container_active_inv[i]);
		}
		
		but_weapons = new Button(group_stage, 1, wight, (int)curtains.img_background.getX(ALIGNMENT.LEFT.get()),
				(int)curtains.img_background.getY(ALIGNMENT.TOP.get()) - (int)(getRatio(1f/7f)), "WEAPONS");
		but_items = new Button(group_stage, 1, wight, (int)but_weapons.img_end.get(0).getX(ALIGNMENT.LEFT.get()) + (int)wight,
				(int)but_weapons.img_main.get(0).getY(ALIGNMENT.BOTTOM.get()), "ITEMS").backward();
		
		but_back = new Button(group_stage, 1, wight / 1.25f, (int)(curtains.img_background.getX(ALIGNMENT.RIGHT.get()) - wight / 8.5f),
				(int)but_items.getY(), "BACK").backward();
		
		/** DELETE
		but_description = new Button(group_stage, 1, wight, (int)curtains.img_background.getX(ALIGNMENT.RIGHT.get()) - (int)(getRatio(2f/32f)),
				(int)but_weapons.img_main.get(0).getY(ALIGNMENT.BOTTOM.get()), "DESCRIPT").backward();
		but_features = new Button(group_stage, 1, wight, (int)but_description.img_end.get(0).getX(ALIGNMENT.LEFT.get()) - (int)wight - (int)but_description.img_end.get(0).getWidth() / 2,
				(int)curtains.img_background.getY(ALIGNMENT.TOP.get()) - (int)(getRatio(1f/7f)), "FEATURES");
		*/
		
		background_items = new Image(Loader.guiMenuBackground("color_background_2.png"));
		background_items.setPosition(curtains.img_background.getX(Alignment.LEFT.get()) + back_w / 24, curtains.img_background.getY(Alignment.BOTTOM.get()) + back_h / 3);
		float w = Gdx.graphics.getWidth() / 2 - background_items.getX(); // lenght for items
		float h = but_weapons.img_main.get(0).getY(Alignment.BOTTOM.get()) - curtains.img_background.getY(Alignment.BOTTOM.get()) - but_weapons.height;
		background_items.setSize(w, getRatio(3f/6f));
		background_items.setPosition(curtains.img_background.getX(Alignment.LEFT.get()) + back_w / 48, curtains.img_background.getY(Alignment.BOTTOM.get()) + back_h / 4);
		
		count = (int) (background_items.getWidth() / container_inv[0].getHeight());
		background_items.setSize(count * container_inv[0].getHeight(), background_items.getHeight());
		group_stage.addActor(background_items);
		
		w = curtains.img_background.getX(Alignment.RIGHT.get()) - background_items.getX(Alignment.RIGHT.get()); // lenght for descript
		h = background_items.getX() - curtains.img_background.getX(Alignment.LEFT.get());	// deference.
		background_description = new Image(Loader.guiMenuBackground("color_background_1.png"));
		background_description.setSize(w * 0.8f, background_items.getHeight());
		background_description.setPosition(curtains.img_background.getX(Alignment.RIGHT.get()) - h - background_description.getWidth(),
				background_items.getY());
		//group_stage.addActor(background_description);
		background_description.setColor(1, 1, 1, 0.5f);
		
		emblem.setPosition(curtains.img_background.getX(Alignment.RIGHT.get()) - w / 2f, background_description.getY(Alignment.CENTER.get()), Alignment.CENTER.get());
		group_stage.addActor(emblem);
		
		field_items.setPosition(background_items.getX(), background_items.getY());
		field_items.setWidth(background_items.getWidth());
		field_items.setHeight(background_items.getHeight());
		field_items.setBounds(field_items.getX(), field_items.getY(Alignment.BOTTOM.get()), field_items.getWidth(), field_items.getHeight());
		field_items.top();
		
		table_items_active.setSize(6 * container_inv[0].getHeight(), container_inv[0].getHeight());
		table_items_active.setPosition(background_items.getX(), curtains.img_background.getY() + h);
		group_stage.addActor(table_items_active);
		for(int i = 0; i < container_active_inv.length; i++)
			table_items_active.add(container_active_inv[i]).expand().grow();
		
		
		for(int i = 0; i < 36 / count; i++) {
			for(int j = 0; j < count; j++) {
				field_items.add(container_inv[i * count + j]).width(container_inv[i * count + j].getWidth()).height(container_inv[i * count + j].getHeight());
			}
			field_items.row();
		}
		
		field_items_scroll = new ScrollPane(field_items, game.NEUTRALIZER_UI);
		
		table_items.setPosition(background_items.getX(), background_items.getY());
		table_items.setBounds(background_items.getX(), background_items.getY(Alignment.BOTTOM.get()), background_items.getWidth(), background_items.getHeight());
		table_items.top();
		
		field_items_scroll.setFillParent(true);
		table_items.add(field_items_scroll);
		field_items_scroll.setFlickScrollTapSquareSize(container_inv[0].getHeight());
		field_items_scroll.setOverscroll(false, false);
		field_items_scroll.setupFadeScrollBars(0, 0);
		group_stage.addActor(table_items);
		
		img_bezel.setSize(container_inv[0].getWidth() * 1.125f, container_inv[0].getHeight() * 1.125f);
		img_bezel.setTouchable(Touchable.disabled);
		img_bezel.setVisible(false);
		group_stage.addActor(img_bezel);
		
		group_stage.addActor(corner_items);
		
		System.out.println("Bicube line: " +  container_inv[0].getHeight());
		System.out.println("Count of slots in the line: " + background_items.getWidth() / container_inv[0].getHeight());
	}
	
	public void getActorPressedStage(Actor actor) { //FIXME
		if(actor != null && actor instanceof Slot) {
			System.out.println("SLOT!!!: ");
			slot_focus = actor;
		}

		System.out.println("ACTOR: " + actor);
		System.out.println("acident: " + field_items_scroll.isAscendantOf(actor));

		if(but_back != null && but_back.pressedButton(actor, 0)) {
			curtains.narrow();
			menu.func_blend.reload();
			menu.func_blend.setBackward(false);
			menu.func_blend.start();
			game.guiManager.removeGui();
		}
	}
	
	public void update(SpriteBatch batch) {
		func_blend.init();
		
		if(curtains.hasExpanded()) {
			this.addElements();
			func_blend.start();
		}
		
		if(curtains.state == curtains.STATE_EXPAND) {
			but_weapons.setColor(1, 1, 1, func_blend.getValue());
			but_items.setColor(1, 1, 1, func_blend.getValue());
			
			but_weapons.setLabelColor(1, 1, 1, func_blend.getValue());
			but_items.setLabelColor(1, 1, 1, func_blend.getValue());
			
			table_items_active.setColor(1, 1, 1, func_blend.getValue());
			table_items.setColor(1, 1, 1, func_blend.getValue());
			emblem.setColor(1, 1, 1, func_blend.getValue());
			background_description.setColor(1, 1, 1, func_blend.getValue());
			
			/** DELETE
			but_description.setColor(1, 1, 1, func_blend.getValue());
			but_features.setColor(1, 1, 1, func_blend.getValue());
			
			but_description.setLabelColor(1, 1, 1, func_blend.getValue());
			but_features.setLabelColor(1, 1, 1, func_blend.getValue());*/
		}
		
		this.animBackground(func_anim);
		
		if(Gdx.input.isKeyPressed(Keys.P)) {
			//field_items_scroll.setBounds(background_items.getX(), background_items.getY(Alignment.TOP.get()), background_items.getWidth(), background_items.getHeight());
			//field_items_scroll.setPosition(background_items.getX(), background_items.getY());
			//field_items.setPosition(background_items.getX(), background_items.getY());
			background_items.setPosition(55, 55);
		}
		
		if(Gdx.input.isKeyPressed(Keys.I)) {
			field_items_scroll.setScrollPercentY(field_items_scroll.getScrollPercentY() - 0.01f);
		} else if(Gdx.input.isKeyPressed(Keys.O)) {
			field_items_scroll.setScrollPercentY(field_items_scroll.getScrollPercentY() + 0.01f);
		} else if(Gdx.input.isKeyPressed(Keys.M)) {
			table_items.layout();
		} else if(Gdx.input.isKeyPressed(Keys.N)) {
			field_items_scroll.layout();
		} else if(Gdx.input.isKeyPressed(Keys.G)) {
			field_items.setPosition(InputHandler.GUI_mouse_x, InputHandler.GUI_mouse_y);
		}
		
		if(slot_focus != null) {
			if(!img_bezel.isVisible())
				img_bezel.setVisible(true);
			
			Vector2 coord = slot_focus.localToStageCoordinates(new Vector2(slot_focus.getWidth() / 2f, slot_focus.getHeight() / 2f));
			img_bezel.setPosition(coord.x, coord.y, Alignment.CENTER.get());
		} else {
			img_bezel.setVisible(false);
		}
		//group_stage.debugAll();
		//field_items_scroll.setTouchable(Touchable.childrenOnly);
		
		//table_items.setTouchable(Touchable.childrenOnly);
		//field_items.setTouchable(Touchable.childrenOnly);
	}
	
	public void animBackground(Function func) {
		func.init();
		
		if(!func.getState()) {
			func.start();
		}
		
		if(background_items != null) {
			background_items.setColor(0, 1f - 70f * func_anim.getValue() / 256f, 1f - 50f * func_anim.getValue() / 256f, 0.9f);
		}
	}
}
