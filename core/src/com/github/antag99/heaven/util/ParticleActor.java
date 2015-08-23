package com.github.antag99.heaven.util;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.github.antag99.retinazer.Handle;

public class ParticleActor extends Actor {
    public ParticleEffect effect;

    public ParticleActor(ParticleEffect effect) {
        this.effect = effect;
        this.effect.start();
    }

    @Override
    public void act(float delta) {
        super.act(delta);

        effect.setPosition(getX(), getY());
        effect.update(delta);

        if (effect.isComplete()) {
            ((Handle) getUserObject()).destroy();
        }
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);

        effect.draw(batch);
    }
}
