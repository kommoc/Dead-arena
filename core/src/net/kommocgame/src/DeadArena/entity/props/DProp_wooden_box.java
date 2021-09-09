package net.kommocgame.src.DeadArena.entity.props;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;

import net.kommocgame.src.Loader;
import net.kommocgame.src.entity.props.EntityProp;
import net.kommocgame.src.world.World;

public class DProp_wooden_box extends EntityProp {

	public DProp_wooden_box(World world, SpriteBatch batch) {
		this(world, batch, 0, 0);
	}

	public DProp_wooden_box(World world, SpriteBatch batch, float x, float y) {
		this(world, batch, x, y, Loader.objectsProps("box_wooden_01.png"), 100);
	}

	public DProp_wooden_box(World world, SpriteBatch batch, float x, float y, Texture tex, int hp) {
		super(world, batch, x, y, tex, hp);
		
		this.setRect(4f, 4f);
		this.setTransparent(false);
		this.setTexture(Loader.objectsProps("box_wooden_01.png"), 6f);
		this.spriteToCenter();
		
		this.getDefinition().getBody().setType(BodyType.DynamicBody);
		this.getDefinition().setDensity(0.1f);
	}
}
