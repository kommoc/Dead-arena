package net.kommocgame.src.DeadArena.entity.props;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import net.kommocgame.src.Loader;
import net.kommocgame.src.entity.props.EntityProp;
import net.kommocgame.src.world.World;

public class SProp_rust_02 extends EntityProp {

	public SProp_rust_02(World world, SpriteBatch batch) {
		this(world, batch, 0, 0);
	}

	public SProp_rust_02(World world, SpriteBatch batch, float x, float y) {
		this(world, batch, x, y, Loader.objectsProps("rubbish_rust_02.png"), 100);
	}

	public SProp_rust_02(World world, SpriteBatch batch, float x, float y, Texture tex, int hp) {
		super(world, batch, x, y, tex, hp);
		
		this.setRect(20f, 10f);
		this.setTransparent(false);
		this.setTexture(Loader.objectsProps("rubbish_rust_02.png"), 8f);
		this.spriteToCenter();
	}
}
