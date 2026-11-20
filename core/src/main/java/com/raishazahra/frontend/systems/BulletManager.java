package com.raishazahra.frontend.systems;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.raishazahra.frontend.objects.BulletType;
import com.raishazahra.frontend.objects.GameObject;
import com.raishazahra.frontend.objects.bullets.Bullet;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class BulletManager {

    private final Queue<Bullet> playerBulletPool = new LinkedList<>();
    private final Queue<Bullet> enemyBulletPool = new LinkedList<>();

    private final List<Bullet> activePlayerBullets = new ArrayList<>();
    private final List<Bullet> activeEnemyBullets = new ArrayList<>();

    // ==============================
    // PLAYER BULLET
    // ==============================

    public Bullet spawnPlayerBullet(
        float x, float y, float vx, float vy, int damage) {

        return spawnPlayerBullet(
            x, y, vx, vy, damage, "bullet_amulet"
        );
    }

    public Bullet spawnPlayerBullet(
        float x, float y, float vx, float vy,
        int damage, String spriteKey) {

        Bullet bullet = playerBulletPool.poll();

        if (bullet == null) {
            bullet = new Bullet(
                x, y, 400f, BulletType.AMULET, damage
            );
        }

        bullet.init(
            x, y, vx, vy,
            BulletType.AMULET, damage, true
        );

        setSprite(bullet, spriteKey);

        activePlayerBullets.add(bullet);

        return bullet;
    }

    // ==============================
    // ENEMY BULLET
    // ==============================

    public Bullet spawnEnemyBullet(
        float x, float y, float vx, float vy, int damage) {

        return spawnEnemyBullet(
            x, y, vx, vy, damage, "bullet_danmaku"
        );
    }

    public Bullet spawnEnemyBullet(
        float x, float y, float vx, float vy,
        int damage, String spriteKey) {

        Bullet bullet = enemyBulletPool.poll();

        if (bullet == null) {
            bullet = new Bullet(
                x, y, 400f, BulletType.DANMAKU, damage
            );
        }

        bullet.init(
            x, y, vx, vy,
            BulletType.DANMAKU, damage, false
        );

        setSprite(bullet, spriteKey);

        activeEnemyBullets.add(bullet);

        return bullet;
    }

    // ==============================
    // SPRITE
    // ==============================

    private void setSprite(Bullet bullet, String spriteKey) {

        if (bullet == null || spriteKey == null) return;

        TextureRegion region =
            AssetManager.getInstance()
                .getTextureRegion(spriteKey);

        if (region != null) {
            bullet.setSprite(region);
        }
    }

    // ==============================
    // UPDATE
    // ==============================

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

    public void update(
        float delta,
        float screenWidth,
        float screenHeight) {

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

    private void updateBulletList(
        List<Bullet> bullets,
        Queue<Bullet> pool,
        float delta,
        float screenWidth,
        float screenHeight) {

        Iterator<Bullet> iterator = bullets.iterator();

        while (iterator.hasNext()) {

            Bullet bullet = iterator.next();

            if (!bullet.isDestroyed()) {
                bullet.update(delta);
            }

            boolean offScreen = screenWidth > 0
                && screenHeight > 0
                && (
                bullet.getX() + bullet.getWidth() < 0
                    || bullet.getX() > screenWidth
                    || bullet.getY() + bullet.getHeight() < 0
                    || bullet.getY() > screenHeight
            );

            if (bullet.isDestroyed() || offScreen) {

                bullet.destroy();

                iterator.remove();

                pool.offer(bullet);
            }
        }
    }

    // ==============================
    // CLEAR BULLETS
    // ==============================

    public void clearEnemyBullets() {
        recycleAll(activeEnemyBullets, enemyBulletPool);
    }

    public void clearEnemyBullets(List<GameObject> entities) {
        clearEnemyBullets();
    }

    public void clearPlayerBullets() {
        recycleAll(activePlayerBullets, playerBulletPool);
    }

    private void recycleAll(
        List<Bullet> bullets,
        Queue<Bullet> pool) {

        Iterator<Bullet> iterator = bullets.iterator();

        while (iterator.hasNext()) {

            Bullet bullet = iterator.next();

            bullet.destroy();

            iterator.remove();

            pool.offer(bullet);
        }
    }

    // ==============================
    // RENDER
    // ==============================

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

    // ==============================
    // GETTERS
    // ==============================

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
