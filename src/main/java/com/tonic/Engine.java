package com.tonic;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.tonic.entities.LootDrop;
import com.tonic.entities.Monster;
import com.tonic.entities.Player;
import com.tonic.items.Item;
import com.tonic.items.Weapon;
import com.tonic.systems.*;
import com.tonic.ui.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Engine {
    public Player player;
    public CombatSystem combatSystem;
    public QuestManager questManager;
    public Map<Integer, Floor> floors;
    public int currentFloor;
    public Floor currentFloorObj;
    public List<LootDrop> lootDrops;
    private final long baseSeed;
    public InventoryUI inventoryUI;
    public EquipmentUI equipmentUI;
    public LootUI simpleLootUI;
    public Minimap minimap;
    public Map<String, StaircaseLink> staircaseLinks = new HashMap<>();
    public boolean gameOver = false;

    private String getStairKey(int floor, int tileX, int tileY, String side) {
        return floor + ":" + tileX + "," + tileY + ":" + side;
    }

    public Engine() {
        baseSeed = MathUtils.random(0, Long.MAX_VALUE);
        combatSystem = new CombatSystem();
        questManager = new QuestManager();
        floors = new HashMap<>();
        lootDrops = new ArrayList<>();
    }

    public void init() {
        currentFloor = 0;
        long seed = baseSeed + currentFloor;
        Floor floor0 = new Floor(currentFloor, 50, 40, seed, false);
        floors.put(currentFloor, floor0);
        currentFloorObj = floor0;

        player = new Player("Hero", 100, 10, 2);
        int[] spawn = currentFloorObj.getRandomFloorTile();
        player.setTile(spawn[0], spawn[1]);
        Weapon sword = new Weapon("Sword", 5);
        player.inventory.addItem(sword);

        Quest quest = new Quest("Defeat monsters", "Eliminate 1000 monsters in the dungeon",
                Quest.QuestType.DEFEAT, 1000);
        questManager.addQuest(quest);

        BitmapFont font = new BitmapFont();
        inventoryUI = new InventoryUI(font);
        equipmentUI = new EquipmentUI(font);
        simpleLootUI = new LootUI(font);
        minimap = new Minimap();
    }

    public void update(float delta, OrthographicCamera hudCamera) {
        if(gameOver)
            return;

        if(player.health <= 0)
        {
            gameOver = true;
            DungeonCrawlerGame.instance.setScreen(new GameOverScreen(DungeonCrawlerGame.instance));
            return;
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.I)) {
            if (equipmentUI.isVisible()) equipmentUI.toggle();
            inventoryUI.toggle();
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.E)) {
            if (inventoryUI.isVisible()) inventoryUI.toggle();
            equipmentUI.toggle();
        }

        player.update(delta, currentFloorObj.dungeonMap);
        for(Monster monster : floors.get(currentFloor).monsters)
        {
            monster.update(delta, currentFloorObj.dungeonMap);
        }

        int playerTileX = (int)(player.x / DungeonMap.TILE_SIZE);
        int playerTileY = (int)(player.y / DungeonMap.TILE_SIZE);

        int onStairs = 0; // 0 = not on stairs, 1 = down, 2 = up.
        if (playerTileX == currentFloorObj.dungeonMap.stairsDownX &&
                playerTileY == currentFloorObj.dungeonMap.stairsDownY)
            onStairs = 1;
        else if (playerTileX == currentFloorObj.dungeonMap.stairsUpX &&
                playerTileY == currentFloorObj.dungeonMap.stairsUpY)
            onStairs = 2;

        if (onStairs != 0 && Gdx.input.isKeyJustPressed(Input.Keys.Q)) {
            String side = (onStairs == 1) ? "down" : "up";
            String key = getStairKey(currentFloor, playerTileX, playerTileY, side);
            if (staircaseLinks.containsKey(key)) {
                StaircaseLink link = staircaseLinks.get(key);
                System.out.println("Toggling staircase: switching from floor " + currentFloor +
                        " to floor " + link.targetFloor);
                currentFloor = link.targetFloor;
                currentFloorObj = floors.get(currentFloor);
                player.setTile(link.targetX, link.targetY);
            } else {
                if (onStairs == 1) {
                    System.out.println("Creating new floor via staircase linking.");
                    int newFloorNum = currentFloor + 1;
                    long seed = baseSeed + newFloorNum;
                    Floor newFloor = new Floor(newFloorNum, 50, 40, seed, playerTileX, playerTileY);
                    floors.put(newFloorNum, newFloor);

                    StaircaseLink linkDown = new StaircaseLink(
                            currentFloor, playerTileX, playerTileY,
                            newFloorNum, newFloor.dungeonMap.stairsUpX, newFloor.dungeonMap.stairsUpY);
                    StaircaseLink linkUp = new StaircaseLink(
                            newFloorNum, newFloor.dungeonMap.stairsUpX, newFloor.dungeonMap.stairsUpY,
                            currentFloor, playerTileX, playerTileY);

                    staircaseLinks.put(getStairKey(currentFloor, playerTileX, playerTileY, "down"), linkDown);
                    staircaseLinks.put(getStairKey(newFloorNum, newFloor.dungeonMap.stairsUpX, newFloor.dungeonMap.stairsUpY, "up"), linkUp);

                    currentFloorObj.staircaseLinked = true;
                    currentFloorObj.linkedFloor = newFloorNum;
                    currentFloorObj.linkedStairsX = newFloor.dungeonMap.stairsUpX;
                    currentFloorObj.linkedStairsY = newFloor.dungeonMap.stairsUpY;
                    newFloor.staircaseLinked = true;
                    newFloor.linkedFloor = currentFloor;
                    newFloor.linkedStairsX = currentFloorObj.dungeonMap.stairsDownX;
                    newFloor.linkedStairsY = currentFloorObj.dungeonMap.stairsDownY;

                    currentFloor = newFloorNum;
                    currentFloorObj = newFloor;
                    // Place the player at the new floor's up staircase.
                    player.setTile(newFloor.dungeonMap.stairsUpX, newFloor.dungeonMap.stairsUpY);
                } else {
                    System.out.println("Cannot create a new link from an up staircase.");
                }
            }
        }

        // New alternating combat logic:
        for (Monster monster : floors.get(currentFloor).monsters) {
            if (monster.isAlive()) {
                if (player.collidesWith(monster)) {
                    if (monster.combatTimer <= 0) {
                        if (monster.playerTurn) {
                            combatSystem.fight(player, monster);
                            if(!monster.isAlive())
                            {
                                Item lootItem = monster.getDrop();
                                if (lootItem != null) {
                                    LootDrop drop = new LootDrop(lootItem, monster.x, monster.y);
                                    lootDrops.add(drop);
                                    System.out.println("Loot dropped: " + lootItem.name);
                                }
                                questManager.updateProgress(1);
                            }
                        } else {
                            combatSystem.fight(monster, player);
                        }
                        monster.playerTurn = !monster.playerTurn;
                        monster.combatTimer = 0.6f;
                    } else {
                        monster.combatTimer -= delta;
                    }
                } else {
                    monster.combatTimer = 0;
                    monster.playerTurn = true;
                }
            }
        }

        // Update loot drops.
        for (int i = 0; i < lootDrops.size(); i++) {
            LootDrop drop = lootDrops.get(i);
            if (player.collidesWith(drop)) {
                simpleLootUI.setActive(drop);
                if (simpleLootUI.isPickupConfirmed()) {
                    player.inventory.addItem(drop.item);
                    lootDrops.remove(i);
                    i--;
                    simpleLootUI.reset();
                    System.out.println("Loot picked up: " + drop.item.name);
                } else if (simpleLootUI.isPickupDeclined()) {
                    lootDrops.remove(i);
                    i--;
                    simpleLootUI.reset();
                    System.out.println("Loot ignored: " + drop.item.name);
                }
            }
        }

        // Update UI panels.
        if (inventoryUI.isVisible())
            inventoryUI.update(player.inventory, player);
        if (equipmentUI.isVisible())
            equipmentUI.update(player, hudCamera);

        questManager.update();
    }

    public void renderVisual(ShapeRenderer shapeRenderer) {
        currentFloorObj.dungeonMap.render(shapeRenderer);
        for (LootDrop drop : lootDrops) {
            drop.render(shapeRenderer);
        }
        player.render(shapeRenderer);
        for (Monster monster : floors.get(currentFloor).monsters) {
            if (monster.isAlive()) {
                monster.render(shapeRenderer);
            }
        }
    }

    public void renderUI(SpriteBatch batch, ShapeRenderer shapeRenderer, OrthographicCamera hudCamera, OrthographicCamera worldCamera) {
        // Draw inventory/equipment panels using HUD camera.
        shapeRenderer.setProjectionMatrix(hudCamera.combined);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        inventoryUI.drawPanelBackground(shapeRenderer);
        equipmentUI.drawPanelBackground(shapeRenderer);
        shapeRenderer.end();

        batch.setProjectionMatrix(hudCamera.combined);
        batch.begin();
        inventoryUI.drawText(batch, player.inventory);
        equipmentUI.drawText(batch, player);
        batch.end();

        // Draw loot prompt and overhead text using the world camera.
        batch.setProjectionMatrix(worldCamera.combined);
        batch.begin();
        simpleLootUI.render(player);
        for(Monster monster : floors.get(currentFloor).monsters)
        {
            monster.drawOverhead();
        }
        player.drawOverhead();
        batch.end();

        // Draw minimap using HUD camera.
        batch.setProjectionMatrix(hudCamera.combined);
        batch.begin();
        minimap.render(currentFloorObj.dungeonMap, hudCamera, player);
        batch.end();
    }

    public void dispose() {
        // Dispose resources if necessary.
    }
}
