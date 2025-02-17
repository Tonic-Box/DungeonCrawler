package com.tonic.items;

public class Weapon extends Item {
    public int damage;

    public Weapon(String name, int damage) {
        super(name);
        this.damage = damage;
    }

    @Override
    public String getStats() {
        return "Damage: " + damage;
    }
}
