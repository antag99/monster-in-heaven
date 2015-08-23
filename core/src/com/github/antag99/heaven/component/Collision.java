package com.github.antag99.heaven.component;

import com.github.antag99.retinazer.Component;

public final class Collision implements Component {
    public static final int FLAG_DEFAULT = 1;
    public static final int FLAG_PLAYER = 2;
    public static final int FLAG_FLOOR = 4;
    public static final int FLAG_WALL = 8;
    public static final int FLAG_CAKE = 16;
    public static final int FLAG_MONSTER = 32;

    /** Collision properties of this object */
    public int properties = FLAG_DEFAULT;
    /** Collision properties of objects to collide with */
    public int collision = FLAG_DEFAULT;

    public Collision properties(int properties) {
        this.properties = properties;
        return this;
    }

    public Collision collision(int collision) {
        this.collision = collision;
        return this;
    }
}
