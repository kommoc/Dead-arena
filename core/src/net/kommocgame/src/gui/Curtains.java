package net.kommocgame.src.gui;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.github.czyzby.kiwi.util.gdx.scene2d.Alignment;

import net.kommocgame.src.Function;
import net.kommocgame.src.Loader;
import net.kommocgame.src.Path;
import net.kommocgame.src.render.GameManager;

@Deprecated
public class Curtains {
	
	public Texture texture_cilia;
	public Texture texture_background;
	
	public TextureRegion reg_leftBot;
	public TextureRegion reg_rightTop;
	public TextureRegion reg_rightBot;
	
	public Image img_leftTop;
	public Image img_leftBot;
	public Image img_rightTop;
	public Image img_rightBot;
	public Image img_background;
	
	public float x;
	public float y;
	
	public float x1;
	public float y1;
	
	public float wight;
	public float height;
	
	public float wight_expand = 0;
	public float height_expand = 0;
	
	public int dif_x;
	public int dif_y;
	
	public int dif_x_expand;
	public int dif_y_expand;
	
	public int x_set;
	public int y_set;
	
	public int wight_set;
	public int height_set;
	
	public final int STATE_CLOSE = 0;
	public final int STATE_OPENING = 1;
	public final int STATE_OPEN = 2;
	public final int STATE_CLOSING = 3;
	public final int STATE_EXPANDING = 4;
	public final int STATE_EXPAND = 5;
	public final int STATE_NARROWING = 6;
	
	
	public int state = STATE_CLOSE;
	private boolean hasOpened = false;
	private boolean hasExpanded = false;
	
	public Function func = new Function(600, Interpolation.exp5Out);
	public Function funcVert = new Function(300, Interpolation.exp5Out);
	public Function funcHorz = new Function(600, Interpolation.exp5Out);
	
	public Function funcVert_expand = new Function(300, Interpolation.exp5Out);
	public Function funcHorz_expand = new Function(300, Interpolation.exp5Out);
	
	public Curtains(Group group, float x, float y, float wight, float height) {
		this(group, x, y, x, y, wight, height);
	}
	
	public Curtains(Group group, float x, float y, float x1, float y1, float wight, float height) {
		texture_cilia = Loader.guiMenuCilia("cilia_part.png");
		texture_background = Loader.guiMenuBackground("cilia_background_1.png");
		
		this.x = x;
		this.y = y;
		this.x1 = x1;
		this.y1 = y1;
		this.wight = wight - 128;
		this.height = height - 128;
		
		reg_leftBot = new TextureRegion(texture_cilia);
		reg_leftBot.setRegion(0, 1f, 1f, 0);
		
		reg_rightTop = new TextureRegion(texture_cilia);
		reg_rightTop.setRegion(1f, 0, 0, 1f);
		
		reg_rightBot = new TextureRegion(texture_cilia);
		reg_rightBot.setRegion(1f, 1f, 0, 0);
		
		img_leftTop = new Image(texture_cilia);
		img_leftBot = new Image(reg_leftBot);
		img_rightTop = new Image(reg_rightTop);
		img_rightBot = new Image(reg_rightBot);
		img_background = new Image(texture_background);
		
		img_leftTop.setScale(0.001f);
		img_leftBot.setScale(0.001f);
		img_rightTop.setScale(0.001f);
		img_rightBot.setScale(0.001f);
		img_background.setScale(0.001f);
		
		img_leftTop.setPosition(x, y, Alignment.BOTTOMRIGHT.get());
		img_leftBot.setPosition(x, y, Alignment.TOPRIGHT.get());
		img_rightTop.setPosition(x, y, Alignment.BOTTOMLEFT.get());
		img_rightBot.setPosition(x, y, Alignment.TOPLEFT.get());
		img_background.setPosition(x, y, Alignment.CENTER.get());
		
		group.addActor(img_background);
		group.addActor(img_leftTop);
		group.addActor(img_leftBot);
		group.addActor(img_rightTop);
		group.addActor(img_rightBot);
	}
	
