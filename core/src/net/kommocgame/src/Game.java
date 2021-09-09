package net.kommocgame.src;

import org.jrenner.smartfont.SmartFontGenerator;

import com.badlogic.ashley.core.Engine;
import com.badlogic.gdx.Application.ApplicationType;
import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ai.GdxAI;
import com.badlogic.gdx.ai.Logger;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

import net.kommocgame.src.DeadArena.core.DACore;
import net.kommocgame.src.DeadArena.core.DAProfile;
import net.kommocgame.src.control.InputHandler;
import net.kommocgame.src.control.KeyBinding;
import net.kommocgame.src.debug.CommandLine;
import net.kommocgame.src.editor.nodes.DHObjects;
import net.kommocgame.src.entity.EntityLiving;
import net.kommocgame.src.entity.IControllable;
import net.kommocgame.src.entity.component.CompCamera;
import net.kommocgame.src.gui.GuiManager;
import net.kommocgame.src.render.GameManager;
import net.kommocgame.src.render.RenderEngine;
import net.kommocgame.src.world.World;

public class Game extends ApplicationAdapter {
	public Engine engine;
	
	public static IControllable player;
	
	public static DACore CORE;
	
	public World world;
	public InputHandler keyHandler;
	public RenderEngine renderEngine;
	public SpriteBatch spriteBatch;
	public static ShapeRenderer shapeRenderer;
	public OrthographicCamera mainCamera;
	public GuiManager guiManager;
	public static DAProfile profile;
	public static AssetManager assetManager;
	public static CommandLine command_line;
	public static Logger logger = GdxAI.getLogger();
	
	public static DHObjects DH_OBJECTS;
	
	private static GameState GAME_STATE;
	
	public static BitmapFont FONT_CONSOLE_32;
	public static BitmapFont FONT_CONSOLE_48;
	public static BitmapFont FONT_CONSOLE_72;
	public static BitmapFont FONT_CONSOLE_120;
	public static BitmapFont FONT_CONSOLE_155;
	
	public static LabelStyle LABEL_STYLE_CONSOLE_32;
	public static LabelStyle LABEL_STYLE_CONSOLE_48;
	public static LabelStyle LABEL_STYLE_CONSOLE_72;
	public static LabelStyle LABEL_STYLE_CONSOLE_120;
	public static LabelStyle LABEL_STYLE_CONSOLE_155;
	
	public static Color			COLOR_TEXT			= Color.valueOf("#d5f3ef");
	public static Color			COLOR_TEXT_SELECTED = Color.valueOf("#eaef93");
	public static Color			COLOR_PANEL_INFO	= Color.valueOf("#9ACD32");
	public static Color			COLOR_BACKGROUND	= Color.valueOf("#b1b196");
	public static Color			COLOR_BUTTON		= Color.valueOf("#ffb977");
	public static Color			COLOR_NODE_TEXT		= Color.valueOf("#7FFFD4");
	
	public static float			GUI_NUMBER_PANEL_GAP;
	
	public static LabelStyle GENERATED_LABEL_STYLE_GOTTHARD_LARGE;
	public static LabelStyle GENERATED_LABEL_STYLE_GOTTHARD_LARGE_WHITE;
	public static LabelStyle GENERATED_LABEL_STYLE_GOTTHARD_BUTTON;
	public static LabelStyle GENERATED_LABEL_STYLE_GOTTHARD_BUTTON_WHITE;
	public static LabelStyle GENERATED_LABEL_STYLE_GOTTHARD_MID_TEXT;
	public static LabelStyle GENERATED_LABEL_STYLE_GOTTHARD_MID_TEXT_WHITE;
	public static LabelStyle GENERATED_LABEL_STYLE_GOTTHARD_LITTLE_TEXT;
	public static LabelStyle GENERATED_LABEL_STYLE_GOTTHARD_LITTLE_TEXT_WHITE;
	public static LabelStyle GENERATED_LABEL_STYLE_GOTTHARD_TINY_TEXT;
	public static LabelStyle GENERATED_LABEL_STYLE_GOTTHARD_TINY_TEXT_WHITE;
	
