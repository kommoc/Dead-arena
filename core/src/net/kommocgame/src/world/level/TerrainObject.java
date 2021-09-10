package net.kommocgame.src.world.level;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.collision.BoundingBox;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.github.czyzby.kiwi.util.gdx.scene2d.Alignment;

import net.kommocgame.src.Function;
import net.kommocgame.src.Game;
import net.kommocgame.src.world.AdditionalData;
import net.kommocgame.src.world.DataObject;
import net.kommocgame.src.world.WorldObject;

public class TerrainObject implements WorldObject {
	
	private Vector2 position;
	private AdditionalData userData;
	private int layer_index = -1;
	private int layer_position_id = -1;
	
	private Image image;
	private Texture texture;
	
	private float scale_x = 1f;
	private float scale_y = 1f;
	
	private float native_size_y;
	private float native_size_x;
	
	private float alpha = 1f;
	
	private String name = "_TERRAINOBJ";
	private LevelBase level;
	
	private boolean isDeleted = false;
	
	private long lastTime = 0;
	private long timer = 1000;
	private boolean setToDelete = false;
	private Function func_alpha = new Function(1000, Interpolation.linear);
	
	public TerrainObject(LevelBase lvl, Texture texture, Vector2 v) {
		this(lvl, texture, v.x, v.y);
	}
	
	public TerrainObject(LevelBase lvl, Texture texture, float x, float y) {
		this(lvl, texture, "DEFAULT_TERRAINOBJ", x, y);
	}
	
	public TerrainObject(LevelBase lvl, Texture texture, String name, float x, float y) {
		userData = new AdditionalData();
		//this.setLayerIndex(0);		XXX CHANGE!!!
		
		position = new Vector2(x, y);
		this.name = name;
		this.level = lvl;
		this.texture = texture;
		this.image = new Image(texture) {
			
			float x_begin, x_end, y_begin, y_end, value;
			
			@Override
			public void draw(Batch batch, float parentAlpha) {
				TerrainObject.this.update();
				
				if(this.frustumCheck(Game.instance.mainCamera))
					super.draw(batch, parentAlpha);
			}
			
			/** Return true if object into the camera canvas. */
			public boolean frustumCheck(OrthographicCamera camera) {
				value = getWidth() > getHeight() ? getWidth() : getHeight();
				
				x_begin = TerrainObject.this.getPosition().x;
				x_end = value / 1.4f;
				y_begin = TerrainObject.this.getPosition().y;
				y_end = value / 1.4f;
				
				return camera.frustum.boundsInFrustum(x_begin, y_begin, 0, x_end, y_end, 0);
			}
		};
		native_size_x = image.getWidth();
		native_size_y = image.getHeight();
		image.setPosition(x, y);
		
		getAdditionalData().getParameters().add(new DataObject<Integer>("layer_index", getLayerIndex()) {
			@Override
			public void setParameter(Integer par1) {
				setLayerIndex(par1);
				
				if(getLevel().isObjectsLoading() && getLayerIndex() != -1 && getLayerPositionID() != -1)
					getLevel().getLayersList().get(getLayerIndex()).getObjectsList().set(getLayerPositionID(), TerrainObject.this);
			}

			@Override
			public Integer getParameter() {
				return getLayerIndex();
			}
		});
		
		getAdditionalData().getParameters().add(new DataObject<Integer>("layer_position_id", getLayerPositionID()) {
			@Override
			public void setParameter(Integer par1) {
				setLayerPositionID(par1);
				
				if(getLevel().isObjectsLoading() && getLayerIndex() != -1 && getLayerPositionID() != -1)
					getLevel().getLayersList().get(getLayerIndex()).getObjectsList().set(getLayerPositionID(), TerrainObject.this);
			}

			@Override
			public Integer getParameter() {
				return getLayerPositionID();
			}
		});
		
		getAdditionalData().getParameters().add(new DataObject<Color>("color", getImage().getColor()) {
			@Override
			public void setParameter(Color par1) {
				getImage().setColor(par1);
			}

			@Override
			public Color getParameter() {
				return getImage().getColor();
			}
		});
		
		System.out.println("Zindex: " + getImage().getZIndex());
		System.out.println("posetZindex: " + getImage().getZIndex());
		
		System.out.println("	TerrainObject(new instance) ### getAssetFileName: " + Game.assetManager.getAssetFileName(texture));
	}
	
