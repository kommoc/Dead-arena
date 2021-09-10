package net.kommocgame.src.gui.game.items;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

import net.kommocgame.src.Game;

public class ButtonStoreBuy extends Button {
	
	Label label_buy				= new Label("BUY", Game.GENERATED_LABEL_STYLE_GOTTHARD_BUTTON);
	Label label_purchased		= new Label("item_name is purchased", Game.GENERATED_LABEL_STYLE_GOTTHARD_LITTLE_TEXT);
	Label label_notAvailable	= new Label("item_name not available", Game.GENERATED_LABEL_STYLE_GOTTHARD_LITTLE_TEXT);			//name not correct 
	
	public ButtonStoreBuy(Skin skin) {
		super(skin);
	}
	
	public void setButton_BUY() {
		this.clearChildren();
		this.add(label_buy);
		this.setTouchable(Touchable.enabled);
		this.setColor(Color.RED);
		
		System.out.println("ButtonStoreBuy.setButton_BUY() ### BUY");
	}
	
	public void setButton_PURCHASED() {
		this.clearChildren();
		this.setColor(Game.COLOR_PANEL_INFO);
		this.setTouchable(Touchable.disabled);
		
		this.add(label_purchased);
		System.out.println("ButtonStoreBuy.setButton_PURCHASED() ### PURCHASED");
	}
	
	public void setButton_NOT_AVAILABLE() {
		this.clearChildren();
		this.setColor(Color.LIGHT_GRAY);
		this.setTouchable(Touchable.disabled);
		
		this.add(label_notAvailable);
		label_notAvailable.setText("Not available");
		System.out.println("ButtonStoreBuy.setButton_NOT_AVAILABLE() ### Not available");
	}
	
	public void setButton_LOW_LVL() {
		this.setButton_NOT_AVAILABLE();
		label_notAvailable.setText("Locked\nNeed [] lvl");
		System.out.println("ButtonStoreBuy.setButton_LOW_LVL() ### Locked Need [] lvl");
	}
	
	public void setButton_ENOUGH_MONEY() {
		this.setButton_NOT_AVAILABLE();
		label_notAvailable.setText("Enough money");
		System.out.println("ButtonStoreBuy.setButton_ENOUGH_MONEY() ### Enough money");
	}
	
}
