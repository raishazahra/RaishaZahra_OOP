
package com.raishazahra.frontend.objects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import com.raishazahra.frontend.objects.bullets.Bullet;
import com.raishazahra.frontend.objects.enemies.Enemy;
import com.raishazahra.frontend.objects.items.Item;
import com.raishazahra.frontend.objects.items.ItemType;
import com.raishazahra.frontend.systems.AssetManager;
import com.raishazahra.frontend.systems.AudioSystem;
import com.raishazahra.frontend.systems.EntityFactory;

public class Player extends GameObject {

    private String name;
    private int hp;
    private int maxHp;
    private int power;
    private int spellCards;
    private long score;
    private int currentDir = 0;

    public Player(String name, int hp, int power, int spellCards) {
        this(280, 40, name, hp, power, spellCards);
    }

    public Player(float x, float y) {
        this(x, y, "Reimu Hakurei", 100, 15, 3);
    }

    public Player(
        float x, float y, String name,
        int hp, int power, int spellCards
    ) {
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

        float dx = 0;

        if (Gdx.input != null) {
            if (Gdx.input.isKeyPressed(Input.Keys.W)
                || Gdx.input.isKeyPressed(Input.Keys.UP)) {
                y += speed * delta;
            }

            if (Gdx.input.isKeyPressed(Input.Keys.S)
                || Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
                y -= speed * delta;
            }

            if (Gdx.input.isKeyPressed(Input.Keys.A)
                || Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
                x -= speed * delta;
                dx = -1;
            }

            if (Gdx.input.isKeyPressed(Input.Keys.D)
                || Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
                x += speed * delta;
                dx = 1;
            }
        }

        updateAnimationState(dx);
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

    public Bullet shootBullet() {
        int damage = 10 + power;

        System.out.println(
            name + " shoots bullet dealing " + damage + " DMG!"
        );

        // Play the firing sound.
        AudioSystem.getInstance().playSound("shoot");

        // Create a bullet with its own position and state.
        return EntityFactory.createPlayerBullet(
            x + width / 2f - 8,
            y + height,
            damage
        );
    }

    @Override
    public void onCollision(Collidable other) {
        if (other instanceof Item item && !item.isDestroyed()) {
            collectItem(item);
            item.destroy();
        }
    }

    public void collectItem(Item item) {
        AudioSystem.getInstance().playSound("item_collect");

        ItemType type = item.getItemTypeEnum();

        if (type == null) {
            addScore(item.getScoreValue());
            return;
        }

        switch (type) {
            case POWER:
                power += type.getPowerBonus();
                addScore(item.getScoreValue());
                System.out.println("POWER collected. Power: " + power);
                break;

            case POINT:
                addScore(item.getScoreValue());
                System.out.println("POINT collected.");
                break;

            case BOMB:
                spellCards++;
                addScore(item.getScoreValue());
                System.out.println("BOMB collected. SpellCards: " + spellCards);
                break;

            case LIFE:
                hp = Math.min(maxHp, hp + 20);
                addScore(item.getScoreValue());
                System.out.println("LIFE collected. HP: " + hp);
                break;
        }
    }

    public void takeDamage(int damage) {
        if (damage > 0) {
            hp = Math.max(0, hp - damage);
            System.out.println(
                name + " took " + damage + " damage! HP: " + hp + "/" + maxHp
            );
        }
    }

    public boolean isAlive() {
        return hp > 0;
    }

    public void shoot(Enemy target) {
        if (target != null && isAlive()) {
            target.takeDamage(10 + power);
        }
    }

    public String getName() { return name; }
    public int getHp() { return hp; }
    public int getMaxHp() { return maxHp; }
    public int getPower() { return power; }
    public int getSpellCards() { return spellCards; }
    public long getScore() { return score; }

    public void addScore(long points) {
        if (points > 0) score += points;
    }

    public void setName(String name) { this.name = name; }
    public void setHp(int hp) { this.hp = Math.max(0, hp); }
    public void setPower(int power) { this.power = power; }

    public void setSpellCards(int spellCards) {
        this.spellCards = spellCards;
    }

    public void setScore(long score) {
        this.score = score;
    }
}
