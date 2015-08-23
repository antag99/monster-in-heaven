package com.github.antag99.heaven.component;

import com.github.antag99.retinazer.Component;

public final class FairySpawner implements Component {
    public float counter = 0f;

    public float minDelay = 0.2f;
    public float maxDelay = 5f;

    public float minSpeed = 12f;
    public float maxSpeed = 20f;

    public int minValue = 2;
    public int maxValue = 5;

    public float minScale = 1f;
    // TODO: This is broken
    public float maxScale = 1f; // 1.5f;
}
