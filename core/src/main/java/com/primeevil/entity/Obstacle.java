package com.primeevil.entity;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Pool;

public class Obstacle extends GameObject<Rectangle> implements Pool.Poolable {

    public static final float SIDE_LENGTH = 1.0f;
    private static final float MAX_Y_SPEED = 0.05f;

    private float ySpeed;

    private boolean hit;

    public Obstacle() {
        bounds = new Rectangle(x, y, SIDE_LENGTH, SIDE_LENGTH);
        setSize(SIDE_LENGTH, SIDE_LENGTH);
    }

    public void drawDebug(ShapeRenderer renderer) {
        renderer.x(bounds.x, bounds.y, 0.1f);
        renderer.rect(bounds.x, bounds.y, bounds.width, bounds.height);
    }

    public void setPosition(float x, float y) {
        this.x = x;
        this.y = y;
        updateBounds();
    }

    public void update() {
        setPosition(x, y - ySpeed);
    }

    public void updateBounds() {
        bounds.setPosition(x, y);
    }

    public static float getSideLength() {
        return SIDE_LENGTH;
    }


    public boolean isPlayerColliding(Player player) {
        boolean overlap = Intersector.overlaps(player.getBounds(), this.getBounds());
        hit = overlap;
        return overlap;
    }

    public boolean isNotHit() {
        return !hit;
    }

    public void setYSpeed(float ySpeed) {
        this.ySpeed = ySpeed;
    }

    @Override
    public void reset() {
        hit = false;
    }
}
