package com.tonic.systems;

import com.tonic.entities.Chest;
import com.tonic.entities.Monster;
import java.util.List;
import java.util.Random;

public class Floor {
    public DungeonMap dungeonMap;
    public int stairsUpX, stairsUpY;
    public int stairsDownX, stairsDownY;
    public int width, height;
    public long seed;
    private final Random random;

    // Stair linking fields:
    public boolean staircaseLinked = false;
    public int linkedFloor = -1;       // the other floor number
    public int linkedStairsX = -1;     // tile coordinate of the staircase on the linked floor
    public int linkedStairsY = -1;
    public final List<Monster> monsters;
    public final List<Chest> chests;

    /**
     * Constructor for a floor that may have an up staircase.
     * (For floor 0, typically no up staircase.)
     */
    public Floor(int level, int width, int height, long seed, boolean hasUpStairs) {
        this.width = width;
        this.height = height;
        this.seed = seed;
        random = new Random(seed);
        dungeonMap = new DungeonMap(width, height, seed);

        // Place down staircase on a random walkable tile.
        int[] tile = getRandomFloorTile();
        stairsDownX = tile[0];
        stairsDownY = tile[1];
        dungeonMap.setStairsDown(stairsDownX, stairsDownY);

        if (hasUpStairs) {
            tile = getRandomFloorTile();
            stairsUpX = tile[0];
            stairsUpY = tile[1];
            dungeonMap.setStairsUp(stairsUpX, stairsUpY);
        } else {
            stairsUpX = -1;
            stairsUpY = -1;
        }

        monsters = Monster.genList(dungeonMap, level);
        chests = Chest.generateChest(dungeonMap, level + 1);
    }

    /**
     * Constructor for a new floor created via a staircase.
     * forcedUpX/Y are tile coordinates from the previous floor.
     * If they aren’t walkable, the nearest walkable tile is used.
     */
    public Floor(int level, int width, int height, long seed, int forcedUpX, int forcedUpY) {
        this.width = width;
        this.height = height;
        this.seed = seed;
        random = new Random(seed);
        dungeonMap = new DungeonMap(width, height, seed);

        // Validate forced up coordinates.
        int[] upTile;
        if (isWalkableTile(forcedUpX, forcedUpY))
            upTile = new int[] { forcedUpX, forcedUpY };
        else
            upTile = findNearestWalkable(forcedUpX, forcedUpY);

        stairsUpX = upTile[0];
        stairsUpY = upTile[1];
        dungeonMap.setStairsUp(stairsUpX, stairsUpY);

        // Place down staircase normally.
        int[] tile = getRandomFloorTile();
        stairsDownX = tile[0];
        stairsDownY = tile[1];
        dungeonMap.setStairsDown(stairsDownX, stairsDownY);

        monsters = Monster.genList(dungeonMap, level);
        chests = Chest.generateChest(dungeonMap, level + 1);
    }

    /**
     * Returns a random walkable tile coordinate as {tileX, tileY}.
     */
    public int[] getRandomFloorTile() {
        int tileX, tileY;
        do {
            tileX = random.nextInt(width);
            tileY = random.nextInt(height);
        } while (dungeonMap.tiles[tileX][tileY] != 1);
        return new int[] { tileX, tileY };
    }

    /**
     * Checks if (x,y) is within bounds and is walkable.
     */
    private boolean isWalkableTile(int x, int y) {
        if (x < 0 || y < 0 || x >= width || y >= height)
            return false;
        return dungeonMap.tiles[x][y] == 1;
    }

    /**
     * Searches for the nearest walkable tile to (startX, startY).
     */
    private int[] findNearestWalkable(int startX, int startY) {
        int radius = 1;
        while (radius < Math.max(width, height)) {
            for (int dx = -radius; dx <= radius; dx++) {
                for (int dy = -radius; dy <= radius; dy++) {
                    int tx = startX + dx;
                    int ty = startY + dy;
                    if (tx < 0 || ty < 0 || tx >= width || ty >= height)
                        continue;
                    if (dungeonMap.tiles[tx][ty] == 1) {
                        return new int[] { tx, ty };
                    }
                }
            }
            radius++;
        }
        // Fallback: return a random floor tile.
        return getRandomFloorTile();
    }
}
