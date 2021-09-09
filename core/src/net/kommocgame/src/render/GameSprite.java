package net.kommocgame.src.render;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;

import net.kommocgame.src.VecUtils;
import net.kommocgame.src.world.physics.BodyBase;

public class GameSprite extends Sprite {
	
	private Sprite attached_sprite = null;
	private float x_attach;
	private float y_attach;
	private float sqrt_val;
	private float angle_diff;
	private float angle_attach;
	
	private float x_offset = 0f;
	private float y_offset = 0f;
	private float x_absolute_offset = 0f;
	private float y_absolute_offset = 0f;
	
	private float angle_offset = 0f;
	
	private boolean setDef = false;
	private BodyBase body;
	
	public GameSprite() {}

	public GameSprite(Texture texture) {
		super(texture);
		setOriginCenter();
	}

	public GameSprite(TextureRegion region) {
		super(region);
		setOriginCenter();
	}

	public GameSprite(Sprite sprite) {
		super(sprite);
		setOriginCenter();
	}

	public GameSprite(Texture texture, int srcWidth, int srcHeight) {
		super(texture, srcWidth, srcHeight);
	}

	public GameSprite(Texture texture, int srcX, int srcY, int srcWidth, int srcHeight) {
		super(texture, srcX, srcY, srcWidth, srcHeight);
	}

	public GameSprite(TextureRegion region, int srcX, int srcY, int srcWidth, int srcHeight) {
		super(region, srcX, srcY, srcWidth, srcHeight);
	}
	
	/** Attached at choose sprite by his position. */
	public void attachToSprite(Sprite sprite) {
		attached_sprite = sprite;
		
		x_absolute_offset = sprite.getOriginX();
		y_absolute_offset = sprite.getOriginY();
		
		x_attach = this.getX() - sprite.getX();
		y_attach = this.getY() - sprite.getY();
		angle_attach = sprite.getRotation() - this.getRotation();
		
		sqrt_val = (float) Math.sqrt(x_attach * x_attach + y_attach * y_attach);
		angle_diff = VecUtils.getAngleAtPoint(x_attach, y_attach);
	}
	
	/** Set the position in local coordinates. */
	public void setLocalPos(float x, float y) {
		x_attach = x;
		y_attach = y;
		
		this.setOffset(x_offset, y_offset);
	}
	
	/** Offset position by local attachment. */
	public void setOffset(float x_offset, float y_offset) {
		this.x_offset = y_offset;	//OFFSET
		this.y_offset = x_offset;	//OFFSET
		
		sqrt_val = (float) Math.sqrt((x_attach + x_offset) * (x_attach + x_offset) + (y_attach + y_offset) * (y_attach + y_offset));
		angle_diff = VecUtils.getAngleAtPoint(x_attach + x_offset, y_attach + y_offset);
	}
	
	/** Offset of angle. In degrees. */
	public void setOffsetAngle(float angle) {
		this.angle_offset = angle;
	}
	
	@Override
	public void setSize (float width, float height) {
		super.setSize(width, height);
		this.setOriginCenter();
	}
	
	private float getAttach_x() {
		float val = sqrt_val * MathUtils.cosDeg(MathUtils.atan2(x_attach + x_offset, y_attach + y_offset) + getAttachAngle()) + x_absolute_offset;

		if(!setDef)
			return attached_sprite.getX() + val;
		else return body.getPosistion().x + val;
	}
	
	private float getAttach_y() {
		float val = sqrt_val * MathUtils.sinDeg(MathUtils.atan2(x_attach + x_offset, y_attach + y_offset) + getAttachAngle()) + y_absolute_offset;
		
		if(!setDef)
			return attached_sprite.getY() + val;
		else return body.getPosistion().y + val;
	}
	
	private float getAttachAngle() {
		return attached_sprite.getRotation() - angle_diff - 270f;
	}
	
	public void setBody(BodyBase body) {
		this.setDef = true;
		this.body = body;
	}
	
	@Override
	public void draw (Batch batch) {
		if(attached_sprite != null) {
			this.setPosition(getAttach_x(), getAttach_y());
			this.setRotation(attached_sprite.getRotation() - angle_attach + angle_offset);
			
			if(x_absolute_offset != attached_sprite.getOriginX() || y_absolute_offset != attached_sprite.getOriginY()) {
				x_absolute_offset = attached_sprite.getOriginX();
				y_absolute_offset = attached_sprite.getOriginY();
			}
		}
		
		super.draw(batch);
	}
}