	public void init() {
		func.init();
		funcVert.init();
		funcHorz.init();
		funcVert_expand.init();
		funcHorz_expand.init();
		
		hasOpened = false;
		hasExpanded = false;
		
		if(state == STATE_OPENING) {
			img_leftTop.setScale(func.getValue());
			img_leftBot.setScale(func.getValue());
			img_rightTop.setScale(func.getValue());
			img_rightBot.setScale(func.getValue());
			img_background.setScale(func.getValue());
			
			dif_x = (int) (x1 - x);
			dif_y = (int) (y1 - y);
			
			x_set = (int) (x + dif_x * func.getValue());
			y_set = (int) (y + dif_y * func.getValue());
			
			if(func.getValue() == 1.0f) {
				funcVert.start();
			} if(funcVert.getValue() == 1.0f) {
				funcHorz.start();
			} if(funcHorz.getValue() == 1.0f) {
				func.setBackward(true);
				funcVert.setBackward(true);
				funcHorz.setBackward(true);
				func.reload();
				funcVert.reload();
				funcHorz.reload();
				
				state = STATE_OPEN;
				hasOpened = true;
			}
			
			//img_background.setSize(img_rightBot.getX(Alignment.RIGHT.get()) - img_leftBot.getX(Alignment.LEFT.get()) - 68, img_rightTop.getY(Alignment.TOP.get()) - img_leftBot.getY(Alignment.BOTTOM.get()) - 68);
			
			wight_set = (int) (wight / 2 * funcHorz.getValue());
			height_set = (int) (height / 2 * funcVert.getValue());
			
			img_leftTop.setPosition(x_set - wight_set, y_set + height_set, Alignment.BOTTOMRIGHT.get());
			img_leftBot.setPosition(x_set - wight_set, y_set - height_set, Alignment.TOPRIGHT.get());
			img_rightTop.setPosition(x_set + wight_set, y_set + height_set, Alignment.BOTTOMLEFT.get());
			img_rightBot.setPosition(x_set + wight_set, y_set - height_set, Alignment.TOPLEFT.get());
			img_background.setPosition(x_set, y_set, Alignment.CENTER.get());
			
		} if(state == STATE_CLOSING) {
			img_leftTop.setScale(func.getValue());
			img_leftBot.setScale(func.getValue());
			img_rightTop.setScale(func.getValue());
			img_rightBot.setScale(func.getValue());
			img_background.setScale(func.getValue());
			
			dif_x = (int) (x1 - x);
			dif_y = (int) (y1 - y);
			
			x_set = (int) (x + dif_x * func.getValue());
			y_set = (int) (y + dif_y * func.getValue());
			
			if(funcHorz.getValue() == 0.0f) {
				funcVert.start();
			} if(funcVert.getValue() == 0.0f) {
				func.start();
			} if(func.getValue() == 0.0f) {
				func.setBackward(false);
				funcVert.setBackward(false);
				funcHorz.setBackward(false);
				func.reload();
				funcVert.reload();
				funcHorz.reload();
				
				state = STATE_CLOSE;
			}
			
			//img_background.setSize(img_rightBot.getX(Alignment.RIGHT.get()) - img_leftBot.getX(Alignment.LEFT.get()) - 68, img_rightTop.getY(Alignment.TOP.get()) - img_leftBot.getY(Alignment.BOTTOM.get()) - 68);
			
			wight_set = (int) (wight / 2 * funcHorz.getValue());
			height_set = (int) (height / 2 * funcVert.getValue());
			
			img_leftTop.setPosition(x_set - wight_set, y_set + height_set, Alignment.BOTTOMRIGHT.get());
			img_leftBot.setPosition(x_set - wight_set, y_set - height_set, Alignment.TOPRIGHT.get());
			img_rightTop.setPosition(x_set + wight_set, y_set + height_set, Alignment.BOTTOMLEFT.get());
			img_rightBot.setPosition(x_set + wight_set, y_set - height_set, Alignment.TOPLEFT.get());
			img_background.setPosition(x_set, y_set, Alignment.CENTER.get());
		} if(state == STATE_EXPANDING) {
			dif_x_expand = (int) (wight_expand - wight);
			dif_y_expand = (int) (height_expand - height);
			
			if(funcVert_expand.getValue() == 1f) {
				funcHorz_expand.start();
			} if(funcHorz_expand.getValue() == 1f) {
				funcHorz_expand.setBackward(true);
				funcVert_expand.setBackward(true);
				funcHorz_expand.reload();
				funcVert_expand.reload();
				
				state = STATE_EXPAND;
				hasExpanded = true;
			}
			
			//img_background.setSize(img_rightBot.getX(Alignment.RIGHT.get()) - img_leftBot.getX(Alignment.LEFT.get()) - 68, img_rightTop.getY(Alignment.TOP.get()) - img_leftBot.getY(Alignment.BOTTOM.get()) - 68);
			
			wight_set = (int) (wight / 2 * funcHorz.getValue() + dif_x_expand / 2 * funcHorz_expand.getValue());
			height_set = (int) (height / 2 * funcVert.getValue() + dif_y_expand / 2 * funcVert_expand.getValue());
			
			img_leftTop.setPosition(x_set - wight_set, y_set + height_set, Alignment.BOTTOMRIGHT.get());
			img_leftBot.setPosition(x_set - wight_set, y_set - height_set, Alignment.TOPRIGHT.get());
			img_rightTop.setPosition(x_set + wight_set, y_set + height_set, Alignment.BOTTOMLEFT.get());
			img_rightBot.setPosition(x_set + wight_set, y_set - height_set, Alignment.TOPLEFT.get());
			img_background.setPosition(x_set, y_set, Alignment.CENTER.get());
			
		} if(state == STATE_NARROWING) {
			dif_x_expand = (int) (wight_expand - wight);
			dif_y_expand = (int) (height_expand - height);
			
			if(funcHorz_expand.getValue() == 0f) {
				funcVert_expand.start();
			} if(funcVert_expand.getValue() == 0f) {
				funcHorz_expand.setBackward(false);
				funcVert_expand.setBackward(false);
				funcHorz_expand.reload();
				funcVert_expand.reload();
				
				state = STATE_OPEN;
			} 
			
			//System.out.println("funcVert_expand: " + funcVert_expand.getValue());
			//System.out.println("funcHorz_expand: " + funcHorz_expand.getValue());
			
			//img_background.setSize(img_rightBot.getX(Alignment.RIGHT.get()) - img_leftBot.getX(Alignment.LEFT.get()) - 68, img_rightTop.getY(Alignment.TOP.get()) - img_leftBot.getY(Alignment.BOTTOM.get()) - 68);
			
			wight_set = (int) (wight / 2f * funcHorz.getValue() + dif_x_expand / 2f * funcHorz_expand.getValue());
			height_set = (int) (height / 2f * funcVert.getValue() + dif_y_expand / 2f * funcVert_expand.getValue());
			
			img_leftTop.setPosition(x_set - wight_set, y_set + height_set, Alignment.BOTTOMRIGHT.get());
			img_leftBot.setPosition(x_set - wight_set, y_set - height_set, Alignment.TOPRIGHT.get());
			img_rightTop.setPosition(x_set + wight_set, y_set + height_set, Alignment.BOTTOMLEFT.get());
			img_rightBot.setPosition(x_set + wight_set, y_set - height_set, Alignment.TOPLEFT.get());
			img_background.setPosition(x_set, y_set, Alignment.CENTER.get());
		}
		
		if(img_background.getWidth() != (img_rightBot.getX(Alignment.RIGHT.get()) - img_leftBot.getX(Alignment.LEFT.get()) - 68)
				|| img_background.getHeight() != (img_rightTop.getY(Alignment.TOP.get()) - img_leftBot.getY(Alignment.BOTTOM.get()) - 68)
					) {
			reset();//FIXME
			//System.out.println("1: " + img_background.getWidth());
			//System.out.println("1XXX: " + (img_rightBot.getX(Alignment.RIGHT.get()) - img_leftBot.getX(Alignment.LEFT.get()) - 68));
			
			//System.out.println("2: " + img_background.getHeight());
			//System.out.println("2XXX: " + (img_rightTop.getY(Alignment.TOP.get()) - img_leftBot.getY(Alignment.BOTTOM.get()) - 68));
		}
	}
	
