package com.tonic.util;

import com.tonic.entities.Entity;
import com.tonic.systems.DungeonMap;

import java.util.Random;

public class MapUtil
{
    private static final Random random = new Random();

    /**
     * Places the monster randomly on a walkable tile.
     */
    public static void placeRandomlyOn(Entity entity, DungeonMap dungeonMap) {
        int tileX, tileY;
        do {
            tileX = random.nextInt(dungeonMap.width);
            tileY = random.nextInt(dungeonMap.height);
        } while (dungeonMap.tiles[tileX][tileY] != 1);
        entity.setTile(tileX, tileY);
        }
    }
