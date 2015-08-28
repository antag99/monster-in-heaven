package com.github.antag99.heaven.system;

import com.github.antag99.heaven.component.Velocity;
import com.github.antag99.heaven.component.Weight;
import com.github.antag99.retinazer.EntityProcessorSystem;
import com.github.antag99.retinazer.Family;
import com.github.antag99.retinazer.Mapper;
import com.github.antag99.retinazer.Wire;

@Wire
public final class GravitySystem extends EntityProcessorSystem {
    private DeltaSystem deltaSystem;
    private Mapper<Velocity> mVelocity;
    private Mapper<Weight> mWeight;
    private float gravity;
    private float acceleration;

    @SuppressWarnings("unchecked")
    public GravitySystem(float gravity, float acceleration) {
        super(Family.with(Velocity.class, Weight.class));
        this.gravity = gravity;
        this.acceleration = acceleration;
    }

    @Override
    protected void process(int entity) {
        float deltaTime = deltaSystem.getDeltaTime();
        Velocity velocity = mVelocity.get(entity);
        Weight weight = mWeight.get(entity);

        if (velocity.y > gravity) {
            velocity.y += gravity * weight.weight * acceleration * deltaTime;
            if (velocity.y < gravity) {
                velocity.y = gravity;
            }
        }
    }
}
