package com.tonic.ui;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.tonic.DungeonCrawlerGame;
import com.tonic.systems.Quest;

public class GameScreen implements Screen {
    final DungeonCrawlerGame game;
    // World camera that follows the player.
    OrthographicCamera camera;
    // HUD camera for fixed UI elements.
    OrthographicCamera hudCamera;
    BitmapFont font;
    ShapeRenderer shapeRenderer;

    public GameScreen(final DungeonCrawlerGame game) {
        this.game = game;
        camera = new OrthographicCamera();
        camera.setToOrtho(false, 800, 600);

        hudCamera = new OrthographicCamera();
        hudCamera.setToOrtho(false, 800, 600);

        font = new BitmapFont();
        shapeRenderer = new ShapeRenderer();
        game.engine.init();
    }

    @Override
    public void render(float delta) {
        // Update game logic; pass hudCamera to UI update methods.
        game.engine.update(delta, hudCamera);

        // Update the world camera to follow the player.
        camera.position.set(game.engine.player.x, game.engine.player.y, 0);
        camera.update();

        Gdx.gl.glClearColor(0, 0, 0.2f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // 1) Render the world (dungeon, entities, loot) using the world camera.
        shapeRenderer.setProjectionMatrix(camera.combined);
        game.batch.setProjectionMatrix(camera.combined);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        game.engine.renderVisual(shapeRenderer);
        shapeRenderer.end();

        // 2) Render the HUD UI panels (inventory, equipment, minimap) with the HUD camera.
        hudCamera.update();
        game.engine.renderUI(game.batch, shapeRenderer, hudCamera, camera);

        // 3) Render additional HUD text:
        //    - Player info (name, HP, floor) in the top-right.
        //    - Quest info in the bottom-left.
        hudCamera.update();
        game.batch.setProjectionMatrix(hudCamera.combined);
        game.batch.begin();
        // Draw player info in top-right:
        String playerInfo = "Player: " + game.engine.player.name +
                "\nHP: " + game.engine.player.health +
                " \nFloor: " + game.engine.currentFloor;
        // Adjust X offset so that the text is near the top-right.
        font.draw(game.batch, playerInfo, 20, hudCamera.viewportHeight - 20);

        // Draw quest info in bottom-left:
        int questY = 40;
        for (Quest quest : game.engine.questManager.getQuests()) {
            String questText = "Quest: " + quest.title + " [" +
                    quest.progress + "/" + quest.target + "] " +
                    (quest.completed ? "COMPLETED" : "");
            font.draw(game.batch, questText, 20, questY);
            questY += 20;
        }
        game.batch.end();
    }

    @Override
    public void resize(int width, int height) {
        camera.setToOrtho(false, width, height);
        hudCamera.setToOrtho(false, width, height);
    }
    @Override public void show() { }
    @Override public void hide() { }
    @Override public void pause() { }
    @Override public void resume() { }

    @Override
    public void dispose() {
        font.dispose();
        shapeRenderer.dispose();
    }
}
