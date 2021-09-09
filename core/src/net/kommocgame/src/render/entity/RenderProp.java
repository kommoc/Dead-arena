package net.kommocgame.src.render.entity;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.brashmonkey.spriter.gdx.Drawer;

import net.kommocgame.src.entity.props.EntityProp;

public class RenderProp extends RenderEntity {
	
	public RenderProp(EntityProp par1, SpriteBatch batch, Drawer drawer) {
		super(par1, batch, drawer);
		
		this.disableShadow();
	}
	
	public void render(OrthographicCamera camera) {
		if(!frustumCheck(camera) || !entity.isSpriteRendering())
			return;
		
		entity.getSprite().setPosition(entity.getPosition().x - entity.getSprite().getOriginX(), 
				entity.getPosition().y - entity.getSprite().getOriginY());
		entity.getSprite().setRotation(entity.getRotation());
		
		entity.compSprite.sprite.draw(renderBatch);
	}

}
