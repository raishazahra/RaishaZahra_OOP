package com.raishazahra.frontend.objects.enemies;
import com.badlogic.gdx.graphics.Color;
import com.raishazahra.frontend.objects.Collidable;
import com.raishazahra.frontend.objects.Player;
import com.raishazahra.frontend.objects.patterns.ShootingPattern;
import com.raishazahra.frontend.objects.patterns.entityStrategy.EntityMovementPattern;
import com.raishazahra.frontend.objects.patterns.shootingStrategy.SpreadShot;
public class Boss extends Enemy {
    public Boss(float x, float y) {
        this(x, y, "Rumia", 100);
    }
    public Boss(String name, int hp) {
        this(176, 420, name, hp);
    }
    public Boss(float x, float y, String name, int hp) {
        this(
            x, y, name, hp,
            new SpreadShot(200f, 3, 30f),
            null
        );
    }
    public Boss(
        float x, float y, String name, int hp,
        ShootingPattern shootingPattern,
        EntityMovementPattern movementPattern
    ) {
        super(
            x, y, 64, 64, Color.RED,
            name, hp, 1000L,
            shootingPattern, movementPattern
        );
    }
    @Override
    public void onCollision(Collidable other) {
        if (other instanceof Player) {
            System.out.println("Player touches boss");
        }
    }
}
