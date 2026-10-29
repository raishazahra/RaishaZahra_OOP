package com.raishazahra.frontend.objects.enemies;

import com.badlogic.gdx.graphics.Color;
import com.raishazahra.frontend.objects.GameObject;
import com.raishazahra.frontend.objects.Player;

public class Enemy extends GameObject {

    private String name;
    private int hp;
    private int maxHp;

    protected long scoreValue;

    public Enemy(String name, int hp) {
        super(
            200,
            380,
            24,
            24,
            0,
            Color.PINK
        );

        this.name = name;
        this.hp = Math.max(0, hp);
        this.maxHp = Math.max(0, hp);
        this.scoreValue = 100L;
    }

    public Enemy(
        float x,
        float y,
        float width,
        float height,
        Color color,
        String name,
        int hp,
        long scoreValue
    ) {
        super(
            x,
            y,
            width,
            height,
            0,
            color
        );

        this.name = name;
        this.hp = Math.max(0, hp);
        this.maxHp = Math.max(0, hp);
        this.scoreValue = scoreValue;
    }

    public boolean takeDamage(int damage) {

        if (getHp() <= 0) {
            return false;
        }

        int oldHp = getHp();

        if (damage > 0) {
            setHp(getHp() - damage);
        }

        return oldHp > 0 && getHp() == 0;
    }

    public boolean isAlive() {
        return hp > 0;
    }

    public void attack(Player player) {
        if (player != null) {
            player.takeDamage(10);
        }
    }

    public String getName() {
        return name;
    }

    public int getHp() {
        return hp;
    }

    public int getMaxHp() {
        return maxHp;
    }

    public long getScoreValue() {
        return scoreValue;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setHp(int hp) {
        this.hp = Math.max(0, hp);
    }

    public void setScoreValue(long scoreValue) {
        this.scoreValue = scoreValue;
    }
}
