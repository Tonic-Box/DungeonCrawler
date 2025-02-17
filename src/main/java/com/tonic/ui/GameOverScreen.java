package com.tonic.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.tonic.DungeonCrawlerGame;

public class GameOverScreen implements Screen {
    private final DungeonCrawlerGame game;
    private final SpriteBatch batch;
    private final ShapeRenderer shapeRenderer;
    private final BitmapFont font;
    private final float buttonX, buttonY, buttonWidth, buttonHeight;

    public GameOverScreen(final DungeonCrawlerGame game) {
        this.game = game;
        this.batch = game.batch;
        this.shapeRenderer = new ShapeRenderer();
        this.font = new BitmapFont();
        font.getData().setScale(2);
        buttonWidth = 200;
        buttonHeight = 50;
        buttonX = (Gdx.graphics.getWidth() - buttonWidth) / 2;
        buttonY = (float) Gdx.graphics.getHeight() / 2 - 100;
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        batch.begin();
        font.setColor(Color.RED);
        font.draw(batch, "GAME OVER", Gdx.graphics.getWidth() / 2f - 60, Gdx.graphics.getHeight() / 2f + 50);
        font.setColor(Color.WHITE);
        font.draw(batch, "Click to Restart", buttonX + 30, buttonY + 30);
        batch.end();
        if (Gdx.input.justTouched()) {
            float touchX = Gdx.input.getX();
            float touchY = Gdx.graphics.getHeight() - Gdx.input.getY();

            if (touchX >= buttonX && touchX <= buttonX + buttonWidth &&
                    touchY >= buttonY && touchY <= buttonY + buttonHeight) {
                game.restartGame();
            }
        }
    }

    @Override
    public void resize(int width, int height) {}

    @Override
    public void show() {}

    @Override
    public void hide() {}

    @Override
    public void pause() {}

    @Override
    public void resume() {}

    @Override
    public void dispose() {
        shapeRenderer.dispose();
        font.dispose();
    }
}
