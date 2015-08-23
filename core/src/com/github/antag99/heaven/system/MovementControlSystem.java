package com.github.antag99.heaven.system;

import com.github.antag99.heaven.component.Control;
import com.github.antag99.heaven.component.Movement;
import com.github.antag99.heaven.component.Velocity;
import com.github.antag99.retinazer.EntityProcessorSystem;
import com.github.antag99.retinazer.Family;
import com.github.antag99.retinazer.Mapper;
import com.github.antag99.retinazer.Wire;

@Wire
public final class MovementControlSystem extends EntityProcessorSystem {
    private Mapper<Control> mControl;
    private Mapper<Movement> mMovement;
    private Mapper<Velocity> mVelocity;

    @SuppressWarnings("unchecked")
    public MovementControlSystem() {
        super(Family.with(Control.class, Movement.class, Velocity.class));
    }

    @Override
    protected void process(int entity) {
        Control control = mControl.get(entity);
        Movement movement = mMovement.get(entity);
        Velocity velocity = mVelocity.get(entity);

        if (control.moveLeft && !control.moveRight)
            velocity.x = -movement.speed;
        else if (control.moveRight && !control.moveLeft)
            velocity.x = movement.speed;
        else
            velocity.x = 0f;
        control.moveLeft = false;
        control.moveRight = false;
    }
}
