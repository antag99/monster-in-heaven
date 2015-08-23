package com.github.antag99.heaven.system;

import static com.github.antag99.heaven.component.Collision.FLAG_FLOOR;
import static com.github.antag99.heaven.component.Collision.FLAG_MONSTER;
import static com.github.antag99.heaven.component.Collision.FLAG_PLAYER;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.math.MathUtils;
import com.esotericsoftware.spine.AnimationState;
import com.esotericsoftware.spine.Skeleton;
import com.github.antag99.heaven.component.Acting;
import com.github.antag99.heaven.component.Attack;
import com.github.antag99.heaven.component.Collision;
import com.github.antag99.heaven.component.Devil;
import com.github.antag99.heaven.component.DevilSpawner;
import com.github.antag99.heaven.component.Direction;
import com.github.antag99.heaven.component.Player;
import com.github.antag99.heaven.component.Position;
import com.github.antag99.heaven.component.Size;
import com.github.antag99.heaven.component.Velocity;
import com.github.antag99.heaven.component.Weight;
import com.github.antag99.heaven.util.CollisionListener;
import com.github.antag99.heaven.util.ParticleActor;
import com.github.antag99.heaven.util.SpineActor;
import com.github.antag99.retinazer.Engine;
import com.github.antag99.retinazer.EntityProcessorSystem;
import com.github.antag99.retinazer.Family;
import com.github.antag99.retinazer.Handle;
import com.github.antag99.retinazer.Mapper;
import com.github.antag99.retinazer.Wire;

@Wire
public final class DevilSpawnerSystem extends EntityProcessorSystem {
    private static final float DEVIL_WIDTH = 6f;
    private static final float DEVIL_HEIGHT = 18f;

    private Engine engine;
    private AssetSystem assetSystem;
    private CollisionSystem collisionSystem;
    private Mapper<DevilSpawner> mDevilSpawner;
    private Mapper<Position> mPosition;
    private Mapper<Size> mSize;
    private Mapper<Attack> mAttack;
    private Mapper<Devil> mDevil;
    private Mapper<Player> mPlayer;

    @SuppressWarnings("unchecked")
    public DevilSpawnerSystem() {
        super(Family.with(DevilSpawner.class));
    }

    @Override
    protected void initialize() {
        collisionSystem.addCollisionListener(FLAG_MONSTER, FLAG_PLAYER, new CollisionListener() {
            @Override
            public void onCollision(int i, int j) {
                Position devilPosition = mPosition.get(i);
                Size devilSize = mSize.get(i);

                if (mDevil.has(i) && mAttack.get(j).hit) {
                    mAttack.get(j).hit = false;
                    Handle particleEntity = engine.createEntity();
                    particleEntity.create(Position.class).xy(devilPosition.x + devilSize.width * 0.5f, devilPosition.y + devilSize.height * 0.5f);
                    particleEntity.create(Acting.class).actor(new ParticleActor(new ParticleEffect(assetSystem.devilDie)));
                    mPlayer.get(j).score += mDevil.get(i).value;
                    assetSystem.devilSound.play();
                    engine.destroyEntity(i);
                }
            }
        });
    }

    @Override
    protected void process(int entity) {
        float deltaTime = Gdx.graphics.getDeltaTime();
        DevilSpawner devilSpawner = mDevilSpawner.get(entity);
        Position position = mPosition.get(entity);
        Size size = mSize.get(entity);
        if ((devilSpawner.counter -= deltaTime) <= 0f) {
            devilSpawner.counter += MathUtils.random(devilSpawner.minDelay, devilSpawner.maxDelay);

            float devilSpeed = MathUtils.random(devilSpawner.minSpeed, devilSpawner.maxSpeed);
            float devilWidth = DEVIL_WIDTH;
            float devilHeight = DEVIL_HEIGHT;

            float devilX = MathUtils.random(position.x, position.x + size.width - devilWidth);
            float devilY = MathUtils.random(position.y, position.y + size.height - devilHeight);

            Handle devilEntity = engine.createEntity();
            devilEntity.create(Position.class).xy(devilX, devilY);
            devilEntity.create(Velocity.class);
            devilEntity.create(Size.class).size(devilWidth, devilHeight);
            devilEntity.create(Collision.class).properties(FLAG_MONSTER).collision(FLAG_PLAYER | FLAG_FLOOR);
            Devil devil = devilEntity.create(Devil.class);
            devil.speed = devilSpeed;
            devil.value = MathUtils.random(devilSpawner.minValue, devilSpawner.maxValue);
            devilEntity.create(Direction.class);
            devilEntity.create(Attack.class).delay(1f);
            devilEntity.create(Weight.class).weight(1f);
            SpineActor devilActor = new SpineActor(new Skeleton(assetSystem.devilSkeletonData),
                    new AnimationState(assetSystem.devilAnimationStateData));
            devilActor.animationState.addAnimation(0, "walk", true, 0f);
            devilEntity.create(Acting.class).actor(devilActor);
        }
    }
}
