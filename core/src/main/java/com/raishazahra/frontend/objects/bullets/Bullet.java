
package com.raishazahra.frontend.objects.bullets;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

import com.raishazahra.frontend.objects.BulletType;
import com.raishazahra.frontend.objects.Collidable;
import com.raishazahra.frontend.objects.GameObject;
import com.raishazahra.frontend.objects.Player;
import com.raishazahra.frontend.objects.enemies.Enemy;

public class Bullet extends GameObject {

    private BulletType bulletType;
    private int damage;

    public Bullet(
        float x, float y, float speed,
        BulletType bulletType, int damage
    ) {
        super(x, y, 16, 16, speed, Color.YELLOW);
        this.bulletType = bulletType;
        this.damage = damage;
    }

    public BulletType getBulletType() {
        return bulletType;
    }

    public int getDamage() {
        return damage;
    }

    @Override
    public void update(float delta) {
        super.update(delta);
        y += speed * delta;
    }

    @Override
    public void render(ShapeRenderer shapeRenderer) {
        shapeRenderer.setColor(Color.YELLOW);
        shapeRenderer.rect(x, y, width, height);
    }

    @Override
    public void onCollision(Collidable other) {
        if (isDestroyed()) return;

        if (other instanceof Enemy enemy && enemy.isAlive()) {
            boolean killed = enemy.takeDamage(damage);

            if (killed) {
                System.out.println(enemy.getName() + " was defeated!");
            }

            destroy();
        } else if (other instanceof Player player) {
            player.takeDamage(damage);
            destroy();
        }
    }
}
