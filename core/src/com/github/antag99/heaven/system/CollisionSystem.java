package com.github.antag99.heaven.system;

import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.IntArray;
import com.github.antag99.heaven.component.Collision;
import com.github.antag99.heaven.component.Position;
import com.github.antag99.heaven.component.Size;
import com.github.antag99.heaven.util.CollisionListener;
import com.github.antag99.retinazer.Engine;
import com.github.antag99.retinazer.EntitySet;
import com.github.antag99.retinazer.EntitySystem;
import com.github.antag99.retinazer.Family;
import com.github.antag99.retinazer.Mapper;
import com.github.antag99.retinazer.Wire;
import com.github.antag99.retinazer.Wire.Ignore;

@Wire
public final class CollisionSystem extends EntitySystem {
    private Engine engine;
    private @Ignore EntitySet entities;
    private Mapper<Position> mPosition;
    private Mapper<Size> mSize;
    private Mapper<Collision> mCollision;

    private static class CollisionListenerRegistration {
        CollisionListener listener;
        int iProperties;
        int jProperties;
    }

    private Array<CollisionListenerRegistration> collisionListeners =
            new Array<CollisionListenerRegistration>(CollisionListenerRegistration.class);

    public void addCollisionListener(int iProperties, int jProperties, CollisionListener listener) {
        CollisionListenerRegistration registration = new CollisionListenerRegistration();
        registration.listener = listener;
        registration.iProperties = iProperties;
        registration.jProperties = jProperties;
        collisionListeners.add(registration);
    }

    public void removeCollisionListener(CollisionListener listener) {
        for (int i = 0; i < collisionListeners.size; i++) {
            if (collisionListeners.get(i).listener == listener) {
                collisionListeners.removeIndex(i--);
            }
        }
    }

    public CollisionSystem() {
    }

    @SuppressWarnings("unchecked")
    @Override
    protected void initialize() {
        entities = engine.getEntitiesFor(Family.with(Position.class, Size.class, Collision.class));
    }

    @Override
    protected void update() {
        IntArray indices = entities.getIndices();
        int[] items = indices.items;
        for (int i = 0, n = indices.size; i < n; i++) {
            Position iPosition = mPosition.get(items[i]);
            Size iSize = mSize.get(items[i]);
            Collision iCollision = mCollision.get(items[i]);
            for (int j = 0; j < n; j++) {
                if (i == j)
                    continue;
                Position jPosition = mPosition.get(items[j]);
                Size jSize = mSize.get(items[j]);
                Collision jCollision = mCollision.get(items[j]);

                if ((iCollision.properties & jCollision.collision) != 0 ||
                        (jCollision.properties & iCollision.collision) != 0) {
                    float iX = iPosition.x;
                    float iY = iPosition.y;
                    float iW = iSize.width;
                    float iH = iSize.height;

                    float jX = jPosition.x;
                    float jY = jPosition.y;
                    float jW = jSize.width;
                    float jH = jSize.height;

                    if (iX + iW > jX && iY + iH > jY && jX + jW > iX && jY + jH > iY) {
                        CollisionListenerRegistration[] listeners = collisionListeners.items;
                        for (int ii = 0, nn = collisionListeners.size; ii < nn; ii++) {
                            CollisionListenerRegistration listener = listeners[ii];
                            if ((listener.iProperties & iCollision.properties) != 0 &&
                                    (listener.jProperties & jCollision.properties) != 0) {
                                listeners[ii].listener.onCollision(items[i], items[j]);
                            } else if ((listener.iProperties & jCollision.properties) != 0 &&
                                    (listener.jProperties & iCollision.properties) != 0) {
                                listeners[ii].listener.onCollision(items[j], items[i]);
                            }
                        }
                    }
                }
            }
        }
    }
}
