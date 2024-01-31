package com.primeevil.util.debug;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Logger;

public class DebugCameraController {

    private static final Logger log = new Logger(DebugCameraController.class.getName(), Logger.DEBUG);

    private Vector2 position = new Vector2();
    private Vector2 startPosition = new Vector2();
    private float zoom = 1.0f;

    private DebugCameraConfig cameraConfig;

    public DebugCameraController() {
        cameraConfig = new DebugCameraConfig();
        log.info("cameraConfig : " + cameraConfig);
    }

    public void setStartPosition(float x, float y) {
        startPosition.set(x, y);
        position.set(x, y);
    }

    public void applyTo(OrthographicCamera camera) {
        camera.position.set(position, 0);
        camera.zoom = zoom;
        camera.update();
    }

    public void handleDebugInput(float delta) {
        if (Gdx.app.getType() != Application.ApplicationType.Desktop ) {
            return;
        }

        float moveSpeed = cameraConfig.getMoveSpeed() * delta;
        float zoomSpeed = cameraConfig.getZoomSpeed() * delta;

        // Movement controls
        if (cameraConfig.isLeftKeyPressed()) {
            moveLeft(moveSpeed);
        } else if (cameraConfig.isRightKeyPressed()) {
            moveRight(moveSpeed);
        } else if (cameraConfig.isUpKeyPressed()) {
            moveUp(moveSpeed);
        } else if (cameraConfig.isDownKeyPressed()) {
            moveDown(moveSpeed);
        }

        // Zoom controls
        if (cameraConfig.isZoomInKeyPressed()) {
            zoomIn(zoomSpeed);
        } else if (cameraConfig.isZoomOutKeyPressed()) {
            zoomOut(zoomSpeed);
        }

        if (cameraConfig.isResetKeyPressed()) {
            reset();
        }

        if (cameraConfig.isLogKeyPressed()) {
            log.debug("Camera position : " + position + " zoom : " + zoom);
        }
    }

    private void setPosition(float x, float y) {
        position.set(x, y);
    }

    private void moveCamera(float xSpeed, float ySpeed) {
        setPosition(position.x + xSpeed, position.y + ySpeed);
    }

    private void setZoom(float value) {
        zoom = MathUtils.clamp(value, cameraConfig.getMaxZoomIn(), cameraConfig.getMaxZoomOut());
    }

    private void moveLeft(float moveSpeed) {
        moveCamera(-moveSpeed, 0);
    }

    private void moveRight(float moveSpeed) {
        moveCamera(moveSpeed, 0);
    }

    private void moveUp(float moveSpeed) {
        moveCamera(0, moveSpeed);
    }

    private void moveDown(float moveSpeed) {
        moveCamera(0, -moveSpeed);
    }

    private void zoomIn(float zoomSpeed) {
        setZoom(zoom - zoomSpeed);
    }

    private void zoomOut(float zoomSpeed) {
        setZoom(zoom + zoomSpeed);
    }

    private void reset() {
        position.set(startPosition);
        setZoom(1.0f);
    }

}
