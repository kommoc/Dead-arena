package net.kommocgame.src.gui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.Actor;

import net.kommocgame.src.Function;
import net.kommocgame.src.Game;
import net.kommocgame.src.gui.game.DAGuiMainMenu_2;
import net.kommocgame.src.render.GameManager;

@Deprecated
public class GuiInGameMenuV2 extends GuiBase implements GuiHandler.PreInit {
	
	public Function func_blend = new Function(200, Interpolation.exp5Out);
	public boolean close_curtains = false;
	
	public CurtainsV2 curtains_pda;
	public Button buttons_pda;
	public Button button_pda_back;
	
	public GuiInGameMenuV2(Game game) {
		super(game);
		System.out.println("OK");
		
		curtains_pda = new CurtainsV2(-50, getRatio(1f) + 50, Gdx.graphics.getWidth() / 2f, getRatio(1f/2f), getRatioY(1f), getRatio(3f/4f)).setExpand(Gdx.graphics.getWidth(), getRatio(1f));
		group_stage.addActor(curtains_pda);
		buttons_pda = new Button(group_stage, 3, Gdx.graphics.getWidth() / 2.5f, (int)getRatio(1f/8f), (int)getRatio(3f/5f), new String[] {"INVENTORY", "SETTINGS", "MENU"});
		button_pda_back = new Button(group_stage, 1, Gdx.graphics.getWidth() / 4f, (int)Gdx.graphics.getWidth() - (int)Gdx.graphics.getWidth() / 11, (int)getRatio(1f/5f), new String[] {"BACK"}).backward();
		
		curtains_pda.open();
		button_pda_back.setColor(1, 1, 1, func_blend.getValue());
		buttons_pda.setColor(1, 1, 1, func_blend.getValue());
		
		button_pda_back.setLabelColor(1, 1, 1, func_blend.getValue());
		buttons_pda.setLabelColor(1, 1, 1, func_blend.getValue());
	}

	public void getActorPressedStage(Actor actor) {
		if(button_pda_back.pressedButton(actor, 0)) {
			//SoundManager.playSound(SoundManager.sound_ui_button);
			if(!func_blend.getState()) {
				func_blend.start();
				close_curtains = true;
			}
		}
		
		if(buttons_pda.pressedButton(actor, 0)) {
			game.guiManager.addGui(new GuiInGameInventoryV2(game, curtains_pda, this));
			this.setHide(true);
		}
		
		if(buttons_pda.pressedButton(actor, 2)) {
			game.world.deleteLevel();
			//level.save //FIXME
			game.guiManager.reset();
			game.guiManager.addGui(new DAGuiMainMenu_2(game));
		}
	}
	
	public void preInit() {
		
	}
	
	//FIXME
	public void update(SpriteBatch batch) {
		func_blend.init();
		
		if(curtains_pda.hasOpen()) {
			func_blend.start();
		}
		
		if(curtains_pda.state == curtains_pda.STATE_CLOSE) {
			game.guiManager.removeGui();
		}
		
		if(func_blend.hasEnded()) {
			func_blend.reload();
			func_blend.switchBackward();
		}
		
		if(close_curtains && func_blend.hasEnded()) {
			curtains_pda.narrow();
			curtains_pda.close();
		} else if(curtains_pda.state == curtains_pda.STATE_CLOSE)
			close_curtains = false;
		
		if(func_blend.getValue() == 0) {
			button_pda_back.setVisible(false);
			buttons_pda.setVisible(false);
		} else {
			button_pda_back.setVisible(true);
			buttons_pda.setVisible(true);
		}
		
		button_pda_back.setColor(1, 1, 1, func_blend.getValue());
		buttons_pda.setColor(1, 1, 1, func_blend.getValue());
		
		button_pda_back.setLabelColor(1, 1, 1, func_blend.getValue());
		buttons_pda.setLabelColor(1, 1, 1, func_blend.getValue());
		
		if(this.game.guiManager.guiList.peek() == this && GameManager.instance.cursor != 1)
			GameManager.instance.setCursorCrosshair();
		
		group_stage.debugAll();
	}
	
	public void setHide(boolean par1) {
		func_blend.reload();
		func_blend.setBackward(par1);
		func_blend.start();
	}
}

