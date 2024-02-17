package com.primeevil.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Logger;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.primeevil.assets.AssetPaths;
import com.primeevil.config.GameConfig;
import com.primeevil.config.GameDifficulty;
import com.primeevil.entity.Obstacle;
import com.primeevil.entity.Player;
import com.primeevil.util.GdxUtils;
import com.primeevil.util.ViewportUtils;
import com.primeevil.util.debug.DebugCameraController;

import java.util.ArrayList;

@Deprecated
public class GameScreenOld implements Screen {

    private static final Logger log = new Logger(GameScreenOld.class.getName(), Logger.DEBUG);

    private OrthographicCamera camera;
    private Viewport viewport;
    private ShapeRenderer renderer;
    private DebugCameraController debugCameraController;
    private OrthographicCamera hudCamera;
    private Viewport hudViewport;
    private SpriteBatch batch;
    private BitmapFont uiFont;
    private final GlyphLayout glyphLayout = new GlyphLayout();

    private Player player;
    private ArrayList<Obstacle> obstacles = new ArrayList<>();
    private float obstacleTimer;
    private int lives = GameConfig.LIVES_START;
    private int score;
    private int displayScore;
    private float scoreTimer;
    private GameDifficulty gameDifficulty = GameDifficulty.HARD;





    @Override
    public void show() {
        camera = new OrthographicCamera();
        viewport = new FitViewport(GameConfig.WORLD_WIDTH, GameConfig.WORLD_HEIGHT, camera);
        renderer = new ShapeRenderer();
        debugCameraController = new DebugCameraController();

        debugCameraController.setStartPosition(GameConfig.WORLD_CENTER_WIDTH, GameConfig.WORLD_CENTER_HEIGHT);

        hudCamera = new OrthographicCamera();
        hudViewport = new FitViewport(GameConfig.HUD_WIDTH, GameConfig.HUD_HEIGHT, hudCamera);
        batch = new SpriteBatch();
        uiFont = new BitmapFont(Gdx.files.internal(AssetPaths.UI_FONT_PATH));

        player = new Player();

        float playerStartXPosition = GameConfig.WORLD_WIDTH / 2f;
        float playerStartYPosition = 1f;

        player.setPosition(playerStartXPosition, playerStartYPosition);
    }

    @Override
    public void render(float delta) {
        debugCameraController.handleDebugInput(delta);
        debugCameraController.applyTo(camera);

        // Update world
        update(delta);

        // Clear screen
        GdxUtils.clearScreen();

        rendererUI();

        // Render debug graphics
        renderDebug();
    }

    private void update(float delta) {
        if (isGameOver()) {
            log.debug("Game Over");
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

    private boolean isGameOver() {
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
            Obstacle obstacle = new Obstacle();
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

    private void renderDebug() {
        renderer.setProjectionMatrix(camera.combined);
        renderer.begin(ShapeRenderer.ShapeType.Line);

        drawDebug();

        renderer.end();

        ViewportUtils.drawGrid(viewport, renderer);

    }

    private void drawDebug() {
        player.drawDebug(renderer);
        for (Obstacle obstacle : obstacles) {
            obstacle.drawDebug(renderer);
        }
    }

    private void rendererUI() {
        batch.setProjectionMatrix(hudCamera.combined);
        batch.begin();

        String livesText = "LIVES : " + lives;
        glyphLayout.setText(uiFont, livesText);
        uiFont.draw(batch, livesText,
            20f, GameConfig.HUD_HEIGHT - glyphLayout.height);

        String scoreText = "SCORE : " + displayScore;
        glyphLayout.setText(uiFont, scoreText);
        uiFont.draw(batch, scoreText,
            (460- glyphLayout.width), (GameConfig.HUD_HEIGHT - glyphLayout.height));

        batch.end();
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height, true);
        hudViewport.update(width, height, true);
        ViewportUtils.debugPixelPerUnit(viewport);
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        renderer.dispose();
        batch.dispose();
        uiFont.dispose();
    }
}