	public static Skin NEUTRALIZER_UI;
	public static Skin NEON_UI;
	public static Skin COMMODORE_64_UI;
	public static Skin KOMMOC_UI;
	public static TextureAtlas SCI_FI_ATLAS;
	public static TextureAtlas FX_ATLAS;
	
	public final int VIEWPORT_WIGHT_MIN = 800;
	public final int VIEWPORT_HEIGHT_MIN = 480;
	
	public final int VIEWPORT_WIGHT_MAX = 1920;
	public final int VIEWPORT_HEIGHT_MAX = 1200;
	
	public static float LIGHT_VALUE = 1f;
	
	public static float SCALE_GUI_VALUE;
	public static float SCALE_WORLD_VALUE;
	public static float SCALE_WORLD_VALUE_FINAL;
	public static float SCALE_WORLD_SPEED_VALUE = 73.8526376f;
	
	public static int GAME_WINDOW_STATE = 0;
	
	public static boolean isGameClosed = false;
	
	public static Game instance;
	
	@Override
	public void create () {
		instance = this;
		checkType();
		Game.profile = new DAProfile();
		
		engine = new Engine();
					System.out.println("Game(new instance) ### engine is created: " + (engine != null));
		mainCamera = new OrthographicCamera(1920, 1200);
					System.out.println("Game(new instance) ### main camera is created: " + (mainCamera != null));
		//keyHandler = new InputHandler(this);
		spriteBatch = new SpriteBatch();
					System.out.println("Game(new instance) ### spriteBatch is created: " + (spriteBatch != null));
		shapeRenderer = new ShapeRenderer();
					System.out.println("Game(new instance) ### shapeRenderer is created: " + (shapeRenderer != null));
		guiManager = new GuiManager(this);
					System.out.println("Game(new instance) ### guiManager is created: " + (guiManager != null));
		new GameManager();
					System.out.println("Game(new instance) ### new GameManager instance: " + (GameManager.instance != null));
		//Gdx.input.setInputProcessor(keyHandler);
		world = new World(this);
					System.out.println("Game(new instance) ### new world instance: " + (world != null));
		world.physics.getBox2d().setContactListener(world);
					System.out.println("Game(new instance) ### setContactListener (world)");
		world.physics.getBox2d().setContactFilter(world);
					System.out.println("Game(new instance) ### setContactFilter (world)");
		engine.addSystem(world);
					System.out.println("Game(new instance) ### world system added to engine: " + engine.getSystems().size());
		renderEngine = new RenderEngine(world, spriteBatch, guiManager, mainCamera).setShapeRenderer(shapeRenderer);
					System.out.println("Game(new instance) ### renderEngine is created: " + (renderEngine != null));
		command_line = new CommandLine(this);
					System.out.println("Game(new instance) ### command_line is created: " + (command_line != null));
		
		COMMODORE_64_UI = assetManager.get("assets/skin/Commodore_64_UI/commodore64ui/uiskin.json");
		SCI_FI_ATLAS = assetManager.get("assets/skin/Sci_fi/panels.atlas");
					System.out.println("Game(new instance) ### COMMODORE_64_UI & SCI_FI_ATLAS is created: " + (COMMODORE_64_UI != null && SCI_FI_ATLAS != null));
		
		Gdx.app.getGraphics().setVSync(false);
		GameManager.instance.setCursorArrow();
		System.out.println("Game(new instance) ### setCursorArrow()");
					
		
		//System.out.close();
		
	}
	
