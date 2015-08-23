package com.github.antag99.heaven.component;

import com.github.antag99.retinazer.Component;

public final class Target implements Component {
    public int entity;

    public Target set(int entity) {
        this.entity = entity;
        return this;
    }
}
