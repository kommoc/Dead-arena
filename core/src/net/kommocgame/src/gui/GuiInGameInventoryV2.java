package net.kommocgame.src.gui;

import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;
import com.github.czyzby.kiwi.util.gdx.scene2d.Alignment;

import net.kommocgame.src.Game;
import net.kommocgame.src.Loader;
import net.kommocgame.src.entity.character.EntityPlayer;
import net.kommocgame.src.item.Slot;
import net.kommocgame.src.render.GameManager;

@Deprecated
public class GuiInGameInventoryV2 extends GuiBase {
	
	private CurtainsV2 curtains;
	private Table table_canvas;
	private GuiInGameMenuV2 menu;
	
	Button but = new Button(Game.KOMMOC_UI, "default");
	Button but_close = new Button(Game.KOMMOC_UI, "default");
	
	private Image icon_deleteItem = new Image(Loader.guiMenuSlot("icon_deleteItem.png"));
	private Slot grabbedSlot;
	
	private Slot[] container_inv = new Slot[36];
	private Slot[] container_inv_active = new Slot[6];
	
	private ScrollPane table_items_scroll;
	
	public GuiInGameInventoryV2(Game game, CurtainsV2 curtains, GuiInGameMenuV2 menu) {
		super(game);
		this.curtains = curtains;
		table_canvas = new Table(Game.NEUTRALIZER_UI);
		this.menu = menu;
		curtains.expand();
		
		group_stage.addActor(table_canvas);
	}
	
	private void addElementsV2() {
		Button back = new Button(Game.KOMMOC_UI, "default");

		but.add(new AnimText("Items", Game.LABEL_STYLE_CONSOLE_72));
		but_close.add(new AnimText("Weapons", Game.LABEL_STYLE_CONSOLE_72));
		back.add(new AnimText("BACK", Game.LABEL_STYLE_CONSOLE_72));
		but.setClip(true);
		but_close.setClip(true);
		back.setClip(true);
		
		table_canvas.setClip(true);
		table_canvas.pad(24f);
		table_canvas.top();
		
		for(int i = 0; i < container_inv.length; i++) {
			container_inv[i] = new Slot(i);
			container_inv[i].setSize(32f * 4f * GameManager.instance.SCALE, 32f * 4f * GameManager.instance.SCALE);
			container_inv[i].setItemStackFIX(Game.CORE.getInventory().getItemStackInSlot(i));
			container_inv[i].setTouchable(Touchable.enabled);
		}
		
		for(int i = 0; i < container_inv_active.length; i++) {
			container_inv_active[i] = new Slot(36 + i);
			container_inv_active[i].setSize(32f * 4f * GameManager.instance.SCALE, 32f * 4f * GameManager.instance.SCALE);
			container_inv_active[i].setItemStackFIX(Game.CORE.getInventory().getItemStackInSlot(i));
			container_inv_active[i].setTouchable(Touchable.enabled);
		}
		
		Table field_items_active = new Table();
		field_items_active.setBackground(new NinePatchDrawable(Game.SCI_FI_ATLAS.createPatch("panel1")));
		field_items_active.pad(16f).padTop(18f).padBottom(18f);
		field_items_active.setClip(true);
		Table table_items_active = new Table();
		
		for(int i = 0; i < container_inv_active.length; i++) {
			table_items_active.add(container_inv_active[i]).width(container_inv_active[i].getWidth()).height(container_inv_active[i].getHeight());
		}
		
		System.out.println("1) SIZE_ON_X: " + table_canvas.getWidth());
		but.setBackground(new NinePatchDrawable(Game.SCI_FI_ATLAS.createPatch("panel1")));
		table_canvas.add(but).width(table_canvas.getWidth() / 3.5f).height(GuiBase.getRatio(1f/7f)).left().pad(10f);
		table_canvas.add(but_close).width(table_canvas.getWidth() / 3.5f).height(GuiBase.getRatio(1f/7f)).left().pad(10f);
		table_canvas.add(back).width(table_canvas.getWidth() / 3.5f).height(GuiBase.getRatio(1f/7f)).right().expandX().pad(10f);
		table_canvas.row();
		System.out.println("2) SIZE_ON_X: " + table_canvas.getWidth());
		Table field_items = new Table(Game.NEUTRALIZER_UI);
		
		NinePatch patch9 = Game.SCI_FI_ATLAS.createPatch("panel1");
		patch9.scale(1, 1);
		NinePatchDrawable nine9 = new NinePatchDrawable(patch9);
		field_items.setBackground(nine9);
		field_items.pad(16f).padTop(18f).padBottom(18f);
		field_items.setClip(true);
		
		int count = (int) (table_canvas.getWidth() / 2f / container_inv[0].getHeight());
		Table table_items = new Table(Game.NEUTRALIZER_UI);
		System.out.println("COUNT: " + count);
		
		for(int i = 0; i < 36 / count; i++) {
			for(int j = 0; j < count; j++) {
				table_items.add(container_inv[i * count + j]).width(container_inv[i * count + j].getWidth()).height(container_inv[i * count + j].getHeight());
			}
			table_items.row();
		}
		
		table_items_scroll = new ScrollPane(table_items, Game.NEUTRALIZER_UI);
		table_items_scroll.setFlickScrollTapSquareSize(container_inv[0].getHeight() / 2f);
		table_items_scroll.setOverscroll(false, false);
		table_items_scroll.setupFadeScrollBars(0, 0);
		
		field_items.add(table_items_scroll);
		table_canvas.add(field_items).colspan(3).left().expandY();
		table_canvas.row();
		
		field_items_active.add(table_items_active);
		table_canvas.add(field_items_active).left().colspan(2);
		table_canvas.top();
		table_canvas.add(icon_deleteItem).center().width(getRatio(1f/7f)).height(getRatio(1f/7f));
		
		System.out.println("3) SIZE_ON_X: " + table_canvas.getWidth());
	}
	