	public void setParams() {
		GAME_STATE = GameState.CONTINUE;
		
		keyHandler = new InputHandler(this);
		Gdx.input.setInputProcessor(keyHandler);
		
		FONT_CONSOLE_32 = assetManager.get("assets/font/console_32.fnt", BitmapFont.class);
		FONT_CONSOLE_48 = assetManager.get("assets/font/console_48.fnt", BitmapFont.class);
		FONT_CONSOLE_72 = assetManager.get("assets/font/console_72.fnt", BitmapFont.class);
		FONT_CONSOLE_120 = assetManager.get("assets/font/console_120.fnt", BitmapFont.class);
		FONT_CONSOLE_155 = assetManager.get("assets/font/console_155.fnt", BitmapFont.class);
		
		LABEL_STYLE_CONSOLE_32 = new LabelStyle(Game.FONT_CONSOLE_32, Color.WHITE);
		LABEL_STYLE_CONSOLE_48 = new LabelStyle(Game.FONT_CONSOLE_48, Color.WHITE);
		LABEL_STYLE_CONSOLE_72 = new LabelStyle(Game.FONT_CONSOLE_72, Color.WHITE);
		LABEL_STYLE_CONSOLE_120 = new LabelStyle(Game.FONT_CONSOLE_120, Color.WHITE);
		LABEL_STYLE_CONSOLE_155 = new LabelStyle(Game.FONT_CONSOLE_155, Color.WHITE);
		
		NEUTRALIZER_UI = assetManager.get("assets/skin/Neutralizer_UI/neutralizerui/neutralizer-ui.json");
		NEON_UI = assetManager.get("assets/skin/NeonUI/neonui/neon-ui.json");
		KOMMOC_UI = assetManager.get("assets/skin/Kommoc_UI/Kommoc_UI.json");
		
		FX_ATLAS = assetManager.get("assets/fx/atlas/tex_fx.atlas");
		
		SmartFontGenerator generator = new SmartFontGenerator();
		BitmapFont font_button = generator.createFont(Loader.getFontFile("gotthard"), "gotthard_button", (int) (Gdx.graphics.getWidth() / 16f));
		GENERATED_LABEL_STYLE_GOTTHARD_BUTTON = new LabelStyle(font_button, COLOR_TEXT_SELECTED);
		GENERATED_LABEL_STYLE_GOTTHARD_BUTTON_WHITE = new LabelStyle(font_button, Color.WHITE);
		
		BitmapFont font_text = generator.createFont(Loader.getFontFile("gotthard"), "gotthard_text", (int) (Gdx.graphics.getWidth() / 24f));
		GENERATED_LABEL_STYLE_GOTTHARD_MID_TEXT = new LabelStyle(font_text, COLOR_TEXT_SELECTED);
		GENERATED_LABEL_STYLE_GOTTHARD_MID_TEXT_WHITE = new LabelStyle(font_text, Color.WHITE);
		
		BitmapFont font_little_text = generator.createFont(Loader.getFontFile("gotthard"), "gotthard_text", (int) (Gdx.graphics.getWidth() / 48f));
		GENERATED_LABEL_STYLE_GOTTHARD_LITTLE_TEXT= new LabelStyle(font_little_text, COLOR_TEXT_SELECTED);
		GENERATED_LABEL_STYLE_GOTTHARD_LITTLE_TEXT_WHITE = new LabelStyle(font_little_text, Color.WHITE);
		
		BitmapFont font_tiny_text = generator.createFont(Loader.getFontFile("gotthard"), "gotthard_text", (int) (Gdx.graphics.getWidth() / 66f));
		GENERATED_LABEL_STYLE_GOTTHARD_TINY_TEXT = new LabelStyle(font_tiny_text, COLOR_TEXT_SELECTED);
		GENERATED_LABEL_STYLE_GOTTHARD_TINY_TEXT_WHITE = new LabelStyle(font_tiny_text, Color.WHITE);
		
		profile.setNewProfile_items();
		profile.loadInventoryPlayer();
		renderEngine.loadGameAssets();
		new SoundManager();
		
		CORE = new DACore(world, profile);
		
		GUI_NUMBER_PANEL_GAP	= Gdx.graphics.getWidth() / 128f;
	}
	
	/// 60 fps
	@Override
	public void render () {
		this.onUpdate();
		renderEngine.init();
		spriteBatch.setProjectionMatrix(mainCamera.combined);
	}
	
	public void onUpdate() {
		mainCamera.update();
		world.getLevelManager().update();
		engine.update(Gdx.graphics.getDeltaTime());
		
		if(CORE != null)
			CORE.UPDATE_GAME_CORE();
		//System.out.println("Memory NATIVE: " + (double)Gdx.app.getNativeHeap()/1024f);
		//System.out.println("Memory   JAVA: " + (double)Gdx.app.getJavaHeap()/1024);
		
		KeyBinding.processing();																//FIXME GlobalEvents
		if(keyHandler != null) {
			keyHandler.guiEvent();																	//FIXME GlobalEvents
			keyHandler.mouseUpdating();
		}
		
		//command_line.processing();
	}

