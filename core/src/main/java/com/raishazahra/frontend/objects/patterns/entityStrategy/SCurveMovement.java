package com.raishazahra.frontend.objects.patterns.entityStrategy;
import com.raishazahra.frontend.objects.GameObject;
public class SCurveMovement implements EntityMovementPattern {
    private final float speed;
    private float angle;
    private float timer;
    private final boolean mirrored;
    public SCurveMovement(float speed, float initialAngle, boolean mirrored) {
        this.speed = speed;
        this.angle = initialAngle;
        this.mirrored = mirrored;
        this.timer = 0f;
    }
    @Override
    public void move(GameObject entity, float delta) {
        if (entity == null || entity.isDestroyed()) return;
        timer += delta;
        float angularVelocity;
        if (timer < 0.67f) {
            angularVelocity = 0f;
        } else if (timer < 2f) {
            angularVelocity = mirrored ? -1.4726f : 1.4726f;
        } else if (timer < 3.67f) {
            angularVelocity = mirrored ? 1.1781f : -1.1781f;
        } else {
            angularVelocity = 0f;
        }
        angle += angularVelocity * delta;
        entity.setX(
            entity.getX() + (float) Math.cos(angle) * speed * delta
        );
        entity.setY(
            entity.getY() + (float) Math.sin(angle) * speed * delta
        );
    }
}
