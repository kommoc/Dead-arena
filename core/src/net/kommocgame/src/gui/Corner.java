package net.kommocgame.src.gui;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.github.czyzby.kiwi.util.gdx.scene2d.Alignment;

import net.kommocgame.src.Game;
import net.kommocgame.src.Loader;
import net.kommocgame.src.render.GameManager;

@Deprecated
public class Corner extends Group {
	
	private Actor aligned;
	
	private Texture texture_corner = Loader.guiMenuCilia("corner_part.png");
	
	private TextureRegion reg_leftBot;
	private TextureRegion reg_rightTop;
	private TextureRegion reg_rightBot;
	
	private Image img_leftTop;
	private Image img_leftBot;
	private Image img_rightTop;
	private Image img_rightBot;
		
	/** Set's the cor */
	public Corner(Actor actor) {
		aligned = actor;
		reg_leftBot = new TextureRegion(texture_corner);
		reg_leftBot.setRegion(0, 1f, 1f, 0);
		
		reg_rightTop = new TextureRegion(texture_corner);
		reg_rightTop.setRegion(1f, 0, 0, 1f);
		
		reg_rightBot = new TextureRegion(texture_corner);
		reg_rightBot.setRegion(1f, 1f, 0, 0);
		
		img_leftTop = new Image(texture_corner);
		img_leftBot = new Image(reg_leftBot);
		img_rightTop = new Image(reg_rightTop);
		img_rightBot = new Image(reg_rightBot);
		
		float scale_x = 2f;
		float scale_y = 2f;
		
		img_leftTop.setSize(img_leftTop.getWidth() * GameManager.instance.SCALE * scale_x, img_leftTop.getHeight() * GameManager.instance.SCALE * scale_y);
		img_leftBot.setSize(img_leftBot.getWidth() * GameManager.instance.SCALE * scale_x, img_leftBot.getHeight() * GameManager.instance.SCALE * scale_y);
		img_rightTop.setSize(img_rightTop.getWidth() * GameManager.instance.SCALE * scale_x, img_rightTop.getHeight() * GameManager.instance.SCALE * scale_y);
		img_rightBot.setSize(img_rightBot.getWidth() * GameManager.instance.SCALE * scale_x, img_rightBot.getHeight() * GameManager.instance.SCALE * scale_y);
		
		//System.out.println("SCALE: " + GameManager.instance.SCALE);
		System.out.println(Game.SCI_FI_ATLAS.getRegions().get(2));
		
		img_leftTop.setTouchable(Touchable.disabled);
		img_leftBot.setTouchable(Touchable.disabled);
		img_rightTop.setTouchable(Touchable.disabled);
		img_rightBot.setTouchable(Touchable.disabled);
		
		this.addActor(img_leftTop);
		this.addActor(img_leftBot);
		this.addActor(img_rightTop);
		this.addActor(img_rightBot);
	}
	
	@Override
	public void act(float deltaTime) {
		if(aligned != null) {
			img_leftTop.setPosition(aligned.getX(Alignment.TOPLEFT.get()), aligned.getY(Alignment.TOPLEFT.get()), Alignment.CENTER.get());
			img_leftBot.setPosition(aligned.getX(Alignment.BOTTOMLEFT.get()), aligned.getY(Alignment.BOTTOMLEFT.get()), Alignment.CENTER.get());
			img_rightTop.setPosition(aligned.getX(Alignment.TOPRIGHT.get()), aligned.getY(Alignment.TOPRIGHT.get()), Alignment.CENTER.get());
			img_rightBot.setPosition(aligned.getX(Alignment.BOTTOMRIGHT.get()), aligned.getY(Alignment.BOTTOMRIGHT.get()), Alignment.CENTER.get());
		}
		
	}

}
