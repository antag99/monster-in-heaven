package com.github.antag99.heaven.component;

import com.github.antag99.retinazer.Component;

public final class Fairy implements Component {

    public float thinkTime = 0f;
    public float thinkDelay = 1f;

    // Target position
    public float targetX;
    public float targetY;

    // Distance between current position and target position to select new target or attack
    public float sensor = 8f;
    // Distance between current position and player position to attack
    public float charge = 8f;
    // Distance between current position and player position to damage
    public float range = 6f;

    public float speed = 10f;

    public int value = 5;
    public int damage = 5;

    public float scale = 1f;

}
