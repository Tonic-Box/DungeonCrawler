package com.tonic;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.tonic.ui.GameScreen;

public class DungeonCrawlerGame extends Game {
    public static DungeonCrawlerGame instance; // static instance
    public SpriteBatch batch;
    public Engine engine;

    @Override
    public void create() {
        instance = this; // set static instance
        batch = new SpriteBatch();
        engine = new Engine();
        setScreen(new GameScreen(this));
    }

    @Override
    public void render() {
        super.render();
    }

    @Override
    public void dispose() {
        batch.dispose();
        engine.dispose();
    }
}
