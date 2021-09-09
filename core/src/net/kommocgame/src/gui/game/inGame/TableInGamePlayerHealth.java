package net.kommocgame.src.gui.game.inGame;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ProgressBar;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;

import net.kommocgame.src.Game;
import net.kommocgame.src.gui.GuiBase;

public class TableInGamePlayerHealth extends Table {
	
	Label label_player_hp		= new Label("", Game.GENERATED_LABEL_STYLE_GOTTHARD_LITTLE_TEXT);
	
	ProgressBar bar_hp_player	= new ProgressBar(0, Game.profile.get_playerMaxHP(), 1, false, Game.NEUTRALIZER_UI);
	
	public boolean isPadDefined = false;
	
	public TableInGamePlayerHealth() {
		this(Game.NEUTRALIZER_UI);
	}

	public TableInGamePlayerHealth(Skin skin) {
		super(skin);
		
		this.add(new Label("Player", Game.GENERATED_LABEL_STYLE_GOTTHARD_LITTLE_TEXT)).padTop(GuiBase.getRatio(1f/48f));
		this.row();
		
		this.add(bar_hp_player).growX().uniformX().pad(Gdx.graphics.getWidth() / 48f).padTop(GuiBase.getRatio(1f/64f))
				.padBottom(GuiBase.getRatio(1f/64f));
		this.row();	
		
		this.add(label_player_hp).expandX().center();
		
		bar_hp_player.setColor(Color.GREEN);
		bar_hp_player.setValue(Game.profile.get_playerHP());
		label_player_hp.setText("" + Game.profile.get_playerHP() + "/" + Game.profile.get_playerMaxHP());
	}
	
	protected void updatePlayerHp() {
		if(Game.CORE.getPlayer() != null && !Game.CORE.getPlayer().isDead()) {
			label_player_hp.setText(Game.CORE.getPlayer().getHP() + "/" + Game.CORE.getPlayer().getMaxHP());
			bar_hp_player.setValue(Game.CORE.getPlayer().getHP());
			bar_hp_player.setColor(1f - (float) Game.CORE.getPlayer().getHP() / (float) Game.CORE.getPlayer().getMaxHP(),
					(float) Game.CORE.getPlayer().getHP() / (float) Game.CORE.getPlayer().getMaxHP(), 0, 1);
		} else {
			label_player_hp.setText("Player is dead");
		}
	}
	
	@Override
	public void act(float delta) {
		super.act(delta);
		
		updatePlayerHp();
	}
}
