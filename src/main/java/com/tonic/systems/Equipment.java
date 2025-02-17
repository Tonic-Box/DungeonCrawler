package com.tonic.systems;

import com.tonic.items.Item;

import java.util.HashMap;
import java.util.Map;

public class Equipment {
    // A simple mapping for equipment slots (e.g., "weapon", "armor").
    public Map<String, Item> slots;

    public Equipment() {
        slots = new HashMap<>();
    }

    public void equip(String slot, Item item) {
        slots.put(slot, item);
    }

    public void unequip(String slot) {
        slots.remove(slot);
    }

    public Item getEquipped(String slot) {
        return slots.get(slot);
    }
}
