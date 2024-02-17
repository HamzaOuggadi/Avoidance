package com.primeevil.config;

public enum GameDifficulty {
    EASY(GameConfig.EASY_OBSTACLE_SPEED),
    NORMAL(GameConfig.NORMAL_OBSTACLE_SPEED),
    HARD(GameConfig.HARD_OBSTACLE_SPEED);

    private final float obstacleSpeed;

    GameDifficulty(float obstacleSpeed) {
        this.obstacleSpeed = obstacleSpeed;
    }

    public float getObstacleSpeed() {
        return obstacleSpeed;
    }
}
