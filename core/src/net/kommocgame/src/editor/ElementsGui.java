package net.kommocgame.src.editor;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.SelectBox;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.github.czyzby.kiwi.util.gdx.scene2d.Alignment;

import net.kommocgame.src.Game;
import net.kommocgame.src.Loader;
import net.kommocgame.src.editor.nodes.params.EColor;

public class ElementsGui {
	
	public static class ButCancel extends Button {
		Label label_cancel = new Label("CANCEL", Game.NEUTRALIZER_UI);
		Image icon_cancel = new Image(Loader.guiEditor("but/menu/icon_cancel.png"));
		
		public ButCancel() {
			this(Game.NEUTRALIZER_UI);
		}
		
		public ButCancel(Skin skin) {
			super(skin);
			this.add(icon_cancel).width(this.getHeight()).height(this.getHeight());
			this.add(label_cancel).padLeft(this.getHeight() / 8f).padRight(this.getHeight() / 8f);
			this.align(Alignment.CENTER.get());
		}
	}
	
	public static class ButApply extends Button {
		
		Label label_apply = new Label("OK", Game.NEUTRALIZER_UI);
		Image icon_apply = new Image(Loader.guiEditor("but/menu/icon_apply.png"));
		
		public ButApply() {
			this(Game.NEUTRALIZER_UI);
		}
		
		public ButApply(Skin skin) {
			super(skin);
			this.add(icon_apply).width(this.getHeight()).height(this.getHeight());
			this.add(label_apply).padLeft(this.getHeight() / 8f).padRight(this.getHeight() / 8f);
			this.align(Alignment.CENTER.get());
		}
		
		@Override
		public void act(float delta) {
			super.act(delta);
		}
	}
	
	public static class SelectorColor extends SelectBox {
		
		public SelectorColor() {
			super(Game.NEON_UI);
			setItems("BLACK", "BLUE", "BROWN", "CHARTREUSE", "CLEAR", "CORAL", "CYAN",
					"DARK_GRAY", "FIREBRICK", "FOREST", "GOLD", "GOLDENROD", "GRAY", "GREEN",
					"LIGHT_GRAY", "LIME", "MAGENTA", "MAROON", "NAVY", "OLIVE", "ORANGE", "PINK",
					"PURPLE", "RED", "ROYAL", "SALMON", "SCARLET", "SKY", "SLATE",
					"TAN", "TEAL", "VIOLET", "WHITE", "YELLOW");
		}
		
		public void setSelectorColor(Color color) {
			this.setSelected(EColor.getEColor(color).toString());
		}
		
		public Color getSelectorColor() {
			return (EColor.valueOf((String)this.getSelection().getLastSelected())).getColor();
		}
	}
}
