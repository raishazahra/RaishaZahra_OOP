package com.raishazahra.frontend.systems;

import com.badlogic.gdx.math.Rectangle;
import com.raishazahra.frontend.objects.GameObject;
import com.raishazahra.frontend.objects.Player;
import com.raishazahra.frontend.objects.bullets.Bullet;
import com.raishazahra.frontend.objects.enemies.Enemy;
import com.raishazahra.frontend.objects.items.Item;

import java.util.ArrayList;
import java.util.List;

public class CollisionReferee {

    public void resolveCollisions(
        Player player,
        List<GameObject> entities,
        BulletManager bulletManager) {

        if (player == null || entities == null || bulletManager == null) {
            return;
        }

        List<GameObject> newDrops = new ArrayList<>();



        for (Bullet bullet : bulletManager.getActivePlayerBullets()) {

            if (bullet.isDestroyed()) {
                continue;
            }

            for (GameObject entity : entities) {

                if (entity instanceof Enemy enemy && !enemy.isDestroyed()) {

                    if (bullet.getCoreHitbox().overlaps(
                        enemy.getCoreHitbox())) {

                        boolean defeated =
                            enemy.takeDamage(bullet.getDamage());

                        if (defeated) {
                            player.addScore(enemy.getScoreValue());

                            Item powerDrop = EntityFactory.createPowerItem(
                                enemy.getX(),
                                enemy.getY()
                            );

                            newDrops.add(powerDrop);
                        }

                        bullet.destroy();
                        break;
                    }
                }
            }
        }



        for (Bullet bullet : bulletManager.getActiveEnemyBullets()) {

            if (bullet.isDestroyed()) {
                continue;
            }

            if (!player.isDestroyed()) {

                Rectangle bulletHitbox = bullet.getCoreHitbox();
                Rectangle playerCore = player.getCoreHitbox();
                Rectangle playerGraze = player.getGrazeHitbox();



                if (bulletHitbox.overlaps(playerCore)) {

                    player.takeDamage(bullet.getDamage());
                    bullet.destroy();



                } else if (!bullet.hasBeenGrazed()
                    && bulletHitbox.overlaps(playerGraze)) {

                    bullet.setGrazed(true);
                    player.addScore(50);

                    System.out.println(
                        "[CollisionReferee] GRAZE! "
                            + player.getName()
                            + " grazed an enemy bullet (+50 pts)!"
                    );
                }
            }
        }



        for (GameObject entity : entities) {

            if (entity.isDestroyed()) {
                continue;
            }

            // Case A: Item Collection

            if (entity instanceof Item item) {

                if (item.getCoreHitbox().overlaps(
                    player.getCoreHitbox())
                    || item.getCoreHitbox().overlaps(
                    player.getGrazeHitbox())) {

                    player.collectItem(item);
                    item.destroy();
                }
            }



            else if (entity instanceof Enemy enemy) {

                if (player.getCoreHitbox().overlaps(
                    enemy.getCoreHitbox())) {

                    player.takeDamage(10);
                }
            }
        }

        entities.addAll(newDrops);
    }
}
