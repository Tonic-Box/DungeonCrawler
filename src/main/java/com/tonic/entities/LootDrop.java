package com.tonic.entities;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.tonic.items.Item;

public class LootDrop extends Entity {
    public Item item;

    public LootDrop(Item item, float x, float y) {
        super(item.name, 0, Color.CYAN, Color.LIME);
        this.item = item;
        this.x = x;
        this.y = y;
    }

    public boolean playerWithin(Player player) {
        float dx = this.x - player.x;
        float dy = this.y - player.y;
        float distance = (float)Math.sqrt(dx * dx + dy * dy);
        return distance < 20; // returns true if within 20 pixels
    }

    @Override
    public void render(ShapeRenderer shapeRenderer) {
        shapeRenderer.setColor(Color.GOLD);
        shapeRenderer.circle(x, y, 8);
    }
}
