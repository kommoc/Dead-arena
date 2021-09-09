package net.kommocgame.src.world.physics;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2D;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.RayCastCallback;
import com.badlogic.gdx.physics.box2d.World;

import box2dLight.RayHandler;

public class Box2dWorld {
	
	private final World box2dWorld;
	public RayHandler guiRayHandler;
	public RayHandler gameRayHandler;
	public Box2DDebugRenderer debugRenderer;
	
	public Box2dWorld() {
		Box2D.init();
		box2dWorld = new World(new Vector2(0, 0), false);
		guiRayHandler = new RayHandler(box2dWorld);
		gameRayHandler = new RayHandler(box2dWorld);
		debugRenderer = new Box2DDebugRenderer();
		
		//box2dWorld.step(1/60f, 6, 2);
	}
	
	public World getBox2d() {
		//System.out.println("Box2dWorld.getBox2dWorld() ### THREAD: " + Thread.currentThread().getName());
		return box2dWorld;
	}
	
	public void rayCast(RayCastCallback callback, Vector2 point1, Vector2 point2) {
		box2dWorld.rayCast(callback, point1, point2);
	}

}
