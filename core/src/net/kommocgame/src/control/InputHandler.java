package net.kommocgame.src.control;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

import net.kommocgame.src.Game;
import net.kommocgame.src.VecUtils;
import net.kommocgame.src.gui.GuiHandler;

public class InputHandler implements InputProcessor {
	
	public static int GLOBAL_mouse_x;
	public static int GLOBAL_mouse_y;
	
	public static int GUI_mouse_x;
	public static int GUI_mouse_y;
	
	public static Vector2 BOX2D_mouse_vec = new Vector2();
	public static float BOX2D_mouse_x = 0;
	public static float BOX2D_mouse_y = 0;
	
	public static float WORLD_mouse_x;
	public static float WORLD_mouse_y;
	
	public static boolean mouse_pressed = false;
	//public static IMouse event;	DELETE
	
	public static Array<IMouse> list_event = new Array<IMouse>();
	
	private Game game;
	private float x_press, y_press;
	
	public InputHandler(Game game) {
		this.game = game;
	}
	
	@Override
	public boolean keyDown(int keycode) {
		//System.out.println("keyDown:\t" + game.guiManager.stage.keyDown(keycode));
		
		game.guiManager.stage.keyDown(keycode);
		//System.out.println("KEYCODE DOWN: " + Keys.toString(keycode));
		return true;
	}

	@Override
	public boolean keyUp(int keycode) {
		//System.out.println("keyUp:\t" + game.guiManager.stage.keyUp(keycode));
		
		game.guiManager.stage.keyUp(keycode);
		//System.out.println("KEYCODE UP: " + Keys.toString(keycode));
		return true;
	}

	@Override
	public boolean keyTyped(char character) {
		//System.out.println("keyTyped:\t" + game.guiManager.stage.keyTyped(character));
		
		game.guiManager.stage.keyTyped(character);
		//System.out.println("KEYCODE TYPED: " + Keys.valueOf(String.valueOf(character)));
		return true;
	}

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		//System.out.println("touchDown:\t" + game.guiManager.stage.touchDown(screenX, screenY, pointer, button));
		x_press = screenX;
		y_press = Gdx.graphics.getHeight() - screenY;
		game.guiManager.stage.touchDown(screenX, screenY, pointer, button);
		mouse_pressed = true;
		KeyBinding.but_pressed_event = true;
		
		if(list_event.size > 0)
			for(IMouse event : list_event) {
				event.touchDown(x_press, y_press);
			}
		
		//if(event != null)						DELETE
		//	event.touchDown(x_press, y_press);	DELETE
		
		return true;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		//System.out.println("touchUp:\t" + game.guiManager.stage.touchUp(screenX, screenY, pointer, button));
		
		game.guiManager.stage.touchUp(screenX, screenY, pointer, button);
		GuiHandler.ActionListener event_click = game.guiManager;
		event_click.mouseClicked();
		mouse_pressed = false;
		
		if(list_event.size > 0)
			for(IMouse event : list_event) {
				event.touchUp();
			}
		