	@Override
	public void resize (int width, int height) {
		if(GAME_WINDOW_STATE == 0) {
			float f1 = height;
			float f2 = width;
			float f3 = f2 / f1;
			
			SCALE_GUI_VALUE = 0.04f / f3;
			SCALE_WORLD_VALUE = 0.04f;
			SCALE_WORLD_VALUE_FINAL = 0.04f;
			mainCamera.zoom = SCALE_WORLD_VALUE_FINAL / f3;
			
			if(width < this.VIEWPORT_WIGHT_MIN) {
				mainCamera.setToOrtho(false, VIEWPORT_WIGHT_MIN, mainCamera.viewportHeight);
			} else if(height < this.VIEWPORT_HEIGHT_MIN) {
				mainCamera.setToOrtho(false, mainCamera.viewportWidth, VIEWPORT_HEIGHT_MIN);
			} else if(width > VIEWPORT_WIGHT_MAX) {
				mainCamera.setToOrtho(false, VIEWPORT_WIGHT_MAX, mainCamera.viewportHeight);
			} else if(height > this.VIEWPORT_HEIGHT_MAX) {
				mainCamera.setToOrtho(false, mainCamera.viewportWidth, VIEWPORT_HEIGHT_MAX);
			} else {
				mainCamera.setToOrtho(false, width, height);
			}
			//System.out.println("viewportWidth: " + mainCamera.viewportWidth);
			//System.out.println("viewportHeight: " + mainCamera.viewportHeight);
			
			guiManager.guiBatch.getTransformMatrix().scale(mainCamera.zoom, mainCamera.zoom, mainCamera.zoom);
			LIGHT_VALUE = mainCamera.viewportHeight * mainCamera.zoom;
			
			guiManager.setStage();
			guiManager.resize();
			
			GAME_WINDOW_STATE++;
		}
	}

	@Override
	public void pause() {
		GAME_STATE = GameState.PAUSE;
	}

	@Override
	public void resume() {
		if(CORE == null || !CORE.isGameRedactorActive())
			GAME_STATE = GameState.CONTINUE;
	}
	
	public static float ratio() {
		return (float)Gdx.graphics.getWidth() / (float)Gdx.graphics.getHeight();
	}
	
	/** Return current mode. */
	public static GameState getGameState() {
		return GAME_STATE;
	}
	
	/** Forced set method. */
	public static void setGameState(GameState state) {
		GAME_STATE = state;
	}
	
	/** Checking the type of device on running on. */
	public void checkType() {
		if(Gdx.app.getType() == ApplicationType.Android) {
			new Path(1);
		} else if(Gdx.app.getType() == ApplicationType.Desktop) {
			new Path(2);
		}
		
		new Loader();
	}
	
	/** Return the player. */
	public static synchronized IControllable getPlayer() {
		return player;
	}
	
	/** Connect player control to entity. */
	public void setPlayer(IControllable entity) {
		System.out.println("SET PLAYER.");
		if(player != null)
			((EntityLiving)player).remove(CompCamera.class);
		
		player = entity;
		((EntityLiving)player).add(new CompCamera(this.mainCamera));
	}

	@Override
	public void dispose () {
		spriteBatch.dispose();
		for(int i = 0; i < engine.getEntities().size() && engine.getEntities().get(i) instanceof EntityLiving; ++i) {
			EntityLiving entity = (EntityLiving) engine.getEntities().get(i);
			
			entity.compTexture.texture.dispose();
		}
		
		guiManager.reset();
		guiManager.getStage().dispose();
		world.physics.getBox2d().dispose();
		world.physics.guiRayHandler.dispose();
		engine.removeSystem(world);
		
		assetManager.dispose();
		isGameClosed = true;
		System.out.println("Game.dispose() ### all resources are disposed.");
	}
}
