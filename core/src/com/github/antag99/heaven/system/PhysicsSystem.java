package com.github.antag99.heaven.system;

import static com.github.antag99.heaven.component.Collision.FLAG_FLOOR;
import static com.github.antag99.heaven.component.Collision.FLAG_WALL;

import com.github.antag99.heaven.component.Collision;
import com.github.antag99.heaven.component.Position;
import com.github.antag99.heaven.component.Size;
import com.github.antag99.heaven.component.Velocity;
import com.github.antag99.heaven.util.CollisionListener;
import com.github.antag99.retinazer.EntityProcessorSystem;
import com.github.antag99.retinazer.Family;
import com.github.antag99.retinazer.Mapper;
import com.github.antag99.retinazer.Wire;
import com.github.antag99.retinazer.Wire.Ignore;

@Wire
public final class PhysicsSystem extends EntityProcessorSystem {
    private CollisionSystem collisionSystem;
    private DeltaSystem deltaSystem;
    private Mapper<Position> mPosition;
    private Mapper<Velocity> mVelocity;
    private Mapper<Collision> mCollision;
    private Mapper<Size> mSize;

    private void resolve(Position iPosition, Velocity iVelocity, Size iSize, Collision iCollision,
            Position jPosition, Velocity jVelocity, Size jSize, Collision jCollision, float deltaTime) {
        if (iVelocity != null) {
            for (int state = 0; state < 4; state++) {
                float iX = iPosition.x;
                float iY = iPosition.y;
                float iW = iSize.width;
                float iH = iSize.height;

                float jX = jPosition.x;
                float jY = jPosition.y;
                float jW = jSize.width;
                float jH = jSize.height;

                if (iX + iW > jX && iY + iH > jY && jX + jW > iX && jY + jH > iY) {
                    switch (state) {
                    case 0:
                        iPosition.x -= iVelocity.x * deltaTime;
                        break;
                    case 1:
                        iPosition.x += iVelocity.x * deltaTime;
                        iPosition.y -= iVelocity.y * deltaTime;
                        break;
                    case 2:
                        iPosition.x -= iVelocity.x * deltaTime;
                        break;
                    }
                } else {
                    switch (state) {
                    case 1:
                        if (iVelocity.x > 0f)
                            iPosition.x = jX - iW;
                        else if (iVelocity.x < 0f)
                            iPosition.x = jX + jW;
                        iVelocity.x = 0f;
                        break;
                    case 2:
                        if (iVelocity.y > 0f)
                            iPosition.y = jY - iH;
                        else if (iVelocity.y < 0f)
                            iPosition.y = jY + jH;
                        iVelocity.y = 0f;
                        break;
                    case 3:
                        if (iVelocity.x > 0f)
                            iPosition.x = jX - iW;
                        else if (iVelocity.x < 0f)
                            iPosition.x = jX + jW;
                        iVelocity.x = 0f;
                        iVelocity.y = 0f;
                        break;
                    }
                    break;
                }
            }
        }

    }

    private @Ignore CollisionListener collisionListener = new CollisionListener() {
        @Override
        public void onCollision(int i, int j) {
            float deltaTime = deltaSystem.getDeltaTime();

            Position iPosition = mPosition.get(i);
            Size iSize = mSize.get(i);
            Velocity iVelocity = mVelocity.get(i);
            Collision iCollision = mCollision.get(i);

            Position jPosition = mPosition.get(j);
            Size jSize = mSize.get(j);
            Velocity jVelocity = mVelocity.get(j);
            Collision jCollision = mCollision.get(j);

            resolve(iPosition, iVelocity, iSize, iCollision,
                    jPosition, jVelocity, jSize, jCollision, deltaTime);
        }

    };

    @SuppressWarnings("unchecked")
    public PhysicsSystem() {
        super(Family.with(Position.class, Velocity.class));
    }

    @Override
    protected void initialize() {
        collisionSystem.addCollisionListener(0xFFFFFFFF, FLAG_FLOOR | FLAG_WALL, collisionListener);
    }

    @Override
    protected void process(int entity) {
        float deltaTime = deltaSystem.getDeltaTime();

        Position position = mPosition.get(entity);
        Velocity velocity = mVelocity.get(entity);

        position.x += velocity.x * deltaTime;
        position.y += velocity.y * deltaTime;
    }
}
