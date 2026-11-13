package com.raishazahra.frontend.objects.patterns.entityStrategy;
import com.raishazahra.frontend.objects.GameObject;
public class TargetPointMovement implements EntityMovementPattern {
    private final float targetX;
    private final float targetY;
    private final float speed;
    public TargetPointMovement(float targetX, float targetY, float speed) {
        this.targetX = targetX;
        this.targetY = targetY;
        this.speed = speed;
    }
    @Override
    public void move(GameObject entity, float delta) {
        if (entity == null || entity.isDestroyed()) return;
        float dx = targetX - entity.getX();
        float dy = targetY - entity.getY();
        float distance = (float) Math.sqrt(dx * dx + dy * dy);
        float step = speed * delta;
        if (distance <= step || distance < 1f) {
            entity.setX(targetX);
            entity.setY(targetY);
            return;
        }
        entity.setX(entity.getX() + dx / distance * step);
        entity.setY(entity.getY() + dy / distance * step);
    }
    public boolean hasArrived(GameObject entity) {
        if (entity == null) return false;
        float dx = targetX - entity.getX();
        float dy = targetY - entity.getY();
        return Math.sqrt(dx * dx + dy * dy) < 1f;
    }
}
