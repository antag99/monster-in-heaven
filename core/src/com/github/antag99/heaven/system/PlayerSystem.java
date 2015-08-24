package com.github.antag99.heaven.system;

import com.esotericsoftware.spine.AnimationState;
import com.esotericsoftware.spine.AnimationState.TrackEntry;
import com.esotericsoftware.spine.Skeleton;
import com.github.antag99.heaven.component.Acting;
import com.github.antag99.heaven.component.Attack;
import com.github.antag99.heaven.component.Direction;
import com.github.antag99.heaven.component.Player;
import com.github.antag99.heaven.component.Size;
import com.github.antag99.heaven.component.Velocity;
import com.github.antag99.heaven.util.SpineActor;
import com.github.antag99.retinazer.EntityProcessorSystem;
import com.github.antag99.retinazer.Family;
import com.github.antag99.retinazer.Mapper;
import com.github.antag99.retinazer.Wire;

@Wire
public final class PlayerSystem extends EntityProcessorSystem {
    private AssetSystem assetSystem;
    private Mapper<Acting> mActing;
    private Mapper<Direction> mDirection;
    private Mapper<Size> mSize;
    private Mapper<Velocity> mVelocity;
    private Mapper<Attack> mAttack;

    @SuppressWarnings("unchecked")
    public PlayerSystem() {
        super(Family.with(Player.class));
    }

    @Override
    protected void process(int entity) {
        Acting acting = mActing.get(entity);
        Direction direction = mDirection.get(entity);
        Velocity velocity = mVelocity.get(entity);
        Size size = mSize.get(entity);
        Attack attack = mAttack.get(entity);

        if (attack.hit) {
            attack.hit = false;
            assetSystem.biteSound.play();
        }

        SpineActor actor = (SpineActor) acting.actor;
        AnimationState animationState = actor.animationState;

        if (animationState.getCurrent(1) == null) {
            if (velocity.x != 0f) {
                animationState.addAnimation(1, "walk", false, 0f).setTimeScale(Math.abs(velocity.x * 0.4f));
            } else {
                animationState.addAnimation(1, "idle", false, 0f);
            }
        }

        if (attack.counter > 0f && animationState.getCurrent(2) == null) {
            TrackEntry track = animationState.addAnimation(2, "bite", false, 0f);
            track.setTimeScale(track.getEndTime() / attack.delay);
        }

        Skeleton skeleton = actor.skeleton;
        skeleton.setFlipX(direction.x == -1);
        skeleton.getRootBone().setX(direction.x == -1 ? -size.width : 0f);
    }
}
