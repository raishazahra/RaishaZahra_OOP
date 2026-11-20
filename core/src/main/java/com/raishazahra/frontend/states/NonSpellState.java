package com.raishazahra.frontend.states;
import com.raishazahra.frontend.objects.GameObject;
import com.raishazahra.frontend.objects.enemies.Boss;
import com.raishazahra.frontend.objects.patterns.entityStrategy.EntityMovementPattern;
import com.raishazahra.frontend.objects.patterns.entityStrategy.FixedMovement;
import com.raishazahra.frontend.systems.LevelWaveManager;
import java.util.List;
import java.util.Random;
import java.util.function.Supplier;
public abstract class NonSpellState implements WaveState {
    private static final float TEST_TIMEOUT_SECONDS = 4f;
    private static final float GLIDE_DISTANCE = 90f;
    private static final float GLIDE_SECONDS = 1f;
    private final Random random = new Random();
    private List<Supplier<Attack>> attacks;
    private Attack currentAttack;
    private int currentIndex;
    private float phaseTimer = 0f;
    protected abstract String getPhaseName();
    protected abstract int getPhaseLife();
    protected abstract int getLifeThreshold();
    protected abstract List<Supplier<Attack>> createAttacks(
        GameObject player
    );
    protected abstract float getTimeoutSeconds();
    protected abstract WaveState createNextState();
    protected float getIntroSeconds() {
        return 0f;
    }
    protected boolean isCollidableDuringIntro() {
        return true;
    }
    protected EntityMovementPattern createIntroMovement(Boss boss) {
        return new FixedMovement();
    }
    @Override
    public void onEnter(LevelWaveManager manager) {
        Boss boss = manager.getBoss();
        if (boss == null || boss.isDestroyed()) {
            boss = manager.spawnBoss(
                320f,
                464f,
                "Rumia",
                getPhaseLife()
            );
        }
        boss.setLife(getPhaseLife());
        boss.setSurviveAtZero(true);
        if (getIntroSeconds() > 0f) {
            boss.setCanTakeDamage(false);
            boss.setCollidable(isCollidableDuringIntro());
            boss.setMovementPattern(createIntroMovement(boss));
        } else {
            boss.setCanTakeDamage(true);
            boss.setCollidable(true);
            boss.setMovementPattern(
                BossGlide.random(
                    boss,
                    GLIDE_DISTANCE,
                    GLIDE_SECONDS,
                    random
                )
            );
        }
        attacks = createAttacks(manager.getPlayer());
        currentAttack = null;
        phaseTimer = 0f;
        System.out.println(
            "[" + getClass().getSimpleName() + "] "
                + getPhaseName()
        );
    }
