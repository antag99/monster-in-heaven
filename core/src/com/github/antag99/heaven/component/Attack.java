package com.github.antag99.heaven.component;

import com.github.antag99.retinazer.Component;

public final class Attack implements Component {
    /** Down-counter in seconds for the current attack */
    public float counter;
    /** Time in seconds to complete an attack */
    public float delay;
    /** The attack will activate when this limit is passed */
    public float trigger;
    /** Whether the attack was activated this frame */
    public boolean hit = false;
    /** Whether the attack was initiated this frame */
    public boolean hot = false;

    public Attack delay(float delay) {
        this.delay = delay;
        return this;
    }

    public Attack trigger(float trigger) {
        this.trigger = trigger;
        return this;
    }

    /**
     * @return Whether this attack is active
     */
    public boolean isActive() {
        return counter > 0f;
    }

    /**
     * Performs the attack
     */
    public void perform() {
        counter = delay;
        hot = true;
    }
}
