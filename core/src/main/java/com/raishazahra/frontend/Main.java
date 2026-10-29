
package com.raishazahra.frontend;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.raishazahra.frontend.objects.GameObject;
import com.raishazahra.frontend.objects.Player;
import com.raishazahra.frontend.objects.bullets.Bullet;
import com.raishazahra.frontend.objects.enemies.Boss;
import com.raishazahra.frontend.objects.enemies.Enemy;
import com.raishazahra.frontend.objects.enemies.Fairy;
import com.raishazahra.frontend.objects.items.Item;
import com.raishazahra.frontend.objects.items.ItemType;
import com.raishazahra.frontend.systems.AssetManager;
import com.raishazahra.frontend.systems.AudioSystem;
import com.raishazahra.frontend.systems.EntityFactory;

public class Main extends ApplicationAdapter {

    private SpriteBatch batch;

    private Player player;
    private Boss boss;
    private Item powerItem;
    private Item pointItem;

    private List<Fairy> fairy;
    private List<GameObject> entities;
    private List<Bullet> bullets;

    @Override
    public void create() {
        batch = new SpriteBatch();

        fairy = new ArrayList<>();
        entities = new ArrayList<>();
        bullets = new ArrayList<>();

        // Initialize graphics assets first.
        AssetManager.getInstance().init();

        // Initialize custom audio system.
        AudioSystem audio = AudioSystem.getInstance();

        audio.loadSound("shoot", "shoot.wav");
        audio.loadSound("enemy_die", "enemy_die.wav");
        audio.loadSound("item_collect", "item_collect.wav");

        audio.loadBgm("game_bgm.wav");
        audio.playBgm();

        // Create Player.
        player = EntityFactory.createPlayer(
            280, 40, "Reimu Hakurei", 100, 15, 3
        );

        // Create Fairies.
        Fairy redFairy = EntityFactory.createFairy(
            150, 380, "Red Fairy", 20
        );

        Fairy blueFairy = EntityFactory.createFairy(
            250, 380, "Blue Fairy", 20,
            "fairy_idle_blue"
        );

        fairy.add(redFairy);
        fairy.add(blueFairy);

        // Create Boss.
        boss = EntityFactory.createBoss(
            380, 400, "Rumia", 150
        );

        // Create Items.
        powerItem = EntityFactory.createItem(
            200, 450, ItemType.POWER
        );

        pointItem = EntityFactory.createItem(
            320, 480, ItemType.POINT
        );

        // Add entities to the scene.
        entities.add(player);
        entities.addAll(fairy);
        entities.add(boss);
        entities.add(powerItem);
        entities.add(pointItem);
    }

    @Override
    public void render() {
        float delta = Gdx.graphics.getDeltaTime();

        ScreenUtils.clear(0.1f, 0.1f, 0.15f, 1f);

        // Update entities.
        for (GameObject entity : entities) {
            if (!entity.isDestroyed()) {
                entity.update(delta);
            }
        }

        // Fire a bullet when Z is pressed.
        if (Gdx.input.isKeyJustPressed(Input.Keys.Z)
            && player.isAlive()) {
            Bullet bullet = player.shootBullet();
            bullets.add(bullet);
            entities.add(bullet);
        }

        // Remove bullets that leave the screen.
        for (Bullet bullet : bullets) {
            if (!bullet.isDestroyed()
                && bullet.getY() > Gdx.graphics.getHeight()) {
                bullet.destroy();
            }
        }

        // Player-item collision.
        for (GameObject entity : entities) {
            if (entity instanceof Item item
                && !item.isDestroyed()
                && player.getCoreHitbox().overlaps(
                item.getCoreHitbox()
            )) {
                player.onCollision(item);
            }
        }

        // Bullet-enemy collision.
        for (Bullet bullet : bullets) {
            if (bullet.isDestroyed()) {
                continue;
            }

            for (GameObject entity : entities) {
                if (entity instanceof Enemy enemy
                    && !enemy.isDestroyed()
                    && enemy.isAlive()
                    && bullet.getCoreHitbox().overlaps(
                    enemy.getCoreHitbox()
                )) {

                    bullet.onCollision(enemy);

                    if (!enemy.isAlive()) {
                        enemy.destroy();
                    }

                    break;
                }
            }
        }

        // Render all active objects.
        batch.begin();

        for (GameObject entity : entities) {
            if (!entity.isDestroyed()) {
                entity.render(batch);
            }
        }

        batch.end();

        // Remove destroyed bullets.
        Iterator<Bullet> iterator = bullets.iterator();

        while (iterator.hasNext()) {
            Bullet bullet = iterator.next();

            if (bullet.isDestroyed()) {
                entities.remove(bullet);
                iterator.remove();
            }
        }
    }

    @Override
    public void dispose() {
        if (batch != null) {
            batch.dispose();
        }

        AudioSystem.getInstance().dispose();
        AssetManager.getInstance().dispose();
    }
}
