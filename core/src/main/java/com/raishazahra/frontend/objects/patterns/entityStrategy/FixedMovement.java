package com.raishazahra.frontend.objects.patterns.entityStrategy;

import com.raishazahra.frontend.objects.GameObject;

public class FixedMovement implements EntityMovementPattern {

    @Override
    public void move(GameObject entity, float delta) {
        // Entity remains stationary.
    }
}
