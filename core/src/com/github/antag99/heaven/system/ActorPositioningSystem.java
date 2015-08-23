package com.github.antag99.heaven.system;

import com.github.antag99.heaven.component.Acting;
import com.github.antag99.heaven.component.Position;
import com.github.antag99.retinazer.EntityProcessorSystem;
import com.github.antag99.retinazer.Family;
import com.github.antag99.retinazer.Mapper;
import com.github.antag99.retinazer.Wire;

@Wire
public final class ActorPositioningSystem extends EntityProcessorSystem {
    private Mapper<Position> mPosition;
    private Mapper<Acting> mActing;

    @SuppressWarnings("unchecked")
    public ActorPositioningSystem() {
        super(Family.with(Position.class, Acting.class));
    }

    @Override
    protected void process(int entity) {
        Position position = mPosition.get(entity);
        Acting acting = mActing.get(entity);
        acting.actor.setX(position.x);
        acting.actor.setY(position.y);
    }
}
