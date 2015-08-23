package com.github.antag99.heaven.system;

import com.badlogic.gdx.Gdx;
import com.github.antag99.heaven.component.Attack;
import com.github.antag99.retinazer.EntityProcessorSystem;
import com.github.antag99.retinazer.Family;
import com.github.antag99.retinazer.Mapper;
import com.github.antag99.retinazer.Wire;

@Wire
public final class AttackSystem extends EntityProcessorSystem {
    private Mapper<Attack> mAttack;

    @SuppressWarnings("unchecked")
    public AttackSystem() {
        super(Family.with(Attack.class));
    }

    @Override
    protected void process(int entity) {
        float deltaTime = Gdx.graphics.getDeltaTime();
        Attack attack = mAttack.get(entity);
        attack.hit = false;
        attack.hot = false;
        if (attack.isActive()) {
            float previousCounter = attack.counter;
            attack.counter -= deltaTime;
            if (previousCounter > attack.trigger && attack.counter <= attack.trigger) {
                attack.hit = true;
            }
        }
    }
}
