package com.raishazahra.frontend.objects.patterns.bulletStrategy;

import com.raishazahra.frontend.objects.bullets.Bullet;

public class LinearBulletMovement implements BulletMovementPattern {

    @Override
    public void move(Bullet bullet, float delta) {

        if (bullet == null) {
            return;
        }

        float angleRadians = (float) Math.toRadians(
            bullet.getAngle()
        );

        float velocityX = (float) Math.cos(angleRadians)
            * bullet.getSpeed();

        float velocityY = (float) Math.sin(angleRadians)
            * bullet.getSpeed();

        bullet.setX(
            bullet.getX() + velocityX * delta
        );

        bullet.setY(
            bullet.getY() + velocityY * delta
        );
    }
}
