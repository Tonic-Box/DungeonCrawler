package com.tonic.systems;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class DungeonMap {
    public int width, height;
    public int[][] tiles; // 0 = wall, 1 = floor.
    public List<Rectangle> rooms;
    public static final int TILE_SIZE = 32;
    private final Random random;
    // New fields for staircases (tile coordinates)
    public int stairsUpX = -1, stairsUpY = -1;
    public int stairsDownX = -1, stairsDownY = -1;

    // New constructor that uses a seed.
    public DungeonMap(int width, int height, long seed) {
        this.width = width;
        this.height = height;
        tiles = new int[width][height];
        rooms = new ArrayList<>();
        random = new Random(seed);
        generateDungeon();
    }

    private void generateDungeon() {
        // Fill map with walls.
        for (int x = 0; x < width; x++){
            for (int y = 0; y < height; y++){
                tiles[x][y] = 0;
            }
        }
        // Generate random rooms.
        int roomCount = 15;
        for (int i = 0; i < roomCount; i++) {
            int roomWidth = random.nextInt(6) + 4; // 4 to 9 tiles wide.
            int roomHeight = random.nextInt(6) + 4;
            int roomX = random.nextInt(width - roomWidth - 1) + 1;
            int roomY = random.nextInt(height - roomHeight - 1) + 1;

            Rectangle newRoom = new Rectangle(roomX, roomY, roomWidth, roomHeight);
            boolean overlaps = false;
            for (Rectangle other : rooms) {
                if (newRoom.overlaps(other)) {
                    overlaps = true;
                    break;
                }
            }
            if (!overlaps) {
                createRoom(roomX, roomY, roomWidth, roomHeight);
                if (!rooms.isEmpty()) {
                    Rectangle prevRoom = rooms.get(rooms.size() - 1);
                    int prevCenterX = (int) (prevRoom.x + prevRoom.width / 2);
                    int prevCenterY = (int) (prevRoom.y + prevRoom.height / 2);
                    int newCenterX = roomX + roomWidth / 2;
                    int newCenterY = roomY + roomHeight / 2;

                    if (random.nextBoolean()) {
                        createHTunnel(prevCenterX, newCenterX, prevCenterY);
                        createVTunnel(prevCenterY, newCenterY, newCenterX);
                    } else {
                        createVTunnel(prevCenterY, newCenterY, prevCenterX);
                        createHTunnel(prevCenterX, newCenterX, newCenterY);
                    }
                }
                rooms.add(newRoom);
            }
        }
    }

    private void createRoom(int x, int y, int roomWidth, int roomHeight) {
        for (int i = x; i < x + roomWidth; i++) {
            for (int j = y; j < y + roomHeight; j++) {
                tiles[i][j] = 1; // floor.
            }
        }
    }

    private void createHTunnel(int x1, int x2, int y) {
        for (int x = Math.min(x1, x2); x <= Math.max(x1, x2); x++) {
            tiles[x][y] = 1;
        }
    }

    private void createVTunnel(int y1, int y2, int x) {
        for (int y = Math.min(y1, y2); y <= Math.max(y1, y2); y++) {
            tiles[x][y] = 1;
        }
    }

    // Methods to set staircase positions.
    public void setStairsUp(int tileX, int tileY) {
        stairsUpX = tileX;
        stairsUpY = tileY;
    }

    public void setStairsDown(int tileX, int tileY) {
        stairsDownX = tileX;
        stairsDownY = tileY;
    }

    public void render(ShapeRenderer shapeRenderer) {
        for (int x = 0; x < width; x++){
            for (int y = 0; y < height; y++){
                if (tiles[x][y] == 1) {
                    shapeRenderer.setColor(0.7f, 0.7f, 0.7f, 1); // Floor.
                } else {
                    shapeRenderer.setColor(0.2f, 0.2f, 0.2f, 1); // Wall.
                }
                shapeRenderer.rect(x * TILE_SIZE, y * TILE_SIZE, TILE_SIZE, TILE_SIZE);

                // Draw up staircase.
                if (x == stairsUpX && y == stairsUpY) {
                    shapeRenderer.setColor(0, 0, 1, 1); // Blue.
                    shapeRenderer.rect(x * TILE_SIZE, y * TILE_SIZE, TILE_SIZE, TILE_SIZE);
                }
                // Draw down staircase.
                if (x == stairsDownX && y == stairsDownY) {
                    shapeRenderer.setColor(1, 0.5f, 0, 1); // Orange.
                    shapeRenderer.rect(x * TILE_SIZE, y * TILE_SIZE, TILE_SIZE, TILE_SIZE);
                }
            }
        }
    }
}
