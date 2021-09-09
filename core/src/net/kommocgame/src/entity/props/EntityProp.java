package net.kommocgame.src.entity.props;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.PolygonShape;

import net.kommocgame.src.entity.EntityBase;
import net.kommocgame.src.world.DataObject;
import net.kommocgame.src.world.World;

public class EntityProp extends EntityBase {
	
	private EBodyType body_type;
	
	private float body_width = 1f;
	private float body_height = 1f;
	
	private float body_radius = 1f;
	
	public EntityProp(World world, SpriteBatch batch) {
		this(world, batch, 0, 0, null, 10);
	}
	
	public EntityProp(World world, SpriteBatch batch, float x, float y) {
		this(world, batch, x, y, null, 10);
	}
	
	public EntityProp(World world, SpriteBatch batch, float x, float y, Texture tex, int hp) {
		super(world, batch, x, y, tex, hp);
		setCircle(1f);
		this.flag_ignoreDamage = true;
		
		getAdditionalData().getParameters().add(new DataObject<EBodyType>("bounds_type", getBodyType()) {
			@Override
			public void setParameter(EBodyType par1) {
				setBodyType(par1);
			}

			@Override
			public EBodyType getParameter() {
				return getBodyType();
			}
		});
		
		getAdditionalData().getParameters().add(new DataObject<BodyType>("body_type", getDefinition().getBody().getType()) {
			@Override
			public void setParameter(BodyType par1) {
				getDefinition().getBody().setType(par1);
			}

			@Override
			public BodyType getParameter() {
				return getDefinition().getBody().getType();
			}
		});
		
		getAdditionalData().getParameters().add(new DataObject<Float>("border_width", getBorderWidth()) {
			@Override
			public void setParameter(Float par1) {
				setRectBorder(par1, getBorderHeight());				
			}

			@Override
			public Float getParameter() {
				return getBorderWidth();
			}
		});
		
		getAdditionalData().getParameters().add(new DataObject<Float>("border_height", getBorderHeight()) {
			@Override
			public void setParameter(Float par1) {
				setRectBorder(getBorderWidth(), par1);				
			}

			@Override
			public Float getParameter() {
				return getBorderHeight();
			}
		});
		
		getAdditionalData().getParameters().add(new DataObject<Float>("border_radius", getBorderRaduis()) {
			@Override
			public void setParameter(Float par1) {
				setRaduisBorder(par1);			
			}

			@Override
			public Float getParameter() {
				return getBorderRaduis();
			}
		});
		
		getAdditionalData().getParameters().add(new DataObject<Float>("texture_zoom", getSprite().getScaleX()) {
			@Override
			public void setParameter(Float par1) {
				setTextureZoom(par1);			
			}

			@Override
			public Float getParameter() {
				return getSprite().getScaleX();
			}
		});
		
		getAdditionalData().getParameters().add(new DataObject<Float>("body_density", getSprite().getScaleX()) {
			@Override
			public void setParameter(Float par1) {
				getDefinition().setDensity(par1);
				getDefinition().getBody().resetMassData();
			}

			@Override
			public Float getParameter() {
				return getDefinition().getDensity();
			}
		});
		
		getAdditionalData().getParameters().add(new DataObject<Boolean>("isTransparent", isTransparent()) {
			@Override
			public void setParameter(Boolean par1) {
				setTransparent(par1);		
			}

			@Override
			public Boolean getParameter() {
				return isTransparent();
			}
		});
		
		getAdditionalData().getParameters().add(new DataObject<Boolean>("isSpriteRendering", isTransparent()) {
			@Override
			public void setParameter(Boolean par1) {
				setRenderSprite(par1);		
			}

			@Override
			public Boolean getParameter() {
				return isSpriteRendering();
			}
		});
	}
	
	public void setBodyType(EBodyType type) {
		if(type == EBodyType.CIRCLE) {
			setCircle(1f);
		} else {
			setRect(2f, 2f);
		}
	}
	
	public void setCircle(float radius) {
		float b_density = getDefinition().getDensity();
		Vector2 vec = this.getPosition();
		float angle = this.getRotation();
		
		this.body_radius = radius;
		compPhysics.setCircle(this, radius, BodyType.StaticBody);
		
		this.body_type = EBodyType.CIRCLE;
		this.setPosition(vec.x, vec.y);
		this.setRotation(angle);
		
		this.getDefinition().getBody().resetMassData();
		this.getDefinition().setDensity(b_density);
	}
	
	public void setRect(float widht, float height) {
		float b_density = getDefinition().getDensity();
		Vector2 vec = this.getPosition();
		float angle = this.getRotation();
		
		this.body_width = widht;
		this.body_height = height;
		compPhysics.setSimpleRect(this, widht / 2f, height / 2f, BodyType.StaticBody);
		
		this.body_type = EBodyType.RECT;
		this.setPosition(vec.x, vec.y);
		this.setRotation(angle);
		
		this.getDefinition().getBody().resetMassData();
		this.getDefinition().setDensity(b_density);
	}
	
	public void setRectBorder(float widht, float height) {
		float b_density = getDefinition().getDensity();
		
		if(getDefinition().getShape() instanceof PolygonShape) {
			PolygonShape shape = (PolygonShape) getDefinition().getShape();
			
			this.body_width = widht;
			this.body_height = height;
			shape.setAsBox(widht / 2f, height / 2f);
			
			this.getDefinition().getBody().resetMassData();
			this.getDefinition().setDensity(b_density);
		}
	}
	
	public void setRaduisBorder(float raduis) {
		float b_density = getDefinition().getDensity();
		
		if(getDefinition().getShape() instanceof CircleShape) {
			CircleShape shape = (CircleShape) getDefinition().getShape();
			
			this.body_radius = raduis;
			shape.setRadius(raduis);
			
			this.getDefinition().getBody().resetMassData();
			this.getDefinition().setDensity(b_density);
		}
	}
	
	public void setTextureZoom(float zoom) {
		this.getSprite().setScale(zoom);
	}
	
	public float getBorderWidth() {
		return body_width;
	}
	
	public float getBorderHeight() {
		return body_height;
	}
	
	public float getBorderRaduis() {
		return body_radius;
	}
	
	public EBodyType getBodyType() {
		return body_type;
	}

}