	public void setAlpha(float f) {
		alpha = f;
	}
	
	public Vector2 set(Vector2 v, Alignment alignment) {
		return this.set(v.x, v.y, alignment);
	}
	
	public Vector2 set(float x, float y, Alignment alignment) {
		image.setPosition(x, y, alignment.get());
		return position.set(x, y);
	}
	
	public void setScale(float f) {
		scale_x = f;
		scale_y = f;
		
		image.setSize(native_size_x * scale_x, native_size_y * scale_y);
	}
	
	public void setScaleX(float f) {
		scale_x = f;
		
		image.setSize(native_size_x * scale_x, native_size_y * scale_y);
	}
	
	public void setScaleY(float f) {
		scale_y = f;
		
		image.setSize(native_size_x * scale_x, native_size_y * scale_y);
	}
	
	@Override
	/** Set the layer index */
	public void setLayerIndex(int par1) {
		layer_index = par1;
	}
	
	/** Set the layer index, that this object will. */
	public void setLayerPositionID(int par1) {
		layer_position_id = par1;
	}
	
	/** At the end of timer terrainObject will be deleted. */
	public TerrainObject setDeleteTimer(long timer) {
		setToDelete = true;
		lastTime = System.currentTimeMillis();
		this.timer = timer;
		return this;
	}
	
	public Image getImage() {
		return image;
	}
	
	public Texture getTexture() {
		return texture;
	}
	
	public String getName() {
		return name;
	}
	
	public Vector2 getPosition() {
		return position;
	}
	
	public LevelBase getLevel() {
		return level;
	}
	
	@Override
	/** Return index layer, that this object in. */
	public int getLayerIndex() {
		//It works only after level save.
		if(layer_index != -1)
			return layer_index;
		
		for(LevelLayer layer : level.getLayersList()) {
			if(layer.getObjectsList().contains(this, false))
				return level.getLayersList().indexOf(layer, false);
		}
		
		System.out.println("TerrainObject.getLayerIndex() ### this TO haven't layer! ");
		return -1;
	}
	
	/** Return position in current layer. */
	public int getLayerPositionID() {
		//It works only after level save.
		if(layer_position_id != -1)
			return layer_position_id;
		
		if(getLayerIndex() == -1)
			return -1;
		
		return level.getLayersList().get(getLayerIndex()).getObjectsList().indexOf(this, false);
	}
	
	@Override
	public AdditionalData getAdditionalData() {
		return userData;
	}
	
	@Override
	public boolean isDeleted() {
		return isDeleted;
	}
	
	/** Update method. */
	public void update() {
		func_alpha.init();
		if(setToDelete && lastTime + timer <= System.currentTimeMillis() && !func_alpha.getState()) {
			func_alpha.start();
		}
		
		if(func_alpha.getState() || func_alpha.ended()) {
			getImage().getColor().a = 1f - func_alpha.getValue();
			
			if(getImage().getColor().a == 0 && !isDeleted) {
				System.out.println("TerrainObject.update() ### obj is deleted. size[" + level.terrain.objects.getChildren().size + "]");
				this.deleteObject();
			}
		}
	}
	
	@Deprecated
	public void render(SpriteBatch batch) {
		if(image != null) {
			image.draw(batch, alpha);
		}
	}

	@Override
	public void deleteObject() {
		level.world.getDeleteList().add(this);
	}

	@Override
	public void del() {
		if(isDeleted()) {
			System.out.println("		TerrainObject.del() ### trying to delete deleted instance.");
			return;
		}
		
		level.removeTerrainObj(this);
		isDeleted = true;
	}
}
