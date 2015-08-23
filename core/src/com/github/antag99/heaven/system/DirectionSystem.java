package com.github.antag99.heaven.system;

import com.github.antag99.heaven.component.Direction;
import com.github.antag99.heaven.component.Velocity;
import com.github.antag99.retinazer.EntityProcessorSystem;
import com.github.antag99.retinazer.Family;
import com.github.antag99.retinazer.Mapper;
import com.github.antag99.retinazer.Wire;

@Wire
public final class DirectionSystem extends EntityProcessorSystem {
    private Mapper<Direction> mDirection;
    private Mapper<Velocity> mVelocity;

    @SuppressWarnings("unchecked")
    public DirectionSystem() {
        super(Family.with(Direction.class, Velocity.class));
    }

    @Override
    protected void process(int entity) {
        Direction direction = mDirection.get(entity);
        Velocity velocity = mVelocity.get(entity);

        direction.x = velocity.x > 0 ? 1 : velocity.x < 0 ? -1 : direction.x;
        direction.y = velocity.y > 0 ? 1 : velocity.y < 0 ? -1 : direction.y;
    }
}
