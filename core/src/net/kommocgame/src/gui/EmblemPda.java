package net.kommocgame.src.gui;

import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.github.czyzby.kiwi.util.gdx.scene2d.Alignment;

import net.kommocgame.src.Function;
import net.kommocgame.src.Loader;
@Deprecated
public class EmblemPda extends Table {
	
	private Image img_emblem = new Image(Loader.guiIcon("emblem_pda.png"));
	private Image img_emblem_effect_scaled = new Image(Loader.guiIcon("emblem_pda_effect_scaled.png"));
	private Image img_emblem_effect_flackern = new Image(Loader.guiIcon("emblem_pda_effect_flackern.png"));
	private Function func_blend = new Function(1000, Interpolation.linear).setBouncing(true);
	
	public EmblemPda(Skin skin) {
		super(skin);
		float diff = img_emblem.getHeight() / img_emblem_effect_flackern.getHeight();
		float ratio = 1f / 1.5f;
		img_emblem.setSize(GuiBase.getRatio(ratio) * diff, GuiBase.getRatio(ratio) * diff);
		img_emblem_effect_scaled.setSize(GuiBase.getRatio(ratio), GuiBase.getRatio(ratio));
		img_emblem_effect_flackern.setSize(GuiBase.getRatio(ratio), GuiBase.getRatio(ratio));;
		
		this.setSize(img_emblem_effect_flackern.getWidth(), img_emblem_effect_flackern.getHeight());
		img_emblem.setOrigin(img_emblem.getWidth() / 2f, img_emblem.getHeight() / 2f);
		img_emblem_effect_scaled.setOrigin(img_emblem_effect_scaled.getWidth() / 2f, img_emblem_effect_scaled.getHeight() / 2f);
		img_emblem_effect_flackern.setOrigin(img_emblem_effect_flackern.getWidth() / 2f, img_emblem_effect_flackern.getHeight() / 2f);
		
		this.addActor(img_emblem_effect_flackern);
		this.addActor(img_emblem_effect_scaled);
		this.addActor(img_emblem);
		
		img_emblem.setPosition(this.getWidth() / 2f, this.getHeight() / 2f, Alignment.CENTER.get());
		img_emblem_effect_scaled.setPosition(this.getWidth() / 2f, this.getHeight() / 2f, Alignment.CENTER.get());
		img_emblem_effect_flackern.setPosition(this.getWidth() / 2f, this.getHeight() / 2f, Alignment.CENTER.get());
		
		this.setTouchable(Touchable.disabled);
		func_blend.start();
	}
	
	@Override
	public void act(float deltaTime) {
		func_blend.init();
		img_emblem_effect_scaled.rotateBy(deltaTime * 5f);
		
		img_emblem.setColor(1, 1, 1, 0.8f - func_blend.getValue() * 0.4f);
		img_emblem_effect_flackern.setColor(1, 1, 1, 1f -func_blend.getValue());
		img_emblem_effect_scaled.setColor(1, 1, 1, 0.5f - func_blend.getValue() * 0.3f);
	}
}
