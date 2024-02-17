package com.primeevil.screen;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Logger;
import com.badlogic.gdx.utils.Pool;
import com.badlogic.gdx.utils.Pools;
import com.primeevil.config.GameConfig;
import com.primeevil.config.GameDifficulty;
import com.primeevil.entity.Background;
import com.primeevil.entity.Obstacle;
import com.primeevil.entity.Player;

import java.util.ArrayList;

public class GameController {

    // ** Constants **
    private static final Logger log = new Logger(GameController.class.getName(), Logger.DEBUG);

    // ** Attributes **
    private Player player;
    private Array<Obstacle> obstacles = new Array<>();
    private float obstacleTimer;
    private int lives = GameConfig.LIVES_START;
    private int score;
    private int displayScore;
    private float scoreTimer;
    private GameDifficulty gameDifficulty = GameDifficulty.EASY;
    private Pool<Obstacle> obstaclePool;
    private Background background;

    // ** Constructors **
    public GameController() {
        init();
    }

    // ** Init **
    private void init() {
        // Create player
        player = new Player();

        float playerStartXPosition = GameConfig.WORLD_WIDTH / 2f;
        float playerStartYPosition = 1f;

        player.setPosition(playerStartXPosition, playerStartYPosition);

        // Initializing the obstacle pool
        obstaclePool = Pools.get(Obstacle.class, 30);

        // Initializing Background
        background = new Background();
        background.setPosition(0,0);
        background.setSize(GameConfig.WORLD_WIDTH, GameConfig.WORLD_HEIGHT);
    }

    // ** Public methods
    public void update(float delta) {
        if (isGameOver()) {
            return;
        }
        updatePlayer();
        spawnObstacles(delta);
        updateScore(delta);
        updateDisplayScore(delta);

        if (isPlayerOnCollisionWithObstacle()) {
            log.debug("Collision detected.");
            lives--;
        }
    }

    public Player getPlayer() {
        return player;
    }

    public Array<Obstacle> getObstacles() {
        return obstacles;
    }

    public Background getBackground() {
        return background;
    }

    public int getLives() {
        return lives;
    }

    public int getDisplayScore() {
        return displayScore;
    }

    public boolean isGameOver() {
        return lives <= 0;
    }

    private boolean isPlayerOnCollisionWithObstacle() {
        for (Obstacle obstacle : obstacles) {
            if (obstacle.isNotHit() && obstacle.isPlayerColliding(player)) {
                return true;
            }
        }
        return false;
    }

    private void updatePlayer() {
        player.update();
        blockPlayerOnBounds();
    }

    private void blockPlayerOnBounds() {
        float playerX = MathUtils.clamp(player.getX(),
            player.getSize() / 2f,
            GameConfig.WORLD_WIDTH - (player.getSize() / 2f));

        player.setPosition(playerX, player.getY());
    }

    private void createObstacle(float delta) {
        obstacleTimer += delta;

        if (obstacleTimer >= GameConfig.DEFAULT_SPAWN_TIME) {
            float obstacleX = MathUtils.random(Obstacle.getSideLength() / 2f, GameConfig.WORLD_WIDTH - Obstacle.getSideLength());
            Obstacle obstacle = obstaclePool.obtain();
            obstacle.setYSpeed(gameDifficulty.getObstacleSpeed());
            obstacle.setPosition(obstacleX, GameConfig.WORLD_HEIGHT);
            obstacles.add(obstacle);
            obstacleTimer = 0f;
        }
    }

    private void spawnObstacles(float delta) {
        for (Obstacle obstacle : obstacles) {
            obstacle.update();
        }
        createObstacle(delta);
        removePassedObstacles();
    }

    private void removePassedObstacles() {
        if (obstacles.size > 0) {
            Obstacle first = obstacles.first();

            float minObstacleY = -Obstacle.SIDE_LENGTH * 2;

            if (first.getY() < minObstacleY) {
                obstacles.removeValue(first, true);
                obstaclePool.free(first);
            }
        }
    }

    private void updateScore(float delta) {
        scoreTimer += delta;

        if (scoreTimer >= GameConfig.SCORE_MAX_TIME) {
            score += MathUtils.random(1, 5);
            scoreTimer = 0.0f;
        }
    }

    private void updateDisplayScore(float delta) {
        if (displayScore < score) {
            displayScore = Math.min(score, displayScore + (int) (60 * delta));
        }
    }
}
