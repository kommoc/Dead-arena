package net.kommocgame.src.gui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.Widget;
import com.badlogic.gdx.utils.Array;
import com.github.czyzby.kiwi.util.gdx.scene2d.Alignment;

import net.kommocgame.src.Game;
import net.kommocgame.src.control.InputHandler;
import net.kommocgame.src.control.KeyBinding;
public class GuiBase implements GuiHandler.ActorPressed {
	
	public Alignment ALIGNMENT;
	////public Group group = new Group();
	public Group group_stage = new Group();
	public Array<Actor> list_exceptions = new Array<Actor>();
	
	public Game game;
	
	public float debug_x = 0f;
	public float debug_y = 0f;
	
	public GuiBase(Game game) {
		this.game = game;
		//this.getStage().addActor(group_stage);
	}
	
	public void drawGui(SpriteBatch batch) {
		this.update(batch);
		////group.draw(batch, 1f);
		group_stage.draw(batch, 1f);
	}
	
	/** Set actor to center oh his texture(if he had). */
	public void setToCenter(Widget widget) {
		widget.setPosition(Gdx.graphics.getWidth() / 2 - widget.getWidth() / 2, Gdx.graphics.getHeight() / 2 - widget.getHeight() / 2);
	}
	
	public void translate(Widget widget, float x, float y) {
		widget.setPosition(widget.getX() + x, widget.getY() + y);
	}
	
	/** Return the obj that been at cursor. */
	public Actor getObjInCursor() {
		Actor group_actor = group_stage.hit(InputHandler.GUI_mouse_x, InputHandler.GUI_mouse_y, true);
		
		if(group_actor == null && list_exceptions != null && list_exceptions.size > 0) {
			Actor group_exception;
			
			for(int i = 0; i < list_exceptions.size; i++) {
				boolean touch = false;
				
				if(list_exceptions.get(i).getParent().isTouchable() == false) {
					touch = true;
					list_exceptions.get(i).getParent().setTouchable(Touchable.enabled);
					System.out.println("	list_exceptions.get(i).getParent(): " + list_exceptions.get(i).getParent());
				}
				
				group_exception = list_exceptions.get(i).getParent().hit(InputHandler.GUI_mouse_x, InputHandler.GUI_mouse_y, true);
					System.out.println("	ACTOR_EXEPTION: " + group_exception);
				
				if(touch)	// Need to return all to back.
					list_exceptions.get(i).getParent().setTouchable(Touchable.disabled);
				
				if(group_exception != null) {
					return group_exception;
				}
			}
		} else return group_actor;
		
		return null;
	}
	
	/** Check list exeptions. */ /*
	public Actor getExceptionGroup() {
		if(list_exceptions != null && list_exceptions.size > 0) {
			Actor object;
			
			for(int i = 0; i < list_exceptions.size; i++) {
				boolean touch = false;
				
				if(list_exceptions.get(i).getParent().isTouchable() == false) {
					touch = true;
					list_exceptions.get(i).getParent().setTouchable(Touchable.enabled);
					System.out.println("	ACTOR_EXEPTION: " +" TOUCHABLE");
				}
				
				object = list_exceptions.get(i).hit(InputHandler.GUI_mouse_x, InputHandler.GUI_mouse_y, true);
					System.out.println("	ACTOR_EXEPTION: " + object);
				
				if(touch)	// Need to return all to back.
					list_exceptions.get(i).getParent().setTouchable(Touchable.disabled);
				
				if(object != null) {
					//return object;
				}
			}
		}
	}*/
	
	/** Return the Actor(on group_stage) that has been pressed. */
	public void getActorPressedStage(Actor actor) { }
	
	/** Set position to left side. Stencil is a quad window. */
	public void setLeftSide(Widget widget, boolean setToStencil) {
		if(!setToStencil)
			widget.setPosition(0, Gdx.graphics.getHeight(), this.ALIGNMENT.TOPLEFT.get());
		else 
			widget.setPosition((Gdx.graphics.getWidth() - Gdx.graphics.getHeight()) / 2, Gdx.graphics.getHeight(), this.ALIGNMENT.TOPLEFT.get());
	}
	
	/** Set position to right side. Stencil is a quad window. */
	public void setRightSide(Widget widget, boolean setToStencil) {
		if(!setToStencil)
			widget.setPosition(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), this.ALIGNMENT.TOPRIGHT.get());
		else 
			widget.setPosition(Gdx.graphics.getWidth() - (Gdx.graphics.getWidth() - Gdx.graphics.getHeight()) / 2, Gdx.graphics.getHeight(), this.ALIGNMENT.TOPRIGHT.get());
	}
	
	public static float getRatio(float ratio) {
		return (float)Gdx.graphics.getHeight() * ratio;
	}
	
	public static float getRatioY(float ratio) {
		return (float)Gdx.graphics.getWidth() * ratio;
	}
	
	public float getBox2dRatioX(float ratio) {
		return game.mainCamera.viewportWidth * game.mainCamera.zoom * ratio;
	}
	
	public float getBox2dRatioY(float ratio) {
		return game.mainCamera.viewportHeight * game.mainCamera.zoom * ratio;
	}
	
	/** Return the stage for a group_stage. */
	public Stage getStage() {
		return game.guiManager.stage;
	}
	
	/** Return the GuiManager instance. */
	public GuiManager getGuiManager() {
		return game.guiManager;
	}
	
	/** Incapsulate actorTo into table with background and pad. */
	public static Table incapsulateIntoTable(Actor actorTo, Skin skin, float pad, String background) {
		return incapsulateIntoTable(actorTo, skin, pad, background, true);
	}
	
	/** Incapsulate actorTo into table with background and pad. */
	public static Table incapsulateIntoTable(Actor actorTo, Skin skin, float pad, String background, boolean grow) {
		Table table	= new Table(skin);
		if(grow)
			table.add(actorTo).grow().pad(pad);
		else table.add(actorTo).expand().pad(pad);
		
		if(!background.isEmpty())
			table.setBackground(background);
		
		return table;
	}
	
	/** Update tick of gui. */
	public void update(SpriteBatch batch) { }
	
	/** Uses for debug and offsets shape collisions stage scene. */
	public void stageDebug(ShapeRenderer renderer) {
		group_stage.drawDebug(renderer);
	}
	
	/** Need to set a position assets. */
	public void debug(float f) {
		if(KeyBinding.but_leftShift)
			f *= 3f;
		
		if(KeyBinding.but_forward) {
			debug_y += f;
		} else if(KeyBinding.but_back) {
			debug_y -= f;
		}
		
		if(KeyBinding.but_left) {
			debug_x -= f;
		} else if(KeyBinding.but_right) {
			debug_x += f;
		}
		
		System.out.println("debug_x: " + debug_x);
		System.out.println("debug_y: " + debug_y);
	}
	
	/** Remove this gui from the list. */
	public void removeGui() {
		if(getGuiManager().guiList.contains(this, false)) {
			boolean last = false;
			if(getGuiManager().guiList.peek() == this)
				last = true;
			
			getGuiManager().guiList.removeValue(this, false);
			getGuiManager().guiList.peek().group_stage.setTouchable(Touchable.enabled);
		}
		
		if(this.getStage().getActors().contains(group_stage, false)) {
			this.getStage().getActors().get(this.getStage().getActors().indexOf(group_stage, false)).remove();
		}
	}
	
	@Deprecated
	public void dispose() {
		//FIXME
	}
}
