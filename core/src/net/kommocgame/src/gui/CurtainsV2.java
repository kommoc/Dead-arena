package net.kommocgame.src.gui;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;
import com.github.czyzby.kiwi.util.gdx.scene2d.Alignment;

import net.kommocgame.src.Function;
import net.kommocgame.src.Game;

@Deprecated
public class CurtainsV2 extends Table {
	
	/***/ /// OLD 
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
	
	/***/ /// NEW
	private float min_width;
	private float min_height;
	
	public CurtainsV2(float x, float y, float width, float height) {
		this(x, y, x, y, width, height);
	}
	
	public CurtainsV2(float x, float y, float x1, float y1, float width, float height) {
		this.x = x;
		this.y = y;
		this.x1 = x1;
		this.y1 = y1;
		
		NinePatch ninePatch = Game.SCI_FI_ATLAS.createPatch("panel2");
		min_width = ninePatch.getLeftWidth() + ninePatch.getRightWidth();
		min_height = ninePatch.getBottomHeight() + ninePatch.getTopHeight();
		this.wight = width - min_width;
		this.height = height - min_height;
		
		this.setBackground(new NinePatchDrawable(ninePatch));
		this.setBounds(x, y, width, height);
	}
	
	@Override
	public void act(float deltaTime) {
		func.init();
		funcVert.init();
		funcHorz.init();
		funcVert_expand.init();
		funcHorz_expand.init();
		
		hasOpened = false;
		hasExpanded = false;
		
		if(state == STATE_OPENING) {
			this.setColor(1, 1, 1, func.getValue());
			
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
			
			wight_set = (int) (wight * funcHorz.getValue()) > min_width ? (int) (wight * funcHorz.getValue()) : (int) min_width;
			height_set = (int) (height * funcVert.getValue()) > min_height ? (int) (height * funcVert.getValue()) : (int) min_height;
			
			this.setSize(wight_set, height_set);
			this.setPosition(x_set, y_set, Alignment.CENTER.get());
			
			//this.setBounds(x_set - wight_set, y_set - height_set, wight_set * 2f, height_set * 2);
		} if(state == STATE_CLOSING) {
			this.setColor(1, 1, 1, func.getValue());
			
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
			
			wight_set = (int) (wight * funcHorz.getValue()) > min_width ? (int) (wight * funcHorz.getValue()) : (int) min_width;
			height_set = (int) (height * funcVert.getValue()) > min_height ? (int) (height * funcVert.getValue()) : (int) min_height;
			
			this.setSize(wight_set, height_set);
			this.setPosition(x_set, y_set, Alignment.CENTER.get());
			
			//this.setBounds(x_set - wight_set, y_set - height_set, wight_set * 2f, height_set * 2);
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
			
			wight_set = (int) (wight * funcHorz.getValue() + dif_x_expand * funcHorz_expand.getValue());
			height_set = (int) (height * funcVert.getValue() + dif_y_expand * funcVert_expand.getValue());
			
			this.setSize(wight_set, height_set);
			this.setPosition(x_set, y_set, Alignment.CENTER.get());
			
			//this.setBounds(x_set - wight_set, y_set - height_set, wight_set * 2f, height_set * 2);
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
			
			wight_set = (int) (wight * funcHorz.getValue() + dif_x_expand * funcHorz_expand.getValue());
			height_set = (int) (height * funcVert.getValue() + dif_y_expand * funcVert_expand.getValue());
			
			this.setSize(wight_set, height_set);
			this.setPosition(x_set, y_set, Alignment.CENTER.get());
			//this.setBounds(x_set - wight_set, y_set - height_set, wight_set * 2f, height_set * 2);
		}
		
		
		/**
		if(this.getWidth() != wight_set * 2f|| this.getHeight() < height_set * 2) {
			
			if(wight_set * 2f < min_width || height_set * 2 < min_height)
				this.setBounds(x_set - wight_set, y_set - height_set, min_width, min_height);
			else
				this.setBounds(x_set - wight_set, y_set - height_set, wight_set * 2f, height_set * 2);
		}
		/**
		if(img_background.getWidth() != (img_rightBot.getX(Alignment.RIGHT.get()) - img_leftBot.getX(Alignment.LEFT.get()) - 68)
				|| img_background.getHeight() != (img_rightTop.getY(Alignment.TOP.get()) - img_leftBot.getY(Alignment.BOTTOM.get()) - 68)
					) {
			reset();//FIXME
			//System.out.println("1: " + img_background.getWidth());
			//System.out.println("1XXX: " + (img_rightBot.getX(Alignment.RIGHT.get()) - img_leftBot.getX(Alignment.LEFT.get()) - 68));
			
			//System.out.println("2: " + img_background.getHeight());
			//System.out.println("2XXX: " + (img_rightTop.getY(Alignment.TOP.get()) - img_leftBot.getY(Alignment.BOTTOM.get()) - 68));
		}*/
	}
	
	public void _reset() {
		img_background.setSize(img_rightBot.getX(Alignment.RIGHT.get()) - img_leftBot.getX(Alignment.LEFT.get()) - 68, img_rightTop.getY(Alignment.TOP.get()) - img_leftBot.getY(Alignment.BOTTOM.get()) - 68);
		img_background.setPosition(x_set, y_set, Alignment.CENTER.get());
	}
	
	/** Set's the final expand. */
	public CurtainsV2 setExpand(float wight, float height) {
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

