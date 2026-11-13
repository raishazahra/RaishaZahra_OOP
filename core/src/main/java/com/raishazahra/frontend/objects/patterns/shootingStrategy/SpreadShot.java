package com.raishazahra.frontend.objects.patterns.shootingStrategy;

import com.raishazahra.frontend.objects.patterns.ShootingPattern;
import com.raishazahra.frontend.systems.BulletManager;

public class SpreadShot implements ShootingPattern {

    private final float speed;
    private final int bulletCount;
    private final int damage;
    private final float spreadAngle;

    public SpreadShot(float speed, int bulletCount, float spreadAngle) {
        this(speed, bulletCount, spreadAngle, 1);
    }

    public SpreadShot(
        float speed,
        int bulletCount,
        float spreadAngle,
        int damage
    ) {
        this.speed = speed;
        this.bulletCount = Math.max(1, bulletCount);
        this.spreadAngle = spreadAngle;
        this.damage = damage;
    }

    @Override
    public void execute(
        float originX,
        float originY,
        BulletManager bulletManager,
        boolean isPlayer
    ) {
        if (bulletManager == null) return;

        float startAngle = 90f - spreadAngle / 2f;
        float angleStep = bulletCount > 1
            ? spreadAngle / (bulletCount - 1)
            : 0f;

        for (int i = 0; i < bulletCount; i++) {
            float angle = startAngle + angleStep * i;
            float radians = (float) Math.toRadians(angle);

            float vx = (float) Math.cos(radians) * speed;
            float vy = (float) Math.sin(radians) * speed;

            if (isPlayer) {
                bulletManager.spawnPlayerBullet(
                    originX, originY, vx, vy, damage
                );
            } else {
                bulletManager.spawnEnemyBullet(
                    originX, originY, vx, vy, damage
                );
            }
        }
    }
}
