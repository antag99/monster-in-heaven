package com.github.antag99.heaven.component;

import com.github.antag99.retinazer.Component;

public final class Movement implements Component {
    public float speed = 5f;

    public Movement speed(float speed) {
        this.speed = speed;
        return this;
    }
}
