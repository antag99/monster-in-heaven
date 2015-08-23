package com.github.antag99.heaven;

import com.badlogic.gdx.Game;

public class HeavenGame extends Game {
    @Override
    public void create() {
        setScreen(new GameScreen(this));
    }
}
