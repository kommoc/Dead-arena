package net.kommocgame.src.render;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.ObjectMap;

import net.kommocgame.src.Path;
import net.kommocgame.src.DeadArena.entity.Bunker;
import net.kommocgame.src.DeadArena.entity.Tree;
import net.kommocgame.src.DeadArena.entity.mobs.DAEntityMobZombie;
import net.kommocgame.src.DeadArena.entity.mobs.DAEntityMobZombieFast;
import net.kommocgame.src.DeadArena.entity.mobs.DAEntityMobZombieFat;
import net.kommocgame.src.DeadArena.render.RenderBunker;
import net.kommocgame.src.DeadArena.render.RenderDAEntityMobZombie;
import net.kommocgame.src.DeadArena.render.RenderDAEntityMobZombieFast;
import net.kommocgame.src.DeadArena.render.RenderDAEntityMobZombieFat;
import net.kommocgame.src.DeadArena.render.RenderTree;
import net.kommocgame.src.entity.EntityBase;
import net.kommocgame.src.entity.EntityLiving;
import net.kommocgame.src.entity.character.EntityPlayer;
import net.kommocgame.src.entity.props.EntityItem;
import net.kommocgame.src.entity.props.EntityProp;
import net.kommocgame.src.entity.props.WallConcrete;
import net.kommocgame.src.render.entity.RenderEntity;
import net.kommocgame.src.render.entity.RenderEntityLiving;
import net.kommocgame.src.render.entity.RenderItem;
import net.kommocgame.src.render.entity.RenderPlayer;
import net.kommocgame.src.render.entity.RenderProp;
import net.kommocgame.src.render.entity.RenderWallHazard;

public class GameManager {
	
	public static GameManager instance;
	public ObjectMap<Class<? extends EntityBase>, Class<? extends RenderEntity>> entityMap = new ObjectMap<Class<? extends EntityBase>, Class<? extends RenderEntity>>();
	
	public final float SCALE = (float)Gdx.graphics.getHeight() / 768f;
	
	/** 0 - default. 1 - crosshair. */
	public int cursor = 0;
	
	public GameManager() {
		instance = this;
		
		entityMap.put(EntityBase.class, RenderEntity.class);
		entityMap.put(EntityProp.class, RenderProp.class);
		
		entityMap.put(EntityLiving.class, RenderEntityLiving.class);
		entityMap.put(EntityPlayer.class, RenderPlayer.class);
		entityMap.put(DAEntityMobZombie.class, RenderDAEntityMobZombie.class);
		entityMap.put(DAEntityMobZombieFat.class, RenderDAEntityMobZombieFat.class);
		entityMap.put(DAEntityMobZombieFast.class, RenderDAEntityMobZombieFast.class);
		
		entityMap.put(Bunker.class, RenderBunker.class);
		
		entityMap.put(WallConcrete.class, RenderWallHazard.class);
		entityMap.put(Tree.class, RenderTree.class);
		
		entityMap.put(EntityItem.class, RenderItem.class);
	}
	
	/** Return .class object of instance. */
	public Class getEntityInstance(EntityBase par1) {
		if(entityMap.containsKey(par1.getClass())) {
			return entityMap.get(par1.getClass());
		} else {
			System.out.println("not find Entity: " + par1);
		}
		
		return null;
	}
	
	/** Return drawable instance. */
	public Drawable getDrawable(Texture texture) {
		return new TextureRegionDrawable(new TextureRegion(texture));
	}
	
	/** Set mouse cursor as arrow. */
	@Deprecated
	public void setCursorArrow() {	//TODO
		if(Gdx.app.getType() == Gdx.app.getType().Desktop) {
			/*cursor = 0;
			Pixmap pm = new Pixmap(Gdx.files.internal(Path.gui("img_cursor_4.png")));		//TODO
			
			Gdx.graphics.setCursor(Gdx.graphics.newCursor(pm, 0, 0));
			pm.dispose();*/
		}
	}
	
	/** Set mouse cursor as crosshair. */
	@Deprecated
	public void setCursorCrosshair() {	//TODO
		if(Gdx.app.getType() == Gdx.app.getType().Desktop) {
			cursor = 1;
			Pixmap pm = new Pixmap(Gdx.files.internal(Path.gui("crosshair.png")));			//TODO
			
			Gdx.graphics.setCursor(Gdx.graphics.newCursor(pm, 32, 32));
			pm.dispose();
		}
	}
}
