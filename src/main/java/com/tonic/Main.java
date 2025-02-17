package com.tonic;

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;

public class Main {
    public static void main(String[] args) {
        // Configure your application
        Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();
        config.setTitle("DungeonDots");
        config.setWindowedMode(800, 600);

        // Launch the LibGDX application
        new Lwjgl3Application(new DungeonCrawlerGame(), config);
    }
}