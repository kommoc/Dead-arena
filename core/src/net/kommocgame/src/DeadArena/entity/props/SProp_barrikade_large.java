package net.kommocgame.src.DeadArena.entity.props;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import net.kommocgame.src.Loader;
import net.kommocgame.src.entity.props.EntityProp;
import net.kommocgame.src.world.World;

public class SProp_barrikade_large extends EntityProp {

	public SProp_barrikade_large(World world, SpriteBatch batch) {
		this(world, batch, 0, 0);
	}

	public SProp_barrikade_large(World world, SpriteBatch batch, float x, float y) {
		this(world, batch, x, y, Loader.objectsProps("barrikade_01.png"), 100);
	}

	public SProp_barrikade_large(World world, SpriteBatch batch, float x, float y, Texture tex, int hp) {
		super(world, batch, x, y, tex, hp);
		
		this.setRect(7f, 23f);
		this.setTransparent(false);
		this.setTexture(Loader.objectsProps("barrikade_01.png"), 5f);
		this.spriteToCenter();
	}

}
