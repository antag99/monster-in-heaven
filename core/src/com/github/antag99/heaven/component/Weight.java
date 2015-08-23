package com.github.antag99.heaven.component;

import com.github.antag99.retinazer.Component;

public final class Weight implements Component {
    public float weight = 1f;

    public Weight weight(float weight) {
        this.weight = weight;
        return this;
    }
}
