package com.tonic.entities;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.tonic.systems.DungeonMap;
import com.tonic.systems.LootTable;
import com.tonic.items.Item;

import java.util.List;
import java.util.Random;

public class Monster extends Entity {
    private float moveTimer = 0;
    private float moveInterval;
    private float wanderDistance = 16; // Pixels per move.
    private Random random = new Random();
    private List<Item> lootTable = LootTable.generate();

    // New fields for combat
    public float combatTimer = 0;   // Time remaining until next hit.
    public boolean playerTurn = true; // true if next hit is by the player.

    public Monster(String name, int maxHealth, int attack, int defense) {
        super(name, maxHealth, attack, defense, Color.ORANGE);
        //random float between .5 and 2.5
        moveInterval = MathUtils.random(.5f, 2.5f);
    }

    /**
     * Updates the monster's movement.
     */
    public void update(float delta, DungeonMap dungeonMap) {
        moveTimer += delta;
        if (moveTimer >= moveInterval) {
            moveTimer = 0;
            moveInterval = MathUtils.random(.5f, 2.5f);
            float dx = 0, dy = 0;
            int dir = random.nextInt(4);
            if (dir == 0) dx = wanderDistance;
            else if (dir == 1) dx = -wanderDistance;
            else if (dir == 2) dy = wanderDistance;
            else dy = -wanderDistance;

            float newX = x + dx;
            float newY = y + dy;
            int tileX = (int)(newX / DungeonMap.TILE_SIZE);
            int tileY = (int)(newY / DungeonMap.TILE_SIZE);
            if (tileX >= 0 && tileY >= 0 && tileX < dungeonMap.width && tileY < dungeonMap.height) {
                if (dungeonMap.tiles[tileX][tileY] == 1) {
                    x = newX;
                    y = newY;
                }
            }
        }
    }

    /**
     * Places the monster randomly on a walkable tile.
     */
    public void placeRandomlyOn(DungeonMap dungeonMap) {
        int tileX, tileY;
        do {
            tileX = random.nextInt(dungeonMap.width);
            tileY = random.nextInt(dungeonMap.height);
        } while (dungeonMap.tiles[tileX][tileY] != 1);
        x = tileX * DungeonMap.TILE_SIZE + DungeonMap.TILE_SIZE / 2;
        y = tileY * DungeonMap.TILE_SIZE + DungeonMap.TILE_SIZE / 2;
    }

    /**
     * Returns an Item drop with a 70% chance.
     */
    public Item getDrop() {
        if (MathUtils.randomBoolean(0.7f)) {
            return lootTable.get(MathUtils.random(0, lootTable.size() - 1));
        }
        return null;
    }

    @Override
    public void render(ShapeRenderer shapeRenderer) {
        shapeRenderer.setColor(1, 0, 2, 1);
        shapeRenderer.circle(x, y, 10);
    }
}
