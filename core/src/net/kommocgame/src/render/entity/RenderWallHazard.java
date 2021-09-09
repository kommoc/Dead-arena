package net.kommocgame.src.render.entity;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.brashmonkey.spriter.gdx.Drawer;
import com.github.czyzby.kiwi.util.gdx.scene2d.Alignment;

import net.kommocgame.src.entity.props.WallConcrete;

public class RenderWallHazard extends RenderEntity {
	
	//Не связан
	private WallConcrete prop;
	private Sprite sprite;
	
	public RenderWallHazard(WallConcrete par1, SpriteBatch batch, Drawer drawer) {
		super(par1, batch, drawer);
		prop = par1;
		sprite = prop.getSprite();
		
		sprite.setScale(8f, 8f);
	}
	
	@Override
	public void render(OrthographicCamera camera) {
		//if(!frustumCheck(camera))
		//	return;
		
		entity.getSprite().setPosition(entity.getPosition().x - entity.getSprite().getOriginX(), 
				entity.getPosition().y - entity.getSprite().getOriginY());
		entity.getSprite().setRotation(entity.getRotation());
		
		prop.compSprite.sprite.draw(renderBatch);
	}
	
	public void renderInfo(SpriteBatch debugBatch) {
		lineHp.setText("HP: " + entity.getHP());
		//System.out.println("VECTOR LINE x:" + lineHp.getX() +" y: " + lineHp.getY());
		lineHp.setPosition(prop.compPhysics.body.getPosistion().x / scale, prop.compPhysics.body.getPosistion().y  / scale + 35, Alignment.CENTER.get());
		
		lineHp.draw(debugBatch, 1f);
	}
	
}
