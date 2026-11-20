package com.raishazahra.frontend.systems;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import com.raishazahra.frontend.objects.BulletType;
import com.raishazahra.frontend.objects.Player;
import com.raishazahra.frontend.objects.bullets.Bullet;
import com.raishazahra.frontend.objects.enemies.Boss;
import com.raishazahra.frontend.objects.enemies.Fairy;
import com.raishazahra.frontend.objects.items.Item;
import com.raishazahra.frontend.objects.items.ItemType;

public class EntityFactory {

    // =========================
    // PLAYER
    // =========================

    public static Player createPlayer(
        float x,
        float y,
        String name,
        int hp,
        int power,
        int spellCards
    ) {
        Player player = new Player(
            x,
            y,
            name,
            hp,
            power,
            spellCards
        );

        Animation<TextureRegion> animation =
            AssetManager.getInstance().getAnimation("player_idle");

        player.setAnimation(animation);

        return player;
    }

    // =========================
    // BOSS
    // =========================

    public static Boss createBoss(
        float x,
        float y,
        String name,
        int hp
    ) {
        Boss boss = new Boss(
            x,
            y,
            name,
            hp
        );

        Animation<TextureRegion> animation =
            AssetManager.getInstance().getAnimation("boss_idle");

        boss.setAnimation(animation);

        return boss;
    }

    // =========================
    // FAIRY - DEFAULT ANIMATION
    // =========================

    public static Fairy createFairy(
        float x,
        float y,
        String name,
        int hp
    ) {
        Fairy fairy = new Fairy(
            x,
            y,
            name,
            hp
        );

        Animation<TextureRegion> animation =
            AssetManager.getInstance().getAnimation("fairy_idle_red");

        fairy.setAnimation(animation);

        return fairy;
    }

    // =========================
    // FAIRY - CUSTOM ANIMATION
    // =========================

    public static Fairy createFairy(
        float x,
        float y,
        String name,
        int hp,
        String animationKey
    ) {
        Fairy fairy = new Fairy(
            x,
            y,
            name,
            hp
        );

        Animation<TextureRegion> animation =
            AssetManager.getInstance().getAnimation(animationKey);

        fairy.setAnimation(animation);

        return fairy;
    }

    // =========================
    // ITEM
    // =========================

    public static Item createItem(
        float x,
        float y,
        ItemType itemType
    ) {
        Item item = new Item(
            x,
            y,
            itemType
        );

        String spriteKey;

        switch (itemType) {
            case POWER:
                spriteKey = "item_power";
                break;

            case POINT:
                spriteKey = "item_point";
                break;

            case BOMB:
                spriteKey = "item_bomb";
                break;

            case LIFE:
                spriteKey = "item_life";
                break;

            default:
                spriteKey = "item_point";
                break;
        }

        TextureRegion sprite =
            AssetManager.getInstance().getTextureRegion(spriteKey);

        item.setSprite(sprite);

        return item;
    }

    // =========================
    // ENEMY BULLET
    // =========================

    public static Bullet createEnemyBullet(
        float x,
        float y,
        int damage
    ) {
        Bullet bullet = new Bullet(
            x,
            y,
            0f,
            BulletType.DANMAKU,
            damage
        );

        TextureRegion sprite =
            AssetManager.getInstance().getTextureRegion("bullet_danmaku");

        bullet.setSprite(sprite);

        return bullet;
    }

    // =========================
    // PLAYER BULLET
    // =========================

    public static Bullet createPlayerBullet(
        float x,
        float y,
        int damage,
        String spriteKey
    ) {
        Bullet bullet = new Bullet(
            x,
            y,
            BulletType.AMULET,
            damage
        );

        TextureRegion sprite =
            AssetManager.getInstance().getTextureRegion(spriteKey);

        bullet.setSprite(sprite);

        return bullet;
    }

    // =========================
    // PLAYER BULLET - DEFAULT
    // =========================

    public static Bullet createPlayerBullet(
        float x,
        float y,
        int damage
    ) {
        return createPlayerBullet(
            x,
            y,
            damage,
            "bullet_amulet"
        );
    }
}
