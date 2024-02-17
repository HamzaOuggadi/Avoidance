package com.primeevil.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.Logger;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.primeevil.assets.AssetPaths;
import com.primeevil.config.GameConfig;
import com.primeevil.entity.Background;
import com.primeevil.entity.Obstacle;
import com.primeevil.entity.Player;
import com.primeevil.util.GdxUtils;
import com.primeevil.util.ViewportUtils;
import com.primeevil.util.debug.DebugCameraController;

import java.util.ArrayList;

public class GameRenderer implements Disposable {

    private static final Logger log = new Logger(GameRenderer.class.getName(), Logger.DEBUG);

    // ** Attributes **
    private OrthographicCamera camera;
    private Viewport viewport;
    private ShapeRenderer renderer;
    private DebugCameraController debugCameraController;
    private OrthographicCamera hudCamera;
    private Viewport hudViewport;
    private SpriteBatch batch;
    private BitmapFont uiFont;
    private final GlyphLayout glyphLayout = new GlyphLayout();
    private Texture playerTexture;
    private Texture obstacleTexture;
    private Texture backgroundTexture;

    private final GameController gameController;


    // ** Constructors **
    public GameRenderer(GameController gameController) {
        this.gameController = gameController;
        init();
    }

    // ** init
    private void init() {
        camera = new OrthographicCamera();
        viewport = new FitViewport(GameConfig.WORLD_WIDTH, GameConfig.WORLD_HEIGHT, camera);
        renderer = new ShapeRenderer();
        debugCameraController = new DebugCameraController();

        debugCameraController.setStartPosition(GameConfig.WORLD_CENTER_WIDTH, GameConfig.WORLD_CENTER_HEIGHT);

        hudCamera = new OrthographicCamera();
        hudViewport = new FitViewport(GameConfig.HUD_WIDTH, GameConfig.HUD_HEIGHT, hudCamera);
        batch = new SpriteBatch();
        uiFont = new BitmapFont(Gdx.files.internal(AssetPaths.UI_FONT_PATH));

        playerTexture = new Texture(Gdx.files.internal("gameplay/player.png"));
        obstacleTexture = new Texture(Gdx.files.internal("gameplay/obstacle.png"));
        backgroundTexture = new Texture(Gdx.files.internal("gameplay/background.png"));
    }

    // ** Public methods **
    public void render(float delta) {
        debugCameraController.handleDebugInput(delta);
        debugCameraController.applyTo(camera);

        // Input
        if (Gdx.input.isTouched() && !gameController.isGameOver()) {
            Vector2 screenTouchPosition = new Vector2(Gdx.input.getX(), Gdx.input.getY());
            Vector2 worldPositionScreenTouch = viewport.unproject(new Vector2(screenTouchPosition));

            Player player = gameController.getPlayer();
            worldPositionScreenTouch.x = MathUtils.clamp(worldPositionScreenTouch.x, 0, GameConfig.WORLD_WIDTH - player.getWidth());
            player.setX(worldPositionScreenTouch.x);


        }

        // Clear screen
        GdxUtils.clearScreen();

        // Render the gameplay
        renderGameplay();

        // Renders UI
        rendererUI();

        // Render debug graphics
        renderDebug();
    }

    public void resize(int width, int height) {
        viewport.update(width, height, true);
        hudViewport.update(width, height, true);
        ViewportUtils.debugPixelPerUnit(viewport);
    }

    @Override
    public void dispose() {
        renderer.dispose();
        batch.dispose();
        uiFont.dispose();
        playerTexture.dispose();
        obstacleTexture.dispose();
        backgroundTexture.dispose();
    }

    private void renderGameplay() {
        viewport.apply();
        batch.setProjectionMatrix(camera.combined);
        batch.begin();

        // Draw background
        Background background = gameController.getBackground();
        batch.draw(backgroundTexture,
            background.getX(), background.getY(),
            background.getWidth(), background.getHeight());

        // Draw player
        Player player = gameController.getPlayer();
        batch.draw(playerTexture,
            player.getX() - player.getWidth()/2, player.getY() - player.getHeight()/2,
            player.getSize(), player.getSize());

        // Draw Obstacles
        for (Obstacle obstacle : gameController.getObstacles()) {
            batch.draw(obstacleTexture,
                obstacle.getX(), obstacle.getY(),
                Obstacle.SIDE_LENGTH, Obstacle.SIDE_LENGTH);
        }

        batch.end();
    }

    private void renderDebug() {
        viewport.apply();
        renderer.setProjectionMatrix(camera.combined);
        renderer.begin(ShapeRenderer.ShapeType.Line);

        drawDebug();

        renderer.end();

        ViewportUtils.drawGrid(viewport, renderer);

    }

    // ** Private methods **
    private void drawDebug() {
        Player player = gameController.getPlayer();
        player.drawDebug(renderer);

        Array<Obstacle> obstacles = gameController.getObstacles();
        for (Obstacle obstacle : obstacles) {
            obstacle.drawDebug(renderer);
        }
    }

    private void rendererUI() {
        hudViewport.apply();
        batch.setProjectionMatrix(hudCamera.combined);
        batch.begin();

        String livesText = "LIVES : " + gameController.getLives();
        glyphLayout.setText(uiFont, livesText);
        uiFont.draw(batch, livesText,
            20f, GameConfig.HUD_HEIGHT - glyphLayout.height);

        String scoreText = "SCORE : " + gameController.getDisplayScore();
        glyphLayout.setText(uiFont, scoreText);
        uiFont.draw(batch, scoreText,
            (460- glyphLayout.width), (GameConfig.HUD_HEIGHT - glyphLayout.height));

        batch.end();
    }

}
