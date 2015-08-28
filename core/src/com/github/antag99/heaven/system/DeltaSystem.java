package com.github.antag99.heaven.system;

import com.badlogic.gdx.Gdx;
import com.github.antag99.retinazer.EntitySystem;

public final class DeltaSystem extends EntitySystem {
    public float getDeltaTime() {
        float deltaTime = Gdx.graphics.getDeltaTime();
        /* Clamp delta; otherwise tunneling and other weirdness may result */
        return deltaTime < 1f / 20f ? deltaTime : 1f / 20f;
    }
}
