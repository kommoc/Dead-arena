package net.kommocgame.src.DeadArena.entity;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;

import net.kommocgame.src.Game;
import net.kommocgame.src.Loader;
import net.kommocgame.src.entity.props.EntityProp;
import net.kommocgame.src.world.World;

public class Bunker extends EntityProp {
	
	public Bunker(World world, SpriteBatch batch) {
		this(world, batch, 0, 0);
	}

	public Bunker(World world, SpriteBatch batch, float x, float y) {
		this(world, batch, x, y, Loader.objectsUnits("building_bunker.png"), 1500);
	}

	public Bunker(World world, SpriteBatch batch, float x, float y, Texture tex, int hp) {
		super(world, batch, x, y, tex, hp);
		this.setMaxHP(Game.profile.get_bunkerMaxHP());
		this.setHP(Game.profile.get_bunkerHP());
		
		this.setShapeSize(6);
		this.compPhysics.body.definition.setType(BodyType.StaticBody);
	}

}
