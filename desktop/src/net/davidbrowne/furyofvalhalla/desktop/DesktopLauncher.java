package net.davidbrowne.furyofvalhalla.desktop;

import com.badlogic.gdx.Files;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import net.davidbrowne.furyofvalhalla.Game;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.title="Fury Of Valhalla";
		//config.fullscreen=true;
		config.addIcon("icon.png", Files.FileType.Internal);
		config.resizable=true;
		new LwjglApplication(new Game(), config);
	}
}
