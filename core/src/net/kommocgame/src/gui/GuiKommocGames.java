package net.kommocgame.src.gui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.ui.Image;

import box2dLight.PointLight;
import net.kommocgame.src.Game;
import net.kommocgame.src.Loader;

public class GuiKommocGames extends GuiBase {
	
	public Image kommocImage = new Image(Loader.guiMiscIcons("gui_logo_512x512.png"));
	public long lastTime = System.currentTimeMillis();
	public long time = 2000;
	
	public int sqrt = 125;
	public float brightness = 0f;
	float x = 30;
	float y = 15;
	float f = 15f;
	
	public PointLight light;
	
	public GuiKommocGames(Game game) {
		super(game);
		System.out.println("	GuiKommocGames(new instance) ### gui KommocGames.");
		
		group_stage.addActor(kommocImage);
		this.setLeftSide(kommocImage, true);
		kommocImage.setSize(Gdx.graphics.getHeight() / 2, Gdx.graphics.getHeight() / 2);
	}
	
	public void drawGui(SpriteBatch batch) {
		group_stage.draw(batch, 1f);
		this.update(batch);
	}
	
	public void update(SpriteBatch batch) {
		kommocImage.setPosition(game.mainCamera.viewportWidth / 2, game.mainCamera.viewportHeight / 2, ALIGNMENT.CENTER.get());
		
		if(light == null) {
			light = new PointLight(game.world.physics.guiRayHandler, 16, Color.WHITE, 0, 0, 0);
		}
		
		if(lastTime + 2000 < System.currentTimeMillis() && !(lastTime + 3000 < System.currentTimeMillis())) {
			float f = 128 - MathUtils.random(256);
			
			float f1 = 128 - MathUtils.random(256);
			
			kommocImage.setPosition(kommocImage.getX() + f/32, kommocImage.getY()+ f1/32);
		}
		
		x -= Math.sqrt(sqrt * sqrt) / 32f;
		f = 15 - x;
		y = game.mainCamera.viewportHeight * game.mainCamera.zoom - f * f / 40f;
		
		light.setColor(f / 15f, f / 30f, 1f, 1f);
		
		if(x >= game.mainCamera.viewportWidth / 22f) {
			sqrt += 1;
			brightness = (30 - x) * 3;
		} else if(x <= game.mainCamera.viewportWidth / 10f) {
			sqrt -= 1;
			brightness -= x / 25f;
		}
		
		light.setDistance(brightness);
		
		light.setPosition(x, y);
		game.world.physics.guiRayHandler.setCombinedMatrix(game.mainCamera);
		game.world.physics.guiRayHandler.updateAndRender();
		if(lastTime + time < System.currentTimeMillis()) {
			System.out.println("	GuiKommocGames.update() ### set new gui.");
		}
	}
}
