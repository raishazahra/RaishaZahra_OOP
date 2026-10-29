package com.raishazahra.frontend.objects.items;

import com.badlogic.gdx.graphics.Color;
import com.raishazahra.frontend.objects.Collidable;
import com.raishazahra.frontend.objects.GameObject;
import com.raishazahra.frontend.objects.Player;

public class Item extends GameObject {

    private String itemType;
    private long scoreValue;
    private ItemType itemTypeEnum;

    public Item(float x, float y, String itemType) {
        super(x, y, 16, 16, 100f, Color.WHITE);
        this.itemType = itemType;
        this.scoreValue = 1000L;
        this.itemTypeEnum = null;
    }

    public Item(
        float x,
        float y,
        float width,
        float height,
        float speed,
        String itemType,
        long scoreValue
    ) {
        super(x, y, width, height, speed, Color.WHITE);
        this.itemType = itemType;
        this.scoreValue = scoreValue;
        this.itemTypeEnum = null;
    }

    public Item(float x, float y, float speed) {
        super(x, y, 16, 16, speed, Color.WHITE);
        this.itemType = "Point Item";
        this.scoreValue = 1000L;
        this.itemTypeEnum = null;
    }

    public Item(float x, float y, ItemType itemTypeEnum) {
        super(x, y, 16, 16, 100f, Color.WHITE);
        this.itemTypeEnum = itemTypeEnum;
        this.itemType = itemTypeEnum.name();
        this.scoreValue = itemTypeEnum.getScoreValue();
    }

    public Item(
        float x,
        float y,
        float width,
        float height,
        float speed,
        ItemType itemTypeEnum,
        long scoreValue
    ) {
        super(x, y, width, height, speed, Color.WHITE);
        this.itemTypeEnum = itemTypeEnum;
        this.itemType = itemTypeEnum.name();
        this.scoreValue = scoreValue;
    }

    @Override
    public void update(float delta) {
        this.y -= speed * delta;
    }

    @Override
    public void onCollision(Collidable other) {
        if (other instanceof Player) {
            // Item pickup is handled by Player.collectItem()
        }
    }

    public String getItemType() {
        return itemType;
    }

    public long getScoreValue() {
        return scoreValue;
    }

    public ItemType getItemTypeEnum() {
        return itemTypeEnum;
    }
}
