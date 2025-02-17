package com.tonic.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.tonic.DungeonCrawlerGame;
import com.tonic.items.Armor;
import com.tonic.items.Food;
import com.tonic.items.Weapon;
import com.tonic.systems.DungeonMap;
import com.tonic.systems.Equipment;
import com.tonic.util.XpLevelUtil;

import java.util.logging.Level;

/**
 * A player entity with equipment and overhead text.
 */
public class Player extends Entity {
    public Equipment equipment;

    public Player(String name, int level) {
        super(name, XpLevelUtil.randomXpForLevel(level), Color.GOLD, Color.RED);
        this.x = 0;
        this.y = 0;

        equipment = new Equipment();
    }

    public static Player getInstance() {
        return DungeonCrawlerGame.instance.engine.player;
    }

    @Override
    public void update(float delta, DungeonMap dungeonMap) {
        super.update(delta, dungeonMap);
        float speed = 200 * delta;
        float newX = x;
        float newY = y;
        if (Gdx.input.isKeyPressed(Input.Keys.LEFT))  newX -= speed;
        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) newX += speed;
        if (Gdx.input.isKeyPressed(Input.Keys.UP))    newY += speed;
        if (Gdx.input.isKeyPressed(Input.Keys.DOWN))  newY -= speed;

        if (isWalkable(newX, newY, dungeonMap)) {
            x = newX;
            y = newY;
        }
    }

    private boolean isWalkable(float newX, float newY, DungeonMap dungeonMap) {
        int tileX = (int)(newX / DungeonMap.TILE_SIZE);
        int tileY = (int)(newY / DungeonMap.TILE_SIZE);
        if (tileX < 0 || tileY < 0 || tileX >= dungeonMap.width || tileY >= dungeonMap.height)
            return false;
        return dungeonMap.tiles[tileX][tileY] == 1;
    }

    @Override
    public void render(ShapeRenderer shapeRenderer) {
        shapeRenderer.setColor(0, 1, 0, 1);
        shapeRenderer.circle(x, y, 10);
    }

    public void equip(Weapon weapon)
    {
        unEquipWeapon();
        equipment.equip("Weapon", weapon);
        inventory.items.remove(weapon);
    }

    public void equip(Armor armor)
    {
        unEquipArmor();
        equipment.equip("Armor", armor);
        inventory.items.remove(armor);
    }

    public void unEquipWeapon()
    {
        if(equipment.getEquipped("Weapon") == null)
            return;
        inventory.items.add(equipment.getEquipped("Weapon"));
        equipment.unequip("Weapon");
    }

    public void unEquipArmor()
    {
        if(equipment.getEquipped("Armor") == null)
            return;
        inventory.items.add(equipment.getEquipped("Armor"));
        equipment.unequip("Armor");
    }

    public void eat(Food food)
    {
        health += food.healAmount;
        inventory.items.remove(food);
    }
}
