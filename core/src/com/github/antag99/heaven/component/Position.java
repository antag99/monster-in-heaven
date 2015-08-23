package com.github.antag99.heaven.component;

import com.github.antag99.retinazer.Component;

public final class Position implements Component {
    public float x;
    public float y;

    public Position xy(float x, float y) {
        this.x = x;
        this.y = y;
        return this;
    }
}
