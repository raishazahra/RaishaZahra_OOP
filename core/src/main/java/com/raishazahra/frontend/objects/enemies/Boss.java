
package com.raishazahra.frontend.objects.enemies;

import com.badlogic.gdx.graphics.Color;
import com.raishazahra.frontend.objects.Collidable;
import com.raishazahra.frontend.objects.Player;
import com.raishazahra.frontend.objects.patterns.ShootingPattern;
import com.raishazahra.frontend.objects.patterns.shootingStrategy.SpreadShot;
import com.raishazahra.frontend.systems.BulletManager;

public class Boss extends Enemy {

    private ShootingPattern shootingPattern;

    public Boss(float x, float y) {
        this(x, y, "Rumia", 100);
    }

    public Boss(float x, float y, String name, int hp) {
        super(x, y, 64, 64, Color.RED, name, hp, 1000L);
        this.shootingPattern = new SpreadShot(200f, 3, 30f);
    }

    public void shootBullet(BulletManager bulletManager) {
        if (bulletManager == null || !isAlive()) {
            return;
        }

        if (shootingPattern != null) {
            shootingPattern.execute(
                x + width / 2f,
                y,
                bulletManager,
                false
            );
        }
    }

    public ShootingPattern getShootingPattern() {
        return shootingPattern;
    }

    public void setShootingPattern(ShootingPattern shootingPattern) {
        this.shootingPattern = shootingPattern;
    }

    @Override
    public void onCollision(Collidable other) {
        if (other instanceof Player) {
            System.out.println("Player touches boss");
        }
    }
}
