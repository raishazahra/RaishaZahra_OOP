package com.raishazahra.frontend.objects.enemies;

import com.badlogic.gdx.graphics.Color;
import com.raishazahra.frontend.objects.Collidable;
import com.raishazahra.frontend.objects.Player;

public class Boss extends Enemy {
    public Boss(float x, float y) {
        this(x, y, "Stage 1 Boss", 100);
    }

    public Boss(float x, float y, String name, int hp) {
        super(x, y, 64, 64, Color.RED, name, hp, 1000L);
    }

    @Override
    public void onCollision(Collidable other) {
        if (other instanceof Player) System.out.println("Player touches boss");
    }
}
