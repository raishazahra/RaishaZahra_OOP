
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

    public static Player createPlayer(
        float x, float y, String name,
        int hp, int power, int spellCards
    ) {
        Player player = new Player(
            x, y, name, hp, power, spellCards
        );

        Animation<TextureRegion> animation =
            AssetManager.getInstance().getAnimation("player_idle");

        if (animation != null) {
            player.setAnimation(animation);
        }

        return player;
    }

    public static Boss createBoss(
        float x, float y, String name, int hp
    ) {
        Boss boss = new Boss(x, y, name, hp);

        Animation<TextureRegion> idleAnim =
            AssetManager.getInstance().getAnimation("boss_idle");

        if (idleAnim != null) {
            boss.setAnimation(idleAnim);
        }

        return boss;
    }

    public static Fairy createFairy(
        float x, float y, String name, int hp
    ) {
        return createFairy(
            x, y, name, hp, "fairy_idle_red"
        );
    }

    public static Fairy createFairy(
        float x, float y, String name,
        int hp, String keyString
    ) {
        Fairy fairy = new Fairy(x, y, name, hp);

        Animation<TextureRegion> idleAnim =
            AssetManager.getInstance().getAnimation(keyString);

        if (idleAnim != null) {
            fairy.setAnimation(idleAnim);
        }

        return fairy;
    }

    public static Item createItem(
        float x, float y, ItemType itemType
    ) {
        Item item = new Item(x, y, itemType);

        String key = switch (itemType) {
            case POWER -> "item_power";
            case POINT -> "item_point";
            case BOMB -> "item_bomb";
            case LIFE -> "item_life";
        };

        TextureRegion sprite =
            AssetManager.getInstance().getTextureRegion(key);

        if (sprite != null) {
            item.setSprite(sprite);
        }

        return item;
    }

    public static Bullet createEnemyBullet(
        float x, float y, int damage
    ) {
        Bullet bullet = new Bullet(
            x, y, 200f, BulletType.DANMAKU, damage
        );

        TextureRegion sprite =
            AssetManager.getInstance()
                .getTextureRegion("bullet_danmaku");

        if (sprite != null) {
            bullet.setSprite(sprite);
        }

        return bullet;
    }

    public static Bullet createPlayerBullet(
        float x, float y, int damage, String spriteKey
    ) {
        TextureRegion sprite =
            AssetManager.getInstance()
                .getTextureRegion(spriteKey);

        Bullet bullet = new Bullet(
            x, y, 400f, BulletType.AMULET, damage
        );

        if (sprite != null) {
            bullet.setSprite(sprite);
        }

        return bullet;
    }

    public static Bullet createPlayerBullet(
        float x, float y, int damage
    ) {
        return createPlayerBullet(
            x, y, damage, "bullet_amulet"
        );
    }
}
