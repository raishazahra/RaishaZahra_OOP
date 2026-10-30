package com.raishazahra.frontend.objects.patterns.shootingStrategy;

import com.raishazahra.frontend.objects.GameObject;
import com.raishazahra.frontend.objects.patterns.ShootingPattern;
import com.raishazahra.frontend.systems.BulletManager;

public class SpreadShot implements ShootingPattern {

    private float speed;
    private int numBullets;
    private float spreadAngleDegrees;
    private GameObject target;

    public SpreadShot(
        float speed,
        int numBullets,
        float spreadAngleDegrees) {
        this(speed, numBullets, spreadAngleDegrees, null);
    }

    public SpreadShot(
        float speed,
        int numBullets,
        float spreadAngleDegrees,
        GameObject target) {
        this.speed = speed;
        this.numBullets = numBullets;
        this.spreadAngleDegrees = spreadAngleDegrees;
        this.target = target;
    }

    @Override
    public void execute(
        float originX,
        float originY,
        BulletManager bulletManager,
        boolean isPlayer) {

        if (bulletManager == null || numBullets <= 0) {
            return;
        }

        float centerAngle;

        if (target != null) {
            float targetX = target.getX() + target.getWidth() / 2f;
            float targetY = target.getY() + target.getHeight() / 2f;

            centerAngle = (float) Math.toDegrees(
                Math.atan2(targetY - originY, targetX - originX)
            );
        } else {
            centerAngle = isPlayer ? 90f : 270f;
        }

        float startAngle = centerAngle - spreadAngleDegrees / 2f;

        float angleStep = numBullets > 1
            ? spreadAngleDegrees / (numBullets - 1)
            : 0f;

        int damage = isPlayer ? 25 : 15;

        for (int i = 0; i < numBullets; i++) {
            float angleDegrees = startAngle + i * angleStep;
            float angleRadians = (float) Math.toRadians(angleDegrees);

            float vx = (float) (speed * Math.cos(angleRadians));
            float vy = (float) (speed * Math.sin(angleRadians));

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

    public float getSpeed() {
        return speed;
    }

    public void setSpeed(float speed) {
        this.speed = speed;
    }

    public int getNumBullets() {
        return numBullets;
    }

    public void setNumBullets(int numBullets) {
        this.numBullets = numBullets;
    }

    public float getSpreadAngleDegrees() {
        return spreadAngleDegrees;
    }

    public void setSpreadAngleDegrees(float spreadAngleDegrees) {
        this.spreadAngleDegrees = spreadAngleDegrees;
    }

    public GameObject getTarget() {
        return target;
    }

    public void setTarget(GameObject target) {
        this.target = target;
    }
}
