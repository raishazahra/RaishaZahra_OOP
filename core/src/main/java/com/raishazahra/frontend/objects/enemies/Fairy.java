package com.raishazahra.frontend.objects.enemies;
import com.badlogic.gdx.graphics.Color;
import com.raishazahra.frontend.objects.Collidable;
import com.raishazahra.frontend.objects.Player;
import com.raishazahra.frontend.objects.patterns.entityStrategy.LinearEntityMovement;
import com.raishazahra.frontend.objects.patterns.shootingStrategy.FrontShot;
public class Fairy extends Enemy {
    private float shootTimer = 0f;
    private float shootCadence = 2f;
    public Fairy(String name, int hp) {
        this(150, 380, name, hp);
    }
    public Fairy(float x, float y) {
        this(x, y, "Stage 1 Fairy", 20);
    }
    public Fairy(float x, float y, String name, int hp) {
        super(
            x, y, 32, 32, Color.PINK,
            name, hp, 500L,
            new FrontShot(180f, 1, 10),
            new LinearEntityMovement(0f, -20f)
        );
    }
    @Override
    public void update(float delta) {
        super.update(delta);
        shootTimer += delta;
    }
    public boolean canShoot() {
        if (shootTimer >= shootCadence) {
            shootTimer = 0f;
            return true;
        }
        return false;
    }
    public float getShootCadence() {
        return shootCadence;
    }
    public void setShootCadence(float cadence) {
        shootCadence = Math.max(0.1f, cadence);
    }
    @Override
    public void onCollision(Collidable other) {
        if (other instanceof Player) {
            System.out.println("Player touches fairy");
        }
    }
}
