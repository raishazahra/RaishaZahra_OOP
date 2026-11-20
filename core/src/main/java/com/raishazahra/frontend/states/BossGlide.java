package com.raishazahra.frontend.states;
import com.raishazahra.frontend.objects.GameObject;
import com.raishazahra.frontend.objects.enemies.Boss;
import com.raishazahra.frontend.objects.patterns.entityStrategy.TargetPointMovement;
import java.util.Random;
final class BossGlide {
    private static final float MIN_X = 32f;
    private static final float MAX_X = 352f;
    private static final float MIN_Y = 288f;
    private static final float MAX_Y = 384f;
    private static final float HOME_X = 192f;
    private static final float HOME_Y = 336f;
    private static final float X_MARGIN = 96f;
    private static final float Y_MARGIN = 48f;
    private BossGlide() {
    }
    static TargetPointMovement home(Boss boss, float seconds) {
        return decelerating(HOME_X, HOME_Y, seconds);
    }
    static TargetPointMovement random(
        Boss boss,
        float distance,
        float seconds,
        Random random
    ) {
        double angle = random.nextDouble() * Math.PI * 2;
        float cos = (float) Math.cos(angle);
        float sin = (float) Math.sin(angle);
        float x = boss.getX();
        float y = boss.getY();
        if ((x < MIN_X + X_MARGIN && cos < 0f)
            || (x > MAX_X - X_MARGIN && cos > 0f)) {
            cos = -cos;
        }
        if ((y < MIN_Y + Y_MARGIN && sin < 0f)
            || (y > MAX_Y - Y_MARGIN && sin > 0f)) {
            sin = -sin;
        }
        float targetX = clamp(x + cos * distance, MIN_X, MAX_X);
        float targetY = clamp(y + sin * distance, MIN_Y, MAX_Y);
        return decelerating(targetX, targetY, seconds);
    }
    private static TargetPointMovement decelerating(
        float targetX,
        float targetY,
        float seconds
    ) {
        float duration = Math.max(0.05f, seconds);
        return new TargetPointMovement(targetX, targetY, 0f) {
            private boolean initialized = false;
            private float startX;
            private float startY;
            private float elapsed = 0f;
            @Override
            public void move(GameObject entity, float delta) {
                if (entity == null) return;
                if (!initialized) {
                    startX = entity.getX();
                    startY = entity.getY();
                    initialized = true;
                }
                elapsed += Math.max(0f, delta);
                float u = Math.min(1f, elapsed / duration);
                if (u >= 1f) {
                    entity.setX(targetX);
                    entity.setY(targetY);
                } else {
                    float remaining = 1f - u;
                    float eased = 1f - remaining * remaining;
                    entity.setX(
                        startX + (targetX - startX) * eased
                    );
                    entity.setY(
                        startY + (targetY - startY) * eased
                    );
                }
            }
        };
    }
    private static float clamp(
        float value,
        float min,
        float max
    ) {
        return Math.max(min, Math.min(max, value));
    }
}
