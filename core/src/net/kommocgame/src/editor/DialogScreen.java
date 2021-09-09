package net.kommocgame.src.editor;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.github.czyzby.kiwi.util.gdx.scene2d.Alignment;

import net.kommocgame.src.Game;
import net.kommocgame.src.Loader;
import net.kommocgame.src.Path;

public class DialogScreen extends Dialog {
	
	private Image icon_apply = new Image(Loader.guiEditor("but/menu/icon_apply.png"));
	private Image icon_cancel = new Image(Loader.guiEditor("but/menu/icon_cancel.png"));
	
	public Button but_apply = new Button(Game.NEUTRALIZER_UI);
	public Button but_cancel = new Button(Game.NEUTRALIZER_UI);
	
	private boolean isActive = false;
	
	public DialogScreen(String title, Skin skin) {
		super(title, skin);
		this.init();
		this.setMovable(false);
		this.setPosition(Gdx.graphics.getWidth() / 2f, Gdx.graphics.getHeight() / 2f, Alignment.CENTER.get());
	}

	public DialogScreen(String title, WindowStyle windowStyle) {
		super(title, windowStyle);
		this.init();
	}

	public DialogScreen(String title, Skin skin, String windowStyleName) {
		super(title, skin, windowStyleName);
		this.init();
	}
	
	public void init() {
		Label label_apply = new Label("OK", Game.NEUTRALIZER_UI);
		Label label_cancel = new Label("CANCEL", Game.NEUTRALIZER_UI);
		
		but_apply.add(icon_apply).width(but_apply.getHeight()).height(but_apply.getHeight());
		but_apply.add(label_apply).padLeft(but_apply.getHeight() / 8f).padRight(but_apply.getHeight() / 8f);
		
		but_cancel.add(icon_cancel).width(but_cancel.getHeight()).height(but_cancel.getHeight());
		but_cancel.add(label_cancel).padLeft(but_cancel.getHeight() / 8f).padRight(but_cancel.getHeight() / 8f);
		
		but_apply.align(Alignment.CENTER.get());
		but_cancel.align(Alignment.CENTER.get());
		
		this.button(but_apply);
		this.button(but_cancel);
		this.setWidth(this.getWidth() * 2.5f);
	}
	
	/** Return's stage have or have'nt that dialog. */
	public boolean isActive() {
		return isActive;
	}
	
	/** If was pressed on OK button. */
	public boolean _apply() {
		return but_apply.isChecked();
	}
	
	/** If was pressed on CANCEL button. */
	public boolean _cancel() {
		return but_cancel.isChecked();
	}
	
	@Override
	public void act(float par1) {
		if(this.getStage() != null)
			isActive = true;
		else 
			isActive = false;
	}
}
