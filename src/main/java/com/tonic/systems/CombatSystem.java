package com.tonic.systems;

import com.tonic.entities.Entity;
import com.tonic.entities.Player;
import com.tonic.items.Item;
import com.tonic.items.Weapon;

import java.util.Random;

public class CombatSystem {
    private final Random random = new Random();
    public void fight(Entity attacker, Entity defender) {
        defender.takeDamage(calculateDamage(attacker, defender));
        attacker.setOverheadText("", -1);
    }

    private int calculateDamage(Entity attacker, Entity defender) {
        int baseDamage = attacker.attack;

        // Add weapon bonus if attacker is a player
        if (attacker instanceof Player) {
            for (Item item : attacker.inventory.items) {
                if (item instanceof Weapon) {
                    baseDamage += ((Weapon) item).damage;
                    break;
                }
            }
        }

        // Attack Roll: Attacker rolls between 1 and (attack stat * 2)
        int attackRoll = random.nextInt(attacker.attack * 2) + 1;

        // Defense Roll: Defender rolls between 1 and (defense stat * 2)
        int defenseRoll = random.nextInt(defender.defense * 2) + 1;

        int damageDealt = 0;

        if (attackRoll > defenseRoll) {
            // Successful hit
            damageDealt = baseDamage;

            // Critical Hit: Attack roll is significantly higher than defense
            if (attackRoll >= defenseRoll * 1.5) {
                damageDealt = (int) Math.ceil(damageDealt * 1.5); // 50% more damage
            }
            // Glancing Blow: Attack barely succeeded
            else if (attackRoll <= defenseRoll * 1.1) {
                damageDealt = (int) Math.floor(damageDealt * 0.5); // 50% less damage
                damageDealt = Math.max(damageDealt, 1); // Ensure at least 1 damage is dealt.
            }
        }
        return damageDealt;
    }
}
