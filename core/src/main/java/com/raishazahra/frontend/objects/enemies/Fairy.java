package com.raishazahra.frontend.objects.enemies;

import com.badlogic.gdx.graphics.Color;
import com.raishazahra.frontend.objects.Collidable;
import com.raishazahra.frontend.objects.Player;

public class Fairy extends Enemy {
    public Fairy(String name, int hp) {
        this(150, 380, name, hp);
    }

    public Fairy(float x, float y, String name, int hp) {
        super(x, y, 32, 32, Color.PINK, name, hp, 500L);
    }

    public Fairy(float x, float y) {
        this(x, y, "Stage 1 Fairy", 20);
    }

    @Override
    public void onCollision(Collidable other) {
        if (other instanceof Player) System.out.println("Player touches fairy");
    }
}
