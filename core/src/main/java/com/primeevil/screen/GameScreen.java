package com.primeevil.screen;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.Logger;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.primeevil.config.GameConfig;
import com.primeevil.entity.Player;
import com.primeevil.util.GdxUtils;
import com.primeevil.util.ViewportUtils;
import com.primeevil.util.debug.DebugCameraController;

public class GameScreen implements Screen {

    private static final Logger log = new Logger(GameScreen.class.getName(), Logger.DEBUG);

    private OrthographicCamera camera;
    private Viewport viewport;
    private ShapeRenderer renderer;
    private DebugCameraController debugCameraController;
    private Player player;


    @Override
    public void show() {
        camera = new OrthographicCamera();
        viewport = new FitViewport(GameConfig.WORLD_WIDTH, GameConfig.WORLD_HEIGHT, camera);
        renderer = new ShapeRenderer();
        player = new Player();
        debugCameraController = new DebugCameraController();

        debugCameraController.setStartPosition(GameConfig.WORLD_CENTER_WIDTH, GameConfig.WORLD_CENTER_HEIGHT);

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

        // Render debug graphics
        renderDebug();
    }

    private void update(float delta) {
        updatePlayer();
    }

    private void updatePlayer() {
        player.update();
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
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height, true);
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
    }
}
