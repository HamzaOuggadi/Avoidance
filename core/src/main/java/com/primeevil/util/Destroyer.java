package com.primeevil.util;

public class Destroyer {

    private static Destroyer instance;

    public static Destroyer getInstance() {
        if (instance == null) {
            instance = new Destroyer();
        }
        return instance;
    }

    private Destroyer() {

    }
}
