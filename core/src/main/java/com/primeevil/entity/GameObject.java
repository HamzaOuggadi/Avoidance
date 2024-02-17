package com.primeevil.entity;

import com.badlogic.gdx.math.Shape2D;

public abstract class GameObject<T extends Shape2D> {

    protected float x;
    protected float y;
    protected float width;
    protected float height;

    protected T bounds;

    public GameObject(T bounds) {
        this.bounds = bounds;
    }

    public GameObject() {

    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public float getWidth() {
        return width;
    }

    public float getHeight() {
        return height;
    }

    public void setX(float x) {
        this.x = x;
    }

    public void setY(float y) {
        this.y = y;
    }

    public void setSize(float width, float height) {
        this.width = width;
        this.height = height;
    }

    public T getBounds() {
        return bounds;
    }

    public void setBounds(T bounds) {
        this.bounds = bounds;
    }

}
