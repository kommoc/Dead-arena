package net.kommocgame.src.entity.component;

import java.util.Random;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.MathUtils;

import net.kommocgame.src.Function;

public class CompCamera implements Component {

	public OrthographicCamera camera;
	
	private Function func_shake_transition = new Function(130, Interpolation.circle);
	private Function func_shake_fade = new Function(500, Interpolation.exp5Out);
	private float camera_shake_current = 0f;
	
	private float cam_transition_x = 0f;
	private float cam_transition_y = 0f;
	
	private float cam_fade_x = 0f;
	private float cam_fade_y = 0f;
	private float cam_fade_prew_x = 0f;	
	private float cam_fade_prew_y = 0f;
	
	private float shake_x = 0f;
	private float shake_y = 0f;
	
	private float rect = 360f;
	
	private Random rand = new Random();
	
	public CompCamera(OrthographicCamera camera) {
		this.camera = camera;
	}
	
	public void setCamera(OrthographicCamera camera) {
		this.camera = camera;
	}
	
	public void updateCamera(float x, float y) {
		if(camera == null)
			return;
		
		//camera.position.x = x;
		//camera.position.y = y;
		
		if(cam_fade_x != 0 || cam_fade_y != 0) {
			cam_fade_x = cam_fade_prew_x - cam_fade_prew_x * func_shake_fade.getValue();
			cam_fade_y = cam_fade_prew_y - cam_fade_prew_y * func_shake_fade.getValue();
			
			if(func_shake_transition.ended()) {
				shake_x = cam_fade_x;
				shake_y = cam_fade_y;
			} else {
				shake_x = cam_fade_x - cam_transition_x * func_shake_transition.getValue();
				shake_y = cam_fade_y - cam_transition_y * func_shake_transition.getValue();
			}
		}
		
		float y1 = (camera.frustum.planePoints[2].y - camera.frustum.planePoints[0].y) / 2f;
		float x1 = (camera.frustum.planePoints[2].x - camera.frustum.planePoints[0].x) / 2f;
		
		if(x - x1 > -rect || x + x1 < rect) {
			camera.position.x = x + shake_x;
		}

		if(y - y1 > -rect || y + y1 < rect) {
			camera.position.y = y + shake_y;
		}
		
		if(x - x1 <= -rect) {
			camera.position.set(-rect + x1, camera.position.y, camera.position.z);
		} else if(x + x1 >= rect) {
			camera.position.set(65f - x1, camera.position.y, camera.position.z);
		}
		
		if(y - y1 <= -rect) {
			camera.position.set(camera.position.x, -rect + y1, camera.position.z);
		} else if(y + y1 >= rect) {
			camera.position.set(camera.position.x, rect - y1, camera.position.z);
		}
		
		func_shake_fade.init();
		func_shake_transition.init();
	}
	
	public void shakeCam(float shake) {
		if(camera == null)
			return;
		
		camera_shake_current = shake / 100f > 1f ? 1f : shake / 100f;
		
		func_shake_fade.reset();
		func_shake_fade.start();
		
		func_shake_transition.reset();
		func_shake_transition.start();
		
		float y = MathUtils.sinDeg(rand.nextInt(360)) * camera_shake_current;
		float x = MathUtils.cosDeg(rand.nextInt(360)) * camera_shake_current;
		
		//System.out.println("	CompCamera.shakeCam() ### X: " + x);
		//System.out.println("	CompCamera.shakeCam() ### Y: " + y);
		//System.out.println("	CompCamera.updateCamera() ### fade is end: " + func_shake_fade.ended());
		//System.out.println("	CompCamera.updateCamera() ### transition is end: " + func_shake_transition.ended());
		
		cam_fade_x = x;
		cam_fade_y = y;
		cam_fade_prew_x = cam_fade_x;
		cam_fade_prew_y = cam_fade_y;
		
		cam_transition_x = shake_x;
		cam_transition_x = shake_y;
	}
}
