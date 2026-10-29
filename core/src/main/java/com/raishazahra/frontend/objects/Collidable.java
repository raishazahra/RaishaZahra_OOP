package com.raishazahra.frontend.objects;

import com.badlogic.gdx.math.Rectangle;

public interface Collidable {

    Rectangle getCoreHitbox();

    Rectangle getGrazeHitbox();

    void onCollision(Collidable other);
}
