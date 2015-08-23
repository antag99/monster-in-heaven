package com.github.antag99.heaven.system;

import com.github.antag99.heaven.component.Attack;
import com.github.antag99.heaven.component.Control;
import com.github.antag99.retinazer.EntityProcessorSystem;
import com.github.antag99.retinazer.Family;
import com.github.antag99.retinazer.Mapper;
import com.github.antag99.retinazer.Wire;

@Wire
public final class AttackControlSystem extends EntityProcessorSystem {
    private Mapper<Control> mControl;
    private Mapper<Attack> mAttack;

    @SuppressWarnings("unchecked")
    public AttackControlSystem() {
        super(Family.with(Control.class, Attack.class));
    }

    @Override
    protected void process(int entity) {
        Control control = mControl.get(entity);
        Attack attack = mAttack.get(entity);

        if (control.attack && attack.counter <= 0) {
            attack.counter = attack.delay;
        }

        control.attack = false;
    }
}
