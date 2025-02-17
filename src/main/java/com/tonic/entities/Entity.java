package com.tonic.entities;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.tonic.DungeonCrawlerGame;
import com.tonic.items.Item;
import com.tonic.systems.DungeonMap;
import com.tonic.systems.Inventory;
import com.tonic.util.XpLevelUtil;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public abstract class Entity {
    protected final Random random = new Random();
    @Getter
    private String overheadText;
    private float duration = 0;
    private final GlyphLayout layout = new GlyphLayout();
    private final BitmapFont font = new BitmapFont();
    protected final Color color;
    public BitmapFont overheadFont;
    public String name;
    public int xp;
    public int maxHealth;
    public int health;
    public int attack;
    public int defense;
    public Inventory inventory;
    protected List<Item> lootTable = new ArrayList<>();

    public float x, y;

    public Entity(String name, int xp, Color color, Color overheadColor) {
        this.name = name;
        this.xp = xp;
        int level = XpLevelUtil.toLevel(xp);
        this.maxHealth = calculateHealth(level);
        this.health = maxHealth;
        this.attack = calculateAttack(level);
        this.defense = calculateDefense(level);
        this.inventory = new Inventory();
        this.overheadText = "";
        this.overheadFont = new BitmapFont();
        this.overheadFont.setColor(overheadColor);
        this.color = color;
        this.x = 0;
        this.y = 0;
    }

    private int calculateHealth(int level) {
        return 50 + (level * 10) + (int) (Math.pow(level, 1.5)); // Scales exponentially with level
    }

    private int calculateAttack(int level) {
        return 5 + (level * 2) + (int) (Math.log(level + 1) * 5); // Slightly logarithmic scaling
    }

    private int calculateDefense(int level) {
        return level + (int) (Math.sqrt(level) * 2); // Slower scaling for defense
    }

    public boolean isAlive() {
        return health > 0;
    }

    public boolean collidesWith(Entity other) {
        float dx = this.x - other.x;
        float dy = this.y - other.y;
        float distance = (float) Math.sqrt(dx * dx + dy * dy);
        return distance < 20;
    }

    public void takeDamage(int damage) {
        int netDamage = Math.max(damage - defense, 0);
        health -= netDamage;
        if (health <= 0) {
            health = 0;
            setOverheadText("", -1);
        } else {
            setOverheadText(name + " takes " + netDamage + " damage!", 1f);
        }
    }

    public void render(ShapeRenderer shapeRenderer) {
        shapeRenderer.setColor(color);
        shapeRenderer.circle(x, y, 10);
    }

    public void update(float delta, DungeonMap dungeonMap) {
        if(duration > 0) {
            duration -= delta;
            if(duration <= 0) {
                setOverheadText("", -1);
            }
        }
    }

    public void drawOverhead() {
        float offset = layout.width / 2;
        overheadFont.draw(DungeonCrawlerGame.instance.batch, overheadText, x - offset + 5, y + 25);
    }

    public void setOverheadText(String text, float duration) {
        this.duration = duration;
        overheadText = text;
        layout.setText(font, text);
    }

    public void setTile(int x, int y) {
        this.x = x * DungeonMap.TILE_SIZE + DungeonMap.TILE_SIZE / 2;
        this.y = y * DungeonMap.TILE_SIZE + DungeonMap.TILE_SIZE / 2;
    }

    public void setTile(int[] tile) {
        setTile(tile[0], tile[1]);
    }

    public void gainXp(int xp) {
        this.xp += xp;
        int level = XpLevelUtil.toLevel(this.xp);
        int newMaxHealth = calculateHealth(level);
        int healthDiff = newMaxHealth - maxHealth;
        maxHealth = newMaxHealth;
        health += healthDiff;
        attack = calculateAttack(level);
        defense = calculateDefense(level);
    }

    public void giveXp(Entity player) {
        int npcLevel = XpLevelUtil.toLevel(this.xp);
        int playerLevel = XpLevelUtil.toLevel(player.xp);

        // Base XP formula
        double baseXp = (maxHealth * 0.5) + (attack * 1.5) + (defense * 1.2);

        // Level difference scaling
        int levelDifference = playerLevel - npcLevel;
        double levelScalingFactor;

        if (levelDifference > 5) {
            levelScalingFactor = 1.0 / (1.0 + (levelDifference * 0.15)); // Reduce XP if much stronger
        } else if (levelDifference < -3) {
            levelScalingFactor = 1.2 + (-levelDifference * 0.1); // Boost XP if fighting stronger NPC
        } else {
            levelScalingFactor = 1.0; // Normal XP gain
        }

        // Random variation to prevent static XP values
        double randomFactor = 0.9 + (random.nextDouble() * 0.2); // Between 0.9x and 1.1x

        // Final XP calculation
        int xpReward = (int) (baseXp * levelScalingFactor * randomFactor) * 3;

        int finalXp = Math.max(xpReward, 1); // Ensure at least 1 XP is given
        player.gainXp(finalXp);
        System.out.println(player.name + " gains " + finalXp + " XP from " + name + "!");
    }

    public int getLevel() {
        return XpLevelUtil.toLevel(xp);
    }

    public int remainingXp()
    {
        return XpLevelUtil.xpUntilNextLevel(xp);
    }

    public boolean overlapping(Entity entity) {
        float dx = this.x - entity.x;
        float dy = this.y - entity.y;
        float distance = (float)Math.sqrt(dx * dx + dy * dy);
        return distance < 20; // returns true if within 20 pixels
    }

    public Item getDrop() {
        if (MathUtils.randomBoolean(0.7f)) {
            return lootTable.get(MathUtils.random(0, lootTable.size() - 1));
        }
        return null;
    }
}
