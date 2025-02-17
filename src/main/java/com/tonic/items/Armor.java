package com.tonic.items;

public class Armor extends Item {
    public int defense;

    public Armor(String name, int defense) {
        super(name);
        this.defense = defense;
    }

    @Override
    public String getStats() {
        return "Defense: " + defense;
    }
}
