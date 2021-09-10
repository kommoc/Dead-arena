package net.kommocgame.src.gui;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.github.czyzby.kiwi.util.gdx.scene2d.Alignment;

import net.kommocgame.src.Game;
import net.kommocgame.src.control.IMouse;
import net.kommocgame.src.control.InputHandler;
import net.kommocgame.src.entity.character.EntityPlayer;
import net.kommocgame.src.gui.DeadArena.DAGuiLoadingMenu;
import net.kommocgame.src.gui.game.DAGuiMainMenu_2;
import net.kommocgame.src.item.Slot;

public class GuiManager implements GuiHandler.ActionListener, IMouse {
	
	public Array<GuiBase> guiList = new Array<GuiBase>();
	public SpriteBatch guiBatch;
	public Stage stage; //EXPEREMENTAL FIXME
	public InputListener inputListener = new InputListener();
	public ClickListener clickListener = new ClickListener();
	private Game game;
	
	private boolean set_gui = false;
	
	private boolean touch_shooting = false;
	
	private long lastTimeTouch;
	private boolean touchDown_slot = false;
	private Slot touching_slot = null;
	private float x_press, y_press, x_offset, y_offset;
	private Image img_slot_grabbing;
	private boolean grabbing = false;
	
	public GuiManager(Game game) {
		guiBatch = new SpriteBatch();
		this.game = game;
		InputHandler.registerIMouse(this);
	}
	
	/** Set the new Stage. */
	public void setStage() {
		System.out.println("	GuiHandler.setStage() ### new instance for stage.");
		stage = new Stage(new ScreenViewport(), guiBatch);
		stage.addCaptureListener(clickListener);
		stage.addCaptureListener(inputListener);
		
		addGui(new DAGuiLoadingMenu(game));
	}
	
	/** Return ready to using. */
	public boolean getReady() {
		if(stage != null && set_gui == false) {
			set_gui = true;
			return true;
		}
		return false;
	}
	
	public Stage getStage() {
		return this.stage;
	}
	
	/** Remove last GUI screen from guiList. */
	@Deprecated
	public void removeGui() {
		//TODO gui dispose
		if(guiList.size == 0)
			return;
		
		stage.getActors().removeValue(stage.getActors().peek(), false);
		stage.getActors().peek().setTouchable(Touchable.enabled);
		
		if(guiList.peek() instanceof IMouse)
			InputHandler.deleteIMouse((IMouse)guiList.peek());
		guiList.removeValue(guiList.peek(), true);
	}
	
	/** Add to guiList[] new instance GUI screen. */
	@Deprecated
	public void addGui(GuiBase gui) {
		//TODO gui dispose
		for(int i = 0; i < stage.getActors().size; i++) {
			stage.getActors().get(i).setTouchable(Touchable.disabled);
		}
		guiList.add(gui);
		stage.addActor(gui.group_stage);
	}
	
	/** Last GUI screen change on other gui. */
	@Deprecated
	public void setGui(GuiBase gui) {
		//TODO gui dispose
		stage.getActors().set(stage.getActors().indexOf(stage.getActors().peek(), false), gui.group_stage);
		
		if(guiList.peek() instanceof IMouse)
			InputHandler.deleteIMouse((IMouse)guiList.peek());
		guiList.set(guiList.indexOf(guiList.peek(), false), gui);
	}
	
	/** Draw gui's. */
	public void updateGui(boolean preInit) {
		for(int i = 0; i < guiList.size && !preInit; i++) {
			guiList.get(i).update(guiBatch);
		} for(int i = 0; i < guiList.size && preInit; i++) {
			if(guiList.get(i) instanceof GuiHandler.PreInit) {
				((GuiHandler.PreInit)guiList.get(i)).preInit();
			}
		}
	}
	
	/** Render method. */
	public void renderGui() {
		updateGui(true);
		stage.draw();
		updateGui(false);
		
		updateMouseEvents();
	}
	
	/** Clean gui list. */
	public void reset() {
		stage.getActors().clear();
		
		for(int i = 0; i < guiList.size; i++) {
			if(guiList.get(i) instanceof IMouse)
				InputHandler.deleteIMouse((IMouse)guiList.get(i));
		}
		guiList.clear();
	}
	
	/** Event mouse clicked. */
	public void mouseClicked() {
		if(guiList.size == 0)
			return;
		
		GuiHandler.ActorPressed event = guiList.peek();
		Actor event_actor = guiList.peek().getObjInCursor();
		event.getActorPressedStage(event_actor);
	}
	
	/** Event ESC pressed. */
	public void escReleased() {
		for(int i = 0; i < guiList.size; i++) {
			if(guiList.get(i) instanceof GuiMainMenu || guiList.get(i) instanceof GuiMainMenuInGame) {
				return;
			}
		}
		
		//this.addGui(new GuiMainMenuInGame(game));
		this.addGui(new DAGuiMainMenu_2(game));
		game.world.deleteLevel();
		
		game.guiManager.reset();
		game.guiManager.addGui(new DAGuiMainMenu_2(game));
		
	}
	
	/** Event F2 pressed(console). */
	public void consoleReleased() {
		for(int i = 0; i < guiList.size; i++) {
			if(guiList.get(i) instanceof GuiConsole) {
				if(guiList.peek() instanceof GuiConsole) 
					removeGui();
				return;
			}
		}
		
		this.addGui(new GuiConsole(game));
	}
	
