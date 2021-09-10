package net.kommocgame.src.gui;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.utils.Array;
import com.github.czyzby.kiwi.util.gdx.scene2d.Alignment;

import net.kommocgame.src.Game;
import net.kommocgame.src.Loader;
import net.kommocgame.src.Path;
import net.kommocgame.src.render.GameManager;

@Deprecated
public class Button extends Actor {
	
	public Texture texture;
	public TextureRegion reg_butBegin;
	public TextureRegion reg_butMain;
	public TextureRegion reg_butEnd;
	
	public int height = 72;
	
	public float ratio_x = 0.75f;
	public float ratio_y = 0.125f;
	
	public float scale;
	public float local_x;
	public float local_y;
	
	public boolean state_backward = false;
	
	public Array<Image> img_begin = new Array<Image>();
	public Array<Image> img_main = new Array<Image>();
	public Array<Image> img_end = new Array<Image>();
	
	public Array<Label> labels = new Array<Label>();
	
	/** Test button. 
	 * @param lenght - in integer until 432px
	 */
	public Button(Group group, int count, float length, int x, int y, String... text) {
		
		texture = Loader.guiMenuBut("button_1.png");
		
		reg_butBegin = new TextureRegion(texture);
		reg_butBegin.setRegion(0, 0, 16f/512f, 1);
		
		reg_butMain = new TextureRegion(texture);
		reg_butMain.setRegion(16f/512f, 0, 448f/512f, 1);
		
		reg_butEnd = new TextureRegion(texture);
		reg_butEnd.setRegion(448f/512f, 0, 1, 1);
		
		for(int i = 0; i < count; i++) {
			img_begin.add(new Image(reg_butBegin));
			img_main.add(new Image(reg_butMain));
			img_end.add(new Image(reg_butEnd));
			
			labels.add(new Label(text[i], Game.LABEL_STYLE_CONSOLE_72));
			labels.get(i).setTouchable(Touchable.disabled);
			
			scale = GameManager.instance.SCALE;
			
			System.out.println("scaleY_ " + scale);
			
			img_begin.get(i).setSize(img_begin.get(i).getWidth() * 1.5f * scale, img_begin.get(i).getHeight() * 1.5f * scale);
			img_main.get(i).setSize(img_main.get(i).getWidth() * 1.5f * scale, img_main.get(i).getHeight() * 1.5f * scale);
			img_end.get(i).setSize(img_end.get(i).getHeight() * 1.5f * scale, img_end.get(i).getHeight() * 1.5f * scale);
		}
		
		height *= 1.5f  * scale;
		//System.out.println("height:" + height);
		//check resize method(); FIXME
		
		for(int i = 0; i < count; i++) {
			group.addActor(img_begin.get(i));
			group.addActor(img_main.get(i));
			group.addActor(img_end.get(i));
			group.addActor(labels.get(i));
		}
		
		float length_main = length - 80;
		
		for(int i = 0; i < count; i++) {
			float c = (float)(count - i) / MathUtils.cosDeg(15f);	//hypotenuse
			float a = c * MathUtils.sinDeg(15f) * height;				//added length
			
			float c1 = (float)i / MathUtils.cosDeg(15f);
			float a1 = c1 * MathUtils.sinDeg(15f) * (float)height;
			
			img_begin.get(i).setPosition(x, y - i * height, Alignment.BOTTOMLEFT.get());
			img_main.get(i).setPosition(img_begin.get(i).getX(Alignment.RIGHT.get()), y - i * height, Alignment.BOTTOMLEFT.get());
			img_main.get(i).setSize((length - 80 * 1.5f  * scale) - a1, img_main.get(i).getHeight());
			img_end.get(i).setPosition(img_main.get(i).getX(Alignment.RIGHT.get()), y - i * height, Alignment.BOTTOMLEFT.get());
			
			labels.get(i).setSize(labels.get(i).getWidth() * 0.6f * scale, labels.get(i).getHeight() * 0.6f * scale);
			labels.get(i).setPosition(img_main.get(i).getX(Alignment.LEFT.get()), img_main.get(i).getY(Alignment.BOTTOMLEFT.get()) + img_main.get(i).getHeight() / 4f);
			labels.get(i).setFontScale(0.8f * scale);
		}
		
		setX(x);
		setY(y);
		
		this.addName();
	}
	
	public Button backward() {
		float[][] position = new float[img_main.size][2]; //x - 0, y - 1
		
		for(int i = 0; i < img_main.size; i++) {
			position[i][0] = img_main.get(i).getX(Alignment.LEFT.get());
			position[i][1] = img_main.get(i).getY(Alignment.TOPLEFT.get());
		}
		
		for(int i = 0; i < img_main.size; i++) {
			img_main.get(i).setPosition(position[img_main.size - i - 1][0], position[img_main.size - i - 1][1], Alignment.TOPRIGHT.get());
			img_end.get(i).setRotation(180f);
			img_end.get(i).setPosition(img_main.get(i).getX(Alignment.LEFT.get()), img_main.get(i).getY(Alignment.TOP.get()), Alignment.BOTTOMLEFT.get());
			
			img_begin.get(i).setOrigin(Alignment.CENTER.get());
			img_begin.get(i).setRotation(180f);
			img_begin.get(i).setPosition(img_main.get(i).getX(Alignment.RIGHT.get()), img_main.get(i).getY(Alignment.TOP.get()), Alignment.TOPLEFT.get());
			
			labels.get(i).setPosition(img_end.get(i).getX(Alignment.LEFT.get()) - img_end.get(i).getWidth() / 2, img_main.get(i).getY(Alignment.BOTTOMLEFT.get()) + img_main.get(i).getHeight() / 4f);
		}
		
		state_backward = true;
		return this;
	}
	
