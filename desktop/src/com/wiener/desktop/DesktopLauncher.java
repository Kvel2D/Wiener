package com.wiener.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.wiener.Constants;
import com.wiener.Main;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();

		config.width = (int) (Constants.VIEWPORT_WIDTH / Constants.METER_TO_PIXEL);
		config.height = (int) (Constants.VIEWPORT_HEIGHT / Constants.METER_TO_PIXEL);
		config.title = "Wiener";
		config.resizable = false;
		config.samples = 4;

		new LwjglApplication(new Main(), config);
	}
}
