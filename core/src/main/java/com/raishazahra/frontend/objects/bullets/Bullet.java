package com.raishazahra.frontend.objects.bullets;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import com.raishazahra.frontend.objects.BulletType;
import com.raishazahra.frontend.objects.GameObject;
import com.raishazahra.frontend.objects.patterns.bulletStrategy.BulletMovementPattern;

public class Bullet extends GameObject {

    private BulletType bulletType;
    private int damage;

    private float vx;
    private float vy;

    private float angle = 90f;

    private boolean isPlayerBullet;
    private boolean grazed;

    // Module 9: Bullet Strategy
    private BulletMovementPattern movementPattern;

    // =========================================
    // CONSTRUCTOR
    // =========================================

    public Bullet(
        float x,
        float y,
        BulletType bulletType,
        int damage
    ) {
        super(x, y, 16, 16, 400f, Color.YELLOW);

        this.bulletType = bulletType;
        this.damage = damage;

        this.vx = 0f;
        this.vy = 400f;

        this.angle = 90f;

        this.isPlayerBullet = true;
        this.grazed = false;

        this.movementPattern = null;
    }

    public Bullet(
        float x,
        float y,
        float speed,
        BulletType bulletType,
        int damage
    ) {
        super(x, y, 16, 16, speed, Color.YELLOW);

        this.bulletType = bulletType;
        this.damage = damage;

        this.vx = 0f;
        this.vy = speed;

        this.angle = speed < 0 ? 270f : 90f;

        this.isPlayerBullet = true;
        this.grazed = false;

        this.movementPattern = null;
    }

    // =========================================
    // OBJECT POOL INITIALIZATION
    // =========================================

    public void init(
        float x,
        float y,
        float vx,
        float vy,
        BulletType bulletType,
        int damage,
        boolean isPlayerBullet
    ) {
        this.x = x;
        this.y = y;

        this.vx = vx;
        this.vy = vy;

        this.angle = (float) Math.toDegrees(
            Math.atan2(vy, vx)
        );

        this.speed = (float) Math.hypot(vx, vy);

        this.bulletType = bulletType;
        this.damage = damage;

        this.isPlayerBullet = isPlayerBullet;

        this.active = true;
        this.stateTime = 0f;
        this.grazed = false;

        // Reset strategy when bullet is reused from pool
        this.movementPattern = null;
    }

    // =========================================
    // UPDATE
    // =========================================

    @Override
    public void update(float delta) {
        super.update(delta);

        if (!active || isDestroyed()) {
            return;
        }

        // Module 9: Strategy Movement
        if (movementPattern != null) {
            movementPattern.move(this, delta);
        } else {
            // Default linear movement
            x += vx * delta;
            y += vy * delta;
        }
    }

    // =========================================
    // RENDER
    // =========================================

    @Override
    public void render(SpriteBatch batch) {
        if (batch == null || !active || isDestroyed()) {
            return;
        }

        if (sprite != null) {
            batch.draw(
                sprite,
                x,
                y,
                width / 2f,
                height / 2f,
                width,
                height,
                1f,
                1f,
                angle - 90f
            );
        } else {
            super.render(batch);
        }
    }

    // =========================================
    // BULLET STRATEGY
    // =========================================

    public BulletMovementPattern getMovementPattern() {
        return movementPattern;
    }

    public void setMovementPattern(
        BulletMovementPattern movementPattern
    ) {
        this.movementPattern = movementPattern;
    }

    // =========================================
    // GETTERS
    // =========================================

    public BulletType getBulletType() {
        return bulletType;
    }

    public int getDamage() {
        return damage;
    }

    public boolean isPlayerBullet() {
        return isPlayerBullet;
    }

    public float getVx() {
        return vx;
    }

    public float getVy() {
        return vy;
    }

    public float getAngle() {
        return angle;
    }

    public boolean hasBeenGrazed() {
        return grazed;
    }


    public void setAngle(float angle) {
        this.angle = angle;

        float radians = (float) Math.toRadians(angle);

        vx = (float) Math.cos(radians) * speed;
        vy = (float) Math.sin(radians) * speed;
    }

    public void setGrazed(boolean grazed) {
        this.grazed = grazed;
    }
}
