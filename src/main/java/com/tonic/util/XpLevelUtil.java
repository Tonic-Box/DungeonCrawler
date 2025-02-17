package com.tonic.util;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Random;

public class XpLevelUtil {
    private static final Random random = new Random();
    public static final Map<Integer, Integer> XP_MAP = generateXpMap();

    /**
     * Generates a map where the key is the level (1-99) and the value is the
     * cumulative XP required to reach that level.
     *
     * @return Map of level to total XP required.
     */
    private static Map<Integer, Integer> generateXpMap() {
        Map<Integer, Integer> xpMap = new LinkedHashMap<>();
        int cumulativeXp = 0;

        for (int level = 1; level <= 99; level++) {
            // For level 1, no XP is required.
            int xpForLevel = (level == 1) ? 0 : (int) (Math.pow(level - 1, 2.2) * 100);
            cumulativeXp += xpForLevel;
            xpMap.put(level, cumulativeXp);
        }

        return xpMap;
    }

    public static int toLevel(int xp)
    {
        for (Map.Entry<Integer, Integer> entry : XP_MAP.entrySet()) {
            if (xp < entry.getValue()) {
                return entry.getKey() - 1; // Return the previous level
            }
        }
        return 99;
    }

    public static int randomXpForLevel(int level) {
        if (level < 1 || level >= 99) {
            throw new IllegalArgumentException("Level must be between 1 and 98 (99 is max).");
        }

        int minXp = XP_MAP.get(level);
        int maxXp = XP_MAP.get(level + 1) - 1; // -1 to keep it within bounds
        return random.nextInt(maxXp - minXp + 1) + minXp;
    }

    public static int xpUntilNextLevel(int xp) {
        int currentLevel = toLevel(xp);
        int nextLevelXp = XP_MAP.get(currentLevel + 1);
        return nextLevelXp - xp;
    }
}
