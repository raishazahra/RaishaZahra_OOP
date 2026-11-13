package com.raishazahra.frontend.objects.enemies;
import com.badlogic.gdx.graphics.Color;
import com.raishazahra.frontend.objects.EntityShooter;
import com.raishazahra.frontend.objects.Player;
import com.raishazahra.frontend.objects.patterns.ShootingPattern;
import com.raishazahra.frontend.objects.patterns.entityStrategy.EntityMovementPattern;
public class Enemy extends EntityShooter {
    private String name;
    private int hp;
    private int maxHp;
    protected long scoreValue;
    public Enemy(String name, int hp) {
        this(200, 380, 24, 24, Color.PINK, name, hp, 100L, null, null);
    }
    public Enemy(
        float x, float y, float width, float height,
        Color color, String name, int hp, long scoreValue
    ) {
        this(x, y, width, height, color, name, hp, scoreValue, null, null);
    }
    public Enemy(
        float x, float y, float width, float height,
        Color color, String name, int hp, long scoreValue,
        ShootingPattern shootingPattern,
        EntityMovementPattern movementPattern
    ) {
        super(x, y, width, height, 0f, color,
            shootingPattern, movementPattern);
        this.name = name;
        this.hp = Math.max(0, hp);
        this.maxHp = Math.max(0, hp);
        this.scoreValue = scoreValue;
    }
    public boolean takeDamage(int damage) {
        if (hp <= 0 || damage <= 0) return false;
        int oldHp = hp;
        hp = Math.max(0, hp - damage);
        return oldHp > 0 && hp == 0;
    }
    public boolean isAlive() {
        return hp > 0 && active;
    }
    public void attack(Player player) {
        if (player != null && isAlive()) {
            player.takeDamage(10);
        }
    }
    public String getName() { return name; }
    public int getHp() { return hp; }
    public int getMaxHp() { return maxHp; }
    public long getScoreValue() { return scoreValue; }
    public void setName(String name) { this.name = name; }
    public void setHp(int hp) { this.hp = Math.max(0, hp); }
    public void setScoreValue(long scoreValue) {
        this.scoreValue = scoreValue;
    }
}
