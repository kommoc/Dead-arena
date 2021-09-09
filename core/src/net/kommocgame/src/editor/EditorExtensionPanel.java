package net.kommocgame.src.editor;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.VerticalGroup;
import com.badlogic.gdx.scenes.scene2d.ui.WidgetGroup;
import com.github.czyzby.kiwi.util.gdx.scene2d.Alignment;

import net.kommocgame.src.Game;
import net.kommocgame.src.control.InputHandler;

public class EditorExtensionPanel extends WidgetGroup {

	protected VerticalGroup panel = new VerticalGroup();
	protected Actor additor;
	
	private int openType = OpenTab.HIT.get();
	private int i;
	
	private boolean state_open = false;
	
	/** Create panel with dropping list. */
	public EditorExtensionPanel(String text, float x, float y) {
		this(text, x, y, Alignment.TOPLEFT);
	}
	
	/** Create panel with dropping list. */
	public EditorExtensionPanel(String text, float x, float y, Alignment alignment) {
		this.addActor(panel);
		this.addButton(text, null);
		panel.columnLeft();
		panel.fill();

		panel.setPosition(x, y, alignment.get());
	}
	
	/** Sets the opening type. */
	public EditorExtensionPanel openType(OpenTab type) {
		openType = type.get();
		
		return this;
	}
	
	/** Add new button to list. */
	public EditorExtensionPanel addButton(String text, Image icon) {
		Button but = new Button(Game.NEUTRALIZER_UI);
		Label label = new Label(text, Game.NEUTRALIZER_UI);
		
		if(icon != null)
			icon.setTouchable(Touchable.disabled);
		label.setTouchable(Touchable.disabled);
		but.align(Alignment.LEFT.get());
		
		if(icon != null)
			but.add(icon).width(but.getHeight()).height(but.getHeight());
		but.add(label).padLeft(but.getHeight() / 8f).padRight(but.getHeight() / 8f);
		
		panel.addActor(but);
		panel.fill();
		panel.left();
		return this;
	}
	
	@Override
	public void setPosition(float x, float y) {
		super.setPosition(x, y);

		panel.setPosition(x, y, Alignment.TOPLEFT.get());
	}
	
	@Override
	public void setPosition(float x, float y, int alignment) {
		super.setPosition(x, y, alignment);

		panel.setPosition(x, y, alignment);
	}
	
	/** Return's the list of panel buttons. */
	public VerticalGroup getPanel() {
		return this.panel;
	}
	
	@Override
	public void act (float delta) {
		super.act(delta);
		if(openType == OpenTab.HIT.get()) {
			if((additor != null && getStage().hit(InputHandler.GUI_mouse_x, InputHandler.GUI_mouse_y, true) == additor) || 
					panel.getChildren().contains(getStage().hit(InputHandler.GUI_mouse_x, InputHandler.GUI_mouse_y, true), false)) {
				panel.setVisible(true);
				panel.setTouchable(Touchable.childrenOnly);
			} else {
				panel.setVisible(false);
				panel.setTouchable(Touchable.disabled);
			}
		}  else if(openType == OpenTab.TAP.get()) {
			if(((Button) additor).isPressed()) {
				state_open = true;
				
			} else if(state_open) { 
				if(!this.checkHit())
					state_open = false;
				
				if(!panel.isVisible()) {
					panel.setVisible(true);
					panel.setTouchable(Touchable.childrenOnly);
				}
			} else if(!state_open){
				panel.setVisible(false);
				panel.setTouchable(Touchable.disabled);
			}
		}
		
		this.checkPress();
	}
	
	public EditorExtensionPanel setAdditor(Actor actor) {
		additor = actor;
		return this;
	}
	
	private void checkPress() {
		for(i = 0; i < panel.getChildren().size; i++) {
			if(((Button)panel.getChildren().get(i)).isPressed()) {
				this.close();
			}
		}
	}
	
	private boolean checkHit() {
		return (additor != null && getStage().hit(InputHandler.GUI_mouse_x, InputHandler.GUI_mouse_y, true) == additor) || 
				panel.getChildren().contains(getStage().hit(InputHandler.GUI_mouse_x, InputHandler.GUI_mouse_y, true), false);
	}
	
	public Button getButton(int id) {
		return (Button)panel.getChildren().get(id);
	}
	
	/** Close the tab. */
	public void close() {
		panel.setVisible(false);
		panel.setTouchable(Touchable.disabled);
	}
}
