package net.kommocgame.src.render;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.utils.Array;
import com.brashmonkey.spriter.SCMLReader;
import com.brashmonkey.spriter.gdx.AtlasLoader;
import com.brashmonkey.spriter.gdx.Drawer;
import com.brashmonkey.spriter.gdx.Loader;
import com.github.czyzby.kiwi.util.gdx.scene2d.Alignment;

import net.kommocgame.src.Game;
import net.kommocgame.src.debug.DebugAStarGrid;
import net.kommocgame.src.debug.DebugEditor;
import net.kommocgame.src.debug.DebugEntityAI;
import net.kommocgame.src.debug.DebugWorld;
import net.kommocgame.src.entity.EntityBase;
import net.kommocgame.src.entity.EntityFX;
import net.kommocgame.src.entity.particle.EntityProjectile;
import net.kommocgame.src.gui.GuiManager;
import net.kommocgame.src.layer.EnumGameLayer;
import net.kommocgame.src.world.World;

public class RenderEngine {
	
	private World world;
	
	private SpriteBatch spriteBatch;
	private SpriteBatch guiBatch;
	private SpriteBatch debugBatch;
	private GuiManager guiManager;
	private ShapeRenderer shapeRenderer;
	
	private ImmutableArray<Entity> entityList;
	private OrthographicCamera camera;
	
	public static Vector2 	debug_line_start = new Vector2(0, 0);
	public static Vector2 	debug_line_end = new Vector2(0, 0);
	public static DebugAStarGrid 	debug_AStar;
	public static DebugEntityAI 	debug_ai;
	public static DebugEditor 		debug_editor;
	public static DebugWorld		debug_world;
	
	public static Drawer drawer_world;
	public static Drawer drawer_gui;
	static SCMLReader reader;
	Loader loader;
	
	Label label_fps_counter;
	boolean vsync = false;
	
	public RenderEngine(World world, SpriteBatch batch, GuiManager guiManager, OrthographicCamera cam) {
		this.world = world;
		spriteBatch = batch;
		guiBatch = new SpriteBatch();
		this.guiManager = guiManager;
		camera = cam;
		
		debugBatch = new SpriteBatch();
		debugBatch.getTransformMatrix().scale(Game.SCALE_WORLD_VALUE_FINAL, Game.SCALE_WORLD_VALUE_FINAL, Game.SCALE_WORLD_VALUE_FINAL);
		System.out.println("RenderEngine(new instance) ### cam: " + cam.zoom);
		
		entityList = world.getEngine().getEntities();
	}
	
	public void init() {
		debugBatch.getTransformMatrix().setToScaling(Game.SCALE_WORLD_VALUE_FINAL, Game.SCALE_WORLD_VALUE_FINAL, Game.SCALE_WORLD_VALUE_FINAL);
		debugBatch.setProjectionMatrix(camera.combined);
		
		Gdx.gl.glClearColor(0, 0, 0, 0);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		spriteBatch.begin();
		this.renderWorld();
		renderLayers(spriteBatch);
		spriteBatch.end();
		
		//System.out.println("RenderEngine.init() ### renderCalls: " + spriteBatch.renderCalls);	//XXX PRINTLN
		//System.out.println("RenderEngine.init() ### maxSpritesInBatch: " + spriteBatch.maxSpritesInBatch);
		//System.out.println("RenderEngine.init() ### totalRenderCalls: " + spriteBatch.totalRenderCalls);
		
		debugBatch.begin();
		if(Game.profile.settings_debug_text()) 
			renderInfo();
		debugBatch.end();
		
		//TO CREATE DISPOSE!!!
		if(loader != null && !loader.isDisposed()) {
			//loader.dispose();
		}
		
		if(Game.profile.settings_debug_bounds())
			world.getBulletDebug(camera.combined);
		
		if(vsync != Game.profile.settings_vsync()) {
			vsync = Game.profile.settings_vsync();
			Gdx.app.getGraphics().setVSync(vsync);
		}
		
		if(shapeRenderer != null && Game.profile.settings_debug_bounds() && world.getLevel() != null) { //TODO
			shapeRenderer.begin();
			shapeRenderer.setProjectionMatrix(camera.combined);
			
			shapeRenderer.setColor(Color.RED);
			shapeRenderer.line(debug_line_start, debug_line_end);
			
			debug_editor.render();
			debug_AStar.render();
			debug_world.renderDebugWorld();
			debug_ai.renderShape();
			
			shapeRenderer.end();
		}
		
		if(guiManager.getStage() != null)
			guiManager.renderGui();					// MAY INPUT
		
		/***** DEBUG_TEST *****/
		if(debugBatch.getTransformMatrix().getScaleX() != Game.SCALE_WORLD_VALUE)
			debugBatch.getTransformMatrix().setToScaling(Game.SCALE_WORLD_VALUE, Game.SCALE_WORLD_VALUE, Game.SCALE_WORLD_VALUE);
		
		if(label_fps_counter != null) {
			if(Game.profile.settings_debug_fpsCounter()) {
				label_fps_counter.setVisible(true);
				drawFPSCounter();
			} else {
				label_fps_counter.setVisible(false);
			}
		}
		//debugBatch.setTransformMatrix(camera.combined);
		/***** DEBUG_TEST *****/		
	}
	