	public void getActorPressedStage(Actor actor) { //FIXME
		System.out.println((getGuiManager().getGrabbedSlot() != null));
		System.out.println("Acotr:" + actor);
		
		if(grabbedSlot != null && actor == icon_deleteItem) {
			//ENTITY DROP SLOT
			grabbedSlot.deleteItemStack();
		}
		
		this.grabbedSlot = null;
	}
	
	public void update(SpriteBatch batch) {
		if(game.guiManager.isGrab()) {
			table_items_scroll.cancel();
		}
		
		group_stage.debugAll();
		if(curtains.hasExpanded()) {
			this.addElementsV2();
			System.out.println("		ADD_ELEMTNTS");
		}
		
		if(but.isPressed()) {
			curtains.narrow();
			menu.func_blend.reload();
			menu.func_blend.setBackward(false);
			menu.func_blend.start();
			
			game.guiManager.removeGui();
		}
		
		if(but_close.isPressed()) {
			game.guiManager.removeGui();
			game.guiManager.removeGui();
		}
		
		if(this.grabbedSlot == null) 
			this.grabbedSlot = getGuiManager().getGrabbedSlot();
		
		_table_update();
	}
	
	@Deprecated
	private void _table_update() {
		if(table_canvas == null && curtains == null)
			return;
			
		if(table_canvas.getWidth() != curtains.getWidth() || table_canvas.getHeight() != curtains.getHeight()) {
			table_canvas.setSize(curtains.getWidth(), curtains.getHeight());
		}
		
		if(table_canvas.getX(Alignment.CENTER.get()) != curtains.getX(Alignment.CENTER.get()) ||
				table_canvas.getY(Alignment.CENTER.get()) != curtains.getY(Alignment.CENTER.get())) {
			table_canvas.setPosition(curtains.getX(Alignment.CENTER.get()), curtains.getY(Alignment.CENTER.get()), Alignment.CENTER.get());
		}
			
	}
}
