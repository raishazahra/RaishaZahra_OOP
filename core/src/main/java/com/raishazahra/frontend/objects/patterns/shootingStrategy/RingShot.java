package com.raishazahra.frontend.objects.patterns.shootingStrategy;
import com.raishazahra.frontend.objects.bullets.Bullet;
import com.raishazahra.frontend.objects.patterns.ShootingPattern;
import com.raishazahra.frontend.objects.patterns.bulletStrategy.BulletMovementPattern;
import com.raishazahra.frontend.systems.BulletManager;
public class RingShot implements ShootingPattern {
    private final float speed;
    private final int count;
    private final int damage;
    private final BulletMovementPattern movementPattern;
    public RingShot(
        float speed,
        int count,
        int damage,
        BulletMovementPattern movementPattern
    ) {
        this.speed = speed;
        this.count = Math.max(3, count);
        this.damage = damage;
        this.movementPattern = movementPattern;
    }
    public RingShot(float speed, int count, int damage) {
        this(speed, count, damage, null);
    }
    @Override
    public void execute(
        float originX,
        float originY,
        BulletManager bulletManager,
        boolean isPlayer
    ) {
        if (bulletManager == null) return;
        for (int i = 0; i < count; i++) {
            float angle = (float) Math.toRadians(
                i * 360f / count
            );
            float vx = (float) Math.cos(angle) * speed;
            float vy = (float) Math.sin(angle) * speed;
            Bullet bullet;
            if (isPlayer) {
                bullet = bulletManager.spawnPlayerBullet(
                    originX, originY, vx, vy, damage
                );
            } else {
                bullet = bulletManager.spawnEnemyBullet(
                    originX, originY, vx, vy, damage
                );
            }
            if (bullet != null && movementPattern != null) {
                bullet.setMovementPattern(movementPattern);
            }
        }
    }
}
