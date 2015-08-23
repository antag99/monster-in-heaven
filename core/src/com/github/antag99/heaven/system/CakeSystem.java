package com.github.antag99.heaven.system;

import static com.github.antag99.heaven.component.Collision.FLAG_CAKE;
import static com.github.antag99.heaven.component.Collision.FLAG_FLOOR;
import static com.github.antag99.heaven.component.Collision.FLAG_PLAYER;

import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.github.antag99.heaven.component.Acting;
import com.github.antag99.heaven.component.Attack;
import com.github.antag99.heaven.component.Cake;
import com.github.antag99.heaven.component.Player;
import com.github.antag99.heaven.component.Position;
import com.github.antag99.heaven.component.Size;
import com.github.antag99.heaven.util.CollisionListener;
import com.github.antag99.heaven.util.ParticleActor;
import com.github.antag99.retinazer.Engine;
import com.github.antag99.retinazer.EntitySystem;
import com.github.antag99.retinazer.Handle;
import com.github.antag99.retinazer.Mapper;
import com.github.antag99.retinazer.Wire;

@Wire
public final class CakeSystem extends EntitySystem {
    private Engine engine;
    private CollisionSystem collisionSystem;
    private AssetSystem assetSystem;
    private Mapper<Cake> mCake;
    private Mapper<Player> mPlayer;
    private Mapper<Attack> mAttack;
    private Mapper<Position> mPosition;
    private Mapper<Size> mSize;

    @Override
    protected void initialize() {
        collisionSystem.addCollisionListener(FLAG_CAKE, FLAG_PLAYER, new CollisionListener() {
            @Override
            public void onCollision(int i, int j) {
                Position cakePosition = mPosition.get(i);
                Size cakeSize = mSize.get(i);

                Position playerPosition = mPosition.get(j);
                Size playerSize = mSize.get(j);

                if (mAttack.get(j).hit && cakePosition.y > playerPosition.y + playerSize.height * 0.3f) {
                    mAttack.get(j).hit = false;
                    assetSystem.eatSound.play();
                    mPlayer.get(j).score += mCake.get(i).value;
                    Handle particleEntity = engine.createEntity();
                    particleEntity.create(Position.class).xy(cakePosition.x + cakeSize.width * 0.5f, cakePosition.y + cakeSize.height * 0.5f);
                    particleEntity.create(Acting.class).actor(new ParticleActor(new ParticleEffect(assetSystem.eatCake)));
                    engine.destroyEntity(i);
                }
            }
        });

        collisionSystem.addCollisionListener(FLAG_CAKE, FLAG_FLOOR, new CollisionListener() {
            @Override
            public void onCollision(int i, int j) {
                Position cakePosition = mPosition.get(i);
                Size cakeSize = mSize.get(i);

                Handle particleEntity = engine.createEntity();
                particleEntity.create(Position.class).xy(cakePosition.x + cakeSize.width * 0.5f, cakePosition.y + cakeSize.height * 0.5f);
                particleEntity.create(Acting.class).actor(new ParticleActor(new ParticleEffect(assetSystem.dieCake)));
                engine.destroyEntity(i);
            }
        });

    }
}
