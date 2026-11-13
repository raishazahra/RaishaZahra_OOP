package com.raishazahra.frontend.objects.patterns.entityStrategy;

import com.raishazahra.frontend.objects.GameObject;

public interface EntityMovementPattern {
    void move(GameObject entity, float delta);
}
