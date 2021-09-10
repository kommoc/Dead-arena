package net.kommocgame.src.entity.props;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonValue;
import com.badlogic.gdx.utils.Json.Serializable;
import com.badlogic.gdx.physics.box2d.MassData;
import com.badlogic.gdx.physics.box2d.PolygonShape;

import net.kommocgame.src.Loader;
import net.kommocgame.src.world.DataObject;
import net.kommocgame.src.world.World;

public class WallConcrete extends EntityProp {
	
	public PolygonShape bounds;
	
	public WallConcrete(World world, SpriteBatch batch, float x, float y) {
		this(world, batch, x, y, null, 10, 5, 5);
		
	}
	
	public WallConcrete(World world, SpriteBatch batch, float x, float y, float w, float h) {
		this(world, batch, x, y, null, 10, w, h);
		
	}

	public WallConcrete(World world, SpriteBatch batch, float x, float y, Texture tex, int hp, float w, float h) {
		super(world, batch, x, y, tex, hp);
		//this.compPhysics.setSimpleRect(w, h, BodyType.StaticBody);
		//bounds = compPhysics.body.figurePolygon;
		//compPhysics.body.setEntityLink(this);
		
		this.setRect(w, h);
		this.setTexture(Loader.objectsProps("wall_hazard.png"), 1f);
		
		this.flag_ignoreDamage = true;
		this.setTransparent(false);
	}
	
	@Override
	public void onUpdate(float par1) { }

}
