package com.tonic.ui;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.tonic.systems.DungeonMap;

public class StairUI {

    public void render(ShapeRenderer shapeRenderer, DungeonMap dungeonMap) {
        // Draw Up Stairs (blue tile with an upward arrow)
        if (dungeonMap.stairsUpX != -1 && dungeonMap.stairsUpY != -1) {
            drawUpStairs(shapeRenderer, dungeonMap.stairsUpX, dungeonMap.stairsUpY);
        }

        // Draw Down Stairs (orange tile with a downward arrow)
        if (dungeonMap.stairsDownX != -1 && dungeonMap.stairsDownY != -1) {
            drawDownStairs(shapeRenderer, dungeonMap.stairsDownX, dungeonMap.stairsDownY);
        }
    }

    private void drawUpStairs(ShapeRenderer shapeRenderer, int x, int y) {
        // Calculate the pixel coordinates.
        float startX = x * DungeonMap.TILE_SIZE;
        float startY = y * DungeonMap.TILE_SIZE;

        // Draw the base tile in blue.
        shapeRenderer.setColor(Color.BLUE);
        shapeRenderer.rect(startX, startY, DungeonMap.TILE_SIZE, DungeonMap.TILE_SIZE);

        // Draw an upward-pointing arrow.
        float arrowCenterX = startX + DungeonMap.TILE_SIZE / 2f;
        float arrowCenterY = startY + DungeonMap.TILE_SIZE / 2f;
        float arrowSize = DungeonMap.TILE_SIZE / 2f;
        shapeRenderer.setColor(Color.WHITE);
        shapeRenderer.triangle(
                arrowCenterX, arrowCenterY + arrowSize / 2f,  // Top point of the arrow.
                arrowCenterX - arrowSize / 2f, arrowCenterY - arrowSize / 2f,  // Bottom left.
                arrowCenterX + arrowSize / 2f, arrowCenterY - arrowSize / 2f   // Bottom right.
        );
    }

    private void drawDownStairs(ShapeRenderer shapeRenderer, int x, int y) {
        // Calculate the pixel coordinates.
        float startX = x * DungeonMap.TILE_SIZE;
        float startY = y * DungeonMap.TILE_SIZE;

        // Draw the base tile in orange.
        shapeRenderer.setColor(Color.ORANGE);
        shapeRenderer.rect(startX, startY, DungeonMap.TILE_SIZE, DungeonMap.TILE_SIZE);

        // Draw a downward-pointing arrow.
        float arrowCenterX = startX + DungeonMap.TILE_SIZE / 2f;
        float arrowCenterY = startY + DungeonMap.TILE_SIZE / 2f;
        float arrowSize = DungeonMap.TILE_SIZE / 2f;
        shapeRenderer.setColor(Color.WHITE);
        shapeRenderer.triangle(
                arrowCenterX, arrowCenterY - arrowSize / 2f,  // Bottom point of the arrow.
                arrowCenterX - arrowSize / 2f, arrowCenterY + arrowSize / 2f,  // Top left.
                arrowCenterX + arrowSize / 2f, arrowCenterY + arrowSize / 2f   // Top right.
        );
    }
}