	/** Return event of pressed but.
	 * @param actor - pressed element.
	 * @param id - number require button. */
	public boolean pressedButton(Actor actor, int id) {
		if(actor instanceof Image) {
			if(!state_backward) {
				if(img_begin.get(id).equals(actor) || img_main.get(id).equals(actor) || img_end.get(id).equals(actor)) {
					return true;
				}
			} else {
				if(img_begin.get(img_begin.size - 1 - id).equals(actor) || img_main.get(img_main.size - 1 - id).equals(actor) || img_end.get(img_end.size - 1 - id).equals(actor)) {
					return true;
				}
			}
		}
		
		return false;
	}
	
	public void setPosition(float x, float y) {
		super.setPosition(x, y);
		
		if(!state_backward) {
			local_x = x - img_begin.get(0).getX();
			local_y = y - img_begin.get(0).getY();
			
			img_begin.get(0).setPosition(x, y, Alignment.BOTTOMLEFT.get());
			img_main.get(0).setPosition(img_begin.get(0).getX(Alignment.RIGHT.get()), y, Alignment.BOTTOMLEFT.get());
			img_end.get(0).setPosition(img_main.get(0).getX(Alignment.RIGHT.get()), y, Alignment.BOTTOMLEFT.get());
			labels.get(0).setPosition(img_main.get(0).getX(Alignment.LEFT.get()), img_main.get(0).getY(Alignment.BOTTOMLEFT.get()) + img_main.get(0).getHeight() / 4f);
	
			
			for(int i = 1; i < img_begin.size; i++) {
				img_begin.get(i).setPosition(img_begin.get(i).getX() + local_x, img_begin.get(i).getY() + local_y, Alignment.BOTTOMLEFT.get());
				img_main.get(i).setPosition(img_begin.get(i).getX(Alignment.RIGHT.get()), img_begin.get(i).getY(), Alignment.BOTTOMLEFT.get());
				img_end.get(i).setPosition(img_main.get(i).getX(Alignment.RIGHT.get()), img_main.get(i).getY(), Alignment.BOTTOMLEFT.get());
				
				labels.get(i).setPosition(img_main.get(i).getX(Alignment.LEFT.get()), img_main.get(i).getY(Alignment.BOTTOMLEFT.get()) + img_main.get(i).getHeight() / 4f);
			}
		} else {
			local_x = x - img_begin.peek().getX(Alignment.TOPRIGHT.get());
			local_y = y - img_begin.peek().getY(Alignment.TOPRIGHT.get());
			
			img_main.peek().setPosition(x, y, Alignment.TOPRIGHT.get());
			img_end.peek().setPosition(img_main.peek().getX(Alignment.LEFT.get()), img_main.peek().getY(Alignment.TOP.get()), Alignment.BOTTOMLEFT.get());
			img_begin.peek().setPosition(img_main.peek().getX(Alignment.RIGHT.get()), img_main.peek().getY(Alignment.TOP.get()), Alignment.TOPLEFT.get());
			labels.peek().setPosition(img_end.peek().getX(Alignment.LEFT.get()) - img_end.peek().getWidth() / 2, img_main.peek().getY(Alignment.BOTTOMLEFT.get()) + img_main.peek().getHeight() / 4f);
			
			for(int i = img_begin.size - 2; i >= 0; i--) {
				img_main.get(i).setPosition(x, img_main.get(i).getY() + local_y, Alignment.BOTTOMRIGHT.get());
				img_end.get(i).setPosition(img_main.get(i).getX(Alignment.LEFT.get()), img_main.get(i).getY(Alignment.TOP.get()), Alignment.BOTTOMLEFT.get());
				img_begin.get(i).setPosition(img_main.get(i).getX(Alignment.RIGHT.get()), img_main.get(i).getY(Alignment.TOP.get()), Alignment.TOPLEFT.get());
				
				labels.get(i).setPosition(img_end.get(i).getX(Alignment.LEFT.get()) - img_end.get(i).getWidth() / 2, img_main.get(i).getY(Alignment.BOTTOMLEFT.get()) + img_main.get(i).getHeight() / 4f);
			}
		}
	}
	
	@Override
	public void setColor(float r, float g, float b, float alpha) {
		for(int i = 0; i < img_begin.size; i++) {
			img_begin.get(i).setColor(r, g, b, alpha);
			img_main.get(i).setColor(r, g, b, alpha);
			img_end.get(i).setColor(r, g, b, alpha);
			
			labels.get(i).setColor(r, g, b, alpha);
		}
	}
	
	@Override
	public void setVisible(boolean par1) {
		super.setVisible(par1);
		for(int i = 0; i < img_begin.size; i++) {
			img_begin.get(i).setVisible(par1);
			img_main.get(i).setVisible(par1);
			img_end.get(i).setVisible(par1);
			
			labels.get(i).setVisible(par1);
		}
	}
	
	public void setLabelColor(float r, float g, float b, float alpha) {
		for(int i = 0; i < img_begin.size; i++) {
			labels.get(i).setColor(r, g, b, alpha);
		}
	}
	
	public void addName() {
		for(int i = 0; i < img_begin.size; i++) {
			img_begin.get(i).setName("but_img_begin[" + i + "]" + " :: WORD :: " + labels.get(i).getText());
			img_main.get(i).setName("but_img_main[" + i + "]" + " :: WORD :: " + labels.get(i).getText());
			img_end.get(i).setName("but_img_end[" + i + "]" + " :: WORD :: " + labels.get(i).getText());
		}
	}
	
	/** Set's the text for current button. */
	public void setText(int button, String text) {
		this.labels.get(button).setText(text);
	}
	
	//TODO resize method() under resolution 
}
