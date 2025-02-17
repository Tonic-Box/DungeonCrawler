package com.tonic.entities;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.tonic.systems.DungeonMap;
import com.tonic.systems.LootTable;
import com.tonic.items.Item;
import com.tonic.util.MapUtil;
import com.tonic.util.XpLevelUtil;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class Monster extends Entity {
    private static final List<MonsterDef> monsterMapWeak = List.of(
            new MonsterDef(1, "Goblin", Color.FIREBRICK),
            new MonsterDef(1, "Orc", Color.RED)
    );

    private static final List<MonsterDef> monsterMapMedium = List.of(
            new MonsterDef(3, "Troll", Color.BROWN),
            new MonsterDef(3, "Ogre", Color.CORAL)
    );

    private static final List<MonsterDef> monsterMapStrong = List.of(
            new MonsterDef(5, "Dragon", Color.CYAN),
            new MonsterDef(5, "Demon", Color.PURPLE)
    );

    public static List<Monster> genList(DungeonMap map, int level) {
        List<Monster> monsters = new ArrayList<>();
        int numWeak = level > 5 ? 5 + (level * 3) / 2 : level + 3;
        int numMedium = level > 10 ? 3 + (level * 3) / 2 : level;
        int numStrong = level > 15 ? 2 + (level / 7) : level / 6;
        //add an amount of random monsters based on level from each list
        for (int i = 0; i < numWeak; i++) {
            Monster monster = monsterMapWeak.get(new Random().nextInt(monsterMapWeak.size())).createMonster(map);
            monsters.add(monster);
        }
        for (int i = 0; i < numMedium; i++) {
            monsters.add(monsterMapMedium.get(new Random().nextInt(monsterMapMedium.size())).createMonster(map));
        }
        for (int i = 0; i < numStrong; i++) {
            monsters.add(monsterMapStrong.get(new Random().nextInt(monsterMapStrong.size())).createMonster(map));
        }
        System.out.println("Monsters: " + monsters.size());
        return monsters;
    }

    private float moveTimer = 0;
    private float moveInterval;
    private final float wanderDistance = 16; // Pixels per move.

    // New fields for combat
    public float combatTimer = 0;
    public boolean playerTurn = true;

    public Monster(String name, int level, Color color) {
        super(name, XpLevelUtil.randomXpForLevel(level), color, Color.YELLOW);
        //random float between .5 and 2.5
        moveInterval = MathUtils.random(.5f, 2.5f);
        lootTable = LootTable.generate();
    }

    /**
     * Updates the monster's movement.
     */
    @Override
    public void update(float delta, DungeonMap dungeonMap) {
        super.update(delta, dungeonMap);
        if(combatTimer > 0) {
            return;
        }
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

    @Data
    private static class MonsterDef
    {
        private final int level;
        private final String name;
        private final Color color;

        public Monster createMonster(DungeonMap map) {
            Monster monster = new Monster(name, level, color);
            MapUtil.placeRandomlyOn(monster, map);
            return monster;
        }
    }
}
