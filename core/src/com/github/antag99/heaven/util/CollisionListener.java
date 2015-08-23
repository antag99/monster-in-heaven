package com.github.antag99.heaven.util;

import com.github.antag99.heaven.system.CollisionSystem;

/**
 * Callback for {@link CollisionSystem}.
 */
public interface CollisionListener {

    /**
     * Invoked when two entities collide.
     *
     * @param i
     *            index of the first entity.
     * @param j
     *            index of the second entity.
     */
    public void onCollision(int i, int j);
}