	void drawFPSCounter() {
		guiManager.guiBatch.begin();
		
		int fps = Gdx.graphics.getFramesPerSecond();
		label_fps_counter.setText("FPS: " + fps);
		
		if(fps > 50) {
			label_fps_counter.setColor(Color.GREEN);
		} else if(fps > 30 && fps <= 50) {
			label_fps_counter.setColor(Color.YELLOW);
		} else if(fps <= 30) {
			label_fps_counter.setColor(Color.RED);
		}
		
		label_fps_counter.setPosition(0, Gdx.graphics.getHeight(), Alignment.TOPLEFT.get());
		
		if(guiManager.getStage().getActors().size > 0 && guiManager.getStage().getActors().peek() instanceof Group &&
				((Group)guiManager.getStage().getActors().peek()).getChildren().peek() != label_fps_counter) {
			((Group)guiManager.getStage().getActors().peek()).getChildren().insert
					(((Group)guiManager.getStage().getActors().peek()).getChildren().size, label_fps_counter);
		}
		
		guiManager.guiBatch.end();
	}
	
	public void renderWorld() {
		if(world.level != null)
			world.level.terrain.render();
	}
	
	public RenderEngine setShapeRenderer(ShapeRenderer renderer) {
		this.shapeRenderer = renderer;
		shapeRenderer.setAutoShapeType(true);
		
		debug_AStar 	= new DebugAStarGrid(shapeRenderer, debugBatch);
		debug_ai 		= new DebugEntityAI(shapeRenderer, debugBatch, world);
		debug_editor	= new DebugEditor(shapeRenderer);
		debug_world		= new DebugWorld(shapeRenderer);
		return this;
	}
	
	/** Return the Entity loaded from file. */
	public static com.brashmonkey.spriter.Entity getSCMLData(String entity_name) {
		return reader.getData().getEntity(entity_name);
	}
	
