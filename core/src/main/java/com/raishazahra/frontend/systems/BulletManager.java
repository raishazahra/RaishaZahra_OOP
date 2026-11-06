
package com.raishazahra.frontend.systems;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import com.raishazahra.frontend.objects.BulletType;
import com.raishazahra.frontend.objects.GameObject;
import com.raishazahra.frontend.objects.bullets.Bullet;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class BulletManager {

    private final Queue<Bullet> playerBulletPool;
    private final Queue<Bullet> enemyBulletPool;

    private final List<Bullet> activePlayerBullets;
    private final List<Bullet> activeEnemyBullets;

    public BulletManager() {
        playerBulletPool = new LinkedList<>();
        enemyBulletPool = new LinkedList<>();

        activePlayerBullets = new ArrayList<>();
        activeEnemyBullets = new ArrayList<>();
    }

    // Spawn Player Bullet
    public Bullet spawnPlayerBullet(
        float x,
        float y,
        float vx,
        float vy,
        int damage
    ) {
        Bullet bullet = playerBulletPool.poll();

        if (bullet == null) {
            bullet = new Bullet(
                x,
                y,
                400f,
                BulletType.AMULET,
                damage
            );
        }

        bullet.init(
            x,
            y,
            vx,
            vy,
            BulletType.AMULET,
            damage,
            true
        );

        activePlayerBullets.add(bullet);

        return bullet;
    }

    // Spawn Enemy Bullet
    public Bullet spawnEnemyBullet(
        float x,
        float y,
        float vx,
        float vy,
        int damage
    ) {
        Bullet bullet = enemyBulletPool.poll();

        if (bullet == null) {
            bullet = new Bullet(
                x,
                y,
                400f,
                BulletType.DANMAKU,
                damage
            );
        }

        bullet.init(
            x,
            y,
            vx,
            vy,
            BulletType.DANMAKU,
            damage,
            false
        );

        activeEnemyBullets.add(bullet);

        return bullet;
    }

    // Update without screen boundary checking
    public void update(float delta) {
        updateBulletList(
            activePlayerBullets,
            playerBulletPool,
            delta,
            -1,
            -1
        );

        updateBulletList(
            activeEnemyBullets,
            enemyBulletPool,
            delta,
            -1,
            -1
        );
    }

    // Update with screen boundary checking
    public void update(
        float delta,
        float screenWidth,
        float screenHeight
    ) {
        updateBulletList(
            activePlayerBullets,
            playerBulletPool,
            delta,
            screenWidth,
            screenHeight
        );

        updateBulletList(
            activeEnemyBullets,
            enemyBulletPool,
            delta,
            screenWidth,
            screenHeight
        );
    }

    // Update and recycle bullets safely using Iterator
    private void updateBulletList(
        List<Bullet> activeBullets,
        Queue<Bullet> pool,
        float delta,
        float screenWidth,
        float screenHeight
    ) {
        Iterator<Bullet> iterator = activeBullets.iterator();

        while (iterator.hasNext()) {
            Bullet bullet = iterator.next();

            if (!bullet.isDestroyed()) {
                bullet.update(delta);
            }

            boolean offScreen = false;

            if (screenWidth > 0 && screenHeight > 0) {
                offScreen =
                    bullet.getX() + bullet.getWidth() < 0 ||
                        bullet.getX() > screenWidth ||
                        bullet.getY() + bullet.getHeight() < 0 ||
                        bullet.getY() > screenHeight;
            }

            if (bullet.isDestroyed() || offScreen) {
                bullet.destroy();

                iterator.remove();
                pool.offer(bullet);
            }
        }
    }

    // Clear all enemy bullets
    public void clearEnemyBullets() {
        Iterator<Bullet> iterator = activeEnemyBullets.iterator();

        while (iterator.hasNext()) {
            Bullet bullet = iterator.next();

            bullet.destroy();

            iterator.remove();
            enemyBulletPool.offer(bullet);
        }
    }

    // Compatible with Player.useBomb()
    public void clearEnemyBullets(List<GameObject> entities) {
        clearEnemyBullets();
    }

    // Clear all player bullets
    public void clearPlayerBullets() {
        Iterator<Bullet> iterator = activePlayerBullets.iterator();

        while (iterator.hasNext()) {
            Bullet bullet = iterator.next();

            bullet.destroy();

            iterator.remove();
            playerBulletPool.offer(bullet);
        }
    }

    // Render active bullets
    public void render(SpriteBatch batch) {
        if (batch == null) return;

        for (Bullet bullet : activePlayerBullets) {
            if (!bullet.isDestroyed()) {
                bullet.render(batch);
            }
        }

        for (Bullet bullet : activeEnemyBullets) {
            if (!bullet.isDestroyed()) {
                bullet.render(batch);
            }
        }
    }

    public List<Bullet> getActivePlayerBullets() {
        return activePlayerBullets;
    }

    public List<Bullet> getActiveEnemyBullets() {
        return activeEnemyBullets;
    }

    public int getPlayerPoolSize() {
        return playerBulletPool.size();
    }

    public int getEnemyPoolSize() {
        return enemyBulletPool.size();
    }
}
