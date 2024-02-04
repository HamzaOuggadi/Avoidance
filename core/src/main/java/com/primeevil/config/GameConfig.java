package com.primeevil.config;

public class GameConfig {

    // In pixels
    public static final float WIDTH = 480f;
    public static final float HEIGHT = 800f;

    // In world units
    public static final float HUD_WIDTH = 480f;
    public static final float HUD_HEIGHT = 800f;

    // In world units
    public static final float WORLD_WIDTH = 6.0f;
    public static final float WORLD_HEIGHT = 10.0f;

    // In world units
    public static final float WORLD_CENTER_WIDTH = WORLD_WIDTH / 2f;
    public static final float WORLD_CENTER_HEIGHT = WORLD_HEIGHT / 2f;

    public static final float DEFAULT_SPAWN_TIME = 0.5f;

    public static final int LIVES_START = 3;

    private GameConfig() {

    }
}
