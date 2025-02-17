package com.tonic.items;

public class Food extends Item {
    public int healAmount;

    public Food(String name, int healAmount) {
        super(name);
        this.healAmount = healAmount;
    }

    @Override
    public String getStats() {
        return "Heals: " + healAmount;
    }
}
