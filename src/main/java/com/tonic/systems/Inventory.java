package com.tonic.systems;

import com.tonic.items.Item;

import java.util.ArrayList;
import java.util.List;

public class Inventory {
    public List<Item> items;

    public Inventory() {
        items = new ArrayList<>();
    }

    public void addItem(Item item) {
        items.add(item);
    }

    public String getItemNames() {
        StringBuilder sb = new StringBuilder();
        for (Item item : items) {
            sb.append(item.name).append(", ");
        }
        return sb.toString();
    }
}
