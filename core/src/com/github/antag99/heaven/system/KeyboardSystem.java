package com.github.antag99.heaven.system;

import com.badlogic.gdx.Gdx;
import com.github.antag99.heaven.component.Control;
import com.github.antag99.heaven.component.Keyboard;
import com.github.antag99.retinazer.EntityProcessorSystem;
import com.github.antag99.retinazer.Family;
import com.github.antag99.retinazer.Mapper;
import com.github.antag99.retinazer.Wire;

@Wire
public final class KeyboardSystem extends EntityProcessorSystem {
    private Mapper<Control> mControl;
    private Mapper<Keyboard> mKeyboard;

    @SuppressWarnings("unchecked")
    public KeyboardSystem() {
        super(Family.with(Control.class, Keyboard.class));
    }

    @Override
    protected void process(int entity) {
        Control control = mControl.get(entity);
        Keyboard keyboard = mKeyboard.get(entity);

        control.moveLeft |= Gdx.input.isKeyPressed(keyboard.keyLeft);
        control.moveRight |= Gdx.input.isKeyPressed(keyboard.keyRight);
        control.attack |= Gdx.input.isKeyPressed(keyboard.keyAttack);
    }
}
