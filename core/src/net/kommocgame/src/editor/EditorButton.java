package net.kommocgame.src.editor;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

public class EditorButton extends Button {
	
	private Image icon;
	
	public EditorButton(Skin skin, Texture tex) {
		super(skin);
		this.setColor(Color.SKY);
		icon = new Image(tex);
		icon.setTouchable(Touchable.disabled);
		this.add(icon).center().width(this.getWidth()).height(this.getWidth());
	}
	
	@Override
	public void act(float deltaTime) {}
}
