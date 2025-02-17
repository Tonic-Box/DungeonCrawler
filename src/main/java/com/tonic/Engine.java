package com.tonic;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.tonic.entities.LootDrop;
import com.tonic.entities.Monster;
import com.tonic.entities.Player;
import com.tonic.items.Item;
import com.tonic.items.Weapon;
import com.tonic.systems.*;
import com.tonic.ui.Minimap;
import com.tonic.ui.SimpleEquipmentUI;
import com.tonic.ui.SimpleInventoryUI;
import com.tonic.ui.SimpleLootUI;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Engine {
    public Player player;
    public List<Monster> monsters;
    public CombatSystem combatSystem;
    public QuestManager questManager;
    public Map<Integer, Floor> floors;
    public int currentFloor;
    public Floor currentFloorObj;
    public List<LootDrop> lootDrops;
    private long baseSeed = 12345;

    // Simple UI panels (drawn manually)
    public SimpleInventoryUI simpleInventoryUI;
    public SimpleEquipmentUI simpleEquipmentUI;
    public SimpleLootUI simpleLootUI; // Loot prompt UI
    public Minimap minimap;

    // Map for staircase links.
    public Map<String, StaircaseLink> staircaseLinks = new HashMap<>();

    private String getStairKey(int floor, int tileX, int tileY, String side) {
        return floor + ":" + tileX + "," + tileY + ":" + side;
    }

    public Engine() {
        monsters = new ArrayList<>();
        combatSystem = new CombatSystem();
        questManager = new QuestManager();
        floors = new HashMap<>();
        lootDrops = new ArrayList<>();
    }

    public void init() {
        currentFloor = 0;
        long seed = baseSeed + currentFloor;
        Floor floor0 = new Floor(50, 40, seed, false);
        floors.put(currentFloor, floor0);
        currentFloorObj = floor0;

        player = new Player("Hero", 100, 10, 2);
        int[] spawn = currentFloorObj.getRandomFloorTile();
        player.x = spawn[0] * DungeonMap.TILE_SIZE + DungeonMap.TILE_SIZE/2;
        player.y = spawn[1] * DungeonMap.TILE_SIZE + DungeonMap.TILE_SIZE/2;
        Weapon sword = new Weapon("Sword", 5);
        player.inventory.addItem(sword);

        Monster goblin = new Monster("Goblin", 30, 5, 1);
        goblin.placeRandomlyOn(currentFloorObj.dungeonMap);
        monsters.add(goblin);

        goblin = new Monster("Goblin", 30, 5, 1);
        goblin.placeRandomlyOn(currentFloorObj.dungeonMap);
        monsters.add(goblin);

        goblin = new Monster("Goblin", 30, 5, 1);
        goblin.placeRandomlyOn(currentFloorObj.dungeonMap);
        monsters.add(goblin);

        goblin = new Monster("Goblin", 30, 5, 1);
        goblin.placeRandomlyOn(currentFloorObj.dungeonMap);
        monsters.add(goblin);

        goblin = new Monster("Goblin", 30, 5, 1);
        goblin.placeRandomlyOn(currentFloorObj.dungeonMap);
        monsters.add(goblin);

        goblin = new Monster("Goblin", 30, 5, 1);
        goblin.placeRandomlyOn(currentFloorObj.dungeonMap);
        monsters.add(goblin);

        goblin = new Monster("Goblin", 30, 5, 1);
        goblin.placeRandomlyOn(currentFloorObj.dungeonMap);
        monsters.add(goblin);

        Monster orc = new Monster("Orc", 50, 8, 3);
        orc.placeRandomlyOn(currentFloorObj.dungeonMap);
        monsters.add(orc);

        orc = new Monster("Orc", 50, 8, 3);
        orc.placeRandomlyOn(currentFloorObj.dungeonMap);
        monsters.add(orc);

        orc = new Monster("Orc", 50, 8, 3);
        orc.placeRandomlyOn(currentFloorObj.dungeonMap);
        monsters.add(orc);


        Quest quest = new Quest("Defeat all monsters", "Eliminate all monsters in the dungeon",
                Quest.QuestType.DEFEAT, monsters.size());
        questManager.addQuest(quest);

        BitmapFont font = new BitmapFont();
        simpleInventoryUI = new SimpleInventoryUI(font);
        simpleEquipmentUI = new SimpleEquipmentUI(font);
        simpleLootUI = new SimpleLootUI(font);
        minimap = new Minimap();
    }

    public void update(float delta, OrthographicCamera hudCamera) {
        if (Gdx.input.isKeyJustPressed(Input.Keys.I)) {
            if (simpleEquipmentUI.isVisible()) simpleEquipmentUI.toggle();
            simpleInventoryUI.toggle();
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.E)) {
            if (simpleInventoryUI.isVisible()) simpleInventoryUI.toggle();
            simpleEquipmentUI.toggle();
        }

        player.update(delta, currentFloorObj.dungeonMap);

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
                player.x = link.targetX * DungeonMap.TILE_SIZE + DungeonMap.TILE_SIZE/2;
                player.y = link.targetY * DungeonMap.TILE_SIZE + DungeonMap.TILE_SIZE/2;
            } else {
                if (onStairs == 1) {
                    System.out.println("Creating new floor via staircase linking.");
                    int newFloorNum = currentFloor + 1;
                    long seed = baseSeed + newFloorNum;
                    int forcedUpX = playerTileX;
                    int forcedUpY = playerTileY;
                    Floor newFloor = new Floor(50, 40, seed, forcedUpX, forcedUpY);
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
                    player.x = newFloor.dungeonMap.stairsUpX * DungeonMap.TILE_SIZE + DungeonMap.TILE_SIZE/2;
                    player.y = newFloor.dungeonMap.stairsUpY * DungeonMap.TILE_SIZE + DungeonMap.TILE_SIZE/2;
                } else {
                    System.out.println("Cannot create a new link from an up staircase.");
                }
            }
        }

        // New alternating combat logic:
        for (Monster monster : monsters) {
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
        if (simpleInventoryUI.isVisible())
            simpleInventoryUI.update(player.inventory, player);
        if (simpleEquipmentUI.isVisible())
            simpleEquipmentUI.update(player, hudCamera);

        questManager.update();
    }

    public void renderVisual(ShapeRenderer shapeRenderer) {
        currentFloorObj.dungeonMap.render(shapeRenderer);
        for (LootDrop drop : lootDrops) {
            drop.render(shapeRenderer);
        }
        player.render(shapeRenderer);
        for (Monster monster : monsters) {
            if (monster.isAlive()) {
                monster.render(shapeRenderer);
            }
        }
    }

    public void renderUI(SpriteBatch batch, ShapeRenderer shapeRenderer, OrthographicCamera hudCamera, OrthographicCamera worldCamera) {
        // Draw inventory/equipment panels using HUD camera.
        shapeRenderer.setProjectionMatrix(hudCamera.combined);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        simpleInventoryUI.drawPanelBackground(shapeRenderer);
        simpleEquipmentUI.drawPanelBackground(shapeRenderer);
        shapeRenderer.end();

        batch.setProjectionMatrix(hudCamera.combined);
        batch.begin();
        simpleInventoryUI.drawText(batch, player.inventory);
        simpleEquipmentUI.drawText(batch, player);
        batch.end();

        // Draw loot prompt and overhead text using the world camera.
        batch.setProjectionMatrix(worldCamera.combined);
        batch.begin();
        simpleLootUI.render(player);
        for(Monster monster : monsters)
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