	public void reset() {
		img_background.setSize(img_rightBot.getX(Alignment.RIGHT.get()) - img_leftBot.getX(Alignment.LEFT.get()) - 68, img_rightTop.getY(Alignment.TOP.get()) - img_leftBot.getY(Alignment.BOTTOM.get()) - 68);
		img_background.setPosition(x_set, y_set, Alignment.CENTER.get());
	}
	
	/** Set's the final expand. */
	public Curtains setExpand(float wight, float height) {
		wight_expand = wight;
		height_expand = height;
		
		funcHorz_expand.setBackward(false);
		funcHorz_expand.reload();
		
		funcVert_expand.setBackward(false);
		funcVert_expand.reload();
		
		return this;
	}
	
	public boolean hasOpen() {
		return hasOpened;
	}
	
	public boolean hasExpanded() {
		return hasExpanded;
	}
	
	public void open() {
		if(state == STATE_CLOSE) {
			func.start();
			state = STATE_OPENING;
		}
	}
	
	public void close() {
		if(state == STATE_OPEN) {
			funcHorz.start();
			state = STATE_CLOSING;
		}
	}
	
	/** Expanding curtains if resolution was changed. */
	public void expand() {
		if(state == STATE_OPEN && (wight_expand != 0 || height_expand != 0)) {
			state = STATE_EXPANDING;
			funcVert_expand.start();
		}
	}
	
	public void narrow() {
		if(state == STATE_EXPAND && (wight_expand != 0 || height_expand != 0)) {
			state = STATE_NARROWING;
			funcHorz_expand.start();
		}
	}
}
