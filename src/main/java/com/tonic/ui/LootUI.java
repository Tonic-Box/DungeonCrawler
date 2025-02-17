package com.tonic.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.tonic.DungeonCrawlerGame;
import com.tonic.entities.LootDrop;
import com.tonic.entities.Player;
import lombok.Getter;

public class LootUI {
    private boolean active = false;
    @Getter
    private boolean pickupConfirmed = false;
    @Getter
    private boolean pickupDeclined = false;
    private LootDrop currentLoot = null;
    private BitmapFont font;
    private GlyphLayout layout;

    public LootUI(BitmapFont font) {
        this.font = font;
        this.layout = new GlyphLayout();
    }

    /**
     * Activates the loot prompt for a given loot drop.
     */
    public void setActive(LootDrop drop) {
        if (!active) {
            active = true;
            currentLoot = drop;
            pickupConfirmed = false;
            pickupDeclined = false;
            System.out.println("SimpleLootUI activated for: " + drop.item.name);
        }
    }

    /**
     * Resets the loot UI state.
     */
    public void reset() {
        active = false;
        pickupConfirmed = false;
        pickupDeclined = false;
        currentLoot = null;
    }

    /**
     * Renders the loot prompt text above the player's position.
     * Assumes that the SpriteBatch has already begun with the world camera's combined matrix.
     *
     * @param player       The player (whose position is used to place the text)
     */
    public void render(Player player) {
        if (!active || currentLoot == null || !currentLoot.playerWithin(player))
        {
            if(player.getOverheadText().startsWith("Pick up "))
            {
                player.setOverheadText("", -1);
            }
            return;
        }
        player.setOverheadText("Pick up " + currentLoot.item.name + "? (Y/N)", -1);

        if (Gdx.input.isKeyJustPressed(Input.Keys.Y)) {
            pickupConfirmed = true;
        } else if (Gdx.input.isKeyJustPressed(Input.Keys.N)) {
            pickupDeclined = true;
        }
    }

    public static LootUI getInstance()
    {
        return DungeonCrawlerGame.instance.engine.simpleLootUI;
    }
}