	/** Call's when screen has been changed. */
	@Deprecated
	public void resize() {
		//TODO resize screens.
		for(int i = 0; i < guiList.size; i++) {
			////guiList.get(i).group.setSize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
			//guiList.get(i).group_stage.setSize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		}
	}
	
	public void debugShapeRenderer(ShapeRenderer renderer) {
		if(guiList.size == 0)
			return;
		
		for(int i = 0; i < guiList.size; i++) {
			guiList.get(i).stageDebug(renderer);
		}
	}
	
	public GuiInGame getGuiInGame() {
		for(int i = 0; i < guiList.size; i++) {
			if(guiList.get(i) instanceof GuiInGame)
				return (GuiInGame)guiList.get(i);
		}
		
		return null;
	}
	
	public static <T extends Actor> T getInstanceOf(Group from, Class<T> type, Actor descendant) {
		if(descendant.isDescendantOf(from)) {
			Actor deActor = descendant;
			
			while(true) {
				//System.out.println("		GuiManager.getInstanceOf() ### deActor: " + deActor);
				if(deActor != from) {
					if(deActor.getClass() == type)
						return (T) deActor;
					else deActor = deActor.getParent();
				} else return null;
			}
		}
		
		System.out.println("		GuiManager.getInstanceOf() ### this actor is not descendant.");
		return null;
	}
	
	/************************************************** IMouse **************************************************/
	
	public void updateMouseEvents() {
		if(touch_shooting && this.getGuiInGame() != null && this.getGuiInGame() == guiList.peek() &&	// FIXME переработать. 
				this.getGuiInGame().getCurrentItemInSlot() != null) {								
			this.getGuiInGame().getCurrentItemInSlot().getItem().onItemUse(
					this.getGuiInGame().getCurrentItemInSlot(), (EntityPlayer)game.player);
		}
				
		if(touchDown_slot) {
			if(lastTimeTouch + touching_slot.getCatchTime() < System.currentTimeMillis()) {
				if(img_slot_grabbing == null) {
					img_slot_grabbing = new Image(touching_slot.getItemStack().getItem().getIcon());
					touching_slot.getStage().addActor(img_slot_grabbing);
					img_slot_grabbing.setTouchable(Touchable.disabled);
				}
				
				img_slot_grabbing.setPosition(x_press - x_offset, y_press - y_offset, Alignment.CENTER.get());
			}
		} else {
			lastTimeTouch = System.currentTimeMillis();
			
			if(img_slot_grabbing != null) {
				img_slot_grabbing.remove();
				img_slot_grabbing = null;
			}
		}
	}
	
	/** Event itemStack grabbing. */
	public boolean isGrab() {
		if(img_slot_grabbing != null)
			return true;
		else 
			return false;
	}
	
	/** Event shooting. */
	public boolean isShooting() {
		return touch_shooting;
	}
	
	/** Return the slot that from have been get itemStack. */
	public Slot getGrabbedSlot() {
		return touching_slot;
	}

	@Override
	public boolean touchDown(float x, float y) {
		if(guiList.size == 0) return false;
		
		if(guiList.peek().getStage().hit(x, y, true) instanceof Slot) {
			Slot slot = (Slot) guiList.peek().getStage().hit(x, y, true);
			
			if(slot.getItemStack() != null) {
				lastTimeTouch = System.currentTimeMillis();
				touchDown_slot = true;
				touching_slot = slot;
				
				x_press = x;
				y_press = y;
			}
		} else if(guiList.peek().getStage().hit(x, y, true) == null) {
			//Shooting.
			touch_shooting = true;
		}
		
		return true;
	}

	@Override
	public boolean touchDragged(float offsetX, float offsetY) {
		if(guiList.size == 0) return false;
		
		if(img_slot_grabbing != null) {
			x_offset = offsetX;
			y_offset = offsetY;
			
			return true;
		}
		Actor actor = guiList.peek().getStage().hit(x_press - offsetX, y_press - offsetY, true);
		
		if(actor instanceof Slot && (Slot)actor == touching_slot) {
			x_offset = offsetX;
			y_offset = offsetY;
		} else {
			touchDown_slot = false;
			touching_slot = null;
		}
		
		return true;
	}

	@Override
	public boolean touchUp() {
		if(guiList.size == 0) return false;
		
		if(touchDown_slot && touching_slot != null) {
			Actor actor = guiList.peek().getStage().hit(x_press - x_offset, y_press - y_offset, true);
			
			if(actor instanceof Slot && actor != touching_slot) {
				System.out.println("	TRANSFER");
				touching_slot.moveItemStackToSlot((Slot)actor);
			}
		}
		
		touchDown_slot = false;
		touching_slot = null;
		x_offset = 0;
		y_offset = 0;
		
		//Shooting.
		touch_shooting = false;
		
		return true;
	}

	@Override
	public void scrolled(int scroll) {
		
	}
	
	@Override
	public String toString() {
		if(stage == null)
			return "[GUIM-STAGE is not created]";
		
		return guiList.toString() + " ::: STAGE_SIZE: " + this.stage.getActors().size;
	}
}
