package com.raishazahra.frontend.objects.patterns.shootingStrategy;
import com.raishazahra.frontend.objects.patterns.ShootingPattern;
import com.raishazahra.frontend.systems.BulletManager;
public class AlternatingRingShot implements ShootingPattern {
    private final float baseSpeed;
    private final int count;
    private final int damage;
    private final int layers;
    private final float speedStep;
    private float offset;
    private float rotationSpeed;
    public AlternatingRingShot(
        float baseSpeed,
        int count,
        int damage,
        int layers,
        float speedStep
    ) {
        this.baseSpeed = baseSpeed;
        this.count = Math.max(3, count);
        this.damage = damage;
        this.layers = Math.max(1, layers);
        this.speedStep = speedStep;
    }
    public AlternatingRingShot(float speed, int count, int damage) {
        this(speed, count, damage, 2, 35f);
    }
    @Override
    public void execute(
        float originX,
        float originY,
        BulletManager bulletManager,
        boolean isPlayer
    ) {
        if (bulletManager == null) return;
        String[] bulletColors = {
            "bullet_danmaku_red",
            "bullet_danmaku_blue",
            "bullet_danmaku_purple",
            "bullet_danmaku_green"
        };
        float angleStep = 360f / count;
        for (int layer = 0; layer < layers; layer++) {
            for (int i = 0; i < count; i++) {
                float angle = (float) Math.toRadians(
                    offset + i * angleStep
                        + angleStep / layers * layer
                );
                float velocity = baseSpeed + layer * speedStep;
                float vx = (float) Math.cos(angle) * velocity;
                float vy = (float) Math.sin(angle) * velocity;
                if (isPlayer) {
                    bulletManager.spawnPlayerBullet(
                        originX, originY, vx, vy, damage
                    );
                } else {
                    bulletManager.spawnEnemyBullet(
                        originX, originY, vx, vy, damage,
                        bulletColors[layer % bulletColors.length]
                    );
                }
            }
        }
        offset = (offset + rotationSpeed) % 360f;
    }
    public void setRotationSpeed(float rotationSpeed) {
        this.rotationSpeed = rotationSpeed;
    }
}
