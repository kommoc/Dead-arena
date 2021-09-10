package net.kommocgame.src.entity.component;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.reflect.ClassReflection;
import com.badlogic.gdx.utils.reflect.ReflectionException;
import com.brashmonkey.spriter.Timeline;
import com.brashmonkey.spriter.gdx.Drawer;

import net.kommocgame.src.entity.EntityBase;
import net.kommocgame.src.entity.EntityLiving;
import net.kommocgame.src.entity.props.EntityProp;
import net.kommocgame.src.render.GameManager;
import net.kommocgame.src.render.RenderEngine;
import net.kommocgame.src.render.entity.IFirePoint;
import net.kommocgame.src.render.entity.RenderEntity;
import net.kommocgame.src.render.entity.RenderEntityLiving;
import net.kommocgame.src.render.entity.RenderProp;

public class CompSprite implements Component {

	public Sprite sprite;
	public RenderEntity render;
	
	public void setRender(EntityBase par1) {
		try {
			render = (RenderEntity) ClassReflection.getConstructor(GameManager.instance.getEntityInstance(par1), par1.getClass(), SpriteBatch.class, Drawer.class)
					.newInstance(par1, par1.getSpriteBatch(), RenderEngine.drawer_world);
		} catch (ReflectionException e1) {
			e1.printStackTrace();
			System.out.println("CompSprite.setRender() ### Not found current render!");
		} catch (NullPointerException e1) {
			e1.printStackTrace();
			
			try {
				if(par1 instanceof EntityLiving) {
					render = (RenderEntity) ClassReflection.getConstructor(RenderEntityLiving.class, EntityLiving.class,
							SpriteBatch.class, Drawer.class).newInstance(par1, par1.getSpriteBatch(), RenderEngine.drawer_world);
					System.out.println("			### EntityLiving");
				} else if(par1 instanceof EntityProp) {
					render = (RenderEntity) ClassReflection.getConstructor(RenderProp.class, EntityProp.class, SpriteBatch.class, Drawer.class)
							.newInstance(par1, par1.getSpriteBatch(), RenderEngine.drawer_world);
					System.out.println("			### EntityProp");
				} else {
					render = (RenderEntity) ClassReflection.getConstructor(RenderEntity.class, EntityBase.class, SpriteBatch.class, Drawer.class)
							.newInstance(par1, par1.getSpriteBatch(), RenderEngine.drawer_world);
					System.out.println("			### EntityBase");
				}
			} catch (ReflectionException e) {
				e.printStackTrace();
				System.out.println("CompSprite.setRender() ### set dynamic render!");
			}
		}
		
		System.out.println(render);
	}
	
	/** Return the fire point of scml sprite. */
	public Timeline.Key.Object getFirePoint() {
		if(render instanceof IFirePoint) {
			return ((IFirePoint)render).getFirePoint();
		}
		
		return null;
	}
}
