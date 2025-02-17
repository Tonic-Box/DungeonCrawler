package com.tonic.entities;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.tonic.DungeonCrawlerGame;
import com.tonic.systems.Inventory;

public abstract class Entity {
    // A string that can be displayed over the player’s head.
    private String overheadText;
    private GlyphLayout layout = new GlyphLayout();
    private BitmapFont font = new BitmapFont();

    // A font for that overhead text (you could share a global font instead).
    public BitmapFont overheadFont;
    public String name;
    public int maxHealth;
    public int health;
    public int attack;
    public int defense;
    public Inventory inventory;
    // Position in pixel coordinates.
    public float x, y;

    public Entity(String name, int maxHealth, int attack, int defense, Color overheadColor) {
        this.name = name;
        this.maxHealth = maxHealth;
        this.health = maxHealth;
        this.attack = attack;
        this.defense = defense;
        this.inventory = new Inventory();
        this.overheadText = "";
        this.overheadFont = new BitmapFont();
        this.overheadFont.setColor(overheadColor);
        this.x = 0;
        this.y = 0;
    }

    public boolean isAlive() {
        return health > 0;
    }

    public boolean collidesWith(Entity other) {
        // Simple collision based on distance.
        float dx = this.x - other.x;
        float dy = this.y - other.y;
        float distance = (float) Math.sqrt(dx * dx + dy * dy);
        return distance < 20; // Collision threshold in pixels.
    }

    public void takeDamage(int damage) {
        int netDamage = Math.max(damage - defense, 0);
        health -= netDamage;
        if (health <= 0) {
            health = 0;
            setOverheadText("");
        }
        else
        {
            setOverheadText(name + " takes " + netDamage + " damage!");
        }
    }

    // Default render method (subclasses should override).
    public void render(com.badlogic.gdx.graphics.glutils.ShapeRenderer shapeRenderer) {
        shapeRenderer.circle(x, y, 10);
    }

    public void update(float delta) {
        // Override if needed.
    }

    public void drawOverhead()
    {
        float offset = layout.width / 2;
        overheadFont.draw(DungeonCrawlerGame.instance.batch, overheadText, x - offset + 5, y + 25);
    }

    public void setOverheadText(String text)
    {
        overheadText = text;
        layout.setText(font, text);
    }

    public String getOverheadText()
    {
        return overheadText;
    }
}
