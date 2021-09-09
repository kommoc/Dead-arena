package net.kommocgame.src.DeadArena.render;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.brashmonkey.spriter.gdx.Drawer;

import net.kommocgame.src.DeadArena.entity.Bunker;
import net.kommocgame.src.render.entity.RenderEntity;

public class RenderBunker extends RenderEntity {
	
	Bunker bunker;
	
	public RenderBunker(Bunker par1, SpriteBatch batch, Drawer drawer) {
		super(par1, batch, drawer);
		bunker = par1;
		bunker.getSprite().setSize(12, 12);
		bunker.getSprite().setOriginCenter();
	}
	
	@Override
	public void render(OrthographicCamera camera) {
		if(!frustumCheck(camera))
			return;
		
		super.render(camera);
	}
}
