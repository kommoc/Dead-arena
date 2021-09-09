package net.kommocgame.src;

import java.io.File;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Application.ApplicationType;
import com.badlogic.gdx.Files.FileType;
import com.badlogic.gdx.Files;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Array;

public class Loader {
	
	private static Array<Texture> array_texture = new Array<Texture>();
	
	static {
		loadFonts();
		loadSkins();
		loadAtlases();
		System.out.println("\n	LOADER::: Assets ");
		
		if(Gdx.app.getType() == ApplicationType.Android) {
			System.out.print("(ANDROID) loading.");
			loadAssetsAndroid("assets/texture/");
			Path.game_dir = "";
		} else if(Gdx.app.getType() == ApplicationType.Desktop) {
			System.out.print("(DESKTOP) loading.");
			loadAssets(Gdx.files.getLocalStoragePath() + "assets/texture/");
			Path.game_dir = Gdx.files.getLocalStoragePath();
		}
		
		System.out.println("\n	LOADER::: Assets DONE.");
		loadSounds();
		//Game.assetManager.finishLoading();
		
		//System.out.println("	LOADER::: finishLoading() DONE.");
		
		System.out.println("Size of loaded assets: " + Game.assetManager.getLoadedAssets());
	}
	
	public void setGameDir(String str) {
		Path.game_dir = str;
	}
	
	private static void loadFonts() {
		System.out.println("	LOADER::: Fonts loading.");
		Game.assetManager.load("assets/font/console_32.fnt", BitmapFont.class);
		Game.assetManager.load("assets/font/console_48.fnt", BitmapFont.class);
		Game.assetManager.load("assets/font/console_72.fnt", BitmapFont.class);
		Game.assetManager.load("assets/font/console_120.fnt", BitmapFont.class);
		Game.assetManager.load("assets/font/console_155.fnt", BitmapFont.class);
		System.out.println("	LOADER::: Fonts DONE.");
	}
	
	private static void loadSounds() {
		System.out.println("	LOADER::: Sound loading.");
		
		FileHandle path = getGameFile("assets/sound/");
		//System.out.println("SOUND: " + path.list().length + "\n");
		for(FileHandle file : path.list()) {
			System.out.print("SOUND_FILE: " + file.path());
			
			if(file.file().getName().endsWith(".ogg")) {
				System.out.println(" ::::: LOADED");
				Game.assetManager.load(file.path().substring(file.path().indexOf("assets")), Sound.class);
			}
		}
		
		System.out.println("	LOADER::: Sound DONE.");
	}
	
	private static void loadSkins() {
		System.out.println("	LOADER::: Skins loading.");
		//Game.assetManager.load("assets/skin/Commodore_64_UI/commodore64ui/uiskin.json", Skin.class);		IS MINIMAL!
		Game.assetManager.load("assets/skin/NeonUI/neonui/neon-ui.json", Skin.class);
		Game.assetManager.load("assets/skin/Neutralizer_UI/neutralizerui/neutralizer-ui.json", Skin.class);
		Game.assetManager.load("assets/skin/Kommoc_UI/Kommoc_UI.json", Skin.class);
		System.out.println("	LOADER::: Skins DONE.");
	}
	
	private static void loadAtlases() {
		System.out.println("	LOADER::: Atlases loading.");
		//Game.assetManager.load("assets/skin/Sci_fi/panels.atlas", TextureAtlas.class);
		Game.assetManager.load("assets/fx/atlas/tex_fx.atlas", TextureAtlas.class);
		
		System.out.println("	LOADER::: Atlases DONE.");
	}
	
	private static void loadAssets(String path) {
		File file = new File(path);
		
		System.out.print("\nFILE: " + file.getPath());
		if(file.isDirectory()) {
			System.out.println("::IS DIRECTORY::");
			for(File s : file.listFiles()) {
				loadAssets(s.getPath());
			}
		} else if(file.getName().endsWith(".png")) {
			System.out.print(" ::::: LOADED");
			Game.assetManager.load(file.getPath().substring(file.getPath().indexOf("assets")), Texture.class);
		} else {
			System.out.println(" - END. :" + file.getName());
		}
	}
	
