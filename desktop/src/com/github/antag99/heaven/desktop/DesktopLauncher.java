package com.github.antag99.heaven.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.badlogic.gdx.tools.texturepacker.TexturePacker;
import com.github.antag99.heaven.HeavenGame;

public class DesktopLauncher {
    public static void main(String[] args) {
        if (args.length > 0 && "--pack".equals(args[0])) {
            String assets = "../core/assets";
            TexturePacker.Settings settings = new TexturePacker.Settings();
            settings.limitMemory = false;
            settings.pot = true;
            settings.maxWidth = 4096;
            settings.maxHeight = 4096;
            TexturePacker.process(settings, assets, assets, "skin.atlas");
        }

        LwjglApplicationConfiguration cfg = new LwjglApplicationConfiguration();
        cfg.title = "Monster In Heaven";
        cfg.width = 800;
        cfg.height = 600;
        new LwjglApplication(new HeavenGame(), cfg);
    }
}
