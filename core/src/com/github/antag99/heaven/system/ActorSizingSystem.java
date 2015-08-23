package com.github.antag99.heaven.system;

import com.github.antag99.heaven.component.Acting;
import com.github.antag99.heaven.component.Size;
import com.github.antag99.retinazer.EntityProcessorSystem;
import com.github.antag99.retinazer.Family;
import com.github.antag99.retinazer.Mapper;
import com.github.antag99.retinazer.Wire;

@Wire
public final class ActorSizingSystem extends EntityProcessorSystem {
    private Mapper<Size> mSize;
    private Mapper<Acting> mActing;

    @SuppressWarnings("unchecked")
    public ActorSizingSystem() {
        super(Family.with(Size.class, Acting.class));
    }

    @Override
    protected void process(int entity) {
        Size size = mSize.get(entity);
        Acting acting = mActing.get(entity);
        acting.actor.setWidth(size.width);
        acting.actor.setHeight(size.height);
    }
}
