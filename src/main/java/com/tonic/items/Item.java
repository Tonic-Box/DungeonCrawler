package com.tonic.items;

public abstract class Item {
    public String name;

    public Item(String name) {
        this.name = name;
    }

    // Returns a string describing the item's stats (for tooltips)
    public String getStats() {
        return "";
    }
}
