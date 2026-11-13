package com.raishazahra.frontend.objects.patterns.bulletStrategy;

import com.raishazahra.frontend.objects.bullets.Bullet;

public interface BulletMovementPattern {

    void move(Bullet bullet, float delta);

}
