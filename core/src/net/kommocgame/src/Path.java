package net.kommocgame.src;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.FileHandleResolver;
import com.badlogic.gdx.assets.loaders.resolvers.LocalFileHandleResolver;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

public class Path {
	
	@Deprecated
	private static String textures;
	@Deprecated
	private static String mobs;
	@Deprecated
	private static String props;
	@Deprecated
	private static String weapons;
	@Deprecated
	private static String units;
	@Deprecated
	private static String gui;
	@Deprecated
	private static String fonts;
	@Deprecated
	private static String skin;
	@Deprecated
	private static String levels;
	
	@Deprecated
	private static String sounds;
	
	private LocalFileHandleResolver resolver;
	
	protected static String game_dir;
	
	public Path(int type) {
		if(type == 1) {					//ANDROID
			Game.assetManager = new AssetManager();
			
			textures = "textures/";
			mobs = "textures/gameObjects/mobs/";
			props = "textures/gameObjects/props/";
			weapons = "textures/gameObjects/weapons/";
			units = "textures/gameObjects/units/";
			gui = "textures/gui/";
			fonts = "fonts/";
			skin = "skin/";
			levels = "textures/levels/";
			
			sounds = "sounds/";
		} else if(type == 2) {            //DESCTOP
			resolver = new LocalFileHandleResolver();
			Game.assetManager = new AssetManager(resolver);

			textures = Gdx.files.getLocalStoragePath() + "textures/";
			mobs = Gdx.files.getLocalStoragePath() + "textures/gameObjects/mobs/";
			props = Gdx.files.getLocalStoragePath() + "textures/gameObjects/props/";
			weapons = Gdx.files.getLocalStoragePath() + "textures/gameObjects/weapons/";
			units = Gdx.files.getLocalStoragePath() + "textures/gameObjects/units/";
			gui = Gdx.files.getLocalStoragePath() + "textures/gui/";
			fonts = Gdx.files.getLocalStoragePath() + "fonts/";
			skin = Gdx.files.getLocalStoragePath() + "skin/";
			levels = Gdx.files.getLocalStoragePath() + "textures/levels/";

			sounds = Gdx.files.getLocalStoragePath() + "sounds/";
		}
		this.minimalUILoading();
	}
	
	private static void minimalUILoading() {
		Game.assetManager.load("android/assets/skin/Commodore_64_UI/commodore64ui/uiskin.json", Skin.class);//assets/skin/Sci_fi/panels.atlas
		Game.assetManager.load("android/assets/skin/Sci_fi/panels.atlas", TextureAtlas.class);
		Game.assetManager.finishLoading();
	}
	
	@Deprecated
	public final static String main(String path) {
		return textures + path;
	}
	@Deprecated
	public final static String mobs(String name) {
		return mobs + name;
	}
	@Deprecated
	public final static String units(String name) {
		return units + name;
	}
	@Deprecated
	public final static String props(String name) {
		return props + name;
	}
	@Deprecated
	public final static String weapons(String name) {
		return weapons + name;
	}
	@Deprecated
	public final static String gui(String name) {
		return gui + name;
	}
	@Deprecated
	public final static String fonts(String name) {
		return fonts + name;
	}
	@Deprecated
	public final static String skin(String name) {
		return skin + name;
	}
	@Deprecated
	public final static String levels(String name) {
		return levels + name;
	}
	//@Deprecated
	//public final static FileHandle sounds(String name) {
	//	return Gdx.files.internal(sounds + name);
	//}
}
