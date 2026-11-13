package com.raishazahra.frontend.objects.patterns.bulletStrategy;
import com.raishazahra.frontend.objects.GameObject;
import com.raishazahra.frontend.objects.bullets.Bullet;
import com.raishazahra.frontend.objects.enemies.Enemy;
import java.util.List;
    public class FantasySealMovement implements BulletMovementPattern {
        private GameObject target;
        private List<GameObject> entities;
        private final float homingSpeed;
        private final float turnSpeed;
        private final float driftDuration;
        private float timer;
        public FantasySealMovement(
            GameObject target,
            List<GameObject> entities,
            float homingSpeed,
            float turnSpeed,
            float driftDuration
        ) {
            this.target = target;
            this.entities = entities;
            this.homingSpeed = homingSpeed;
            this.turnSpeed = turnSpeed;
            this.driftDuration = driftDuration;
        }
        public FantasySealMovement(
            GameObject target,
            List<GameObject> entities
        ) {
            this(target, entities, 400f, 300f, 0.4f);
        }
        public FantasySealMovement(GameObject target) {
            this(target, null);
        }
        @Override
        public void move(Bullet bullet, float delta) {
            if (bullet == null || bullet.isDestroyed()) return;
            timer += delta;
            if (timer < driftDuration) {
                bullet.setX(bullet.getX() + bullet.getVx() * delta);
                bullet.setY(bullet.getY() + bullet.getVy() * delta);
                return;
            }
            if ((target == null || target.isDestroyed()) && entities != null) {
                float closestDistance = Float.MAX_VALUE;
                for (GameObject entity : entities) {
                    if (entity instanceof Enemy && !entity.isDestroyed()) {
                        float dx = entity.getX() - bullet.getX();
                        float dy = entity.getY() - bullet.getY();
                        float distance = dx * dx + dy * dy;
                        if (distance < closestDistance) {
                            closestDistance = distance;
                            target = entity;
                        }
                    }
                }
            }
     }   }
