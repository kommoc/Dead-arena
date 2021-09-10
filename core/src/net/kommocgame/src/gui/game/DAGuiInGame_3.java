package net.kommocgame.src.gui.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.ui.Cell;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.WidgetGroup;
import com.badlogic.gdx.utils.Align;
import com.github.czyzby.kiwi.util.gdx.scene2d.Alignment;

import net.kommocgame.src.Game;
import net.kommocgame.src.gui.GuiBase;
import net.kommocgame.src.gui.DeadArena.DAGuiSettings;
import net.kommocgame.src.gui.game.inGame.TableInGameItemSwitcher;
import net.kommocgame.src.gui.game.inGame.TableInGameControls;
import net.kommocgame.src.gui.game.inGame.TableInGameMenu;
import net.kommocgame.src.gui.game.inGame.TableInGamePlayerHealth;

public class DAGuiInGame_3 extends GuiBase {
	
	TableInGameMenu			table_menu			= new TableInGameMenu();
	TableInGameControls		table_controls		= new TableInGameControls();
	TableInGameItemSwitcher	table_itemSwitcher	= new TableInGameItemSwitcher();
	TableInGamePlayerHealth	table_player_health	= new TableInGamePlayerHealth();
	
	private Table incapsulate_table_menu;
	private Table incapsulate_table_controls;
	private Table incapsulate_table_item_switcher;
	private Table incapsulate_table_player_health;
	
	public DAGuiInGame_3(Game game) {
		super(game);
		
		incapsulate_table_player_health = createCanvas(table_player_health, true, false, Align.top);
		incapsulate_table_menu = createCanvas(table_menu, false, false, Align.topLeft);
		table_menu.menuState(true);
		
		incapsulate_table_controls = createCanvas(table_controls, true, false, Align.bottom);
		incapsulate_table_item_switcher = createCanvas(table_itemSwitcher, false, false, Align.topRight);
	}
	
	/** Create full screen canvas and add actor into it with specified attributes. 
	 * The canvas will be added to group_stage. */
	private Table createCanvas(Actor toActor, boolean growX, boolean growY, int align) {
		Table table = new Table();
		group_stage.addActor(table);
		table.setSize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		table.setPosition(0, Gdx.graphics.getHeight(), Alignment.TOPLEFT.get());
		
		Cell cell = table.add(toActor);
		
		if(growX) cell.growX().align(align).expand();
		else if(growY) cell.growY().align(align).expand();
		else cell.expand().align(align);
		
		return table;
	}
	
	@Override
	public void update(SpriteBatch batch) {
		super.update(batch);
		
		updateTableMenu();
		updateTableControls();
		updateTableItemSwicher();
		updateTablePlayerHealth();
	}
	
	/** Update menu table. */
	public void updateTableMenu() {
		if(table_menu.but_menu.isChecked()) {
			table_menu.but_menu.setChecked(false);
			
			table_menu.menuState(!table_menu.isOpened());
		}
		
		if(table_menu.but_inventory.isChecked()) {
			table_menu.but_inventory.setChecked(false);
			
		}
		
		if(table_menu.but_settings.isChecked()) {
			table_menu.but_settings.setChecked(false);
			
			getGuiManager().addGui(new DAGuiSettings(game));
		}
		
		if(table_menu.but_exit.isChecked()) {
			table_menu.but_exit.setChecked(false);
			
			Game.CORE.deconstructLevel();
			game.guiManager.reset();
			game.guiManager.addGui(new DAGuiMainMenu_2(game));
		}
	}
	
	/** Update controls table. */
	public void updateTableControls() {}
	
	/** Update item switcher table. */
	public void updateTableItemSwicher() {}
	
	/** Update item switcher table. */
	public void updateTablePlayerHealth() {
		if(!table_player_health.isPadDefined) {
			incapsulate_table_player_health.getCell(table_player_health).padLeft(table_menu.getWidth() + Game.GUI_NUMBER_PANEL_GAP)
					.padRight(table_itemSwitcher.getWidth() + Game.GUI_NUMBER_PANEL_GAP);
			incapsulate_table_player_health.invalidate();
			table_menu.menuState(false);
			
			table_player_health.isPadDefined = true;
		}
	}
	
}
