package net.kommocgame.src.render;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.github.czyzby.kiwi.util.gdx.scene2d.Alignment;

import net.kommocgame.src.Loader;
import net.kommocgame.src.world.level.LevelBase;

public class RenderLevelTEST extends RenderLevel {
	//public Image img_main = new Image(Loader.level("level_1/level_plan.png"));;
	
	public RenderLevelTEST(SpriteBatch batch, LevelBase level) {
		super(batch, level);
		//objects.addActor(img_main);
		//img_main.setPosition(0, 0, Alignment.CENTER.get());
	}
}
