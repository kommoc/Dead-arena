package net.kommocgame.src.gui.game.tables;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;

import net.kommocgame.src.Game;
import net.kommocgame.src.Loader;

public class TableResourceState extends Table {
	
	Image img_premium_money = new Image(Loader.guiIcon("icon_premium_money.png"));
	Image img_money			= new Image(Loader.guiIcon("icon_money.png"));
	
	Label label_login_name		= new Label("login_name", Game.GENERATED_LABEL_STYLE_GOTTHARD_LITTLE_TEXT_WHITE);
	Label label_money			= new Label("1160", Game.GENERATED_LABEL_STYLE_GOTTHARD_LITTLE_TEXT_WHITE);
	Label label_premium_money	= new Label("100", Game.GENERATED_LABEL_STYLE_GOTTHARD_LITTLE_TEXT_WHITE);
	
	public TableResourceState(float sprite_size) {
		super(Game.NEUTRALIZER_UI);
		this.setBackground("button");
		this.setColor(Color.BROWN);
		
		label_login_name.setColor(Game.COLOR_TEXT_SELECTED);
		label_money.setColor(Game.COLOR_TEXT);
		label_premium_money.setColor(Game.COLOR_TEXT_SELECTED);
		
		Table table_money = new Table(Game.NEUTRALIZER_UI);
		Table table_premium_money = new Table(Game.NEUTRALIZER_UI);
		Table table_login_name = new Table(Game.NEUTRALIZER_UI);
		
		table_money.setBackground("panel");
		table_premium_money.setBackground("panel");
		table_login_name.setBackground("panel");
		
		table_money.setColor(Color.GOLDENROD);
		table_premium_money.setColor(Color.GOLDENROD);
		table_login_name.setColor(Color.GOLDENROD);
		
		table_money.left();
		table_money.add(img_money).size(sprite_size).padLeft(sprite_size / 2f).padRight(sprite_size / 2f);
		table_money.add(label_money);
		
		table_premium_money.left();
		table_premium_money.add(img_premium_money).size(sprite_size).padLeft(sprite_size / 2f).padRight(sprite_size / 2f);
		table_premium_money.add(label_premium_money);
		
		table_login_name.add(label_login_name);
		
		this.add(table_money).left().growY().width(Gdx.graphics.getWidth() / 4f);
		this.add(table_premium_money).left().growY().width(Gdx.graphics.getWidth() / 4f);
		this.add(table_login_name).right().growY().width(Gdx.graphics.getWidth() / 4f).expandX();
	}
	
	@Override
	public void act(float delta) {
		super.act(delta);
		
		label_money.setText("" + Game.profile.get_money());
		label_premium_money.setText("" + Game.profile.get_cryptcoin());
	}
	
}
