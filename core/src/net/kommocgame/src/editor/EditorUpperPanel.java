package net.kommocgame.src.editor;

import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.VerticalGroup;
import com.badlogic.gdx.scenes.scene2d.ui.WidgetGroup;
import com.github.czyzby.kiwi.util.gdx.scene2d.Alignment;

import net.kommocgame.src.Game;
import net.kommocgame.src.control.InputHandler;

public class EditorUpperPanel extends WidgetGroup {
	
	private TextButton but_main;
	protected VerticalGroup panel = new VerticalGroup();
	
	private int openType = OpenTab.TAP.get();
	private int i;
	
	/** Create panel with dropping list. */
	public EditorUpperPanel(String text, float x, float y) {
		this(text, x, y, Alignment.BOTTOMLEFT);
	}
	
	/** Create panel with dropping list. */
	public EditorUpperPanel(String text, float x, float y, Alignment alignment) {
		but_main = new TextButton(text, Game.NEUTRALIZER_UI);
		this.addActor(but_main);
		this.addActor(panel);
		panel.columnLeft();
		panel.fill();
		
		but_main.setPosition(x, y, alignment.get());
		panel.setPosition(x, but_main.getY(Alignment.BOTTOM.get()), Alignment.TOPLEFT.get());
	}
	
	/** Sets the opening type. */
	public EditorUpperPanel openType(OpenTab type) {
		openType = type.get();
		
		return this;
	}
	
	/** Add new button to list. */
	public EditorUpperPanel addButton(String text, Image icon) {
		Button but = new Button(Game.NEUTRALIZER_UI);
		Label label = new Label(text, Game.NEUTRALIZER_UI);
		icon.setTouchable(Touchable.disabled);
		label.setTouchable(Touchable.disabled);
		
		but.align(Alignment.LEFT.get());
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
		
		but_main.setPosition(x, y);
		panel.setPosition(x, but_main.getY(Alignment.BOTTOM.get()), Alignment.TOPLEFT.get());
	}
	
	@Override
	public void setPosition(float x, float y, int alignment) {
		super.setPosition(x, y, alignment);
		
		but_main.setPosition(x, y, alignment);
		panel.setPosition(x, but_main.getY(Alignment.BOTTOM.get()), Alignment.TOPLEFT.get());
	}
	
	/** Return's the main button. */
	public TextButton getMainBut() {
		return this.but_main;
	}
	
	/** Return's the list of panel buttons. */
	public VerticalGroup getPanel() {
		return this.panel;
	}
	
	@Override
	public void act (float delta) {
		super.act(delta);
		
		if(openType == OpenTab.HIT.get()) {
			if(but_main.isOver() || 
					panel.getChildren().contains(getStage().hit(InputHandler.GUI_mouse_x, InputHandler.GUI_mouse_y, true), false)) {
				panel.setVisible(true);
				panel.setTouchable(Touchable.childrenOnly);
			} else {
				panel.setVisible(false);
				panel.setTouchable(Touchable.disabled);
				
			}
		} else if(openType == OpenTab.TAP.get()) {
			if(but_main.isChecked()) {
				if(!panel.isVisible()) {
					panel.setVisible(true);
					panel.setTouchable(Touchable.childrenOnly);
				}
			} else {
				panel.setVisible(false);
				panel.setTouchable(Touchable.disabled);
			}
		}
		
		this.checkPress();
	}
	
	private void checkPress() {
		for(i = 0; i < panel.getChildren().size; i++) {
			if(((Button)panel.getChildren().get(i)).isPressed()) {
				this.close();
			}
		}
	}
	
	public Button getButton(int id) {
		return (Button)panel.getChildren().get(id);
	}
	
	/** Close the tab. */
	public void close() {
		if(openType == OpenTab.TAP.get())
			but_main.setChecked(false);
		
		panel.setVisible(false);
		panel.setTouchable(Touchable.disabled);
	}
}
