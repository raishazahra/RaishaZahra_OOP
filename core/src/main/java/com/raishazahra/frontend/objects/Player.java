
package com.raishazahra.frontend.objects;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;

import com.raishazahra.frontend.objects.bullets.Bullet;
import com.raishazahra.frontend.objects.enemies.Enemy;
import com.raishazahra.frontend.objects.items.Item;
import com.raishazahra.frontend.objects.items.ItemType;
import com.raishazahra.frontend.objects.patterns.ShootingPattern;
import com.raishazahra.frontend.observers.GameObserver;
import com.raishazahra.frontend.systems.AssetManager;
import com.raishazahra.frontend.systems.AudioSystem;
import com.raishazahra.frontend.systems.BulletManager;
import com.raishazahra.frontend.systems.EntityFactory;

import java.util.ArrayList;
import java.util.List;

public class Player extends GameObject {

    private String name;
    private int hp;
    private int maxHp;
    private int lives = 3;
    private int power;
    private int spellCards;
    private long score;

    private int currentDir = 0;
    private int grazeCount = 0;

    private boolean focused = false;

    private float shootTimer = 0.1f;
    private float shootCooldown = 0.1f;
    private float invulnerableTimer = 0f;

    private ShootingPattern shootingPattern;

    private final List<GameObserver> observers = new ArrayList<>();

    public Player(String name, int hp, int power, int spellCards) {
        this(280, 40, name, hp, power, spellCards);
    }

    public Player(float x, float y) {
        this(x, y, "Reimu Hakurei", 100, 15, 3);
    }

    public Player(float x, float y, String name, int hp, int power, int spellCards) {
        super(x, y, 32, 48, 200f, Color.RED);

        this.name = name;
        this.hp = Math.max(0, hp);
        this.maxHp = Math.max(0, hp);
        this.power = power;
        this.spellCards = spellCards;
        this.score = 0;
    }

    @Override
    public void update(float delta) {
        super.update(delta);

        shootTimer += delta;

        if (invulnerableTimer > 0f) {
            invulnerableTimer -= delta;

            if (invulnerableTimer < 0f) {
                invulnerableTimer = 0f;
            }
        }
    }

    public void updateAnimationState(float dx) {
        AssetManager assets = AssetManager.getInstance();

        if (dx < 0) {
            if (currentDir != -1) {
                currentDir = -1;
                Animation<TextureRegion> anim =
                    assets.getAnimation("player_left");

                if (anim != null) setAnimation(anim);
            }
        } else if (dx > 0) {
            if (currentDir != 1) {
                currentDir = 1;
                Animation<TextureRegion> anim =
                    assets.getAnimation("player_right");

                if (anim != null) setAnimation(anim);
            }
        } else if (currentDir != 0) {
            currentDir = 0;
            Animation<TextureRegion> anim =
                assets.getAnimation("player_idle");

            if (anim != null) setAnimation(anim);
        }
    }

    public void move(float dx, float dy) {
        float movementSpeed = focused ? speed * 0.5f : speed;

        x += dx * movementSpeed * com.badlogic.gdx.Gdx.graphics.getDeltaTime();
        y += dy * movementSpeed * com.badlogic.gdx.Gdx.graphics.getDeltaTime();

        clampToPlayfield();
    }

    private void clampToPlayfield() {
        float minX = 32f;
        float maxX = 416f - width;
        float minY = 16f;
        float maxY = 560f - height;

        x = Math.max(minX, Math.min(x, maxX));
        y = Math.max(minY, Math.min(y, maxY));
    }

    public Rectangle getCoreHitbox() {
        return new Rectangle(
            x + width / 2f - 4f,
            y + height / 2f - 4f,
            8f,
            8f
        );
    }

    public Rectangle getGrazeHitbox() {
        return new Rectangle(x, y, width, height);
    }

    public Bullet shootBullet() {
        int damage = 10 + power;

        System.out.println(name + " shoots bullet dealing " + damage + " DMG!");

        AudioSystem.getInstance().playSound("shoot");

        return EntityFactory.createPlayerBullet(
            x + width / 2f - 8f,
            y + height,
            damage
        );
    }

    public void shootBullet(BulletManager bulletManager) {
        if (bulletManager == null || shootTimer < shootCooldown) {
            return;
        }

        AudioSystem.getInstance().playSound("shoot");

        if (shootingPattern != null) {
            shootingPattern.execute(
                x + width / 2f - 8f,
                y + height,
                bulletManager,
                true
            );
        } else {
            bulletManager.spawnPlayerBullet(
                x + width / 2f - 8f,
                y + height,
                0f,
                400f,
                10 + power
            );
        }

        shootTimer = 0f;
    }

    public void useBomb(
        BulletManager bulletManager,
        List<GameObject> entities
    ) {
        if (spellCards <= 0) {
            System.out.println("No Spell Cards remaining!");
            return;
        }

        spellCards--;
        invulnerableTimer = 5f;

        if (bulletManager != null) {
            bulletManager.clearEnemyBullets(entities);
        }

        notifyObservers();

        System.out.println(
            name + " unleashes SPELL CARD! Enemy bullets cleared!"
        );
    }

    public void useBomb(BulletManager bulletManager) {
        useBomb(bulletManager, null);
    }

    public void addGraze() {
        grazeCount++;
        addScore(50);
        notifyObservers();
    }

