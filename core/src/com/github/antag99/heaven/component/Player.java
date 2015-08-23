package com.github.antag99.heaven.component;

import com.github.antag99.retinazer.Component;

public final class Player implements Component {
    public int score = 0;
    public int health = 10;

    public Player health(int health) {
        this.health = health;
        return this;
    }
}
