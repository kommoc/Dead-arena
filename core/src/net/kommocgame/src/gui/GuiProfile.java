package net.kommocgame.src.gui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;

import net.kommocgame.src.Game;
import net.kommocgame.src.Path;

@Deprecated
public class GuiProfile extends GuiBase {
	public Image backGround = new Image(new Texture(Path.gui("mainMenu.png")));			//TODO
	
	public Image back = new Image(new Texture(Path.gui("button1.png")));				//TODO
	public Image profile1 = new Image(new Texture(Path.gui("button1.png")));			//TODO
	public Image profile2 = new Image(new Texture(Path.gui("button1.png")));			//TODO
	public Image profile3 = new Image(new Texture(Path.gui("button1.png")));			//TODO
	
	public Label label_back = new Label("BACK TO MENU", Game.NEUTRALIZER_UI);
	public Label label_profile1 = new Label("PROFILE 1", Game.NEUTRALIZER_UI);
	public Label label_profile2 = new Label("PROFILE 2", Game.NEUTRALIZER_UI);
	public Label label_profile3 = new Label("PROFILE 3", Game.NEUTRALIZER_UI);
	
	public GuiProfile(Game game) {
		super(game);
		this.group_stage.addActor(backGround);
		backGround.setSize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		
		this.addNames();
		this.addButtons();
		this.addLabels();
	}
	
	public void addButtons() {
		group_stage.addActor(back);
		this.setLeftSide(back, true);
		translate(back, 0, -getRatio(1f/6f));
		back.setSize(getRatio(1f), getRatio(1f/12f));
		
		group_stage.addActor(profile1);
		this.setLeftSide(profile1, true);
		translate(profile1, 0, -getRatio(1f/7f) * 2);
		profile1.setSize(getRatio(1f), getRatio(1f/12f));
		
		group_stage.addActor(profile2);
		this.setLeftSide(profile2, true);
		translate(profile2, 0, -getRatio(1f/7f) * 3);
		profile2.setSize(getRatio(1f), getRatio(1f/12f));
		
		group_stage.addActor(profile3);
		this.setLeftSide(profile3, true);
		translate(profile3, 0, -getRatio(1f/7f) * 4);
		profile3.setSize(getRatio(1f), getRatio(1f/12f));
	}
	
	public void addLabels() {
		this.setLeftSide(label_back, true);
		label_back.setTouchable(Touchable.disabled);
		translate(label_back, getRatio(1f/4f), -getRatio(1.1f/6f));
		label_back.setFontScale(getRatio(1f/256f));
		label_back.setColor(Color.LIGHT_GRAY);
		group_stage.addActor(label_back);
		
		this.setLeftSide(label_profile1, true);
		label_profile1.setTouchable(Touchable.disabled);
		translate(label_profile1, getRatio(1f/4f), -getRatio(1f/7f) * 2.1f);
		label_profile1.setFontScale(getRatio(1f/256f));
		label_profile1.setColor(Color.LIGHT_GRAY);
		group_stage.addActor(label_profile1);
		
		this.setLeftSide(label_profile2, true);
		label_profile2.setTouchable(Touchable.disabled);
		translate(label_profile2, getRatio(1f/4f), -getRatio(1f/7f) * 3.1f);
		label_profile2.setFontScale(getRatio(1f/256f));
		label_profile2.setColor(Color.LIGHT_GRAY);
		group_stage.addActor(label_profile2);
		
		this.setLeftSide(label_profile3, true);
		label_profile3.setTouchable(Touchable.disabled);
		translate(label_profile3, getRatio(1f/4f), -getRatio(1f/7f) * 4.1f);
		label_profile3.setFontScale(getRatio(1f/256f));
		label_profile3.setColor(Color.LIGHT_GRAY);
		group_stage.addActor(label_profile3);
		
		//label_profile1.setText(GameSettings.getProfileName(GameSettings.profile1));
		//label_profile2.setText(GameSettings.getProfileName(GameSettings.profile2));
		//label_profile3.setText(GameSettings.getProfileName(GameSettings.profile3));
	}
	
	public void addNames() {
		backGround.setName("mainMenuInGame");
		back.setName("back");
		profile1.setName("profile1");
		profile2.setName("profile2");
		profile3.setName("profile3");
	}
	
	@Override
	public void getActorPressedStage(Actor actor) {
		if(actor == back)
			game.guiManager.removeGui();
		
		if(actor == profile3) {
			//GameSettings.newProfile(GameSettings.profile3, "profileepti");
			game.guiManager.setGui(new GuiMainMenu(game));
		}
	}
	
	@Override
	public void update(SpriteBatch batch) {
		if(this.getObjInCursor() == back) {
			label_back.setColor(Color.RED);
		} else {
			label_back.setColor(Color.LIGHT_GRAY);
		}
		
		if(this.getObjInCursor() == profile1) {
			label_profile1.setColor(Color.RED);
		} else {
			label_profile1.setColor(Color.LIGHT_GRAY);
		}
		
		if(this.getObjInCursor() == profile2) {
			label_profile2.setColor(Color.RED);
		} else {
			label_profile2.setColor(Color.LIGHT_GRAY);
		}
		
		if(this.getObjInCursor() == profile3) {
			label_profile3.setColor(Color.RED);
		} else {
			label_profile3.setColor(Color.LIGHT_GRAY);
		}
	}
}
