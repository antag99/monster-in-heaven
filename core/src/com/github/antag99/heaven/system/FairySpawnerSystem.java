package com.github.antag99.heaven.system;

import static com.github.antag99.heaven.component.Collision.FLAG_FLOOR;
import static com.github.antag99.heaven.component.Collision.FLAG_MONSTER;
import static com.github.antag99.heaven.component.Collision.FLAG_PLAYER;

import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.math.MathUtils;
import com.esotericsoftware.spine.AnimationState;
import com.esotericsoftware.spine.Skeleton;
import com.github.antag99.heaven.GameScreen;
import com.github.antag99.heaven.component.Acting;
import com.github.antag99.heaven.component.Attack;
import com.github.antag99.heaven.component.Collision;
import com.github.antag99.heaven.component.Direction;
import com.github.antag99.heaven.component.Fairy;
import com.github.antag99.heaven.component.FairySpawner;
import com.github.antag99.heaven.component.Player;
import com.github.antag99.heaven.component.Position;
import com.github.antag99.heaven.component.Size;
import com.github.antag99.heaven.component.Velocity;
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
public final class FairySpawnerSystem extends EntityProcessorSystem {
    private static final float FAIRY_WIDTH = 4f;
    private static final float FAIRY_HEIGHT = 4f;

    private Engine engine;
    private CollisionSystem collisionSystem;
    private AssetSystem assetSystem;
    private DeltaSystem deltaSystem;
    private Mapper<FairySpawner> mFairySpawner;
    private Mapper<Position> mPosition;
    private Mapper<Size> mSize;
    private Mapper<Attack> mAttack;
    private Mapper<Fairy> mFairy;
    private Mapper<Player> mPlayer;
    private GameScreen gameScreen;

    @SuppressWarnings("unchecked")
    public FairySpawnerSystem() {
        super(Family.with(FairySpawner.class, Position.class, Size.class));
    }

    @Override
    protected void initialize() {
        collisionSystem.addCollisionListener(FLAG_MONSTER, FLAG_PLAYER, new CollisionListener() {
            @Override
            public void onCollision(int i, int j) {
                if (mFairy.has(i) && mAttack.get(j).hit) {
                    mAttack.get(j).hit = false;
                    mPlayer.get(j).score += mFairy.get(i).value;
                    assetSystem.fairySound.play();
                    Position fairyPosition = mPosition.get(i);
                    Size fairySize = mSize.get(i);
                    Handle particleEntity = engine.createEntity();
                    particleEntity.create(Position.class).xy(fairyPosition.x + fairySize.width * 0.5f, fairyPosition.y + fairySize.height * 0.5f);
                    particleEntity.create(Acting.class).actor(new ParticleActor(new ParticleEffect(gameScreen.isHell ? assetSystem.hellFairyDie : assetSystem.fairyDie)));
                    engine.destroyEntity(i);
                }
            }
        });
    }

    @Override
    protected void process(int entity) {
        float deltaTime = deltaSystem.getDeltaTime();
        FairySpawner fairySpawner = mFairySpawner.get(entity);
        Position position = mPosition.get(entity);
        Size size = mSize.get(entity);
        if ((fairySpawner.counter -= deltaTime) <= 0f) {
            fairySpawner.counter += MathUtils.random(fairySpawner.minDelay, fairySpawner.maxDelay);

            float fairyScale = MathUtils.random(fairySpawner.minScale, fairySpawner.maxScale);
            float fairyWidth = FAIRY_WIDTH * fairyScale;
            float fairyHeight = FAIRY_HEIGHT * fairyScale;

            float fairyX = MathUtils.random(position.x, position.x + size.width - fairyWidth);
            float fairyY = MathUtils.random(position.y, position.y + size.height - fairyHeight);

            Handle fairyEntity = engine.createEntity();
            fairyEntity.create(Position.class).xy(fairyX, fairyY);
            fairyEntity.create(Velocity.class);
            fairyEntity.create(Size.class).size(fairyWidth, fairyHeight);
            fairyEntity.create(Collision.class).properties(FLAG_MONSTER).collision(FLAG_PLAYER | FLAG_FLOOR);
            Fairy fairy = fairyEntity.create(Fairy.class);
            fairy.scale = fairyScale;
            fairy.value = MathUtils.random(fairySpawner.minValue, fairySpawner.maxValue);
            fairyEntity.create(Direction.class);
            fairyEntity.create(Attack.class).delay(0.5f);
            SpineActor fairyActor = new SpineActor(new Skeleton(assetSystem.fairySkeletonData),
                    new AnimationState(assetSystem.fairyAnimationStateData));
            // fairyActor.debug();
            fairyActor.animationState.addAnimation(0, "fly", true, 0f);
            fairyEntity.create(Acting.class).actor(fairyActor);
        }
    }
}
