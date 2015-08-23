package com.github.antag99.heaven.util;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.esotericsoftware.spine.AnimationState;
import com.esotericsoftware.spine.Skeleton;
import com.esotericsoftware.spine.SkeletonRenderer;

public class SpineActor extends Actor {
    public Skeleton skeleton;
    public AnimationState animationState;
    private SkeletonRenderer renderer;

    public SpineActor(Skeleton skeleton, AnimationState animationState) {
        this.skeleton = skeleton;
        this.animationState = animationState;
        this.renderer = new SkeletonRenderer();
    }

    @Override
    public void act(float delta) {
        super.act(delta);

        skeleton.setX(getX());
        skeleton.setY(getY());
        skeleton.getRootBone().setScaleX(getScaleX());
        skeleton.getRootBone().setScaleY(getScaleY());
        animationState.update(delta);
        animationState.apply(skeleton);
        skeleton.updateWorldTransform();
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);

        renderer.draw(batch, skeleton);
    }
}