	private static void loadAssetsAndroid(String path) {
		FileHandle file = Gdx.files.getFileHandle(path, FileType.Internal);
		
		System.out.print("FILE: " + file.path());
		
		if(file.isDirectory()) {
			System.out.println("::IS DIRECTORY::");
			for(FileHandle s : file.list()) {
				loadAssetsAndroid(s.path());
			}
		} else if(file.extension().equals("png")) {
			System.out.print(" ::::: LOADED");
			Game.assetManager.load(file.path().substring(file.path().indexOf("assets")), Texture.class);
		} else {
			System.out.println(" - END. ");
		}
	}
	
	public static String getGameDir() {
		return Path.game_dir;
	}
	
	public static FileHandle getGameFile(String path) {
		return Gdx.files.getFileHandle(getGameDir() + path, FileType.Internal);
	}
	
	public static Texture objects(String path) {
		return Game.assetManager.get("assets/texture/objects/" + path, Texture.class);
	}
	
	public static Texture objectsMobs(String name) {
		return Game.assetManager.get("assets/texture/objects/mobs/" + name, Texture.class);
	}
	
	public static Texture objectsProps(String name) {
		return Game.assetManager.get("assets/texture/objects/props/" + name, Texture.class);
	}
	
	public static Texture objectsItems(String name) {
		return Game.assetManager.get("assets/texture/objects/items/" + name, Texture.class);
	}
	
	public static Texture objectsUnits(String name) {
		return Game.assetManager.get("assets/texture/objects/units/" + name, Texture.class);
	}
	
	public static Texture objectsWeapons(String name) {
		return Game.assetManager.get("assets/texture/objects/weapons/" + name, Texture.class);
	}
	
	public static Texture guiEditor(String path) {
		return Game.assetManager.get("assets/texture/gui/editor/" + path, Texture.class);
	}
	
	public static Texture guiMisc(String name) {
		return Game.assetManager.get("assets/texture/gui/misc/" + name, Texture.class);
	}
	
	public static Texture guiMiscIcons(String name) {
		return Game.assetManager.get("assets/texture/gui/misc/icons/" + name, Texture.class);
	}
	
	public static Texture guiMenuBut(String name) {
		return Game.assetManager.get("assets/texture/gui/menu/but/" + name, Texture.class);
	}
	
	public static Texture guiMenuPanel(String name) {
		return Game.assetManager.get("assets/texture/gui/menu/panel/" + name, Texture.class);
	}
	
	public static Texture guiMenuSlot(String name) {
		return Game.assetManager.get("assets/texture/gui/menu/slot/" + name, Texture.class);
	}
	
	public static Texture guiMenuCONCEPT(String name) {
		return Game.assetManager.get("assets/texture/gui/menu/CONCEPT/" + name, Texture.class);
	}
	
	public static Texture guiIcon(String name) {
		return Game.assetManager.get("assets/texture/gui/icon/" + name, Texture.class);
	}
	
	public static Texture guiMenuCilia(String name) {
		return Game.assetManager.get("assets/texture/gui/menu/cilia/" + name, Texture.class);
	}
	
	public static Texture guiMenuBackground(String name) {
		return Game.assetManager.get("assets/texture/gui/menu/background/" + name, Texture.class);
	}
	
	public static Texture impact(String name) {
		return Game.assetManager.get("assets/texture/objects/impacts/" + name, Texture.class);
	}
	
	public static Texture level(String path) {
		return Game.assetManager.get("assets/texture/levels/" + path, Texture.class);
	}
	
	public static Texture get(String path) {
		return Game.assetManager.get(path, Texture.class);
	}
	
	public static FileHandle getLevel(String level) {
		return getGameFile("assets/SJF/" + level);
	}
	
	/** Return the font-file. Enter only name, suffix will be added automatic.*/
	public static FileHandle getFontFile(String name) {
		return getGameFile("assets/fontType/" + name + ".ttf").exists() ? getGameFile("assets/fontType/" + name) : 
			getGameFile("assets/fontType/" + name + ".otf");
	}
	
	/** Return the lang-file. Enter only language, suffix will be added automatic.*/
	public static FileHandle getLangFile(String lang) {
		return getGameFile("assets/language/" + lang + "lang-" + lang + ".txt");
	}
	
	public static Drawable getDrowable(Texture texture) {
		return new TextureRegionDrawable(new TextureRegion(texture));
	}
	
	private static Array<Texture> getTextures() {
		return Game.assetManager.getAll(Texture.class, array_texture);
	}
}