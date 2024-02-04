package com.primeevil.entity;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;
import com.primeevil.config.GameConfig;
import com.primeevil.util.Destroyer;

public class Obstacle extends GameObject<Rectangle> {

    private static final float SIDE_LENGTH = 1.0f;
    private static final float MAX_Y_SPEED = 0.05f;


    public Obstacle() {
        bounds = new Rectangle(x, y, SIDE_LENGTH, SIDE_LENGTH);
    }

    public void drawDebug(ShapeRenderer renderer) {
        renderer.rect(bounds.x, bounds.y, bounds.width, bounds.height);
    }

    public void setPosition(float x, float y) {
        this.x = x;
        this.y = y;
        updateBounds();
    }

    public void update() {
        setPosition(x, y - MAX_Y_SPEED);
    }

    public void updateBounds() {
        bounds.setPosition(x, y);
    }

    public static float getSideLength() {
        return SIDE_LENGTH;
    }


    public boolean isPlayerColliding(Player player) {
        return Intersector.overlaps(player.getBounds(), this.getBounds());
    }
}
