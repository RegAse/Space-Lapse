package com.spacelapse;

/**
 * Player class to store things like xp
 */
public class Player {

    public String screenName;

    public int level;
    public int xpToLevelUp;
    public int xp;

    // Stats
    private float damage = 0.2f;
    private float fireRate = 0.3f;
    private int health = 100;

    public Player(String screenName, int level, int xp, int xpToLevelUp) {
        this.screenName = screenName;
        this.level = level;
        this.xp = xp;
        this.xpToLevelUp = xpToLevelUp;
    }

    public static Player loadPlayer() {
        // Load from a json file etc..
        return new Player("RegAse", 4, 4000, 1000);
    }

    public float getDamage() {
        return level * damage;
    }

    public float getFireRate() {
        return level * fireRate;
    }

    public int getHealth() {
        return health + (level * 10);
    }
}
