package net.kommocgame.src.render.entity;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.brashmonkey.spriter.gdx.Drawer;
import com.github.czyzby.kiwi.util.gdx.scene2d.Alignment;

import net.kommocgame.src.Game;
import net.kommocgame.src.entity.props.EntityItem;

public class RenderItem extends RenderEntity {
	
	private EntityItem entityItem;
	private Label line_item;
	
	public RenderItem(EntityItem par1, SpriteBatch batch, Drawer drawer) {
		super(par1, batch, drawer);
		line_item = new Label("Item->", Game.COMMODORE_64_UI);
		line_item.setFontScale(0.65f);
		entityItem = par1;
	}
	
	@Override
	public void render(OrthographicCamera camera) {
		if(!frustumCheck(camera))
			return;
		
		entity.getSprite().setPosition(entity.getPosition().x - entity.getSprite().getOriginX(), 
				entity.getPosition().y - entity.getSprite().getOriginY());
		entity.getSprite().setRotation(entity.getRotation());
		
		entityItem.getSprite().draw(renderBatch);
	}
	
	public void renderInfo(SpriteBatch debugBatch) {
		//lineHp.setText("HP: " + entityItem.compHP.hp);
		//lineHp.setPosition(entityItem.compPhysics.body.getPosistion().x / scale, entityItem.compPhysics.body.getPosistion().y  / scale + 35, Alignment.CENTER.get());
		line_item.setText(entityItem.getItemStack().getItem().toString());
		line_item.setPosition(entityItem.compPhysics.body.getPosistion().x / scale - 15, entityItem.compPhysics.body.getPosistion().y  / scale + 25, Alignment.CENTER.get());
		
		//lineHp.draw(debugBatch, 1f);
		line_item.draw(debugBatch, 1f);
	}
}
