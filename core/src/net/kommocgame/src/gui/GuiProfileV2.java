package net.kommocgame.src.gui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.ObjectMap;
import com.github.czyzby.kiwi.util.gdx.scene2d.Alignment;

import net.kommocgame.src.Function;
import net.kommocgame.src.Game;
import net.kommocgame.src.Loader;
import net.kommocgame.src.Path;
import net.kommocgame.src.SoundManager;
import net.kommocgame.src.control.InputHandler;

@Deprecated
public class GuiProfileV2 extends GuiBase {
	
	public Button but_back;
	private GuiMainMenuV2 preGui;
	private float par1 = 0f;
	private boolean close_profile = false;
	private int currentSlot = -1; // -1 - null, 1 - first i t.d.
	
	public Function func_blend = new Function(300, Interpolation.linear);
	
	public Texture tex_profile_deactive = Loader.guiMenuPanel("profile_slot.png");
	public Texture tex_profile_active = Loader.guiMenuPanel("profile_slot_active.png");
	
	private TextureRegion tex_reg_profile_active = new TextureRegion(tex_profile_active);
	private TextureRegion tex_reg_profile_deactive = new TextureRegion(tex_profile_deactive);
	
	private TextureRegionDrawable drawable_profile_active = new TextureRegionDrawable(tex_reg_profile_active);
	private TextureRegionDrawable drawable_profile_deactive = new TextureRegionDrawable(tex_reg_profile_deactive);
	
	public ObjectMap<Image, Integer> map_image_profile = new ObjectMap<Image, Integer>();
	
	public Image image_profile_1 = new Image(drawable_profile_deactive);
	public Image image_profile_2 = new Image(drawable_profile_deactive);
	public Image image_profile_3 = new Image(drawable_profile_deactive);
	public Image image_profile_4 = new Image(drawable_profile_deactive);
	
	public Image image_panel_profile = new Image(Loader.guiMenuPanel("rect_panel_1.png"));
	
	public Table profile_group;	//FIXME (setVisible, setTouchable);
	
	public GuiProfileV2(Game game, GuiMainMenuV2 gui) {
		super(game);
		preGui = gui;
		
		float factorScale = Gdx.graphics.getWidth() / image_panel_profile.getWidth();
		
		image_panel_profile.setSize(image_panel_profile.getWidth() * factorScale, image_panel_profile.getHeight() * factorScale);
		image_panel_profile.setPosition(-image_panel_profile.getWidth(), (Gdx.graphics.getHeight() - image_panel_profile.getHeight()) / 2f);
		group_stage.addActor(image_panel_profile);
		
		but_back = new Button(group_stage, 1, getRatioImgX(1f/2f), (int)image_panel_profile.getX(Alignment.RIGHT.get()) - (int)getRatioImgX(1f/8f),
				(int)getRatioImgY(1f/8f), new String[] {"BACK"}).backward();
		
		profile_group = new Table(game.NEUTRALIZER_UI);
		profile_group.setBounds(getRatioImgX(1f/12f), getRatioImgY(1f/12f), getRatioImgX(10f/12f), getRatioImgX(10f/12f));
		
		profile_group.add(image_profile_1).expand().fill();
		profile_group.add(image_profile_2).expand().fill();
		profile_group.row();
		profile_group.add(image_profile_3).expand().fill();
		profile_group.add(image_profile_4).expand().fill();
		
		map_image_profile.put(image_profile_1, 1);
		map_image_profile.put(image_profile_2, 2);
		map_image_profile.put(image_profile_3, 3);
		map_image_profile.put(image_profile_4, 4);
		
		group_stage.addActor(profile_group);
		
		profile_group.setVisible(false);
		profile_group.setTouchable(Touchable.disabled);
		but_back.setVisible(false);
	}
	
	public void update(SpriteBatch batch) {
		if(-image_panel_profile.getWidth() * (1f - preGui.func_openProfile.getValue()) != image_panel_profile.getX()) {
			image_panel_profile.setPosition(-image_panel_profile.getWidth() * (1f - preGui.func_openProfile.getValue()), (Gdx.graphics.getHeight() - image_panel_profile.getHeight()) / 2f);
			but_back.setPosition(image_panel_profile.getX(Alignment.RIGHT.get()) - getRatioImgX(1f/8f), getRatioImgY(1f/4f));
			
			if(preGui.func_openProfile.getValue() == 0f && preGui.func_openProfile.ended()) {
				game.guiManager.removeGui();
			}
		}
		
		if(preGui.func_openProfile.ended()) {
			but_back.setVisible(true);
			profile_group.setVisible(true);
			profile_group.setTouchable(Touchable.enabled);
			
			func_blend.start();
		}
		
		if(profile_group.getColor().r != func_blend.getValue()) {
			par1 = func_blend.getValue();
			
			profile_group.setColor(1, 1, 1, par1);
			but_back.setColor(1, 1, 1, par1);
		}
		
		if(func_blend.hasEnded() && func_blend.getValue() == 0f) {
			preGui.func_openProfile.setBackward(true);
			preGui.func_openProfile.reload();
			preGui.func_openProfile.start();
		}
		
		func_blend.init();
	}
	
	public void setActiveSlot(int id) {
		if(id == -1 && map_image_profile.findKey(currentSlot, false).getDrawable() == drawable_profile_active) {
			map_image_profile.findKey(currentSlot, false).setDrawable(drawable_profile_deactive);
			
			return;
		}
		
		for(int i = 1; i <= map_image_profile.size; i++) {
			if(i == id) {
				map_image_profile.findKey(i, false).setDrawable(drawable_profile_active);
				
				currentSlot = i;
			} else {
				map_image_profile.findKey(i, false).setDrawable(drawable_profile_deactive);
			}
		}
	}
	
	public void getActorPressedStage(Actor actor) {
		//SoundManager.playSound(SoundManager.sound_ui_button);
		
		if(but_back.pressedButton(actor, 0)) {
			if(close_profile != true) {
				func_blend.reload();
				func_blend.setBackward(true);
				func_blend.start();
			}
			
			//SoundManager.playSound(SoundManager.sound_ui_button);
			close_profile = true;
			return;
		}
		
		setActiveSlot(map_image_profile.get((Image) actor));
	}
	
	public float getRatioImgX(float ratio) {
		return image_panel_profile.getHeight() * ratio;
	}
	
	public float getRatioImgY(float ratio) {
		return image_panel_profile.getY() + image_panel_profile.getHeight() * ratio;
	}
}
