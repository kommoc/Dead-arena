package net.kommocgame.src.DeadArena.core;

import java.io.IOException;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.DataInput;
import com.badlogic.gdx.utils.StreamUtils;
import com.kotcrab.vis.ui.widget.file.FileChooser.FileItem;

import net.kommocgame.src.Game;
import net.kommocgame.src.Loader;

public class LanguageManager {
	
	
	
	private Game game;
	
	public LanguageManager(Game game) {
		
	}
	
	public void setLanguage() {
		
	}
	
	private class Language {
		
		private String path;
		private String language;
		private FileItem text_file;
		
		public Language(String language_name) {
			FileHandle fileHandle = Loader.getFontFile(language_name);
			DataInput input_level = new DataInput(fileHandle.read());
			try {
				String json_file = StreamUtils.copyStreamToString(input_level);
				
				input_level.close();
			} catch (IOException e) { e.printStackTrace(); }
			
			Loader.getLangFile(language_name);
		}
	}
}
