package ru.codemonkeystudio.terrarum.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import ru.codemonkeystudio.terrarum.Terrarum;

//Лаунчер игры на desktop
public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.width = 1366;
		config.height = 768;
		config.fullscreen = false;
		new LwjglApplication(new Terrarum(), config);
	}
}
