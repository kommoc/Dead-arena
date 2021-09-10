package net.kommocgame.src.gui.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.github.czyzby.kiwi.util.gdx.scene2d.Alignment;

import net.kommocgame.src.Game;
import net.kommocgame.src.gui.GuiBase;
import net.kommocgame.src.gui.game.tables.TableEquip;
import net.kommocgame.src.gui.game.tables.TableResourceState;

public class DAGuiEquipMenu extends GuiBase {
	
	TableResourceState table_resources	= new TableResourceState(getRatio(1f/14f));
	TableEquip	table_store;
	
	public DAGuiEquipMenu(Game game) {
		super(game);
		group_stage.addActor(table_resources);
		
		table_resources.setSize(getRatioY(1f), getRatio(1f/10f));
		table_resources.setPosition(getRatioY(1f/2f), getRatio(1f), Alignment.TOP.get());
		
		table_store = new TableEquip(Game.NEUTRALIZER_UI);
		group_stage.addActor(table_store);
		table_store.setSize(Gdx.graphics.getWidth(), getRatio(9f/10f));
		table_store.setPosition(0, 0, Alignment.BOTTOMLEFT.get());
	}
	
	@Override
	public void update(SpriteBatch batch) {
		super.update(batch);
		if(table_store.but_back.isChecked()) {
			table_store.but_back.setChecked(false);
			game.guiManager.removeGui();
			game.guiManager.addGui(new DAGuiStartMenu(game));
		}
		
		//table_store.debugAll();
	}

}
