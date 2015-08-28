package com.github.antag99.heaven.system;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.IntArray;
import com.github.antag99.heaven.GameScreen;
import com.github.antag99.heaven.component.Acting;
import com.github.antag99.heaven.component.Attack;
import com.github.antag99.heaven.component.Direction;
import com.github.antag99.heaven.component.Fairy;
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
public final class FairySystem extends EntityProcessorSystem {
    private Engine engine;
    private Mapper<Position> mPosition;
    private Mapper<Size> mSize;
    private Mapper<Fairy> mFairy;
    private Mapper<Player> mPlayer;
    private Mapper<Acting> mActing;
    private Mapper<Direction> mDirection;
    private Mapper<Target> mTarget;
    private Mapper<Attack> mAttack;
    private Mapper<Velocity> mVelocity;
    private GameScreen gameScreen;
    private AssetSystem assetSystem;
    private DeltaSystem deltaSystem;

    private @Ignore EntitySet players;
    private @Ignore Vector2 vTmp = new Vector2();

    @SuppressWarnings("unchecked")
    public FairySystem() {
        super(Family.with(Fairy.class));
    }

    @SuppressWarnings("unchecked")
    @Override
    protected void initialize() {
        players = engine.getEntitiesFor(Family.with(Player.class));
    }

    private Target findPlayer(int fairyEntity) {
        IntArray indices = players.getIndices();
        int[] items = indices.items;
        if (indices.size > 0) {
            return mTarget.create(fairyEntity).set(items[0]);
        }
        return null;
    }

    private void findTarget(int fairyEntity) {
        Fairy fairy = mFairy.get(fairyEntity);
        int targetEntity = mTarget.get(fairyEntity).entity;

        Position targetPosition = mPosition.get(targetEntity);
        Size targetSize = mSize.get(targetEntity);

        fairy.targetX = targetPosition.x + targetSize.width * 0.5f;
        fairy.targetY = targetPosition.y + targetSize.height * 0.5f;
    }

    @Override
    protected void process(int entity) {
        float deltaTime = deltaSystem.getDeltaTime();
        Fairy fairy = mFairy.get(entity);
        Position position = mPosition.get(entity);
        Velocity velocity = mVelocity.get(entity);
        Size size = mSize.get(entity);
        Direction direction = mDirection.get(entity);
        Acting acting = mActing.get(entity);
        Target target = mTarget.get(entity);
        Attack attack = mAttack.get(entity);

        SpineActor actor = (SpineActor) acting.actor;
        actor.skeleton.setFlipX(direction.x == 1);
        actor.skeleton.getRootBone().setX(direction.x == 1 ? -size.width : 0f);
        actor.skeleton.setSkin(gameScreen.isHell ? "hell" : "overworld");

        if (target == null) {
            target = findPlayer(entity);

            if (target != null)
                findTarget(entity);
        }

        if (target != null && attack.counter <= 0f) {
            Position targetPosition = mPosition.get(target.entity);
            Size targetSize = mSize.get(target.entity);

            float targetDistance = vTmp.set(position.x + size.width * 0.5f, position.y + size.height * 0.5f)
                    .dst(fairy.targetX, fairy.targetY);
            if (targetDistance < fairy.sensor) {
                findTarget(entity);

                float currentDistance = vTmp.set(position.x + size.width * 0.5f, position.y + size.height * 0.5f)
                        .dst(targetPosition.x + targetSize.width * 0.5f, targetPosition.y + targetSize.height * 0.5f);
                if (currentDistance < fairy.charge) {
                    attack.counter = attack.delay;
                    actor.animationState.addAnimation(1, "attack", false, 0f);
                    assetSystem.ringSound.play();
                }
            }
        }

        if (attack.hit) {
            Player player = mPlayer.get(target.entity);
            Position targetPosition = mPosition.get(target.entity);
            Size targetSize = mSize.get(target.entity);

            float currentDistance = vTmp.set(position.x + size.width * 0.5f, position.y + size.height * 0.5f)
                    .dst(targetPosition.x + targetSize.width * 0.5f, targetPosition.y + targetSize.height * 0.5f);
            if (currentDistance < fairy.range) {
                float alpha = 1f - (currentDistance / fairy.range);
                int damage = MathUtils.ceil(fairy.damage * alpha);
                player.health -= damage;
            }
        }

        if ((fairy.thinkTime -= deltaTime) <= 0f) {
            fairy.thinkTime = fairy.thinkDelay;

            velocity.x = Math.signum(fairy.targetX - position.x) * fairy.speed;
            velocity.y = Math.signum(fairy.targetY - position.y) * fairy.speed;
        }
    }
}
