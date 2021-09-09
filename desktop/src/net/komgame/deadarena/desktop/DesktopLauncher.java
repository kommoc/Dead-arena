package net.komgame.deadarena.desktop;

import org.jrenner.smartfont.example.ExampleMain;

import com.badlogic.gdx.Files.FileType;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;

import net.kommocgame.src.Game;
import net.kommocgame.src.Path;
import net.kommocgame.src.UITest;

public class DesktopLauncher {
	public static void main (String[] arg) {
		openGame();
	}
	
	private static void openGame() {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		setResolution3(config);
		config.title = "game";
		config.addIcon("data/icon_small.png", FileType.Local);
		config.resizable = false;
		new LwjglApplication(new Game(), config);
		//new LwjglApplication(new UITest(), config);
	}
	
	private static void openExampleFont() {
		LwjglApplicationConfiguration cfg = new LwjglApplicationConfiguration();
		cfg.height = 720;
		cfg.width = 1280;
		cfg.resizable = false;

		new LwjglApplication(new ExampleMain(), cfg);
	}
	
	public static void setResolution1(LwjglApplicationConfiguration config) {
		config.width = 800;
		config.height = 480;
	}
	
	public static void setResolution2(LwjglApplicationConfiguration config) {
		config.width = 1024;
		config.height = 768;
	}
	
	public static void setResolution3(LwjglApplicationConfiguration config) {
		config.width = 1280;
		config.height = 720;
	}
	
	public static void setResolution4K(LwjglApplicationConfiguration config) {
		config.width = 4096;
		config.height = 2160;
	}
	
	public static void setResolution4(LwjglApplicationConfiguration config) {
		config.width = 1920;
		config.height = 1200;
	}
	
	public static void setResolution5(LwjglApplicationConfiguration config) {
		config.width = 1920;
		config.height = 1080;
	}
	
	public static void setResolution6(LwjglApplicationConfiguration config) {
		config.width = 800;
		config.height = 600;
	}
	
	public static void setResolution7(LwjglApplicationConfiguration config) {
		config.width = 1280;
		config.height = 800;
	}
}
