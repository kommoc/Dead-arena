package net.kommocgame.src.render.entity;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.physics.box2d.Shape.Type;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.brashmonkey.spriter.gdx.Drawer;
import com.github.czyzby.kiwi.util.gdx.scene2d.Alignment;

import net.kommocgame.src.Game;
import net.kommocgame.src.Loader;
import net.kommocgame.src.entity.EntityBase;

public class RenderEntity {

	public EntityBase entity;
	public SpriteBatch renderBatch;
	public Drawer drawer;
	
	Texture shadow_texture;
	Sprite sprite_shadow;
	
	protected float posX;
	protected float posY;
	
	public float scale = Game.SCALE_WORLD_VALUE_FINAL;
	public Label lineHp = new Label("HP: ", Game.NEUTRALIZER_UI);
	
	public RenderEntity(EntityBase par1, SpriteBatch batch, Drawer drawer) {
		entity = par1;
		this.renderBatch = batch;
		this.drawer = drawer;
		
		lineHp.setColor(Color.RED);
		this.enableShadow();
	}
	
	public void disableShadow() {
		sprite_shadow = null;
	}
	
	/** Set the shadow sprite for circle shape. */
	public void enableShadow() {
		if(sprite_shadow != null)
			return;
		
		if(entity.getDefinition().getShape().getType() == Type.Circle) {
			sprite_shadow = new Sprite(Loader.impact("shadow_circle.png"));
			sprite_shadow.setAlpha(0.25f);
			sprite_shadow.setOriginCenter();
			
			sprite_shadow.setSize(sprite_shadow.getWidth() * Game.SCALE_WORLD_VALUE_FINAL * entity.getShapeSize() * 1.5f,
					sprite_shadow.getHeight() * Game.SCALE_WORLD_VALUE_FINAL * entity.getShapeSize() * 1.5f);
		} else {
			if(shadow_texture == null) {
				System.out.println("WARNING RenderEntity.setShadow() ### shadow texture is empty!");
				return;
			}
			
			sprite_shadow = new Sprite(shadow_texture);
			sprite_shadow.setAlpha(0.25f);
			sprite_shadow.setOriginCenter();
			
			sprite_shadow.setSize(sprite_shadow.getWidth() * Game.SCALE_WORLD_VALUE_FINAL,
					sprite_shadow.getHeight() * Game.SCALE_WORLD_VALUE_FINAL);
		}
		
		//rectangle implementation is empty, because sprite has set manually.
	}
	
	public void render(OrthographicCamera camera) {
		if(!frustumCheck(camera))
			return;
		
		entity.getSprite().setPosition(entity.getPosition().x - entity.getSprite().getOriginX(), 
				entity.getPosition().y - entity.getSprite().getOriginY());
		entity.getSprite().setRotation(entity.getRotation());
		
		entity.getSprite().draw(renderBatch);
	}
	
	public void renderShadow(OrthographicCamera camera) {
		if(!frustumCheck(camera))
			return;
		
		if(sprite_shadow != null) {
			if(entity.getDefinition().getShape().getType() == Type.Circle) {
				//System.out.println("PRINT");
				sprite_shadow.setScale(entity.getShapeSize());
			} else {
				//sprite_shadow.setScale(sprite_shadow.getWidth() * Game.SCALE_WORLD_VALUE_FINAL,
				//		sprite_shadow.getHeight() * Game.SCALE_WORLD_VALUE_FINAL);
			}
			
			sprite_shadow.setPosition(entity.getPosition().x - sprite_shadow.getOriginX(),
					entity.getPosition().y - sprite_shadow.getOriginY());
			sprite_shadow.setOriginCenter();
			sprite_shadow.setRotation(entity.getRotation());
			sprite_shadow.draw(renderBatch);
		}
	}
	
	/** Return true if object into the camera canvas. */
	public boolean frustumCheck(OrthographicCamera camera) {
		return camera.frustum.boundsInFrustum(entity.getPosition().x, entity.getPosition().y, 0,
				entity.getSprite().getWidth() / 1.5f, entity.getSprite().getHeight() / 1.5f, 0);
	}
	
	public void renderInfo(SpriteBatch debugBatch) {
		lineHp.setText("HP: " + entity.getHP());
		//System.out.println("VECTOR LINE x:" + lineHp.getX() +" y: " + lineHp.getY());
		lineHp.setPosition(entity.compPhysics.body.getPosistion().x / scale, entity.compPhysics.body.getPosistion().y  / scale + 35, Alignment.CENTER.get());
		
		lineHp.draw(debugBatch, 1f);
	}

}
