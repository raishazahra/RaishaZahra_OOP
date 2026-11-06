
package com.raishazahra.frontend;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;

import com.raishazahra.frontend.commands.Command;
import com.raishazahra.frontend.commands.InputHandler;

import com.raishazahra.frontend.objects.GameObject;
import com.raishazahra.frontend.objects.Player;
import com.raishazahra.frontend.objects.enemies.Boss;
import com.raishazahra.frontend.objects.enemies.Fairy;
import com.raishazahra.frontend.objects.items.Item;
import com.raishazahra.frontend.objects.items.ItemType;

import com.raishazahra.frontend.observers.GameObserver;
import com.raishazahra.frontend.systems.AssetManager;
import com.raishazahra.frontend.systems.AudioSystem;
import com.raishazahra.frontend.systems.BulletManager;
import com.raishazahra.frontend.systems.CollisionReferee;
import com.raishazahra.frontend.systems.EntityFactory;
import com.raishazahra.frontend.ui.GameHUD;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Main extends ApplicationAdapter {

    private SpriteBatch batch;

    private Player player;
    private Boss boss;

    private List<GameObject> entities;

    private BulletManager bulletManager;
    private CollisionReferee collisionReferee;

    private InputHandler inputHandler;
    private GameHUD gameHUD;

    private float enemyShootTimer = 0f;

    @Override
    public void create() {

        batch = new SpriteBatch();

        entities = new ArrayList<>();

        // Initialize assets
        AssetManager.getInstance().init();

        // Initialize audio
        AudioSystem audio = AudioSystem.getInstance();

        audio.loadSound("shoot", "shoot.wav");
        audio.loadSound("enemy_die", "enemy_die.wav");
        audio.loadSound("item_collect", "item_collect.wav");

        audio.loadBgm("game_bgm.wav");
        audio.playBgm();

        // Initialize BulletManager
        bulletManager = new BulletManager();

        // Initialize CollisionReferee
        collisionReferee = new CollisionReferee();

        // Create Player
        player = EntityFactory.createPlayer(
            280, 40,
            "Reimu Hakurei",
            100,
            15,
            3
        );

        // Create Fairies
        Fairy redFairy = EntityFactory.createFairy(
            150, 380,
            "Red Fairy",
            20
        );

        Fairy blueFairy = EntityFactory.createFairy(
            250, 380,
            "Blue Fairy",
            20,
            "fairy_idle_blue"
        );

        // Create Boss
        boss = EntityFactory.createBoss(
            380, 400,
            "Rumia",
            150
        );

        // Create Items
        Item powerItem = EntityFactory.createItem(
            200, 450,
            ItemType.POWER
        );

        Item pointItem = EntityFactory.createItem(
            320, 480,
            ItemType.POINT
        );

        // Add entities
        entities.add(player);
        entities.add(redFairy);
        entities.add(blueFairy);
        entities.add(boss);
        entities.add(powerItem);
        entities.add(pointItem);

        // Initialize Command Pattern
        inputHandler = new InputHandler(entities);

        // Initialize Observer Pattern
        gameHUD = new GameHUD();
        player.addObserver(gameHUD);
    }

    // Boss shooting scheduler
    private void updateEnemyScheduler(float delta) {

        enemyShootTimer += delta;

        if (enemyShootTimer >= 1.5f) {

            if (boss != null && !boss.isDestroyed() && boss.isAlive()) {
                boss.shootBullet(bulletManager);
            }

            enemyShootTimer = 0f;
        }
    }

    // Update and remove destroyed entities safely
    private void updateAndClean(
        List<GameObject> entityList,
        float delta,
        float screenWidth,
        float screenHeight
    ) {

        Iterator<GameObject> iterator = entityList.iterator();

        while (iterator.hasNext()) {

            GameObject entity = iterator.next();

            if (entity.isDestroyed()) {
                iterator.remove();
                continue;
            }

            entity.update(delta);

            // Remove objects outside screen
            if (entity.getX() + entity.getWidth() < 0 ||
                entity.getX() > screenWidth ||
                entity.getY() + entity.getHeight() < 0 ||
                entity.getY() > screenHeight) {

                if (entity != player && entity != boss) {
                    entity.destroy();
                    iterator.remove();
                }
            }
        }
    }

    @Override
    public void render() {

        float delta = Gdx.graphics.getDeltaTime();

        // 1. Command Pattern: Handle player input
        if (inputHandler != null) {

            for (Command command : inputHandler.handleInput(delta)) {
                command.execute(player, bulletManager);
            }
        }

        // 2. Boss shooting scheduler
        updateEnemyScheduler(delta);

        // 3. Update standard entities
        updateAndClean(
            entities,
            delta,
            Gdx.graphics.getWidth(),
            Gdx.graphics.getHeight()
        );

        // 4. Update BulletManager
        bulletManager.update(
            delta,
            Gdx.graphics.getWidth(),
            Gdx.graphics.getHeight()
        );

        // 5. Mediator Pattern: Resolve collisions
        collisionReferee.resolveCollisions(
            player,
            entities,
            bulletManager
        );

        // 6. Clear screen
        ScreenUtils.clear(0.1f, 0.1f, 0.15f, 1f);

        // 7. Render entities and bullets
        batch.begin();

        for (GameObject entity : entities) {

            if (!entity.isDestroyed()) {
                entity.render(batch);
            }
        }

        bulletManager.render(batch);

        // 8. Observer Pattern: Render HUD
        gameHUD.render(batch);

        batch.end();
    }

    @Override
    public void dispose() {

        if (batch != null) {
            batch.dispose();
        }

        if (gameHUD != null) {
            gameHUD.dispose();
        }

        AudioSystem.getInstance().dispose();
        AssetManager.getInstance().dispose();
    }
}
