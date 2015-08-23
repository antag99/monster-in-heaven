package com.github.antag99.heaven.system;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.github.antag99.retinazer.EntitySystem;

public final class ClearSystem extends EntitySystem {
    private Color color = new Color();

    public ClearSystem(Color color) {
        this.color.set(color);
    }

    @Override
    protected void update() {
        Gdx.gl.glClearColor(color.r, color.g, color.b, color.a);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
    }
}
