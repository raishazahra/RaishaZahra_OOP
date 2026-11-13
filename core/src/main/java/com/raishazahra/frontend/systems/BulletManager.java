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
    public Bullet spawnPlayerBullet(
        float x, float y, float vx, float vy, int damage
    ) {
        return spawnPlayerBullet(x, y, vx, vy, damage, "bullet_amulet");
    }
    public Bullet spawnPlayerBullet(
        float x, float y, float vx, float vy,
        int damage, String spriteKey
    ) {
        Bullet bullet = playerBulletPool.poll();
        if (bullet == null) {
            bullet = new Bullet(
                x, y, 400f, BulletType.AMULET, damage
            );
        }
        bullet.init(
            x, y, vx, vy, BulletType.AMULET, damage, true
        );
        setSprite(bullet, spriteKey);
        activePlayerBullets.add(bullet);
        return bullet;
    }
    public Bullet spawnEnemyBullet(
        float x, float y, float vx, float vy, int damage
    ) {
        return spawnEnemyBullet(
            x, y, vx, vy, damage, "bullet_danmaku"
        );
    }
    public Bullet spawnEnemyBullet(
        float x, float y, float vx, float vy,
        int damage, String spriteKey
    ) {
        Bullet bullet = enemyBulletPool.poll();
        if (bullet == null) {
            bullet = new Bullet(
                x, y, 400f, BulletType.DANMAKU, damage
            );
        }
        bullet.init(
            x, y, vx, vy, BulletType.DANMAKU, damage, false
        );
        setSprite(bullet, spriteKey);
        activeEnemyBullets.add(bullet);
        return bullet;
    }
    private void setSprite(Bullet bullet, String spriteKey) {
        if (bullet == null || spriteKey == null) return;
        TextureRegion region =
            AssetManager.getInstance().getTextureRegion(spriteKey);
        if (region != null) {
            bullet.setSprite(region);
        }
    }}
