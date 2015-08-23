package com.github.antag99.heaven.component;

import com.badlogic.gdx.Input;
import com.github.antag99.retinazer.Component;

public final class Keyboard implements Component {
    public int keyLeft = Input.Keys.A;
    public int keyRight = Input.Keys.D;
    public int keyAttack = Input.Keys.SPACE;

    public Keyboard keyLeft(int keyLeft) {
        this.keyLeft = keyLeft;
        return this;
    }

    public Keyboard keyRight(int keyRight) {
        this.keyRight = keyRight;
        return this;
    }

    public Keyboard keyAttack(int keyAttack) {
        this.keyAttack = keyAttack;
        return this;
    }
}
