package net.kommocgame.src.gui.game.items;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Align;
import com.brashmonkey.spriter.Entity;
import com.brashmonkey.spriter.Player;
import com.brashmonkey.spriter.gdx.Drawer;

import net.kommocgame.src.Game;
import net.kommocgame.src.gui.GuiBase;
import net.kommocgame.src.item.Item;
import net.kommocgame.src.render.RenderEngine;

public class TablePlayerSprite extends Table {
	
	/** Body SCML sprite. */
	Player sprite_player;
	/** Boots SCML sprite. */
	Player sprite_boots;
	
	Drawer drawer;
	Item item;
	
	public TablePlayerSprite(Drawer drawer) {
		this(Game.NEUTRALIZER_UI, drawer);
	}

	public TablePlayerSprite(Skin skin, Drawer drawer) {
		super(skin);
		this.drawer = drawer;
		
		sprite_player = new Player(RenderEngine.getSCMLData("human_main"));
		//sprite_player.setScale(Game.SCALE_WORLD_VALUE_FINAL * 0.75f);
		
		sprite_boots = new Player(RenderEngine.getSCMLData("human_main_feet"));
		//sprite_boots.setScale(Game.SCALE_WORLD_VALUE_FINAL * 0.75f);
		
		if(sprite_player.characterMaps == null)
			sprite_player.characterMaps = new Entity.CharacterMap[16];
	}
	
	public void setItem(Item item) {
		this.item = item;
	}
	
	@Override
	public void draw(Batch batch, float parentAlpha) {	//Создать новый экземпляр отрисовщика для GuiBatch (Drawer).
		super.draw(batch, parentAlpha);
		
		if(item == null) {
			sprite_player.characterMaps[9] = sprite_player.getEntity().getCharacterMap("BA_clear");
		} else {
			if(item == Item.item_armor_light) {
			sprite_player.characterMaps[9] = sprite_player.getEntity().getCharacterMap("BA_light");
			} else if(item == Item.item_armor_medium) {
				sprite_player.characterMaps[9] = sprite_player.getEntity().getCharacterMap("BA_medium");
			}
		}
		
		sprite_player.setScale(getHeight() / 156f);
		sprite_boots.setScale(getHeight() / 156f);
		
		sprite_player.setPosition(this.getX(Align.center), this.getY(Align.center));
		sprite_boots.setPosition(this.getX(Align.center), this.getY(Align.center));
		sprite_player.update();
		sprite_boots.update();
		
		drawer.draw(sprite_boots);
		drawer.draw(sprite_player);
		
		
	}
}
