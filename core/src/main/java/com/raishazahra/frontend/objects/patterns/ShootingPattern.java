
package com.raishazahra.frontend.objects.patterns;

import com.raishazahra.frontend.systems.BulletManager;

public interface ShootingPattern {

    void execute(
        float originX,
        float originY,
        BulletManager bulletManager,
        boolean isPlayer
    );
}
