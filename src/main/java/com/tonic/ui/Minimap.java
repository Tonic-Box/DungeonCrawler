package com.tonic.ui;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.Color;
import com.tonic.systems.DungeonMap;
import com.tonic.entities.Player;

public class Minimap {
    /**
     * Renders a minimap in the top-right corner using the given HUD camera.
     * Also draws an indicator (a red circle) for the player's position.
     */
    public void render(DungeonMap dungeonMap, OrthographicCamera hudCamera, Player player) {
        ShapeRenderer shapeRenderer = new ShapeRenderer();
        shapeRenderer.setProjectionMatrix(hudCamera.combined);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);

        // Define minimap dimensions.
        float minimapWidth = 150;
        float minimapHeight = 150;
        float margin = 10;
        float offsetX = hudCamera.viewportWidth - minimapWidth - margin;
        float offsetY = hudCamera.viewportHeight - minimapHeight - margin;
        float cellWidth = minimapWidth / dungeonMap.width;
        float cellHeight = minimapHeight / dungeonMap.height;

        // Draw each dungeon tile.
        for (int x = 0; x < dungeonMap.width; x++) {
            for (int y = 0; y < dungeonMap.height; y++) {
                if (dungeonMap.tiles[x][y] == 1) {
                    shapeRenderer.setColor(Color.LIGHT_GRAY);
                } else {
                    shapeRenderer.setColor(Color.DARK_GRAY);
                }
                shapeRenderer.rect(offsetX + x * cellWidth, offsetY + y * cellHeight, cellWidth, cellHeight);
            }
        }

        // Draw staircases.
        shapeRenderer.setColor(Color.BLUE);
        shapeRenderer.rect(offsetX + dungeonMap.stairsUpX * cellWidth, offsetY + dungeonMap.stairsUpY * cellHeight, cellWidth, cellHeight);
        shapeRenderer.setColor(Color.ORANGE);
        shapeRenderer.rect(offsetX + dungeonMap.stairsDownX * cellWidth, offsetY + dungeonMap.stairsDownY * cellHeight, cellWidth, cellHeight);

        // Draw player indicator.
        int playerTileX = (int)(player.x / DungeonMap.TILE_SIZE);
        int playerTileY = (int)(player.y / DungeonMap.TILE_SIZE);
        shapeRenderer.setColor(Color.RED);
        float playerIndicatorX = offsetX + playerTileX * cellWidth + cellWidth / 2;
        float playerIndicatorY = offsetY + playerTileY * cellHeight + cellHeight / 2;
        shapeRenderer.circle(playerIndicatorX, playerIndicatorY, Math.min(cellWidth, cellHeight) / 3);

        shapeRenderer.end();
        shapeRenderer.dispose();
    }

    // For backward compatibility, if needed.
    public void render(DungeonMap dungeonMap, OrthographicCamera hudCamera) {
        render(dungeonMap, hudCamera, new Player("dummy", 0, 0, 0));
    }
}
