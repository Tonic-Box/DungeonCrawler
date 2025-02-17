package com.tonic.systems;

import com.tonic.entities.Entity;
import com.tonic.entities.Player;
import com.tonic.items.Item;
import com.tonic.items.Weapon;

public class CombatSystem {

    public void fight(Entity attacker, Entity defender) {
        // Base damage is the attacker's base stat.
        int baseDamage = attacker.attack;
        // If the attacker is a player, add any weapon bonus.
        if (attacker instanceof Player) {
            for (Item item : attacker.inventory.items) {
                if (item instanceof Weapon) {
                    baseDamage += ((Weapon) item).damage;
                    break;
                }
            }
        }
        defender.takeDamage(baseDamage);
        attacker.setOverheadText("");
    }
}
