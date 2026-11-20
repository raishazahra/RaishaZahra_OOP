package com.raishazahra.frontend.states;

import com.raishazahra.frontend.objects.GameObject;
import com.raishazahra.frontend.objects.enemies.Boss;
import com.raishazahra.frontend.systems.LevelWaveManager;

import java.util.List;
import java.util.Random;
import java.util.function.Supplier;

public abstract class SpellCardState implements WaveState {

    private static final float INTRO_SECONDS = 2f;
    private static final float TEST_TIMEOUT_SECONDS = 4f;

    private final Random random = new Random();

    private List<Supplier<Attack>> blocks;
    private Attack currentBlock;
    private int blockIndex = -1;
    private float phaseTimer = 0f;

    protected abstract String getSpellName();

    protected abstract int getStartingLife();

    protected abstract float getTimeoutSeconds();

    protected abstract List<Supplier<Attack>> createAttackBlocks(
        GameObject player
    );

    protected abstract WaveState createNextState();

    protected float getGlideDistance() {
        return 90f;
    }

    protected float getGlideSeconds() {
        return 1.5f;
    }

    protected boolean isFinalPhase() {
        return false;
    }

    protected void onSpellStart(Boss boss) {
    }

    protected void onSpellEnd(
        LevelWaveManager manager,
        Boss boss
    ) {
    }

    @Override
    public void onEnter(LevelWaveManager manager) {

        Boss boss = manager.getBoss();

        if (boss == null || boss.isDestroyed()) {
            boss = manager.spawnBoss(
                200f,
                420f,
                "Rumia",
                getStartingLife()
            );
        }

        boss.setHp(getStartingLife());
        boss.setSurviveAtZero(!isFinalPhase());

        boss.setCanTakeDamage(false);
        boss.setCollidable(true);

        boss.setMovementPattern(
            BossGlide.home(boss, INTRO_SECONDS)
        );

        onSpellStart(boss);

        blocks = createAttackBlocks(manager.getPlayer());

        currentBlock = null;
        blockIndex = -1;
        phaseTimer = 0f;

        System.out.println(
            "[" + getClass().getSimpleName() + "] "
                + getSpellName()
        );
    }

    @Override
    public void update(
        LevelWaveManager manager,
        float delta
    ) {

        phaseTimer += delta;

        Boss boss = manager.getBoss();

        if (boss != null
            && !boss.isDestroyed()
            && phaseTimer >= INTRO_SECONDS) {

            if (currentBlock == null) {
                boss.setCanTakeDamage(true);
            }

            if (currentBlock == null
                || currentBlock.isFinished()) {
                startNextBlock(boss);
            }

            currentBlock.update(
                boss,
                manager.getBulletManager(),
                delta
            );
        }

        float timeout = manager.getPlayer() == null
            ? TEST_TIMEOUT_SECONDS
            : getTimeoutSeconds();

        boolean bossDefeated =
            boss != null && boss.getHp() <= 0;

        if (bossDefeated || phaseTimer >= timeout) {

            if (manager.getBulletManager() != null) {
                manager.getBulletManager().clearEnemyBullets();
            }

            onSpellEnd(manager, boss);

            manager.setState(createNextState());
        }
    }

    private void startNextBlock(Boss boss) {

        boolean firstBlock = blockIndex < 0;

        blockIndex = (blockIndex + 1) % blocks.size();

        currentBlock = blocks.get(blockIndex).get();

        if (!firstBlock) {
            boss.setMovementPattern(
                BossGlide.random(
                    boss,
                    getGlideDistance(),
                    getGlideSeconds(),
                    random
                )
            );
        }

        System.out.println(
            "[" + getClass().getSimpleName()
                + "] Rumia block: "
                + currentBlock.getName()
        );
    }
}