	/** Load entity sprites. */	//TODO DISPOSE RESOURSES!!!
	public void loadGameAssets() {
		FileHandle fileHandle = net.kommocgame.src.Loader.getGameFile("android/assets/SCML/sprite_file.scml");
		reader = new SCMLReader(fileHandle.read());
		
		System.out.println("RenderEngine.loadGameAssets() ### fileHandle exist: " + fileHandle.exists());
		try {
			loader = new Loader(reader.getData());
			drawer_world = new Drawer(loader, spriteBatch, shapeRenderer);
			drawer_gui = new Drawer(loader, guiManager.guiBatch, shapeRenderer);
			loader.load(fileHandle.file());
			
			label_fps_counter = new Label("FPS: ", Game.GENERATED_LABEL_STYLE_GOTTHARD_LITTLE_TEXT_WHITE);
			label_fps_counter.setTouchable(Touchable.disabled);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		FileHandle imagesDir = net.kommocgame.src.Loader.getGameFile("android/assets/fx/texfx/");
		
		EntityFX.fx_explosion_1.load(net.kommocgame.src.Loader.getGameFile("android/assets/fx/FX_explosion_1.json"), imagesDir);
		EntityFX.fx_smoke_1.load(net.kommocgame.src.Loader.getGameFile("android/assets/fx/FX_smoke_1.json"), imagesDir);
		//EntityFX.fx_shoot_1.load(net.kommocgame.src.Loader.getGameFile("assets/fx/FX_shoot_1.json"), imagesDir);
		EntityFX.fx_shoot_bloom.load(net.kommocgame.src.Loader.getGameFile("android/assets/fx/FX_shoot_bloom.json"), imagesDir);
		EntityFX.fx_shoot_smoke.load(net.kommocgame.src.Loader.getGameFile("android/assets/fx/FX_shoot_smoke.json"), imagesDir);
		EntityFX.fx_bullet_1_16.load(net.kommocgame.src.Loader.getGameFile("android/assets/fx/FX_bullet_1_16.json"), imagesDir);
		EntityFX.fx_bullet_2_16.load(net.kommocgame.src.Loader.getGameFile("android/assets/fx/FX_bullet_2_16.json"), imagesDir);
		EntityFX.fx_bullet_1.load(net.kommocgame.src.Loader.getGameFile("android/assets/fx/FX_bullet_1.json"), imagesDir);
		EntityFX.fx_blood_red_1.load(net.kommocgame.src.Loader.getGameFile("android/assets/fx/FX_blood_red_1.json"), imagesDir);
		EntityFX.fx_blood_red_2.load(net.kommocgame.src.Loader.getGameFile("android/assets/fx/FX_blood_red_2.json"), imagesDir);
		
		/*
		EntityFX.fx_explosion_1.load(net.kommocgame.src.Loader.getGameFile("assets/fx/FX_explosion_1.json"),	Game.FX_ATLAS);
		EntityFX.fx_smoke_1.load(net.kommocgame.src.Loader.getGameFile("assets/fx/FX_smoke_1.json"),			Game.FX_ATLAS);
		EntityFX.fx_shoot_1.load(net.kommocgame.src.Loader.getGameFile("assets/fx/FX_shoot_1.json"),			Game.FX_ATLAS);
		EntityFX.fx_shoot_bloom.load(net.kommocgame.src.Loader.getGameFile("assets/fx/FX_shoot_bloom.json"),	Game.FX_ATLAS);
		EntityFX.fx_shoot_smoke.load(net.kommocgame.src.Loader.getGameFile("assets/fx/FX_shoot_smoke.json"),	Game.FX_ATLAS);
		EntityFX.fx_bullet_1_16.load(net.kommocgame.src.Loader.getGameFile("assets/fx/FX_bullet_1_16.json"),	Game.FX_ATLAS);
		EntityFX.fx_bullet_2_16.load(net.kommocgame.src.Loader.getGameFile("assets/fx/FX_bullet_2_16.json"),	Game.FX_ATLAS);
		EntityFX.fx_bullet_1.load(net.kommocgame.src.Loader.getGameFile("assets/fx/FX_bullet_1.json"),			Game.FX_ATLAS);
		EntityFX.fx_blood_red_1.load(net.kommocgame.src.Loader.getGameFile("assets/fx/FX_blood_red_1.json"),	Game.FX_ATLAS);
		EntityFX.fx_blood_red_2.load(net.kommocgame.src.Loader.getGameFile("assets/fx/FX_blood_red_2.json"),	Game.FX_ATLAS);
		*/
	}
	
	public void renderLayers(SpriteBatch batch) {
		for(int i = 1; i <= EnumGameLayer.values().length; i++) {
			if(i == EnumGameLayer.Default.getPriority() && Game.profile.settings_shadows())
				this.renderShadows(batch);
			
			this.renderSprites(EnumGameLayer.getEnum(i), batch);
			
			if(i == EnumGameLayer.Entity.getPriority())	//BADCODE TEST TODO
				this.renderParticles(EnumGameLayer.getEnum(i), batch);
		}
	}
	
	public void renderShadows(SpriteBatch batch) {
		for(int i = 0; i < entityList.size(); i++) {
			if(entityList.get(i) instanceof EntityBase) {
				EntityBase entity = (EntityBase)entityList.get(i);
				
				entity.compSprite.render.renderShadow(camera);
			}
		}
	}
	
	public void renderSprites(EnumGameLayer layer, SpriteBatch batch) {
		//22.09.18 test "layer_index"
		for(int i = 0; i < entityList.size(); i++) {
			if(entityList.get(i) instanceof EntityBase) {
				EntityBase entity = (EntityBase)entityList.get(i);
				
				if(entity.compLayer.getEnum() == layer)
					entity.compSprite.render.render(camera);
			}
		}
	}
	
	private void renderParticles(EnumGameLayer layer, SpriteBatch batch) {
		//System.out.println("RenderParticles_START");
		
		/** WITH_ATLAS */
		/*for(int i = 0; i < world.getProjectileList().size; i++) {
			EntityProjectile projectile = world.getProjectileList().get(i);
			
			if(projectile.getLayer() == layer.getPriority()) {
				//System.out.println("RenderEngine.renderParticles() ### projectile_ID: " + projectile.getCurrentParticle().getID() +
				//		"; array[" + i + "]");
				projectile.setScale(Game.SCALE_WORLD_VALUE_FINAL * 2f);
				projectile.draw(batch, Gdx.graphics.getDeltaTime(), camera);
			}
		}*/
		
		for(int id = 1; id <= 10; id++) { // BADCODE RELATION BY ENTITY_FX;
			for(int i = 0; i < world.getProjectileList().size; i++) {
				EntityProjectile projectile = world.getProjectileList().get(i);
				
				if(projectile.getLayer() == layer.getPriority() && projectile.getDefaultParticle().getID() == id) {
					//System.out.println("RenderEngine.renderParticles() ### projectile_ID: " + projectile.getCurrentParticle().getID() +
					//		"; array[" + i + "]");
					projectile.setScale(Game.SCALE_WORLD_VALUE_FINAL * 2f);
					projectile.draw(batch, Gdx.graphics.getDeltaTime(), camera);
				}
			}
		}
		//System.out.println("RenderParticles_END");
	}
	
	public void renderInfo() {
		for(int i = 0; i < entityList.size(); i++) {
			if(entityList.get(i) instanceof EntityBase) {
				EntityBase entity = (EntityBase)entityList.get(i);
				
				entity.compSprite.render.renderInfo(debugBatch);
				/*
				try {
					ClassReflection.getMethod(entity.getComponent(CompSprite.class).render.getClass(), "renderInfo",
							SpriteBatch.class).invoke(entity.getComponent(CompSprite.class).render, debugBatch);
				} catch (ReflectionException e1) {
					e1.printStackTrace();
				}*/
			}
		}
		
		if(debug_AStar.getState())
			debug_AStar.renderAB();
	}
}
