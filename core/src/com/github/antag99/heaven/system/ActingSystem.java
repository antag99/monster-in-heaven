package com.github.antag99.heaven.system;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.IntArray;
import com.github.antag99.heaven.component.Acting;
import com.github.antag99.retinazer.Engine;
import com.github.antag99.retinazer.EntitySetListener;
import com.github.antag99.retinazer.EntitySystem;
import com.github.antag99.retinazer.Family;
import com.github.antag99.retinazer.Mapper;
import com.github.antag99.retinazer.Wire;

public final class ActingSystem extends EntitySystem {
    private @Wire Engine engine;
    private @Wire Mapper<Acting> mActing;

    private Stage stage;

    private EntitySetListener listener = new EntitySetListener() {
        @Override
        public void inserted(IntArray entities) {
            int[] items = entities.items;
            for (int i = 0, n = entities.size; i < n; i++) {
                Actor actor = mActing.get(items[i]).actor;
                actor.setUserObject(engine.createHandle(items[i]));
                stage.addActor(actor);
            }
        }

        @Override
        public void removed(IntArray entities) {
            int[] items = entities.items;
            for (int i = 0, n = entities.size; i < n; i++) {
                Actor actor = mActing.get(items[i]).actor;
                actor.setUserObject(null);
                actor.remove();
            }
        }
    };

    public ActingSystem(Stage stage) {
        this.stage = stage;
    }

    @SuppressWarnings("unchecked")
    @Override
    protected void initialize() {
        engine.getEntitiesFor(Family.with(Acting.class)).addListener(listener);
    }
}
