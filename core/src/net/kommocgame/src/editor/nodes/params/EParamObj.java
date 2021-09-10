package net.kommocgame.src.editor.nodes.params;

import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.ui.TextField.TextFieldFilter;

import net.kommocgame.src.Game;
import net.kommocgame.src.editor.GuiEditor;
import net.kommocgame.src.editor.objects.EObject;

public class EParamObj extends Table {
	
	EObject object;
	GuiEditor gui;
	
	TextFieldFilter filter = new TextFieldFilter() {
		@Override
		public boolean acceptChar(TextField textField, char c) {
			if((c >= '0' && c <= '9') || c == '.' || c == '-') {
				if(textField.getText().contains(".") && c == '.')
					return false;
				
				if(textField.getText().contains("-") && c == '-')
					return false;
				
				return true;
			}
			
			return false;
		}
	};
	
	public EParamObj(GuiEditor gui) {
		this(gui, Game.NEUTRALIZER_UI);
	}

	public EParamObj(GuiEditor gui, Skin skin) {
		super(skin);
		this.gui = gui;
		object = gui.core.getEAHandler().getChoosedObject();
	}
	
	@Override
	public void act(float delta) {
		super.act(delta);
		if(object == null) {
			this.remove();
		}			
	}

}
