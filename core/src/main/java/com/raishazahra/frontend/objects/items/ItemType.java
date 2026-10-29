package com.raishazahra.frontend.objects.items;

public enum ItemType {

    POWER(100L, 1),
    POINT(1000L, 0),
    BOMB(500L, 0),
    LIFE(2000L, 0);

    private final long scoreValue;
    private final int powerBonus;

    ItemType(long scoreValue, int powerBonus) {
        this.scoreValue = scoreValue;
        this.powerBonus = powerBonus;
    }

    public long getScoreValue() {
        return scoreValue;
    }

    public int getPowerBonus() {
        return powerBonus;
    }
}
