package com.github.antag99.heaven.component;

import com.github.antag99.retinazer.Component;

public final class DevilSpawner implements Component {
    public float counter;

    public float minDelay = 5f;
    public float maxDelay = 15f;

    public float minSpeed = 10f;
    public float maxSpeed = 20f;

    public int minValue = 4;
    public int maxValue = 8;
}
