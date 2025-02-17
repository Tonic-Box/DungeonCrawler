package com.tonic.systems;

import com.tonic.items.Armor;
import com.tonic.items.Food;
import com.tonic.items.Item;
import com.tonic.items.Weapon;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class LootTable
{
    private final List<Item> items = new ArrayList<>();
    private final Random random = new Random();
    public static LootTable get()
    {
        return new LootTable();
    }

    public static List<Item> generate()
    {
        return get()
                .addBread()
                .addApple()
                .addMeal()
                .addLpWeapon("Dagger")
                .addHpWeapon("Scimitar")
                .addLeatherArmor()
                .addSteelArmor()
                .addGoldArmor()
                .build();
    }

    public LootTable addWeapon(String name, int power)
    {
        items.add(new Weapon(name, power));
        return this;
    }

    public LootTable addLpWeapon(String name)
    {
        items.add(new Weapon(name, random.nextInt(7) + 1));
        return this;
    }

    public LootTable addHpWeapon(String name)
    {
        items.add(new Weapon(name, random.nextInt(15) + 1));
        return this;
    }

    public LootTable addApple()
    {
        items.add(new Food("Apple", 15));
        return this;
    }

    public LootTable addBread()
    {
        items.add(new Food("Bread", 15));
        return this;
    }

    public LootTable addMeal()
    {
        items.add(new Food("Meal", 25));
        return this;
    }

    public LootTable addLeatherArmor()
    {
        items.add(new Armor("Leather Armor", 10));
        return this;
    }

    public LootTable addSteelArmor()
    {
        items.add(new Armor("Steel Armor", 20));
        return this;
    }

    public LootTable addGoldArmor()
    {
        items.add(new Armor("Gold Armor", 30));
        return this;
    }

    public List<Item> build()
    {
        return items;
    }
}
