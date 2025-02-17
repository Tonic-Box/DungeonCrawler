package com.tonic.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.tonic.entities.Player;

public class SimpleEquipmentUI {
    private boolean visible = false;
    // Panel dimensions.
    private float panelWidth, panelHeight;
    // Panel position (recalculated each frame).
    private float panelX, panelY;
    private final float margin = 20f;

    // Reference to a BitmapFont for text.
    private BitmapFont font;

    public SimpleEquipmentUI(BitmapFont font) {
        this.font = font;
        panelWidth = 200;
        panelHeight = 100;
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

        shapeRenderer.setColor(new Color(0.15f, 0.15f, 0.15f, 0.8f));
        shapeRenderer.rect(panelX, panelY, panelWidth, panelHeight);
    }

    /**
     * Draws the text for the equipped weapon/armor.
     * The caller has already begun batch with hudCamera.
     */
    public void drawText(SpriteBatch batch, Player player) {
        if (!visible) return;

        font.setColor(Color.WHITE);

        // Recalc panel pos to ensure text lines up with background.
        float screenWidth = Gdx.graphics.getWidth();
        panelX = screenWidth - panelWidth - margin;
        panelY = margin;

        String weaponText = "Weapon: " + (player.equipment.getEquipped("Weapon") != null
                ? player.equipment.getEquipped("Weapon").name : "None");
        String armorText = "Armor: " + (player.equipment.getEquipped("Armor") != null
                ? player.equipment.getEquipped("Armor").name : "None");

        float weaponY = panelY + panelHeight - 10;
        float armorY  = panelY + panelHeight - 40;
        float textX   = panelX + 10;

        font.draw(batch, weaponText, textX, weaponY);
        font.draw(batch, armorText,  textX, armorY);
    }

    /**
     * Checks for mouse clicks to unequip items.
     * If near top of panel => unequip weapon; if lower => unequip armor.
     */
    public void update(Player player, OrthographicCamera hudCamera) {
        if (!visible) return;
        if (Gdx.input.justTouched()) {
            float screenWidth = Gdx.graphics.getWidth(); // or hudCamera.viewportWidth
            panelX = screenWidth - panelWidth - margin;
            panelY = margin;

            float mouseX = Gdx.input.getX();
            float mouseY = Gdx.graphics.getHeight() - Gdx.input.getY();

            if (mouseX >= panelX && mouseX <= panelX + panelWidth &&
                    mouseY >= panelY && mouseY <= panelY + panelHeight) {
                // If click is near the top, assume weapon slot.
                if (mouseY >= panelY + panelHeight - 20) {
                    player.unEquipWeapon();
                }
                // If it's lower, assume armor slot.
                else if (mouseY >= panelY + panelHeight - 50 && mouseY < panelY + panelHeight - 20) {
                    player.unEquipArmor();
                }
            }
        }
    }
}
