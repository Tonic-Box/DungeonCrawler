package com.tonic.ui;

import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.tonic.DungeonCrawlerGame;
import com.tonic.entities.LootDrop;
import com.tonic.entities.Player;
import com.tonic.items.Armor;
import com.tonic.items.Food;
import com.tonic.items.Item;
import com.tonic.items.Weapon;

public class ContextMenu extends Window {
    public ContextMenu(Skin skin, final Item item, final Player player) {
        super("Options", skin);
        setModal(true);
        // For weapons and gear.
        if (item instanceof Weapon || item instanceof Armor) {
            TextButton equipButton = new TextButton("Equip", skin);
            equipButton.addListener(new ChangeListener(){
                @Override
                public void changed(ChangeEvent event, Actor actor) {
                    if (item instanceof Weapon) {
                        player.equipment.equip("Weapon", item);
                    } else if (item instanceof Armor) {
                        player.equipment.equip("Armor", item);
                    }
                    player.inventory.items.remove(item);
                    remove();
                }
            });
            add(equipButton).row();
        } else if (item instanceof Food) {  // For food.
            TextButton eatButton = new TextButton("Eat", skin);
            eatButton.addListener(new ChangeListener(){
                @Override
                public void changed(ChangeEvent event, Actor actor) {
                    player.health = Math.min(player.maxHealth, player.health + ((Food)item).healAmount);
                    player.inventory.items.remove(item);
                    remove();
                }
            });
            add(eatButton).row();
        }
        // "Drop" option (available for all items).
        TextButton dropButton = new TextButton("Drop", skin);
        dropButton.addListener(new ChangeListener(){
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                player.inventory.items.remove(item);
                // Drop the item at the player's position.
                DungeonCrawlerGame.instance.engine.lootDrops.add(new LootDrop(item, player.x, player.y));
                remove();
            }
        });
        add(dropButton).row();
        pack();
    }

    public void show(Stage stage, float x, float y) {
        setPosition(x, y);
        stage.addActor(this);
    }
}