    public void takeDamage(int damage) {
        if (damage <= 0 || isInvulnerable() || isDestroyed()) {
            return;
        }

        hp = Math.max(0, hp - damage);

        System.out.println(
            name + " took " + damage + " damage! HP: " + hp + "/" + maxHp
        );

        notifyObservers();

        if (hp == 0) {
            loseLife(null, null);
        }
    }

    public void loseLife(
        BulletManager bulletManager,
        List<GameObject> entities
    ) {
        lives--;

        if (bulletManager != null) {
            bulletManager.clearEnemyBullets();
            bulletManager.clearPlayerBullets();
        }

        if (lives > 0) {
            respawn();
        } else {
            destroy();
        }

        notifyObservers();
    }

    public void respawn() {
        active = true;
        x = 200f;
        y = 50f;
        hp = maxHp;
        invulnerableTimer = 3f;
        power = Math.max(10, power - 16);
        spellCards = 3;

        notifyObservers();
    }

    public void resetGame() {
        lives = 3;
        hp = maxHp;
        power = 15;
        spellCards = 3;
        score = 0;
        grazeCount = 0;
        active = true;
        x = 200f;
        y = 50f;
        invulnerableTimer = 3f;
        shootTimer = shootCooldown;
        focused = false;

        notifyObservers();
    }

    @Override
    public void onCollision(Collidable other) {
        if (other instanceof Item item && !item.isDestroyed()) {
            collectItem(item);
        }
    }

    public void collectItem(Item item) {
        if (item == null || item.isDestroyed()) {
            return;
        }

        AudioSystem.getInstance().playSound("item_collect");

        ItemType type = item.getItemTypeEnum();

        if (type == null) {
            addScore(item.getScoreValue());
            item.destroy();
            return;
        }

        switch (type) {
            case POWER:
                power += type.getPowerBonus();
                addScore(item.getScoreValue());
                break;

            case POINT:
                addScore(item.getScoreValue());
                break;

            case BOMB:
                spellCards++;
                addScore(item.getScoreValue());
                break;

            case LIFE:
                lives++;
                hp = maxHp;
                addScore(item.getScoreValue());
                break;
        }

        item.destroy();
        notifyObservers();
    }

    @Override
    public void render(SpriteBatch batch) {
        if (batch == null || !active || isDestroyed()) {
            return;
        }

        if (invulnerableTimer > 0f) {
            boolean visible = ((int) (invulnerableTimer * 16)) % 2 == 0;

            if (!visible) {
                return;
            }
        }

        if (focused) {
            batch.setColor(1f, 1f, 1f, 0.5f);
        }

        super.render(batch);
        batch.setColor(Color.WHITE);
    }

    public void renderFocusIndicator(ShapeRenderer shapeRenderer) {
        if (focused && shapeRenderer != null && active) {
            Rectangle core = getCoreHitbox();

            shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);

            shapeRenderer.setColor(Color.RED);
            shapeRenderer.circle(
                core.x + core.width / 2f,
                core.y + core.height / 2f,
                4f
            );

            shapeRenderer.setColor(Color.WHITE);
            shapeRenderer.circle(
                core.x + core.width / 2f,
                core.y + core.height / 2f,
                2f
            );

            shapeRenderer.end();
        }
    }

    public void addObserver(GameObserver observer) {
        if (observer != null && !observers.contains(observer)) {
            observers.add(observer);
            notifyObserver(observer);
        }
    }

    public void removeObserver(GameObserver observer) {
        observers.remove(observer);
    }

    private void notifyObserver(GameObserver observer) {
        observer.onScoreChanged(score);
        observer.onHpChanged(hp);
        observer.onPowerChanged(power);
        observer.onSpellCardsChanged(spellCards);
        observer.onGrazeChanged(grazeCount);
        observer.onLivesChanged(lives);
    }

    private void notifyObservers() {
        for (GameObserver observer : observers) {
            notifyObserver(observer);
        }
    }

    public boolean isAlive() {
        return hp > 0 && lives > 0 && !isDestroyed();
    }

    public boolean isGameOver() {
        return lives <= 0;
    }

    public boolean isInvulnerable() {
        return invulnerableTimer > 0f;
    }

    public boolean isFocused() {
        return focused;
    }

    public void setFocused(boolean focused) {
        this.focused = focused;
    }

    public int getLives() {
        return lives;
    }

    public void setLives(int lives) {
        this.lives = lives;
        notifyObservers();
    }

    public int getGrazeCount() {
        return grazeCount;
    }

    public String getName() {
        return name;
    }

    public int getHp() {
        return hp;
    }

    public int getMaxHp() {
        return maxHp;
    }

    public int getPower() {
        return power;
    }

    public int getSpellCards() {
        return spellCards;
    }

    public long getScore() {
        return score;
    }

    public void addScore(long points) {
        if (points > 0) {
            score += points;
            notifyObservers();
        }
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setHp(int hp) {
        this.hp = Math.max(0, hp);
        notifyObservers();
    }

    public void setPower(int power) {
        this.power = power;
        notifyObservers();
    }

    public void setSpellCards(int spellCards) {
        this.spellCards = spellCards;
        notifyObservers();
    }

    public void setScore(long score) {
        this.score = score;
        notifyObservers();
    }

    public ShootingPattern getShootingPattern() {
        return shootingPattern;
    }

    public void setShootingPattern(ShootingPattern shootingPattern) {
        this.shootingPattern = shootingPattern;
    }
}
