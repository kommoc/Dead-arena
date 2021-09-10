package net.kommocgame.src.control;

import com.badlogic.gdx.Application.ApplicationType;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.math.Vector2;

import net.kommocgame.src.Game;
import net.kommocgame.src.entity.EntityLiving;

public class KeyBinding {
	
	public static boolean but_forward = false;
	public static boolean but_back = false;
	public static boolean but_right = false;
	public static boolean but_left = false;
	public static boolean but_leftShift = false;
	
	public static boolean but_action = false;
	
	public static boolean but_escape = false;
	public static boolean but_console = false;
	
	private static float gamepadMoveKnobX = 0;
	private static float gamepadMoveKnobY = 0;
	
	private static float gamepadLookKnobX = 0;
	private static float gamepadLookKnobY = 0;
	
	/***********************************************************/
	
	static Vector2 vector_1 = new Vector2(0, 1);
	static Vector2 vector_2 = new Vector2();
	
	/** Need for once click event. */
	protected static boolean but_pressed_event = false;
	/** Editor button. ACTION: apply action. */
	public static boolean but_e_event_apply = false;
	/** Editor button. ACTION: cancel action. */
	public static boolean but_e_event_cancel = false;
	
	/** Editor button. ACTION: choose the obj. */
	public static boolean but_e_chooseObj = false;
	/** Editor button. ACTION: translate choosen obj. */
	public static boolean but_e_co_translate = false;
	/** Editor button. ACTION: rotate choosen obj. */
	public static boolean but_e_co_rotate = false;
	/** Editor button. ACTION: scale choosen obj. */
	public static boolean but_e_co_scale = false;
	/** Editor button. ACTION: delete choosen obj. */
	public static boolean but_e_co_delete = false;
	
	/** Editor button. ACTION: copy instance of EObject. */
	public static boolean but_e_copy = false;
	/** Editor button. ACTION: paste copy EObject. */
	public static boolean but_e_paste = false;
	
	/** Editor button. ACTION: translate choosen obj by step. */
	public static boolean but_e_co_trns_step = false;
	/** Editor button. ACTION: translate choosen obj smoothed. */
	public static boolean but_e_co_trns_smooth = false;
	/** Editor button. ACTION: translate choosen obj by cont. */
	public static boolean but_e_co_trns_cont = false;
	
	public static final int mouse_but_leftClick = 0;
	public static final int mouse_but_rightClick = 1;
	public static final int mouse_but_midClick = 2;
	public static final int mouse_but_3 = 3;
	public static final int mouse_but_4 = 4;
	
	private static float forceX = 0;
	private static float forceY = 0;
	private Game game;
	
	public KeyBinding(Game game) {
		this.game = game;
	}
	
	public static void processing() {
		but_forward 	= getKey(Keys.W);
		but_back 		= getKey(Keys.S);
		but_right 		= getKey(Keys.D);
		but_left 		= getKey(Keys.A);
		but_action 		= getKey(Keys.E);
		but_leftShift 	= getKey(Keys.SHIFT_LEFT);
		
		but_escape = getKeyRealised(Keys.ESCAPE);
		but_console = getKeyRealised(Keys.F1);
		
		if(Gdx.app.getType() == ApplicationType.Desktop) {
			keyboard();
		}
		
		but_e_chooseObj 	= getMouse(mouse_but_rightClick);
		
		but_e_event_apply	= getMouse(mouse_but_leftClick);
		but_e_event_cancel	= getMouse(mouse_but_rightClick);
		
		but_e_co_translate 	= getKey(Keys.G);
		but_e_co_rotate 	= getKey(Keys.R);
		but_e_co_scale 		= getKey(Keys.S);
		but_e_co_delete 	= getKey(Keys.X);
		
		but_e_copy			= getKeyRealised(Keys.C);
		but_e_paste			= getKeyRealised(Keys.V);
		
		but_e_co_trns_smooth 	= getKey(Keys.SHIFT_LEFT);
		but_e_co_trns_cont 		= getKey(Keys.CONTROL_LEFT);
		but_e_co_trns_step		= getKey(Keys.ALT_LEFT);
		
		but_pressed_event = false;
	}
	
	public static void keyboard() {
		if(but_forward)
			forceY = 1f;
		else if(but_back)
			forceY = -1;
		else forceY = 0;
		
		if(but_right)
			forceX = 1f;
		else if(but_left)
			forceX = -1;
		else forceX = 0;
		
		if(Gdx.app.getType() == ApplicationType.Desktop) {
			setMoveKnob(forceX, forceY, 0, 0);
		}
	}
	
	/** Get key mouse pressed. */
	public static boolean getMouse(int key) {
		return Gdx.input.isButtonPressed(key) && but_pressed_event;
	}
	
	public static boolean getKey(int key) {
		return Gdx.input.isKeyPressed(key);
	}
	
	public static boolean getKeyRealised(int key) {
		return Gdx.input.isKeyJustPressed(key);
	}
	
	public static void setMoveKnob(float par1, float par2, float par3, float par4) {
		gamepadMoveKnobX = par1;
		gamepadMoveKnobY = par2;
		gamepadLookKnobX = par3;
		gamepadLookKnobY = par4;
		
		setMoveForce(par1, par2);
		setLookForce(par3, par4);
	}
	
	public static void setMoveForce(float par1, float par2) {
		try {
			if(!((EntityLiving) Game.getPlayer()).isDead())
				Game.getPlayer().toMove(par1, par2);
		} catch(NullPointerException e) { }
	}
	
	public static void setLookForce(float par1, float par2) {
		try {
			if(!((EntityLiving) Game.getPlayer()).isDead())
				Game.getPlayer().toLook(par1, par2);
		} catch(NullPointerException e) { }
	}
	
	public static float getMoveKnobX() {
		return gamepadMoveKnobX;
	}
	
	public static float getMoveKnobY() {
		return gamepadMoveKnobY;
	}
	
	public static float getLookKnobX() {
		return gamepadLookKnobX;
	}
	
	public static float getLookKnobY() {
		return gamepadLookKnobY;
	}
	
	/** Return angle force in rad. */
	public static float getLookAngle(float x, float y) {
		//Vector2 vec = new Vector2(0, 1);
		//vec.angleRad(new Vector2(par1, par2));
		
		//return vec.angleRad(new Vector2(par1, par2));
		vector_2.set(x, y);
		return vector_1.angleRad(vector_2);
	}
}
