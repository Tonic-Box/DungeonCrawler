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
        instance = this;
        batch = new SpriteBatch();
        restartGame(); // Call restart game method initially
    }

    @Override
    public void render() {
        super.render();
    }

    public void restartGame() {
        engine = new Engine();
        engine.init();
        setScreen(new GameScreen(this));
    }

    @Override
    public void dispose() {
        batch.dispose();
        engine.dispose();
    }
}
