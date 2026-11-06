
package com.raishazahra.frontend.systems;

import com.raishazahra.frontend.objects.GameObject;
import com.raishazahra.frontend.objects.Player;
import com.raishazahra.frontend.objects.bullets.Bullet;
import com.raishazahra.frontend.objects.enemies.Enemy;
import com.raishazahra.frontend.objects.items.Item;
import com.raishazahra.frontend.objects.items.ItemType;

import java.util.ArrayList;
import java.util.List;

public class CollisionReferee {

    public CollisionReferee() {
    }

    public void resolveCollisions(
        Player player,
        List<GameObject> entities,
        BulletManager bulletManager
    ) {

        if (player == null || entities == null || bulletManager == null) {
            return;
        }

        List<GameObject> newDrops = new ArrayList<>();

        for (Bullet bullet : bulletManager.getActivePlayerBullets()) {

            if (bullet.isDestroyed()) {
                continue;
            }

            for (GameObject entity : entities) {

                if (entity instanceof Enemy enemy
                    && !enemy.isDestroyed()
                    && enemy.isAlive()) {

                    if (bullet.getCoreHitbox().overlaps(
                        enemy.getCoreHitbox()
                    )) {

                        boolean wasAlive = enemy.isAlive();

                        enemy.takeDamage(bullet.getDamage());

                        if (wasAlive && !enemy.isAlive()) {

                            // Add enemy score to player
                            player.addScore(enemy.getScoreValue());

                            // Create POWER item drop
                            Item powerDrop = EntityFactory.createItem(
                                enemy.getX(),
                                enemy.getY(),
                                ItemType.POWER
                            );

                            if (powerDrop != null) {
                                newDrops.add(powerDrop);
                            }
                        }

                        // Destroy bullet after hitting enemy
                        bullet.destroy();

                        break;
                    }
                }
            }
        }

        if (!player.isDestroyed()) {

            for (Bullet bullet : bulletManager.getActiveEnemyBullets()) {

                if (bullet.isDestroyed()) {
                    continue;
                }


                if (bullet.getCoreHitbox().overlaps(
                    player.getCoreHitbox()
                )) {

                    player.takeDamage(bullet.getDamage());

                    bullet.destroy();

                    continue;
                }


                if (!bullet.hasBeenGrazed()
                    && bullet.getCoreHitbox().overlaps(
                    player.getGrazeHitbox()
                )) {

                    bullet.setGrazed(true);

                    player.addGraze();

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
                    player.getCoreHitbox()
                ) || item.getCoreHitbox().overlaps(
                    player.getGrazeHitbox()
                )) {

                    player.collectItem(item);
                }
            }

            // Case B: Physical Enemy Contact
            else if (entity instanceof Enemy enemy) {

                if (!enemy.isDestroyed()
                    && player.getCoreHitbox().overlaps(
                    enemy.getCoreHitbox()
                )) {

                    player.takeDamage(10);
                }
            }
        }


        entities.addAll(newDrops);
    }
}
