package net.kommocgame.src.DeadArena.entity;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;

import net.kommocgame.src.Loader;
import net.kommocgame.src.entity.props.EntityProp;
import net.kommocgame.src.layer.EnumGameLayer;
import net.kommocgame.src.world.DataObject;
import net.kommocgame.src.world.World;

public class Tree extends EntityProp {
	
	private int type = 1;
	
	/** Float crone scale value. */
	private float crone_size = 1f;
	
	public Tree(World world, SpriteBatch batch) {
		this(world, batch, 0, 0);
	}

	public Tree(World world, SpriteBatch batch, float x, float y) {
		this(world, batch, x, y, Loader.objectsProps("trees/wood_penek.png"), 10);
	}

	public Tree(World world, SpriteBatch batch, float x, float y, Texture tex, int hp) {
		super(world, batch, x, y, tex, hp);
		compPhysics.body.definition.setType(BodyType.StaticBody);
		
		//RandomXS128 rand = new RandomXS128();
		//setRotation(rand.nextInt(360));
		getAdditionalData().getParameters().add(new DataObject<Integer>("tree_type", type) {
			@Override
			public void setParameter(Integer par1) {
				setType(par1);
			}
			
			@Override
			public Integer getParameter() {
				return getType();
			}
		});
		
		getAdditionalData().getParameters().add(new DataObject<Float>("tree_crone_size", getCroneSize()) {
			@Override
			public void setParameter(Float par1) {
				setCroneSize(par1);
			}
			
			@Override
			public Float getParameter() {
				return getCroneSize();
			}
		});
		
		this.setLayer(EnumGameLayer.HighLayer);
		this.flag_ignoreDamage = true;
	}
	
	@Override
	public void onUpdate(float par1) {
		super.onUpdate(par1);
		
	}
	
	/** Set the crone scale. */
	public Tree setCroneSize(float scale) {
		this.crone_size = scale;
		return this;
	}
	
	/** Return the scale of the crone tree. */
	public float getCroneSize() {
		return crone_size;
	}
	
	/** Set the tree type. */
	public Tree setType(int type) {
		this.type = type;
		return this;
	}
	
	/** Return the type of this tree. */
	public int getType() {
		return type;
	}
}
