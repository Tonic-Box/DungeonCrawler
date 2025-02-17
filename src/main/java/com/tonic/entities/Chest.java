package com.tonic.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.tonic.DungeonCrawlerGame;
import com.tonic.items.Item;
import com.tonic.items.Weapon;
import com.tonic.systems.DungeonMap;
import com.tonic.systems.LootTable;
import com.tonic.util.MapUtil;

import java.util.ArrayList;
import java.util.List;

public class Chest extends Entity {
    public static List<Chest> generateChest(DungeonMap map, int count)
    {
        List<Chest> chests = new ArrayList<>();
        for (int i = 0; i < count; i++)
        {
            Chest chest = new Chest("Chest " + i, 0, Color.BROWN, Color.YELLOW);
            MapUtil.placeRandomlyOn(chest, map);
            chests.add(chest);
        }
        return chests;
    }
    private boolean opened = false;

    public Chest(String name, int xp, Color color, Color overheadColor) {
        super(name, xp, color, overheadColor);
        lootTable = LootTable.generate();
    }

    @Override
    public void render(ShapeRenderer shapeRenderer) {
        // Define chest dimensions.
        float chestWidth = 30f;
        float chestHeight = 20f;
        // Calculate base position so that (x,y) is the center.
        float baseX = x - chestWidth / 2;
        float baseY = y - chestHeight / 2;

        // Draw the chest base.
        shapeRenderer.setColor(color);
        shapeRenderer.rect(baseX, baseY, chestWidth, chestHeight);

        if (!opened) {
            // --- Closed Chest ---
            // Draw a horizontal line at the top edge of the base to indicate the lid's edge.
            shapeRenderer.setColor(Color.DARK_GRAY);
            shapeRenderer.line(baseX, baseY + chestHeight, baseX + chestWidth, baseY + chestHeight);

            // Draw a small lock in the center.
            float lockWidth = 6f;
            float lockHeight = 8f;
            float lockX = x - lockWidth / 2;
            float lockY = baseY + (chestHeight - lockHeight) / 2;
            shapeRenderer.rect(lockX, lockY, lockWidth, lockHeight);
        } else {
            // --- Open Chest ---
            // Draw the open lid as a filled quadrilateral by splitting it into two triangles.
            shapeRenderer.setColor(color);
            float lidHeight = chestHeight * 0.6f;
            // Define the four corners of the lid:
            float blX = baseX;                    // bottom left (attached to chest)
            float blY = baseY + chestHeight;
            float brX = baseX + chestWidth;         // bottom right (attached to chest)
            float brY = baseY + chestHeight;
            float trX = baseX + chestWidth - 5;       // top right (lid swung open)
            float trY = baseY + chestHeight + lidHeight;
            float tlX = baseX + 5;                  // top left (lid swung open)
            float tlY = baseY + chestHeight + lidHeight;

            // Draw two triangles to fill the quadrilateral.
            shapeRenderer.triangle(blX, blY, brX, brY, trX, trY);
            shapeRenderer.triangle(blX, blY, trX, trY, tlX, tlY);

            // Draw hinge lines to indicate where the lid attaches.
            shapeRenderer.setColor(Color.DARK_GRAY);
            shapeRenderer.line(baseX, baseY + chestHeight, baseX + 5, baseY + chestHeight + lidHeight);
            shapeRenderer.line(baseX + chestWidth, baseY + chestHeight, baseX + chestWidth - 5, baseY + chestHeight + lidHeight);
        }

        // If the chest is already opened, no further interaction.
        if (opened)
            return;

        Player player = Player.getInstance();
        if (overlapping(player)) {
            player.setOverheadText("Press Q to " + (opened ? "close" : "open") + " the chest", 0.1f);

            if (Gdx.input.isKeyJustPressed(Input.Keys.Q)) {
                opened = true;
                DungeonCrawlerGame.instance.engine.lootDrops.add(new LootDrop(getDrop(), x, y));
            }
        }
    }

    // (Optional) Toggle the chest's state
    public void toggle() {
        opened = !opened;
    }
}
