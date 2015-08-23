package com.github.antag99.heaven.component;

import com.github.antag99.retinazer.Component;

public final class Size implements Component {
    public float width;
    public float height;

    public Size size(float width, float height) {
        this.width = width;
        this.height = height;
        return this;
    }
}
