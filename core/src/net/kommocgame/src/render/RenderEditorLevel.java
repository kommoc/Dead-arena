package net.kommocgame.src.render;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.github.czyzby.kiwi.util.gdx.scene2d.Alignment;

import net.kommocgame.src.Loader;
import net.kommocgame.src.world.level.LevelBase;

public class RenderEditorLevel extends RenderLevel {
	
	public Image img_level = new Image(Loader.level("level_1/level_plan.png"));
	public Image img_axis = new Image(Loader.level("level_editor/ort_axis.png"));
	
	public RenderEditorLevel(SpriteBatch batch, LevelBase level) {
		super(batch, level);
		img_axis.setSize(img_axis.getWidth() * 0.04f, img_axis.getHeight() * 0.04f);
		addObject(img_level);
		addObject(img_axis);
		img_axis.setPosition(0, 0, Alignment.CENTER.get());
		img_level.setPosition(0, 0, Alignment.CENTER.get());
	}
}
