package com.primeevil.util.debug;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;
import com.badlogic.gdx.utils.Logger;

public class DebugCameraConfig {

    private static final Logger log = new Logger(DebugCameraController.class.getName(), Logger.DEBUG);

    /**
     * Constants
     */
    private static final int DEFAULT_LEFT_KEY = Input.Keys.A;
    private static final int DEFAULT_RIGHT_KEY = Input.Keys.D;
    private static final int DEFAULT_UP_KEY = Input.Keys.W;
    private static final int DEFAULT_DOWN_KEY = Input.Keys.S;

    private static final int DEFAULT_ZOOM_IN_KEY = Input.Keys.DOWN;
    private static final int DEFAULT_ZOOM_OUT_KEY = Input.Keys.UP;

    private static final int DEFAULT_RESET_KEY = Input.Keys.BACKSPACE;
    private static final int DEFAULT_LOG_KEY = Input.Keys.L;

    private static final float DEFAULT_MOVE_SPEED = 20.0f;
    private static final float DEFAULT_ZOOM_SPEED = 2.0f;
    private static final float DEFAULT_MAX_ZOOM_IN = 0.2f;
    private static final float DEFAULT_MAX_ZOOM_OUT = 6.0f;

    private static final String FILE_PATH = "debug/debugCameraConfig.json";

    /**
     * Attributes
     */
    private float maxZoomIn;
    private float maxZoomOut;
    private float moveSpeed;
    private float zoomSpeed;

    private int upKey;
    private int downKey;
    private int leftKey;
    private int rightKey;

    private int zoomInKey;
    private int zoomOutKey;

    private int resetKey;
    private int logKey;

    private FileHandle fileHandle;


    public DebugCameraConfig() {
        init();
    }

    private void init() {
        fileHandle = Gdx.files.internal(FILE_PATH);

        if (fileHandle.exists()) {
            load();
        } else {
            log.debug("Using default settings, config file doesn't exist or failed to load. " + FILE_PATH);
            setupDefaults();
        }
    }

    private void load() {
        try {
            JsonReader reader = new JsonReader();
            JsonValue root = reader.parse(fileHandle);

            maxZoomIn = root.getFloat("maxZoomIn", DEFAULT_MAX_ZOOM_IN);
            maxZoomOut = root.getFloat("maxZoomOut", DEFAULT_MAX_ZOOM_OUT);
            moveSpeed = root.getFloat("moveSpeed", DEFAULT_MOVE_SPEED);
            zoomSpeed = root.getFloat("zoomSpeed", DEFAULT_ZOOM_SPEED);

            upKey = getInputKeyValue(root, "upKey", DEFAULT_UP_KEY);
            downKey = getInputKeyValue(root, "downKey", DEFAULT_DOWN_KEY);
            leftKey = getInputKeyValue(root, "leftKey", DEFAULT_LEFT_KEY);
            rightKey = getInputKeyValue(root, "rightKey", DEFAULT_RIGHT_KEY);

            zoomInKey = getInputKeyValue(root, "zoomInKey", DEFAULT_ZOOM_IN_KEY);
            zoomOutKey = getInputKeyValue(root, "zoomOutKey", DEFAULT_ZOOM_OUT_KEY);

            resetKey = getInputKeyValue(root, "resetKey", DEFAULT_RESET_KEY);
            logKey = getInputKeyValue(root, "logKey", DEFAULT_LOG_KEY);
        } catch (Exception e) {
            log.error(e.getMessage());
            setupDefaults();
            throw new RuntimeException(e);
        }

    }

    private void setupDefaults() {
        maxZoomIn = DEFAULT_MAX_ZOOM_IN;
        maxZoomOut =DEFAULT_MAX_ZOOM_OUT;
        moveSpeed = DEFAULT_MOVE_SPEED;
        zoomSpeed = DEFAULT_ZOOM_SPEED;
        upKey = DEFAULT_UP_KEY;
        downKey = DEFAULT_DOWN_KEY;
        leftKey = DEFAULT_LEFT_KEY;
        rightKey = DEFAULT_RIGHT_KEY;
        zoomInKey = DEFAULT_ZOOM_IN_KEY;
        zoomOutKey = DEFAULT_ZOOM_OUT_KEY;
        resetKey = DEFAULT_RESET_KEY;
        logKey = DEFAULT_LOG_KEY;
    }

    private static int getInputKeyValue(JsonValue root, String inputName, int defaultKeyCode) {
        String keyName = root.getString(inputName, Input.Keys.toString(defaultKeyCode));
        return Input.Keys.valueOf(keyName);
    }

    public float getMaxZoomIn() {
        return maxZoomIn;
    }

    public float getMaxZoomOut() {
        return maxZoomOut;
    }

    public float getMoveSpeed() {
        return moveSpeed;
    }

    public float getZoomSpeed() {
        return zoomSpeed;
    }

    public boolean isUpKeyPressed() {
        return Gdx.input.isKeyPressed(upKey);
    }

    public boolean isDownKeyPressed() {
        return Gdx.input.isKeyPressed(downKey);
    }

    public boolean isLeftKeyPressed() {
        return Gdx.input.isKeyPressed(leftKey);
    }

    public boolean isRightKeyPressed() {
        return Gdx.input.isKeyPressed(rightKey);
    }

    public boolean isZoomInKeyPressed() {
        return Gdx.input.isKeyPressed(zoomInKey);
    }

    public boolean isZoomOutKeyPressed() {
        return Gdx.input.isKeyPressed(zoomOutKey);
    }

    public boolean isResetKeyPressed() {
        return Gdx.input.isKeyPressed(resetKey);
    }

    public boolean isLogKeyPressed() {
        return Gdx.input.isKeyPressed(logKey);
    }

    @Override
    public String toString() {
        String ls = System.getProperty("line.separator");
        return "DebugCameraConfig{" + ls +
            "maxZoomIn=" + maxZoomIn + ls +
            "maxZoomOut=" + maxZoomOut + ls +
            "moveSpeed=" + moveSpeed + ls +
            "zoomSpeed=" + zoomSpeed + ls +
            "upKey=" + Input.Keys.toString(upKey) + ls +
            "downKey=" + Input.Keys.toString(downKey) + ls +
            "leftKey=" + Input.Keys.toString(leftKey) + ls +
            "rightKey=" + Input.Keys.toString(rightKey) + ls +
            "zoomInKey=" + Input.Keys.toString(zoomInKey) + ls +
            "zoomOutKey=" + Input.Keys.toString(zoomOutKey) + ls +
            "resetKey=" + Input.Keys.toString(resetKey) + ls +
            "logKey=" + Input.Keys.toString(logKey) + ls +
            "fileHandle=" + fileHandle + ls +
            '}';
    }
}
