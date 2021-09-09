package net.kommocgame.src.DeadArena.render;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.brashmonkey.spriter.gdx.Drawer;

import net.kommocgame.src.Game;
import net.kommocgame.src.Loader;
import net.kommocgame.src.DeadArena.entity.Tree;
import net.kommocgame.src.render.GameSprite;
import net.kommocgame.src.render.entity.RenderEntity;

public class RenderTree extends RenderEntity {
	
	Tree tree;
	GameSprite tree_crone;
	
	int type;
	
	public RenderTree(Tree par1, SpriteBatch batch, Drawer drawer) {
		super(par1, batch, drawer);
		this.tree = (Tree) par1;
		
		setType();
	}
	
	@Override
	public void render(OrthographicCamera camera) {
		if(tree.getType() != type)
			this.setType();
		if(tree.getCroneSize() != tree_crone.getScaleX())
			this.tree_crone.setScale(tree.getCroneSize());
		
		if(!frustumCheck(camera))
			return;
		
		float distance = tree.getPosition().dst(tree.worldObj.game.mainCamera.position.x, tree.worldObj.game.mainCamera.position.y);
		float alpha = distance <= 10 ? 0.9f - (1f - distance / 10f) <= 0.35f ? 0.35f : 0.9f - (1f - distance / 10f)  : 0.9f;
		
		tree_crone.setPosition(tree.getPosition().x - tree_crone.getOriginX(), tree.getPosition().y - tree_crone.getOriginY());
		tree_crone.setRotation(tree.getRotation());
		
		super.render(camera);
		tree_crone.draw(renderBatch, alpha);
	}
	
	@Override
	public boolean frustumCheck(OrthographicCamera camera) {
		return camera.frustum.boundsInFrustum(entity.getPosition().x, entity.getPosition().y, 0,
				tree_crone.getWidth() / 1.1f, tree_crone.getHeight() / 1.1f, 0);
	}
	
	private void setType() {
		type = (tree.getType() != 0 ? tree.getType() : tree.getType() + 1);
		
		if(tree_crone != null)
			tree_crone.set(new GameSprite(Loader.objectsProps("trees/tree" + type + ".png")));
		else tree_crone = new GameSprite(Loader.objectsProps("trees/tree" + type + ".png"));
		
		tree_crone.setSize(tree_crone.getWidth() * Game.SCALE_WORLD_VALUE_FINAL, tree_crone.getHeight() * Game.SCALE_WORLD_VALUE_FINAL);
		tree_crone.setOrigin(tree_crone.getWidth() / 2f, tree_crone.getHeight() / 2f);
		tree_crone.setPosition(tree.getPosition().x, tree.getPosition().y);
	}
}
