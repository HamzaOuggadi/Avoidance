package com.primeevil.util;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.Logger;
import com.badlogic.gdx.utils.viewport.Viewport;

public class ViewportUtils {
    private static final Logger log = new Logger(ViewportUtils.class.getName(), Logger.DEBUG);

    private static final int DEFAULT_CELL_SIZE = 1;

    public static void drawGrid(Viewport viewport, ShapeRenderer renderer) {
        drawGrid(viewport, renderer, DEFAULT_CELL_SIZE);
    }

    public static void drawGrid(Viewport viewport, ShapeRenderer renderer, int cellSize) {
        // Validate arguments/parameters
        if (viewport == null) {
            throw new IllegalArgumentException("Viewport argument/parameter is null.");
        }

        if (renderer == null) {
            throw new IllegalArgumentException("Renderer argument/parameter is null.");
        }

        if (cellSize < DEFAULT_CELL_SIZE) {
            cellSize = DEFAULT_CELL_SIZE;
        }

        // Get copy from renderer
        Color oldColor = new Color(renderer.getColor());

        int worldWidth = (int) viewport.getWorldWidth();
        int worldHeight = (int) viewport.getWorldHeight();
        int doubleWorldWidth = worldWidth * 2;
        int doubleWorldHeight = worldHeight * 2;

        renderer.setProjectionMatrix(viewport.getCamera().combined);
        renderer.begin(ShapeRenderer.ShapeType.Line);
        renderer.setColor(Color.WHITE);

        // Drawing vertical lines
        for (int x = -doubleWorldWidth; x <= doubleWorldWidth; x+= cellSize) {
            renderer.line(x, -doubleWorldHeight, x, doubleWorldHeight);
        }

        // Drawing Horizontal lines
        for (int y = -doubleWorldHeight; y <= doubleWorldHeight; y+= cellSize) {
            renderer.line(-doubleWorldWidth, y, doubleWorldWidth, y);
        }

        // Drawing the X and Y axis
        renderer.setColor(Color.RED);
        renderer.line(-doubleWorldWidth, 0, doubleWorldWidth, 0);
        renderer.line(0, -doubleWorldHeight, 0, doubleWorldHeight);

        // Drawing world bounds
        renderer.setColor(Color.GREEN);
        renderer.line(0f, worldHeight, worldWidth, worldHeight);
        renderer.line(0f, 0f, worldWidth, 0f);
        renderer.line(0f, worldHeight, 0f, 0f);
        renderer.line(worldWidth, worldHeight, worldWidth, 0f);

//        renderer.line(-doubleWorldWidth, -doubleWorldHeight, doubleWorldWidth, -doubleWorldHeight);
//        renderer.line(-doubleWorldWidth, doubleWorldHeight, doubleWorldWidth, doubleWorldHeight);
//        renderer.line(-doubleWorldWidth, doubleWorldHeight, -doubleWorldWidth, -doubleWorldHeight);
//        renderer.line(doubleWorldWidth, doubleWorldHeight, doubleWorldWidth, -doubleWorldHeight);

        renderer.end();

        renderer.setColor(oldColor);

    }

    public static void debugPixelPerUnit(Viewport viewport) {
        if (viewport == null) {
            throw new IllegalArgumentException("Viewport argument/parameter is null.");
        }
        float screenWidth = viewport.getScreenWidth();
        float screenHeight = viewport.getScreenHeight();

        float worldWidth = viewport.getWorldWidth();
        float worldHeight = viewport.getWorldHeight();

        float xPPU = screenWidth / worldWidth;
        float yPPU = screenHeight / worldHeight;

        log.debug("xPPU = " + xPPU + " yPPU = " + yPPU);
    }

    private ViewportUtils() {

    }
}
