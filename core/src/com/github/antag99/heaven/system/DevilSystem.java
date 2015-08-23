package com.github.antag99.heaven.system;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.IntArray;
import com.github.antag99.heaven.component.Acting;
import com.github.antag99.heaven.component.Attack;
import com.github.antag99.heaven.component.Devil;
import com.github.antag99.heaven.component.Direction;
import com.github.antag99.heaven.component.Player;
import com.github.antag99.heaven.component.Position;
import com.github.antag99.heaven.component.Size;
import com.github.antag99.heaven.component.Target;
import com.github.antag99.heaven.component.Velocity;
import com.github.antag99.heaven.util.SpineActor;
import com.github.antag99.retinazer.Engine;
import com.github.antag99.retinazer.EntityProcessorSystem;
import com.github.antag99.retinazer.EntitySet;
import com.github.antag99.retinazer.Family;
import com.github.antag99.retinazer.Mapper;
import com.github.antag99.retinazer.Wire;
import com.github.antag99.retinazer.Wire.Ignore;

@Wire
public final class DevilSystem extends EntityProcessorSystem {
    private Engine engine;
    private AssetSystem assetSystem;
    private Mapper<Position> mPosition;
    private Mapper<Size> mSize;
    private Mapper<Devil> mDevil;
    private Mapper<Player> mPlayer;
    private Mapper<Acting> mActing;
    private Mapper<Direction> mDirection;
    private Mapper<Target> mTarget;
    private Mapper<Attack> mAttack;
    private Mapper<Velocity> mVelocity;

    // private GameScreen gameScreen;

    private @Ignore EntitySet players;
    private @Ignore Vector2 vTmp = new Vector2();

    @SuppressWarnings("unchecked")
    public DevilSystem() {
        super(Family.with(Devil.class));
    }

    @SuppressWarnings("unchecked")
    @Override
    protected void initialize() {
        players = engine.getEntitiesFor(Family.with(Player.class));
    }

    private Target findPlayer(int devilEntity) {
        IntArray indices = players.getIndices();
        int[] items = indices.items;
        if (indices.size > 0) {
            return mTarget.create(devilEntity).set(items[0]);
        }
        return null;
    }

    private void findTarget(int devilEntity) {
        Devil devil = mDevil.get(devilEntity);
        Position position = mPosition.get(devilEntity);
        Velocity velocity = mVelocity.get(devilEntity);
        int targetEntity = mTarget.get(devilEntity).entity;

        Position targetPosition = mPosition.get(targetEntity);
        Size targetSize = mSize.get(targetEntity);

        devil.targetX = targetPosition.x + targetSize.width * 0.5f;
        velocity.x = Math.signum(devil.targetX - position.x) * devil.speed;
    }

    @Override
    protected void process(int entity) {
        Devil devil = mDevil.get(entity);
        Position position = mPosition.get(entity);
        Velocity velocity = mVelocity.get(entity);
        Size size = mSize.get(entity);
        Direction direction = mDirection.get(entity);
        Acting acting = mActing.get(entity);
        Target target = mTarget.get(entity);
        Attack attack = mAttack.get(entity);

        SpineActor actor = (SpineActor) acting.actor;
        actor.skeleton.setFlipX(direction.x == -1);
        actor.skeleton.getRootBone().setX(direction.x == -1 ? -size.width : 0f);

        if (target == null) {
            target = findPlayer(entity);

            if (target != null)
                findTarget(entity);
        }

        if (target != null && attack.counter <= 0f) {
            Position targetPosition = mPosition.get(target.entity);
            Size targetSize = mSize.get(target.entity);

            float targetDistance = Math.abs(devil.targetX - (position.x + size.width * 0.5f));
            if (targetDistance < devil.sensor) {
                findTarget(entity);

                float currentDistance = Math.abs(targetPosition.x + targetSize.width * 0.5f - (position.x + size.width * 0.5f));
                if (currentDistance < devil.charge) {
                    attack.counter = attack.delay;
                    actor.animationState.addAnimation(1, "attack", false, 0f);
                    velocity.x = 0f;
                    assetSystem.tridentSound.play();
                }
            }
        }

        if (attack.hit) {
            Player player = mPlayer.get(target.entity);
            Position targetPosition = mPosition.get(target.entity);
            Size targetSize = mSize.get(target.entity);

            float currentDistance = Math.abs(targetPosition.x + targetSize.width * 0.5f - (position.x + size.width * 0.5f));
            if (currentDistance < devil.range) {
                float alpha = 1f - (currentDistance / devil.range);
                int damage = MathUtils.ceil(devil.damage * alpha);
                player.health -= damage;
            }
        }
    }
}