		//if(InputHandler.event != null)	DELETE
		//	InputHandler.event.touchUp();	DELETE
		return true;
	}

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		//System.out.println("touchDragged:\t" + game.guiManager.stage.touchDragged(screenX, screenY, pointer));
		
		game.guiManager.stage.touchDragged(screenX, screenY, pointer);
		
		GLOBAL_mouse_x = screenX;
		GLOBAL_mouse_y = screenY;
		GUI_mouse_x = screenX;
		GUI_mouse_y = Gdx.graphics.getHeight() - screenY;
		
		if(list_event.size > 0)
			for(IMouse event : list_event) {
				event.touchDragged(x_press - GUI_mouse_x, y_press - GUI_mouse_y);
			}
		
		//if(event != null)														DELETE
		//	event.touchDragged(x_press - GUI_mouse_x, y_press - GUI_mouse_y);	DELETE
		return true;
	}

	@Override
	public boolean mouseMoved(int screenX, int screenY) {
		//System.out.println("mouseMoved:\t" + game.guiManager.stage.mouseMoved(screenX, screenY));
		
		//if(list_event.size > 0)																		// ONLY IN DEBUG
		//	System.out.println("COUNT OF IMOUSE_LISTENERS: " + (list_event.get(0) != null));			// ONLY IN DEBUG
		game.guiManager.stage.mouseMoved(screenX, screenY);
		
		GLOBAL_mouse_x = screenX;
		GLOBAL_mouse_y = screenY;
		GUI_mouse_x = screenX;
		GUI_mouse_y = Gdx.graphics.getHeight() - screenY;

		return true;
	}

	@Override
	public boolean scrolled(float motionX, float motionY) {


		int amount = (int) (motionX + motionY);
		System.out.println("scrolled:\t" + amount);
		
		if(list_event.size > 0)
			for(IMouse event : list_event) {
				event.scrolled(amount);
			}
		
		//if(event != null)				DELETE
		//	event.scrolled(amount);		DELETE
		
		game.guiManager.stage.scrolled(motionX, motionY);
		return true;
	}
	
	public void guiEvent() {
		if(KeyBinding.but_escape) {
			GuiHandler.ActionListener handler = this.game.guiManager;
			handler.escReleased();
		} else if(KeyBinding.but_console) {
			GuiHandler.ActionListener handler = this.game.guiManager;
			handler.consoleReleased();
		}
	}
	
	public void mouseUpdating() {
		if(game.world.level != null) {
			BOX2D_mouse_x = (float)GUI_mouse_x * game.mainCamera.zoom + game.mainCamera.position.x
					- Gdx.graphics.getWidth() / 2 * game.mainCamera.zoom;
			BOX2D_mouse_y = (float)GUI_mouse_y * game.mainCamera.zoom + game.mainCamera.position.y
					- Gdx.graphics.getHeight() / 2 * game.mainCamera.zoom;
			
			BOX2D_mouse_vec.set(BOX2D_mouse_x, BOX2D_mouse_y);
		}
	}
	
	public static int getNodeX() {
		int x;
		float scale = 1f;
		
		if(Game.instance.world.game.world.getLevel() != null && Game.instance.world.game.world.getLevel().getGridNodes() != null)
			scale = Game.instance.world.game.world.getLevel().getGridNodes().getScale();
		
		x = (int) (InputHandler.BOX2D_mouse_x / scale);
		
		if(InputHandler.BOX2D_mouse_x < 0) {
			x = (int) Math.floor(InputHandler.BOX2D_mouse_x / scale);
		}if(InputHandler.BOX2D_mouse_x < 0 && InputHandler.BOX2D_mouse_y < 0) {
			x = (int) Math.floor(InputHandler.BOX2D_mouse_x / scale);
		}
		
		return (int) (x * scale);
	}
	
	public static int getNodeY() {
		int y;
		float scale = 1f;
		
		if(Game.instance.world.game.world.getLevel() != null && Game.instance.world.game.world.getLevel().getGridNodes() != null)
			scale = Game.instance.world.game.world.getLevel().getGridNodes().getScale();
		
		y = (int) (InputHandler.BOX2D_mouse_y / scale);
		
		if(InputHandler.BOX2D_mouse_y < 0) {
			y = (int) Math.floor(InputHandler.BOX2D_mouse_y / scale);
		} if(InputHandler.BOX2D_mouse_x < 0 && InputHandler.BOX2D_mouse_y < 0) {
			y = (int) Math.floor(InputHandler.BOX2D_mouse_y / scale);
		}
		
		return (int) (y * scale);
	}
	
	@Deprecated
	public static float getMouseRot() {
		float x = InputHandler.GLOBAL_mouse_x - Gdx.graphics.getWidth() / 2;
		float y = Gdx.graphics.getHeight() - InputHandler.GLOBAL_mouse_y - Gdx.graphics.getHeight() / 2;
		
		InputHandler.WORLD_mouse_x = x;
		InputHandler.WORLD_mouse_y = y;
		
		return VecUtils.getAngleAtPoint(x, y);
	}
	
	public static void registerIMouse(IMouse imouse) {
		//event = imouse; 	DELETE
		list_event.add(imouse);
	}
	
	public static boolean deleteIMouse(IMouse imouse) {
		if(list_event.contains(imouse, false)) {
			list_event.removeValue(imouse, false);
			return true;
		}
		
		return false;
	}
}
