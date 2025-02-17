package com.tonic.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.tonic.DungeonCrawlerGame;
import com.tonic.systems.Inventory;
import com.tonic.entities.LootDrop;
import com.tonic.entities.Player;
import com.tonic.items.Armor;
import com.tonic.items.Food;
import com.tonic.items.Item;
import com.tonic.items.Weapon;

public class InventoryUI {
    private boolean visible = false;
    // Panel dimensions.
    private final float panelWidth;
    private final float panelHeight;
    // Panel position (recalculated each frame).
    private float panelX, panelY;
    private final float margin = 20f;

    // We'll store a reference to a BitmapFont so we can draw text.
    private final BitmapFont font;

    public InventoryUI(BitmapFont font) {
        this.font = font;
        panelWidth = 200;
        panelHeight = 200;
    }

    public void toggle() {
        visible = !visible;
    }

    public boolean isVisible() {
        return visible;
    }

    /**
     * Draws the colored panel background in HUD coordinates.
     * The caller has already begun shapeRenderer with hudCamera.
     */
    public void drawPanelBackground(ShapeRenderer shapeRenderer) {
        if (!visible) return;
        float screenWidth = Gdx.graphics.getWidth();
        panelX = screenWidth - panelWidth - margin;
        panelY = margin;

        shapeRenderer.setColor(new Color(0.1f, 0.1f, 0.1f, 0.8f));
        shapeRenderer.rect(panelX, panelY, panelWidth, panelHeight);
    }

    /**
     * Draws the text (item names) on top of the panel using the batch.
     * The caller has already begun the batch with hudCamera.
     */
    public void drawText(SpriteBatch batch, Inventory inventory) {
        if (!visible) return;

        font.setColor(Color.WHITE);
        int lineHeight = 15; // Adjust this value to set spacing between lines
        for (int i = 0; i < inventory.items.size(); i++) {
            float itemX = panelX + 5;
            float itemY = panelY + panelHeight - 5 - i * lineHeight;
            font.draw(batch, inventory.items.get(i).name, itemX, itemY);
        }
    }

    /**
     * Handles mouse clicks within the panel to equip/use or drop items.
     * Right-click: equip/use
     * Left-click: drop
     */
    public void update(Inventory inventory, Player player) {
        if (!visible) return;

        if (Gdx.input.justTouched()) {
            // Recalculate panel position
            float screenWidth = Gdx.graphics.getWidth();
            panelX = screenWidth - panelWidth - margin;
            panelY = margin;

            float mouseX = Gdx.input.getX();
            float mouseY = Gdx.graphics.getHeight() - Gdx.input.getY();

            if (mouseX >= panelX && mouseX <= panelX + panelWidth &&
                    mouseY >= panelY && mouseY <= panelY + panelHeight) {

                int lineHeight = 15; // Matches the text spacing
                int index = (int) ((panelY + panelHeight - mouseY) / lineHeight);

                if (index >= 0 && index < inventory.items.size()) {
                    Item item = inventory.items.get(index);
                    if (Gdx.input.isButtonPressed(Input.Buttons.RIGHT)) {
                        // Right-click: equip/use
                        if (item instanceof Weapon) {
                            player.equip((Weapon) item);
                        } else if (item instanceof Armor) {
                            player.equip((Armor) item);
                        } else if (item instanceof Food) {
                            player.eat((Food) item);
                        }
                    } else if (Gdx.input.isButtonPressed(Input.Buttons.LEFT)) {
                        // Left-click: drop
                        inventory.items.remove(index);
                        DungeonCrawlerGame.instance.engine.lootDrops.add(new LootDrop(item, player.x, player.y));
                    }
                }
            }
        }
    }
}
